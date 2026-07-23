package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.*
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.GameViewModel

@Composable
fun CampaignScreen(
    gameVm: GameViewModel,
    onStartFloor: (actId: Int, floorNum: Int) -> Unit,
    onViewDeck: () -> Unit,
    onViewQuests: () -> Unit,
    onQuit: () -> Unit
) {
    val run by gameVm.run.collectAsState()
    val r = run ?: return

    val act = getStoryAct(r.act)

    // Quit confirmation state
    var showQuitConfirm by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding()
        ) {
            // ---- Header ----
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(DarkSurface)
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text       = act?.title ?: "Victory!",
                                color      = SwordGold,
                                fontSize   = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text  = "Act ${r.act} · ${r.cultId.displayName}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text  = "${r.playerHp}/${r.playerMaxHp}",
                                    color = if (r.playerHp <= r.playerMaxHp * 0.25) DangerRed else HpGreen,
                                    fontSize   = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("❤ HP", color = TextSecondary, fontSize = 9.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${r.gold}", color = EnergyAmber, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("💰 Gold", color = TextSecondary, fontSize = 9.sp)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${r.swordsmanRank.displayName}", color = AccentGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("Rank", color = TextSecondary, fontSize = 9.sp)
                            }
                        }
                    }

                    // Relics
                    if (r.relics.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            r.relics.forEach { relic ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SwordGold.copy(alpha = 0.15f))
                                        .border(0.5.dp, SwordGold.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(relic.name, color = AccentGold, fontSize = 9.sp)
                                }
                            }
                        }
                    }
                }
            }

            // ---- Act intro ----
            if (r.floor == 1 && act != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardSurface)
                        .padding(10.dp)
                ) {
                    Text(
                        text       = act.intro,
                        color      = TextSecondary,
                        fontSize   = 11.sp,
                        lineHeight = 15.sp,
                        textAlign  = TextAlign.Center
                    )
                }
            }

            // ---- Floor list ----
            LazyColumn(
                modifier            = Modifier.weight(1f).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding      = PaddingValues(vertical = 8.dp)
            ) {
                val floors = act?.floors ?: emptyList()
                items(floors) { floor ->
                    val isCompleted  = r.completedFloors.contains("${r.act}-${floor.floor}")
                    val isCurrent    = floor.floor == r.floor && !isCompleted
                    val isLocked     = floor.floor > r.floor

                    FloorCard(
                        floor       = floor,
                        isCompleted = isCompleted,
                        isCurrent   = isCurrent,
                        isLocked    = isLocked,
                        onClick     = {
                            if (isCurrent) onStartFloor(r.act, floor.floor)
                        }
                    )
                }
            }

            // ---- Bottom actions ----
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick  = onViewDeck,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = AccentGold),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, AccentGold.copy(alpha = 0.5f)),
                    shape    = RoundedCornerShape(8.dp)
                ) {
                    Text("🃏 Deck (${r.deck.size})", fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick  = onViewQuests,
                    modifier = Modifier.weight(1f),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    shape    = RoundedCornerShape(8.dp)
                ) {
                    Text("📜 Quests", fontSize = 13.sp)
                }
                // Quit — shows confirmation before abandoning the run
                IconButton(onClick = { showQuitConfirm = true }) {
                    Icon(Icons.Default.ExitToApp, null, tint = DangerRed)
                }
            }
        }

        // ---- Abandon run confirmation dialog ----
        if (showQuitConfirm) {
            AlertDialog(
                onDismissRequest = { showQuitConfirm = false },
                containerColor   = DarkSurface,
                title = {
                    Text(
                        "Abandon Your Path?",
                        color      = SwordGold,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp
                    )
                },
                text = {
                    Text(
                        "You have chosen the ${r.cultId.displayName} path. Leaving now will permanently end this run and all progress will be lost.",
                        color    = TextSecondary,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        showQuitConfirm = false
                        onQuit()
                    }) {
                        Text("Abandon Run", color = DangerRed, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showQuitConfirm = false }) {
                        Text("Keep Fighting", color = HpGreen)
                    }
                }
            )
        }
    }
}

@Composable
private fun FloorCard(
    floor: StoryFloor,
    isCompleted: Boolean,
    isCurrent: Boolean,
    isLocked: Boolean,
    onClick: () -> Unit
) {
    val floorColor = when {
        isCompleted -> HpGreen
        isCurrent   -> SwordGold
        floor.isBoss -> DangerRed
        floor.type == "event" -> AccentGold
        else        -> TextSecondary
    }

    val floorLabel = when {
        isCompleted  -> "✅ Floor ${floor.floor}"
        isCurrent    -> "⚔ Floor ${floor.floor} — YOUR TURN"
        isLocked     -> "🔒 Floor ${floor.floor}"
        floor.isBoss -> "💀 Floor ${floor.floor} — BOSS"
        floor.type == "event" -> "📖 Floor ${floor.floor} — Event"
        else         -> "Floor ${floor.floor}"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isCurrent) CardSurface else DarkSurface)
            .border(
                width = if (isCurrent) 2.dp else 1.dp,
                color = if (isCurrent) floorColor else CardBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .then(if (isCurrent) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(12.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = floorLabel,
                    color      = if (isLocked) TextSecondary.copy(alpha = 0.4f) else floorColor,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    fontSize   = 13.sp
                )
                val enemyNames = floor.enemyIds.mapNotNull { ENEMIES[it]?.name }
                if (enemyNames.isNotEmpty()) {
                    Text(
                        text     = enemyNames.joinToString(" + "),
                        color    = TextSecondary.copy(alpha = if (isLocked) 0.3f else 0.7f),
                        fontSize = 11.sp
                    )
                }
                if (floor.type == "event" && floor.eventText.isNotEmpty()) {
                    Text(
                        text      = floor.eventText.take(70) + "…",
                        color     = TextSecondary.copy(alpha = 0.5f),
                        fontSize  = 10.sp,
                        lineHeight = 13.sp
                    )
                }
            }
            if (isCurrent) {
                Text("▶", color = SwordGold, fontSize = 18.sp)
            }
        }
    }
}
