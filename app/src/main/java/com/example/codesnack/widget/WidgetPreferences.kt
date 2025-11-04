package com.example.codesnack.widget

import android.content.Context
import com.example.codesnack.BuildConfig

class WidgetPreferences(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getCurrentSnippetId(): Int {
        return prefs.getInt(KEY_CURRENT_SNIPPET_ID, 1)
    }

    fun setCurrentSnippetId(id: Int) {
        prefs.edit().putInt(KEY_CURRENT_SNIPPET_ID, id).commit()
    }

    // Get single language (for backward compatibility)
    fun getSelectedLanguage(): String? {
        return prefs.getString(KEY_SELECTED_LANGUAGE, null)
    }

    fun setSelectedLanguage(language: String?) {
        prefs.edit().putString(KEY_SELECTED_LANGUAGE, language).apply()
    }

    // Get multiple selected languages
    fun getSelectedLanguages(): Set<String> {
        val languagesString = prefs.getString(KEY_SELECTED_LANGUAGES, null)
        return if (languagesString.isNullOrEmpty()) {
            emptySet()
        } else {
            languagesString.split(",").toSet()
        }
    }

    // Set multiple selected languages
    fun setSelectedLanguages(languages: Set<String>) {
        val languagesString = languages.joinToString(",")
        prefs.edit().putString(KEY_SELECTED_LANGUAGES, languagesString).commit()
    }

    // Get a random language from selected languages (for widget display)
    fun getRandomSelectedLanguage(): String? {
        val languages = getSelectedLanguages()
        return if (languages.isEmpty()) {
            null // Show all languages
        } else {
            languages.random()
        }
    }

    fun getGeminiApiKey(): String? {
        // Return user-set API key if available, otherwise fall back to BuildConfig
        val userApiKey = prefs.getString(KEY_GEMINI_API_KEY, null)
        return if (!userApiKey.isNullOrEmpty()) {
            userApiKey
        } else if (BuildConfig.GEMINI_API_KEY.isNotEmpty()) {
            BuildConfig.GEMINI_API_KEY
        } else {
            null
        }
    }

    fun setGeminiApiKey(apiKey: String?) {
        prefs.edit().putString(KEY_GEMINI_API_KEY, apiKey).commit()
    }

    fun isAiEnabled(): Boolean {
        // Check both user-set API key and BuildConfig API key
        val userApiKey = prefs.getString(KEY_GEMINI_API_KEY, null)
        return !userApiKey.isNullOrEmpty() || BuildConfig.GEMINI_API_KEY.isNotEmpty()
    }

    fun getUpdateFrequency(): Int {
        return prefs.getInt(KEY_UPDATE_FREQUENCY, 4) // Default: 4 hours
    }

    fun setUpdateFrequency(hours: Int) {
        prefs.edit().putInt(KEY_UPDATE_FREQUENCY, hours).commit()
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
        private const val KEY_SELECTED_LANGUAGES = "selected_languages" // Multiple languages
        private const val KEY_GEMINI_API_KEY = "gemini_api_key"
        private const val KEY_UPDATE_FREQUENCY = "update_frequency"
    }
}
