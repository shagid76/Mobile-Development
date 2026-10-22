package com.example.practicalwork6_variant6.composables.lesson22

import android.content.Context
import com.example.practicalwork6_variant6.domain.ProcessCodec
import com.example.practicalwork6_variant6.domain.ProcessRecord
import java.io.File
import java.io.IOException

class ProcessFileStorage(context: Context) {

    private val dir = File(context.filesDir, "process_data").apply { mkdirs() }
    private val csvFile = File(dir, "biogas_process.csv")
    private val jsonFile = File(dir, "biogas_process.json")

    fun saveCsv(records: List<ProcessRecord>): Result<Unit> = runCatching {
        csvFile.writeText(ProcessCodec.encodeCsv(records))
    }

    fun loadCsv(): Result<List<ProcessRecord>> = runCatching {
        if (!csvFile.exists()) return@runCatching emptyList()
        ProcessCodec.decodeCsv(csvFile.readText())
    }.recoverCatching { throw IOException("Не вдалося прочитати CSV-файл: ${it.message}", it) }

    fun deleteCsv(): Boolean = csvFile.exists() && csvFile.delete()

    fun saveJson(records: List<ProcessRecord>): Result<Unit> = runCatching {
        jsonFile.writeText(ProcessCodec.encodeJson(records))
    }

    fun loadJson(): Result<List<ProcessRecord>> = runCatching {
        if (!jsonFile.exists()) return@runCatching emptyList()
        ProcessCodec.decodeJson(jsonFile.readText())
    }.recoverCatching { throw IOException("Не вдалося прочитати JSON-файл: ${it.message}", it) }

    fun deleteJson(): Boolean = jsonFile.exists() && jsonFile.delete()
}
