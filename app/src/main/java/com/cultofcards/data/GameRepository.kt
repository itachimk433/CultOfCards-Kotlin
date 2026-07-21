package com.cultofcards.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class GameRun(
    val cultId: CultId,
    val act: Int,
    val floor: Int,
    val playerHp: Int,
    val playerMaxHp: Int,
    val gold: Int,
    val deck: List<GameCard>,
    val relics: List<Relic>,
    val maxEnergy: Int,
    val bonusEnergyPerTurn: Int,
    val extraDrawPerTurn: Int,
    val isFirstAttackDouble: Boolean,
    val attackBonus: Int,
    val completedFloors: List<String>,
    val pendingEvent: Boolean
)

data class GlobalProgress(
    val totalEnemiesKilled: Int = 0,
    val battlesWon: Int = 0,
    val lowHpWins: Int = 0,
    val attackOnlyWins: Int = 0,
    val totalRuns: Int = 0
)

class GameRepository(context: Context) {

    private val prefs = context.getSharedPreferences("cult_of_cards", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_RUN = "run_v2"
        private const val KEY_QUESTS = "quests_v2"
        private const val KEY_GLOBAL = "global_v2"
    }

    // ---- Run ----
    fun saveRun(run: GameRun?) {
        prefs.edit().apply {
            if (run != null) putString(KEY_RUN, gson.toJson(run))
            else remove(KEY_RUN)
            apply()
        }
    }

    fun loadRun(): GameRun? {
        val json = prefs.getString(KEY_RUN, null) ?: return null
        return try { gson.fromJson(json, GameRun::class.java) } catch (e: Exception) { null }
    }

    // ---- Quests ----
    fun saveQuests(quests: List<Quest>) {
        val type = object : TypeToken<List<Quest>>() {}.type
        prefs.edit().putString(KEY_QUESTS, gson.toJson(quests, type)).apply()
    }

    fun loadQuests(): List<Quest> {
        val json = prefs.getString(KEY_QUESTS, null) ?: return INITIAL_QUESTS
        return try {
            val type = object : TypeToken<List<Quest>>() {}.type
            gson.fromJson(json, type)
        } catch (e: Exception) { INITIAL_QUESTS }
    }

    // ---- Global Progress ----
    fun saveGlobal(progress: GlobalProgress) {
        prefs.edit().putString(KEY_GLOBAL, gson.toJson(progress)).apply()
    }

    fun loadGlobal(): GlobalProgress {
        val json = prefs.getString(KEY_GLOBAL, null) ?: return GlobalProgress()
        return try { gson.fromJson(json, GlobalProgress::class.java) } catch (e: Exception) { GlobalProgress() }
    }
}
