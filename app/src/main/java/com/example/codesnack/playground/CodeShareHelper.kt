package com.example.codesnack.playground

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object CodeShareHelper {

    fun shareCodeAsImage(
        context: Context,
        code: String,
        language: String,
        onError: (String) -> Unit
    ) {
        try {
            // Create bitmap
            val bitmap = createCodeBitmap(code, language)

            // Save to cache
            val file = File(context.cacheDir, "shared_code_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            // Share
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Code snippet in $language - Made with CodeSnack")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share Code"))

        } catch (e: Exception) {
            onError("Failed to share code: ${e.message}")
        }
    }

    private fun createCodeBitmap(code: String, language: String): Bitmap {
        val width = 1200
        val padding = 60
        val lineHeight = 45
        val headerHeight = 120

        val lines = code.lines()
        val height = headerHeight + (lines.size * lineHeight) + padding * 2

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background
        val bgPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#1E1E1E")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Header background
        val headerBgPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#2D2D2D")
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), headerHeight.toFloat(), headerBgPaint)

        // Language badge
        val badgePaint = Paint().apply {
            color = android.graphics.Color.parseColor("#7F52FF")
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(
            padding.toFloat(),
            40f,
            (padding + 150).toFloat(),
            80f,
            15f,
            15f,
            badgePaint
        )

        // Language text
        val languageTextPaint = Paint().apply {
            color = Color.WHITE
            textSize = 32f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
        }
        canvas.drawText(language, (padding + 20).toFloat(), 68f, languageTextPaint)

        // "CodeSnack" watermark
        val watermarkPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#9E9E9E")
            textSize = 28f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            isAntiAlias = true
        }
        canvas.drawText("CodeSnack", (width - padding - 180).toFloat(), 68f, watermarkPaint)

        // Code text
        val codePaint = Paint().apply {
            color = android.graphics.Color.parseColor("#4FC3F7")
            textSize = 30f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
            isAntiAlias = true
        }

        var y = headerHeight + padding + 30
        lines.forEach { line ->
            canvas.drawText(line, padding.toFloat(), y.toFloat(), codePaint)
            y += lineHeight
        }

        return bitmap
    }
}