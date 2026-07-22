package com.mkdev.cultofcardsword.ui.screens

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
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.GameViewModel

@Composable
fun VictoryScreen(
    gameVm: GameViewModel,
    onContinue: () -> Unit
) {
    val run by gameVm.run.collectAsState()
    val r = run

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF001A00), Color(0xFF002200), DeepBlack)
                )
            )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🏆", fontSize = 80.sp)

            Spacer(Modifier.height(12.dp))

            Text(
                text          = "VICTORIOUS",
                color         = SwordGold,
                fontWeight    = FontWeight.Black,
                fontSize      = 36.sp,
                letterSpacing = 6.sp
            )

            Text(
                text      = "The path of the sword stretches ever forward.",
                color     = TextSecondary,
                fontSize  = 14.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(top = 8.dp, bottom = 28.dp)
            )

            // Run summary
            if (r != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurface, RoundedCornerShape(14.dp))
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text          = "GRAND VICTORY",
                            color         = SwordGold,
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 4.sp
                        )
                        HorizontalDivider(color = CardBorder)
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            VictoryStat("Cult",  r.cultId.displayName,        SwordGold)
                            VictoryStat("Rank",  r.swordsmanRank.displayName, AccentGold)
                            VictoryStat("Deck",  "${r.deck.size} cards",      ManaBlue)
                        }
                        if (r.relics.isNotEmpty()) {
                            HorizontalDivider(color = CardBorder)
                            Text(
                                text      = "Relics: " + r.relics.joinToString(" · ") { it.name },
                                color     = TextSecondary,
                                fontSize  = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick  = onContinue,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = SwordGold, contentColor = Color.Black)
            ) {
                Text("Continue Journey ⚔", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun VictoryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.Center)
        Text(label, color = TextSecondary, fontSize = 10.sp)
    }
}
