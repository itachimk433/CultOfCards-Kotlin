package com.mkdev.cultofcardsword.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mkdev.cultofcardsword.ui.screens.*
import com.mkdev.cultofcardsword.viewmodel.BattleViewModel
import com.mkdev.cultofcardsword.viewmodel.GameViewModel

object Routes {
    const val SPLASH          = "splash"
    const val MAIN_MENU       = "main_menu"
    const val CULT_SELECT     = "cult_select"
    const val CAMPAIGN        = "campaign"
    const val BATTLE          = "battle"
    const val REWARD          = "reward"
    const val RELIC_REWARD    = "relic_reward"
    const val STORY           = "story/{actId}/{floorNum}"
    const val VICTORY         = "victory"
    const val GAME_OVER       = "game_over"
    const val QUESTS          = "quests"
    const val DECK            = "deck"
    fun story(actId: Int, floorNum: Int) = "story/$actId/$floorNum"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gameVm: GameViewModel   = viewModel()
    val battleVm: BattleViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onReady = {
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN_MENU) {
            MainMenuScreen(
                gameVm         = gameVm,
                onNewGame      = { navController.navigate(Routes.CULT_SELECT) },
                onContinue     = { navController.navigate(Routes.CAMPAIGN) },
                onViewQuests   = { navController.navigate(Routes.QUESTS) }
            )
        }

        composable(Routes.CULT_SELECT) {
            CultSelectScreen(
                onCultSelected = { cultId ->
                    gameVm.startNewRun(cultId)
                    navController.navigate(Routes.CAMPAIGN) {
                        popUpTo(Routes.MAIN_MENU)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.CAMPAIGN) {
            CampaignScreen(
                gameVm       = gameVm,
                onStartFloor = { actId, floorNum ->
                    val run   = gameVm.run.value ?: return@CampaignScreen
                    val floor = com.mkdev.cultofcardsword.data.getStoryFloor(actId, floorNum) ?: return@CampaignScreen
                    when (floor.type) {
                        "boss", "battle" -> {
                            battleVm.initBattle(run, floor)
                            navController.navigate(Routes.BATTLE)
                        }
                        "event" -> navController.navigate(Routes.story(actId, floorNum))
                        else    -> {}
                    }
                },
                onViewDeck   = { navController.navigate(Routes.DECK) },
                onViewQuests = { navController.navigate(Routes.QUESTS) },
                onQuit       = {
                    gameVm.abandonRun()
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.BATTLE) {
            BattleScreen(
                gameVm   = gameVm,
                battleVm = battleVm,
                onVictory = {
                    gameVm.advanceFloor()
                    navController.navigate(Routes.REWARD) {
                        popUpTo(Routes.CAMPAIGN)
                    }
                },
                onDefeat = {
                    navController.navigate(Routes.GAME_OVER) {
                        popUpTo(Routes.MAIN_MENU)
                    }
                },
                onLeave = {
                    // Player chose to flee mid-battle — treat as defeat / return to campaign
                    navController.navigate(Routes.CAMPAIGN) {
                        popUpTo(Routes.CAMPAIGN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REWARD) {
            val run by gameVm.run.collectAsState()
            RewardScreen(
                run          = run,
                onCardPicked = { card ->
                    gameVm.addCardToRun(card)
                    navController.navigate(Routes.CAMPAIGN) {
                        popUpTo(Routes.CAMPAIGN) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(Routes.CAMPAIGN) {
                        popUpTo(Routes.CAMPAIGN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.RELIC_REWARD) {
            val run by gameVm.run.collectAsState()
            RelicRewardScreen(
                run          = run,
                onRelicTaken = { relic ->
                    gameVm.addRelicToRun(relic)
                    navController.navigate(Routes.CAMPAIGN) {
                        popUpTo(Routes.CAMPAIGN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.STORY) { backStackEntry ->
            val actId    = backStackEntry.arguments?.getString("actId")?.toIntOrNull()    ?: 1
            val floorNum = backStackEntry.arguments?.getString("floorNum")?.toIntOrNull() ?: 1
            StoryScreen(
                actId    = actId,
                floorNum = floorNum,
                onDone   = {
                    gameVm.advanceFloor()
                    val run = gameVm.run.value
                    if (run?.pendingEvent == true) {
                        navController.navigate(Routes.RELIC_REWARD) { popUpTo(Routes.CAMPAIGN) }
                    } else {
                        navController.navigate(Routes.CAMPAIGN) { popUpTo(Routes.CAMPAIGN) { inclusive = true } }
                    }
                }
            )
        }

        composable(Routes.VICTORY) {
            VictoryScreen(
                gameVm = gameVm,
                onContinue = {
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.GAME_OVER) {
            val run by gameVm.run.collectAsState()
            GameOverScreen(
                run      = run,
                onRetry  = { navController.navigate(Routes.CULT_SELECT) { popUpTo(Routes.MAIN_MENU) } },
                onMainMenu = {
                    gameVm.abandonRun()
                    navController.navigate(Routes.MAIN_MENU) {
                        popUpTo(Routes.MAIN_MENU) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.QUESTS) {
            QuestsScreen(
                gameVm = gameVm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DECK) {
            val run by gameVm.run.collectAsState()
            DeckScreen(
                run    = run,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
