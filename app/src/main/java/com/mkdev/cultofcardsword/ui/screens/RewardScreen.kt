package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.GameCard
import com.mkdev.cultofcardsword.data.GameRun
import com.mkdev.cultofcardsword.data.getRewardCards
import com.mkdev.cultofcardsword.ui.components.CardView
import com.mkdev.cultofcardsword.ui.theme.*

@Composable
fun RewardScreen(
    run: GameRun?,
    onCardPicked: (GameCard) -> Unit,
    onSkip: () -> Unit
) {
    val r = run
    val rewardCards = remember(r?.cultId) {
        if (r != null) getRewardCards(r.cultId, 3) else emptyList()
    }
    var selectedCard by remember { mutableStateOf<GameCard?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(Color(0xFF001A00), DeepBlack))
            )
    ) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Victory banner
            Text("⚔", fontSize = 52.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text          = "VICTORY REWARD",
                color         = HpGreen,
                fontWeight    = FontWeight.Black,
                fontSize      = 16.sp,
                letterSpacing = 4.sp
            )
            Text(
                text     = "Choose one card to add to your deck",
                color    = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 28.dp)
            )

            if (rewardCards.isEmpty()) {
                Text("No cards available", color = TextSecondary)
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding        = PaddingValues(horizontal = 8.dp)
                ) {
                    items(rewardCards) { card ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CardView(
                                card        = card,
                                isPlayable  = true,
                                isSelected  = selectedCard?.id == card.id,
                                onSingleTap = {
                                    selectedCard = if (selectedCard?.id == card.id) null else card
                                },
                                onDoubleTap = {
                                    selectedCard = card
                                },
                                modifier    = Modifier.width(110.dp).height(165.dp)
                            )
                            if (selectedCard?.id == card.id) {
                                Spacer(Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(HpGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text("Selected", color = HpGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick  = { selectedCard?.let { onCardPicked(it) } },
                enabled  = selectedCard != null,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = HpGreen, contentColor = Color.Black)
            ) {
                Text(
                    text       = if (selectedCard != null) "Add \"${selectedCard!!.name}\" to Deck" else "Select a Card",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp
                )
            }

            Spacer(Modifier.height(10.dp))

            TextButton(onClick = onSkip) {
                Text("Skip Reward", color = TextSecondary, fontSize = 12.sp)
            }
        }
    }
}
