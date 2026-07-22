package com.mkdev.cultofcardsword.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.mkdev.cultofcardsword.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = GameRepository(application)

    private val _run     = MutableStateFlow<GameRun?>(null)
    val run: StateFlow<GameRun?> = _run.asStateFlow()

    private val _quests  = MutableStateFlow<List<Quest>>(emptyList())
    val quests: StateFlow<List<Quest>> = _quests.asStateFlow()

    private val _global  = MutableStateFlow(GlobalProgress())
    val global: StateFlow<GlobalProgress> = _global.asStateFlow()

    init { loadAll() }

    private fun loadAll() {
        _run.value    = repo.loadRun()
        _quests.value = repo.loadQuests()
        _global.value = repo.loadGlobal()
    }

    // ---- Run management ----

    fun startNewRun(cultId: CultId) {
        val deck      = buildStartingDeck(cultId)
        val globalNow = _global.value
        val rank      = rankFromBattlesWon(globalNow.battlesWon)
        val run = GameRun(
            cultId              = cultId,
            act                 = 1,
            floor               = 1,
            playerHp            = 80,
            playerMaxHp         = 80,
            gold                = 0,
            deck                = deck,
            relics              = emptyList(),
            maxEnergy           = 3,
            bonusEnergyPerTurn  = 0,
            extraDrawPerTurn    = 0,
            isFirstAttackDouble = false,
            attackBonus         = 0,
            completedFloors     = emptyList(),
            pendingEvent        = false,
            swordsmanRank       = rank,
            manaBonus           = 0
        )
        _run.value = run
        repo.saveRun(run)
        val newGlobal = globalNow.copy(totalRuns = globalNow.totalRuns + 1)
        _global.value = newGlobal
        repo.saveGlobal(newGlobal)
    }

    fun getCurrentFloor(): StoryFloor? {
        val r = _run.value ?: return null
        return getStoryFloor(r.act, r.floor)
    }

    fun advanceFloor() {
        val r = _run.value ?: return
        val act = getStoryAct(r.act) ?: return
        val key = "${r.act}-${r.floor}"
        val completed = r.completedFloors + key

        val nextFloor = r.floor + 1
        val floorExists = act.floors.any { it.floor == nextFloor }

        if (floorExists) {
            save(r.copy(floor = nextFloor, completedFloors = completed))
        } else {
            // Move to next act
            val nextAct = r.act + 1
            val nextActExists = getStoryAct(nextAct) != null
            if (nextActExists) {
                val g = _global.value.copy(actsCompleted = _global.value.actsCompleted + 1)
                _global.value = g
                repo.saveGlobal(g)
                save(r.copy(act = nextAct, floor = 1, completedFloors = completed))
            } else {
                // Game complete
                val g = _global.value.copy(actsCompleted = _global.value.actsCompleted + 1)
                _global.value = g
                repo.saveGlobal(g)
                save(r.copy(completedFloors = completed, pendingEvent = true))
            }
        }
        checkQuestProgress()
    }

    fun onBattleWon(battleState: BattleState) {
        val r = _run.value ?: return

        val enemiesKilled = battleState.enemies.count { it.hp <= 0 }
        val usedSkills    = battleState.usedSkills
        val isLowHp       = battleState.playerHp <= 5
        val dmgDealt      = battleState.totalDamageDealt
        val turns         = battleState.totalTurnsPlayed

        // Update run HP
        var newHp = battleState.playerHp
        r.relics.forEach { relic ->
            if (relic.healAfterBattle > 0) newHp = minOf(r.playerMaxHp, newHp + relic.healAfterBattle)
        }

        val goldGain = (10..25).random() + r.relics.sumOf { it.goldAfterBattle }
        val newRun   = r.copy(playerHp = newHp, gold = r.gold + goldGain)
        _run.value   = newRun
        repo.saveRun(newRun)

        // Update global progress
        var g = _global.value.copy(
            battlesWon         = _global.value.battlesWon + 1,
            totalEnemiesKilled = _global.value.totalEnemiesKilled + enemiesKilled,
            totalDamageDealt   = _global.value.totalDamageDealt + dmgDealt,
            totalTurns         = _global.value.totalTurns + turns
        )
        if (isLowHp) g = g.copy(lowHpWins = g.lowHpWins + 1)
        if (!usedSkills) g = g.copy(attackOnlyWins = g.attackOnlyWins + 1)
        _global.value = g
        repo.saveGlobal(g)

        // Update rank
        val newRank = rankFromBattlesWon(g.battlesWon)
        val rankedRun = (_run.value ?: return).copy(swordsmanRank = newRank)
        _run.value = rankedRun
        repo.saveRun(rankedRun)

        checkQuestProgress()
    }

    fun addCardToRun(card: GameCard) {
        val r = _run.value ?: return
        save(r.copy(deck = r.deck + card))
        checkQuestProgress()
    }

    fun addRelicToRun(relic: Relic) {
        val r = _run.value ?: return
        var updatedRun = r.copy(relics = r.relics + relic)

        // Apply relic passive effects immediately
        if (relic.energyBonus > 0)   updatedRun = updatedRun.copy(bonusEnergyPerTurn = updatedRun.bonusEnergyPerTurn + relic.energyBonus)
        if (relic.extraDraw   > 0)   updatedRun = updatedRun.copy(extraDrawPerTurn   = updatedRun.extraDrawPerTurn   + relic.extraDraw)
        if (relic.firstAttackDouble)  updatedRun = updatedRun.copy(isFirstAttackDouble = true)
        if (relic.attackBonus > 0)   updatedRun = updatedRun.copy(attackBonus         = updatedRun.attackBonus         + relic.attackBonus)
        if (relic.extraMaxHp  > 0)   updatedRun = updatedRun.copy(playerMaxHp         = updatedRun.playerMaxHp         + relic.extraMaxHp,
                                                                    playerHp            = updatedRun.playerHp            + relic.extraMaxHp)
        if (relic.manaBonus   > 0)   updatedRun = updatedRun.copy(manaBonus           = updatedRun.manaBonus           + relic.manaBonus)

        save(updatedRun)
    }

    fun abandonRun() {
        _run.value = null
        repo.saveRun(null)
    }

    fun updateRunHp(newHp: Int) {
        val r = _run.value ?: return
        save(r.copy(playerHp = maxOf(0, minOf(r.playerMaxHp, newHp))))
    }

    // ---- Quests ----

    private fun checkQuestProgress() {
        val r  = _run.value
        val g  = _global.value
        val qs = _quests.value.map { quest ->
            val progress = when (quest.trackKey) {
                "battlesWon"         -> g.battlesWon
                "totalEnemiesKilled" -> g.totalEnemiesKilled
                "lowHpWins"          -> g.lowHpWins
                "attackOnlyWins"     -> g.attackOnlyWins
                "deckSize"           -> r?.deck?.size ?: 0
                "actsCompleted"      -> g.actsCompleted
                "totalDamageDealt"   -> g.totalDamageDealt
                "totalTurns"         -> g.totalTurns
                else                 -> 0
            }
            if (!quest.completed && progress >= quest.goal) quest.copy(completed = true)
            else quest
        }
        _quests.value = qs
        repo.saveQuests(qs)
    }

    fun getQuestProgress(quest: Quest): Int {
        val r = _run.value
        val g = _global.value
        return when (quest.trackKey) {
            "battlesWon"         -> g.battlesWon
            "totalEnemiesKilled" -> g.totalEnemiesKilled
            "lowHpWins"          -> g.lowHpWins
            "attackOnlyWins"     -> g.attackOnlyWins
            "deckSize"           -> r?.deck?.size ?: 0
            "actsCompleted"      -> g.actsCompleted
            "totalDamageDealt"   -> g.totalDamageDealt
            "totalTurns"         -> g.totalTurns
            else                 -> 0
        }
    }

    // ---- Helpers ----

    private fun save(run: GameRun) {
        _run.value = run
        repo.saveRun(run)
    }
}
