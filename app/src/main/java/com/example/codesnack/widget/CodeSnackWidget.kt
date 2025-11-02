package com.example.codesnack.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.model.CodeSnippet

class CodeSnackWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent(context)
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context) {
        val prefs = WidgetPreferences(context)
        val currentSnippetId = prefs.getCurrentSnippetId()
        val snippet = SnippetRepository.getSnippetById(currentSnippetId)
            ?: SnippetRepository.getRandomSnippet()

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFF1E1E1E)))
                .cornerRadius(16.dp)
                .padding(12.dp)
        ) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .clickable(actionRunCallback<RefreshWidgetAction>()),
                verticalAlignment = Alignment.Vertical.Top,
                horizontalAlignment = Alignment.Horizontal.Start
            ) {
                // Header with language and category
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    // Language badge
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(getLanguageColor(snippet)))
                            .cornerRadius(8.dp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.language.displayName,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    // Category badge
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(Color(0xFF37474F)))
                            .cornerRadius(8.dp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.category.displayName,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(Color(0xFFB0BEC5))
                            )
                        )
                    }
                }

                // Title
                Text(
                    text = snippet.title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(Color(0xFFE0E0E0))
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp)
                )

                // Code block
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(ColorProvider(Color(0xFF2D2D2D)))
                        .cornerRadius(8.dp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = snippet.code,
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = ColorProvider(Color(0xFF4FC3F7))
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Explanation
                Text(
                    text = snippet.explanation,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color(0xFFB0BEC5))
                    ),
                    modifier = GlanceModifier.padding(bottom = 6.dp)
                )

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Footer - tap to refresh hint
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = "👆 Tap to see next tip",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = ColorProvider(Color(0xFF757575))
                        )
                    )
                }
            }
        }
    }

    private fun getLanguageColor(snippet: CodeSnippet): Color {
        return when (snippet.language) {
            com.example.codesnack.model.ProgrammingLanguage.KOTLIN -> Color(0xFF7F52FF)
            com.example.codesnack.model.ProgrammingLanguage.PYTHON -> Color(0xFF3776AB)
            com.example.codesnack.model.ProgrammingLanguage.JAVASCRIPT -> Color(0xFFF7DF1E)
            com.example.codesnack.model.ProgrammingLanguage.JAVA -> Color(0xFFED8B00)
            com.example.codesnack.model.ProgrammingLanguage.CPP -> Color(0xFF00599C)
            com.example.codesnack.model.ProgrammingLanguage.SWIFT -> Color(0xFFFA7343)
            com.example.codesnack.model.ProgrammingLanguage.RUST -> Color(0xFFCE422B)
            com.example.codesnack.model.ProgrammingLanguage.GO -> Color(0xFF00ADD8)
        }
    }
}
