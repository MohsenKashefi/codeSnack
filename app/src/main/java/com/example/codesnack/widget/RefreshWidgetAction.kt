package com.example.codesnack.widget

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.codesnack.data.SnippetRepository

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d("RefreshWidgetAction", "Refresh action triggered")

        // Get current snippet ID and selected languages
        val prefs = WidgetPreferences(context)
        val currentId = prefs.getCurrentSnippetId()
        val selectedLanguages = prefs.getSelectedLanguages()

        Log.d("RefreshWidgetAction", "Current snippet ID: $currentId, Selected languages: $selectedLanguages")

        // Get the next snippet from selected languages
        val nextSnippet = if (selectedLanguages.isNotEmpty()) {
            SnippetRepository.getNextSnippetFromLanguages(currentId, selectedLanguages)
        } else {
            SnippetRepository.getNextSnippet(currentId, null)
        }

        Log.d("RefreshWidgetAction", "Next snippet: ${nextSnippet.id} - ${nextSnippet.title} (${nextSnippet.language.name})")

        // Update Glance widget state - THIS triggers recomposition!
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[intPreferencesKey("current_snippet_id")] = nextSnippet.id
        }

        // Also save to SharedPreferences for backward compatibility
        context.getSharedPreferences("CodeSnackWidgetPrefs", Context.MODE_PRIVATE).edit()
            .putInt("current_snippet_id", nextSnippet.id)
            .apply()

        // Update widget - now it will recompose with new state!
        CodeSnackWidget().update(context, glanceId)
        Log.d("RefreshWidgetAction", "Widget updated with new snippet ID: ${nextSnippet.id}")
    }
}
