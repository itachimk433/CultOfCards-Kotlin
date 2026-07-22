package com.mkdev.cultofcardsword.viewmodel

import androidx.lifecycle.ViewModel
import com.mkdev.cultofcardsword.data.*
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

data class AttackAnimation(
    val damage: Int,
    val targetEnemyIdx: Int = 0,
    val isPlayerAttack: Boolean = true,
    val attackerName: String = "",
    val isCritical: Boolean = false
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
    val playerMana: Int,
    val playerMaxMana: Int,
    val turn: Int,
    val phase: BattlePhase,
    val message: String,
    val isFirstAttack: Boolean,
    val energyBonusNextTurn: Int,
    val extraDraw: Int,
    val usedSkills: Boolean,
    val cardsPlayedThisTurn: Int = 0,
    val attackAnimation: AttackAnimation? = null,
    val totalDamageDealt: Int = 0,
    val totalTurnsPlayed: Int = 0
)

enum class BattlePhase { PLAYER, ENEMY, VICTORY, DEFEAT }

class BattleViewModel : ViewModel() {

    private val _battleState = MutableStateFlow<BattleState?>(null)
    val battleState: StateFlow<BattleState?> = _battleState.asStateFlow()

    fun initBattle(run: GameRun, floor: StoryFloor) {
        val enemies = floor.enemyIds.mapNotNull { eid ->
            val tmpl = ENEMIES[eid] ?: return@mapNotNull null
            BattleEnemy(
                templateId  = eid,
                name        = tmpl.name,
                maxHp       = tmpl.maxHp,
                hp          = tmpl.maxHp,
                block       = 0,
                effects     = emptyList(),
                moveIndex   = 0,
                moves       = tmpl.moves,
                isBoss      = tmpl.isBoss,
                strength    = 0
            )
        }

        val blockBonus  = run.relics.sumOf { it.blockBonus }
        val energyBonus = run.bonusEnergyPerTurn
        val extraDraw   = run.extraDrawPerTurn
        val maxEnergy   = run.maxEnergy + energyBonus
        val manaBonus   = run.relics.sumOf { it.manaBonus } + run.manaBonus
        val baseMana    = 20 + manaBonus

        val shuffledDeck = run.deck.shuffled()
        val drawCount    = 5 + extraDraw
        val hand         = shuffledDeck.take(drawCount)
        val drawPile     = shuffledDeck.drop(drawCount)

        _battleState.value = BattleState(
            drawPile             = drawPile,
            hand                 = hand,
            discardPile          = emptyList(),
            enemies              = enemies,
            energy               = maxEnergy,
            maxEnergy            = maxEnergy,
            playerHp             = run.playerHp,
            playerMaxHp          = run.playerMaxHp,
            playerBlock          = blockBonus,
            playerEffects        = emptyList(),
            playerStrength       = 0,
            playerMana           = baseMana,
            playerMaxMana        = baseMana,
            turn                 = 1,
            phase                = BattlePhase.PLAYER,
            message              = "Your turn — double-tap a card to use it.",
            isFirstAttack        = run.isFirstAttackDouble,
            energyBonusNextTurn  = 0,
            extraDraw            = extraDraw,
            usedSkills           = false,
            cardsPlayedThisTurn  = 0,
            attackAnimation      = null,
            totalDamageDealt     = 0,
            totalTurnsPlayed     = 0
        )
    }

    /** Returns an error string or null on success. */
    fun playCard(card: GameCard, run: GameRun): String? {
        val s = _battleState.value ?: return "No battle active"
        if (s.phase != BattlePhase.PLAYER) return "Not your turn"
        if (s.cardsPlayedThisTurn >= 1)     return "One card per turn!"
        if (card.cost > s.energy)           return "Not enough energy!"
        if (card.manaCost > s.playerMana)   return "Not enough mana!"

        var state = s.copy(
            energy              = s.energy - card.cost,
            playerMana          = s.playerMana - card.manaCost,
            hand                = s.hand.filter { it.id != card.id },
            discardPile         = s.discardPile + card,
            cardsPlayedThisTurn = s.cardsPlayedThisTurn + 1,
            usedSkills          = if (card.type == CardType.SKILL) true else s.usedSkills
        )

        val effects     = card.effects
        val hasEdgeTalisman = run.relics.any { it.id == "edge_talisman" }
        val attackBonus = run.attackBonus + (if (hasEdgeTalisman) 2 else 0)
        val isWeak      = state.getEffect(EffectType.WEAK) > 0

        var msg = card.name
        var totalDmg = state.totalDamageDealt

        // Mana gain
        if (effects.manaGain > 0) {
            state = state.copy(playerMana = minOf(state.playerMaxMana, state.playerMana + effects.manaGain))
        }

        // Damage to primary target
        if (effects.damage > 0 && state.enemies.isNotEmpty()) {
            var dmg = effects.damage + state.playerStrength + attackBonus
            if (state.isFirstAttack) { dmg *= 2; state = state.copy(isFirstAttack = false) }
            if (isWeak) dmg = (dmg * 0.75).toInt()
            val animation = AttackAnimation(
                damage          = dmg,
                targetEnemyIdx  = 0,
                isPlayerAttack  = true,
                attackerName    = card.name,
                isCritical      = state.isFirstAttack
            )
            state = state.dealDamageToEnemy(0, dmg).copy(attackAnimation = animation)
            totalDmg += dmg
            msg = "${card.name}: $dmg damage"
        }

        // Damage all enemies
        if (effects.damageAll > 0) {
            var dmg = effects.damageAll + state.playerStrength + attackBonus
            if (state.isFirstAttack) { dmg *= 2; state = state.copy(isFirstAttack = false) }
            if (isWeak) dmg = (dmg * 0.75).toInt()
            val newEnemies = state.enemies.mapIndexed { i, _ -> state.dealDamageToEnemy(i, dmg).enemies[i] }
            val aoe = AttackAnimation(damage = dmg, targetEnemyIdx = -1, isPlayerAttack = true, attackerName = card.name)
            state = state.copy(enemies = newEnemies, attackAnimation = aoe)
            totalDmg += dmg * newEnemies.count { it.hp > 0 }
            msg = "${card.name}: $dmg to all"
        }

        // Block
        if (effects.block > 0) {
            state = state.copy(playerBlock = state.playerBlock + effects.block)
            msg = "${card.name}: +${effects.block} block"
        }

        // Draw
        if (effects.draw > 0) state = state.drawCards(effects.draw)

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
            if (effects.poison     > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.POISON,     effects.poison))
            if (effects.burn       > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.BURN,       effects.burn))
            if (effects.freeze     > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.FREEZE,     effects.freeze))
            if (effects.vulnerable > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.VULNERABLE, effects.vulnerable))
            if (effects.weak       > 0) enemy = enemy.copy(effects = applyEffect(enemy.effects, EffectType.WEAK,       effects.weak))
            val newEnemies = state.enemies.toMutableList().also { it[0] = enemy }
            state = state.copy(enemies = newEnemies)
            if (effects.poison > 0) msg = "${card.name}: ${effects.poison} poison applied"
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

        state = state.copy(totalDamageDealt = totalDmg)

        // Check victory
        val aliveEnemies = state.enemies.filter { it.hp > 0 }
        if (aliveEnemies.isEmpty()) {
            state = state.copy(phase = BattlePhase.VICTORY, message = "Victory! The enemy falls!")
        } else {
            state = state.copy(message = msg)
        }

        _battleState.value = state
        return null
    }

    fun clearAnimation() {
        _battleState.value = _battleState.value?.copy(attackAnimation = null)
    }

    fun endTurn() {
        var s = _battleState.value ?: return
        if (s.phase != BattlePhase.PLAYER) return

        s = s.copy(phase = BattlePhase.ENEMY, totalTurnsPlayed = s.totalTurnsPlayed + 1)

        // Process each enemy
        val newEnemies = s.enemies.map { enemy ->
            if (enemy.hp <= 0) return@map enemy
            var e = enemy

            // Burn damage (does NOT decrement — stacks stay)
            val burnStacks = getEffectValue(e.effects, EffectType.BURN)
            if (burnStacks > 0) e = e.copy(hp = maxOf(0, e.hp - burnStacks))

            // Poison damage (decrements by 1 per turn)
            val poisonStacks = getEffectValue(e.effects, EffectType.POISON)
            if (poisonStacks > 0) {
                e = e.copy(
                    hp      = maxOf(0, e.hp - poisonStacks),
                    effects = decrementEffect(e.effects, EffectType.POISON)
                )
            }

            // Freeze reduces movement — skip attack if frozen
            val freezeStacks = getEffectValue(e.effects, EffectType.FREEZE)
            if (freezeStacks > 0) {
                e = e.copy(effects = decrementEffect(e.effects, EffectType.FREEZE))
                return@map e.copy(moveIndex = e.moveIndex + 1)
            }

            if (e.hp <= 0) return@map e

            // Execute move
            val move = e.moves[e.moveIndex % e.moves.size]
            when (move.type) {
                "attack" -> {
                    val dmg = move.value + e.strength
                    val isVulnerable = getEffectValue(s.playerEffects, EffectType.VULNERABLE) > 0
                    val finalDmg = if (isVulnerable) (dmg * 1.5).toInt() else dmg
                    val blocked  = minOf(s.playerBlock, finalDmg)
                    val netDmg   = maxOf(0, finalDmg - blocked)
                    s = s.copy(
                        playerBlock  = maxOf(0, s.playerBlock - finalDmg),
                        playerHp     = maxOf(0, s.playerHp - netDmg),
                        message      = "${e.name}: ${move.label} hit for $netDmg damage",
                        attackAnimation = AttackAnimation(
                            damage         = netDmg,
                            targetEnemyIdx = -1,
                            isPlayerAttack = false,
                            attackerName   = e.name
                        )
                    )
                    // Apply move's debuff to player
                    if (move.effectType != null && move.effectType != EffectType.STRENGTH) {
                        s = s.copy(playerEffects = applyEffect(s.playerEffects, move.effectType, move.effectValue))
                    }
                }
                "block" -> {
                    e = e.copy(block = e.block + move.value)
                    s = s.copy(message = "${e.name}: ${move.label} (+${move.value} block)")
                }
                "buff" -> {
                    if (move.effectType == EffectType.STRENGTH) {
                        e = e.copy(strength = e.strength + move.effectValue)
                    } else if (move.effectType != null) {
                        e = e.copy(effects = applyEffect(e.effects, move.effectType, move.effectValue))
                    }
                    s = s.copy(message = "${e.name}: ${move.label}")
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

        // Regenerate mana (5 per turn, up to max)
        val newMana = minOf(s.playerMaxMana, s.playerMana + 5)

        // Start next player turn
        val drawCount = 5 + s.extraDraw
        s = s.drawCards(drawCount)
        s = s.copy(
            playerBlock         = 0,
            playerMana          = newMana,
            energy              = s.maxEnergy + s.energyBonusNextTurn,
            energyBonusNextTurn = 0,
            enemies             = s.enemies.map { it.copy(block = 0) },
            turn                = s.turn + 1,
            phase               = BattlePhase.PLAYER,
            cardsPlayedThisTurn = 0,
            attackAnimation     = null
        )
        if (!s.message.contains("damage") && !s.message.contains("hit")) {
            s = s.copy(message = "Your turn — double-tap a card to use it.")
        }

        _battleState.value = s
    }

    fun clearBattle() { _battleState.value = null }

    // ---- Helpers ----

    private fun applyEffect(effects: List<EffectStack>, type: EffectType, value: Int): List<EffectStack> {
        val idx = effects.indexOfFirst { it.type == type }
        return if (idx >= 0) {
            effects.toMutableList().also { it[idx] = it[idx].copy(stacks = it[idx].stacks + value) }
        } else {
            effects + EffectStack(type, value)
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
        var draw    = drawPile.toMutableList()
        var discard = discardPile.toMutableList()
        var h       = hand.toMutableList()

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
        val dmg     = if (isVulnerable) (rawDmg * 1.5).toInt() else rawDmg
        val blocked = minOf(enemy.block, dmg)
        val newBlock = maxOf(0, enemy.block - dmg)
        val newHp    = maxOf(0, enemy.hp - maxOf(0, dmg - blocked))
        val updatedEnemies = enemies.toMutableList().also {
            it[idx] = enemy.copy(block = newBlock, hp = newHp)
        }
        return copy(enemies = updatedEnemies)
    }
}

// Top-level helpers used by enemy turn logic
private fun applyEffect(effects: List<EffectStack>, type: EffectType, value: Int): List<EffectStack> {
    val idx = effects.indexOfFirst { it.type == type }
    return if (idx >= 0) {
        effects.toMutableList().also { it[idx] = it[idx].copy(stacks = it[idx].stacks + value) }
    } else {
        effects + EffectStack(type, value)
    }
}

private fun getEffectValue(effects: List<EffectStack>, type: EffectType): Int =
    effects.find { it.type == type }?.stacks ?: 0

private fun decrementEffect(effects: List<EffectStack>, type: EffectType): List<EffectStack> =
    effects.map { if (it.type == type) it.copy(stacks = maxOf(0, it.stacks - 1)) else it }
        .filter { it.stacks > 0 }
