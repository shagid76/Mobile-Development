package com.example.practicalwork6_variant6

import com.example.practicalwork6_variant6.domain.ResourceEvaluator
import com.example.practicalwork6_variant6.domain.ResourceStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResourceEvaluatorTest {

    @Test
    fun `classify returns NORMAL below 70 percent`() {
        assertEquals(ResourceStatus.NORMAL, ResourceEvaluator.classify(69.9f))
    }

    @Test
    fun `classify returns WARNING at boundary 70 percent`() {
        assertEquals(ResourceStatus.WARNING, ResourceEvaluator.classify(70f))
    }

    @Test
    fun `classify returns CRITICAL at boundary 90 percent`() {
        assertEquals(ResourceStatus.CRITICAL, ResourceEvaluator.classify(90f))
    }

    @Test
    fun `classify handles NaN gracefully as NORMAL`() {
        assertEquals(ResourceStatus.NORMAL, ResourceEvaluator.classify(Float.NaN))
    }

    @Test
    fun `isBatteryLow true for 15 percent and below`() {
        assertTrue(ResourceEvaluator.isBatteryLow(15))
        assertFalse(ResourceEvaluator.isBatteryLow(16))
    }

    @Test
    fun `memoryUsagePercent computes correct ratio`() {
        val percent = ResourceEvaluator.memoryUsagePercent(totalBytes = 1000L, availableBytes = 250L)
        assertEquals(75f, percent, 0.01f)
    }

    @Test
    fun `memoryUsagePercent returns zero for invalid total`() {
        assertEquals(0f, ResourceEvaluator.memoryUsagePercent(totalBytes = 0L, availableBytes = 10L), 0.01f)
    }
}
