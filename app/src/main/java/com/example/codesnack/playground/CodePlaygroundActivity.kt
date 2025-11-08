package com.example.codesnack.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.example.codesnack.model.ProgrammingLanguage
import com.example.codesnack.ui.theme.CodeSnackTheme
import kotlinx.coroutines.launch

class CodePlaygroundActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CodeSnackTheme {
                CodePlaygroundScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodePlaygroundScreen(
    onBack: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(ProgrammingLanguage.KOTLIN) }
    var code by remember { mutableStateOf(CodeTemplates.getTemplate(ProgrammingLanguage.KOTLIN)) }
    var output by remember { mutableStateOf("") }
    var isExecuting by remember { mutableStateOf(false) }
    var executionTime by remember { mutableStateOf("") }
    var memory by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(true) }
    var codeHistory by remember { mutableStateOf(mutableListOf(CodeTemplates.getTemplate(ProgrammingLanguage.KOTLIN))) }
    var historyIndex by remember { mutableStateOf(0) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val codeExecutionService = remember { CodeExecutionService() }

    // Update code template when language changes
    LaunchedEffect(selectedLanguage) {
        code = CodeTemplates.getTemplate(selectedLanguage)
        codeHistory = mutableListOf(code)
        historyIndex = 0
        output = ""
        executionTime = ""
        memory = ""
    }

    // Minimal glassmorphism background
    val backgroundBrush = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D0D0D),
                Color(0xFF1A1A1A),
                Color(0xFF0D0D0D)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF5F5F7),
                Color(0xFFFFFFFF),
                Color(0xFFF5F5F7)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Minimal Top Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White,
                shadowElevation = 0.5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Back",
                            tint = if (isDarkTheme) Color.White else Color.Black
                        )
                    }
                    Text(
                        "Code Playground",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isDarkTheme) Color.White else Color.Black
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Minimal Language Selector
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White,
                    shadowElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Language",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color.White else Color.Black
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            Surface(
                                modifier = Modifier
                                    .width(140.dp)
                                    .menuAnchor(),
                                shape = RoundedCornerShape(8.dp),
                                color = if (isDarkTheme) Color(0xFF2C2C2E) else Color(0xFFF2F2F7)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        selectedLanguage.displayName,
                                        fontSize = 14.sp,
                                        color = if (isDarkTheme) Color.White else Color.Black
                                    )
                                }
                            }

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                ProgrammingLanguage.entries.forEach { language ->
                                    DropdownMenuItem(
                                        text = { Text(language.displayName) },
                                        onClick = {
                                            selectedLanguage = language
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Minimal Code Editor
                MinimalCodeEditor(
                    code = code,
                    onCodeChange = {
                        if (codeHistory.size > historyIndex + 1) {
                            codeHistory = codeHistory.subList(0, historyIndex + 1).toMutableList()
                        }
                        code = it
                        codeHistory.add(it)
                        historyIndex = codeHistory.size - 1
                    },
                    isDarkTheme = isDarkTheme,
                    language = selectedLanguage.displayName
                )

                // Minimal Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Undo Button
                    MinimalButton(
                        onClick = {
                            if (historyIndex > 0) {
                                historyIndex--
                                code = codeHistory[historyIndex]
                            }
                        },
                        isDarkTheme = isDarkTheme,
                        enabled = historyIndex > 0,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text(
                            text = "↶",
                            fontSize = 22.sp,
                            color = if (historyIndex > 0) {
                                if (isDarkTheme) Color(0xFF0A84FF) else Color(0xFF007AFF)
                            } else {
                                Color(0xFF8E8E93)
                            }
                        )
                    }

                    // Theme Toggle
                    MinimalButton(
                        onClick = { isDarkTheme = !isDarkTheme },
                        isDarkTheme = isDarkTheme,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (isDarkTheme) "☀️" else "🌙",
                                fontSize = 18.sp
                            )
                            Text(
                                text = if (isDarkTheme) "Light" else "Dark",
                                fontSize = 11.sp,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }

                    // Share Button
                    MinimalButton(
                        onClick = {
                            CodeShareHelper.shareCodeAsImage(
                                context = context,
                                code = code,
                                language = selectedLanguage.displayName
                            ) { error ->
                                android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        isDarkTheme = isDarkTheme,
                        enabled = code.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "📤", fontSize = 18.sp)
                            Text(
                                text = "Share",
                                fontSize = 11.sp,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }

                    // Save Button
                    MinimalButton(
                        onClick = {
                            scope.launch {
                                try {
                                    val database = com.example.codesnack.data.AppDatabase.getDatabase(context)
                                    val favorite = com.example.codesnack.data.FavoriteCode(
                                        language = selectedLanguage.displayName,
                                        code = code,
                                        title = "Saved ${selectedLanguage.displayName} code"
                                    )
                                    database.favoriteCodeDao().insertFavorite(favorite)
                                    android.widget.Toast.makeText(
                                        context,
                                        "Saved!",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                } catch (e: Exception) {
                                    android.widget.Toast.makeText(
                                        context,
                                        "Failed",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        isDarkTheme = isDarkTheme,
                        enabled = code.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "💾", fontSize = 18.sp)
                            Text(
                                text = "Save",
                                fontSize = 11.sp,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                        }
                    }
                }

                // Run Button
                Button(
                    onClick = {
                        scope.launch {
                            isExecuting = true
                            output = "Executing..."
                            executionTime = ""
                            memory = ""

                            val result = codeExecutionService.executeCode(code, selectedLanguage)

                            when (result) {
                                is CodeExecutionResult.Success -> {
                                    output = result.output.ifEmpty { "(No output)" }
                                    executionTime = "${result.executionTime}s"
                                    memory = "${result.memory} KB"
                                }
                                is CodeExecutionResult.Error -> {
                                    output = "Error:\n${result.message}"
                                    executionTime = ""
                                    memory = ""
                                }
                            }
                            isExecuting = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDarkTheme) Color(0xFF30D158) else Color(0xFF34C759),
                        disabledContainerColor = if (isDarkTheme) Color(0xFF1E3A28) else Color(0xFFC7E5D0)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isExecuting && code.isNotBlank()
                ) {
                    if (isExecuting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Running...", fontSize = 15.sp)
                    } else {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Run",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Run Code", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Output Section
                if (output.isNotEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White,
                        shadowElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Output",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDarkTheme) Color.White else Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = output,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = if (output.startsWith("Error:"))
                                    Color(0xFFFF453A)
                                else if (isDarkTheme)
                                    Color(0xFF30D158)
                                else
                                    Color(0xFF34C759)
                            )

                            if (executionTime.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(
                                    color = if (isDarkTheme) Color(0xFF38383A) else Color(0xFFE5E5EA)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "⏱ $executionTime",
                                        fontSize = 12.sp,
                                        color = if (isDarkTheme) Color(0xFF8E8E93) else Color(0xFF636366)
                                    )
                                    Text(
                                        text = "💾 $memory",
                                        fontSize = 12.sp,
                                        color = if (isDarkTheme) Color(0xFF8E8E93) else Color(0xFF636366)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MinimalButton(
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (enabled) {
        Box(
            modifier = modifier
                .height(44.dp)
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isDarkTheme)
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF2C2C2E),
                                Color(0xFF1C1C1E)
                            )
                        )
                    else
                        Brush.verticalGradient(
                            listOf(
                                Color.White,
                                Color(0xFFFAFAFA)
                            )
                        )
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                onClick = onClick,
                color = Color.Transparent,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    content()
                }
            }
        }
    } else {
        Box(
            modifier = modifier
                .height(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isDarkTheme) Color(0xFF1C1C1E).copy(alpha = 0.3f) else Color(0xFFF2F2F7)
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun MinimalCodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    isDarkTheme: Boolean,
    language: String
) {
    val lineCount = code.lines().size

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .shadow(6.dp, RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isDarkTheme)
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF242427),
                            Color(0xFF1C1C1E)
                        )
                    )
                else
                    Brush.verticalGradient(
                        listOf(
                            Color.White,
                            Color(0xFFFCFCFC)
                        )
                    )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Line numbers
            Column(
                modifier = Modifier
                    .width(35.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                repeat(lineCount.coerceAtLeast(15)) { index ->
                    Text(
                        text = "${index + 1}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color(0xFF0A84FF).copy(alpha = 0.6f) else Color(0xFF007AFF).copy(alpha = 0.6f),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }

            // Code area
            OutlinedTextField(
                value = code,
                onValueChange = onCodeChange,
                modifier = Modifier.fillMaxSize(),
                textStyle = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    lineHeight = 20.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = if (isDarkTheme) Color(0xFF0A84FF) else Color(0xFF007AFF)
                ),
                placeholder = {
                    Text(
                        "Write your $language code here...",
                        color = if (isDarkTheme) Color(0xFF636366) else Color(0xFF8E8E93),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )
                }
            )
        }
    }
}