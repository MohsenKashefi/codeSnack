package com.example.codesnack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.ui.theme.CodeSnackTheme
import com.example.codesnack.widget.CodeSnackWidget
import com.example.codesnack.widget.WidgetPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = WidgetPreferences(this)
        val currentLanguages = prefs.getSelectedLanguages()
        val currentFrequency = prefs.getUpdateFrequency()

        setContent {
            CodeSnackTheme {
                SettingsScreen(
                    initialLanguages = currentLanguages,
                    initialFrequency = currentFrequency,
                    onSave = { languages, frequency ->
                        prefs.setSelectedLanguages(languages)
                        prefs.setUpdateFrequency(frequency)

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
    onSave: (Set<String>, Int) -> Unit
) {
    var selectedLanguages by remember { mutableStateOf(initialLanguages) }
    var showAllLanguages by remember { mutableStateOf(initialLanguages.isEmpty()) }
    var selectedFrequency by remember { mutableStateOf(initialFrequency) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Widget Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Language Selection Section
            Text(
                text = "Programming Languages",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (showAllLanguages) {
                    "All languages selected"
                } else if (selectedLanguages.isEmpty()) {
                    "Select languages to show in widget"
                } else {
                    "${selectedLanguages.size} language(s) selected"
                },
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // All Languages option
            LanguageSettingOption(
                title = "All Languages",
                subtitle = "Show tips from all programming languages",
                isSelected = showAllLanguages,
                color = Color(0xFF6200EE),
                onClick = {
                    showAllLanguages = !showAllLanguages
                    if (showAllLanguages) {
                        selectedLanguages = emptySet()
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Language list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(ProgrammingLanguage.entries.toList()) { language ->
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

            Spacer(modifier = Modifier.height(24.dp))

            // Update Frequency Section
            Text(
                text = "Update Frequency",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val frequencies = listOf(1, 4, 12, 24)
            val frequencyLabels = listOf("1 hour", "4 hours", "12 hours", "Daily")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Save button
            Button(
                onClick = {
                    val finalLanguages = if (showAllLanguages) emptySet() else selectedLanguages
                    onSave(finalLanguages, selectedFrequency)
                },
                modifier = Modifier
                    .fillMaxWidth()
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2C2C2E) else Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = title.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0E0E0)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(20.dp)
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
    }
}