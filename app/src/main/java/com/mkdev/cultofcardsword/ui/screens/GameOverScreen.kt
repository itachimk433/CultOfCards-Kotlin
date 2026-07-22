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
import com.mkdev.cultofcardsword.data.GameRun
import com.mkdev.cultofcardsword.data.getCultColor
import com.mkdev.cultofcardsword.ui.theme.*

@Composable
fun GameOverScreen(
    run: GameRun?,
    onRetry: () -> Unit,
    onMainMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF220000), Color(0xFF100000), DeepBlack)
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
            Text("💀", fontSize = 80.sp)

            Spacer(Modifier.height(12.dp))

            Text(
                text          = "DEFEATED",
                color         = DangerRed,
                fontWeight    = FontWeight.Black,
                fontSize      = 40.sp,
                letterSpacing = 6.sp
            )

            Text(
                text     = "The path of the sword ends here.",
                color    = TextSecondary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 28.dp)
            )

            // Run epitaph
            if (run != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurface, RoundedCornerShape(14.dp))
                        .padding(18.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text          = "THIS RUN",
                            color         = TextSecondary,
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                        HorizontalDivider(color = CardBorder)
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            EpitaphStat("Act",      "${run.act}",              SwordGold)
                            EpitaphStat("Floor",    "${run.floor}",            AccentGold)
                            EpitaphStat("Deck",     "${run.deck.size} cards",  ManaBlue)
                        }
                        if (run.relics.isNotEmpty()) {
                            HorizontalDivider(color = CardBorder)
                            Text(
                                text      = run.relics.joinToString(" · ") { it.name },
                                color     = TextSecondary,
                                fontSize  = 11.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 15.sp
                            )
                        }
                        Text(
                            text     = run.cultId.displayName,
                            color    = Color(getCultColor(run.cultId.name)),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            Button(
                onClick  = onRetry,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = SwordGold, contentColor = Color.Black)
            ) {
                Text("Rise Again — New Run ⚔", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(Modifier.height(10.dp))

            TextButton(onClick = onMainMenu) {
                Text("Return to Main Menu", color = TextSecondary, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun EpitaphStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center)
        Text(label, color = TextSecondary, fontSize = 10.sp)
    }
}
