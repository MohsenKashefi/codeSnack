package com.example.codesnack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
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
import com.example.codesnack.data.AppDatabase
import com.example.codesnack.data.SnippetRepository
import com.example.codesnack.model.CodeSnippet
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.model.UnifiedTip
import com.example.codesnack.ui.theme.CodeSnackTheme
import com.example.codesnack.worker.WidgetWorkScheduler
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    // State to hold unified tips (static + AI)
    var unifiedTips by remember { mutableStateOf<List<UnifiedTip>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Register broadcast receiver for automatic refresh when AI tip is generated
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                android.util.Log.d("MainActivity", "Received AI_TIP_GENERATED broadcast, refreshing list automatically")
                // Trigger automatic refresh
                refreshTrigger++
            }
        }

        val filter = IntentFilter("com.example.codesnack.AI_TIP_GENERATED")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }

        onDispose {
            context.unregisterReceiver(receiver)
        }
    }

    // Load tips whenever selected language changes or refreshTrigger changes
    LaunchedEffect(selectedLanguage, refreshTrigger) {
        isLoading = true
        scope.launch {
            try {
                val database = AppDatabase.getDatabase(context)
                val aiTipDao = database.aiTipDao()

                // Get static tips
                val staticSnippets = if (selectedLanguage != null) {
                    SnippetRepository.getSnippetsByLanguage(selectedLanguage!!)
                } else {
                    SnippetRepository.getAllSnippets()
                }

                // Get AI tips from database
                val aiTips = if (selectedLanguage != null) {
                    aiTipDao.getTipsByLanguage(selectedLanguage!!.displayName)
                } else {
                    aiTipDao.getAllTips()
                }

                // Convert to unified tips
                val staticUnified = staticSnippets.map { UnifiedTip.fromCodeSnippet(it) }
                val aiUnified = aiTips.map { UnifiedTip.fromAiTip(it) }

                // Combine: AI tips first (they're newest), then static tips
                unifiedTips = aiUnified + staticUnified

                android.util.Log.d("MainActivity", "Loaded ${aiUnified.size} AI tips and ${staticUnified.size} static tips")

            } catch (e: Exception) {
                android.util.Log.e("MainActivity", "Error loading tips", e)
                // Fallback to just static tips
                val staticSnippets = if (selectedLanguage != null) {
                    SnippetRepository.getSnippetsByLanguage(selectedLanguage!!)
                } else {
                    SnippetRepository.getAllSnippets()
                }
                unifiedTips = staticSnippets.map { UnifiedTip.fromCodeSnippet(it) }
            } finally {
                isLoading = false
            }
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

        // AI Generation Button - generates 1 tip for selected language
        Button(
            onClick = {
                val languageName = selectedLanguage?.displayName
                WidgetWorkScheduler.triggerImmediateAiGeneration(context, languageName)
                val message = if (languageName != null) {
                    "Generating 1 AI tip for $languageName..."
                } else {
                    "Generating 1 AI tip for all languages..."
                }
                android.widget.Toast.makeText(
                    context,
                    message,
                    android.widget.Toast.LENGTH_LONG
                ).show()
                // List will refresh automatically when broadcast is received
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text(
                text = "🤖 Generate AI Tip Now",
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

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF7F52FF))
            }
        } else {
            // Unified tips list (AI + Static)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(unifiedTips) { tip ->
                    UnifiedTipCard(tip = tip)
                }
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
fun UnifiedTipCard(tip: UnifiedTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tip.isAiGenerated) Color(0xFF1E2A1E) else Color(0xFF1E1E1E) // Slightly green tint for AI tips
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AI badge
                    if (tip.isAiGenerated) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF4CAF50)
                        ) {
                            Text(
                                text = "🤖 AI",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Language badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = getLanguageColor(tip.language)
                    ) {
                        Text(
                            text = tip.language.displayName,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Category badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF37474F)
                ) {
                    Text(
                        text = tip.category.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = tip.title,
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
                    text = tip.code,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (tip.isAiGenerated) Color(0xFF66BB6A) else Color(0xFF4FC3F7) // Different color for AI tips
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = tip.explanation,
                fontSize = 13.sp,
                color = Color(0xFFB0BEC5),
                lineHeight = 18.sp
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
        ProgrammingLanguage.DART -> Color(0xFF0175C2)
    }
}

@Preview(showBackground = true)
@Composable
fun CodeSnackHomePreview() {
    CodeSnackTheme {
        CodeSnackHome()
    }
}
