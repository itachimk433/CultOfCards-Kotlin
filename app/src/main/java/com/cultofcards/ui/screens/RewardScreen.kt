package com.cultofcards.ui.screens

import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import com.cultofcards.data.GameCard
import com.cultofcards.ui.components.CardView
import com.cultofcards.ui.navigation.Routes
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun RewardScreen(gameVm: GameViewModel, navController: NavHostController) {
    val rewardCards by gameVm.pendingRewardCards.collectAsState()
    var selectedCard by remember { mutableStateOf<GameCard?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF001A00), BackgroundDeep)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✓", fontSize = 48.sp, color = HpGreen)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "CHOOSE A REWARD",
                color = HpGreen,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                letterSpacing = 4.sp
            )
            Text(
                text = "Pick one card to add to your deck",
                color = TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            if (rewardCards.isEmpty()) {
                Text("No cards available", color = TextMuted)
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(rewardCards) { card ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CardView(
                                card = card,
                                onClick = { selectedCard = if (selectedCard?.id == card.id) null else card },
                                selected = selectedCard?.id == card.id,
                                modifier = Modifier.size(width = 140.dp, height = 200.dp)
                            )
                            if (selectedCard?.id == card.id) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(HpGreen.copy(0.2f))
                                        .padding(horizontal = 10.dp, vertical = 3.dp)
                                ) {
                                    Text("Selected", color = HpGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    selectedCard?.let { card -> gameVm.addCardToDeck(card) }
                    gameVm.setPendingRewardCards(emptyList())
                    navController.navigate(Routes.CAMPAIGN) { popUpTo(Routes.CAMPAIGN) }
                },
                enabled = selectedCard != null,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HpGreen)
            ) {
                Text(
                    text = if (selectedCard != null) "Add \"${selectedCard!!.name}\" to Deck" else "Select a Card",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = {
                    gameVm.setPendingRewardCards(emptyList())
                    navController.navigate(Routes.CAMPAIGN) { popUpTo(Routes.CAMPAIGN) }
                }
            ) {
                Text("Skip Reward", color = TextMuted, fontSize = 13.sp)
            }
        }
    }
}
