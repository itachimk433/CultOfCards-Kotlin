package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.data.*
import com.cultofcards.ui.components.CardView
import com.cultofcards.ui.components.EnemyView
import com.cultofcards.ui.navigation.Routes
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.BattlePhase
import com.cultofcards.viewmodel.BattleViewModel
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun BattleScreen(
    gameVm: GameViewModel,
    battleVm: BattleViewModel,
    navController: NavHostController
) {
    val run by gameVm.run.collectAsState()
    val battleState by battleVm.battleState.collectAsState()
    var selectedCard by remember { mutableStateOf<GameCard?>(null) }

    // Init battle on entry
    LaunchedEffect(Unit) {
        val r = run ?: run {
            navController.navigate(Routes.MAIN_MENU) { popUpTo(0) }
            return@LaunchedEffect
        }
        val floor = gameVm.getCurrentFloor() ?: run {
            navController.navigate(Routes.CAMPAIGN)
            return@LaunchedEffect
        }
        battleVm.initBattle(r, floor)
    }

    // Handle victory/defeat navigation
    LaunchedEffect(battleState?.phase) {
        when (battleState?.phase) {
            BattlePhase.VICTORY -> {
                // Wait a moment then navigate
            }
            BattlePhase.DEFEAT -> {
                // Wait a moment then navigate
            }
            else -> {}
        }
    }

    if (battleState == null || run == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(BackgroundDeep),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Purple)
        }
        return
    }

    val s = battleState!!
    val r = run!!

    // Victory screen
    if (s.phase == BattlePhase.VICTORY) {
        ResultScreen(
            isVictory = true,
            hpRemaining = s.playerHp,
            onContinue = {
                val floor = gameVm.getCurrentFloor()
                val enemiesKilled = s.enemies.size
                gameVm.completeFloor(s.playerHp, enemiesKilled, !s.usedSkills)
                gameVm.updateRun { copy(playerHp = s.playerHp) }

                if (floor?.isBoss == true) {
                    val relic = getRandomRelic(r.relics.map { it.id })
                    gameVm.addRelic(relic)
                    gameVm.setPendingRelic(relic)
                    gameVm.advanceFloor()
                    battleVm.clearBattle()
                    navController.navigate(Routes.RELIC_REWARD) {
                        popUpTo(Routes.CAMPAIGN)
                    }
                } else {
                    val cards = getRewardCards(r.cultId)
                    gameVm.setPendingRewardCards(cards)
                    gameVm.advanceFloor()
                    battleVm.clearBattle()
                    navController.navigate(Routes.REWARD) {
                        popUpTo(Routes.CAMPAIGN)
                    }
                }
            }
        )
        return
    }

    // Defeat screen
    if (s.phase == BattlePhase.DEFEAT) {
        ResultScreen(
            isVictory = false,
            hpRemaining = 0,
            onContinue = {
                gameVm.endRun()
                battleVm.clearBattle()
                navController.navigate(Routes.GAME_OVER) {
                    popUpTo(0)
                }
            }
        )
        return
    }

    // Main battle UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0018), Color(0xFF08000D), Color(0xFF100005))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Top bar: turn + deck count + energy
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Turn ${s.turn}", color = TextMuted, fontSize = 12.sp)
                    Icon(Icons.Default.Style, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                    Text("${s.drawPile.size}", color = TextMuted, fontSize = 12.sp)
                }
                // Energy dots
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically) {
                    repeat(s.maxEnergy) { idx ->
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (idx < s.energy) EnergyAmber else EnergyAmber.copy(0.18f))
                        )
                    }
                    Text("${s.energy}/${s.maxEnergy}", color = EnergyAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(start = 2.dp))
                }
                // Deck view button
                TextButton(onClick = { navController.navigate(Routes.DECK) }) {
                    Text("Deck", color = TextMuted, fontSize = 12.sp)
                }
            }

            // Player HP bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val hpPct = (s.playerHp.toFloat() / s.playerMaxHp.toFloat()).coerceIn(0f, 1f)
                val hpColor = when {
                    hpPct > 0.5f -> HpGreen
                    hpPct > 0.25f -> HpYellow
                    else -> HpRed
                }
                Text("❤", fontSize = 12.sp)
                LinearProgressIndicator(
                    progress = { hpPct },
                    modifier = Modifier
                        .weight(1f)
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = hpColor,
                    trackColor = SurfaceVariant
                )
                Text("${s.playerHp}/${s.playerMaxHp}", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                if (s.playerBlock > 0) {
                    Text("🛡 ${s.playerBlock}", color = BlockBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                // Relics
                r.relics.forEach { _ ->
                    Text("◆", color = Purple, fontSize = 10.sp)
                }
            }

            // Enemies
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val aliveEnemies = s.enemies.filter { it.hp > 0 }
                items(aliveEnemies) { enemy ->
                    EnemyView(
                        enemy = enemy,
                        isActive = aliveEnemies.indexOf(enemy) == 0
                    )
                }
            }

            // Battle message
            Text(
                text = s.message,
                color = TextMuted,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // Hand of cards
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface.copy(alpha = 0.9f))
            ) {
                Column {
                    // Cards row
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (s.hand.isEmpty()) {
                            Box(
                                modifier = Modifier.size(width = 120.dp, height = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No cards in hand", color = TextMuted, fontSize = 12.sp)
                            }
                        }
                        s.hand.forEach { card ->
                            CardView(
                                card = card,
                                onClick = {
                                    if (selectedCard?.id == card.id) {
                                        // Play on second tap
                                        val error = battleVm.playCard(card, r)
                                        selectedCard = null
                                        if (error != null) { /* show snackbar if needed */ }
                                    } else {
                                        selectedCard = card
                                    }
                                },
                                disabled = card.cost > s.energy || s.phase != BattlePhase.PLAYER,
                                selected = selectedCard?.id == card.id
                            )
                        }
                    }

                    // Selected card play hint + End Turn
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (selectedCard != null) {
                            Text(
                                text = "Tap again to play ${selectedCard!!.name}",
                                color = EnergyAmber,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Button(
                            onClick = {
                                selectedCard = null
                                battleVm.endTurn()
                            },
                            enabled = s.phase == BattlePhase.PLAYER,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Purple,
                                disabledContainerColor = SurfaceVariant
                            ),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text(
                                text = if (s.phase == BattlePhase.PLAYER) "End Turn ▶" else "Enemy Turn…",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }
}

@Composable
private fun ResultScreen(
    isVictory: Boolean,
    hpRemaining: Int,
    onContinue: () -> Unit
) {
    val bgColor = if (isVictory) Color(0xFF001500) else Color(0xFF150000)
    val accentColor = if (isVictory) HpGreen else HpRed
    val icon = if (isVictory) "✓" else "💀"
    val title = if (isVictory) "VICTORY" else "DEFEATED"
    val sub = if (isVictory) "HP Remaining: $hpRemaining" else "The darkness claims you."
    val btnText = if (isVictory) "Continue →" else "Accept Death"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        if (isVictory) Color(0xFF002200) else Color(0xFF220000),
                        bgColor
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = icon, fontSize = 80.sp)
            Text(
                text = title,
                color = accentColor,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
                letterSpacing = 8.sp
            )
            Text(text = sub, color = TextSecondary, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier.height(52.dp).widthIn(min = 200.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text(btnText, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
        }
    }
}
