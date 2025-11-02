package com.example.codesnack.model

data class CodeSnippet(
    val id: Int,
    val language: ProgrammingLanguage,
    val title: String,
    val code: String,
    val explanation: String,
    val category: SnippetCategory
)

enum class ProgrammingLanguage(val displayName: String) {
    KOTLIN("Kotlin"),
    PYTHON("Python"),
    JAVASCRIPT("JavaScript"),
    JAVA("Java"),
    CPP("C++"),
    SWIFT("Swift"),
    RUST("Rust"),
    GO("Go")
}

enum class SnippetCategory(val displayName: String) {
    TIP("Tip"),
    TRICK("Trick"),
    SHORTCUT("Shortcut"),
    BEST_PRACTICE("Best Practice"),
    GOTCHA("Gotcha")
}
