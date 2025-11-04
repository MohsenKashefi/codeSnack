package com.example.codesnack.service

import android.content.Context
import android.util.Log
import com.example.codesnack.data.AiTip
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.model.CodeSnippet
import com.example.codesnack.widget.WidgetPreferences
import kotlin.random.Random

class TipProvider(private val context: Context) {

    private val prefs = WidgetPreferences(context)

    companion object {
        private const val AI_TIP_PROBABILITY = 0.7 // 70% chance of AI tip when enabled
    }

    data class Tip(
        val title: String,
        val code: String,
        val explanation: String,
        val language: String,
        val category: String,
        val isAiGenerated: Boolean
    )

    fun getNextTip(currentId: Int, language: String?, forceGenerate: Boolean = false): Tip {
        // ALWAYS use static tips for widget to avoid ANR
        // AI tips are pre-cached by background worker and stored in SharedPreferences
        // Widget will display them through the cached tip mechanism in RefreshWidgetAction
        Log.d("TipProvider", "Getting next tip (static only to avoid ANR)")
        return getStaticTip(currentId, language)
    }

    // Function to get cached AI tip synchronously from SharedPreferences (fast, no ANR)
    fun getCachedAiTipFromPrefs(): Tip? {
        val sharedPrefs = context.getSharedPreferences("CodeSnackWidgetPrefs", Context.MODE_PRIVATE)
        val title = sharedPrefs.getString("cached_ai_tip_title", null)
        val code = sharedPrefs.getString("cached_ai_tip_code", null)
        val explanation = sharedPrefs.getString("cached_ai_tip_explanation", null)
        val lang = sharedPrefs.getString("cached_ai_tip_language", null)
        val category = sharedPrefs.getString("cached_ai_tip_category", null)

        return if (title != null && code != null && explanation != null) {
            Log.d("TipProvider", "Retrieved cached AI tip from prefs: $title")
            Tip(
                title = title,
                code = code,
                explanation = explanation ?: "",
                language = lang ?: "ALL",
                category = category ?: "TIP",
                isAiGenerated = true
            )
        } else {
            null
        }
    }

    // Separate function for background AI generation (not used in widget refresh)
    suspend fun getNextTipWithAI(currentId: Int, language: String?, forceGenerate: Boolean = false): Tip {
        val useAiTip = shouldUseAiTip(forceGenerate)

        return if (useAiTip) {
            Log.d("TipProvider", "Using AI-generated tip")
            getAiTip(language, forceGenerate) ?: getStaticTip(currentId, language)
        } else {
            Log.d("TipProvider", "Using static tip")
            getStaticTip(currentId, language)
        }
    }

    private fun shouldUseAiTip(forceGenerate: Boolean): Boolean {
        if (forceGenerate) return true
        if (!prefs.isAiEnabled()) return false
        return Random.nextFloat() < AI_TIP_PROBABILITY
    }

    private suspend fun getAiTip(language: String?, forceGenerate: Boolean): Tip? {
        // Use API key from preferences if set, otherwise fall back to BuildConfig
        val apiKey = prefs.getGeminiApiKey()
        val geminiService = GeminiService(context, apiKey)

        return try {
            val aiTip = geminiService.getOrGenerateTip(language, forceGenerate)
            aiTip?.let {
                Tip(
                    title = it.title,
                    code = it.code,
                    explanation = it.explanation,
                    language = it.language,
                    category = it.category,
                    isAiGenerated = true
                )
            }
        } catch (e: Exception) {
            Log.e("TipProvider", "Error getting AI tip", e)
            null
        }
    }

    private fun getStaticTip(currentId: Int, language: String?): Tip {
        val snippet = SnippetRepository.getNextSnippet(currentId, language)
        return snippetToTip(snippet)
    }

    fun getRandomTip(language: String?): Tip {
        val snippet = SnippetRepository.getRandomSnippetByLanguage(language)
        return snippetToTip(snippet)
    }

    fun snippetToTip(snippet: CodeSnippet): Tip {
        return Tip(
            title = snippet.title,
            code = snippet.code,
            explanation = snippet.explanation,
            language = snippet.language.displayName,
            category = snippet.category.displayName,
            isAiGenerated = false
        )
    }

    fun getCurrentTip(snippetId: Int, language: String?): Tip {
        val snippet = SnippetRepository.getSnippetByIdAndLanguage(snippetId, language)
            ?: SnippetRepository.getRandomSnippetByLanguage(language)
        return snippetToTip(snippet)
    }
}
