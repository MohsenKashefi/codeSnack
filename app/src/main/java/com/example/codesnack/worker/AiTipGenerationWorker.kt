package com.example.codesnack.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.service.GeminiService
import com.example.codesnack.widget.WidgetPreferences
import kotlinx.coroutines.delay

class AiTipGenerationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        private const val TIPS_TO_GENERATE = 5 // Generate 5 tips per run
        private const val DELAY_BETWEEN_GENERATIONS_MS = 2000L // 2 seconds between API calls
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d("AiTipGenerationWorker", "Starting AI tip pre-generation")

            val prefs = WidgetPreferences(applicationContext)

            // Only generate if AI is enabled
            if (!prefs.isAiEnabled()) {
                Log.d("AiTipGenerationWorker", "AI not enabled, skipping generation")
                return Result.success()
            }

            val geminiService = GeminiService(applicationContext, prefs.getGeminiApiKey())

            // Get selected language or generate for all languages
            val selectedLanguage = prefs.getSelectedLanguage()
            val languagesToGenerate = if (selectedLanguage != null) {
                listOf(selectedLanguage)
            } else {
                // Generate tips for popular languages
                listOf("Kotlin", "Python", "JavaScript", "Java")
            }

            var successCount = 0
            var failureCount = 0

            for (language in languagesToGenerate) {
                // Check if we already have enough cached tips for this language
                val cachedCount = geminiService.getTipCountByLanguage(language)
                if (cachedCount >= 10) {
                    Log.d("AiTipGenerationWorker", "Already have $cachedCount tips for $language, skipping")
                    continue
                }

                // Generate tips for this language
                val tipsNeeded = (TIPS_TO_GENERATE - (10 - cachedCount)).coerceAtLeast(1)
                Log.d("AiTipGenerationWorker", "Generating $tipsNeeded tips for $language")

                repeat(tipsNeeded) { index ->
                    try {
                        val tip = geminiService.generateTip(language)
                        if (tip != null) {
                            successCount++
                            Log.d("AiTipGenerationWorker", "Generated tip ${index + 1}/$tipsNeeded for $language: ${tip.title}")

                            // Cache the latest tip in SharedPreferences for instant widget access
                            val sharedPrefs = applicationContext.getSharedPreferences("CodeSnackWidgetPrefs", Context.MODE_PRIVATE)
                            sharedPrefs.edit().apply {
                                putString("cached_ai_tip_title", tip.title)
                                putString("cached_ai_tip_code", tip.code)
                                putString("cached_ai_tip_explanation", tip.explanation)
                                putString("cached_ai_tip_language", tip.language)
                                putString("cached_ai_tip_category", tip.category)
                                putLong("cached_ai_tip_timestamp", System.currentTimeMillis())
                                apply()
                            }
                            Log.d("AiTipGenerationWorker", "Cached AI tip in SharedPreferences for widget")
                        } else {
                            failureCount++
                            Log.w("AiTipGenerationWorker", "Failed to generate tip ${index + 1}/$tipsNeeded for $language")
                        }

                        // Delay between API calls to avoid rate limiting
                        if (index < tipsNeeded - 1) {
                            delay(DELAY_BETWEEN_GENERATIONS_MS)
                        }
                    } catch (e: Exception) {
                        failureCount++
                        Log.e("AiTipGenerationWorker", "Error generating tip for $language", e)
                    }
                }

                // Delay between languages
                if (language != languagesToGenerate.last()) {
                    delay(DELAY_BETWEEN_GENERATIONS_MS)
                }
            }

            Log.d("AiTipGenerationWorker", "AI tip generation completed: $successCount success, $failureCount failures")

            // Consider it successful if we generated at least one tip
            if (successCount > 0) {
                Result.success()
            } else if (failureCount > 0) {
                // Retry if all attempts failed
                Result.retry()
            } else {
                // Nothing needed to be done
                Result.success()
            }
        } catch (e: Exception) {
            Log.e("AiTipGenerationWorker", "Unexpected error during AI tip generation", e)
            Result.retry()
        }
    }
}