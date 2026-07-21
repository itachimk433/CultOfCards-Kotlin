package com.cultofcards.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cultofcards.data.EffectType
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.BattleEnemy

@Composable
fun EnemyView(
    enemy: BattleEnemy,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val hpPct = (enemy.hp.toFloat() / enemy.maxHp.toFloat()).coerceIn(0f, 1f)
    val hpColor = when {
        hpPct > 0.5f -> HpGreen
        hpPct > 0.25f -> HpYellow
        else -> HpRed
    }

    val nextMove = enemy.moves.getOrNull(enemy.moveIndex % enemy.moves.size)
    val bossColor = if (enemy.isBoss) Color(0xFFAA00AA) else Border

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) SurfaceVariant else Surface)
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = bossColor,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Name + boss badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (enemy.isBoss) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFAA00AA).copy(alpha = 0.3f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("BOSS", color = Color(0xFFCC44FF), fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                    Text(
                        text = enemy.name,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Text(
                    text = "${enemy.hp} / ${enemy.maxHp}",
                    color = hpColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }

            // HP bar
            LinearProgressIndicator(
                progress = { hpPct },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = hpColor,
                trackColor = Surface
            )

            // Block + effects row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (enemy.block > 0) {
                    StatusChip(text = "🛡 ${enemy.block}", color = BlockBlue)
                }
                if (enemy.strength > 0) {
                    StatusChip(text = "💪 ${enemy.strength}", color = StrengthGold)
                }
                enemy.effects.forEach { eff ->
                    val (label, color) = when (eff.type) {
                        EffectType.POISON -> "☠ ${eff.stacks}" to PoisonGreen
                        EffectType.BURN -> "🔥 ${eff.stacks}" to BurnOrange
                        EffectType.WEAK -> "↓ ${eff.stacks}" to WeakGray
                        EffectType.VULNERABLE -> "↑ ${eff.stacks}" to VulnerableRed
                        EffectType.STRENGTH -> "💪 ${eff.stacks}" to StrengthGold
                    }
                    StatusChip(text = label, color = color)
                }
            }

            // Next move intent
            nextMove?.let { move ->
                val (icon, desc) = when (move.type) {
                    "attack" -> "⚔" to "${move.label} (${move.value + enemy.strength} dmg)"
                    "block" -> "🛡" to "${move.label} (+${move.value})"
                    else -> "✦" to move.label
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = icon, fontSize = 11.sp)
                    Text(
                        text = "Intends: $desc",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.18f))
            .border(0.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}
