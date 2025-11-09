package com.example.codesnack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
                    containerColor = Color(0xFF121212),
                    floatingActionButton = {
                        GlassmorphismPlaygroundButton(
                            onClick = {
                                startActivity(Intent(this@MainActivity, com.example.codesnack.playground.CodePlaygroundActivity::class.java))
                            }
                        )
                    }
                ) { innerPadding ->
                    CodeSnackHome(modifier = Modifier.padding(innerPadding))
                }





















            }
        }
    }
}

@Composable
fun GlassmorphismPlaygroundButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF9D50BB),
                        Color(0xFF6E48AA),
                        Color(0xFF4A90E2)
                    )
                )
            )
    ) {
        Surface(
            onClick = onClick,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
            color = Color.Transparent
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "Playground",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    "Playground",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}

@Composable
fun CodeSnackHome(modifier: Modifier = Modifier) {
    var selectedLanguage by remember { mutableStateOf<ProgrammingLanguage?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()

    // State to hold unified tips (static + AI)
    var unifiedTips by remember { mutableStateOf<List<UnifiedTip>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableStateOf(0) }
    var isGeneratingAiTip by remember { mutableStateOf(false) }

    // Register broadcast receiver for automatic refresh when AI tip is generated
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                android.util.Log.d("MainActivity", "Received AI_TIP_GENERATED broadcast, refreshing list automatically")
                // Stop shimmer and trigger automatic refresh
                isGeneratingAiTip = false
                refreshTrigger++
            }
        }

        val filter = IntentFilter("com.example.codesnack.AI_TIP_GENERATED")
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

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

    // Filter tips based on search query
    val filteredTips = remember(unifiedTips, searchQuery) {
        if (searchQuery.isBlank()) {
            unifiedTips
        } else {
            unifiedTips.filter { tip ->
                tip.title.contains(searchQuery, ignoreCase = true) ||
                tip.code.contains(searchQuery, ignoreCase = true) ||
                tip.explanation.contains(searchQuery, ignoreCase = true) ||
                tip.language.displayName.contains(searchQuery, ignoreCase = true) ||
                tip.category.displayName.contains(searchQuery, ignoreCase = true)
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
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFFFFF),
                letterSpacing = (-0.5).sp
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
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF8E8E93),
            letterSpacing = 0.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(54

                    .dp),
            placeholder = {
                Text(
                    "Search tips...",
                    fontSize = 15.sp,
                    color = Color(0xFF8E8E93)
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF8E8E93),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = Color(0xFF8E8E93),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1C1C1E),
                unfocusedContainerColor = Color(0xFF1C1C1E),
                focusedBorderColor = Color(0xFF38383A),
                unfocusedBorderColor = Color(0xFF38383A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF0A84FF)
            ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // AI Generation Button - generates 1 tip for selected language
        Button(
            onClick = {
                // Start shimmer loading
                isGeneratingAiTip = true

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
                containerColor = Color(0xFF34C759)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "🤖 Generate AI Tip Now",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp
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
        } else if (filteredTips.isEmpty()) {
            // Empty state when no results found
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🔍",
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (searchQuery.isNotEmpty()) "No tips found" else "No tips available",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFE0E0E0)
                    )
                    if (searchQuery.isNotEmpty()) {
                        Text(
                            text = "Try a different search term",
                            fontSize = 14.sp,
                            color = Color(0xFF8E8E93)
                        )
                    }
                }
            }
        } else {
            // Filtered tips list with shimmer loading
            val listState = rememberLazyListState()

            // Force scroll to top immediately when generating starts
            LaunchedEffect(isGeneratingAiTip) {
                if (isGeneratingAiTip) {
                    listState.scrollToItem(0) // Immediate scroll, not animated
                }
            }

            // Keep forcing scroll to top while generating (in case user tries to scroll)
            LaunchedEffect(isGeneratingAiTip, listState.firstVisibleItemIndex) {
                if (isGeneratingAiTip && listState.firstVisibleItemIndex > 0) {
                    listState.scrollToItem(0)
                }
            }

            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Show shimmer loading card at top when generating AI tip
                if (isGeneratingAiTip) {
                    item(key = "shimmer_loading") {
                        ShimmerLoadingCard()
                    }
                }

                // Regular tips
                items(filteredTips) { tip ->
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
    val context = androidx.compose.ui.platform.LocalContext.current

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

@Composable
fun ShimmerLoadingCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E2A1E) // Green tint for AI
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Shimmer badges row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // AI badge shimmer
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = shimmerAlpha))
                    )
                    // Language badge shimmer
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF7F52FF).copy(alpha = shimmerAlpha))
                    )
                }

                // Category badge shimmer
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF37474F).copy(alpha = shimmerAlpha))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF8E8E93).copy(alpha = shimmerAlpha))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Code block shimmer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF2D2D2D)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF66BB6A).copy(alpha = shimmerAlpha))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF66BB6A).copy(alpha = shimmerAlpha))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(14.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF66BB6A).copy(alpha = shimmerAlpha))
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explanation shimmer
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFB0BEC5).copy(alpha = shimmerAlpha))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFB0BEC5).copy(alpha = shimmerAlpha))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFB0BEC5).copy(alpha = shimmerAlpha))
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

@Preview(showBackground = true)
@Composable
fun CodeSnackHomePreview() {
    CodeSnackTheme {
        CodeSnackHome()
    }
}
