package com.example.codesnack.model

import com.example.codesnack.data.AiTip

/**
 * Unified data class that represents both static tips and AI-generated tips
 */
data class UnifiedTip(
    val id: String,  // Use String to differentiate between static ("static_1") and AI ("ai_1") tips
    val language: ProgrammingLanguage,
    val title: String,
    val code: String,
    val explanation: String,
    val category: SnippetCategory,
    val isAiGenerated: Boolean,
    val generatedAt: Long? = null  // Only for AI tips
) {
    companion object {
        // Convert static CodeSnippet to UnifiedTip
        fun fromCodeSnippet(snippet: CodeSnippet): UnifiedTip {
            return UnifiedTip(
                id = "static_${snippet.id}",
                language = snippet.language,
                title = snippet.title,
                code = snippet.code,
                explanation = snippet.explanation,
                category = snippet.category,
                isAiGenerated = false,
                generatedAt = null
            )
        }

        // Convert AI tip to UnifiedTip
        fun fromAiTip(aiTip: AiTip): UnifiedTip {
            // Map language string to ProgrammingLanguage enum
            val language = try {
                ProgrammingLanguage.entries.find { it.displayName.equals(aiTip.language, ignoreCase = true) }
                    ?: ProgrammingLanguage.entries.find { it.name.equals(aiTip.language, ignoreCase = true) }
                    ?: ProgrammingLanguage.KOTLIN // Default fallback
            } catch (e: Exception) {
                ProgrammingLanguage.KOTLIN // Default fallback
            }

            // Map category string to SnippetCategory enum
            val category = try {
                SnippetCategory.valueOf(aiTip.category.uppercase())
            } catch (e: Exception) {
                SnippetCategory.TIP // Default fallback
            }

            return UnifiedTip(
                id = "ai_${aiTip.id}",
                language = language,
                title = aiTip.title,
                code = aiTip.code,
                explanation = aiTip.explanation,
                category = category,
                isAiGenerated = true,
                generatedAt = aiTip.generatedAt
            )
        }
    }
}