package com.example.codesnack.playground

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Data models for Judge0 API
data class SubmissionRequest(
    val source_code: String,
    val language_id: Int,
    val stdin: String = "",
    val expected_output: String? = null
)

data class SubmissionResponse(
    val token: String
)

data class SubmissionResult(
    val stdout: String?,
    val stderr: String?,
    val compile_output: String?,
    val message: String?,
    val status: Status,
    val time: String?,
    val memory: Int?
)

data class Status(
    val id: Int,
    val description: String
)

// Retrofit interface for Judge0 API
interface Judge0ApiService {
    @Headers("Content-Type: application/json")
    @POST("submissions?base64_encoded=false&wait=true")
    suspend fun submitCode(
        @Body submission: SubmissionRequest
    ): SubmissionResult

    @GET("submissions/{token}")
    suspend fun getSubmission(
        @Path("token") token: String,
        @Query("base64_encoded") base64Encoded: Boolean = false
    ): SubmissionResult
}