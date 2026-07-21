package com.cultofcards.ui.screens

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

@Composable
fun RelicRewardScreen(gameVm: GameViewModel, navController: NavHostController) {
    val relic by gameVm.pendingRelic.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A0030), BackgroundDeep),
                    radius = 1000f
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
            Text("💎", fontSize = 56.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "BOSS DEFEATED",
                color = Color(0xFFCC44FF),
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                letterSpacing = 4.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "You claim a powerful relic",
                color = TextMuted,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (relic != null) {
                val r = relic!!
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceVariant)
                        .border(2.dp, PurpleBright.copy(0.5f), RoundedCornerShape(20.dp))
                        .padding(28.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("◆", fontSize = 32.sp, color = PurpleBright)
                        Text(
                            text = r.name,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(color = Border)
                        Text(
                            text = r.description,
                            color = TextSecondary,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )

                        // Show relic bonuses
                        val bonuses = buildList {
                            if (r.blockBonus > 0) add("Start each battle with +${r.blockBonus} Block")
                            if (r.healAfterBattle > 0) add("Heal ${r.healAfterBattle} HP after each battle")
                            if (r.energyBonus > 0) add("+${r.energyBonus} Energy per turn")
                            if (r.extraDraw > 0) add("Draw ${r.extraDraw} extra card per turn")
                            if (r.attackBonus > 0) add("All attacks deal +${r.attackBonus} damage")
                            if (r.extraMaxHp > 0) add("+${r.extraMaxHp} Max HP")
                            if (r.firstAttackDouble) add("First attack per battle deals double damage")
                            if (r.goldAfterBattle > 0) add("+${r.goldAfterBattle} Gold after each battle")
                        }
                        bonuses.forEach { bonus ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("✦", color = Purple, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                                Text(bonus, color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    gameVm.clearPendingRelic()
                    navController.navigate(Routes.CAMPAIGN) { popUpTo(Routes.CAMPAIGN) }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text("Continue to Next Act ✦", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
