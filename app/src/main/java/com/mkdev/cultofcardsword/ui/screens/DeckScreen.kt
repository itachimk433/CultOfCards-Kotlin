package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.CardType
import com.mkdev.cultofcardsword.data.GameRun
import com.mkdev.cultofcardsword.data.getCultColor
import com.mkdev.cultofcardsword.ui.components.CardView
import com.mkdev.cultofcardsword.ui.theme.*

@Composable
fun DeckScreen(
    run: GameRun?,
    onBack: () -> Unit
) {
    val deck         = run?.deck ?: emptyList()
    val attackCount  = deck.count { it.type == CardType.ATTACK }
    val skillCount   = deck.count { it.type == CardType.SKILL }
    val powerCount   = deck.count { it.type == CardType.POWER }

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {
        Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
            // Top bar
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextSecondary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Your Deck", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text("${deck.size} cards total", color = TextSecondary, fontSize = 11.sp)
                }
                if (run != null) {
                    Text(
                        text     = run.cultId.displayName,
                        color    = Color(getCultColor(run.cultId.name)),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }

            // Stats row
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .background(CardSurface)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DeckStat("⚔ Attack", attackCount, DangerRed)
                DeckStat("🛡 Skill",  skillCount,  ManaBlue)
                DeckStat("✨ Power",  powerCount,  EnergyAmber)
            }

            if (deck.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Deck is empty", color = TextSecondary, fontSize = 15.sp)
                }
            } else {
                LazyVerticalGrid(
                    columns             = GridCells.Fixed(3),
                    modifier            = Modifier.fillMaxSize(),
                    contentPadding      = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(deck) { card ->
                        CardView(
                            card        = card,
                            isPlayable  = false,
                            isSelected  = false,
                            onSingleTap = {},
                            onDoubleTap = {},
                            modifier    = Modifier.fillMaxWidth().height(145.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeckStat(label: String, count: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count.toString(), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = TextSecondary, fontSize = 11.sp)
    }
}
