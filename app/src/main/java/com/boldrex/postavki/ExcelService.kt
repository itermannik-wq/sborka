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

    suspend fun previewProductImport(context: Context, uri: Uri, repo: ShipmentRepository): ProductImportPreview {
        val parsed = parseImportFile(context, uri)
        val firstBarcodeRows = mutableSetOf<String>()
        val rows = parsed.rows.map { row ->
            val article = valueByAliases(row.values, ARTICLE_ALIASES).orEmpty().trim()
            val name = valueByAliases(row.values, NAME_ALIASES).orEmpty().trim()
            val barcode = valueByAliases(row.values, BARCODE_ALIASES)?.trim()?.takeIf { it.isNotBlank() }
            val errors = buildList {
                if (name.isBlank()) add("Не заполнено название товара")
            }
            val duplicateInFile = barcode != null && !firstBarcodeRows.add(barcode)
            val willUpdate = errors.isEmpty() && !duplicateInFile && (barcode?.let { repo.findProductByBarcode(it) != null } == true)
            ImportRowPreview(
                rowNumber = row.rowNumber,
                article = article,
                name = name,
                barcode = barcode,
                errors = errors,
                duplicateInFile = duplicateInFile,
                willUpdate = willUpdate
            )
        }
        val importable = rows.filter { it.canImport }
        return ProductImportPreview(
            fileName = parsed.fileName,
            fileType = parsed.fileType,
            rowsTotal = rows.size,
            rowsForImport = importable.size,
            errorRows = rows.count { it.errors.isNotEmpty() },
            duplicateBarcodeRows = rows.count { it.duplicateInFile },
            updateRows = importable.count { it.willUpdate },
            addRows = importable.count { !it.willUpdate },
            columns = importColumnPreview(parsed),
            rows = rows
        )
    }

    suspend fun commitProductImport(preview: ProductImportPreview, repo: ShipmentRepository): ProductImportResult {
        var added = 0
        var updated = 0
        var runtimeErrors = 0
        var skipped = 0

        preview.rows.forEach { row ->
            if (!row.canImport) {
                skipped++
                return@forEach
            }
            try {
                val existed = row.barcode?.let { repo.findProductByBarcode(it) != null } == true
                repo.createOrUpdateProduct(row.article, row.name, row.barcode, createdFromScan = false)
                if (existed) updated++ else added++
            } catch (_: Throwable) {
                runtimeErrors++
                skipped++
            }
        }

        repo.logImport(preview.fileName, preview.fileType, preview.rowsTotal, added + updated, skipped)
        return ProductImportResult(
            fileName = preview.fileName,
            rowsTotal = preview.rowsTotal,
            added = added,
            updated = updated,
            skipped = skipped,
            errors = preview.errorRows + runtimeErrors,
            duplicateBarcodes = preview.duplicateBarcodeRows
        )
    }

    suspend fun importProducts(context: Context, uri: Uri, repo: ShipmentRepository): ImportResult {
        val preview = previewProductImport(context, uri, repo)
        val result = commitProductImport(preview, repo)
        return ImportResult(result.rowsTotal, result.added + result.updated, result.errors + result.duplicateBarcodes)
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
        val sheets = listOf(
            Sheet("02_Города", listOf(listOf("Город", "Количество коробок", "Товарных позиций", "Единиц товара")) + cityRows, autoFilter = false),
            Sheet("04_Состав_коробок", listOf(listOf("Город", "Номер коробки", "Название товара", "Количество", "Артикул")) +
                rows.map { listOf(it.city, it.boxNumber, it.productName, it.quantity, it.article) }, autoFilter = true)
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

    private data class ParsedImportFile(
        val fileName: String,
        val fileType: String,
        val hasHeader: Boolean,
        val headers: List<String>,
        val rows: List<ParsedImportRow>
    )

    private data class ParsedImportRow(
        val rowNumber: Int,
        val values: Map<String, String>
    )

    private fun parseImportFile(context: Context, uri: Uri): ParsedImportFile {
        val fileName = displayName(context, uri)
        val lowerName = fileName.lowercase(Locale.getDefault())
        val fileType = lowerName.substringAfterLast('.', "csv").ifBlank { "csv" }
        val input = context.contentResolver.openInputStream(uri) ?: error("Не удалось открыть файл")
        val bytes = input.use { it.readBytes() }
        return when {
            lowerName.endsWith(".xlsx") -> parseXlsx(fileName, fileType, bytes.inputStream())
            lowerName.endsWith(".xml") -> parseXml(fileName, fileType, decodeText(bytes))
            else -> parseCsv(fileName, fileType, decodeText(bytes))
        }
    }

    private fun importColumnPreview(parsed: ParsedImportFile): List<ImportColumnPreview> {
        fun sourceByAliases(aliases: Set<String>, noHeaderSource: String): ImportColumnPreview {
            val header = parsed.headers.firstOrNull { normalizeKey(it) in aliases }
            return when {
                header != null -> ImportColumnPreview(roleName(aliases), header.ifBlank { noHeaderSource }, true)
                !parsed.hasHeader -> ImportColumnPreview(roleName(aliases), noHeaderSource, true)
                else -> ImportColumnPreview(roleName(aliases), "Не найдена", false)
            }
        }
        return listOf(
            sourceByAliases(ARTICLE_ALIASES, "1-я колонка"),
            sourceByAliases(NAME_ALIASES, "2-я колонка"),
            sourceByAliases(BARCODE_ALIASES, "3-я колонка")
        )
    }

    private fun roleName(aliases: Set<String>): String = when (aliases) {
        ARTICLE_ALIASES -> "Артикул"
        NAME_ALIASES -> "Название"
        else -> "Штрихкод"
    }

    private fun parseCsv(fileName: String, fileType: String, text: String): ParsedImportFile {
        val clean = text.removePrefix("\uFEFF")
        val lines = clean.lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return ParsedImportFile(fileName, fileType, hasHeader = true, headers = emptyList(), rows = emptyList())
        val delimiter = detectDelimiter(lines.first())
        val first = splitCsvLine(lines.first(), delimiter).map { it.trim() }
        val hasHeader = first.any { normalizeKey(it) in KNOWN_IMPORT_KEYS }
        val headers = if (hasHeader) first else listOf("article", "name", "barcode")
        val dataLines = lines.drop(if (hasHeader) 1 else 0)
        val rows = dataLines.mapIndexedNotNull { index, line ->
            val values = splitCsvLine(line, delimiter)
            if (values.all { it.isBlank() }) return@mapIndexedNotNull null
            val rowNumber = index + if (hasHeader) 2 else 1
            ParsedImportRow(
                rowNumber = rowNumber,
                values = headers.mapIndexedNotNull { columnIndex, key ->
                    key to values.getOrElse(columnIndex) { "" }.trim()
                }.toMap()
            )
        }
        return ParsedImportFile(fileName, fileType, hasHeader, headers, rows)
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
        var i = 0
        while (i < line.length) {
            val ch = line[i]
            when {
                ch == '"' && quoted && i + 1 < line.length && line[i + 1] == '"' -> {
                    sb.append('"')
                    i++
                }
                ch == '"' -> quoted = !quoted
                ch == delimiter && !quoted -> { result += sb.toString(); sb.clear() }
                else -> sb.append(ch)
            }
            i++
        }
        result += sb.toString()
        return result
    }

    private fun parseXml(fileName: String, fileType: String, text: String): ParsedImportFile {
        val itemRegex = Regex("<product([^>]*)/?>|<товар([^>]*)/?>", RegexOption.IGNORE_CASE)
        val attrRegex = Regex("(article|артикул|sku|name|название|barcode|штрихкод|код)=['\"]([^'\"]*)['\"]", RegexOption.IGNORE_CASE)
        val rows = itemRegex.findAll(text).mapIndexed { index, match ->
            val attrs = match.groupValues.drop(1).joinToString(" ")
            ParsedImportRow(
                rowNumber = index + 1,
                values = attrRegex.findAll(attrs).associate { it.groupValues[1].trim() to it.groupValues[2].trim() }
            )
        }.toList()
        val headers = rows.flatMap { it.values.keys }.distinct()
        return ParsedImportFile(fileName, fileType, hasHeader = true, headers = headers, rows = rows)
    }

    private fun parseXlsx(fileName: String, fileType: String, input: InputStream): ParsedImportFile {
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
        val rawRows = Regex("<row([^>]*)>(.*?)</row>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).findAll(sheet).mapIndexed { rowIndex, rowMatch ->
            val rowAttrs = rowMatch.groupValues[1]
            val rowNumber = Regex("\\br=['\"]?(\\d+)", RegexOption.IGNORE_CASE).find(rowAttrs)?.groupValues?.get(1)?.toIntOrNull() ?: rowIndex + 1
            rowNumber to parseXlsxRow(rowMatch.groupValues[2], shared)
        }.filter { it.second.any { cell -> cell.isNotBlank() } }.toList()
        if (rawRows.isEmpty()) return ParsedImportFile(fileName, fileType, hasHeader = true, headers = emptyList(), rows = emptyList())
        val first = rawRows.first().second.map { it.trim() }
        val hasHeader = first.any { normalizeKey(it) in KNOWN_IMPORT_KEYS }
        val headers = if (hasHeader) first else listOf("article", "name", "barcode")
        val rows = rawRows.drop(if (hasHeader) 1 else 0).map { (rowNumber, values) ->
            ParsedImportRow(
                rowNumber = rowNumber,
                values = headers.mapIndexed { index, key -> key to values.getOrElse(index) { "" }.trim() }.toMap()
            )
        }
        return ParsedImportFile(fileName, fileType, hasHeader, headers, rows)
    }

    private fun parseXlsxRow(rowXml: String, shared: List<String>): List<String> {
        val valuesByColumn = mutableMapOf<Int, String>()
        var fallbackIndex = 0
        Regex("<c([^>]*)>(.*?)</c>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).findAll(rowXml).forEach { c ->
            val attrs = c.groupValues[1]
            val body = c.groupValues[2]
            val columnIndex = xlsxColumnIndex(attrs) ?: fallbackIndex
            fallbackIndex = columnIndex + 1
            val isShared = attrs.contains("t=\"s\"") || attrs.contains("t='s'")
            val inline = Regex("<t[^>]*>(.*?)</t>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).find(body)?.groupValues?.get(1)
            val value = Regex("<v[^>]*>(.*?)</v>", setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)).find(body)?.groupValues?.get(1)
            valuesByColumn[columnIndex] = when {
                inline != null -> html(inline)
                isShared && value != null -> shared.getOrElse(value.toIntOrNull() ?: -1) { "" }
                else -> html(value.orEmpty())
            }
        }
        val maxIndex = valuesByColumn.keys.maxOrNull() ?: return emptyList()
        return (0..maxIndex).map { valuesByColumn[it].orEmpty() }
    }

    private fun xlsxColumnIndex(attrs: String): Int? {
        val ref = Regex("\\br=['\"]?([A-Z]+)\\d+", RegexOption.IGNORE_CASE).find(attrs)?.groupValues?.get(1) ?: return null
        return ref.uppercase(Locale.getDefault()).fold(0) { acc, ch -> acc * 26 + (ch.code - 'A'.code + 1) } - 1
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

    private val ARTICLE_ALIASES = setOf(
        "article",
        "артикул",
        "артикултовара",
        "sku",
        "vendorcode",
        "vendorarticle",
        "offerid"
    )
    private val NAME_ALIASES = setOf(
        "name",
        "title",
        "название",
        "названиетовара",
        "наименование",
        "наименованиетовара",
        "товар",
        "номенклатура",
        "названиеноменклатуры",
        "productname",
        "producttitle"
    )
    private val BARCODE_ALIASES = setOf(
        "barcode",
        "barcodes",
        "штрихкод",
        "штрихкодтовара",
        "код",
        "кодтовара",
        "ean",
        "ean13"
    )
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
