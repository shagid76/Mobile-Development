package com.example.practicalwork6_variant6.domain

data class ProcessRecord(
    val date: String,
    val output: Double,
    val status: String = ""
)

object ProcessCodec {

    const val CSV_HEADER = "date,output"

    fun toCsvLine(record: ProcessRecord): String = "${record.date},${record.output}"
    fun parseCsvLine(line: String): ProcessRecord? {
        val parts = line.split(",").map { it.trim() }
        if (parts.size < 2) return null
        val date = parts[0]
        val output = parts[1].toDoubleOrNull() ?: return null
        if (date.isBlank()) return null
        return ProcessRecord(date = date, output = output)
    }

    fun encodeCsv(records: List<ProcessRecord>): String =
        (listOf(CSV_HEADER) + records.map(::toCsvLine)).joinToString(separator = "\n")

    fun decodeCsv(text: String): List<ProcessRecord> =
        text.lineSequence()
            .drop(1) // заголовок
            .mapNotNull { if (it.isBlank()) null else parseCsvLine(it) }
            .toList()

    private fun escapeJson(value: String): String =
        value.replace("\\", "\\\\").replace("\"", "\\\"")

    fun toJsonObject(record: ProcessRecord): String =
        "{\"date\":\"${escapeJson(record.date)}\"," +
            "\"output\":${record.output}," +
            "\"status\":\"${escapeJson(record.status)}\"}"

    fun encodeJson(records: List<ProcessRecord>): String =
        records.joinToString(prefix = "[", postfix = "]", separator = ",") { toJsonObject(it) }

    private val FIELD_REGEX = Regex("\"(date|status)\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"|\"output\"\\s*:\\s*(-?[0-9]+(?:\\.[0-9]+)?)")

    fun decodeJson(text: String): List<ProcessRecord> {
        val trimmed = text.trim()
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) return emptyList()
        val inner = trimmed.substring(1, trimmed.length - 1).trim()
        if (inner.isEmpty()) return emptyList()

        val objects = splitTopLevelObjects(inner)
        return objects.mapNotNull { obj -> parseJsonObject(obj) }
    }

    private fun splitTopLevelObjects(inner: String): List<String> {
        val result = mutableListOf<String>()
        var depth = 0
        var start = -1
        var inString = false
        var i = 0
        while (i < inner.length) {
            val c = inner[i]
            when {
                c == '"' && (i == 0 || inner[i - 1] != '\\') -> inString = !inString
                inString -> {}
                c == '{' -> {
                    if (depth == 0) start = i
                    depth++
                }
                c == '}' -> {
                    depth--
                    if (depth == 0 && start >= 0) {
                        result.add(inner.substring(start, i + 1))
                        start = -1
                    }
                }
            }
            i++
        }
        return result
    }

    private fun parseJsonObject(obj: String): ProcessRecord? {
        var date = ""
        var output: Double? = null
        var status = ""
        for (match in FIELD_REGEX.findAll(obj)) {
            val outputValue = match.groupValues[3]
            if (outputValue.isNotEmpty()) {
                output = outputValue.toDoubleOrNull()
                continue
            }
            when (match.groupValues[1]) {
                "date" -> date = match.groupValues[2]
                "status" -> status = match.groupValues[2]
            }
        }
        if (date.isBlank() || output == null) return null
        return ProcessRecord(date = date, output = output, status = status)
    }

    fun filterByDateRange(records: List<ProcessRecord>, from: String, to: String): List<ProcessRecord> =
        records.filter { it.date in from..to }

    fun sortedByDate(records: List<ProcessRecord>): List<ProcessRecord> =
        records.sortedBy { it.date }
}
