package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EmojiEvents
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

@Composable
fun MainMenuScreen(gameVm: GameViewModel, navController: NavHostController) {
    val run by gameVm.run.collectAsState()
    val globalProgress by gameVm.globalProgress.collectAsState()
    val isLoading by gameVm.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A0030), BackgroundDeep),
                    radius = 1200f
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
            // Title
            Text(
                text = "✦",
                fontSize = 48.sp,
                color = Purple,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "CULT",
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
                letterSpacing = 12.sp
            )
            Text(
                text = "OF CARDS",
                style = MaterialTheme.typography.displayMedium,
                color = PurpleBright,
                letterSpacing = 8.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "A dark deck-building roguelite",
                color = TextMuted,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Purple)
            } else {
                // Continue / New Run
                if (run != null) {
                    val r = run!!
                    // Continue button
                    PrimaryButton(
                        text = "Continue Run",
                        subtitle = "Act ${r.act} · Floor ${r.floor} · ${r.cultId.displayName}",
                        icon = { Icon(Icons.Default.PlayArrow, null, tint = Color.White) },
                        onClick = { navController.navigate(Routes.CAMPAIGN) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            gameVm.endRun()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = HpRed)
                    ) {
                        Text("Abandon Run", color = HpRed)
                    }
                } else {
                    PrimaryButton(
                        text = "New Run",
                        subtitle = "Choose your cult and begin",
                        icon = { Text("✦", fontSize = 18.sp, color = Color.White) },
                        onClick = { navController.navigate(Routes.CULT_SELECT) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quests
                OutlinedButton(
                    onClick = { navController.navigate(Routes.QUESTS) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleBright)
                ) {
                    Icon(Icons.Outlined.EmojiEvents, null, tint = StrengthGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Quests", color = PurpleBright)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats
                if (globalProgress.totalRuns > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceVariant)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("Runs", "${globalProgress.totalRuns}")
                            StatItem("Wins", "${globalProgress.battlesWon}")
                            StatItem("Kills", "${globalProgress.totalEnemiesKilled}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Purple)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            icon()
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                Text(text = subtitle, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = PurpleBright, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = label, color = TextMuted, fontSize = 11.sp)
    }
}
