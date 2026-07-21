package com.cultofcards.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cultofcards.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GameRepository(application)

    private val _run = MutableStateFlow<GameRun?>(null)
    val run: StateFlow<GameRun?> = _run.asStateFlow()

    private val _quests = MutableStateFlow<List<Quest>>(emptyList())
    val quests: StateFlow<List<Quest>> = _quests.asStateFlow()

    private val _globalProgress = MutableStateFlow(GlobalProgress())
    val globalProgress: StateFlow<GlobalProgress> = _globalProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Transient reward tracking
    private val _pendingRewardCards = MutableStateFlow<List<GameCard>>(emptyList())
    val pendingRewardCards: StateFlow<List<GameCard>> = _pendingRewardCards.asStateFlow()

    private val _pendingRelic = MutableStateFlow<Relic?>(null)
    val pendingRelic: StateFlow<Relic?> = _pendingRelic.asStateFlow()

    init {
        loadState()
    }

    private fun loadState() {
        _run.value = repo.loadRun()
        _quests.value = repo.loadQuests()
        _globalProgress.value = repo.loadGlobal()
        _isLoading.value = false
    }

    fun startRun(cultId: CultId) {
        val baseHp = 70
        val newRun = GameRun(
            cultId = cultId,
            act = 1,
            floor = 1,
            playerHp = baseHp,
            playerMaxHp = baseHp,
            gold = 0,
            deck = buildStartingDeck(cultId),
            relics = emptyList(),
            maxEnergy = 3,
            bonusEnergyPerTurn = 0,
            extraDrawPerTurn = 0,
            isFirstAttackDouble = false,
            attackBonus = 0,
            completedFloors = emptyList(),
            pendingEvent = false
        )
        _run.value = newRun
        repo.saveRun(newRun)

        val newGlobal = _globalProgress.value.copy(totalRuns = _globalProgress.value.totalRuns + 1)
        _globalProgress.value = newGlobal
        repo.saveGlobal(newGlobal)
    }

    fun updateRun(updates: GameRun.() -> GameRun) {
        val current = _run.value ?: return
        val updated = current.updates()
        _run.value = updated
        repo.saveRun(updated)
    }

    fun endRun() {
        _run.value = null
        repo.saveRun(null)
    }

    fun addCardToDeck(card: GameCard) {
        updateRun { copy(deck = deck + card.copy(id = makeCardId())) }
    }

    fun addRelic(relic: Relic) {
        updateRun {
            val hpBonus = relic.extraMaxHp
            val newMaxHp = playerMaxHp + hpBonus
            val newHp = minOf(playerHp + hpBonus, newMaxHp)
            copy(
                relics = relics + relic,
                playerMaxHp = newMaxHp,
                playerHp = newHp,
                bonusEnergyPerTurn = bonusEnergyPerTurn + relic.energyBonus,
                attackBonus = attackBonus + relic.attackBonus,
                extraDrawPerTurn = extraDrawPerTurn + relic.extraDraw,
                isFirstAttackDouble = isFirstAttackDouble || relic.firstAttackDouble
            )
        }
    }

    fun getCurrentFloor(): StoryFloor? {
        val r = _run.value ?: return null
        return getStoryFloor(r.act, r.floor)
    }

    fun advanceFloor() {
        updateRun {
            val act = getStoryAct(this.act) ?: return@updateRun this
            val nextFloor = floor + 1
            if (nextFloor > act.floors.size) {
                val nextAct = this.act + 1
                copy(
                    act = nextAct,
                    floor = 1,
                    completedFloors = completedFloors + "${this.act}-${this.floor}"
                )
            } else {
                copy(
                    floor = nextFloor,
                    completedFloors = completedFloors + "${this.act}-${this.floor}"
                )
            }
        }
    }

    fun completeFloor(hpRemaining: Int, enemiesKilled: Int, usedOnlyAttacks: Boolean) {
        val currentRun = _run.value ?: return

        // Apply post-battle relics
        val bloodVial = currentRun.relics.find { it.id == "blood_vial" }
        val darkIdol = currentRun.relics.find { it.id == "dark_idol" }
        val newHp = if (bloodVial != null)
            minOf(currentRun.playerMaxHp, hpRemaining + bloodVial.healAfterBattle)
        else hpRemaining
        val newGold = currentRun.gold + (darkIdol?.goldAfterBattle ?: 0)

        updateRun {
            copy(
                playerHp = newHp,
                gold = newGold,
                isFirstAttackDouble = relics.any { it.id == "shadow_cloak" }
            )
        }

        // Update global progress
        val newGlobal = _globalProgress.value.copy(
            battlesWon = _globalProgress.value.battlesWon + 1,
            totalEnemiesKilled = _globalProgress.value.totalEnemiesKilled + enemiesKilled,
            lowHpWins = if (hpRemaining <= 5) _globalProgress.value.lowHpWins + 1 else _globalProgress.value.lowHpWins,
            attackOnlyWins = if (usedOnlyAttacks) _globalProgress.value.attackOnlyWins + 1 else _globalProgress.value.attackOnlyWins
        )
        _globalProgress.value = newGlobal
        repo.saveGlobal(newGlobal)

        // Update quests
        val currentDeckSize = _run.value?.deck?.size ?: 0
        val updatedQuests = _quests.value.map { q ->
            if (q.completed) return@map q
            val progress = when (q.trackKey) {
                "battlesWon" -> newGlobal.battlesWon
                "lowHpWins" -> newGlobal.lowHpWins
                "totalEnemiesKilled" -> newGlobal.totalEnemiesKilled
                "attackOnlyWins" -> newGlobal.attackOnlyWins
                "deckSize" -> currentDeckSize
                else -> 0
            }
            q.copy(completed = progress >= q.goal)
        }
        _quests.value = updatedQuests
        repo.saveQuests(updatedQuests)
    }

    fun setPendingRewardCards(cards: List<GameCard>) {
        _pendingRewardCards.value = cards
    }

    fun setPendingRelic(relic: Relic?) {
        _pendingRelic.value = relic
    }

    fun clearPendingRelic() {
        _pendingRelic.value = null
    }

    fun getRewardInfo(): Triple<Int, Int, Boolean> {
        val r = _run.value ?: return Triple(1, 1, false)
        val floor = getCurrentFloor()
        return Triple(r.act, r.floor, floor?.isBoss ?: false)
    }
}
