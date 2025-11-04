package com.example.codesnack.worker

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.glance.appwidget.updateAll
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.widget.CodeSnackWidget
import com.example.codesnack.widget.CodeSnackWidgetReceiver
import com.example.codesnack.widget.WidgetPreferences
import kotlinx.coroutines.delay

class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("WidgetUpdateWorker", "Hourly update triggered")

            // Get current snippet and move to next with language filter
            val prefs = WidgetPreferences(applicationContext)
            val currentId = prefs.getCurrentSnippetId()
            val selectedLanguage = prefs.getSelectedLanguage()
            val nextSnippet = SnippetRepository.getNextSnippet(currentId, selectedLanguage)

            Log.d("WidgetUpdateWorker", "Updating from snippet $currentId to ${nextSnippet.id} (Language: $selectedLanguage)")

            // Save new snippet ID synchronously
            val editor = applicationContext.getSharedPreferences("CodeSnackWidgetPrefs", Context.MODE_PRIVATE).edit()
            editor.putInt("current_snippet_id", nextSnippet.id)
            editor.commit()

            // Small delay to ensure preference is flushed
            delay(50)

            // Update all widget instances
            CodeSnackWidget().updateAll(applicationContext)

            // Send explicit update broadcast
            val intent = Intent(applicationContext, CodeSnackWidgetReceiver::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
            val widgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(applicationContext, CodeSnackWidgetReceiver::class.java)
            )
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
            applicationContext.sendBroadcast(intent)

            Log.d("WidgetUpdateWorker", "Hourly update completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("WidgetUpdateWorker", "Error during hourly update", e)
            Result.retry()
        }
    }
}
