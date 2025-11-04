package com.example.codesnack

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.model.CodeSnippet
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.ui.theme.CodeSnackTheme
import com.example.codesnack.worker.WidgetWorkScheduler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule hourly widget updates
        WidgetWorkScheduler.scheduleHourlyUpdate(this)

        // Schedule AI tip generation (runs every 3 hours in background)
        WidgetWorkScheduler.scheduleAiTipGeneration(this)

        enableEdgeToEdge()
        setContent {
            CodeSnackTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFF121212)
                ) { innerPadding ->
                    CodeSnackHome(modifier = Modifier.padding(innerPadding))
                }





















            }
        }
    }
}

@Composable
fun CodeSnackHome(modifier: Modifier = Modifier) {
    var selectedLanguage by remember { mutableStateOf<ProgrammingLanguage?>(null) }
    val context = androidx.compose.ui.platform.LocalContext.current

    val snippets = remember(selectedLanguage) {
        if (selectedLanguage != null) {
            SnippetRepository.getSnippetsByLanguage(selectedLanguage!!)
        } else {
            SnippetRepository.getAllSnippets()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CodeSnack",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0E0E0)
            )

            // Settings button
            IconButton(
                onClick = {
                    context.startActivity(Intent(context, SettingsActivity::class.java))
                }
            ) {
                Text(
                    text = "⚙️",
                    fontSize = 24.sp
                )
            }
        }

        Text(
            text = "Add widget to your home screen for daily tips!",
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        // AI Generation Test Button
        Button(
            onClick = {
                WidgetWorkScheduler.triggerImmediateAiGeneration(context)
                android.widget.Toast.makeText(
                    context,
                    "AI tip generation started! Check logs for progress.",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "🤖 Generate AI Tips Now (Test)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Language filter chips
        LanguageFilterChips(
            selectedLanguage = selectedLanguage,
            onLanguageSelected = { selectedLanguage = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Snippet list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(snippets) { snippet ->
                SnippetCard(snippet = snippet)
            }
        }
    }
}

@Composable
fun LanguageFilterChips(
    selectedLanguage: ProgrammingLanguage?,
    onLanguageSelected: (ProgrammingLanguage?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedLanguage == null,
                onClick = { onLanguageSelected(null) },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF7F52FF),
                    selectedLabelColor = Color.White
                )
            )
        }

        items(ProgrammingLanguage.entries.size) { index ->
            val language = ProgrammingLanguage.entries[index]
            FilterChip(
                selected = selectedLanguage == language,
                onClick = { onLanguageSelected(language) },
                label = { Text(language.displayName) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = getLanguageColor(language),
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}

@Composable
fun SnippetCard(snippet: CodeSnippet) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = getLanguageColor(snippet.language)
                ) {
                    Text(
                        text = snippet.language.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF37474F)
                ) {
                    Text(
                        text = snippet.category.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = snippet.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF2D2D2D)
            ) {
                Text(
                    text = snippet.code,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF4FC3F7)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = snippet.explanation,
                fontSize = 13.sp,
                color = Color(0xFFB0BEC5),
                lineHeight = 18.sp
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

@Preview(showBackground = true)
@Composable
fun CodeSnackHomePreview() {
    CodeSnackTheme {
        CodeSnackHome()
    }
}
