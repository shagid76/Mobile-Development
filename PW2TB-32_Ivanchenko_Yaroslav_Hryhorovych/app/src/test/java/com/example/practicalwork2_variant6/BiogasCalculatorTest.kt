package com.example.practicalwork2_variant6

import com.example.practicalwork2_variant6.domain.BiogasCalculator
import com.example.practicalwork2_variant6.domain.ProcessStatus
import com.example.practicalwork2_variant6.domain.ReactorState
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
    fun processStatus_stableRange() {
        assertEquals(ProcessStatus.STABLE, ProcessStatus.fromMethane(60))
    }

    @Test
    fun processStatus_criticalRange() {
        assertEquals(ProcessStatus.CRITICAL, ProcessStatus.fromMethane(10))
    }

    @Test
    fun estimateDailyYield_emptyReactor_returnsZero() {
        val result = calculator.estimateDailyYield(ReactorState())
        assertEquals(0.0, result, 0.001)
    }

    @Test
    fun estimateDailyYield_fullReactorNormalMethane_returnsPositiveValue() {
        val state = ReactorState(fillLevel = 100, methaneConcentration = 60, temperature = 35, pressure = 120)
        val result = calculator.estimateDailyYield(state)
        assertEquals(6.0, result, 0.001) // 1.0 * 0.6 * 1.0 * 10.0
    }
}
