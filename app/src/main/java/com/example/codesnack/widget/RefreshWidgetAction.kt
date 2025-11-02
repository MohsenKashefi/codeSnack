package com.example.codesnack.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.updateAll

class RefreshWidgetAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Get next snippet
        val prefs = WidgetPreferences(context)
        prefs.incrementSnippet()

        // Update the widget
        CodeSnackWidget().updateAll(context)
    }
}
