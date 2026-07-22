package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.mkdev.cultofcardsword.data.Quest
import com.mkdev.cultofcardsword.ui.theme.*
import com.mkdev.cultofcardsword.viewmodel.GameViewModel

@Composable
fun QuestsScreen(
    gameVm: GameViewModel,
    onBack: () -> Unit
) {
    val quests by gameVm.quests.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier          = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextSecondary)
                }
                Text(
                    "⚔ Sword Quests",
                    color      = SwordGold,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.size(48.dp))
            }

            val completed = quests.count { it.completed }
            Text(
                "$completed / ${quests.size} quests completed",
                color     = TextSecondary,
                fontSize  = 11.sp,
                modifier  = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(quests, key = { it.id }) { quest ->
                    QuestCard(quest = quest, progress = gameVm.getQuestProgress(quest))
                }
            }
        }
    }
}

@Composable
private fun QuestCard(quest: Quest, progress: Int) {
    val isDone        = quest.completed
    val fraction      = (progress.toFloat() / quest.goal.toFloat()).coerceIn(0f, 1f)
    val borderColor   = if (isDone) SwordGold else CardBorder
    val bgColor       = if (isDone) DarkSurface else CardSurface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text      = if (isDone) "✅" else "📜",
                        fontSize  = 16.sp,
                        modifier  = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            quest.name,
                            color      = if (isDone) SwordGold else TextPrimary,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            quest.description,
                            color    = TextSecondary,
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }

                // Reward badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDone) SwordGold.copy(alpha = 0.2f) else CardBorder.copy(alpha = 0.5f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text     = rewardIcon(quest.rewardType),
                        fontSize = 11.sp
                    )
                }
            }

            if (!isDone) {
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    LinearProgressIndicator(
                        progress   = { fraction },
                        modifier   = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color      = SwordGold,
                        trackColor = CardBorder
                    )
                    Text(
                        "$progress / ${quest.goal}",
                        color    = TextSecondary,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

private fun rewardIcon(type: String): String = when (type) {
    "common_card"   -> "🃏 Card"
    "uncommon_card" -> "🃏 Rare"
    "rare_card"     -> "🃏 Epic"
    "gold"          -> "💰 Gold"
    "relic"         -> "⚗️ Relic"
    else            -> "🎁"
}
