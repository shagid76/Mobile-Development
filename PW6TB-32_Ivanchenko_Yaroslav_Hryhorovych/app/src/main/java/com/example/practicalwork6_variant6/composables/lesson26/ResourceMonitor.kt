package com.example.practicalwork6_variant6.composables.lesson26

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Process
import com.example.practicalwork6_variant6.domain.ResourceEvaluator

data class ResourceSnapshot(
    val cpuPercent: Float,
    val memoryPercent: Float,
    val batteryPercent: Int,
    val networkStatus: String
)

class ResourceMonitor(private val context: Context) {

    private var lastCpuTimeMs: Long = Process.getElapsedCpuTime()
    private var lastWallTimeMs: Long = System.currentTimeMillis()

    fun snapshot(): ResourceSnapshot {
        val cpu = computeCpuPercent()
        val memory = computeMemoryPercent()
        val battery = computeBatteryPercent()
        val network = computeNetworkStatus()
        return ResourceSnapshot(cpu, memory, battery, network)
    }

    private fun computeCpuPercent(): Float {
        val cpuNow = Process.getElapsedCpuTime()
        val wallNow = System.currentTimeMillis()
        val cpuDelta = (cpuNow - lastCpuTimeMs).coerceAtLeast(0L)
        val wallDelta = (wallNow - lastWallTimeMs).coerceAtLeast(1L)
        lastCpuTimeMs = cpuNow
        lastWallTimeMs = wallNow
        return (cpuDelta.toFloat() / wallDelta.toFloat() * 100f).coerceIn(0f, 100f)
    }

    private fun computeMemoryPercent(): Float {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return 0f
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)
        return ResourceEvaluator.memoryUsagePercent(info.totalMem, info.availMem)
    }

    private fun computeBatteryPercent(): Int {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, filter) ?: return -1
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        if (level < 0 || scale <= 0) return -1
        return (level * 100 / scale)
    }

    private fun computeNetworkStatus(): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            ?: return "Невідомо"
        val network = cm.activeNetwork ?: return "Немає з'єднання"
        val capabilities = cm.getNetworkCapabilities(network) ?: return "Немає з'єднання"
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Мобільна мережа"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Інше з'єднання"
        }
    }
}
