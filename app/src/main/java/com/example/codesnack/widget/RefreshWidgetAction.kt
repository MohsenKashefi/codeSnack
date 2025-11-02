package com.example.codesnack.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll
import com.example.codesnack.data.SnippetRepository
import kotlinx.coroutines.delay

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("RefreshWidgetAction", "Refresh action triggered")

        // Get current snippet ID from preferences
        val prefs = WidgetPreferences(context)
        val currentId = prefs.getCurrentSnippetId()
        Log.d("RefreshWidgetAction", "Current snippet ID: $currentId")

        // Get the next snippet using repository's method
        val nextSnippet = SnippetRepository.getNextSnippet(currentId)
        Log.d("RefreshWidgetAction", "Next snippet ID: ${nextSnippet.id} - ${nextSnippet.title}")

        // Save the new snippet ID synchronously with explicit commit
        val editor = context.getSharedPreferences("CodeSnackWidgetPrefs", Context.MODE_PRIVATE).edit()
        editor.putInt("current_snippet_id", nextSnippet.id)
        editor.commit()

        // Verify it was saved
        val savedId = prefs.getCurrentSnippetId()
        Log.d("RefreshWidgetAction", "Saved snippet ID: $savedId")

        // Small delay to ensure preference is flushed
        delay(50)

        // Force immediate widget update for all instances using multiple methods
        CodeSnackWidget().updateAll(context)
        Log.d("RefreshWidgetAction", "Widget updateAll called")

        // Also send explicit update broadcast to widget receiver
        val intent = Intent(context, CodeSnackWidgetReceiver::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(context, CodeSnackWidgetReceiver::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        context.sendBroadcast(intent)
        Log.d("RefreshWidgetAction", "Update broadcast sent for ${widgetIds.size} widgets")
    }
}
