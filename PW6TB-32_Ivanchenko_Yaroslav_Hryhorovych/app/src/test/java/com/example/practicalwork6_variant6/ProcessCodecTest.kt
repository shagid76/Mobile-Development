package com.example.practicalwork6_variant6

import com.example.practicalwork6_variant6.domain.ProcessCodec
import com.example.practicalwork6_variant6.domain.ProcessRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProcessCodecTest {

    private val sample = listOf(
        ProcessRecord("2025-09-10", 48.0, "Активна"),
        ProcessRecord("2025-09-11", 52.5, "Обслуговування")
    )

    @Test
    fun `CSV encode-decode round trip preserves date and output`() {
        val csv = ProcessCodec.encodeCsv(sample)
        val decoded = ProcessCodec.decodeCsv(csv)

        assertEquals(2, decoded.size)
        assertEquals("2025-09-10", decoded[0].date)
        assertEquals(48.0, decoded[0].output, 0.0001)
    }

    @Test
    fun `CSV decode skips malformed lines`() {
        val text = "date,output\n2025-09-10,48.0\nnot-a-valid-line\n2025-09-12,not_a_number\n2025-09-13,10"
        val decoded = ProcessCodec.decodeCsv(text)

        assertEquals(2, decoded.size)
        assertEquals("2025-09-13", decoded[1].date)
    }

    @Test
    fun `parseCsvLine returns null for blank date`() {
        assertNull(ProcessCodec.parseCsvLine(",42.0"))
    }

    @Test
    fun `JSON encode-decode round trip preserves status`() {
        val json = ProcessCodec.encodeJson(sample)
        val decoded = ProcessCodec.decodeJson(json)

        assertEquals(2, decoded.size)
        assertEquals("Обслуговування", decoded[1].status)
        assertEquals(52.5, decoded[1].output, 0.0001)
    }

    @Test
    fun `JSON decode of empty array returns empty list`() {
        assertTrue(ProcessCodec.decodeJson("[]").isEmpty())
    }

    @Test
    fun `JSON decode of malformed text returns empty list`() {
        assertTrue(ProcessCodec.decodeJson("not json at all").isEmpty())
    }

    @Test
    fun `filterByDateRange keeps only records within bounds`() {
        val extended = sample + ProcessRecord("2025-10-01", 10.0)
        val filtered = ProcessCodec.filterByDateRange(extended, "2025-09-01", "2025-09-30")

        assertEquals(2, filtered.size)
    }

    @Test
    fun `sortedByDate orders records chronologically`() {
        val unordered = listOf(sample[1], sample[0])
        val sorted = ProcessCodec.sortedByDate(unordered)

        assertEquals("2025-09-10", sorted.first().date)
    }
}
