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
        // Minimal constraints for more reliable updates
        val constraints = Constraints.Builder()
            .build()

        val hourlyWorkRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
            1, TimeUnit.HOURS,
            15, TimeUnit.MINUTES // 15 minute flex window
        )
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.HOURS) // First update after 1 hour
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WIDGET_UPDATE_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE, // Replace existing to ensure fresh schedule
            hourlyWorkRequest
        )
    }

    fun cancelScheduledUpdates(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK_NAME)
    }
}
