package com.cultofcards.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cultofcards.ui.screens.*
import com.cultofcards.viewmodel.BattleViewModel
import com.cultofcards.viewmodel.GameViewModel

object Routes {
    const val MAIN_MENU = "main_menu"
    const val CULT_SELECT = "cult_select"
    const val CAMPAIGN = "campaign"
    const val STORY = "story"
    const val BATTLE = "battle"
    const val REWARD = "reward"
    const val RELIC_REWARD = "relic_reward"
    const val DECK = "deck"
    const val QUESTS = "quests"
    const val GAME_OVER = "game_over"
    const val VICTORY = "victory"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val gameVm: GameViewModel = viewModel()
    val battleVm: BattleViewModel = viewModel()

    val run by gameVm.run.collectAsState()
    val isLoading by gameVm.isLoading.collectAsState()

    NavHost(navController = navController, startDestination = Routes.MAIN_MENU) {
        composable(Routes.MAIN_MENU) {
            MainMenuScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.CULT_SELECT) {
            CultSelectScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.CAMPAIGN) {
            CampaignScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.STORY) {
            StoryScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.BATTLE) {
            BattleScreen(
                gameVm = gameVm,
                battleVm = battleVm,
                navController = navController
            )
        }
        composable(Routes.REWARD) {
            RewardScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.RELIC_REWARD) {
            RelicRewardScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.DECK) {
            DeckScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.QUESTS) {
            QuestsScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.GAME_OVER) {
            GameOverScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
        composable(Routes.VICTORY) {
            VictoryScreen(
                gameVm = gameVm,
                navController = navController
            )
        }
    }
}
