package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.GameViewModel

@Composable
fun MainMenuScreen(
    gameVm: GameViewModel,
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onViewQuests: () -> Unit
) {
    val run    by gameVm.run.collectAsState()
    val global by gameVm.global.collectAsState()
    val hasRun = run != null

    // Pulsing title animation
    val infiniteTransition = rememberInfiniteTransition(label = "title_pulse")
    val titleScale by infiniteTransition.animateFloat(
        initialValue   = 1f,
        targetValue    = 1.03f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.6f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {

        // Background decorative elements
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(SwordGold.copy(alpha = 0.06f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier            = Modifier.fillMaxSize().systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(0.5f))

            // Title
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text       = "CULT",
                    color      = SwordGold.copy(alpha = glowAlpha),
                    fontSize   = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle  = FontStyle.Italic,
                    letterSpacing = 6.sp,
                    modifier   = Modifier.scale(titleScale)
                )
                Text(
                    text       = "OF CARDS",
                    color      = TextPrimary,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                )
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SwordGold.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text       = "⚔  S W O R D S M A N S H I P  ⚔",
                        color      = SwordGold,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(Modifier.weight(0.3f))

            // Rank display
            val rank = run?.swordsmanRank
            if (rank != null) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkSurface)
                        .padding(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Current Rank", color = TextSecondary, fontSize = 9.sp)
                        Text(rank.displayName, color = AccentGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(rank.description, color = TextSecondary, fontSize = 9.sp, textAlign = TextAlign.Center, lineHeight = 12.sp)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            // Buttons
            Column(
                modifier = Modifier.padding(horizontal = 36.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (hasRun) {
                    Button(
                        onClick  = onContinue,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = SwordGold, contentColor = Color.Black),
                        shape    = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text("⚔ Continue Run", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                    OutlinedButton(
                        onClick  = onNewGame,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border   = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        shape    = RoundedCornerShape(12.dp)
                    ) {
                        Text("New Run", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick  = onNewGame,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = SwordGold, contentColor = Color.Black),
                        shape    = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text("⚔ Begin Your Path", fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                    }
                }

                OutlinedButton(
                    onClick  = onViewQuests,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = AccentGold),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.4f)),
                    shape    = RoundedCornerShape(12.dp)
                ) {
                    Text("📜 Quests", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.weight(0.3f))

            // Stats footer
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatChip("⚔ Battles", global.battlesWon.toString())
                StatChip("💀 Slain",  global.totalEnemiesKilled.toString())
                StatChip("🏆 Runs",   global.totalRuns.toString())
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Double-tap a card to use it • One card per turn",
                color    = TextSecondary.copy(alpha = 0.5f),
                fontSize = 9.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = SwordGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(label, color = TextSecondary, fontSize = 9.sp)
    }
}
