package com.example.codesnack

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.glassmorphism(isDarkTheme: Boolean = isSystemInDarkTheme()): Modifier {
    return this
        .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Black.copy(alpha = 0.1f),
            spotColor = Color.Black.copy(alpha = 0.1f)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(
            if (isDarkTheme) {
                // Dark theme: more visible with stronger gradient
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF242427),
                        Color(0xFF1C1C1E)
                    )
                )
            } else {
                // Light theme: solid white with slight transparency for glass effect
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFCFCFC)
                    )
                )
            }
        )
}

@Composable
fun Modifier.searchGlassmorphism(isDarkTheme: Boolean = isSystemInDarkTheme()): Modifier {
    return this
        .shadow(
            elevation = 6.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = Color.Black.copy(alpha = 0.15f),
            spotColor = Color.Black.copy(alpha = 0.15f)
        )
        .clip(RoundedCornerShape(16.dp))
        .background(
            if (isDarkTheme) {
                // Dark theme: more prominent background
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2C2C2E),
                        Color(0xFF242427)
                    )
                )
            } else {
                // Light theme: pure white with subtle shadow for depth
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFAFAFA)
                    )
                )
            }
        )
        .border(
            width = 0.5.dp,
            color = if (isDarkTheme) {
                Color.White.copy(alpha = 0.1f)
            } else {
                Color.Black.copy(alpha = 0.08f)
            },
            shape = RoundedCornerShape(16.dp)
        )
}

@Composable
fun getBackgroundColor(isDarkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (isDarkTheme) Color(0xFF0A0A0A) else Color(0xFFF5F5F7)
}

@Composable
fun getTextColor(isDarkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (isDarkTheme) Color(0xFFFFFFFF) else Color(0xFF1C1C1E)
}

@Composable
fun getSecondaryTextColor(isDarkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (isDarkTheme) Color(0xFF8E8E93) else Color(0xFF6E6E73)
}

@Composable
fun getCodeBackgroundColor(isDarkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if (isDarkTheme) {
        Color.White.copy(alpha = 0.05f)
    } else {
        Color.Black.copy(alpha = 0.05f)
    }
}