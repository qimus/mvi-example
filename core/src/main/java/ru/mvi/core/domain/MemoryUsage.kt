package ru.mvi.core.domain

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

data class MemoryUsageInfo(
    val available: Long,
    val total: Long,
    val appAlloc: Long
)

interface MemoryUsage {
    fun provide(): Flow<MemoryUsageInfo>
}

class MemoryUsageImpl @Inject constructor(context: Context) : MemoryUsage {
    private val activityManger: ActivityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    override fun provide(): Flow<MemoryUsageInfo> = flow {
        while (true) {
            val memInfo = ActivityManager.MemoryInfo()
            activityManger.getMemoryInfo(memInfo)
            emit(
                MemoryUsageInfo(
                    available = memInfo.availMem,
                    total = memInfo.totalMem,
                    appAlloc = Debug.getNativeHeapAllocatedSize()
                )
            )
            delay(500)
        }
    }
}
