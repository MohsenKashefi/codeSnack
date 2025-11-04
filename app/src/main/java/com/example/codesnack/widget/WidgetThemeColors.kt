package com.example.codesnack.widget

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.codesnack.model.WidgetTheme

data class ThemeColors(
    val background: Color,
    val backgroundGradient: Brush?,
    val titleColor: Color,
    val codeBackground: Color,
    val codeTextColor: Color,
    val explanationColor: Color,
    val badgeOverlay: Color,
    val categoryBadgeBackground: Color,
    val categoryBadgeText: Color,
    val tapHintColor: Color
)

fun getThemeColors(theme: WidgetTheme): ThemeColors {
    return when (theme) {
        WidgetTheme.IOS_LIGHT -> ThemeColors(
            background = Color(0xFFF0F0F0),
            backgroundGradient = null,
            titleColor = Color(0xFF000000),
            codeBackground = Color(0xFFF5F5F7),
            codeTextColor = Color(0xFF007AFF),
            explanationColor = Color(0xFF8E8E93),
            badgeOverlay = Color(0x00000000),
            categoryBadgeBackground = Color(0x1A000000),
            categoryBadgeText = Color(0xFF666666),
            tapHintColor = Color(0xFFC7C7CC)
        )

        WidgetTheme.DARK_MODE -> ThemeColors(
            background = Color(0xFF1E1E1E),
            backgroundGradient = null,
            titleColor = Color(0xFFE0E0E0),
            codeBackground = Color(0xFF2D2D2D),
            codeTextColor = Color(0xFF4FC3F7),
            explanationColor = Color(0xFFB0BEC5),
            badgeOverlay = Color(0x00000000),
            categoryBadgeBackground = Color(0xFF37474F),
            categoryBadgeText = Color(0xFFB0BEC5),
            tapHintColor = Color(0xFF757575)
        )

        WidgetTheme.GRADIENT_PURPLE -> ThemeColors(
            background = Color(0xFF5A48B0), // Mid-point of gradient for widget use
            backgroundGradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF6B46C1),
                    Color(0xFF4C51BF),
                    Color(0xFF4299E1)
                )
            ),
            titleColor = Color.White,
            codeBackground = Color(0x33FFFFFF),
            codeTextColor = Color(0xFFFFFFFF),
            explanationColor = Color(0xE6FFFFFF),
            badgeOverlay = Color(0x00000000),
            categoryBadgeBackground = Color(0x33FFFFFF),
            categoryBadgeText = Color(0xFFFFFFFF),
            tapHintColor = Color(0xCCFFFFFF)
        )

        WidgetTheme.OCEAN_BLUE -> ThemeColors(
            background = Color(0xFF0893D1), // Mid-point of gradient for widget use
            backgroundGradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0EA5E9),
                    Color(0xFF0284C7),
                    Color(0xFF0369A1)
                )
            ),
            titleColor = Color.White,
            codeBackground = Color(0x26FFFFFF),
            codeTextColor = Color(0xFFFFFFFF),
            explanationColor = Color(0xE6FFFFFF),
            badgeOverlay = Color(0x00000000),
            categoryBadgeBackground = Color(0x26FFFFFF),
            categoryBadgeText = Color(0xFFFFFFFF),
            tapHintColor = Color(0xCCFFFFFF)
        )
    }
}