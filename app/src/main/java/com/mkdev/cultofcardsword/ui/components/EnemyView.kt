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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import kotlinx.coroutines.delay

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

    // ---- Hit state ----
    var hitState by remember { mutableStateOf(false) }

    // Shake: oscillating horizontal offset
    val infiniteShake = rememberInfiniteTransition(label = "shake")
    val shakeOffset by infiniteShake.animateFloat(
        initialValue  = -10f,
        targetValue   = 10f,
        animationSpec = infiniteRepeatable(tween(60, easing = LinearEasing), RepeatMode.Reverse),
        label         = "shake_offset"
    )
    val activeShakeX = if (hitState) shakeOffset else 0f

    // Flash: red tint overlay alpha
    val flashAlpha by animateFloatAsState(
        targetValue   = if (hitState) 0.45f else 0f,
        animationSpec = tween(durationMillis = 100),
        label         = "hit_flash"
    )

    // Sprite scale pulse on hit
    val spriteScale by animateFloatAsState(
        targetValue   = if (hitState) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessHigh),
        label         = "sprite_scale"
    )

    // Damage float-up
    var showDmg by remember { mutableStateOf(false) }
    var dmgValue by remember { mutableIntStateOf(0) }
    val dmgOffsetY by animateFloatAsState(
        targetValue   = if (showDmg) -52f else -8f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label         = "dmg_float"
    )
    val dmgAlpha by animateFloatAsState(
        targetValue   = if (showDmg) 0f else 1f,
        animationSpec = tween(durationMillis = 700, delayMillis = 200),
        label         = "dmg_alpha"
    )

    LaunchedEffect(attackAnimation) {
        val anim = attackAnimation
        if (anim != null && anim.isPlayerAttack && anim.damage > 0) {
            dmgValue  = anim.damage
            showDmg   = false   // reset before re-triggering
            hitState  = true
            delay(20)
            showDmg   = true
            delay(150)
            hitState  = false
            delay(600)
            showDmg   = false
        }
    }

    val borderColor = if (isTarget) Color(0xFFFF6B6B) else CardBorder

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .offset(x = activeShakeX.dp)
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

            // Enemy sprite box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .scale(spriteScale)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (enemy.hp > 0) {
                            if (hitState) Color(0xFF3A0000) else CardSurface
                        } else Color.DarkGray
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (enemy.hp <= 0) {
                    Text("💀", fontSize = 32.sp)
                } else {
                    Text(
                        text      = getEnemyIcon(enemy.name, enemy.isBoss),
                        fontSize  = 30.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Red flash overlay
                if (flashAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red.copy(alpha = flashAlpha))
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
                progress   = { hpFraction },
                modifier   = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color      = hpColor,
                trackColor = CardBorder
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

            // Strength badge
            if (enemy.strength > 0) {
                Spacer(Modifier.height(2.dp))
                Text("💪 ${enemy.strength} STR", color = Color(0xFFFF8C00), fontSize = 9.sp)
            }

            // Next move preview
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
                        color     = when (nextMove.type) {
                            "attack" -> Color(0xFFFF8A80)
                            "block"  -> BlockGray
                            else     -> AccentGold
                        },
                        fontSize  = 9.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // ---- Floating damage popup (overlaid outside the Column) ----
        if (dmgValue > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = dmgOffsetY.dp)
                    .alpha(1f - dmgAlpha) // inverted: start opaque, fade to 0
            ) {
                // Shadow text
                Text(
                    text       = "-$dmgValue",
                    color      = Color.Black.copy(alpha = 0.5f),
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier   = Modifier.offset(x = 1.dp, y = 1.dp)
                )
                Text(
                    text       = "-$dmgValue",
                    color      = if (dmgValue >= 20) Color(0xFFFFD700) else DangerRed,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
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
