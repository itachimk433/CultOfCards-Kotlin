package com.cultofcards.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.ui.navigation.Routes
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun StoryScreen(gameVm: GameViewModel, navController: NavHostController) {
    val run by gameVm.run.collectAsState()
    if (run == null) {
        LaunchedEffect(Unit) { navController.navigate(Routes.MAIN_MENU) { popUpTo(0) } }
        return
    }

    val floor = gameVm.getCurrentFloor()
    val dialogue = floor?.dialogue ?: emptyList()
    var currentLine by remember { mutableIntStateOf(0) }
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A0020), BackgroundDeep, Color(0xFF050008))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Boss icon
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Text(
                    text = "💀",
                    fontSize = 72.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Text(
                text = "BOSS ENCOUNTER",
                color = Color(0xFFAA00AA),
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (dialogue.isNotEmpty() && currentLine < dialogue.size) {
                val line = dialogue[currentLine]
                val isPlayer = line.speaker == "You"

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { 20 })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (isPlayer) Color(0xFF001830) else SurfaceVariant
                            )
                            .border(
                                1.dp,
                                if (isPlayer) BlockBlue.copy(0.5f) else Color(0xFFAA00AA).copy(0.4f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = line.speaker.uppercase(),
                                color = if (isPlayer) BlockBlue else Color(0xFFCC44FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"${line.text}\"",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                fontWeight = if (isPlayer) FontWeight.Normal else FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    dialogue.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == currentLine) 8.dp else 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (idx <= currentLine) Color(0xFFCC44FF) else Border)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Next / Enter battle
            Button(
                onClick = {
                    if (currentLine < dialogue.size - 1) {
                        currentLine++
                        visible = false
                    } else {
                        navController.navigate(Routes.BATTLE)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentLine < dialogue.size - 1)
                        SurfaceVariant else Color(0xFF6600AA)
                )
            ) {
                Text(
                    text = if (currentLine < dialogue.size - 1) "Continue..." else "Enter Battle ⚔",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (currentLine < dialogue.size - 1) TextSecondary else Color.White
                )
            }

            if (dialogue.isEmpty()) {
                Button(
                    onClick = { navController.navigate(Routes.BATTLE) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6600AA))
                ) {
                    Text("Enter Battle ⚔", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tap to advance dialogue",
                color = TextMuted,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
