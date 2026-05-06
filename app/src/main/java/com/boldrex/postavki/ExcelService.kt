package com.boldrex.postavki

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ExcelService {
    data class ImportResult(val rowsTotal: Int, val rowsImported: Int, val errorsCount: Int)

    suspend fun importProducts(context: Context, uri: Uri, repo: ShipmentRepository): ImportResult {
        val name = displayName(context, uri).lowercase(Locale.getDefault())
        val input = context.contentResolver.openInputStream(uri) ?: return ImportResult(0, 0, 1)
        val bytes = input.readBytes()
        val rows = when {
            name.endsWith(".xlsx") -> parseXlsx(bytes.inputStream())
            name.endsWith(".xml") -> parseXml(decodeText(bytes))
            else -> parseCsv(decodeText(bytes))
        }
        var ok = 0
        var errors = 0
        rows.forEach { row ->
            try {
                val article = valueByAliases(row, ARTICLE_ALIASES).orEmpty()
                val title = valueByAliases(row, NAME_ALIASES).orEmpty()
                val barcode = valueByAliases(row, BARCODE_ALIASES)
                if (title.isBlank() && article.isBlank() && barcode.isNullOrBlank()) return@forEach
                repo.createOrUpdateProduct(article, title.ifBlank { article.ifBlank { barcode.orEmpty() } }, barcode, createdFromScan = false)
                ok++
            } catch (_: Throwable) {
                errors++
            }
        }
        repo.logImport(displayName(context, uri), name.substringAfterLast('.', "csv"), rows.size, ok, errors)
        return ImportResult(rows.size, ok, errors)
    }

    fun generateXlsx(context: Context, info: ShipmentInfo, rows: List<ReportRow>, boxes: List<ReportBox>): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        if (!dir.exists()) dir.mkdirs()
        val safeTitle = info.title.safeFilePart()
        val safeMarket = info.marketplace.safeFilePart()
        val file = File(dir, "${safeTitle}_${info.date}_${safeMarket}.xlsx")

        val cityRows = rows.groupBy { it.city }.map { (city, list) ->
            val boxCount = boxes.count { it.city == city }
            listOf(city, boxCount, list.size, list.sumOf { it.quantity })
        }
        val productRows = rows.groupBy { Triple(it.article, it.productName, it.barcode.orEmpty()) }.map { (key, list) ->
            listOf(key.first, key.second, key.third, list.sumOf { it.quantity }, list.map { it.boxNumber }.distinct().size, list.map { it.city }.distinct().joinToString(", "))
        }.sortedBy { it[0].toString() }
        val newProducts = rows.filter { it.isCreatedFromScan }.distinctBy { it.barcode ?: it.article }.map {
            listOf(formatTime(it.productCreatedAt), it.article, it.productName, it.barcode.orEmpty(), "скан")
        }

        val sheets = listOf(
            Sheet("01_Сводка", listOf(
                listOf("Показатель", "Значение"),
                listOf("Название поставки", info.title),
                listOf("Дата", info.date),
                listOf("Маркетплейс", info.marketplace),
                listOf("Количество городов", rows.map { it.city }.distinct().size),
                listOf("Количество коробок", boxes.size),
                listOf("SKU/артикулов", rows.map { it.article }.distinct().size),
                listOf("Единиц товара", rows.sumOf { it.quantity }),
                listOf("Дата формирования отчёта", formatTime(System.currentTimeMillis()))
            ), autoFilter = false),
            Sheet("02_Города", listOf(listOf("Город", "Количество коробок", "Товарных позиций", "Единиц товара")) + cityRows, autoFilter = false),
            Sheet("03_Коробки", listOf(listOf("Город", "Номер коробки", "Маркетплейс", "Количество позиций", "Количество единиц", "Комментарий")) +
                boxes.map { listOf(it.city, it.boxNumber, it.marketplace, it.positionCount, it.itemCount, it.comment.orEmpty()) }, autoFilter = true),
            Sheet("04_Состав_коробок", listOf(listOf("Маркетплейс", "Поставка", "Дата", "Город", "Номер коробки", "Артикул", "Название товара", "Штрихкод", "Количество")) +
                rows.map { listOf(it.marketplace, it.shipment, it.shipmentDate, it.city, it.boxNumber, it.article, it.productName, it.barcode.orEmpty(), it.quantity) }, autoFilter = true),
            Sheet("05_Товары", listOf(listOf("Артикул", "Название", "Штрихкод", "Общее количество", "Количество коробок", "Города")) + productRows, autoFilter = true),
            Sheet("06_Новые_товары", listOf(listOf("Дата добавления", "Артикул", "Название", "Штрихкод", "Источник")) + newProducts, autoFilter = false)
        )

        ZipOutputStream(file.outputStream()).use { zip ->
            zip.putText("[Content_Types].xml", contentTypes(sheets.size))
            zip.putText("_rels/.rels", rootRels())
            zip.putText("xl/workbook.xml", workbookXml(sheets))
            zip.putText("xl/_rels/workbook.xml.rels", workbookRels(sheets.size))
            zip.putText("xl/styles.xml", stylesXml())
            sheets.forEachIndexed { index, sheet ->
                zip.putText("xl/worksheets/sheet${index + 1}.xml", sheetXml(sheet))
            }
        }
        return file
    }

    fun generateCsv(context: Context, info: ShipmentInfo, rows: List<ReportRow>): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "${info.title.safeFilePart()}_${info.date}_${info.marketplace.safeFilePart()}.csv")
        val sb = StringBuilder("\uFEFF")
        sb.appendLine("Маркетплейс;Поставка;Дата;Город;Номер коробки;Артикул;Название товара;Штрихкод;Количество")
        rows.forEach {
            sb.appendLine(listOf(it.marketplace, it.shipment, it.shipmentDate, it.city, it.boxNumber, it.article, it.productName, it.barcode.orEmpty(), it.quantity).joinToString(";") { v -> csv(v.toString()) })
        }
        file.writeText(sb.toString(), Charsets.UTF_8)
        return file
    }

    fun shareFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        val mime = if (file.extension.equals("xlsx", true)) "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" else "text/csv"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Поделиться отчётом").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private data class Sheet(val name: String, val rows: List<List<Any?>>, val autoFilter: Boolean)

    private fun sheetXml(sheet: Sheet): String {
        val maxCol = sheet.rows.maxOfOrNull { it.size } ?: 1
        val endRef = colName(maxCol) + (sheet.rows.size.coerceAtLeast(1))
        val rowsXml = sheet.rows.mapIndexed { rIndex, row ->
            val rowNum = rIndex + 1
            val cells = row.mapIndexed { cIndex, value ->
                val ref = colName(cIndex + 1) + rowNum
                cellXml(ref, value, header = rIndex == 0)
            }.joinToString("")
            "<row r=\"$rowNum\">$cells</row>"
        }.joinToString("")
        val filter = if (sheet.autoFilter && sheet.rows.size > 1) "<autoFilter ref=\"A1:$endRef\"/>" else ""
        val cols = (1..maxCol).joinToString("") { "<col min=\"$it\" max=\"$it\" width=\"22\" customWidth=\"1\"/>" }
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
              <dimension ref="A1:$endRef"/>
              <sheetViews><sheetView workbookViewId="0"><pane ySplit="1" topLeftCell="A2" activePane="bottomLeft" state="frozen"/></sheetView></sheetViews>
              <cols>$cols</cols>
              <sheetData>$rowsXml</sheetData>
              $filter
            </worksheet>
        """.trimIndent()
    }

    private fun cellXml(ref: String, value: Any?, header: Boolean): String {
        val style = if (header) " s=\"1\"" else ""
        return when (value) {
            is Number -> "<c r=\"$ref\"$style><v>${value}</v></c>"
            else -> "<c r=\"$ref\" t=\"inlineStr\"$style><is><t>${xml(value?.toString().orEmpty())}</t></is></c>"
        }
    }

    private fun contentTypes(sheetCount: Int): String {
        val sheetOverrides = (1..sheetCount).joinToString("") {
            "<Override PartName=\"/xl/worksheets/sheet$it.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
        }
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
              <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
              <Default Extension="xml" ContentType="application/xml"/>
              <Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>
              <Override PartName="/xl/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"/>
              $sheetOverrides
            </Types>
        """.trimIndent()
    }

    private fun rootRels() = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
          <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="xl/workbook.xml"/>
        </Relationships>
    """.trimIndent()

    private fun workbookXml(sheets: List<Sheet>): String {
        val xmlSheets = sheets.mapIndexed { i, sheet ->
            "<sheet name=\"${xml(sheet.name.take(31))}\" sheetId=\"${i + 1}\" r:id=\"rId${i + 1}\"/>"
        }.joinToString("")
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">
              <sheets>$xmlSheets</sheets>
            </workbook>
        """.trimIndent()
    }

    private fun workbookRels(sheetCount: Int): String {
        val rels = (1..sheetCount).joinToString("") {
            "<Relationship Id=\"rId$it\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet$it.xml\"/>"
        } + "<Relationship Id=\"rId${sheetCount + 1}\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
        return """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">$rels</Relationships>
        """.trimIndent()
    }

    private fun stylesXml() = """
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <styleSheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">
          <fonts count="2"><font><sz val="11"/><name val="Calibri"/></font><font><b/><sz val="11"/><color rgb="FFFFFFFF"/><name val="Calibri"/></font></fonts>
          <fills count="3"><fill><patternFill patternType="none"/></fill><fill><patternFill patternType="gray125"/></fill><fill><patternFill patternType="solid"><fgColor rgb="FF4472C4"/><bgColor indexed="64"/></patternFill></fill></fills>
          <borders count="1"><border><left/><right/><top/><bottom/><diagonal/></border></borders>
          <cellStyleXfs count="1"><xf numFmtId="0" fontId="0" fillId="0" borderId="0"/></cellStyleXfs>
          <cellXfs count="2"><xf numFmtId="0" fontId="0" fillId="0" borderId="0" xfId="0"/><xf numFmtId="0" fontId="1" fillId="2" borderId="0" xfId="0" applyFont="1" applyFill="1"/></cellXfs>
        </styleSheet>
    """.trimIndent()

    private fun ZipOutputStream.putText(path: String, text: String) {
        putNextEntry(ZipEntry(path))
        write(text.toByteArray(Charsets.UTF_8))
        closeEntry()
    }

    private fun parseCsv(text: String): List<Map<String, String>> {
        val clean = text.removePrefix("\uFEFF")
        val lines = clean.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return emptyList()
        val delimiter = detectDelimiter(lines.first())
        val first = splitCsvLine(lines.first(), delimiter).map { it.trim().lowercase(Locale.getDefault()) }
        val hasHeader = first.any { normalizeKey(it) in KNOWN_IMPORT_KEYS }
        val headers = if (hasHeader) first else listOf("article", "name", "barcode")
        return lines.drop(if (hasHeader) 1 else 0).mapNotNull { line ->
            val values = splitCsvLine(line, delimiter)
            if (values.all { it.isBlank() }) null else headers.mapIndexedNotNull { index, key -> key to values.getOrElse(index) { "" }.trim() }.toMap()
        }
    }

    private fun detectDelimiter(header: String): Char {
        val candidates = listOf(';', ',', '\t')
        return candidates.maxByOrNull { d -> header.count { it == d } } ?: ';'
    }

    private fun decodeText(bytes: ByteArray): String {
        if (bytes.size >= 2) {
            if (bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte()) return bytes.toString(Charsets.UTF_16LE)
            if (bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte()) return bytes.toString(Charsets.UTF_16BE)
        }
        val utf8 = bytes.toString(Charsets.UTF_8)
        if (!utf8.contains('\uFFFD')) return utf8
        return bytes.toString(Charset.forName("windows-1251"))
    }

    private fun splitCsvLine(line: String, delimiter: Char): List<String> {
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var quoted = false
        line.forEach { ch ->
            when {
                ch == '"' -> quoted = !quoted
                ch == delimiter && !quoted -> { result += sb.toString(); sb.clear() }
                else -> sb.append(ch)
            }
        }
        result += sb.toString()
        return result
    }

    private fun parseXml(text: String): List<Map<String, String>> {
        val itemRegex = Regex("<product([^>]*)/?>|<товар([^>]*)/?>", RegexOption.IGNORE_CASE)
        val attrRegex = Regex("(article|артикул|sku|name|название|barcode|штрихкод|код)=['\"]([^'\"]*)['\"]", RegexOption.IGNORE_CASE)
        return itemRegex.findAll(text).map { match ->
            val attrs = match.groupValues.drop(1).joinToString(" ")
            attrRegex.findAll(attrs).associate { it.groupValues[1].lowercase(Locale.getDefault()) to it.groupValues[2] }
        }.toList()
    }

    private fun parseXlsx(input: InputStream): List<Map<String, String>> {
        val entries = mutableMapOf<String, String>()
        ZipInputStream(input).use { zip ->
            while (true) {
                val entry = zip.nextEntry ?: break
                if (!entry.isDirectory && (entry.name == "xl/sharedStrings.xml" || entry.name == "xl/worksheets/sheet1.xml")) {
                    entries[entry.name] = zip.readBytes().toString(Charsets.UTF_8)
                }
                zip.closeEntry()
            }
        }
        val shared = parseSharedStrings(entries["xl/sharedStrings.xml"].orEmpty())
        val sheet = entries["xl/worksheets/sheet1.xml"].orEmpty()
        val rows = Regex("<row[^>]*>(.*?)</row>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).findAll(sheet).map { rowMatch ->
            Regex("<c([^>]*)>(.*?)</c>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).findAll(rowMatch.groupValues[1]).map { c ->
                val attrs = c.groupValues[1]
                val body = c.groupValues[2]
                val isShared = attrs.contains("t=\"s\"") || attrs.contains("t='s'")
                val inline = Regex("<t[^>]*>(.*?)</t>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).find(body)?.groupValues?.get(1)
                val value = Regex("<v[^>]*>(.*?)</v>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).find(body)?.groupValues?.get(1)
                when {
                    inline != null -> html(inline)
                    isShared && value != null -> shared.getOrElse(value.toIntOrNull() ?: -1) { "" }
                    else -> html(value.orEmpty())
                }
            }.toList()
        }.toList()
        if (rows.isEmpty()) return emptyList()
        val header = rows.first().map { it.trim().lowercase(Locale.getDefault()) }
        val headers = if (header.any { normalizeKey(it) in KNOWN_IMPORT_KEYS }) header else listOf("article", "name", "barcode")
        return rows.drop(if (headers == header) 1 else 0).map { row -> headers.mapIndexed { i, h -> h to row.getOrElse(i) { "" }.trim() }.toMap() }
    }

    private fun valueByAliases(row: Map<String, String>, aliases: Set<String>): String? {
        val normalized = row.entries.associate { normalizeKey(it.key) to it.value }
        return aliases.firstNotNullOfOrNull { normalized[it] }?.trim()?.takeIf { it.isNotBlank() }
    }

    private fun normalizeKey(key: String): String = key
        .trim()
        .lowercase(Locale.getDefault())
        .replace("\uFEFF", "")
        .replace("_", "")
        .replace("-", "")
        .replace(" ", "")

    private val ARTICLE_ALIASES = setOf("article", "артикул", "sku", "vendorcode")
    private val NAME_ALIASES = setOf("name", "название", "наименование", "товар", "номенклатура", "productname")
    private val BARCODE_ALIASES = setOf("barcode", "штрихкод", "код", "ean", "ean13")
    private val KNOWN_IMPORT_KEYS = ARTICLE_ALIASES + NAME_ALIASES + BARCODE_ALIASES

    private fun parseSharedStrings(xml: String): List<String> = Regex("<si[^>]*>(.*?)</si>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE))
        .findAll(xml).map { si ->
            Regex("<t[^>]*>(.*?)</t>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).findAll(si.groupValues[1]).joinToString("") { html(it.groupValues[1]) }
        }.toList()

    private fun displayName(context: Context, uri: Uri): String {
        context.contentResolver.query(uri, null, null, null, null)?.use { c ->
            val index = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0 && c.moveToFirst()) return c.getString(index)
        }
        return uri.lastPathSegment ?: "products.csv"
    }

    private fun formatTime(millis: Long): String = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        .withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(millis))

    private fun colName(index: Int): String {
        var n = index
        val sb = StringBuilder()
        while (n > 0) {
            val rem = (n - 1) % 26
            sb.append(('A'.code + rem).toChar())
            n = (n - 1) / 26
        }
        return sb.reverse().toString()
    }

    private fun String.safeFilePart(): String = replace(Regex("[^А-Яа-яA-Za-z0-9._-]+"), "_").trim('_').ifBlank { "Postavka" }
    private fun xml(s: String): String = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
    private fun html(s: String): String = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")
    private fun csv(s: String): String = if (s.any { it == ';' || it == '"' || it == '\n' }) "\"${s.replace("\"", "\"\"")}\"" else s
}
