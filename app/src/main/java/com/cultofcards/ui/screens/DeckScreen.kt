package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.data.CardType
import com.cultofcards.ui.components.CardView
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun DeckScreen(gameVm: GameViewModel, navController: NavHostController) {
    val run by gameVm.run.collectAsState()
    val deck = run?.deck ?: emptyList()

    val attackCount = deck.count { it.type == CardType.ATTACK }
    val skillCount = deck.count { it.type == CardType.SKILL }
    val powerCount = deck.count { it.type == CardType.POWER }

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
            // Top bar
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your Deck",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                    Text(
                        text = "${deck.size} cards total",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
            }

            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVariant)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DeckStat("⚔ Attack", attackCount, HpRed)
                DeckStat("✦ Skill", skillCount, BlockBlue)
                DeckStat("★ Power", powerCount, StrengthGold)
            }

            if (deck.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Deck is empty", color = TextMuted, fontSize = 16.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(deck) { card ->
                        CardView(
                            card = card,
                            compact = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeckStat(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = TextMuted, fontSize = 11.sp)
    }
}
