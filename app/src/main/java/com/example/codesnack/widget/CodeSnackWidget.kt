package com.example.codesnack.widget

import android.content.Context
import android.util.Log
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
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.layout.wrapContentSize
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

        Log.d("CodeSnackWidget", "Loading snippet with ID: $currentSnippetId")

        // Get the snippet by ID, or fallback to snippet with ID 1 if not found
        val snippet = SnippetRepository.getSnippetById(currentSnippetId)
            ?: SnippetRepository.getSnippetById(1)
            ?: SnippetRepository.getRandomSnippet()

        Log.d("CodeSnackWidget", "Displaying snippet: ${snippet.id} - ${snippet.title}")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xCC1A1A1A))) // Dark frosted glass
                .cornerRadius(20.dp)
                .padding(10.dp)
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
                        .padding(bottom = 6.dp),
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    // Language badge
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(getLanguageColorGlass(snippet)))
                            .cornerRadius(12.dp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.language.displayName,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorProvider(Color.White)
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    // Category badge
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(Color(0x40FFFFFF))) // Frosted white overlay
                            .cornerRadius(12.dp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.category.displayName,
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(Color(0xFFE0E0E0))
                            )
                        )
                    }
                }

                // Title
                Text(
                    text = snippet.title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(Color(0xFFF5F5F5))
                    ),
                    modifier = GlanceModifier.padding(bottom = 6.dp),
                    maxLines = 2
                )

                // Code block
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(ColorProvider(Color(0x33000000))) // Darker glass layer
                        .cornerRadius(14.dp)
                        .padding(10.dp)
                ) {
                    Text(
                        text = snippet.code,
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(Color(0xFF64B5F6)) // Bright cyan-blue
                        ),
                        maxLines = 5
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Explanation
                Text(
                    text = snippet.explanation,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color(0xFFBDBDBD))
                    ),
                    modifier = GlanceModifier.padding(bottom = 2.dp),
                    maxLines = 3
                )
            

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Footer - tap to refresh hint
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = "✨ Tap to refresh",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(Color(0x99FFFFFF))
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

    private fun getLanguageColorGlass(snippet: CodeSnippet): Color {
        // Vibrant semi-transparent glass versions with better opacity for dark theme
        return when (snippet.language) {
            com.example.codesnack.model.ProgrammingLanguage.KOTLIN -> Color(0x4D7F52FF)
            com.example.codesnack.model.ProgrammingLanguage.PYTHON -> Color(0x4D3776AB)
            com.example.codesnack.model.ProgrammingLanguage.JAVASCRIPT -> Color(0x66F7DF1E)
            com.example.codesnack.model.ProgrammingLanguage.JAVA -> Color(0x59ED8B00)
            com.example.codesnack.model.ProgrammingLanguage.CPP -> Color(0x4D00599C)
            com.example.codesnack.model.ProgrammingLanguage.SWIFT -> Color(0x59FA7343)
            com.example.codesnack.model.ProgrammingLanguage.RUST -> Color(0x59CE422B)
            com.example.codesnack.model.ProgrammingLanguage.GO -> Color(0x4D00ADD8)
        }
    }
}
