package com.example.codesnack.data

import com.example.codesnack.model.CodeSnippet
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.model.SnippetCategory

object SnippetRepository {

    private val snippets = listOf(
        // Kotlin snippets
        CodeSnippet(
            id = 1,
            language = ProgrammingLanguage.KOTLIN,
            title = "Elvis Operator for Null Safety",
            code = "val name = user?.name ?: \"Unknown\"",
            explanation = "Use ?: to provide a default value when the left side is null",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 2,
            language = ProgrammingLanguage.KOTLIN,
            title = "Destructuring Declarations",
            code = "val (name, age) = person",
            explanation = "Destructure data classes into multiple variables in one line",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 3,
            language = ProgrammingLanguage.KOTLIN,
            title = "Scope Functions: apply",
            code = "val person = Person().apply {\n  name = \"John\"\n  age = 30\n}",
            explanation = "Use apply for object configuration - returns the object itself",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 4,
            language = ProgrammingLanguage.KOTLIN,
            title = "Smart Casts",
            code = "if (x is String) {\n  println(x.length) // auto-cast!\n}",
            explanation = "Kotlin automatically casts after type checking",
            category = SnippetCategory.TIP
        ),

        // Python snippets
        CodeSnippet(
            id = 5,
            language = ProgrammingLanguage.PYTHON,
            title = "List Comprehension",
            code = "squares = [x**2 for x in range(10)]",
            explanation = "Create lists in a single, readable line instead of loops",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 6,
            language = ProgrammingLanguage.PYTHON,
            title = "Swap Variables",
            code = "a, b = b, a",
            explanation = "Python's tuple unpacking makes swapping elegant",
            category = SnippetCategory.SHORTCUT
        ),
        CodeSnippet(
            id = 7,
            language = ProgrammingLanguage.PYTHON,
            title = "Dictionary Get with Default",
            code = "value = my_dict.get('key', 'default')",
            explanation = "Avoid KeyError by providing a default value",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 8,
            language = ProgrammingLanguage.PYTHON,
            title = "F-Strings for Formatting",
            code = "msg = f\"Hello {name}, you are {age}\"",
            explanation = "F-strings are faster and more readable than .format()",
            category = SnippetCategory.TIP
        ),

        // JavaScript snippets
        CodeSnippet(
            id = 9,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array Destructuring",
            code = "const [first, ...rest] = array",
            explanation = "Extract first element and gather remaining into new array",
            category = SnippetCategory.TRICK
        ),
        CodeSnippet(
            id = 10,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Optional Chaining",
            code = "const city = user?.address?.city",
            explanation = "Safely access nested properties without null checks",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 11,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Nullish Coalescing",
            code = "const value = input ?? 'default'",
            explanation = "Use ?? instead of || to only check for null/undefined",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 12,
            language = ProgrammingLanguage.JAVASCRIPT,
            title = "Array Filter & Map Chain",
            code = "const result = arr\n  .filter(x => x > 0)\n  .map(x => x * 2)",
            explanation = "Chain array methods for readable data transformations",
            category = SnippetCategory.TRICK
        ),

        // Java snippets
        CodeSnippet(
            id = 13,
            language = ProgrammingLanguage.JAVA,
            title = "Streams for Collection Processing",
            code = "list.stream()\n  .filter(x -> x > 0)\n  .collect(Collectors.toList())",
            explanation = "Use streams for declarative collection operations",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 14,
            language = ProgrammingLanguage.JAVA,
            title = "Try-With-Resources",
            code = "try (var reader = new FileReader(file)) {\n  // auto-closes!\n}",
            explanation = "Automatically close resources, preventing leaks",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 15,
            language = ProgrammingLanguage.JAVA,
            title = "Var for Type Inference",
            code = "var list = new ArrayList<String>()",
            explanation = "Use var to reduce verbosity (Java 10+)",
            category = SnippetCategory.SHORTCUT
        ),

        // C++ snippets
        CodeSnippet(
            id = 16,
            language = ProgrammingLanguage.CPP,
            title = "Range-Based For Loop",
            code = "for (const auto& item : vec) {\n  // process item\n}",
            explanation = "Cleaner syntax than iterator loops (C++11)",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 17,
            language = ProgrammingLanguage.CPP,
            title = "Smart Pointers",
            code = "auto ptr = std::make_unique<MyClass>()",
            explanation = "Use smart pointers to avoid manual memory management",
            category = SnippetCategory.TIP
        ),
        CodeSnippet(
            id = 18,
            language = ProgrammingLanguage.CPP,
            title = "Auto Type Deduction",
            code = "auto result = calculate()",
            explanation = "Let compiler deduce types for cleaner code",
            category = SnippetCategory.SHORTCUT
        ),

        // Swift snippets
        CodeSnippet(
            id = 19,
            language = ProgrammingLanguage.SWIFT,
            title = "Guard for Early Exit",
            code = "guard let value = optional else {\n  return\n}",
            explanation = "Guard statements improve code readability",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 20,
            language = ProgrammingLanguage.SWIFT,
            title = "Nil Coalescing",
            code = "let name = username ?? \"Guest\"",
            explanation = "Provide default values for optionals concisely",
            category = SnippetCategory.TIP
        ),

        // Rust snippets
        CodeSnippet(
            id = 21,
            language = ProgrammingLanguage.RUST,
            title = "Pattern Matching",
            code = "match result {\n  Ok(val) => println!(\"{}\", val),\n  Err(e) => eprintln!(\"{}\", e)\n}",
            explanation = "Match is exhaustive and more powerful than if-else",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 22,
            language = ProgrammingLanguage.RUST,
            title = "If Let for Single Pattern",
            code = "if let Some(x) = option {\n  println!(\"{}\", x)\n}",
            explanation = "Cleaner than match for single pattern cases",
            category = SnippetCategory.SHORTCUT
        ),

        // Go snippets
        CodeSnippet(
            id = 23,
            language = ProgrammingLanguage.GO,
            title = "Multiple Return Values",
            code = "result, err := doSomething()\nif err != nil {\n  return err\n}",
            explanation = "Go's idiomatic error handling pattern",
            category = SnippetCategory.BEST_PRACTICE
        ),
        CodeSnippet(
            id = 24,
            language = ProgrammingLanguage.GO,
            title = "Defer for Cleanup",
            code = "defer file.Close()\n// file auto-closes on function exit",
            explanation = "Defer ensures cleanup happens even with early returns",
            category = SnippetCategory.TIP
        )
    )

    fun getRandomSnippet(): CodeSnippet {
        return snippets.random()
    }

    fun getRandomSnippetByLanguage(languageName: String?): CodeSnippet {
        val filteredSnippets = if (languageName != null) {
            snippets.filter { it.language.name == languageName }
        } else {
            snippets
        }
        return filteredSnippets.randomOrNull() ?: snippets.random()
    }

    fun getSnippetsByLanguage(language: ProgrammingLanguage): List<CodeSnippet> {
        return snippets.filter { it.language == language }
    }

    fun getSnippetById(id: Int): CodeSnippet? {
        return snippets.find { it.id == id }
    }

    fun getSnippetByIdAndLanguage(id: Int, languageName: String?): CodeSnippet? {
        val filteredSnippets = if (languageName != null) {
            snippets.filter { it.language.name == languageName }
        } else {
            snippets
        }
        return filteredSnippets.find { it.id == id }
    }

    fun getAllSnippets(): List<CodeSnippet> {
        return snippets
    }

    fun getNextSnippet(currentId: Int, languageName: String? = null): CodeSnippet {
        android.util.Log.d("SnippetRepository", "getNextSnippet called with languageName: $languageName")

        val filteredSnippets = if (languageName != null) {
            val filtered = snippets.filter { it.language.name == languageName }
            android.util.Log.d("SnippetRepository", "Filtered ${filtered.size} snippets for language: $languageName")
            filtered
        } else {
            android.util.Log.d("SnippetRepository", "No filter - using all ${snippets.size} snippets")
            snippets
        }

        if (filteredSnippets.isEmpty()) {
            android.util.Log.w("SnippetRepository", "No snippets found for language: $languageName, falling back to all")
            return snippets.random()
        }

        val currentIndex = filteredSnippets.indexOfFirst { it.id == currentId }
        if (currentIndex == -1) {
            // Current snippet not in filtered list, return first filtered snippet
            android.util.Log.d("SnippetRepository", "Current ID $currentId not in filtered list, returning first")
            return filteredSnippets.firstOrNull() ?: snippets.first()
        }

        val nextIndex = (currentIndex + 1) % filteredSnippets.size
        val nextSnippet = filteredSnippets[nextIndex]
        android.util.Log.d("SnippetRepository", "Returning snippet ${nextSnippet.id}: ${nextSnippet.title} (${nextSnippet.language.name})")
        return nextSnippet
    }

    // Get next snippet from multiple selected languages
    fun getNextSnippetFromLanguages(currentId: Int, languageNames: Set<String>): CodeSnippet {
        android.util.Log.d("SnippetRepository", "getNextSnippetFromLanguages called with languages: $languageNames")

        val filteredSnippets = if (languageNames.isNotEmpty()) {
            val filtered = snippets.filter { it.language.name in languageNames }
            android.util.Log.d("SnippetRepository", "Filtered ${filtered.size} snippets for languages: $languageNames")
            filtered
        } else {
            android.util.Log.d("SnippetRepository", "No filter - using all ${snippets.size} snippets")
            snippets
        }

        if (filteredSnippets.isEmpty()) {
            android.util.Log.w("SnippetRepository", "No snippets found for languages: $languageNames, falling back to all")
            return snippets.random()
        }

        // Return random snippet from filtered list
        return filteredSnippets.random()
    }
}
