package com.example.codesnack.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.glance.appwidget.updateAll
import com.example.codesnack.widget.CodeSnackWidget
import com.example.codesnack.widget.WidgetPreferences

class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Update to next snippet
            val prefs = WidgetPreferences(applicationContext)
            prefs.incrementSnippet()

            // Update all widget instances
            CodeSnackWidget().updateAll(applicationContext)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
