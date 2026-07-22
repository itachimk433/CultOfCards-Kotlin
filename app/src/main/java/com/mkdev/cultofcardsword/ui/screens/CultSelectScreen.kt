package com.mkdev.cultofcardsword.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mkdev.cultofcardsword.data.CultId
import com.mkdev.cultofcardsword.ui.theme.*

@Composable
fun CultSelectScreen(
    onCultSelected: (CultId) -> Unit,
    onBack: () -> Unit
) {
    var selectedCult  by remember { mutableStateOf<CultId?>(null) }
    val cults = CultId.entries.toList()

    Box(modifier = Modifier.fillMaxSize().background(DeepBlack)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 12.dp)
        ) {
            // Top bar
            Row(
                modifier          = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextSecondary)
                }
                Text(
                    "Choose Your Sword Path",
                    color      = SwordGold,
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.weight(1f),
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.size(48.dp))
            }

            Text(
                "Select a Cult to begin your journey. Each path has unique skills and stats.",
                color     = TextSecondary,
                fontSize  = 11.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            )

            // Grid of cults
            LazyVerticalGrid(
                columns            = GridCells.Fixed(3),
                verticalArrangement   = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier           = Modifier.weight(1f)
            ) {
                items(cults) { cult ->
                    CultCard(
                        cult       = cult,
                        isSelected = selectedCult == cult,
                        onClick    = { selectedCult = cult }
                    )
                }
            }

            // Detail panel for selected cult
            selectedCult?.let { cult ->
                Spacer(Modifier.height(12.dp))
                CultDetailPanel(cult = cult, onConfirm = { onCultSelected(cult) })
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CultCard(cult: CultId, isSelected: Boolean, onClick: () -> Unit) {
    val cultColor = Color(cult.color)
    Box(
        modifier = Modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) cultColor.copy(alpha = 0.2f) else CardSurface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) cultColor else CardBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(cult.icon, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                text       = cult.displayName,
                color      = if (isSelected) cultColor else TextPrimary,
                fontSize   = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
                lineHeight = 10.sp
            )
        }
    }
}

@Composable
private fun CultDetailPanel(cult: CultId, onConfirm: () -> Unit) {
    val cultColor = Color(cult.color)
    val stats     = cult.totalStats

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurface)
            .border(1.dp, cultColor, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(cult.icon, fontSize = 28.sp)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(cult.displayName, color = cultColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(cult.subtitle,    color = AccentGold, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(cult.description, color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
            Spacer(Modifier.height(8.dp))

            // Stat bars
            Text("Base Stat Distribution (Total: ${stats.overallPower})", color = TextSecondary, fontSize = 9.sp)
            Spacer(Modifier.height(4.dp))
            StatBar("⚔ ATK", stats.attack,   stats.overallPower, DangerRed)
            StatBar("🛡 DEF", stats.defense,  stats.overallPower, BlockGray)
            StatBar("💨 AGI", stats.agility,  stats.overallPower, FreezeBlue)
            StatBar("💧 MAN", stats.mana,     stats.overallPower, ManaBlue)
            StatBar("✨ SPI", stats.spirit,   stats.overallPower, AccentGold)

            Spacer(Modifier.height(10.dp))

            Button(
                onClick  = onConfirm,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = cultColor, contentColor = Color.Black),
                shape    = RoundedCornerShape(10.dp)
            ) {
                Text("Begin as ${cult.displayName}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun StatBar(label: String, value: Int, total: Int, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.padding(vertical = 2.dp)
    ) {
        Text(label, color = TextSecondary, fontSize = 9.sp, modifier = Modifier.width(48.dp))
        LinearProgressIndicator(
            progress     = { value.toFloat() / total.toFloat() },
            modifier     = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
            color        = color,
            trackColor   = CardBorder
        )
        Text("$value", color = color, fontSize = 9.sp, modifier = Modifier.width(22.dp), textAlign = TextAlign.End)
    }
}
