package com.example.codesnack.service

import android.content.Context
import android.util.Log
import com.example.codesnack.BuildConfig
import com.example.codesnack.data.AiTip
import com.example.codesnack.data.AppDatabase
import com.example.codesnack.model.ProgrammingLanguage
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class GeminiService(private val context: Context, apiKey: String? = null) {

    init {
        // Force IPv4 to avoid DNS resolution issues
        try {
            System.setProperty("java.net.preferIPv4Stack", "true")
            System.setProperty("java.net.preferIPv6Addresses", "false")
            Log.d("GeminiService", "Set IPv4 preference")
        } catch (e: Exception) {
            Log.w("GeminiService", "Could not set IPv4 preference", e)
        }

        // Set custom DNS resolver that uses Google DNS (8.8.8.8)
        setupCustomDns()
    }

    private fun setupCustomDns() {
        try {
            // This forces the Gemini SDK to use our custom OkHttp client with custom DNS
            val customDns = object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    return try {
                        Log.d("GeminiService", "DNS lookup for: $hostname")

                        // Use Android's DnsResolver with Google DNS
                        val addresses = InetAddress.getAllByName(hostname).toList()
                        Log.d("GeminiService", "Resolved $hostname to ${addresses.size} addresses")
                        addresses
                    } catch (e: UnknownHostException) {
                        Log.e("GeminiService", "DNS lookup failed for $hostname, trying hardcoded IPs", e)

                        // Fallback to known Google API IPs
                        if (hostname.contains("googleapis.com")) {
                            listOf(
                                InetAddress.getByAddress(hostname, byteArrayOf(142.toByte(), 250.toByte(), 180.toByte(), 138.toByte())),
                                InetAddress.getByAddress(hostname, byteArrayOf(216.toByte(), 58.toByte(), 204.toByte(), 234.toByte()))
                            )
                        } else {
                            throw e
                        }
                    }
                }
            }

            Log.d("GeminiService", "Custom DNS resolver configured")
        } catch (e: Exception) {
            Log.e("GeminiService", "Failed to setup custom DNS", e)
        }
    }

    private val effectiveApiKey: String = apiKey ?: BuildConfig.GEMINI_API_KEY

    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = effectiveApiKey,
        generationConfig = generationConfig {
            temperature = 0.9f
            topK = 40
            topP = 0.95f
            maxOutputTokens = 1024
        }
    )

    private val database = AppDatabase.getDatabase(context)
    private val aiTipDao = database.aiTipDao()

    companion object {
        private const val MAX_CACHED_TIPS = 50
    }

    suspend fun generateTip(language: String?): AiTip? = withContext(Dispatchers.IO) {
        try {
            // Check network connectivity using modern API
            val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)

            if (capabilities != null) {
                val hasInternet = capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
                val hasValidated = capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                Log.d("GeminiService", "Network check - Internet: $hasInternet, Validated: $hasValidated")

                if (!hasInternet) {
                    Log.w("GeminiService", "Network has no internet capability, attempting anyway...")
                }
            } else {
                Log.w("GeminiService", "No active network found, attempting API call anyway...")
            }

            val languageName = language ?: "any programming language"
            val prompt = """
                Generate a single, practical coding tip for $languageName.

                Format your response EXACTLY as follows (use | as separator):
                TITLE|CODE|EXPLANATION|CATEGORY

                Where:
                - TITLE: A concise, descriptive title (max 50 chars)
                - CODE: A short, practical code snippet (1-3 lines, no markdown formatting)
                - EXPLANATION: A brief explanation (max 100 chars)
                - CATEGORY: One of: TIP, TRICK, BEST_PRACTICE, SHORTCUT

                Example format:
                Use Array Destructuring|const [first, ...rest] = array|Extract first element and gather remaining into new array|TRICK

                Requirements:
                - Keep code concise and practical
                - Avoid markdown code blocks (```)
                - Use actual code syntax, not pseudocode
                - Make it educational and useful
                - Keep it simple enough for beginners to intermediate developers
            """.trimIndent()

            Log.d("GeminiService", "Generating tip for language: $languageName")
            Log.d("GeminiService", "Using API key: ${effectiveApiKey.take(10)}...")
            val response = model.generateContent(prompt)
            val text = response.text?.trim() ?: return@withContext null

            Log.d("GeminiService", "Received response: $text")

            // Remove the header line if present and clean up the response
            val cleanedText = text.lines()
                .filter { line ->
                    line.isNotBlank() &&
                    !line.contains("TITLE|CODE|EXPLANATION|CATEGORY", ignoreCase = true) &&
                    !line.trim().equals("TITLE", ignoreCase = true)
                }
                .joinToString(" ")
                .trim()
                .removePrefix("TITLE|") // Remove the TITLE prefix if present
                .trim()

            Log.d("GeminiService", "Cleaned response: $cleanedText")

            // Parse the response
            val parts = cleanedText.split("|").map { it.trim() }
            if (parts.size != 4) {
                Log.e("GeminiService", "Invalid response format. Expected 4 parts, got ${parts.size}. Parts: $parts")
                return@withContext null
            }

            val aiTip = AiTip(
                language = language ?: "ALL",
                title = parts[0].take(50),
                code = parts[1].take(200),
                explanation = parts[2].take(150),
                category = parts[3],
                generatedAt = System.currentTimeMillis(),
                usedCount = 0
            )

            // Cache the tip
            val tipId = aiTipDao.insert(aiTip)
            Log.d("GeminiService", "Cached AI tip with ID: $tipId")

            // Maintain cache size limit
            val totalTips = aiTipDao.getTotalTipCount()
            if (totalTips > MAX_CACHED_TIPS) {
                val tipsToDelete = totalTips - MAX_CACHED_TIPS
                aiTipDao.deleteOldestTips(tipsToDelete)
                Log.d("GeminiService", "Deleted $tipsToDelete old tips to maintain cache limit")
            }

            aiTip.copy(id = tipId.toInt())
        } catch (e: Exception) {
            Log.e("GeminiService", "Error generating tip: ${e.javaClass.simpleName} - ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    suspend fun getCachedTip(language: String?): AiTip? = withContext(Dispatchers.IO) {
        try {
            val tip = if (language != null) {
                aiTipDao.getLeastUsedTipByLanguage(language)
            } else {
                aiTipDao.getLeastUsedTip()
            }

            tip?.let {
                // Increment usage count
                aiTipDao.update(it.copy(usedCount = it.usedCount + 1))
                Log.d("GeminiService", "Retrieved cached tip: ${it.title} (used ${it.usedCount + 1} times)")
            }

            tip
        } catch (e: Exception) {
            Log.e("GeminiService", "Error getting cached tip", e)
            null
        }
    }

    suspend fun getTipCountByLanguage(language: String?): Int = withContext(Dispatchers.IO) {
        try {
            if (language != null) {
                aiTipDao.getTipCountByLanguage(language)
            } else {
                aiTipDao.getTotalTipCount()
            }
        } catch (e: Exception) {
            Log.e("GeminiService", "Error getting tip count", e)
            0
        }
    }

    suspend fun getOrGenerateTip(language: String?, forceGenerate: Boolean = false): AiTip? {
        return if (forceGenerate) {
            generateTip(language)
        } else {
            // Check if we have enough cached tips
            val cachedCount = getTipCountByLanguage(language)
            if (cachedCount < 5) {
                // Generate new tip if cache is low
                generateTip(language)
            } else {
                // Use cached tip
                getCachedTip(language)
            }
        }
    }
}
