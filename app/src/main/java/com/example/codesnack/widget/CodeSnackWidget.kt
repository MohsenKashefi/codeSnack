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
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
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
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.datastore.preferences.core.intPreferencesKey
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
        // Use Glance state to track snippet ID - this forces recomposition!
        val prefs = currentState<androidx.datastore.preferences.core.Preferences>()
        val currentSnippetId = prefs[intPreferencesKey("current_snippet_id")] ?: 1

        Log.d("CodeSnackWidget", "WidgetContent recomposing - Loading snippet with ID: $currentSnippetId")

        // Get the snippet by ID, or fallback to snippet with ID 1 if not found
        val snippet = SnippetRepository.getSnippetById(currentSnippetId)
            ?: SnippetRepository.getSnippetById(1)
            ?: SnippetRepository.getRandomSnippet()

        Log.d("CodeSnackWidget", "Displaying snippet: ${snippet.id} - ${snippet.title}")

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xF0F0F0F0))) // iOS light mode background
                .cornerRadius(24.dp)
                .padding(16.dp)

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
                    // Language badge - iOS style
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(getLanguageColor(snippet)))
                            .cornerRadius(8.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.language.displayName,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(Color.White),
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    // Category badge - iOS subtle style
                    Box(
                        modifier = GlanceModifier
                            .background(ColorProvider(Color(0x1A000000))) // iOS subtle overlay
                            .cornerRadius(8.dp)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = snippet.category.displayName,
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorProvider(Color(0xFF666666)),
                                fontFamily = FontFamily.SansSerif
                            )
                        )
                    }
                }

                // Title - iOS style
                Text(
                    text = snippet.title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(Color(0xFF000000)),
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = GlanceModifier.padding(bottom = 8.dp),
                    maxLines = 2
                )

                // Code block - iOS monospace style
                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(ColorProvider(Color(0xFFF5F5F7))) // iOS light code background
                        .cornerRadius(12.dp)
                        .padding(12.dp)
                ) {
                    Text(
                        text = snippet.code,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = ColorProvider(Color(0xFF007AFF)), // iOS blue
                            fontFamily = FontFamily.Monospace
                        ),
                        maxLines = 5
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Explanation - iOS secondary text
                Text(
                    text = snippet.explanation,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = ColorProvider(Color(0xFF8E8E93)), // iOS secondary label
                        fontFamily = FontFamily.SansSerif
                    ),
                    modifier = GlanceModifier.padding(bottom = 4.dp),
                    maxLines = 3
                )
            

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Footer - iOS style tap hint
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    Text(
                        text = "Tap for next tip",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = ColorProvider(Color(0xFFC7C7CC)), // iOS tertiary label
                            fontFamily = FontFamily.SansSerif
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
