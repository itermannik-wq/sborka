package com.boldrex.postavki

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val PRINT_LOG_FILE_NAME = "preassembly_print_log.txt"
private const val PRINT_LOG_MAX_BYTES = 900_000
private const val PRINT_LOG_KEEP_BYTES = 600_000

object PreAssemblyPrintLog {
    private val lock = Any()
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    private val fileNameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    fun append(context: Context, message: String, error: Throwable? = null) {
        runCatching {
            synchronized(lock) {
                val file = logFile(context)
                val line = buildString {
                    append(LocalDateTime.now().format(dateTimeFormatter))
                    append(" [")
                    append(Thread.currentThread().name)
                    append("] ")
                    append(message)
                    appendLine()
                    if (error != null) {
                        append(stackTraceText(error))
                        appendLine()
                    }
                }
                file.appendText(line, Charsets.UTF_8)
                trimIfNeeded(file)
            }
        }
    }

    fun share(context: Context): Result<Unit> = runCatching {
        val snapshot = snapshotFile(context)
        val uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", snapshot)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Логи печати предварительной сборки")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, "Выгрузить логи печати")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun path(context: Context): String = logFile(context).absolutePath

    private fun snapshotFile(context: Context): File {
        synchronized(lock) {
            val source = logFile(context)
            val file = File(logDir(context), "preassembly_print_log_${LocalDateTime.now().format(fileNameFormatter)}.txt")
            val text = if (source.exists()) source.readText(Charsets.UTF_8) else "Лог печати пока пуст.\n"
            file.writeText(buildHeader(context) + text, Charsets.UTF_8)
            return file
        }
    }

    private fun buildHeader(context: Context): String {
        val packageInfo = runCatching { context.packageManager.getPackageInfo(context.packageName, 0) }.getOrNull()
        val versionCode = packageInfo?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) it.longVersionCode else it.versionCode.toLong()
        } ?: 0L
        return buildString {
            appendLine("=== PreAssembly print log export ===")
            appendLine("Exported: ${LocalDateTime.now().format(dateTimeFormatter)}")
            appendLine("Package: ${context.packageName}")
            appendLine("Version: ${packageInfo?.versionName ?: "unknown"} ($versionCode)")
            appendLine("Device: ${Build.MANUFACTURER} ${Build.MODEL}, Android ${Build.VERSION.RELEASE}")
            appendLine("Saved bridge host: ${PreAssemblyPrinter.savedBridgeHost(context)}")
            appendLine("Saved Windows printer name: ${PreAssemblyPrinter.savedPrinterName(context)}")
            appendLine("USB direct print: ${if (UsbDirectPrinter.isEnabled(context)) "enabled" else "disabled"}")
            appendLine("USB PDF to TSPL: ${if (UsbDirectPrinter.isRawPdfEnabled(context)) "enabled" else "disabled"}")
            appendLine("USB print offset: ${UsbDirectPrinter.formatOffsetXMm(UsbDirectPrinter.offsetXMm(context))} mm")
            appendLine("USB printer status: ${UsbDirectPrinter.statusText(context)}")
            appendLine("Log file: ${logFile(context).absolutePath}")
            appendLine("====================================")
            appendLine()
        }
    }

    private fun logFile(context: Context): File {
        val dir = logDir(context)
        return File(dir, PRINT_LOG_FILE_NAME)
    }

    private fun logDir(context: Context): File {
        val dir = File(context.filesDir, "reports")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    private fun trimIfNeeded(file: File) {
        if (file.length() <= PRINT_LOG_MAX_BYTES) return
        val bytes = file.readBytes()
        val keepFrom = (bytes.size - PRINT_LOG_KEEP_BYTES).coerceAtLeast(0)
        val kept = bytes.copyOfRange(keepFrom, bytes.size).toString(Charsets.UTF_8)
        file.writeText(
            "=== Старые строки лога обрезаны, чтобы файл не рос бесконечно ===\n$kept",
            Charsets.UTF_8
        )
    }

    private fun stackTraceText(error: Throwable): String {
        val writer = StringWriter()
        error.printStackTrace(PrintWriter(writer))
        return writer.toString()
    }
}
