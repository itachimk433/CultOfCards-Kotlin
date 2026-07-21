package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun VictoryScreen(gameVm: GameViewModel, navController: NavHostController) {
    val run by gameVm.run.collectAsState()
    val r = run

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF002200), Color(0xFF001800), BackgroundDeep)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🏆", fontSize = 80.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "VICTORY",
                color = StrengthGold,
                fontWeight = FontWeight.Black,
                fontSize = 48.sp,
                letterSpacing = 8.sp
            )
            Text(
                text = "The Dark God falls. Your cult prevails.",
                color = TextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (r != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceVariant, RoundedCornerShape(16.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "RUN COMPLETE",
                            color = StrengthGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 3.sp
                        )
                        HorizontalDivider(color = Border)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            VictoryStat("Cult", r.cultId.displayName, Color(r.cultId.color))
                            VictoryStat("HP Left", "${r.playerHp}/${r.playerMaxHp}", HpGreen)
                            VictoryStat("Deck Size", "${r.deck.size}", PurpleBright)
                        }
                        if (r.relics.isNotEmpty()) {
                            HorizontalDivider(color = Border)
                            Text("Relics Collected", color = TextMuted, fontSize = 11.sp)
                            r.relics.forEach { relic ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("◆", color = PurpleBright, fontSize = 12.sp)
                                    Text(relic.name, color = TextSecondary, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    gameVm.endRun()
                    navController.navigate(Routes.MAIN_MENU) { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StrengthGold)
            ) {
                Text("Return to Menu", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = {
                    gameVm.endRun()
                    navController.navigate(Routes.CULT_SELECT) { popUpTo(0) }
                }
            ) {
                Text("Start Another Run", color = TextMuted)
            }
        }
    }
}

@Composable
private fun VictoryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center)
        Text(label, color = TextMuted, fontSize = 11.sp)
    }
}
