package com.example.codesnack.widget

import android.content.Context

class WidgetPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCurrentSnippetId(): Int {
        return prefs.getInt(KEY_CURRENT_SNIPPET_ID, 1)
    }

    fun setCurrentSnippetId(id: Int) {
        prefs.edit().putInt(KEY_CURRENT_SNIPPET_ID, id).commit()
    }

    fun getSelectedLanguage(): String? {
        return prefs.getString(KEY_SELECTED_LANGUAGE, null)
    }

    fun setSelectedLanguage(language: String?) {
        prefs.edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }

    fun incrementSnippet() {
        val currentId = getCurrentSnippetId()
        val nextId = if (currentId >= 24) 1 else currentId + 1
        setCurrentSnippetId(nextId)
    }

    companion object {
        private const val PREFS_NAME = "CodeSnackWidgetPrefs"
        private const val KEY_CURRENT_SNIPPET_ID = "current_snippet_id"
        private const val KEY_SELECTED_LANGUAGE = "selected_language"
    }
}
