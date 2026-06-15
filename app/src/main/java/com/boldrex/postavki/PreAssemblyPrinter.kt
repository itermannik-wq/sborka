package com.boldrex.postavki

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import android.util.Log
import jcifs.config.PropertyConfiguration
import jcifs.context.BaseContext
import jcifs.smb.NtlmPasswordAuthenticator
import jcifs.smb.SmbFile
import jcifs.smb.SmbFileOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

private const val PRE_ASSEMBLY_PRINTER_TAG = "PreAssemblyPrinter"
private const val PRE_ASSEMBLY_PRINT_PREFS = "preassembly_print"
private const val PRE_ASSEMBLY_BRIDGE_HOST_KEY = "bridge_host"
private const val PRE_ASSEMBLY_PRINTER_NAME_KEY = "printer_name"
private const val PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST = "192.168.0.137"
private const val PRE_ASSEMBLY_DEFAULT_PRINTER_NAME = "HP LaserJet MFP M129-M134"
private const val PRE_ASSEMBLY_SMB_HOST = "192.168.10.104"
private const val PRE_ASSEMBLY_SMB_SHARE = "bx-proizv"
private const val PRE_ASSEMBLY_SMB_USER = "Office"
private const val PRE_ASSEMBLY_SMB_PASSWORD = "123!Pegas!321"

object PreAssemblyPrinter {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun savedBridgeHost(context: Context): String {
        val prefs = context.getSharedPreferences(PRE_ASSEMBLY_PRINT_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(PRE_ASSEMBLY_BRIDGE_HOST_KEY, PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST)
            ?.trim()
            ?.ifBlank { PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST }
            ?: PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST
    }

    fun saveBridgeHost(context: Context, host: String) {
        val normalized = normalizeBridgeHost(host).ifBlank { PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST }
        context.getSharedPreferences(PRE_ASSEMBLY_PRINT_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(PRE_ASSEMBLY_BRIDGE_HOST_KEY, normalized)
            .apply()
        PreAssemblyPrintLog.append(context.applicationContext, "Saved print bridge host: $normalized")
    }

    fun savedPrinterName(context: Context): String {
        val prefs = context.getSharedPreferences(PRE_ASSEMBLY_PRINT_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(PRE_ASSEMBLY_PRINTER_NAME_KEY, PRE_ASSEMBLY_DEFAULT_PRINTER_NAME)
            ?.trim()
            ?.ifBlank { PRE_ASSEMBLY_DEFAULT_PRINTER_NAME }
            ?: PRE_ASSEMBLY_DEFAULT_PRINTER_NAME
    }

    fun savePrinterName(context: Context, printerName: String) {
        val normalized = printerName.trim().ifBlank { PRE_ASSEMBLY_DEFAULT_PRINTER_NAME }
        context.getSharedPreferences(PRE_ASSEMBLY_PRINT_PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(PRE_ASSEMBLY_PRINTER_NAME_KEY, normalized)
            .apply()
        PreAssemblyPrintLog.append(context.applicationContext, "Saved Windows printer name: $normalized")
    }

    fun printTransferList(context: Context, reportText: String): Result<Unit> {
        val printedAt = LocalDateTime.now().format(dateFormatter)
        val printerName = savedPrinterName(context)
        val rawText = rawPrinterText(
            title = "ЛИСТ НА СБОРКУ И ПЕРЕМЕЩЕНИЕ",
            subtitle = "Предварительная сборка Ozon, $printedAt",
            body = reportText,
            printerName = printerName
        )
        return printInBackground(
            context = context,
            jobName = "Лист на сборку Ozon",
            printerName = printerName,
            rawText = rawText,
            successMessage = "Лист отправлен на принтер $printerName"
        )
    }

    fun printTestPage(context: Context): Result<Unit> {
        val printedAt = LocalDateTime.now().format(dateFormatter)
        val printerName = savedPrinterName(context)
        val bridgeHost = savedBridgeHost(context)
        val testText = """
Тестовая печать

Принтер: $printerName
Мост: $bridgeHost
Время: $printedAt

Если этот лист вышел из принтера, печать листков предварительной сборки готова к работе.
        """.trimIndent()

        val rawText = rawPrinterText(
            title = "ТЕСТ ПРИНТЕРА",
            subtitle = "$printerName, $printedAt",
            body = testText,
            printerName = printerName
        )
        return printInBackground(
            context = context,
            jobName = "Тест принтера $printerName",
            printerName = printerName,
            rawText = rawText,
            successMessage = "Тестовая печать отправлена на $printerName"
        )
    }

    private fun printInBackground(
        context: Context,
        jobName: String,
        printerName: String,
        rawText: String,
        successMessage: String
    ): Result<Unit> = runCatching {
        val activity = context.findPrintActivity()
            ?: error("Не удалось открыть окно печати: активность приложения недоступна")
        val appContext = activity.applicationContext
        PreAssemblyPrintLog.append(appContext, "Print job created '$jobName': printer='$printerName', chars=${rawText.length}, lines=${rawText.lines().size}")
        val mainHandler = Handler(Looper.getMainLooper())
        Thread {
            PreAssemblyPrintLog.append(appContext, "Print job started in background '$jobName'")
            val directPrint = runCatching {
                val usbPrint = UsbDirectPrinter.printTextIfAvailable(
                    context = appContext,
                    jobName = jobName,
                    text = rawText
                )
                if (usbPrint != null) {
                    usbPrint
                        .onSuccess { usbResult ->
                            PreAssemblyPrintLog.append(
                                appContext,
                                "Print job finished via USB OTG '$jobName': printer='${usbResult.printerName}', bytes=${usbResult.bytesWritten}"
                            )
                            return@runCatching "Лист отправлен на USB-принтер ${usbResult.printerName}"
                        }
                        .onFailure { usbError ->
                            PreAssemblyPrintLog.append(
                                appContext,
                                "USB OTG print failed for '$jobName', falling back to print bridge",
                                usbError
                            )
                        }
                }

                try {
                    printViaBridge(context = appContext, jobName = jobName, printerName = printerName, text = rawText)
                } catch (bridgeError: Throwable) {
                    PreAssemblyPrintLog.append(
                        appContext,
                        "HTTP print bridge failed for '$jobName', trying SMB printer share \\\\$PRE_ASSEMBLY_SMB_HOST\\$PRE_ASSEMBLY_SMB_SHARE",
                        bridgeError
                    )
                    printViaSmbShare(context = appContext, jobName = jobName, text = rawText)
                }
                successMessage
            }
            mainHandler.post {
                directPrint
                    .onSuccess { message ->
                        PreAssemblyPrintLog.append(appContext, "Print job finished successfully '$jobName'")
                        PrinterUiNotifier.success(
                            title = "Печать отправлена",
                            text = message
                        )
                    }
                    .onFailure { error ->
                        PreAssemblyPrintLog.append(appContext, "Print job failed '$jobName'", error)
                        Log.e(PRE_ASSEMBLY_PRINTER_TAG, "Print failed", error)
                        PrinterUiNotifier.error(
                            title = "Не удалось напечатать",
                            text = error.message ?: "Проверьте мост или SMB-принтер"
                        )
                    }
            }
        }.apply {
            name = "pre-assembly-printer"
            isDaemon = true
            start()
        }
    }

    private fun printViaBridge(context: Context, jobName: String, printerName: String, text: String) {
        val errors = mutableListOf<String>()
        val targets = bridgeTargets(context)
        PreAssemblyPrintLog.append(context, "Print bridge targets: ${targets.joinToString { it.healthUrl }}")
        targets.forEach { target ->
            PreAssemblyPrintLog.append(context, "Checking print bridge '${target.name}' at ${target.healthUrl}")
            val health = runCatching { checkBridgeHealth(target) }
            if (health.isFailure) {
                val message = health.exceptionOrNull()?.message ?: "нет ответа"
                errors += "${target.name}: $message"
                PreAssemblyPrintLog.append(context, "Print bridge '${target.name}' health failed: $message", health.exceptionOrNull())
                Log.w(PRE_ASSEMBLY_PRINTER_TAG, "Print bridge health failed at ${target.healthUrl}", health.exceptionOrNull())
                return@forEach
            }
            PreAssemblyPrintLog.append(context, "Print bridge '${target.name}' is ready: ${health.getOrNull().orEmpty()}")

            PreAssemblyPrintLog.append(context, "Sending print job '$jobName' to ${target.printUrl}")
            val attempt = runCatching { postPrintJob(target = target, jobName = jobName, printerName = printerName, text = text) }
            if (attempt.isSuccess) {
                PreAssemblyPrintLog.append(context, "Print bridge '${target.name}' accepted '$jobName': ${attempt.getOrNull().orEmpty()}")
                Log.i(PRE_ASSEMBLY_PRINTER_TAG, "Print bridge printed job via ${target.printUrl}")
                return
            }
            val message = attempt.exceptionOrNull()?.message ?: "ошибка печати"
            errors += "${target.name}: $message"
            PreAssemblyPrintLog.append(context, "Print bridge '${target.name}' returned print error: $message", attempt.exceptionOrNull())
            Log.w(PRE_ASSEMBLY_PRINTER_TAG, "Print bridge failed at ${target.printUrl}", attempt.exceptionOrNull())
        }
        error(
            "не найден рабочий мост печати. Проверьте IP моста (${savedBridgeHost(context)}) и запустите StartPreAssemblyPrintBridgeBackground.bat. " +
                errors.joinToString(" | ")
        )
    }

    private fun bridgeTargets(context: Context): List<PrintBridgeTarget> {
        val savedHost = savedBridgeHost(context)
        val hosts = listOf(savedHost, PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST, "10.0.2.2")
            .map { normalizeBridgeHost(it) }
            .filter { it.isNotBlank() }
            .distinct()
        return hosts.map { host ->
            val baseUrl = bridgeBaseUrl(host)
            val name = when (host) {
                "10.0.2.2" -> "эмулятор 10.0.2.2"
                PRE_ASSEMBLY_DEFAULT_BRIDGE_HOST -> "ПК с принтером $host"
                else -> "настроенный мост $host"
            }
            PrintBridgeTarget(
                name = name,
                healthUrl = "$baseUrl/health",
                printUrl = "$baseUrl/print"
            )
        }
    }

    private fun checkBridgeHealth(target: PrintBridgeTarget): String {
        val connection = (URL(target.healthUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 2500
            readTimeout = 3500
            setRequestProperty("Connection", "close")
        }
        try {
            val code = connection.responseCode
            val body = when {
                code in 200..299 -> connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
                else -> connection.errorStream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
            }
            if (code !in 200..299 || !body.contains("PreAssemblyPrintBridge OK")) {
                error("порт открыт, но это не новый мост печати")
            }
            return "HTTP $code: $body"
        } finally {
            connection.disconnect()
        }
    }

    private fun postPrintJob(target: PrintBridgeTarget, jobName: String, printerName: String, text: String): String {
        val body = """
{
  "printer": "${printerName.escapeJson()}",
  "title": "${jobName.escapeJson()}",
  "text": "${text.escapeJson()}"
}
        """.trimIndent()
        val connection = (URL(target.printUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 2500
            readTimeout = 60000
            doOutput = true
            setRequestProperty("Connection", "close")
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }
        try {
            OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { writer ->
                writer.write(body)
            }
            val code = connection.responseCode
            val responseText = if (code in 200..299) {
                connection.inputStream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            } else {
                connection.errorStream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                    ?: connection.responseMessage.orEmpty()
            }
            if (code !in 200..299) {
                error("мост печати вернул $code: $responseText")
            }
            return "HTTP $code: $responseText"
        } finally {
            connection.disconnect()
        }
    }

    private fun printViaSmbShare(context: Context, jobName: String, text: String) {
        val smbUrl = "smb://$PRE_ASSEMBLY_SMB_HOST/$PRE_ASSEMBLY_SMB_SHARE"
        PreAssemblyPrintLog.append(context, "Sending print job '$jobName' to SMB printer share $smbUrl as $PRE_ASSEMBLY_SMB_USER")

        val properties = Properties().apply {
            setProperty("jcifs.smb.client.enableSMB2", "true")
            setProperty("jcifs.smb.client.useSMB2Negotiation", "true")
            setProperty("jcifs.smb.client.disableSMB1", "false")
            setProperty("jcifs.smb.client.connTimeout", "5000")
            setProperty("jcifs.smb.client.responseTimeout", "15000")
            setProperty("jcifs.smb.client.soTimeout", "15000")
        }
        val contextWithCredentials = BaseContext(PropertyConfiguration(properties))
            .withCredentials(
                NtlmPasswordAuthenticator(
                    PRE_ASSEMBLY_SMB_HOST,
                    PRE_ASSEMBLY_SMB_USER,
                    PRE_ASSEMBLY_SMB_PASSWORD
                )
            )
        val printer = SmbFile(smbUrl, contextWithCredentials)
        val bytes = (text.trimEnd() + "\r\n\r\n\u000C").toByteArray(Charset.forName("windows-1251"))

        SmbFileOutputStream(printer).use { output ->
            output.write(bytes)
            output.flush()
        }
        PreAssemblyPrintLog.append(context, "SMB printer share accepted '$jobName': $smbUrl, bytes=${bytes.size}")
    }

    private fun rawPrinterText(title: String, subtitle: String, body: String, printerName: String): String {
        return """
$title
$subtitle
Принтер: $printerName

${body.trim()}
        """.trimIndent()
    }
}

private fun normalizeBridgeHost(value: String): String {
    return value
        .trim()
        .removePrefix("http://")
        .removePrefix("https://")
        .substringBefore("/")
        .trim()
}

private fun bridgeBaseUrl(host: String): String {
    val normalized = normalizeBridgeHost(host)
    val hostWithPort = if (normalized.substringAfterLast(":") == normalized) {
        "$normalized:8787"
    } else {
        normalized
    }
    return "http://$hostWithPort"
}

private data class PrintBridgeTarget(
    val name: String,
    val healthUrl: String,
    val printUrl: String
)

private tailrec fun Context.findPrintActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findPrintActivity()
    else -> null
}

private fun String.escapeJson(): String = buildString(length + 16) {
    this@escapeJson.forEach { char ->
        when (char) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\b' -> append("\\b")
            '\u000C' -> append("\\f")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            in '\u0000'..'\u001F' -> append("\\u%04x".format(char.code))
            else -> append(char)
        }
    }
}
