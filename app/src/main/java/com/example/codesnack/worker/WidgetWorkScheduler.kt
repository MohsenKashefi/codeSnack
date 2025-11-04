package com.example.codesnack.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WidgetWorkScheduler {

    private const val WIDGET_UPDATE_WORK_NAME = "widget_update_work"
    private const val AI_TIP_GENERATION_WORK_NAME = "ai_tip_generation_work"

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

    fun scheduleAiTipGeneration(context: Context) {
        // Schedule AI tip generation every 3 hours with network requirement
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true) // Don't drain battery
            .build()

        val aiTipWorkRequest = PeriodicWorkRequestBuilder<AiTipGenerationWorker>(
            3, TimeUnit.HOURS, // Run every 3 hours
            30, TimeUnit.MINUTES // 30 minute flex window
        )
            .setConstraints(constraints)
            .setInitialDelay(5, TimeUnit.MINUTES) // Start after 5 minutes to let app initialize
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                15, TimeUnit.MINUTES // Retry with exponential backoff if failed
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            AI_TIP_GENERATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing schedule to avoid duplicate work
            aiTipWorkRequest
        )
    }

    fun triggerImmediateAiGeneration(context: Context) {
        // Trigger one-time AI generation immediately for testing
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val immediateWorkRequest = OneTimeWorkRequestBuilder<AiTipGenerationWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueue(immediateWorkRequest)
    }

    fun cancelScheduledUpdates(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WIDGET_UPDATE_WORK_NAME)
        WorkManager.getInstance(context).cancelUniqueWork(AI_TIP_GENERATION_WORK_NAME)
    }
}
