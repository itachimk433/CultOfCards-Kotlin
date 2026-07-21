package com.cultofcards.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cultofcards.data.*
import com.cultofcards.ui.theme.*

@Composable
fun CardView(
    card: GameCard,
    onClick: () -> Unit = {},
    disabled: Boolean = false,
    selected: Boolean = false,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val cultColor = Color(getCultColor(card.cult))
    val rarityColor = Color(getRarityColor(card.rarity))
    val cardShape = RoundedCornerShape(12.dp)

    val typeSymbol = when (card.type) {
        CardType.ATTACK -> "⚔"
        CardType.SKILL -> "✦"
        CardType.POWER -> "★"
    }
    val typeColor = when (card.type) {
        CardType.ATTACK -> HpRed
        CardType.SKILL -> BlockBlue
        CardType.POWER -> StrengthGold
    }

    Box(
        modifier = modifier
            .width(if (compact) 100.dp else 120.dp)
            .height(if (compact) 140.dp else 170.dp)
            .clip(cardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SurfaceVariant,
                        BackgroundMid
                    )
                )
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) cultColor else Border,
                shape = cardShape
            )
            .alpha(if (disabled) 0.45f else 1f)
            .clickable(enabled = !disabled) { onClick() }
            .padding(if (compact) 8.dp else 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top: cost + type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Energy cost
                Box(
                    modifier = Modifier
                        .size(if (compact) 20.dp else 24.dp)
                        .clip(RoundedCornerShape(50))
                        .background(EnergyAmber),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${card.cost}",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        fontSize = if (compact) 10.sp else 12.sp
                    )
                }
                // Type symbol
                Text(
                    text = typeSymbol,
                    color = typeColor,
                    fontSize = if (compact) 12.sp else 14.sp
                )
            }

            // Card name
            Text(
                text = card.name,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = if (compact) 11.sp else 12.sp,
                textAlign = TextAlign.Center,
                lineHeight = if (compact) 14.sp else 16.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // Cult strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(cultColor.copy(alpha = 0.6f))
            )

            // Description
            Text(
                text = card.description,
                color = TextSecondary,
                fontSize = if (compact) 9.sp else 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = if (compact) 12.sp else 14.sp,
                modifier = Modifier.fillMaxWidth()
            )

            // Rarity
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .width(if (compact) 30.dp else 40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(rarityColor)
                )
            }
        }
    }
}
