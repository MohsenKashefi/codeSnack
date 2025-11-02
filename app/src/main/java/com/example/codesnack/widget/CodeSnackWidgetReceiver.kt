package com.example.codesnack.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.codesnack.worker.WidgetWorkScheduler

class CodeSnackWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CodeSnackWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule hourly updates when the first widget is added
        WidgetWorkScheduler.scheduleHourlyUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel updates when all widgets are removed
        WidgetWorkScheduler.cancelScheduledUpdates(context)
    }
}
