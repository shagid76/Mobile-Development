package com.example.practicalwork1_variant6

import com.example.practicalwork1_variant6.domain.BiogasCalculator
import com.example.practicalwork1_variant6.domain.ProcessStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class BiogasCalculatorTest {

    private val calculator = BiogasCalculator()

    @Test
    fun calculateBiogasBasic_returnsExpectedYield() {
        val result = calculator.calculateBiogasBasic("100")
        assertEquals(35.0, result, 0.001)
    }

    @Test
    fun calculateBiogasBasic_invalidInput_returnsZero() {
        val result = calculator.calculateBiogasBasic("abc")
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun calculateBiogasMedium_returnsExpectedYield() {
        // mass=100, humidity=50% -> 100 * 0.35 * (0.5 + 0.5) = 35.0
        val result = calculator.calculateBiogasMedium("100", "50")
        assertEquals(35.0, result, 0.001)
    }

    @Test
    fun processStatus_stableRange() {
        assertEquals(ProcessStatus.STABLE, ProcessStatus.fromMethane(60))
    }

    @Test
    fun processStatus_criticalRange() {
        assertEquals(ProcessStatus.CRITICAL, ProcessStatus.fromMethane(10))
    }
}
