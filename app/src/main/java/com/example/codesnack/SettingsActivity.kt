package com.example.codesnack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.model.WidgetTheme
import com.example.codesnack.ui.theme.CodeSnackTheme
import com.example.codesnack.widget.CodeSnackWidget
import com.example.codesnack.widget.WidgetPreferences
import com.example.codesnack.widget.getThemeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = WidgetPreferences(this)
        val currentLanguages = prefs.getSelectedLanguages()
        val currentFrequency = prefs.getUpdateFrequency()
        val currentTheme = prefs.getWidgetTheme()

        setContent {
            CodeSnackTheme {
                SettingsScreen(
                    initialLanguages = currentLanguages,
                    initialFrequency = currentFrequency,
                    initialTheme = currentTheme,
                    onSave = { languages, frequency, theme ->
                        prefs.setSelectedLanguages(languages)
                        prefs.setUpdateFrequency(frequency)
                        prefs.setWidgetTheme(theme)

                        // Update widget with new snippet from selected languages
                        CoroutineScope(Dispatchers.IO).launch {
                            // Get a new snippet from the selected languages
                            val snippet = if (languages.isNotEmpty()) {
                                val allSnippets = com.example.codesnack.data.SnippetRepository.getAllSnippets()
                                val filteredSnippets = allSnippets.filter { it.language.name in languages }
                                filteredSnippets.randomOrNull() ?: allSnippets.first()
                            } else {
                                com.example.codesnack.data.SnippetRepository.getRandomSnippet()
                            }

                            // Update SharedPreferences
                            val sharedPrefs = getSharedPreferences("CodeSnackWidgetPrefs", MODE_PRIVATE)
                            sharedPrefs.edit().apply {
                                putInt("current_snippet_id", snippet.id)
                                apply()
                            }

                            // Update Glance state for all widgets
                            val glanceIds = GlanceAppWidgetManager(this@SettingsActivity)
                                .getGlanceIds(CodeSnackWidget::class.java)

                            glanceIds.forEach { glanceId ->
                                updateAppWidgetState(this@SettingsActivity, glanceId) { state ->
                                    state[intPreferencesKey("current_snippet_id")] = snippet.id
                                }
                                CodeSnackWidget().update(this@SettingsActivity, glanceId)
                            }

                            // Finish on main thread
                            CoroutineScope(Dispatchers.Main).launch {
                                finish()
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
    initialLanguages: Set<String>,
    initialFrequency: Int,
    initialTheme: WidgetTheme,
    onSave: (Set<String>, Int, WidgetTheme) -> Unit
) {
    var selectedLanguages by remember { mutableStateOf(initialLanguages) }
    var showAllLanguages by remember { mutableStateOf(initialLanguages.isEmpty()) }
    var selectedFrequency by remember { mutableStateOf(initialFrequency) }
    var selectedTheme by remember { mutableStateOf(initialTheme) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Widget Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Language Selection Section
                Text(
                    text = "Programming Languages",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (showAllLanguages) {
                        "All languages selected"
                    } else if (selectedLanguages.isEmpty()) {
                        "Select languages"
                    } else {
                        "${selectedLanguages.size} selected"
                    },
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable card containing language options
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // All Languages option
                        LanguageSettingOption(
                            title = "All Languages",
                            subtitle = "Show tips from all languages",
                            isSelected = showAllLanguages,
                            color = Color(0xFF6200EE),
                            onClick = {
                                showAllLanguages = !showAllLanguages
                                if (showAllLanguages) {
                                    selectedLanguages = emptySet()
                                }
                            }
                        )

                        // Language list
                        ProgrammingLanguage.entries.forEach { language ->
                            LanguageSettingOption(
                                title = language.displayName,
                                subtitle = "Include ${language.displayName} tips",
                                isSelected = language.name in selectedLanguages,
                                color = getLanguageColor(language),
                                onClick = {
                                    showAllLanguages = false
                                    selectedLanguages = if (language.name in selectedLanguages) {
                                        selectedLanguages - language.name
                                    } else {
                                        selectedLanguages + language.name
                                    }
                                }
                            )
                        }
                    }
                }

            Spacer(modifier = Modifier.height(12.dp))

            // Widget Theme Section
            Text(
                text = "Widget Theme",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose appearance",
                fontSize = 11.sp,
                color = Color(0xFF9E9E9E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Theme previews in horizontal scrollable row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(WidgetTheme.entries.size) { index ->
                    val theme = WidgetTheme.entries[index]
                    WidgetThemePreview(
                        theme = theme,
                        isSelected = selectedTheme == theme,
                        onClick = { selectedTheme = theme }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Update Frequency Section
            Text(
                text = "Update Frequency",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val frequencies = listOf(1, 4, 12, 24)
            val frequencyLabels = listOf("1 hour", "4 hours", "12 hours", "Daily")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                frequencies.forEachIndexed { index, frequency ->
                    FrequencyChip(
                        label = frequencyLabels[index],
                        isSelected = selectedFrequency == frequency,
                        onClick = { selectedFrequency = frequency },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Save button - fixed at bottom
            Button(
                onClick = {
                    val finalLanguages = if (showAllLanguages) emptySet() else selectedLanguages
                    onSave(finalLanguages, selectedFrequency, selectedTheme)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Save Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LanguageSettingOption(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2C2C2E) else Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 3.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = title.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0E0E0)
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun FrequencyChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF6200EE) else Color(0xFF1E1E1E)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color(0xFFB0BEC5)
            )
        }
    }
}

@Composable
fun WidgetThemePreview(
    theme: WidgetTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val themeColors = getThemeColors(theme)
    val demoSnippet = remember { SnippetRepository.getSnippetById(1) ?: SnippetRepository.getRandomSnippet() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Preview card
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(
                    width = if (isSelected) 3.dp else 0.dp,
                    color = if (isSelected) Color(0xFF6200EE) else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable(onClick = onClick)
                .then(
                    if (themeColors.backgroundGradient != null) {
                        Modifier.background(themeColors.backgroundGradient, RoundedCornerShape(16.dp))
                    } else {
                        Modifier.background(themeColors.background, RoundedCornerShape(16.dp))
                    }
                )
                .padding(12.dp)
        ) {
            Column {
                // Header with language badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF7F52FF), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Kotlin",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Box(
                        modifier = Modifier
                            .background(themeColors.categoryBadgeBackground, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Best Practice",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Medium,
                            color = themeColors.categoryBadgeText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = demoSnippet.title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColors.titleColor,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Code block
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(themeColors.codeBackground, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = demoSnippet.code.take(50) + "...",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Normal,
                        color = themeColors.codeTextColor,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Explanation
                Text(
                    text = demoSnippet.explanation.take(80) + "...",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Normal,
                    color = themeColors.explanationColor,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tap hint
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Tap for next tip",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium,
                        color = themeColors.tapHintColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Theme name
        Text(
            text = theme.displayName,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF6200EE) else Color(0xFFB0BEC5)
        )
    }
}

private fun getLanguageColor(language: ProgrammingLanguage): Color {
    return when (language) {
        ProgrammingLanguage.KOTLIN -> Color(0xFF7F52FF)
        ProgrammingLanguage.PYTHON -> Color(0xFF3776AB)
        ProgrammingLanguage.JAVASCRIPT -> Color(0xFFF7DF1E)
        ProgrammingLanguage.JAVA -> Color(0xFFED8B00)
        ProgrammingLanguage.CPP -> Color(0xFF00599C)
        ProgrammingLanguage.SWIFT -> Color(0xFFFA7343)
        ProgrammingLanguage.RUST -> Color(0xFFCE422B)
        ProgrammingLanguage.GO -> Color(0xFF00ADD8)
        ProgrammingLanguage.DART -> Color(0xFF0175C2)
    }
}