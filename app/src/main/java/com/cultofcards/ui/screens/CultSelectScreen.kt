package com.cultofcards.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cultofcards.data.CultId
import com.cultofcards.ui.navigation.Routes
import com.cultofcards.ui.theme.*
import com.cultofcards.viewmodel.GameViewModel

@Composable
fun CultSelectScreen(gameVm: GameViewModel, navController: NavHostController) {
    var selectedCult by remember { mutableStateOf<CultId?>(null) }

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
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null, tint = TextSecondary)
                }
                Text(
                    text = "Choose Your Cult",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text(
                text = "Your cult determines your starting deck and play style.",
                color = TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(CultId.entries) { cult ->
                    CultCard(
                        cult = cult,
                        selected = selectedCult == cult,
                        onClick = { selectedCult = cult }
                    )
                }
            }

            // Start button
            Button(
                onClick = {
                    selectedCult?.let { cult ->
                        gameVm.startRun(cult)
                        navController.navigate(Routes.CAMPAIGN) {
                            popUpTo(Routes.MAIN_MENU)
                        }
                    }
                },
                enabled = selectedCult != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) {
                Text(
                    text = if (selectedCult != null) "Begin with ${selectedCult!!.displayName}" else "Select a Cult",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun CultCard(cult: CultId, selected: Boolean, onClick: () -> Unit) {
    val cultColor = Color(cult.color)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (selected)
                    Brush.horizontalGradient(listOf(cultColor.copy(0.2f), SurfaceVariant))
                else
                    Brush.horizontalGradient(listOf(SurfaceVariant, Surface))
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) cultColor else Border,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = cult.icon, fontSize = 28.sp)
                Column {
                    Text(
                        text = cult.displayName,
                        color = if (selected) cultColor else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    Text(
                        text = cult.subtitle,
                        color = cultColor.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                if (selected) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(cultColor.copy(0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Selected", color = cultColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Text(
                text = cult.description,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}
