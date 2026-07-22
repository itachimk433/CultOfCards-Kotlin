package com.mkdev.cultofcardsword.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary          = SwordGold,
    onPrimary        = Color.Black,
    secondary        = AccentSilver,
    onSecondary      = Color.Black,
    background       = DeepBlack,
    onBackground     = TextPrimary,
    surface          = DarkSurface,
    onSurface        = TextPrimary,
    surfaceVariant   = CardSurface,
    onSurfaceVariant = TextSecondary,
    error            = DangerRed,
    onError          = Color.White,
    outline          = CardBorder
)

@Composable
fun CultOfCardsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography  = Typography,
        content     = content
    )
}
