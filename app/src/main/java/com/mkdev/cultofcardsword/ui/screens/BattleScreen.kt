package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.mkdev.cultofcardsword.data.*
import com.mkdev.cultofcardsword.ui.components.CardView
import com.mkdev.cultofcardsword.ui.components.EnemyView
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.BattlePhase
import com.mkdev.cultofcardsword.viewmodel.BattleViewModel
import com.mkdev.cultofcardsword.viewmodel.GameViewModel
import kotlinx.coroutines.delay

@Composable
fun BattleScreen(
    gameVm: GameViewModel,
    battleVm: BattleViewModel,
    onVictory: () -> Unit,
    onDefeat: () -> Unit
) {
    val state        by battleVm.battleState.collectAsState()
    val run          by gameVm.run.collectAsState()
    var selectedCard by remember { mutableStateOf<String?>(null) }
    var errorMsg     by remember { mutableStateOf<String?>(null) }
    var showAnimMsg  by remember { mutableStateOf(false) }

    // ---- Full-screen flash ----
    var flashColor  by remember { mutableStateOf(Color.Transparent) }
    var flashActive by remember { mutableStateOf(false) }
    val flashAlpha  by animateFloatAsState(
        targetValue   = if (flashActive) 0.38f else 0f,
        animationSpec = tween(80),
        label         = "flash_alpha"
    )

    // ---- Slash / impact icon ----
    var slashVisible by remember { mutableStateOf(false) }
    var slashIcon    by remember { mutableStateOf("⚔") }
    val slashScale   by animateFloatAsState(
        targetValue   = if (slashVisible) 1.6f else 0.4f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        label         = "slash_scale"
    )
    val slashAlpha   by animateFloatAsState(
        targetValue   = if (slashVisible) 0f else 1f,
        animationSpec = tween(600, delayMillis = 100, easing = FastOutSlowInEasing),
        label         = "slash_alpha"
    )

    // ---- Player damage float — state lives here so no nested LaunchedEffect can cancel it ----
    var playerDmgValue  by remember { mutableIntStateOf(0) }
    var showPlayerDmg   by remember { mutableStateOf(false) }
    var playerHitActive by remember { mutableStateOf(false) }

    val playerDmgOffsetY by animateFloatAsState(
        targetValue   = if (showPlayerDmg) -44f else 0f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label         = "player_dmg_y"
    )
    val playerDmgAlpha by animateFloatAsState(
        targetValue   = if (showPlayerDmg) 0f else 1f,
        animationSpec = tween(700, delayMillis = 150),
        label         = "player_dmg_alpha"
    )
    val infiniteShake = rememberInfiniteTransition(label = "player_shake")
    val shakeX by infiniteShake.animateFloat(
        initialValue  = -6f,
        targetValue   = 6f,
        animationSpec = infiniteRepeatable(tween(55, easing = LinearEasing), RepeatMode.Reverse),
        label         = "shake_x"
    )
    val activeShakeX = if (playerHitActive) shakeX else 0f

    // ---- Single coroutine drives ALL attack animations ----
    LaunchedEffect(state?.attackAnimation) {
        val anim = state?.attackAnimation ?: return@LaunchedEffect
        showAnimMsg = true

        if (anim.isPlayerAttack) {
            flashColor = Color(getCultColor(run?.cultId?.name ?: "DUAL"))
            slashIcon  = when {
                run?.cultId?.name?.contains("FLAME")     == true -> "🔥"
                run?.cultId?.name?.contains("ICE")       == true -> "❄"
                run?.cultId?.name?.contains("LIGHTNING") == true -> "⚡"
                run?.cultId?.name?.contains("SHADOW")    == true -> "🌑"
                run?.cultId?.name?.contains("HOLY")      == true -> "✨"
                run?.cultId?.name?.contains("DARK")      == true -> "💀"
                run?.cultId?.name?.contains("BLOOD")     == true -> "🩸"
                run?.cultId?.name?.contains("MAGIC")     == true -> "✨"
                anim.damage >= 25                                 -> "💥"
                else                                              -> "⚔"
            }
        } else {
            // Enemy attacks player
            flashColor = if (anim.damage > 0) Color.Red else Color.Blue
            slashIcon  = if (anim.damage > 0) "💥" else "🛡"

            if (anim.damage > 0) {
                // Trigger player damage float from this coroutine — cannot be cancelled by recomposition
                playerDmgValue  = anim.damage
                showPlayerDmg   = false      // snap back to start position
                playerHitActive = true
                delay(20)                    // one frame so the snap propagates
                showPlayerDmg   = true       // begin float-up + fade-out
                delay(180)
                playerHitActive = false      // stop shake
                delay(560)
                showPlayerDmg   = false      // number fades/returns
            }
        }

        // Flash + slash fire for both attack directions
        flashActive  = true
        slashVisible = true
        delay(120)
        flashActive  = false
        delay(500)
        slashVisible = false
        delay(400)

        showAnimMsg = false
        battleVm.clearAnimation()
    }

    LaunchedEffect(errorMsg) {
        if (errorMsg != null) { delay(1800); errorMsg = null }
    }

    LaunchedEffect(state?.phase) {
        when (state?.phase) {
            BattlePhase.VICTORY -> {
                val s = state ?: return@LaunchedEffect
                delay(1200)
                gameVm.onBattleWon(s)
                onVictory()
            }
            BattlePhase.DEFEAT -> { delay(1500); onDefeat() }
            else -> {}
        }
    }

    val s = state ?: run {
        Box(Modifier.fillMaxSize().background(DeepBlack), Alignment.Center) {
            CircularProgressIndicator(color = SwordGold)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 12.dp)
        ) {
            // ---- Player stats bar ----
            PlayerStatsBar(
                hp           = s.playerHp,
                maxHp        = s.playerMaxHp,
                block        = s.playerBlock,
                mana         = s.playerMana,
                maxMana      = s.playerMaxMana,
                energy       = s.energy,
                maxEnergy    = s.maxEnergy,
                rank         = run?.swordsmanRank,
                turn         = s.turn,
                cultId       = run?.cultId,
                dmgValue     = playerDmgValue,
                showDmg      = showPlayerDmg,
                dmgOffsetY   = playerDmgOffsetY,
                dmgAlpha     = playerDmgAlpha,
                shakeOffsetX = activeShakeX
            )

            Spacer(Modifier.height(8.dp))

            // ---- Message / animation row ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardSurface.copy(alpha = 0.7f))
                    .padding(vertical = 6.dp, horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                val displayMsg = errorMsg ?: if (showAnimMsg && s.attackAnimation != null) {
                    val anim = s.attackAnimation
                    if (anim.isPlayerAttack) {
                        if (anim.targetEnemyIdx == -1) "⚔ ${anim.attackerName}: ${anim.damage} to all!"
                        else "⚔ ${anim.attackerName} deals ${anim.damage} damage!"
                    } else {
                        if (anim.damage > 0) "💥 ${anim.attackerName} strikes you for ${anim.damage}!"
                        else "🛡 ${anim.attackerName}'s attack is blocked!"
                    }
                } else s.message

                Text(
                    text       = displayMsg,
                    color      = when {
                        errorMsg != null                                                                                   -> DangerRed
                        showAnimMsg && s.attackAnimation?.isPlayerAttack == true                                           -> SwordGold
                        showAnimMsg && s.attackAnimation?.isPlayerAttack == false && (s.attackAnimation?.damage ?: 0) > 0 -> DangerRed
                        else                                                                                               -> TextPrimary
                    },
                    fontSize   = 11.sp,
                    fontWeight = if (errorMsg != null || showAnimMsg) FontWeight.Bold else FontWeight.Normal,
                    textAlign  = TextAlign.Center
                )
            }

            Spacer(Modifier.height(8.dp))

            // ---- Enemies ----
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
            ) {
                s.enemies.forEachIndexed { idx, enemy ->
                    EnemyView(
                        enemy           = enemy,
                        isTarget        = idx == 0 && enemy.hp > 0,
                        attackAnimation = if (showAnimMsg) s.attackAnimation else null,
                        modifier        = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // ---- Cards played hint ----
            if (s.phase == BattlePhase.PLAYER) {
                val cardUsed = s.cardsPlayedThisTurn >= 1
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text     = if (cardUsed) "Card used — End Turn to continue" else "Double-tap a card to strike",
                        color    = if (cardUsed) EnergyAmber else TextSecondary,
                        fontSize = 10.sp
                    )
                }
                Spacer(Modifier.height(4.dp))
            }

            // ---- Hand ----
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier              = Modifier.fillMaxWidth()
            ) {
                items(s.hand, key = { it.id }) { card ->
                    val alreadyPlayed = s.cardsPlayedThisTurn >= 1
                    val isPlayable    = s.phase == BattlePhase.PLAYER &&
                            card.cost <= s.energy &&
                            card.manaCost <= s.playerMana &&
                            !alreadyPlayed
                    val isSelected = selectedCard == card.id

                    CardView(
                        card        = card,
                        isPlayable  = isPlayable,
                        isSelected  = isSelected,
                        onSingleTap = { selectedCard = if (isSelected) null else card.id },
                        onDoubleTap = {
                            val r = run ?: return@CardView
                            val err = battleVm.playCard(card, r)
                            if (err != null) errorMsg = err
                            else { selectedCard = null; errorMsg = null }
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // ---- Deck info + End Turn ----
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Draw",    color = TextSecondary, fontSize = 9.sp)
                    Text("${s.drawPile.size}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hand",    color = TextSecondary, fontSize = 9.sp)
                    Text("${s.hand.size}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Discard", color = TextSecondary, fontSize = 9.sp)
                    Text("${s.discardPile.size}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick  = { if (s.phase == BattlePhase.PLAYER) battleVm.endTurn() },
                    enabled  = s.phase == BattlePhase.PLAYER,
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = if (s.phase == BattlePhase.PLAYER) SwordGold else CardBorder,
                        contentColor   = if (s.phase == BattlePhase.PLAYER) Color.Black else TextSecondary
                    ),
                    modifier = Modifier.height(44.dp).width(110.dp),
                    shape    = RoundedCornerShape(10.dp)
                ) {
                    Text("End Turn", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        // ---- Full-screen flash ----
        if (flashAlpha > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(flashAlpha)
                    .background(flashColor)
            )
        }

        // ---- Slash / impact icon ----
        if (slashVisible || slashAlpha < 1f) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text     = slashIcon,
                    fontSize = 72.sp,
                    modifier = Modifier
                        .scale(slashScale)
                        .alpha(1f - slashAlpha)
                )
            }
        }

        // ---- Victory / Defeat overlays ----
        when (s.phase) {
            BattlePhase.VICTORY -> PhaseOverlay("⚔ VICTORY!", SwordGold, "The enemy has fallen.")
            BattlePhase.DEFEAT  -> PhaseOverlay("💀 DEFEATED", DangerRed, "You have fallen in battle...")
            else                -> {}
        }
    }
}

// ---------------------------------------------------------------------------
// PlayerStatsBar — receives pre-computed animation values from BattleScreen
// ---------------------------------------------------------------------------
@Composable
private fun PlayerStatsBar(
    hp: Int, maxHp: Int,
    block: Int,
    mana: Int, maxMana: Int,
    energy: Int, maxEnergy: Int,
    rank: SwordsmanRank?,
    turn: Int,
    cultId: CultId?,
    dmgValue: Int,
    showDmg: Boolean,
    dmgOffsetY: Float,
    dmgAlpha: Float,
    shakeOffsetX: Float
) {
    val hpFraction   = (hp.toFloat() / maxHp.toFloat()).coerceIn(0f, 1f)
    val manaFraction = if (maxMana > 0) (mana.toFloat() / maxMana.toFloat()).coerceIn(0f, 1f) else 0f
    val cultColor    = cultId?.let { Color(it.color) } ?: SwordGold

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(DarkSurface)
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.offset(x = shakeOffsetX.dp)) {

            // Name + rank + turn
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = cultId?.displayName ?: "Swordsman",
                    color      = cultColor,
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Turn $turn", color = TextSecondary, fontSize = 9.sp)
                Text(rank?.displayName ?: "", color = AccentGold, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(4.dp))

            // HP row — Box lets the floating damage number overlay it
            Box {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier              = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text       = "❤ $hp/$maxHp",
                        color      = HpGreen,
                        fontSize   = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier   = Modifier.width(72.dp)
                    )
                    LinearProgressIndicator(
                        progress   = { hpFraction },
                        modifier   = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color      = when {
                            hpFraction < 0.25f -> DangerRed
                            hpFraction < 0.5f  -> EnergyAmber
                            else               -> HpGreen
                        },
                        trackColor = CardBorder
                    )
                    if (block > 0) Text("🛡$block", color = BlockGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Floating "-N" that rises from the HP bar when an enemy hits
                if (dmgValue > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = 76.dp, y = dmgOffsetY.dp)
                            // dmgAlpha: 1→0 as it rises; we invert so number is opaque while rising, then transparent
                            .alpha(1f - dmgAlpha)
                    ) {
                        // Drop shadow
                        Text(
                            text       = "-$dmgValue",
                            color      = Color.Black.copy(alpha = 0.6f),
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier   = Modifier.offset(x = 1.dp, y = 1.dp)
                        )
                        Text(
                            text       = "-$dmgValue",
                            color      = if (dmgValue >= 15) Color(0xFFFF6600) else DangerRed,
                            fontSize   = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            Spacer(Modifier.height(3.dp))

            // Mana + energy row
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text       = "💧$mana/$maxMana",
                    color      = ManaBlue,
                    fontSize   = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.width(72.dp)
                )
                LinearProgressIndicator(
                    progress   = { manaFraction },
                    modifier   = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color      = ManaBlue,
                    trackColor = CardBorder
                )
                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    repeat(maxEnergy) { idx ->
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (idx < energy) EnergyAmber else CardBorder)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhaseOverlay(title: String, color: Color, subtitle: String) {
    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.65f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = color, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(8.dp))
            Text(subtitle, color = TextSecondary, fontSize = 14.sp)
        }
    }
}
