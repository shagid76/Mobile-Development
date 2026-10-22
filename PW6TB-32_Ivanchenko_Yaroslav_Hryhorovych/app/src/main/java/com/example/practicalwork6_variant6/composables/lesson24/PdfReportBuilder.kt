package com.example.practicalwork6_variant6.composables.lesson24

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.practicalwork6_variant6.domain.ProcessRecord
import java.io.File
import java.io.FileOutputStream

private const val PAGE_WIDTH = 595 // A4 при 72dpi
private const val PAGE_HEIGHT = 842
private const val MARGIN = 40f

class PdfReportBuilder(private val context: Context) {

    private val dir = File(context.filesDir, "reports").apply { mkdirs() }

    private val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true; color = Color.BLACK }
    private val headerPaint = Paint().apply { textSize = 13f; isFakeBoldText = true; color = Color.BLACK }
    private val textPaint = Paint().apply { textSize = 11f; color = Color.BLACK }
    private val linePaint = Paint().apply { color = Color.DKGRAY; strokeWidth = 1f }
    private val barPaint = Paint().apply { color = Color.rgb(76, 110, 245) }

    fun buildBasicReport(records: List<ProcessRecord>, objectName: String): Result<File> = runCatching {
        val document = PdfDocument()
        val page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create())
        val canvas = page.canvas
        var y = MARGIN

        canvas.drawText("Звіт про стан біогазової установки", MARGIN, y, titlePaint); y += 28f
        canvas.drawText("Об'єкт: $objectName", MARGIN, y, textPaint); y += 18f
        canvas.drawText(
            "Цей звіт сформовано автоматично на основі показників виходу біогазу, " +
                "зафіксованих у застосунку моніторингу процесів установки.",
            MARGIN, y, textPaint
        )
        y += 28f

        y = drawTable(canvas, records, y)

        document.finishPage(page)
        writeDocument(document, "basic_report_${System.currentTimeMillis()}.pdf")
    }

    fun buildMediumReport(records: List<ProcessRecord>, objectName: String): Result<File> = runCatching {
        val document = PdfDocument()
        val page = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create())
        val canvas = page.canvas
        var y = MARGIN

        canvas.drawText("Аналітичний звіт: $objectName", MARGIN, y, titlePaint); y += 24f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint); y += 20f

        canvas.drawText("Таблиця показників виходу біогазу", MARGIN, y, headerPaint); y += 18f
        y = drawTable(canvas, records, y)
        y += 20f

        canvas.drawText("Графік динаміки виходу біогазу", MARGIN, y, headerPaint); y += 10f
        y = drawBarChart(canvas, records, y)

        document.finishPage(page)
        writeDocument(document, "medium_report_${System.currentTimeMillis()}.pdf")
    }

    fun buildAdvancedReport(
        records: List<ProcessRecord>,
        objectName: String,
        comment: String
    ): Result<File> = runCatching {
        val document = PdfDocument()

        // Сторінка 1: титул + таблиця
        val page1 = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create())
        var y = MARGIN
        page1.canvas.drawText("Повний звіт про стан енергетичної мережі", MARGIN, y, titlePaint); y += 26f
        page1.canvas.drawText("Об'єкт: $objectName", MARGIN, y, textPaint); y += 20f
        page1.canvas.drawText("Таблиця показників", MARGIN, y, headerPaint); y += 18f
        drawTable(page1.canvas, records, y)
        document.finishPage(page1)

        val page2 = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 2).create())
        var y2 = MARGIN
        page2.canvas.drawText("Графік динаміки показників", MARGIN, y2, headerPaint); y2 += 20f
        drawBarChart(page2.canvas, records, y2)
        document.finishPage(page2)

        val page3 = document.startPage(PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 3).create())
        var y3 = MARGIN
        page3.canvas.drawText("Пояснення та висновки", MARGIN, y3, headerPaint); y3 += 20f
        wrapText(comment.ifBlank { "Коментар не надано." }, 70).forEach { line ->
            page3.canvas.drawText(line, MARGIN, y3, textPaint)
            y3 += 16f
        }
        document.finishPage(page3)

        writeDocument(document, "advanced_report_${System.currentTimeMillis()}.pdf")
    }

    private fun drawTable(canvas: Canvas, records: List<ProcessRecord>, startY: Float): Float {
        var y = startY
        canvas.drawText("Дата", MARGIN, y, headerPaint)
        canvas.drawText("Вихід, м³", MARGIN + 150f, y, headerPaint)
        canvas.drawText("Статус", MARGIN + 280f, y, headerPaint)
        y += 6f
        canvas.drawLine(MARGIN, y, PAGE_WIDTH - MARGIN, y, linePaint)
        y += 16f
        records.take(30).forEach { r ->
            canvas.drawText(r.date, MARGIN, y, textPaint)
            canvas.drawText("%.1f".format(r.output), MARGIN + 150f, y, textPaint)
            canvas.drawText(r.status.ifBlank { "—" }, MARGIN + 280f, y, textPaint)
            y += 16f
        }
        return y
    }

    private fun drawBarChart(canvas: Canvas, records: List<ProcessRecord>, startY: Float): Float {
        if (records.isEmpty()) return startY
        val chartHeight = 150f
        val chartWidth = PAGE_WIDTH - 2 * MARGIN
        val maxValue = (records.maxOfOrNull { it.output } ?: 1.0).coerceAtLeast(1.0)
        val barWidth = chartWidth / records.size.coerceAtLeast(1)

        canvas.drawLine(MARGIN, startY + chartHeight, MARGIN + chartWidth, startY + chartHeight, linePaint)
        records.take(20).forEachIndexed { index, r ->
            val barHeight = (r.output / maxValue * chartHeight).toFloat()
            val left = MARGIN + index * barWidth + 2f
            val top = startY + chartHeight - barHeight
            canvas.drawRect(left, top, left + barWidth - 4f, startY + chartHeight, barPaint)
        }
        return startY + chartHeight + 20f
    }

    private fun wrapText(text: String, maxCharsPerLine: Int): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var current = StringBuilder()
        for (word in words) {
            if (current.length + word.length + 1 > maxCharsPerLine) {
                lines.add(current.toString())
                current = StringBuilder()
            }
            if (current.isNotEmpty()) current.append(" ")
            current.append(word)
        }
        if (current.isNotEmpty()) lines.add(current.toString())
        return lines
    }

    private fun writeDocument(document: PdfDocument, fileName: String): File {
        val file = File(dir, fileName)
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    fun listReports(): List<File> = dir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun deleteReport(file: File): Boolean = file.exists() && file.delete()
}
