package com.mkdev.cultofcardsword.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.EffectStack
import com.mkdev.cultofcardsword.data.EffectType
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.AttackAnimation
import com.mkdev.cultofcardsword.viewmodel.BattleEnemy

@Composable
fun EnemyView(
    enemy: BattleEnemy,
    isTarget: Boolean,
    attackAnimation: AttackAnimation?,
    modifier: Modifier = Modifier
) {
    val hpFraction = (enemy.hp.toFloat() / enemy.maxHp.toFloat()).coerceIn(0f, 1f)
    val hpColor    = when {
        hpFraction < 0.25f -> DangerRed
        hpFraction < 0.5f  -> EnergyAmber
        else               -> HpGreen
    }

    // Shake animation when hit
    var wasHit by remember { mutableStateOf(false) }
    val offsetX by animateFloatAsState(
        targetValue      = if (wasHit) 8f else 0f,
        animationSpec    = spring(dampingRatio = Spring.DampingRatioHighBouncy),
        label            = "enemy_shake"
    )

    LaunchedEffect(attackAnimation) {
        if (attackAnimation != null && attackAnimation.isPlayerAttack) {
            wasHit = true
            kotlinx.coroutines.delay(100)
            wasHit = false
        }
    }

    val borderColor = if (isTarget) Color(0xFFFF6B6B) else CardBorder

    Column(
        modifier = modifier
            .offset(x = offsetX.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(
                width = if (isTarget) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Enemy name
        Text(
            text       = enemy.name,
            color      = if (enemy.isBoss) Color(0xFFFF4444) else TextPrimary,
            fontSize   = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        )

        if (enemy.isBoss) {
            Text("⚡ BOSS", color = Color(0xFFFFD700), fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        // Enemy sprite (text art)
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (enemy.hp > 0) CardSurface else Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            if (enemy.hp <= 0) {
                Text("💀", fontSize = 32.sp)
            } else {
                Text(
                    text     = getEnemyIcon(enemy.name, enemy.isBoss),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // HP bar
        Text(
            text     = "${enemy.hp}/${enemy.maxHp} HP",
            color    = TextSecondary,
            fontSize = 10.sp
        )
        LinearProgressIndicator(
            progress         = { hpFraction },
            modifier         = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color            = hpColor,
            trackColor       = CardBorder
        )

        // Block badge
        if (enemy.block > 0) {
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🛡 ${enemy.block}", color = BlockGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Status effects
        if (enemy.effects.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                enemy.effects.forEach { EffectBadge(it) }
            }
        }

        // Strength
        if (enemy.strength > 0) {
            Spacer(Modifier.height(2.dp))
            Text("💪 ${enemy.strength} STR", color = Color(0xFFFF8C00), fontSize = 9.sp)
        }

        // Next move
        val nextMove = enemy.moves.getOrNull(enemy.moveIndex % enemy.moves.size)
        if (nextMove != null && enemy.hp > 0) {
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(CardSurface)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (nextMove.type) {
                    "attack" -> "⚔"
                    "block"  -> "🛡"
                    "buff"   -> "✨"
                    else     -> "❓"
                }
                Text(
                    text      = "$icon ${nextMove.label}${if (nextMove.value > 0) " ${nextMove.value}" else ""}",
                    color     = TextSecondary,
                    fontSize  = 9.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Damage popup
        if (attackAnimation != null && attackAnimation.isPlayerAttack && attackAnimation.damage > 0 && enemy.hp > 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                text       = "-${attackAnimation.damage}",
                color      = DangerRed,
                fontSize   = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
private fun EffectBadge(effect: EffectStack) {
    val (icon, color) = when (effect.type) {
        EffectType.POISON     -> "☠" to PoisonGreen
        EffectType.BURN       -> "🔥" to BurnOrange
        EffectType.FREEZE     -> "❄" to FreezeBlue
        EffectType.WEAK       -> "💔" to Color(0xFFE74C3C)
        EffectType.VULNERABLE -> "💢" to Color(0xFFE67E22)
        EffectType.STRENGTH   -> "💪" to Color(0xFFFF8C00)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text("$icon ${effect.stacks}", color = color, fontSize = 8.sp, fontWeight = FontWeight.Bold)
    }
}

private fun getEnemyIcon(name: String, isBoss: Boolean): String = when {
    "Bandit"      in name -> "🗡"
    "Student"     in name -> "🤺"
    "Guard"       in name -> "🛡"
    "Mercenary"   in name -> "⚔"
    "Rival"       in name -> "🤺"
    "Duelist"     in name -> "🏹"
    "Assassin"    in name -> "🌑"
    "General"     in name -> "👑"
    "Champion"    in name -> "🏆"
    "Dragon"      in name -> "🐉"
    "Phantom"     in name -> "👻"
    "Cursed"      in name -> "💀"
    "Warlord"     in name -> "⚔"
    "Fallen"      in name -> "💔"
    "Demon"       in name -> "😈"
    isBoss                -> "👹"
    else                  -> "⚔"
}
