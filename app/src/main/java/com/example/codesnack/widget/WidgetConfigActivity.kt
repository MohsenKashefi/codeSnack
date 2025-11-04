package com.example.codesnack.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WidgetConfigActivity : ComponentActivity() {
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED initially
        setResult(RESULT_CANCELED)

        // Get the widget ID from the intent
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If this activity was started with an intent without an app widget ID, finish with an error
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            CodeSnackTheme {
                WidgetConfigScreen(
                    onLanguagesSelected = { languages ->
                        // Save preferences and update widget state in coroutine
                        CoroutineScope(Dispatchers.Main).launch {
                            saveLanguagePreferences(languages)
                            finishWithSuccess()
                        }
                    }
                )
            }
        }
    }

    private suspend fun saveLanguagePreferences(languages: Set<String>) {
        val prefs = WidgetPreferences(this)
        prefs.setSelectedLanguages(languages)

        // Get a random snippet from the selected languages
        val snippet = if (languages.isNotEmpty()) {
            val allSnippets = com.example.codesnack.data.SnippetRepository.getAllSnippets()
            val filteredSnippets = allSnippets.filter { it.language.name in languages }
            filteredSnippets.randomOrNull() ?: allSnippets.first()
        } else {
            com.example.codesnack.data.SnippetRepository.getRandomSnippet()
        }

        // Set the initial snippet ID in SharedPreferences
        val sharedPrefs = getSharedPreferences("CodeSnackWidgetPrefs", MODE_PRIVATE)
        sharedPrefs.edit().apply {
            putInt("current_snippet_id", snippet.id)
            apply()
        }

        // IMPORTANT: Update Glance state BEFORE finishing - use withContext to await completion
        withContext(Dispatchers.IO) {
            val glanceId = GlanceAppWidgetManager(this@WidgetConfigActivity)
                .getGlanceIds(CodeSnackWidget::class.java)
                .firstOrNull {
                    // Find the glance ID for this specific app widget ID
                    true // We just added it, so use the first/only one
                }

            if (glanceId != null) {
                updateAppWidgetState(this@WidgetConfigActivity, glanceId) { state ->
                    state[intPreferencesKey("current_snippet_id")] = snippet.id
                }
                CodeSnackWidget().update(this@WidgetConfigActivity, glanceId)
            }
        }

        // Set default update frequency to 4 hours if not set
        if (prefs.getUpdateFrequency() == 0) {
            prefs.setUpdateFrequency(4)
        }
    }

    private fun finishWithSuccess() {
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

@Composable
fun WidgetConfigScreen(onLanguagesSelected: (Set<String>) -> Unit) {
    var selectedLanguages by remember { mutableStateOf<Set<String>>(emptySet()) }
    var showAllLanguages by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CodeSnack Widget",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (selectedLanguages.isEmpty() && !showAllLanguages) {
                    "Select programming languages (multiple)"
                } else if (showAllLanguages) {
                    "All languages selected"
                } else {
                    "${selectedLanguages.size} language(s) selected"
                },
                fontSize = 14.sp,
                color = Color(0xFF9E9E9E)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // All Languages option
            MultiSelectLanguageOption(
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OR SELECT SPECIFIC LANGUAGES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF757575),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Language list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(ProgrammingLanguage.entries) { language ->
                    MultiSelectLanguageOption(
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

            // Continue button
            Button(
                onClick = {
                    val finalSelection = if (showAllLanguages) emptySet() else selectedLanguages
                    onLanguagesSelected(finalSelection)
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
                    text = "Add Widget",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MultiSelectLanguageOption(
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2C2C2E) else Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    Text(
                        text = title.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE0E0E0)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(24.dp)
                )
            }
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
        ProgrammingLanguage.DART -> Color(0xFF0175C2)
    }
}