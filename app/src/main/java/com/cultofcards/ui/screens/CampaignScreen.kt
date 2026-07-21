package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.data.*
import com.cultofcards.ui.navigation.Routes
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun CampaignScreen(gameVm: GameViewModel, navController: NavHostController) {
    val run by gameVm.run.collectAsState()

    if (run == null) {
        LaunchedEffect(Unit) { navController.navigate(Routes.MAIN_MENU) { popUpTo(0) } }
        return
    }

    val r = run!!
    val act = getStoryAct(r.act)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = act?.title ?: "Victory!",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary
                            )
                            Text(
                                text = "Act ${r.act} · ${r.cultId.displayName}",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // HP
                            StatBadge(
                                label = "HP",
                                value = "${r.playerHp}/${r.playerMaxHp}",
                                color = if (r.playerHp <= r.playerMaxHp * 0.25) HpRed else HpGreen
                            )
                            // Gold
                            StatBadge(label = "Gold", value = "${r.gold}", color = StrengthGold)
                        }
                    }

                    if (r.relics.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            r.relics.forEach { relic ->
                                RelicBadge(relic = relic)
                            }
                        }
                    }
                }
            }

            // Act intro (only if at floor 1)
            if (r.floor == 1 && act != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariant)
                        .padding(12.dp)
                ) {
                    Text(text = act.intro, color = TextSecondary, fontSize = 13.sp, lineHeight = 20.sp)
                }
            }

            // Floor list
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (act != null) {
                    items(act.floors) { floor ->
                        val isCompleted = "${r.act}-${floor.floor}" in r.completedFloors
                        val isCurrent = floor.floor == r.floor
                        FloorItem(
                            floor = floor,
                            isCompleted = isCompleted,
                            isCurrent = isCurrent,
                            onClick = {
                                if (isCurrent) {
                                    when (floor.type) {
                                        "boss" -> navController.navigate(Routes.STORY)
                                        "event" -> handleEvent(gameVm, navController)
                                        else -> navController.navigate(Routes.BATTLE)
                                    }
                                }
                            }
                        )
                    }
                } else {
                    // Campaign complete
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🏆", fontSize = 48.sp)
                                Text("Campaign Complete!", color = StrengthGold, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { navController.navigate(Routes.VICTORY) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Purple)
                                ) {
                                    Text("Claim Victory")
                                }
                            }
                        }
                    }
                }
            }

            // Bottom actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Routes.DECK) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleBright)
                ) {
                    Icon(Icons.Default.Style, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Deck (${r.deck.size})")
                }
                OutlinedButton(
                    onClick = { navController.navigate(Routes.QUESTS) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StrengthGold)
                ) {
                    Text("Quests")
                }
            }
        }
    }
}

private fun handleEvent(gameVm: GameViewModel, navController: NavHostController) {
    val run = gameVm.run.value ?: return
    val floor = gameVm.getCurrentFloor() ?: return
    when (floor.eventReward) {
        "relic" -> {
            val relic = getRandomRelic(run.relics.map { it.id })
            gameVm.addRelic(relic)
            gameVm.setPendingRelic(relic)
            gameVm.advanceFloor()
            navController.navigate(Routes.RELIC_REWARD)
        }
        "card" -> {
            val cards = getRewardCards(run.cultId)
            gameVm.setPendingRewardCards(cards)
            gameVm.advanceFloor()
            navController.navigate(Routes.REWARD)
        }
        "heal" -> {
            gameVm.updateRun {
                copy(playerHp = minOf(playerMaxHp, playerHp + 15))
            }
            gameVm.advanceFloor()
        }
        else -> {
            gameVm.advanceFloor()
        }
    }
}

@Composable
private fun FloorItem(
    floor: StoryFloor,
    isCompleted: Boolean,
    isCurrent: Boolean,
    onClick: () -> Unit
) {
    val floorColor = when {
        floor.isBoss -> Color(0xFFAA00AA)
        floor.type == "event" -> StrengthGold
        else -> Purple
    }
    val alpha = when {
        isCompleted -> 0.35f
        isCurrent -> 1f
        else -> 0.5f
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isCurrent) SurfaceVariant else Surface)
            .border(
                width = if (isCurrent) 1.5.dp else 1.dp,
                color = if (isCurrent) floorColor else Border,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(enabled = isCurrent) { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Floor number circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) floorColor.copy(0.1f)
                    else if (isCurrent) floorColor.copy(0.25f)
                    else floorColor.copy(0.08f)
                )
                .border(1.dp, floorColor.copy(alpha), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text("✓", color = floorColor.copy(alpha), fontSize = 14.sp, fontWeight = FontWeight.Bold)
            } else {
                Text("${floor.floor}", color = floorColor.copy(alpha), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            val floorLabel = when {
                floor.isBoss -> "Boss Battle"
                floor.type == "event" -> "Event"
                else -> "Battle"
            }
            val typeIcon = when {
                floor.isBoss -> "💀"
                floor.type == "event" -> "✦"
                else -> "⚔"
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(typeIcon, fontSize = 14.sp)
                Text(
                    text = floorLabel,
                    color = if (isCompleted) TextMuted else TextPrimary,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
            val enemyNames = floor.enemyIds.mapNotNull { com.cultofcards.data.ENEMIES[it]?.name }
            if (enemyNames.isNotEmpty()) {
                Text(
                    text = enemyNames.joinToString(" + "),
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            if (floor.type == "event" && floor.eventText.isNotEmpty()) {
                Text(
                    text = floor.eventText.take(60) + "…",
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        if (isCurrent && !isCompleted) {
            Text("▶", color = floorColor, fontSize = 16.sp)
        }
    }
}

@Composable
private fun StatBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = color, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = label, color = TextMuted, fontSize = 10.sp)
    }
}

@Composable
private fun RelicBadge(relic: Relic) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Purple.copy(0.2f))
            .border(0.5.dp, Purple.copy(0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text = relic.name, color = PurpleBright, fontSize = 10.sp)
    }
}
