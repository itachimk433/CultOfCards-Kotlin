package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.data.Quest
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun QuestsScreen(gameVm: GameViewModel, navController: NavHostController) {
    val quests by gameVm.quests.collectAsState()
    val globalProgress by gameVm.globalProgress.collectAsState()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = TextSecondary)
                }
                Column {
                    Text("Quests", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
                    Text("${quests.count { it.completed }}/${quests.size} completed", color = TextMuted, fontSize = 12.sp)
                }
            }

            // Global stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVariant)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuestStat("Runs", "${globalProgress.totalRuns}", Purple)
                QuestStat("Battles Won", "${globalProgress.battlesWon}", HpGreen)
                QuestStat("Enemies Killed", "${globalProgress.totalEnemiesKilled}", HpRed)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quests) { quest ->
                    QuestItem(quest = quest, globalProgress = globalProgress)
                }
            }
        }
    }
}

@Composable
private fun QuestItem(quest: Quest, globalProgress: com.cultofcards.data.GlobalProgress) {
    val progress = when (quest.trackKey) {
        "battlesWon" -> globalProgress.battlesWon
        "lowHpWins" -> globalProgress.lowHpWins
        "totalEnemiesKilled" -> globalProgress.totalEnemiesKilled
        "attackOnlyWins" -> globalProgress.attackOnlyWins
        else -> 0
    }
    val pct = if (quest.goal > 0) (progress.toFloat() / quest.goal.toFloat()).coerceIn(0f, 1f) else 0f
    val isCompleted = quest.completed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isCompleted) 0.6f else 1f)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isCompleted) SurfaceVariant.copy(0.5f) else SurfaceVariant)
            .border(
                1.dp,
                if (isCompleted) HpGreen.copy(0.3f) else Border,
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (isCompleted) "✓" else "○", color = if (isCompleted) HpGreen else TextMuted, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = quest.name,
                        color = if (isCompleted) HpGreen else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                // Reward badge
                val rewardText = when (quest.rewardType) {
                    "common_card" -> "Common Card"
                    "uncommon_card" -> "Uncommon Card"
                    "rare_card" -> "Rare Card"
                    "relic" -> "Relic"
                    "gold" -> "${quest.rewardAmount} Gold"
                    else -> quest.rewardType
                }
                val rewardColor = when (quest.rewardType) {
                    "rare_card" -> StrengthGold
                    "uncommon_card" -> BlockBlue
                    "relic" -> PurpleBright
                    "gold" -> StrengthGold
                    else -> TextMuted
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(rewardColor.copy(0.15f))
                        .border(0.5.dp, rewardColor.copy(0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(rewardText, color = rewardColor, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Text(quest.description, color = TextSecondary, fontSize = 13.sp)

            if (!isCompleted) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Progress", color = TextMuted, fontSize = 11.sp)
                        Text("$progress / ${quest.goal}", color = TextMuted, fontSize = 11.sp)
                    }
                    LinearProgressIndicator(
                        progress = { pct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Purple,
                        trackColor = Surface
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, color = TextMuted, fontSize = 10.sp)
    }
}
