package com.mkdev.cultofcardsword.ui.screens

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
import com.mkdev.cultofcardsword.data.getStoryAct
import com.mkdev.cultofcardsword.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun StoryScreen(
    actId: Int,
    floorNum: Int,
    onDone: () -> Unit
) {
    val act   = remember(actId) { getStoryAct(actId) }
    val floor = remember(actId, floorNum) { act?.floors?.find { it.floor == floorNum } }
    val dialogue = floor?.dialogue ?: emptyList()

    var currentLine by remember { mutableIntStateOf(0) }
    var visible     by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(180)
        visible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A0020), DeepBlack, Color(0xFF050008))
                )
            )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Text("💀", fontSize = 72.sp, modifier = Modifier.padding(bottom = 12.dp))
            }

            Text(
                text          = "BOSS ENCOUNTER",
                color         = DangerRed.copy(alpha = 0.85f),
                fontWeight    = FontWeight.Black,
                fontSize      = 11.sp,
                letterSpacing = 4.sp
            )

            Spacer(Modifier.height(22.dp))

            if (dialogue.isNotEmpty() && currentLine < dialogue.size) {
                val line     = dialogue[currentLine]
                val isPlayer = line.speaker == "You"

                AnimatedVisibility(
                    visible = visible,
                    enter   = fadeIn() + slideInVertically(initialOffsetY = { 20 })
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isPlayer) Color(0xFF001830) else DarkSurface)
                            .border(
                                1.dp,
                                if (isPlayer) ManaBlue.copy(alpha = 0.5f) else DangerRed.copy(alpha = 0.4f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(18.dp)
                    ) {
                        Column {
                            Text(
                                text          = line.speaker.uppercase(),
                                color         = if (isPlayer) ManaBlue else DangerRed,
                                fontWeight    = FontWeight.Bold,
                                fontSize      = 10.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text       = "\"${line.text}\"",
                                color      = TextPrimary,
                                fontSize   = 15.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))

                // Progress dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    dialogue.forEachIndexed { idx, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (idx == currentLine) 8.dp else 5.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (idx <= currentLine) DangerRed else CardBorder)
                        )
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            Button(
                onClick = {
                    if (dialogue.isEmpty() || currentLine >= dialogue.size - 1) {
                        onDone()
                    } else {
                        visible = false
                        currentLine++
                        // Re-animate
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = if (dialogue.isEmpty() || currentLine >= dialogue.size - 1)
                        DangerRed else CardSurface
                )
            ) {
                Text(
                    text       = if (dialogue.isEmpty() || currentLine >= dialogue.size - 1)
                        "Enter Battle ⚔" else "Continue…",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = if (dialogue.isEmpty() || currentLine >= dialogue.size - 1)
                        Color.White else TextSecondary
                )
            }

            Spacer(Modifier.height(10.dp))
            Text("Tap to advance dialogue", color = TextSecondary.copy(alpha = 0.5f), fontSize = 10.sp, textAlign = TextAlign.Center)
        }
    }
}
