package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onReady: () -> Unit) {
    // Fade-in for the whole screen content
    var visible by remember { mutableStateOf(false) }

    val contentAlpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label         = "splash_alpha"
    )
    val logoScale by animateFloatAsState(
        targetValue   = if (visible) 1f else 0.72f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label         = "logo_scale"
    )

    // Infinite pulse on the sword icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.55f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Trigger entrance + navigate after assets are "ready"
    LaunchedEffect(Unit) {
        visible = true
        // Allow time for Filament / SceneView / Compose to fully initialise
        delay(2600)
        onReady()
    }

    Box(
        modifier         = Modifier
            .fillMaxSize()
            .background(DeepBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.alpha(contentAlpha)
        ) {
            // Sword icon — pulsing glow effect
            Text(
                text     = "⚔",
                fontSize = 72.sp,
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(pulseAlpha)
            )

            Spacer(Modifier.height(24.dp))

            // Game title
            Text(
                text       = "CULT OF CARDS",
                color      = SwordGold,
                fontSize   = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign  = TextAlign.Center,
                letterSpacing = 4.sp,
                modifier   = Modifier.scale(logoScale)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text       = "SWORDS OF FATE",
                color      = AccentSilver,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Medium,
                textAlign  = TextAlign.Center,
                letterSpacing = 6.sp
            )

            Spacer(Modifier.height(56.dp))

            // Loading dots
            LoadingDots()
        }
    }
}

@Composable
private fun LoadingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dotOffset by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 3f,
        animationSpec = infiniteRepeatable(
            animation  = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dot_step"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(3) { idx ->
            val alpha = if (dotOffset.toInt() == idx) 1f else 0.25f
            val dotAlpha by animateFloatAsState(
                targetValue   = alpha,
                animationSpec = tween(200),
                label         = "dot_$idx"
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(dotAlpha)
                    .background(SwordGold, shape = androidx.compose.foundation.shape.CircleShape)
            )
        }
    }
}
