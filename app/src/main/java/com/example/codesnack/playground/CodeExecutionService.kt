package com.example.codesnack.playground

import android.util.Log
import com.example.codesnack.BuildConfig
import com.example.codesnack.model.ProgrammingLanguage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class CodeExecutionService {

    // RapidAPI Key - Get free key at https://rapidapi.com/judge0-official/api/judge0-ce
    // Free tier: 50 requests/day
    // Key is loaded from .env file via BuildConfig
    private val RAPIDAPI_KEY = BuildConfig.RAPIDAPI_KEY

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key", RAPIDAPI_KEY)
                .addHeader("X-RapidAPI-Host", "judge0-ce.p.rapidapi.com")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://judge0-ce.p.rapidapi.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(Judge0ApiService::class.java)

    // Map ProgrammingLanguage to Judge0 language IDs
    private fun getLanguageId(language: ProgrammingLanguage): Int {
        return when (language) {
            ProgrammingLanguage.KOTLIN -> 78  // Kotlin 1.3.70
            ProgrammingLanguage.PYTHON -> 71   // Python 3.8.1
            ProgrammingLanguage.JAVASCRIPT -> 63 // JavaScript (Node.js 12.14.0)
            ProgrammingLanguage.JAVA -> 62     // Java (OpenJDK 13.0.1)
            ProgrammingLanguage.CPP -> 54      // C++ (GCC 9.2.0)
            ProgrammingLanguage.SWIFT -> 83    // Swift 5.2.3
            ProgrammingLanguage.RUST -> 73     // Rust 1.40.0
            ProgrammingLanguage.GO -> 60       // Go 1.13.5
            ProgrammingLanguage.DART -> 90     // Dart 2.19.2
        }
    }

    suspend fun executeCode(
        code: String,
        language: ProgrammingLanguage,
        input: String = ""
    ): CodeExecutionResult {
        return try {
            Log.d("CodeExecution", "Executing ${language.displayName} code...")

            val submission = SubmissionRequest(
                source_code = code,
                language_id = getLanguageId(language),
                stdin = input
            )

            val result = api.submitCode(submission)

            when (result.status.id) {
                3 -> { // Accepted
                    CodeExecutionResult.Success(
                        output = result.stdout?.trim() ?: "",
                        executionTime = result.time ?: "0",
                        memory = result.memory ?: 0
                    )
                }
                4 -> { // Wrong Answer (but still ran)
                    CodeExecutionResult.Success(
                        output = result.stdout?.trim() ?: "",
                        executionTime = result.time ?: "0",
                        memory = result.memory ?: 0
                    )
                }
                5 -> { // Time Limit Exceeded
                    CodeExecutionResult.Error("Time limit exceeded")
                }
                6 -> { // Compilation Error
                    CodeExecutionResult.Error(
                        result.compile_output?.trim() ?: "Compilation failed"
                    )
                }
                7, 8, 9, 10, 11, 12 -> { // Runtime Error, etc.
                    CodeExecutionResult.Error(
                        result.stderr?.trim() ?: result.message ?: "Runtime error occurred"
                    )
                }
                13 -> { // Internal Error
                    CodeExecutionResult.Error("Internal error occurred. Please try again.")
                }
                14 -> { // Exec Format Error
                    CodeExecutionResult.Error("Execution format error")
                }
                else -> {
                    CodeExecutionResult.Error("Unknown error: ${result.status.description}")
                }
            }
        } catch (e: Exception) {
            Log.e("CodeExecution", "Error executing code", e)
            CodeExecutionResult.Error("Failed to execute code: ${e.message}")
        }
    }
}

sealed class CodeExecutionResult {
    data class Success(
        val output: String,
        val executionTime: String,
        val memory: Int
    ) : CodeExecutionResult()

    data class Error(val message: String) : CodeExecutionResult()
}
