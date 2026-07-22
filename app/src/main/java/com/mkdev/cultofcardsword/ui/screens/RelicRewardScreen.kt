package com.mkdev.cultofcardsword.ui.screens

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
import com.mkdev.cultofcardsword.data.GameRun
import com.mkdev.cultofcardsword.data.Relic
import com.mkdev.cultofcardsword.data.getRandomRelic
import com.mkdev.cultofcardsword.ui.theme.*

@Composable
fun RelicRewardScreen(
    run: GameRun?,
    onRelicTaken: (Relic) -> Unit
) {
    val relic = remember(run) {
        val ownedIds = run?.relics?.map { it.id } ?: emptyList()
        getRandomRelic(ownedIds)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1A0030), DeepBlack),
                    radius = 1200f
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
            Text("💎", fontSize = 56.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text          = "BOSS DEFEATED",
                color         = SwordGold,
                fontWeight    = FontWeight.Black,
                fontSize      = 13.sp,
                letterSpacing = 4.sp
            )
            Text(
                text     = "You claim a powerful relic",
                color    = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(36.dp))

            // Relic card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkSurface)
                    .border(2.dp, SwordGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("◆", fontSize = 28.sp, color = SwordGold)
                    Text(
                        text       = relic.name,
                        color      = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        textAlign  = TextAlign.Center
                    )
                    HorizontalDivider(color = CardBorder)
                    Text(
                        text      = relic.description,
                        color     = TextSecondary,
                        fontSize  = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    // Bonus bullets
                    val bonuses = buildList {
                        if (relic.blockBonus > 0)        add("Start each battle with +${relic.blockBonus} Block")
                        if (relic.healAfterBattle > 0)   add("Heal ${relic.healAfterBattle} HP after each battle")
                        if (relic.energyBonus > 0)       add("+${relic.energyBonus} Energy per turn")
                        if (relic.extraDraw > 0)         add("Draw ${relic.extraDraw} extra card per turn")
                        if (relic.attackBonus > 0)       add("All attacks deal +${relic.attackBonus} damage")
                        if (relic.extraMaxHp > 0)        add("+${relic.extraMaxHp} Max HP")
                        if (relic.firstAttackDouble)     add("First attack per battle deals double damage")
                        if (relic.goldAfterBattle > 0)   add("+${relic.goldAfterBattle} Gold after each battle")
                        if (relic.manaBonus > 0)         add("+${relic.manaBonus} Mana at battle start")
                    }
                    bonuses.forEach { bonus ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment     = Alignment.Top
                        ) {
                            Text("✦", color = SwordGold, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
                            Text(bonus, color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            Button(
                onClick  = { onRelicTaken(relic) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = SwordGold, contentColor = Color.Black)
            ) {
                Text("Claim Relic & Continue ✦", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}
