package com.example.practicalwork5_variant6

import com.example.practicalwork5_variant6.domain.PowerHistoryProcessor
import com.example.practicalwork5_variant6.domain.ReactorThresholds
import com.example.practicalwork5_variant6.domain.ProcessReading
import com.example.practicalwork5_variant6.domain.ReadingType
import org.junit.Assert.*
import org.junit.Test

class PowerHistoryProcessorTest {

    @Test
    fun `generateHistory returns requested number of points`() {
        val history = PowerHistoryProcessor.generateHistory(20)
        assertEquals(20, history.size)
    }

    @Test
    fun `generateHistory is deterministic for the same seed`() {
        val a = PowerHistoryProcessor.generateHistory(15, seed = 7L)
        val b = PowerHistoryProcessor.generateHistory(15, seed = 7L)
        assertEquals(a, b)
    }

    @Test
    fun `windowed returns at most the requested number of minutes`() {
        val history = PowerHistoryProcessor.generateHistory(30)
        val window = PowerHistoryProcessor.windowed(history, 10)
        assertEquals(10, window.size)
        assertEquals(history.takeLast(10), window)
    }

    @Test
    fun `average is within sensor coercion bounds`() {
        val history = PowerHistoryProcessor.generateHistory(50)
        val avg = PowerHistoryProcessor.average(history)
        assertTrue(avg in 8f..30f)
    }

    @Test
    fun `peak returns the point with the highest power`() {
        val history = PowerHistoryProcessor.generateHistory(50)
        val peak = PowerHistoryProcessor.peak(history)
        assertNotNull(peak)
        assertTrue(history.all { it.powerKw <= peak!!.powerKw })
    }

    @Test
    fun `threshold is exceeded above the configured maximum`() {
        val hot = ProcessReading(ReadingType.TEMPERATURE, ReactorThresholds.TEMPERATURE_MAX_C + 1f)
        val normal = ProcessReading(ReadingType.TEMPERATURE, ReactorThresholds.TEMPERATURE_MAX_C - 1f)
        assertTrue(ReactorThresholds.isExceeded(hot))
        assertFalse(ReactorThresholds.isExceeded(normal))
    }
}
