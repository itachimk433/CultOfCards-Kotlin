package com.cultofcards.viewmodel

import androidx.lifecycle.ViewModel
import com.cultofcards.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BattleEnemy(
    val templateId: String,
    val name: String,
    val maxHp: Int,
    val hp: Int,
    val block: Int,
    val effects: List<EffectStack>,
    val moveIndex: Int,
    val moves: List<EnemyMoveData>,
    val isBoss: Boolean,
    val strength: Int
)

data class BattleState(
    val drawPile: List<GameCard>,
    val hand: List<GameCard>,
    val discardPile: List<GameCard>,
    val enemies: List<BattleEnemy>,
    val energy: Int,
    val maxEnergy: Int,
    val playerHp: Int,
    val playerMaxHp: Int,
    val playerBlock: Int,
    val playerEffects: List<EffectStack>,
    val playerStrength: Int,
    val turn: Int,
    val phase: BattlePhase,
    val message: String,
    val isFirstAttack: Boolean,
    val energyBonusNextTurn: Int,
    val extraDraw: Int,
    val usedSkills: Boolean
)

enum class BattlePhase { PLAYER, ENEMY, VICTORY, DEFEAT }

class BattleViewModel : ViewModel() {

    private val _battleState = MutableStateFlow<BattleState?>(null)
    val battleState: StateFlow<BattleState?> = _battleState.asStateFlow()

    fun initBattle(run: com.cultofcards.data.GameRun, floor: StoryFloor) {
        val enemies = floor.enemyIds.mapNotNull { eid ->
            val tmpl = ENEMIES[eid] ?: return@mapNotNull null
            BattleEnemy(
                templateId = eid,
                name = tmpl.name,
                maxHp = tmpl.maxHp,
                hp = tmpl.maxHp,
                block = 0,
                effects = emptyList(),
                moveIndex = 0,
                moves = tmpl.moves,
                isBoss = tmpl.isBoss,
                strength = 0
            )
        }

        val blockBonus = run.relics.sumOf { it.blockBonus }
        val energyBonus = run.bonusEnergyPerTurn
        val extraDraw = run.extraDrawPerTurn
        val maxEnergy = run.maxEnergy + energyBonus

        val shuffledDeck = run.deck.shuffled()
        val drawCount = 5 + extraDraw
        val hand = shuffledDeck.take(drawCount)
        val drawPile = shuffledDeck.drop(drawCount)

        _battleState.value = BattleState(
            drawPile = drawPile,
            hand = hand,
            discardPile = emptyList(),
            enemies = enemies,
            energy = maxEnergy,
            maxEnergy = maxEnergy,
            playerHp = run.playerHp,
            playerMaxHp = run.playerMaxHp,
            playerBlock = blockBonus,
            playerEffects = emptyList(),
            playerStrength = 0,
            turn = 1,
            phase = BattlePhase.PLAYER,
            message = "Your turn — play cards or end turn.",
            isFirstAttack = run.isFirstAttackDouble,
            energyBonusNextTurn = 0,
            extraDraw = extraDraw,
            usedSkills = false
        )
    }

    fun playCard(card: GameCard, run: com.cultofcards.data.GameRun): String? {
        val s = _battleState.value ?: return "No battle active"
        if (s.phase != BattlePhase.PLAYER) return "Not your turn"
        if (card.cost > s.energy) return "Not enough energy!"

        var state = s.copy(
            energy = s.energy - card.cost,
            hand = s.hand.filter { it.id != card.id },
            discardPile = s.discardPile + card,
            usedSkills = if (card.type == CardType.SKILL) true else s.usedSkills
        )

        val effects = card.effects
        val hasFireGem = run.relics.any { it.id == "fire_gem" }
        val attackBonus = run.attackBonus + (if (hasFireGem) 2 else 0)
        val isWeak = state.getEffect(EffectType.WEAK) > 0

        var msg = card.name

        // Damage to primary target
        if (effects.damage > 0 && state.enemies.isNotEmpty()) {
            var dmg = effects.damage + state.playerStrength + attackBonus
            if (state.isFirstAttack) { dmg *= 2; state = state.copy(isFirstAttack = false) }
            if (isWeak) dmg = (dmg * 0.75).toInt()
            state = state.dealDamageToEnemy(0, dmg)
            msg = "${card.name}: $dmg damage"
        }

        // Damage all enemies
        if (effects.damageAll > 0) {
            var dmg = effects.damageAll + state.playerStrength + attackBonus
            if (state.isFirstAttack) { dmg *= 2; state = state.copy(isFirstAttack = false) }
            if (isWeak) dmg = (dmg * 0.75).toInt()
            val newEnemies = state.enemies.mapIndexed { i, _ -> state.dealDamageToEnemy(i, dmg).enemies[i] }
            state = state.copy(enemies = newEnemies)
            msg = "${card.name}: $dmg to all"
        }

        // Block
        if (effects.block > 0) {
            state = state.copy(playerBlock = state.playerBlock + effects.block)
            msg = "${card.name}: +${effects.block} block"
        }

        // Draw
        if (effects.draw > 0) {
            state = state.drawCards(effects.draw)
        }

        // Heal
        if (effects.heal > 0) {
            state = state.copy(playerHp = minOf(state.playerMaxHp, state.playerHp + effects.heal))
            msg = "${card.name}: healed ${effects.heal} HP"
        }

        // Lose HP
        if (effects.loseHp > 0) {
            state = state.copy(playerHp = maxOf(1, state.playerHp - effects.loseHp))
        }

        // Apply enemy effects
        if (state.enemies.isNotEmpty()) {
            var enemy = state.enemies[0]
            if (effects.poison > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.POISON, effects.poison))
            if (effects.burn > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.BURN, effects.burn))
            if (effects.vulnerable > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.VULNERABLE, effects.vulnerable))
            if (effects.weak > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.WEAK, effects.weak))
            val newEnemies = state.enemies.toMutableList().also { it[0] = enemy }
            state = state.copy(enemies = newEnemies)
            if (effects.poison > 0) msg = "${card.name}: ${effects.poison} poison"
        }

        // Strength
        if (effects.strength > 0) {
            state = state.copy(playerStrength = state.playerStrength + effects.strength)
            msg = "${card.name}: +${effects.strength} strength"
        }

        // Energy next turn
        if (effects.energyNext > 0) {
            state = state.copy(energyBonusNextTurn = state.energyBonusNextTurn + effects.energyNext)
        }

        // Check victory
        val aliveEnemies = state.enemies.filter { it.hp > 0 }
        if (aliveEnemies.isEmpty()) {
            state = state.copy(phase = BattlePhase.VICTORY, message = "Victory!")
        } else {
            state = state.copy(message = msg)
        }

        _battleState.value = state
        return null
    }

    fun endTurn() {
        var s = _battleState.value ?: return
        if (s.phase != BattlePhase.PLAYER) return

        s = s.copy(phase = BattlePhase.ENEMY)

        // Process each enemy
        val newEnemies = s.enemies.map { enemy ->
            if (enemy.hp <= 0) return@map enemy
            var e = enemy

            // Burn damage
            val burnStacks = getEffectValue(e.effects, EffectType.BURN)
            if (burnStacks > 0) e = e.copy(hp = maxOf(0, e.hp - burnStacks))

            // Poison damage
            val poisonStacks = getEffectValue(e.effects, EffectType.POISON)
            if (poisonStacks > 0) {
                e = e.copy(
                    hp = maxOf(0, e.hp - poisonStacks),
                    effects = decrementEffect(e.effects, EffectType.POISON)
                )
            }

            if (e.hp <= 0) return@map e

            // Execute move
            val move = e.moves[e.moveIndex % e.moves.size]
            when (move.type) {
                "attack" -> {
                    val dmg = move.value + e.strength
                    val isVulnerable = getEffectValue(s.playerEffects, EffectType.VULNERABLE) > 0
                    val finalDmg = if (isVulnerable) (dmg * 1.5).toInt() else dmg
                    val blocked = minOf(s.playerBlock, finalDmg)
                    s = s.copy(
                        playerBlock = maxOf(0, s.playerBlock - finalDmg),
                        playerHp = maxOf(0, s.playerHp - maxOf(0, finalDmg - blocked)),
                        message = "${e.name} ${move.label} for ${maxOf(0, finalDmg - blocked)} damage"
                    )
                }
                "block" -> {
                    e = e.copy(block = e.block + move.value)
                    s = s.copy(message = "${e.name} ${move.label}: +${move.value} block")
                }
                "buff" -> {
                    if (move.effectType == EffectType.STRENGTH) {
                        e = e.copy(strength = e.strength + (move.effectValue))
                    } else if (move.effectType != null) {
                        e = e.copy(effects = applyEffect(e.effects, move.effectType, move.effectValue))
                    }
                    s = s.copy(message = "${e.name} uses ${move.label}")
                }
            }
            e = e.copy(moveIndex = e.moveIndex + 1)
            e
        }

        s = s.copy(enemies = newEnemies)

        // Decrement player debuffs
        s = s.copy(
            playerEffects = s.playerEffects
                .map { e ->
                    if (e.type == EffectType.WEAK || e.type == EffectType.VULNERABLE)
                        e.copy(stacks = maxOf(0, e.stacks - 1))
                    else e
                }
                .filter { it.stacks > 0 }
        )

        // Check defeat
        if (s.playerHp <= 0) {
            _battleState.value = s.copy(phase = BattlePhase.DEFEAT, message = "You have fallen...")
            return
        }

        // Start next player turn
        val drawCount = 5 + s.extraDraw
        s = s.drawCards(drawCount)
        s = s.copy(
            playerBlock = 0,
            energy = s.maxEnergy + s.energyBonusNextTurn,
            energyBonusNextTurn = 0,
            enemies = s.enemies.map { it.copy(block = 0) },
            turn = s.turn + 1,
            phase = BattlePhase.PLAYER
        )
        if (!s.message.contains("damage")) {
            s = s.copy(message = "Your turn.")
        }

        _battleState.value = s
    }

    fun clearBattle() {
        _battleState.value = null
    }

    // ---- Helpers ----

    private fun applyEffect(effects: List<EffectStack>, type: EffectType, val_: Int): List<EffectStack> {
        val idx = effects.indexOfFirst { it.type == type }
        return if (idx >= 0) {
            effects.toMutableList().also { it[idx] = it[idx].copy(stacks = it[idx].stacks + val_) }
        } else {
            effects + EffectStack(type, val_)
        }
    }

    private fun getEffectValue(effects: List<EffectStack>, type: EffectType): Int =
        effects.find { it.type == type }?.stacks ?: 0

    private fun decrementEffect(effects: List<EffectStack>, type: EffectType): List<EffectStack> =
        effects.map { if (it.type == type) it.copy(stacks = maxOf(0, it.stacks - 1)) else it }
            .filter { it.stacks > 0 }

    private fun BattleState.getEffect(type: EffectType): Int =
        playerEffects.find { it.type == type }?.stacks ?: 0

    private fun BattleState.drawCards(count: Int): BattleState {
        var draw = drawPile.toMutableList()
        var discard = discardPile.toMutableList()
        var h = hand.toMutableList()

        if (draw.size < count) {
            draw.addAll(discard.shuffled())
            discard = mutableListOf()
        }
        val drawn = draw.take(count)
        draw = draw.drop(count).toMutableList()
        h.addAll(drawn)
        return copy(drawPile = draw, discardPile = discard, hand = h)
    }

    private fun BattleState.dealDamageToEnemy(idx: Int, rawDmg: Int): BattleState {
        if (idx >= enemies.size) return this
        val enemy = enemies[idx]
        if (enemy.hp <= 0) return this
        val isVulnerable = getEffectValue(enemy.effects, EffectType.VULNERABLE) > 0
        val dmg = if (isVulnerable) (rawDmg * 1.5).toInt() else rawDmg
        val blocked = minOf(enemy.block, dmg)
        val newBlock = maxOf(0, enemy.block - dmg)
        val newHp = maxOf(0, enemy.hp - maxOf(0, dmg - blocked))
        val updatedEnemies = enemies.toMutableList().also {
            it[idx] = enemy.copy(block = newBlock, hp = newHp)
        }
        return copy(enemies = updatedEnemies)
    }
}

private fun applyEffect(effects: List<EffectStack>, type: EffectType, val_: Int): List<EffectStack> {
    val idx = effects.indexOfFirst { it.type == type }
    return if (idx >= 0) {
        effects.toMutableList().also { it[idx] = it[idx].copy(stacks = it[idx].stacks + val_) }
    } else {
        effects + EffectStack(type, val_)
    }
}

private fun getEffectValue(effects: List<EffectStack>, type: EffectType): Int =
    effects.find { it.type == type }?.stacks ?: 0

private fun decrementEffect(effects: List<EffectStack>, type: EffectType): List<EffectStack> =
    effects.map { if (it.type == type) it.copy(stacks = maxOf(0, it.stacks - 1)) else it }
        .filter { it.stacks > 0 }
