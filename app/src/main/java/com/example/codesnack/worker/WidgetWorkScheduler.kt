package com.example.codesnack.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WidgetWorkScheduler {

    private const val WIDGET_UPDATE_WORK_NAME = "widget_update_work"

    fun scheduleDailyUpdate(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            24, TimeUnit.HOURS,
            15, TimeUnit.MINUTES // Flex interval
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WIDGET_UPDATE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }

    fun scheduleHourlyUpdate(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val hourlyWorkRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WIDGET_UPDATE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            hourlyWorkRequest
        )
    }

    fun cancelScheduledUpdates(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK_NAME)
    }
}
