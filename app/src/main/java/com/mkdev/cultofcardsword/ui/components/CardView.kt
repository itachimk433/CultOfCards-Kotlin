package com.mkdev.cultofcardsword.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.*
import com.mkdev.cultofcardsword.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CardView(
    card: GameCard,
    isPlayable: Boolean,
    isSelected: Boolean,
    onSingleTap: () -> Unit,
    onDoubleTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    // Selection scale
    val scale by animateFloatAsState(
        targetValue   = if (isSelected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label         = "card_scale"
    )

    // Playable pulse glow
    var pulseState by remember { mutableStateOf(false) }
    val borderAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else if (isPlayable) if (pulseState) 1f else 0.45f else 0.2f,
        animationSpec = tween(500),
        label       = "border_alpha"
    )

    LaunchedEffect(isPlayable) {
        while (isPlayable) {
            pulseState = true
            delay(700)
            pulseState = false
            delay(700)
        }
        pulseState = false
    }

    // Play animation state
    var isBeingPlayed by remember { mutableStateOf(false) }
    val playScale by animateFloatAsState(
        targetValue   = if (isBeingPlayed) 1.35f else scale,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessHigh),
        label         = "play_scale"
    )
    val playAlpha by animateFloatAsState(
        targetValue   = if (isBeingPlayed) 0f else 1f,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label         = "play_alpha"
    )
    val playOffsetY by animateFloatAsState(
        targetValue   = if (isBeingPlayed) -60f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing),
        label         = "play_offset"
    )

    val cultColor   = Color(getCultColor(card.cult))
    val rarityColor = Color(getRarityColor(card.rarity))
    val bgColor     = when {
        isBeingPlayed -> cultColor.copy(alpha = 0.3f)
        isPlayable    -> CardSurface
        else          -> DarkSurface.copy(alpha = 0.6f)
    }
    val borderColor = when {
        isBeingPlayed -> cultColor
        isSelected    -> rarityColor
        isPlayable    -> cultColor.copy(alpha = borderAlpha)
        else          -> CardBorder.copy(alpha = 0.3f)
    }
    val textAlpha   = if (isPlayable) 1f else 0.5f

    Box(
        modifier = modifier
            .width(100.dp)
            .height(150.dp)
            .offset(y = playOffsetY.dp)
            .scale(playScale)
            .alpha(playAlpha)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(
                width = if (isSelected || isBeingPlayed) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap       = { onSingleTap() },
                    onDoubleTap = {
                        // Trigger play animation then call callback
                        scope.launch {
                            isBeingPlayed = true
                            delay(220)
                            isBeingPlayed = false
                            onDoubleTap()
                        }
                    }
                )
            }
    ) {
        Column(
            modifier            = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cost + mana badge row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Energy cost pip
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(50))
                        .background(EnergyAmber),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = "${card.cost}",
                        color      = Color.Black,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Mana cost pip (if any)
                if (card.manaCost > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(ManaBlue.copy(alpha = 0.85f))
                            .padding(horizontal = 3.dp, vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = "${card.manaCost}M",
                            color      = Color.White,
                            fontSize   = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                // Type badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(rarityColor.copy(alpha = 0.3f))
                        .padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(
                        text       = card.type.name.first().toString(),
                        color      = rarityColor,
                        fontSize   = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Art band — glows on select/play
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(cultColor.copy(alpha = if (isBeingPlayed) 0.45f else 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text     = when (card.type) {
                        CardType.ATTACK -> "⚔"
                        CardType.SKILL  -> "🛡"
                        CardType.POWER  -> "✨"
                    },
                    fontSize = if (isBeingPlayed) 20.sp else 16.sp
                )
            }

            Spacer(Modifier.height(4.dp))

            // Card name
            Text(
                text       = card.name,
                color      = TextPrimary.copy(alpha = textAlpha),
                fontSize   = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )

            Spacer(Modifier.height(3.dp))

            // Description
            Text(
                text      = card.description,
                color     = TextSecondary.copy(alpha = textAlpha),
                fontSize  = 7.5.sp,
                textAlign = TextAlign.Center,
                maxLines  = 3,
                overflow  = TextOverflow.Ellipsis,
                lineHeight = 10.sp,
                modifier  = Modifier.weight(1f)
            )

            // Overall power badge
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(cultColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = "PWR ${card.skillStats.overallPower}",
                    color      = cultColor,
                    fontSize   = 7.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Selected glow overlay
        if (isSelected && !isBeingPlayed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(cultColor.copy(alpha = 0.10f))
            )
        }

        // Played flash overlay
        if (isBeingPlayed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(cultColor.copy(alpha = 0.25f))
            )
        }

        // Unplayable dim overlay
        if (!isPlayable && !isBeingPlayed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black.copy(alpha = 0.42f))
            )
        }
    }
}
