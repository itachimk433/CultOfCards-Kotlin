package com.mkdev.cultofcardsword.data

import kotlin.random.Random

// =====================
// RANK SYSTEM
// =====================

enum class SwordsmanRank(val displayName: String, val level: Int, val description: String) {
    NOVICE("Novice", 1, "Barely knows how to hold and swing a sword."),
    APPRENTICE("Apprentice", 2, "Learning basic stances, footwork, and attacks."),
    SWORDSMAN("Swordsman", 3, "Competent with one or more swords."),
    VETERAN("Veteran Swordsman", 4, "Experienced fighter with refined techniques."),
    ELITE("Elite Swordsman", 5, "Mastery over conventional swordsmanship."),
    EXPERT("Sword Expert", 6, "Can overwhelm ordinary swordsmen with superior skill."),
    MASTER("Sword Master", 7, "Has reached mastery; techniques become highly efficient."),
    GRAND_MASTER("Grand Sword Master", 8, "Near-perfect sword control; may create sword aura."),
    SAINT("Sword Saint", 9, "Legendary swordsman whose skill borders on the supernatural."),
    EMPEROR("Sword Emperor", 10, "One of the strongest swordsmen in the world."),
    GOD("Sword God", 11, "A divine-level swordsman capable of impossible feats."),
    ORIGIN("Sword Origin", 12, "The source of all sword techniques. Beyond mortal comprehension.")
}

fun rankFromBattlesWon(won: Int): SwordsmanRank = when {
    won >= 55 -> SwordsmanRank.ORIGIN
    won >= 44 -> SwordsmanRank.GOD
    won >= 35 -> SwordsmanRank.EMPEROR
    won >= 27 -> SwordsmanRank.SAINT
    won >= 20 -> SwordsmanRank.GRAND_MASTER
    won >= 14 -> SwordsmanRank.MASTER
    won >= 9  -> SwordsmanRank.EXPERT
    won >= 5  -> SwordsmanRank.ELITE
    won >= 3  -> SwordsmanRank.VETERAN
    won >= 2  -> SwordsmanRank.SWORDSMAN
    won >= 1  -> SwordsmanRank.APPRENTICE
    else      -> SwordsmanRank.NOVICE
}

// =====================
// SKILL STATS
// =====================

data class SkillStats(
    val attack: Int = 0,
    val defense: Int = 0,
    val agility: Int = 0,
    val mana: Int = 0,
    val spirit: Int = 0
) {
    val overallPower: Int get() = attack + defense + agility + mana + spirit
}

// =====================
// ENUMS & TYPES
// =====================

enum class CultId(
    val displayName: String,
    val subtitle: String,
    val description: String,
    val color: Long,
    val icon: String,
    val totalStats: SkillStats
) {
    DUAL(
        "Dual Swordsman", "Twin Blades, One Soul",
        "Master the art of wielding two swords simultaneously. Swift combo strikes overwhelm enemies before they can react.",
        0xFFD4A017, "⚔",
        SkillStats(attack = 20, defense = 10, agility = 10, mana = 5, spirit = 5)
    ),
    GREAT(
        "Greatswordsman", "Immovable Mountain, Crushing Wave",
        "Wield a massive two-handed sword with devastating force. Each blow carries the weight of mountains.",
        0xFF607D8B, "🗡",
        SkillStats(attack = 25, defense = 10, agility = 5, mana = 5, spirit = 5)
    ),
    RAPIER(
        "Rapier Swordsman", "Speed and Precision Above All",
        "Focus on lightning-fast precise thrusts. Draw more cards than any other style and strike before foes can respond.",
        0xFFB2BABB, "🤺",
        SkillStats(attack = 15, defense = 8, agility = 20, mana = 5, spirit = 2)
    ),
    SPELL(
        "Magic Swordsman", "Steel and Sorcery United",
        "Combine swordsmanship with arcane magic. Channel mana through your blade for supernatural attacks.",
        0xFF8B3DC8, "✨",
        SkillStats(attack = 12, defense = 8, agility = 5, mana = 20, spirit = 5)
    ),
    ELEMENTAL(
        "Elemental Swordsman", "Nature's Wrath in Blade Form",
        "Channel fire, ice, lightning, and wind through your sword. Each element bends to your blade.",
        0xFF00BCD4, "🌊",
        SkillStats(attack = 15, defense = 5, agility = 8, mana = 15, spirit = 7)
    ),
    HOLY(
        "Holy Swordsman", "Divine Light Banishes Darkness",
        "Wield divine power through a blessed blade. Heal your wounds and smite evil with righteous fury.",
        0xFFF1C40F, "✝",
        SkillStats(attack = 10, defense = 15, agility = 5, mana = 5, spirit = 15)
    ),
    DARK(
        "Dark Swordsman", "Shadow and Demonic Blade Arts",
        "Harness shadow and demonic energy to amplify your strikes. The darkness is your greatest weapon.",
        0xFF4A148C, "🌑",
        SkillStats(attack = 18, defense = 8, agility = 8, mana = 12, spirit = 4)
    ),
    SPIRIT(
        "Spirit Swordsman", "Contracts Forged Beyond the Veil",
        "Form bonds with ancient spirits who fight alongside you. Their power flows through your blade.",
        0xFF1E88E5, "👻",
        SkillStats(attack = 10, defense = 8, agility = 8, mana = 8, spirit = 16)
    ),
    RUNE(
        "Rune Swordsman", "Ancient Glyphs Enhance Every Edge",
        "Engrave magical runes onto your blade to unlock arcane enhancements. Each rune multiplies your power.",
        0xFF0277BD, "🔮",
        SkillStats(attack = 12, defense = 12, agility = 5, mana = 16, spirit = 5)
    ),
    DRAGON(
        "Dragon Swordsman", "Dragon's Heart, Dragonslayer's Edge",
        "Master dragon-slaying techniques or draw upon dragon power itself. Your blade drinks dragon essence.",
        0xFFB71C1C, "🐉",
        SkillStats(attack = 22, defense = 12, agility = 8, mana = 5, spirit = 3)
    ),
    DEMON(
        "Demon Swordsman", "Cursed Blade, Boundless Power",
        "Wield cursed and demonic sword arts. Great power demands great sacrifice — your wounds feed your strength.",
        0xFFE53935, "😈",
        SkillStats(attack = 20, defense = 10, agility = 8, mana = 8, spirit = 4)
    ),
    BLOOD(
        "Blood Swordsman", "Blood Fuels the Eternal Blade",
        "Use blood-based techniques that grow stronger as you shed blood. Pain is power for those who endure.",
        0xFFC62828, "🩸",
        SkillStats(attack = 18, defense = 5, agility = 10, mana = 8, spirit = 9)
    ),
    SHADOW(
        "Shadow Swordsman", "Strike Before They See You",
        "Specialize in stealth and assassinations. Strike from shadow, poison, and vanish before retaliation.",
        0xFF37474F, "🌫",
        SkillStats(attack = 16, defense = 5, agility = 18, mana = 8, spirit = 3)
    ),
    LIGHTNING(
        "Lightning Swordsman", "Faster Than Thunder, Sharper Than Light",
        "Rely on explosive speed beyond human comprehension. Strike and withdraw before enemies register the pain.",
        0xFFFFD600, "⚡",
        SkillStats(attack = 18, defense = 5, agility = 20, mana = 5, spirit = 2)
    ),
    WIND(
        "Wind Swordsman", "Agile as the Storm, Free as the Gale",
        "Flow like wind with agile, unblockable attacks. Draw cards and evade with natural grace.",
        0xFF29B6F6, "🌪",
        SkillStats(attack = 12, defense = 8, agility = 22, mana = 5, spirit = 3)
    ),
    FLAME(
        "Flame Swordsman", "Passion Burns in Every Strike",
        "Infuse every attack with searing flame. Burns stack on enemies while your fury grows each turn.",
        0xFFE64A19, "🔥",
        SkillStats(attack = 22, defense = 8, agility = 5, mana = 10, spirit = 5)
    ),
    ICE(
        "Ice Swordsman", "Frozen Resolve, Absolute Control",
        "Freeze enemies solid with ice-enhanced blade techniques. Control the battlefield with glacial precision.",
        0xFF00ACC1, "❄",
        SkillStats(attack = 12, defense = 20, agility = 5, mana = 10, spirit = 3)
    ),
    VOID(
        "Void Swordsman", "Where Nothing Is, My Blade Reigns",
        "Manipulate the emptiness of space to create cuts that ignore armor and pass through defenses.",
        0xFF1A237E, "🌀",
        SkillStats(attack = 15, defense = 10, agility = 8, mana = 15, spirit = 2)
    ),
    TIME(
        "Time Swordsman", "Past, Present, and Future Strike at Once",
        "Wield time-based sword techniques. Slow enemies, accelerate yourself, and strike moments twice.",
        0xFFA0522D, "⏳",
        SkillStats(attack = 12, defense = 12, agility = 10, mana = 12, spirit = 4)
    ),
    SPACE(
        "Space Swordsman", "Cut Through the Fabric of Existence",
        "Cut through space itself, teleporting strikes across any distance. Distance is an illusion you control.",
        0xFF283593, "🌌",
        SkillStats(attack = 15, defense = 8, agility = 12, mana = 12, spirit = 3)
    ),
    DIMENSIONAL(
        "Dimensional Swordsman", "Blades That Cross Worlds",
        "Attack across dimensions simultaneously. Your blade strikes from angles that defy physical reality.",
        0xFF6A1B9A, "🔭",
        SkillStats(attack = 15, defense = 10, agility = 10, mana = 12, spirit = 3)
    ),
    CELESTIAL(
        "Celestial Swordsman", "Starfire and Moonlight Forged as Steel",
        "Channel the power of stars and the heavens through your divine blade. Light itself is your weapon.",
        0xFFF57F17, "⭐",
        SkillStats(attack = 12, defense = 12, agility = 8, mana = 8, spirit = 10)
    ),
    CHAOS(
        "Chaos Swordsman", "Harness the Unpredictable",
        "Harness chaotic energy that defies prediction. Each strike could shatter reality — even you don't know what comes next.",
        0xFFC2185B, "🎲",
        SkillStats(attack = 20, defense = 5, agility = 12, mana = 10, spirit = 3)
    ),
    COSMIC(
        "Cosmic Swordsman", "Universal Forces Bow to Your Blade",
        "Manipulate universal forces and cosmic energy. Your sword is a focal point for the power of entire galaxies.",
        0xFF4527A0, "🌠",
        SkillStats(attack = 14, defense = 10, agility = 8, mana = 14, spirit = 4)
    ),
    IMMORTAL(
        "Immortal Swordsman", "Unending Journey, Unbreakable Will",
        "Nearly ageless and endlessly refining your art. Recover from wounds that would slay ordinary fighters.",
        0xFF2E7D32, "♾",
        SkillStats(attack = 10, defense = 18, agility = 8, mana = 8, spirit = 6)
    ),
    DAO(
        "Dao Sword Cultivator", "The Blade Is the Way, the Way Is the Blade",
        "Cultivate the Sword Dao through deep understanding. Your techniques grow more powerful the more you meditate.",
        0xFF33691E, "☯",
        SkillStats(attack = 14, defense = 12, agility = 8, mana = 12, spirit = 4)
    ),
    SAGE(
        "Sword Sage", "Enlightened Beyond Technique",
        "Your understanding of the sword transcends technique. A single glance reveals all weaknesses; one breath decides the duel.",
        0xFF546E7A, "🧙",
        SkillStats(attack = 12, defense = 12, agility = 8, mana = 10, spirit = 8)
    ),
    MONARCH(
        "Sword Monarch", "All Swordsmen Bend the Knee",
        "The ruler among swordsmen. Your presence breaks wills, and your blade breaks armies.",
        0xFF6A0572, "👑",
        SkillStats(attack = 18, defense = 15, agility = 8, mana = 5, spirit = 4)
    ),
    KING(
        "Sword King", "The Strongest Blade Under Heaven",
        "The title given to the strongest mortal swordsman. Every technique perfected; every rival surpassed.",
        0xFFF9A825, "🏆",
        SkillStats(attack = 20, defense = 15, agility = 8, mana = 5, spirit = 2)
    )
}

enum class CardType { ATTACK, SKILL, POWER }
enum class CardRarity { STARTER, COMMON, UNCOMMON, RARE }
enum class EffectType { POISON, BURN, WEAK, VULNERABLE, STRENGTH, FREEZE }

// =====================
// DATA CLASSES
// =====================

data class CardEffect(
    val damage: Int = 0,
    val damageAll: Int = 0,
    val block: Int = 0,
    val draw: Int = 0,
    val heal: Int = 0,
    val poison: Int = 0,
    val burn: Int = 0,
    val freeze: Int = 0,
    val weak: Int = 0,
    val vulnerable: Int = 0,
    val strength: Int = 0,
    val energyNext: Int = 0,
    val loseHp: Int = 0,
    val manaGain: Int = 0
)

data class GameCard(
    val id: String,
    val templateId: String,
    val name: String,
    val cult: String,
    val type: CardType,
    val cost: Int,
    val manaCost: Int = 0,
    val effects: CardEffect,
    val description: String,
    val rarity: CardRarity,
    val skillStats: SkillStats = SkillStats()
)

data class EffectStack(val type: EffectType, val stacks: Int)

data class EnemyMoveData(
    val type: String,
    val value: Int,
    val label: String,
    val effectType: EffectType? = null,
    val effectValue: Int = 0
)

data class EnemyTemplate(
    val id: String,
    val name: String,
    val maxHp: Int,
    val moves: List<EnemyMoveData>,
    val isBoss: Boolean
)

data class Relic(
    val id: String,
    val name: String,
    val description: String,
    val trigger: String,
    val blockBonus: Int = 0,
    val healAfterBattle: Int = 0,
    val energyBonus: Int = 0,
    val firstAttackDouble: Boolean = false,
    val extraDraw: Int = 0,
    val attackBonus: Int = 0,
    val extraMaxHp: Int = 0,
    val goldAfterBattle: Int = 0,
    val manaBonus: Int = 0
)

data class Quest(
    val id: String,
    val name: String,
    val description: String,
    val goal: Int,
    val rewardType: String,
    val rewardAmount: Int = 0,
    val trackKey: String,
    val completed: Boolean
)

data class StoryDialogue(val speaker: String, val text: String)

data class StoryFloor(
    val floor: Int,
    val type: String,
    val enemyIds: List<String>,
    val isBoss: Boolean,
    val dialogue: List<StoryDialogue> = emptyList(),
    val eventText: String = "",
    val eventReward: String = ""
)

data class StoryAct(
    val id: Int,
    val title: String,
    val intro: String,
    val floors: List<StoryFloor>
)

// =====================
// HELPERS
// =====================

fun makeCardId(): String = System.currentTimeMillis().toString(36) + Random.nextLong().toString(36)

// =====================
// CULT STARTING DECKS (10 cards per cult)
// =====================

val CULT_START_CARDS: Map<CultId, List<String>> = mapOf(
    CultId.DUAL      to listOf("dual_slash","dual_slash","dual_guard","dual_guard","dual_dance","dual_step","dual_strike","dual_ward","dual_cross","dual_power"),
    CultId.GREAT     to listOf("great_cleave","great_cleave","great_guard","great_guard","great_brace","great_momentum","great_slam","great_fortress","great_titan","great_power"),
    CultId.RAPIER    to listOf("rapier_thrust","rapier_thrust","rapier_parry","rapier_parry","rapier_finesse","rapier_grace","rapier_lunge","rapier_deflect","rapier_riposte","rapier_precision"),
    CultId.SPELL     to listOf("spell_edge","spell_edge","spell_ward","spell_ward","spell_surge","spell_channel","spell_burst","spell_barrier","spell_resonance","spell_awakening"),
    CultId.ELEMENTAL to listOf("elem_strike","elem_strike","elem_guard","elem_guard","elem_surge","elem_shift","elem_blast","elem_barrier","elem_storm","elem_mastery"),
    CultId.HOLY      to listOf("holy_smite","holy_smite","holy_aegis","holy_aegis","holy_light","holy_prayer","holy_radiance","holy_fortress","holy_blessing","holy_judgment"),
    CultId.DARK      to listOf("dark_slash","dark_slash","dark_shroud","dark_shroud","dark_drain","dark_surge","dark_strike","dark_wall","dark_pact","dark_power"),
    CultId.SPIRIT    to listOf("spirit_slash","spirit_slash","spirit_veil","spirit_veil","spirit_call","spirit_surge","spirit_strike","spirit_ward","spirit_bond","spirit_power"),
    CultId.RUNE      to listOf("rune_edge","rune_edge","rune_ward","rune_ward","rune_inscribe","rune_surge","rune_blast","rune_fortress","rune_resonance","rune_mastery"),
    CultId.DRAGON    to listOf("dragon_fang","dragon_fang","dragon_scale","dragon_scale","dragon_breath","dragon_surge","dragon_claw","dragon_hide","dragon_roar","dragon_power"),
    CultId.DEMON     to listOf("demon_slash","demon_slash","demon_ward","demon_ward","demon_drain","demon_surge","demon_strike","demon_barrier","demon_pact","demon_power"),
    CultId.BLOOD     to listOf("blood_slash","blood_slash","blood_veil","blood_veil","blood_drain","blood_surge","blood_strike","blood_ward","blood_pact","blood_power"),
    CultId.SHADOW    to listOf("shadow_stab","shadow_stab","shadow_veil","shadow_veil","shadow_step","shadow_surge","shadow_strike","shadow_cloak","shadow_mark","shadow_power"),
    CultId.LIGHTNING to listOf("lightning_slash","lightning_slash","lightning_ward","lightning_ward","lightning_step","lightning_surge","lightning_strike","lightning_barrier","lightning_chain","lightning_power"),
    CultId.WIND      to listOf("wind_slash","wind_slash","wind_veil","wind_veil","wind_step","wind_surge","wind_strike","wind_ward","wind_dance","wind_power"),
    CultId.FLAME     to listOf("flame_slash","flame_slash","flame_guard","flame_guard","flame_ignite","flame_surge","flame_strike","flame_wall","flame_inferno","flame_power"),
    CultId.ICE       to listOf("ice_slash","ice_slash","ice_ward","ice_ward","ice_freeze","ice_surge","ice_strike","ice_barrier","ice_storm","ice_power"),
    CultId.VOID      to listOf("void_slash","void_slash","void_veil","void_veil","void_step","void_surge","void_strike","void_ward","void_rift","void_power"),
    CultId.TIME      to listOf("time_slash","time_slash","time_ward","time_ward","time_slow","time_surge","time_strike","time_barrier","time_rewind","time_power"),
    CultId.SPACE     to listOf("space_slash","space_slash","space_ward","space_ward","space_step","space_surge","space_strike","space_barrier","space_rift","space_power"),
    CultId.DIMENSIONAL to listOf("dim_slash","dim_slash","dim_ward","dim_ward","dim_step","dim_surge","dim_strike","dim_barrier","dim_cross","dim_power"),
    CultId.CELESTIAL to listOf("cel_slash","cel_slash","cel_ward","cel_ward","cel_light","cel_surge","cel_strike","cel_barrier","cel_radiance","cel_power"),
    CultId.CHAOS     to listOf("chaos_slash","chaos_slash","chaos_ward","chaos_ward","chaos_wild","chaos_surge","chaos_strike","chaos_barrier","chaos_unravel","chaos_power"),
    CultId.COSMIC    to listOf("cosmic_slash","cosmic_slash","cosmic_ward","cosmic_ward","cosmic_pull","cosmic_surge","cosmic_strike","cosmic_barrier","cosmic_collapse","cosmic_power"),
    CultId.IMMORTAL  to listOf("immortal_slash","immortal_slash","immortal_ward","immortal_ward","immortal_endure","immortal_surge","immortal_strike","immortal_barrier","immortal_rebirth","immortal_power"),
    CultId.DAO       to listOf("dao_slash","dao_slash","dao_ward","dao_ward","dao_insight","dao_surge","dao_strike","dao_barrier","dao_harmony","dao_power"),
    CultId.SAGE      to listOf("sage_slash","sage_slash","sage_ward","sage_ward","sage_insight","sage_surge","sage_strike","sage_barrier","sage_enlighten","sage_power"),
    CultId.MONARCH   to listOf("monarch_slash","monarch_slash","monarch_ward","monarch_ward","monarch_decree","monarch_surge","monarch_strike","monarch_barrier","monarch_domain","monarch_power"),
    CultId.KING      to listOf("king_slash","king_slash","king_ward","king_ward","king_dominion","king_surge","king_strike","king_barrier","king_conquest","king_power")
)

// =====================
// CARD TEMPLATES
// =====================

val CARD_TEMPLATES: Map<String, GameCard> = mapOf(

    // ---- DUAL SWORDSMAN (ATK20 DEF10 AGI10 MAN5 SPI5 = 50) ----
    "dual_slash"  to GameCard("","dual_slash","Twin Slash","dual",CardType.ATTACK,1,0,CardEffect(damage=9),"Deal 9 damage.",CardRarity.COMMON,SkillStats(attack=3,defense=1,agility=1)),
    "dual_guard"  to GameCard("","dual_guard","Cross Guard","dual",CardType.SKILL,1,0,CardEffect(block=7,draw=1),"Gain 7 Block. Draw 1.",CardRarity.COMMON,SkillStats(attack=1,defense=2,agility=2)),
    "dual_strike" to GameCard("","dual_strike","Dual Fangs","dual",CardType.ATTACK,2,0,CardEffect(damage=16),"Deal 16 damage.",CardRarity.COMMON,SkillStats(attack=4,defense=1,agility=1)),
    "dual_ward"   to GameCard("","dual_ward","Parry Stance","dual",CardType.SKILL,2,0,CardEffect(block=12),"Gain 12 Block.",CardRarity.COMMON,SkillStats(attack=1,defense=3,agility=1)),
    "dual_dance"  to GameCard("","dual_dance","Blade Dance","dual",CardType.SKILL,1,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=3,mana=1,spirit=1)),
    "dual_step"   to GameCard("","dual_step","Step & Slash","dual",CardType.SKILL,0,0,CardEffect(draw=1),"Draw 1 card.",CardRarity.COMMON,SkillStats(agility=2,attack=1)),
    "dual_cross"  to GameCard("","dual_cross","Scissor Cross","dual",CardType.ATTACK,2,0,CardEffect(damage=10,block=8),"Deal 10 damage. Gain 8 Block.",CardRarity.UNCOMMON,SkillStats(attack=3,defense=2,agility=1)),
    "dual_power"  to GameCard("","dual_power","Twin Spirit","dual",CardType.POWER,2,0,CardEffect(strength=2),"Gain 2 Strength permanently.",CardRarity.UNCOMMON,SkillStats(attack=3,spirit=2)),
    "dual_fury"   to GameCard("","dual_fury","Spinning Fury","dual",CardType.ATTACK,3,0,CardEffect(damageAll=9),"Deal 9 to all enemies.",CardRarity.RARE,SkillStats(attack=4,agility=2)),
    "dual_ultimate" to GameCard("","dual_ultimate","One Thousand Cuts","dual",CardType.POWER,3,0,CardEffect(strength=3,energyNext=1),"Gain 3 Strength. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=4,agility=1,mana=1,spirit=1)),

    // ---- GREATSWORDSMAN (ATK25 DEF10 AGI5 MAN5 SPI5 = 50) ----
    "great_cleave"   to GameCard("","great_cleave","Titan Cleave","great",CardType.ATTACK,1,0,CardEffect(damage=10),"Deal 10 damage.",CardRarity.COMMON,SkillStats(attack=4,defense=1)),
    "great_guard"    to GameCard("","great_guard","Iron Bastion","great",CardType.SKILL,1,0,CardEffect(block=8),"Gain 8 Block.",CardRarity.COMMON,SkillStats(defense=3,agility=1,spirit=1)),
    "great_slam"     to GameCard("","great_slam","Earthshatter","great",CardType.ATTACK,2,0,CardEffect(damage=18),"Deal 18 damage.",CardRarity.COMMON,SkillStats(attack=5,defense=1)),
    "great_fortress" to GameCard("","great_fortress","Fortress Stance","great",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,agility=1)),
    "great_brace"    to GameCard("","great_brace","Brace & Draw","great",CardType.SKILL,1,0,CardEffect(block=4,draw=1),"Gain 4 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=2,agility=1,spirit=1)),
    "great_momentum" to GameCard("","great_momentum","Build Momentum","great",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=1,mana=2,spirit=1)),
    "great_titan"    to GameCard("","great_titan","Titan's Resolve","great",CardType.SKILL,2,0,CardEffect(block=16),"Gain 16 Block.",CardRarity.UNCOMMON,SkillStats(defense=4,spirit=1)),
    "great_power"    to GameCard("","great_power","Colossus Form","great",CardType.POWER,2,0,CardEffect(strength=3),"Gain 3 Strength permanently.",CardRarity.UNCOMMON,SkillStats(attack=4,spirit=1)),
    "great_shockwave" to GameCard("","great_shockwave","Shockwave","great",CardType.ATTACK,3,0,CardEffect(damageAll=12),"Deal 12 to all enemies.",CardRarity.RARE,SkillStats(attack=5,agility=1)),
    "great_doom"     to GameCard("","great_doom","Doom Crusher","great",CardType.POWER,3,0,CardEffect(strength=4,energyNext=1),"Gain 4 Strength. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=5,mana=1,spirit=1)),

    // ---- RAPIER SWORDSMAN (ATK15 DEF8 AGI20 MAN5 SPI2 = 50) ----
    "rapier_thrust"    to GameCard("","rapier_thrust","Quick Thrust","rapier",CardType.ATTACK,1,0,CardEffect(damage=8,draw=1),"Deal 8 damage. Draw 1.",CardRarity.COMMON,SkillStats(attack=2,agility=3)),
    "rapier_parry"     to GameCard("","rapier_parry","Perfect Parry","rapier",CardType.SKILL,1,0,CardEffect(block=6,draw=1),"Gain 6 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=2,agility=3)),
    "rapier_lunge"     to GameCard("","rapier_lunge","Lunge Strike","rapier",CardType.ATTACK,2,0,CardEffect(damage=14),"Deal 14 damage.",CardRarity.COMMON,SkillStats(attack=3,agility=2)),
    "rapier_deflect"   to GameCard("","rapier_deflect","Light Deflect","rapier",CardType.SKILL,2,0,CardEffect(block=10,draw=1),"Gain 10 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=2,agility=3)),
    "rapier_finesse"   to GameCard("","rapier_finesse","Finesse Flow","rapier",CardType.SKILL,1,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=4,mana=1)),
    "rapier_grace"     to GameCard("","rapier_grace","Blademaster's Grace","rapier",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=4,spirit=1)),
    "rapier_riposte"   to GameCard("","rapier_riposte","Riposte","rapier",CardType.ATTACK,2,0,CardEffect(damage=12,block=6),"Deal 12 damage. Gain 6 Block.",CardRarity.UNCOMMON,SkillStats(attack=2,defense=2,agility=2)),
    "rapier_precision" to GameCard("","rapier_precision","Precision Form","rapier",CardType.POWER,2,0,CardEffect(strength=2,draw=1),"Gain 2 Strength. Draw 1 extra per turn.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=3,spirit=1)),
    "rapier_flurry"    to GameCard("","rapier_flurry","Flurry of Blades","rapier",CardType.ATTACK,3,0,CardEffect(damage=6,draw=2),"Deal 6 damage. Draw 2.",CardRarity.RARE,SkillStats(attack=2,agility=4,mana=1)),
    "rapier_mastery"   to GameCard("","rapier_mastery","Rapier Mastery","rapier",CardType.POWER,3,0,CardEffect(strength=2,draw=1,energyNext=1),"Gain 2 Strength. Draw 1 extra per turn. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=2,agility=4,mana=1,spirit=1)),

    // ---- MAGIC SWORDSMAN (ATK12 DEF8 AGI5 MAN20 SPI5 = 50) ----
    "spell_edge"      to GameCard("","spell_edge","Arcane Edge","spell",CardType.ATTACK,1,2,CardEffect(damage=10),"Deal 10 damage. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3,spirit=1)),
    "spell_ward"      to GameCard("","spell_ward","Mana Ward","spell",CardType.SKILL,1,0,CardEffect(block=7,manaGain=3),"Gain 7 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "spell_surge"     to GameCard("","spell_surge","Mana Surge","spell",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=4),"Draw 1. Gain 4 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=4)),
    "spell_channel"   to GameCard("","spell_channel","Channel Arcane","spell",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=2),"Draw 2. Gain 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "spell_burst"     to GameCard("","spell_burst","Spell Burst","spell",CardType.ATTACK,2,4,CardEffect(damage=18),"Deal 18 damage. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=4)),
    "spell_barrier"   to GameCard("","spell_barrier","Arcane Barrier","spell",CardType.SKILL,2,0,CardEffect(block=12,manaGain=2),"Gain 12 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "spell_resonance" to GameCard("","spell_resonance","Resonance","spell",CardType.SKILL,1,3,CardEffect(draw=3),"Draw 3 cards. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(agility=2,mana=4)),
    "spell_awakening" to GameCard("","spell_awakening","Arcane Awakening","spell",CardType.POWER,2,5,CardEffect(strength=3,energyNext=1),"Gain 3 Strength. +1 Energy next turn. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=2,mana=4,spirit=1)),
    "spell_blast"     to GameCard("","spell_blast","Arcane Blast","spell",CardType.ATTACK,3,6,CardEffect(damageAll=12),"Deal 12 to all enemies. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=4)),
    "spell_mastery"   to GameCard("","spell_mastery","Spellblade Mastery","spell",CardType.POWER,3,8,CardEffect(strength=2,draw=1,manaGain=6),"Gain 2 Strength. Draw 1 extra per turn. Gain 6 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=1,mana=4,spirit=2)),

    // ---- ELEMENTAL SWORDSMAN (ATK15 DEF5 AGI8 MAN15 SPI7 = 50) ----
    "elem_strike"  to GameCard("","elem_strike","Elemental Edge","elemental",CardType.ATTACK,1,2,CardEffect(damage=9,burn=2),"Deal 9 damage. Apply 2 Burn. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=2,spirit=1)),
    "elem_guard"   to GameCard("","elem_guard","Elemental Guard","elemental",CardType.SKILL,1,0,CardEffect(block=6,manaGain=2),"Gain 6 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=2,spirit=1)),
    "elem_blast"   to GameCard("","elem_blast","Storm Blade","elemental",CardType.ATTACK,2,3,CardEffect(damage=14,vulnerable=1),"Deal 14 damage. Apply 1 Vulnerable. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3,spirit=1)),
    "elem_barrier" to GameCard("","elem_barrier","Elemental Barrier","elemental",CardType.SKILL,2,0,CardEffect(block=10,manaGain=3),"Gain 10 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3,spirit=1)),
    "elem_surge"   to GameCard("","elem_surge","Elemental Surge","elemental",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=4),"Draw 1. Gain 4 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "elem_shift"   to GameCard("","elem_shift","Element Shift","elemental",CardType.SKILL,1,0,CardEffect(draw=2),"Draw 2. Swap damage type.",CardRarity.COMMON,SkillStats(agility=3,mana=2)),
    "elem_storm"   to GameCard("","elem_storm","Elemental Storm","elemental",CardType.ATTACK,3,5,CardEffect(damageAll=10,burn=2),"Deal 10 to all. Apply 2 Burn to all. Cost 5 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=3,spirit=1)),
    "elem_mastery" to GameCard("","elem_mastery","Elemental Mastery","elemental",CardType.POWER,2,4,CardEffect(strength=2,manaGain=4),"Gain 2 Strength. Gain 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,mana=4,spirit=1)),
    "elem_wrath"   to GameCard("","elem_wrath","Nature's Wrath","elemental",CardType.ATTACK,2,3,CardEffect(damage=12,poison=3),"Deal 12 damage. Apply 3 Poison. Cost 3 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=3,spirit=1)),
    "elem_avatar"  to GameCard("","elem_avatar","Elemental Avatar","elemental",CardType.POWER,3,6,CardEffect(strength=3,energyNext=1,manaGain=5),"Gain 3 Strength. +1 Energy next turn. Gain 5 Mana.",CardRarity.RARE,SkillStats(attack=2,mana=4,spirit=2)),

    // ---- HOLY SWORDSMAN (ATK10 DEF15 AGI5 MAN5 SPI15 = 50) ----
    "holy_smite"    to GameCard("","holy_smite","Holy Smite","holy",CardType.ATTACK,1,0,CardEffect(damage=8),"Deal 8 damage. (Deals 12 vs undead/dark)",CardRarity.COMMON,SkillStats(attack=2,spirit=3)),
    "holy_aegis"    to GameCard("","holy_aegis","Divine Aegis","holy",CardType.SKILL,1,0,CardEffect(block=8),"Gain 8 Block.",CardRarity.COMMON,SkillStats(defense=3,spirit=2)),
    "holy_radiance" to GameCard("","holy_radiance","Holy Radiance","holy",CardType.ATTACK,2,0,CardEffect(damage=10,weak=2),"Deal 10 damage. Apply 2 Weak.",CardRarity.COMMON,SkillStats(attack=2,spirit=3)),
    "holy_fortress" to GameCard("","holy_fortress","Holy Fortress","holy",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,spirit=1)),
    "holy_light"    to GameCard("","holy_light","Divine Light","holy",CardType.SKILL,1,0,CardEffect(heal=8,draw=1),"Heal 8 HP. Draw 1.",CardRarity.COMMON,SkillStats(defense=1,spirit=4)),
    "holy_prayer"   to GameCard("","holy_prayer","Sacred Prayer","holy",CardType.SKILL,0,0,CardEffect(heal=4),"Heal 4 HP.",CardRarity.COMMON,SkillStats(spirit=3,mana=1,defense=1)),
    "holy_blessing" to GameCard("","holy_blessing","Holy Blessing","holy",CardType.SKILL,2,0,CardEffect(heal=12,block=6),"Heal 12 HP. Gain 6 Block.",CardRarity.UNCOMMON,SkillStats(defense=2,spirit=4)),
    "holy_judgment" to GameCard("","holy_judgment","Divine Judgment","holy",CardType.ATTACK,3,0,CardEffect(damage=20,vulnerable=2),"Deal 20 damage. Apply 2 Vulnerable.",CardRarity.UNCOMMON,SkillStats(attack=3,spirit=3)),
    "holy_guardian" to GameCard("","holy_guardian","Holy Guardian","holy",CardType.POWER,2,0,CardEffect(strength=2,heal=6),"Gain 2 Strength. Heal 6 HP.",CardRarity.RARE,SkillStats(attack=1,defense=2,spirit=3)),
    "holy_divine"   to GameCard("","holy_divine","Divine Wrath","holy",CardType.POWER,3,0,CardEffect(strength=2,energyNext=1,heal=8),"Gain 2 Strength. +1 Energy next turn. Heal 8 HP.",CardRarity.RARE,SkillStats(attack=2,defense=1,spirit=4,mana=1)),

    // ---- DARK SWORDSMAN (ATK18 DEF8 AGI8 MAN12 SPI4 = 50) ----
    "dark_slash"  to GameCard("","dark_slash","Shadow Slash","dark",CardType.ATTACK,1,0,CardEffect(damage=9),"Deal 9 damage.",CardRarity.COMMON,SkillStats(attack=3,agility=1,mana=1)),
    "dark_shroud" to GameCard("","dark_shroud","Dark Shroud","dark",CardType.SKILL,1,0,CardEffect(block=7),"Gain 7 Block.",CardRarity.COMMON,SkillStats(defense=2,agility=1,mana=1,spirit=1)),
    "dark_strike" to GameCard("","dark_strike","Darkness Strike","dark",CardType.ATTACK,2,2,CardEffect(damage=15,vulnerable=1),"Deal 15 damage. Apply 1 Vulnerable. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=3,mana=2,spirit=1)),
    "dark_wall"   to GameCard("","dark_wall","Void Wall","dark",CardType.SKILL,2,0,CardEffect(block=12,manaGain=2),"Gain 12 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "dark_drain"  to GameCard("","dark_drain","Life Drain","dark",CardType.ATTACK,1,2,CardEffect(damage=6,heal=4),"Deal 6 damage. Heal 4 HP. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=2,spirit=1)),
    "dark_surge"  to GameCard("","dark_surge","Dark Surge","dark",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "dark_pact"   to GameCard("","dark_pact","Dark Pact","dark",CardType.SKILL,1,4,CardEffect(draw=3,loseHp=3),"Lose 3 HP. Draw 3. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(agility=2,mana=3,spirit=1)),
    "dark_power"  to GameCard("","dark_power","Demonic Power","dark",CardType.POWER,2,4,CardEffect(strength=3),"Gain 3 Strength permanently. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=4,mana=2,spirit=1)),
    "dark_oblivion" to GameCard("","dark_oblivion","Oblivion Blade","dark",CardType.ATTACK,3,0,CardEffect(damage=22),"Deal 22 damage.",CardRarity.RARE,SkillStats(attack=5,agility=1,mana=1)),
    "dark_nightmare" to GameCard("","dark_nightmare","Living Nightmare","dark",CardType.POWER,3,5,CardEffect(strength=3,draw=1,energyNext=1),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=3,agility=1,mana=2,spirit=1)),

    // ---- SPIRIT SWORDSMAN (ATK10 DEF8 AGI8 MAN8 SPI16 = 50) ----
    "spirit_slash"  to GameCard("","spirit_slash","Spirit Edge","spirit",CardType.ATTACK,1,0,CardEffect(damage=8),"Deal 8 damage. Spirit-infused.",CardRarity.COMMON,SkillStats(attack=2,spirit=3)),
    "spirit_veil"   to GameCard("","spirit_veil","Spirit Veil","spirit",CardType.SKILL,1,0,CardEffect(block=7),"Gain 7 Block.",CardRarity.COMMON,SkillStats(defense=2,spirit=3)),
    "spirit_strike" to GameCard("","spirit_strike","Spirit Strike","spirit",CardType.ATTACK,2,0,CardEffect(damage=12,heal=5),"Deal 12 damage. Heal 5 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=4)),
    "spirit_ward"   to GameCard("","spirit_ward","Spirit Ward","spirit",CardType.SKILL,2,0,CardEffect(block=10,heal=4),"Gain 10 Block. Heal 4 HP.",CardRarity.COMMON,SkillStats(defense=2,spirit=3)),
    "spirit_call"   to GameCard("","spirit_call","Spirit Call","spirit",CardType.SKILL,1,0,CardEffect(draw=2,heal=3),"Draw 2. Heal 3 HP.",CardRarity.COMMON,SkillStats(agility=2,spirit=3,mana=1)),
    "spirit_surge"  to GameCard("","spirit_surge","Spirit Surge","spirit",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=2),"Draw 1. Gain 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=2,spirit=1)),
    "spirit_bond"   to GameCard("","spirit_bond","Spirit Contract","spirit",CardType.SKILL,2,0,CardEffect(heal=14,draw=1),"Heal 14 HP. Draw 1.",CardRarity.UNCOMMON,SkillStats(defense=1,agility=1,spirit=4,mana=1)),
    "spirit_power"  to GameCard("","spirit_power","Spirit Empowerment","spirit",CardType.POWER,2,0,CardEffect(strength=2,heal=6),"Gain 2 Strength. Heal 6 HP permanently.",CardRarity.UNCOMMON,SkillStats(attack=1,spirit=5)),
    "spirit_guardian" to GameCard("","spirit_guardian","Guardian Spirit","spirit",CardType.ATTACK,3,0,CardEffect(damage=16,heal=8,block=6),"Deal 16 damage. Heal 8. Gain 6 Block.",CardRarity.RARE,SkillStats(attack=2,defense=1,spirit=4,mana=1)),
    "spirit_ascension" to GameCard("","spirit_ascension","Spirit Ascension","spirit",CardType.POWER,3,0,CardEffect(strength=2,draw=1,heal=10,energyNext=1),"Gain 2 Strength. Draw 1 extra. Heal 10. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=1,agility=1,spirit=4,mana=1)),

    // ---- RUNE SWORDSMAN (ATK12 DEF12 AGI5 MAN16 SPI5 = 50) ----
    "rune_edge"      to GameCard("","rune_edge","Rune Edge","rune",CardType.ATTACK,1,2,CardEffect(damage=10),"Deal 10 damage. Runic enhancement. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "rune_ward"      to GameCard("","rune_ward","Rune Ward","rune",CardType.SKILL,1,0,CardEffect(block=8,manaGain=2),"Gain 8 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "rune_blast"     to GameCard("","rune_blast","Rune Blast","rune",CardType.ATTACK,2,3,CardEffect(damage=14,vulnerable=1),"Deal 14 damage. Apply 1 Vulnerable. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3,spirit=1)),
    "rune_fortress"  to GameCard("","rune_fortress","Rune Fortress","rune",CardType.SKILL,2,0,CardEffect(block=12,manaGain=3),"Gain 12 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=3,mana=3)),
    "rune_inscribe"  to GameCard("","rune_inscribe","Inscribe Rune","rune",CardType.SKILL,1,0,CardEffect(draw=1,manaGain=4),"Draw 1. Gain 4 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=4)),
    "rune_surge"     to GameCard("","rune_surge","Runic Surge","rune",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=3,spirit=1)),
    "rune_resonance" to GameCard("","rune_resonance","Runic Resonance","rune",CardType.SKILL,2,4,CardEffect(draw=3,block=6),"Draw 3. Gain 6 Block. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(defense=1,agility=2,mana=3)),
    "rune_mastery"   to GameCard("","rune_mastery","Rune Mastery","rune",CardType.POWER,2,5,CardEffect(strength=2,manaGain=5),"Gain 2 Strength. Gain 5 Mana permanently per turn. Cost 5 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,mana=4,spirit=1)),
    "rune_annihilate" to GameCard("","rune_annihilate","Runic Annihilation","rune",CardType.ATTACK,3,6,CardEffect(damageAll=14),"Deal 14 to all enemies. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=4)),
    "rune_divine"    to GameCard("","rune_divine","Divine Rune","rune",CardType.POWER,3,8,CardEffect(strength=3,draw=1,energyNext=1,manaGain=6),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 6 Mana. Cost 8 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=1,mana=4,spirit=1)),

    // ---- DRAGON SWORDSMAN (ATK22 DEF12 AGI8 MAN5 SPI3 = 50) ----
    "dragon_fang"   to GameCard("","dragon_fang","Dragon Fang","dragon",CardType.ATTACK,1,0,CardEffect(damage=10),"Deal 10 damage.",CardRarity.COMMON,SkillStats(attack=4,defense=1)),
    "dragon_scale"  to GameCard("","dragon_scale","Dragon Scale","dragon",CardType.SKILL,1,0,CardEffect(block=9),"Gain 9 Block.",CardRarity.COMMON,SkillStats(defense=3,attack=1,spirit=1)),
    "dragon_claw"   to GameCard("","dragon_claw","Dragon Claw","dragon",CardType.ATTACK,2,0,CardEffect(damage=18),"Deal 18 damage.",CardRarity.COMMON,SkillStats(attack=5,defense=1)),
    "dragon_hide"   to GameCard("","dragon_hide","Dragon Hide","dragon",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,spirit=1)),
    "dragon_breath" to GameCard("","dragon_breath","Dragon Breath","dragon",CardType.ATTACK,2,0,CardEffect(damage=10,burn=4),"Deal 10 damage. Apply 4 Burn.",CardRarity.COMMON,SkillStats(attack=3,agility=1,mana=1)),
    "dragon_surge"  to GameCard("","dragon_surge","Dragon Spirit","dragon",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=2,mana=2)),
    "dragon_roar"   to GameCard("","dragon_roar","Dragon Roar","dragon",CardType.SKILL,2,0,CardEffect(weak=3,vulnerable=2),"Apply 3 Weak and 2 Vulnerable to enemy.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=2,spirit=1)),
    "dragon_power"  to GameCard("","dragon_power","Dragon Empowerment","dragon",CardType.POWER,2,0,CardEffect(strength=3),"Gain 3 Strength permanently.",CardRarity.UNCOMMON,SkillStats(attack=4,spirit=1)),
    "dragon_fury"   to GameCard("","dragon_fury","Dragon Fury","dragon",CardType.ATTACK,3,0,CardEffect(damageAll=12,burn=3),"Deal 12 to all enemies. Apply 3 Burn to all.",CardRarity.RARE,SkillStats(attack=5,mana=1)),
    "dragon_ascend" to GameCard("","dragon_ascend","Dragon Ascension","dragon",CardType.POWER,3,0,CardEffect(strength=4,energyNext=1),"Gain 4 Strength. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=5,agility=1,mana=1,spirit=1)),

    // ---- DEMON SWORDSMAN (ATK20 DEF10 AGI8 MAN8 SPI4 = 50) ----
    "demon_slash"   to GameCard("","demon_slash","Demon Slash","demon",CardType.ATTACK,1,0,CardEffect(damage=9),"Deal 9 damage.",CardRarity.COMMON,SkillStats(attack=3,agility=1,mana=1)),
    "demon_ward"    to GameCard("","demon_ward","Demonic Ward","demon",CardType.SKILL,1,0,CardEffect(block=7),"Gain 7 Block.",CardRarity.COMMON,SkillStats(defense=2,agility=1,mana=1,spirit=1)),
    "demon_strike"  to GameCard("","demon_strike","Cursed Strike","demon",CardType.ATTACK,2,0,CardEffect(damage=16,loseHp=3),"Deal 16 damage. Lose 3 HP.",CardRarity.COMMON,SkillStats(attack=4,agility=1,spirit=1)),
    "demon_barrier" to GameCard("","demon_barrier","Demon Barrier","demon",CardType.SKILL,2,2,CardEffect(block=12,manaGain=3),"Gain 12 Block. Gain 3 Mana. Cost 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "demon_drain"   to GameCard("","demon_drain","Soul Drain","demon",CardType.ATTACK,1,2,CardEffect(damage=8,heal=4),"Deal 8 damage. Heal 4. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=2,spirit=1)),
    "demon_surge"   to GameCard("","demon_surge","Demon Surge","demon",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "demon_pact"    to GameCard("","demon_pact","Demonic Pact","demon",CardType.SKILL,1,3,CardEffect(draw=3,loseHp=4),"Lose 4 HP. Draw 3. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(agility=2,mana=2,spirit=1)),
    "demon_power"   to GameCard("","demon_power","Demonic Power","demon",CardType.POWER,2,4,CardEffect(strength=3),"Gain 3 Strength. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=4,mana=2,spirit=1)),
    "demon_annihilate" to GameCard("","demon_annihilate","Demon Annihilation","demon",CardType.ATTACK,3,0,CardEffect(damage=24,loseHp=5),"Deal 24 damage. Lose 5 HP.",CardRarity.RARE,SkillStats(attack=5,agility=1,spirit=1)),
    "demon_king"    to GameCard("","demon_king","Demon King Form","demon",CardType.POWER,3,5,CardEffect(strength=4,energyNext=1),"Gain 4 Strength. +1 Energy next turn. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=4,agility=1,mana=2,spirit=1)),

    // ---- BLOOD SWORDSMAN (ATK18 DEF5 AGI10 MAN8 SPI9 = 50) ----
    "blood_slash"  to GameCard("","blood_slash","Blood Slash","blood",CardType.ATTACK,1,0,CardEffect(damage=9,loseHp=1),"Deal 9 damage. Lose 1 HP.",CardRarity.COMMON,SkillStats(attack=3,agility=1,spirit=1)),
    "blood_veil"   to GameCard("","blood_veil","Blood Veil","blood",CardType.SKILL,1,0,CardEffect(block=6,heal=2),"Gain 6 Block. Heal 2 HP.",CardRarity.COMMON,SkillStats(defense=2,spirit=2,agility=1)),
    "blood_strike" to GameCard("","blood_strike","Crimson Strike","blood",CardType.ATTACK,2,0,CardEffect(damage=14,loseHp=3),"Deal 14 damage. Lose 3 HP.",CardRarity.COMMON,SkillStats(attack=4,spirit=2)),
    "blood_ward"   to GameCard("","blood_ward","Blood Ward","blood",CardType.SKILL,2,0,CardEffect(block=10,heal=5),"Gain 10 Block. Heal 5 HP.",CardRarity.COMMON,SkillStats(defense=2,spirit=3)),
    "blood_drain"  to GameCard("","blood_drain","Vampiric Drain","blood",CardType.ATTACK,1,0,CardEffect(damage=7,heal=5),"Deal 7 damage. Heal 5 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=3)),
    "blood_surge"  to GameCard("","blood_surge","Blood Surge","blood",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=2),"Draw 1. Gain 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=2,spirit=1)),
    "blood_pact"   to GameCard("","blood_pact","Blood Pact","blood",CardType.SKILL,1,0,CardEffect(loseHp=5,draw=3,heal=3),"Lose 5 HP. Draw 3. Heal 3.",CardRarity.UNCOMMON,SkillStats(agility=2,spirit=3,mana=1)),
    "blood_power"  to GameCard("","blood_power","Blood Empowerment","blood",CardType.POWER,2,0,CardEffect(strength=2,heal=8),"Gain 2 Strength. Heal 8 HP.",CardRarity.UNCOMMON,SkillStats(attack=2,spirit=4,agility=1)),
    "blood_sacrifice" to GameCard("","blood_sacrifice","Blood Sacrifice","blood",CardType.ATTACK,3,0,CardEffect(damage=20,loseHp=8,heal=12),"Deal 20 damage. Lose 8 HP. Heal 12.",CardRarity.RARE,SkillStats(attack=4,spirit=3,agility=1)),
    "blood_sovereign" to GameCard("","blood_sovereign","Blood Sovereign","blood",CardType.POWER,3,0,CardEffect(strength=3,draw=1,heal=6,energyNext=1),"Gain 3 Strength. Draw 1 extra per turn. Heal 6. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=3,spirit=3,agility=1,mana=1)),

    // ---- SHADOW SWORDSMAN (ATK16 DEF5 AGI18 MAN8 SPI3 = 50) ----
    "shadow_stab"  to GameCard("","shadow_stab","Shadow Stab","shadow",CardType.ATTACK,1,0,CardEffect(damage=8,poison=2),"Deal 8 damage. Apply 2 Poison.",CardRarity.COMMON,SkillStats(attack=3,agility=2)),
    "shadow_veil"  to GameCard("","shadow_veil","Shadow Veil","shadow",CardType.SKILL,1,0,CardEffect(block=5,draw=1),"Gain 5 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=1,agility=3,mana=1)),
    "shadow_strike" to GameCard("","shadow_strike","Assassination","shadow",CardType.ATTACK,2,0,CardEffect(damage=14,poison=4),"Deal 14 damage. Apply 4 Poison.",CardRarity.COMMON,SkillStats(attack=3,agility=2,mana=1)),
    "shadow_cloak" to GameCard("","shadow_cloak","Shadow Cloak","shadow",CardType.SKILL,2,0,CardEffect(block=8,draw=2),"Gain 8 Block. Draw 2.",CardRarity.COMMON,SkillStats(defense=1,agility=4)),
    "shadow_step"  to GameCard("","shadow_step","Shadow Step","shadow",CardType.SKILL,1,0,CardEffect(draw=3),"Draw 3 cards.",CardRarity.COMMON,SkillStats(agility=4,mana=1)),
    "shadow_surge" to GameCard("","shadow_surge","Shadow Surge","shadow",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=3,mana=2)),
    "shadow_mark"  to GameCard("","shadow_mark","Death Mark","shadow",CardType.SKILL,2,0,CardEffect(vulnerable=2,weak=2,poison=3),"Apply 2 Vulnerable, 2 Weak, 3 Poison.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=3,spirit=1)),
    "shadow_power" to GameCard("","shadow_power","Shadow Empowerment","shadow",CardType.POWER,2,0,CardEffect(strength=2,draw=1),"Gain 2 Strength. Draw 1 extra per turn.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=3,spirit=1)),
    "shadow_void"  to GameCard("","shadow_void","Void Execution","shadow",CardType.ATTACK,3,0,CardEffect(damage=18,poison=6),"Deal 18 damage. Apply 6 Poison.",CardRarity.RARE,SkillStats(attack=3,agility=3,mana=1)),
    "shadow_god"   to GameCard("","shadow_god","God of Death","shadow",CardType.POWER,3,0,CardEffect(strength=2,draw=2,poison=4,energyNext=1),"Gain 2 Strength. Draw 2 extra per turn. Apply 4 Poison. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=2,agility=4,mana=1,spirit=1)),

    // ---- LIGHTNING SWORDSMAN (ATK18 DEF5 AGI20 MAN5 SPI2 = 50) ----
    "lightning_slash"   to GameCard("","lightning_slash","Thunder Slash","lightning",CardType.ATTACK,1,0,CardEffect(damage=8,draw=1),"Deal 8 damage. Draw 1.",CardRarity.COMMON,SkillStats(attack=2,agility=3)),
    "lightning_ward"    to GameCard("","lightning_ward","Shock Guard","lightning",CardType.SKILL,1,0,CardEffect(block=5,draw=1),"Gain 5 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=1,agility=3,mana=1)),
    "lightning_strike"  to GameCard("","lightning_strike","Lightning Strike","lightning",CardType.ATTACK,2,0,CardEffect(damage=14,draw=1),"Deal 14 damage. Draw 1.",CardRarity.COMMON,SkillStats(attack=3,agility=3)),
    "lightning_barrier" to GameCard("","lightning_barrier","Lightning Barrier","lightning",CardType.SKILL,2,0,CardEffect(block=10,draw=1),"Gain 10 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=2,agility=3)),
    "lightning_step"    to GameCard("","lightning_step","Thunder Step","lightning",CardType.SKILL,1,0,CardEffect(draw=2),"Draw 2. Move at lightning speed.",CardRarity.COMMON,SkillStats(agility=4,mana=1)),
    "lightning_surge"   to GameCard("","lightning_surge","Lightning Surge","lightning",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=4,spirit=1)),
    "lightning_chain"   to GameCard("","lightning_chain","Chain Lightning","lightning",CardType.ATTACK,2,0,CardEffect(damageAll=8,draw=1),"Deal 8 to all enemies. Draw 1.",CardRarity.UNCOMMON,SkillStats(attack=3,agility=2,mana=1)),
    "lightning_power"   to GameCard("","lightning_power","Lightning Form","lightning",CardType.POWER,2,0,CardEffect(strength=2,draw=1),"Gain 2 Strength. Draw 1 extra per turn.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=3,spirit=1)),
    "lightning_god"     to GameCard("","lightning_god","Thunder God Slash","lightning",CardType.ATTACK,3,0,CardEffect(damage=22,draw=2),"Deal 22 damage. Draw 2.",CardRarity.RARE,SkillStats(attack=4,agility=3,spirit=1)),
    "lightning_transcend" to GameCard("","lightning_transcend","Transcendent Speed","lightning",CardType.POWER,3,0,CardEffect(strength=2,draw=2,energyNext=1),"Gain 2 Strength. Draw 2 extra per turn. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=2,agility=4,mana=1,spirit=1)),

    // ---- WIND SWORDSMAN (ATK12 DEF8 AGI22 MAN5 SPI3 = 50) ----
    "wind_slash"  to GameCard("","wind_slash","Gust Slash","wind",CardType.ATTACK,1,0,CardEffect(damage=7,draw=1),"Deal 7 damage. Draw 1.",CardRarity.COMMON,SkillStats(attack=2,agility=3)),
    "wind_veil"   to GameCard("","wind_veil","Wind Veil","wind",CardType.SKILL,1,0,CardEffect(block=6,draw=1),"Gain 6 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=2,agility=3)),
    "wind_strike" to GameCard("","wind_strike","Wind Blade","wind",CardType.ATTACK,2,0,CardEffect(damage=12,draw=1),"Deal 12 damage. Draw 1.",CardRarity.COMMON,SkillStats(attack=2,agility=3)),
    "wind_ward"   to GameCard("","wind_ward","Wind Ward","wind",CardType.SKILL,2,0,CardEffect(block=10,draw=2),"Gain 10 Block. Draw 2.",CardRarity.COMMON,SkillStats(defense=2,agility=3)),
    "wind_step"   to GameCard("","wind_step","Wind Step","wind",CardType.SKILL,1,0,CardEffect(draw=3),"Draw 3 cards. Ride the wind.",CardRarity.COMMON,SkillStats(agility=4,mana=1)),
    "wind_surge"  to GameCard("","wind_surge","Wind Surge","wind",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=4,spirit=1)),
    "wind_dance"  to GameCard("","wind_dance","Wind Dance","wind",CardType.ATTACK,2,0,CardEffect(damage=8,draw=2,weak=1),"Deal 8 damage. Draw 2. Apply 1 Weak.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=4)),
    "wind_power"  to GameCard("","wind_power","Wind Mastery","wind",CardType.POWER,2,0,CardEffect(strength=1,draw=2),"Gain 1 Strength. Draw 2 extra per turn.",CardRarity.UNCOMMON,SkillStats(attack=1,agility=4,spirit=1)),
    "wind_typhoon" to GameCard("","wind_typhoon","Typhoon Blade","wind",CardType.ATTACK,3,0,CardEffect(damageAll=10,draw=2),"Deal 10 to all enemies. Draw 2.",CardRarity.RARE,SkillStats(attack=2,agility=5,spirit=1)),
    "wind_freedom" to GameCard("","wind_freedom","Free Wind Form","wind",CardType.POWER,3,0,CardEffect(strength=1,draw=3,energyNext=1),"Gain 1 Strength. Draw 3 extra per turn. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=1,agility=5,mana=1,spirit=1)),

    // ---- FLAME SWORDSMAN (ATK22 DEF8 AGI5 MAN10 SPI5 = 50) ----
    "flame_slash"   to GameCard("","flame_slash","Flame Slash","flame",CardType.ATTACK,1,0,CardEffect(damage=8,burn=2),"Deal 8 damage. Apply 2 Burn.",CardRarity.COMMON,SkillStats(attack=3,mana=1,spirit=1)),
    "flame_guard"   to GameCard("","flame_guard","Flame Guard","flame",CardType.SKILL,1,0,CardEffect(block=7,burn=1),"Gain 7 Block. Apply 1 Burn.",CardRarity.COMMON,SkillStats(defense=2,mana=1,spirit=1,attack=1)),
    "flame_strike"  to GameCard("","flame_strike","Inferno Strike","flame",CardType.ATTACK,2,0,CardEffect(damage=16,burn=3),"Deal 16 damage. Apply 3 Burn.",CardRarity.COMMON,SkillStats(attack=4,mana=1,spirit=1)),
    "flame_wall"    to GameCard("","flame_wall","Flame Wall","flame",CardType.SKILL,2,2,CardEffect(block=12,burn=2),"Gain 12 Block. Apply 2 Burn. Cost 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=2,spirit=1)),
    "flame_ignite"  to GameCard("","flame_ignite","Ignite","flame",CardType.SKILL,1,0,CardEffect(burn=6),"Apply 6 Burn to enemy.",CardRarity.COMMON,SkillStats(attack=2,mana=2,spirit=1)),
    "flame_surge"   to GameCard("","flame_surge","Flame Surge","flame",CardType.SKILL,0,0,CardEffect(draw=1,burn=1),"Draw 1. Apply 1 Burn.",CardRarity.COMMON,SkillStats(agility=1,mana=2,spirit=1,attack=1)),
    "flame_inferno" to GameCard("","flame_inferno","Inferno","flame",CardType.ATTACK,3,0,CardEffect(damageAll=10,burn=4),"Deal 10 to all enemies. Apply 4 Burn to all.",CardRarity.UNCOMMON,SkillStats(attack=4,mana=2,spirit=1)),
    "flame_power"   to GameCard("","flame_power","Flame Empowerment","flame",CardType.POWER,2,3,CardEffect(strength=3,burn=2),"Gain 3 Strength. Apply 2 Burn. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(attack=4,mana=2,spirit=1)),
    "flame_god"     to GameCard("","flame_god","Flame God Slash","flame",CardType.ATTACK,3,0,CardEffect(damage=24,burn=5),"Deal 24 damage. Apply 5 Burn.",CardRarity.RARE,SkillStats(attack=5,mana=2,spirit=1)),
    "flame_phoenix" to GameCard("","flame_phoenix","Phoenix Form","flame",CardType.POWER,3,4,CardEffect(strength=3,heal=10,energyNext=1),"Gain 3 Strength. Heal 10 HP. +1 Energy next turn. Cost 4 Mana.",CardRarity.RARE,SkillStats(attack=4,mana=2,spirit=2,defense=1)),

    // ---- ICE SWORDSMAN (ATK12 DEF20 AGI5 MAN10 SPI3 = 50) ----
    "ice_slash"   to GameCard("","ice_slash","Ice Slash","ice",CardType.ATTACK,1,0,CardEffect(damage=8,freeze=1),"Deal 8 damage. Apply 1 Freeze.",CardRarity.COMMON,SkillStats(attack=2,defense=1,mana=1,spirit=1)),
    "ice_ward"    to GameCard("","ice_ward","Ice Ward","ice",CardType.SKILL,1,0,CardEffect(block=9),"Gain 9 Block.",CardRarity.COMMON,SkillStats(defense=3,mana=1,agility=1)),
    "ice_strike"  to GameCard("","ice_strike","Glacial Strike","ice",CardType.ATTACK,2,0,CardEffect(damage=12,freeze=2),"Deal 12 damage. Apply 2 Freeze (enemy loses 2 Agility stacks).",CardRarity.COMMON,SkillStats(attack=2,defense=1,mana=2,spirit=1)),
    "ice_barrier" to GameCard("","ice_barrier","Ice Barrier","ice",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,mana=1)),
    "ice_freeze"  to GameCard("","ice_freeze","Deep Freeze","ice",CardType.SKILL,1,2,CardEffect(vulnerable=2,weak=2,freeze=2),"Apply 2 Vulnerable, 2 Weak, 2 Freeze. Cost 2 Mana.",CardRarity.COMMON,SkillStats(defense=1,attack=1,mana=2,spirit=1)),
    "ice_surge"   to GameCard("","ice_surge","Ice Surge","ice",CardType.SKILL,0,0,CardEffect(block=4,manaGain=2),"Gain 4 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "ice_storm"   to GameCard("","ice_storm","Blizzard Blade","ice",CardType.ATTACK,3,0,CardEffect(damageAll=8,freeze=3,block=6),"Deal 8 to all. Apply 3 Freeze. Gain 6 Block.",CardRarity.UNCOMMON,SkillStats(attack=2,defense=3,mana=2)),
    "ice_power"   to GameCard("","ice_power","Glacier Form","ice",CardType.POWER,2,3,CardEffect(strength=1,block=8,manaGain=3),"Gain 1 Strength. Gain 8 Block. Gain 3 Mana. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(defense=3,mana=2,attack=1,spirit=1)),
    "ice_fortress" to GameCard("","ice_fortress","Ice Fortress","ice",CardType.SKILL,3,0,CardEffect(block=22,freeze=2),"Gain 22 Block. Apply 2 Freeze.",CardRarity.RARE,SkillStats(defense=5,mana=2,spirit=1)),
    "ice_sovereign" to GameCard("","ice_sovereign","Ice Sovereign","ice",CardType.POWER,3,4,CardEffect(strength=2,block=10,energyNext=1,manaGain=4),"Gain 2 Strength. Gain 10 Block. +1 Energy next turn. Gain 4 Mana. Cost 4 Mana.",CardRarity.RARE,SkillStats(attack=2,defense=4,mana=2,spirit=1)),

    // ---- VOID SWORDSMAN (ATK15 DEF10 AGI8 MAN15 SPI2 = 50) ----
    "void_slash"  to GameCard("","void_slash","Void Slash","void",CardType.ATTACK,1,2,CardEffect(damage=11),"Deal 11 damage (ignores half block). Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "void_veil"   to GameCard("","void_veil","Void Veil","void",CardType.SKILL,1,0,CardEffect(block=8,manaGain=2),"Gain 8 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "void_strike" to GameCard("","void_strike","Void Strike","void",CardType.ATTACK,2,3,CardEffect(damage=16),"Deal 16 damage. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=3,mana=3)),
    "void_ward"   to GameCard("","void_ward","Void Ward","void",CardType.SKILL,2,0,CardEffect(block=12,manaGain=3),"Gain 12 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=3,mana=3)),
    "void_step"   to GameCard("","void_step","Void Step","void",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=2),"Draw 2. Gain 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "void_surge"  to GameCard("","void_surge","Void Surge","void",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=4)),
    "void_rift"   to GameCard("","void_rift","Void Rift","void",CardType.ATTACK,2,4,CardEffect(damage=14,vulnerable=2),"Deal 14 damage. Apply 2 Vulnerable. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=3,spirit=1)),
    "void_power"  to GameCard("","void_power","Void Empowerment","void",CardType.POWER,2,4,CardEffect(strength=2,manaGain=4),"Gain 2 Strength. Gain 4 Mana. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=4,spirit=1)),
    "void_collapse" to GameCard("","void_collapse","Void Collapse","void",CardType.ATTACK,3,6,CardEffect(damageAll=14),"Deal 14 to all enemies. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=4,mana=4)),
    "void_eternal" to GameCard("","void_eternal","Eternal Void","void",CardType.POWER,3,8,CardEffect(strength=3,draw=1,energyNext=1,manaGain=6),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 6 Mana. Cost 8 Mana.",CardRarity.RARE,SkillStats(attack=3,agility=2,mana=4,spirit=1)),

    // ---- TIME SWORDSMAN (ATK12 DEF12 AGI10 MAN12 SPI4 = 50) ----
    "time_slash"   to GameCard("","time_slash","Timestep Slash","time",CardType.ATTACK,1,0,CardEffect(damage=8,draw=1),"Deal 8 damage. Draw 1 (time compressed).",CardRarity.COMMON,SkillStats(attack=2,agility=2,mana=1)),
    "time_ward"    to GameCard("","time_ward","Temporal Ward","time",CardType.SKILL,1,0,CardEffect(block=7,manaGain=2),"Gain 7 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "time_strike"  to GameCard("","time_strike","Time Strike","time",CardType.ATTACK,2,3,CardEffect(damage=14,vulnerable=1,draw=1),"Deal 14 damage. Apply 1 Vulnerable. Draw 1. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,agility=2,mana=2)),
    "time_barrier" to GameCard("","time_barrier","Time Barrier","time",CardType.SKILL,2,0,CardEffect(block=11,manaGain=3),"Gain 11 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "time_slow"    to GameCard("","time_slow","Time Slow","time",CardType.SKILL,1,3,CardEffect(weak=3,vulnerable=2),"Apply 3 Weak and 2 Vulnerable. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=1,agility=2,mana=3)),
    "time_surge"   to GameCard("","time_surge","Time Surge","time",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3,energyNext=1),"Draw 1. Gain 3 Mana. +1 Energy next turn.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "time_rewind"  to GameCard("","time_rewind","Time Rewind","time",CardType.SKILL,2,4,CardEffect(heal=12,draw=2),"Heal 12 HP. Draw 2. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(defense=2,agility=2,mana=3,spirit=1)),
    "time_power"   to GameCard("","time_power","Temporal Mastery","time",CardType.POWER,2,4,CardEffect(strength=2,manaGain=4,draw=1),"Gain 2 Strength. Gain 4 Mana. Draw 1 extra per turn. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=2,mana=3)),
    "time_paradox" to GameCard("","time_paradox","Time Paradox","time",CardType.ATTACK,3,5,CardEffect(damage=20,draw=2,energyNext=1),"Deal 20 damage. Draw 2. +1 Energy next turn. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=3,agility=2,mana=3)),
    "time_god"     to GameCard("","time_god","Chronos Form","time",CardType.POWER,3,6,CardEffect(strength=3,draw=1,manaGain=5,energyNext=1),"Gain 3 Strength. Draw 1 extra per turn. Gain 5 Mana. +1 Energy next turn. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=2,mana=4,spirit=1)),

    // ---- SPACE SWORDSMAN (ATK15 DEF8 AGI12 MAN12 SPI3 = 50) ----
    "space_slash"   to GameCard("","space_slash","Space Cut","space",CardType.ATTACK,1,2,CardEffect(damage=10),"Deal 10 damage anywhere. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "space_ward"    to GameCard("","space_ward","Space Ward","space",CardType.SKILL,1,0,CardEffect(block=7,manaGain=2),"Gain 7 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "space_strike"  to GameCard("","space_strike","Dimensional Slash","space",CardType.ATTACK,2,3,CardEffect(damage=14,draw=1),"Deal 14 damage. Draw 1. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,agility=2,mana=2)),
    "space_barrier" to GameCard("","space_barrier","Space Barrier","space",CardType.SKILL,2,0,CardEffect(block=11,manaGain=3),"Gain 11 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "space_step"    to GameCard("","space_step","Space Step","space",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=2),"Draw 2. Gain 2 Mana. Teleport.",CardRarity.COMMON,SkillStats(agility=3,mana=2)),
    "space_surge"   to GameCard("","space_surge","Space Surge","space",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "space_rift"    to GameCard("","space_rift","Space Rift","space",CardType.ATTACK,2,4,CardEffect(damageAll=10),"Deal 10 to all enemies through spatial rifts. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=3,agility=1)),
    "space_power"   to GameCard("","space_power","Spatial Mastery","space",CardType.POWER,2,4,CardEffect(strength=2,manaGain=4),"Gain 2 Strength. Gain 4 Mana. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=2,mana=3)),
    "space_collapse" to GameCard("","space_collapse","Space Collapse","space",CardType.ATTACK,3,5,CardEffect(damage=22,vulnerable=2),"Deal 22 damage. Apply 2 Vulnerable. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=4,mana=4,agility=1)),
    "space_god"      to GameCard("","space_god","Space God Form","space",CardType.POWER,3,7,CardEffect(strength=3,draw=1,energyNext=1,manaGain=5),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 5 Mana. Cost 7 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=2,mana=4,spirit=1)),

    // ---- DIMENSIONAL SWORDSMAN (ATK15 DEF10 AGI10 MAN12 SPI3 = 50) ----
    "dim_slash"   to GameCard("","dim_slash","Dimensional Slash","dimensional",CardType.ATTACK,1,2,CardEffect(damage=10),"Deal 10 damage across dimensions. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "dim_ward"    to GameCard("","dim_ward","Dimensional Ward","dimensional",CardType.SKILL,1,0,CardEffect(block=8,manaGain=2),"Gain 8 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "dim_strike"  to GameCard("","dim_strike","Cross-Dimension Strike","dimensional",CardType.ATTACK,2,3,CardEffect(damage=14,draw=1),"Deal 14 damage. Draw 1. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,agility=2,mana=2)),
    "dim_barrier" to GameCard("","dim_barrier","Dimensional Barrier","dimensional",CardType.SKILL,2,0,CardEffect(block=11,manaGain=3),"Gain 11 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=3,mana=3)),
    "dim_step"    to GameCard("","dim_step","Dimensional Step","dimensional",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=2),"Draw 2. Gain 2 Mana. Shift dimensions.",CardRarity.COMMON,SkillStats(agility=3,mana=2)),
    "dim_surge"   to GameCard("","dim_surge","Dimensional Surge","dimensional",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "dim_cross"   to GameCard("","dim_cross","Cross-Dimensional Strike","dimensional",CardType.ATTACK,2,3,CardEffect(damageAll=9),"Deal 9 to all enemies simultaneously. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=3,agility=1)),
    "dim_power"   to GameCard("","dim_power","Dimensional Power","dimensional",CardType.POWER,2,4,CardEffect(strength=2,manaGain=4),"Gain 2 Strength. Gain 4 Mana. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,agility=2,mana=3)),
    "dim_collapse" to GameCard("","dim_collapse","Dimensional Collapse","dimensional",CardType.ATTACK,3,6,CardEffect(damageAll=13,vulnerable=2),"Deal 13 to all. Apply 2 Vulnerable. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=4,agility=1,spirit=1)),
    "dim_god"     to GameCard("","dim_god","Dimensional God","dimensional",CardType.POWER,3,7,CardEffect(strength=3,draw=1,energyNext=1,manaGain=5),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 5 Mana. Cost 7 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=2,mana=4,spirit=1)),

    // ---- CELESTIAL SWORDSMAN (ATK12 DEF12 AGI8 MAN8 SPI10 = 50) ----
    "cel_slash"    to GameCard("","cel_slash","Starlight Slash","celestial",CardType.ATTACK,1,0,CardEffect(damage=8,heal=2),"Deal 8 damage. Heal 2 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=3)),
    "cel_ward"     to GameCard("","cel_ward","Celestial Ward","celestial",CardType.SKILL,1,0,CardEffect(block=8),"Gain 8 Block. Blessed by the stars.",CardRarity.COMMON,SkillStats(defense=3,spirit=2)),
    "cel_strike"   to GameCard("","cel_strike","Heaven Strike","celestial",CardType.ATTACK,2,0,CardEffect(damage=13,heal=4),"Deal 13 damage. Heal 4 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=4)),
    "cel_barrier"  to GameCard("","cel_barrier","Celestial Barrier","celestial",CardType.SKILL,2,0,CardEffect(block=12,heal=3),"Gain 12 Block. Heal 3 HP.",CardRarity.COMMON,SkillStats(defense=3,spirit=3)),
    "cel_light"    to GameCard("","cel_light","Celestial Light","celestial",CardType.SKILL,1,0,CardEffect(heal=8,draw=1),"Heal 8 HP. Draw 1.",CardRarity.COMMON,SkillStats(spirit=4,agility=1,mana=1)),
    "cel_surge"    to GameCard("","cel_surge","Celestial Surge","celestial",CardType.SKILL,0,0,CardEffect(draw=1,heal=3,manaGain=2),"Draw 1. Heal 3. Gain 2 Mana.",CardRarity.COMMON,SkillStats(spirit=3,agility=1,mana=1)),
    "cel_radiance" to GameCard("","cel_radiance","Star Radiance","celestial",CardType.ATTACK,2,0,CardEffect(damage=11,heal=6,weak=1),"Deal 11 damage. Heal 6. Apply 1 Weak.",CardRarity.UNCOMMON,SkillStats(attack=2,spirit=4)),
    "cel_power"    to GameCard("","cel_power","Celestial Empowerment","celestial",CardType.POWER,2,0,CardEffect(strength=2,heal=8),"Gain 2 Strength. Heal 8 HP permanently.",CardRarity.UNCOMMON,SkillStats(attack=1,spirit=5)),
    "cel_nova"     to GameCard("","cel_nova","Celestial Nova","celestial",CardType.ATTACK,3,0,CardEffect(damageAll=10,heal=10),"Deal 10 to all enemies. Heal 10 HP.",CardRarity.RARE,SkillStats(attack=2,spirit=5,agility=1)),
    "cel_divinity" to GameCard("","cel_divinity","Star Divinity","celestial",CardType.POWER,3,0,CardEffect(strength=2,draw=1,heal=12,energyNext=1),"Gain 2 Strength. Draw 1 extra per turn. Heal 12. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=1,spirit=5,agility=1,mana=1)),

    // ---- CHAOS SWORDSMAN (ATK20 DEF5 AGI12 MAN10 SPI3 = 50) ----
    "chaos_slash"   to GameCard("","chaos_slash","Chaos Slash","chaos",CardType.ATTACK,1,0,CardEffect(damage=9),"Deal 9 damage. Chaotic edge.",CardRarity.COMMON,SkillStats(attack=3,agility=1,mana=1)),
    "chaos_ward"    to GameCard("","chaos_ward","Chaos Ward","chaos",CardType.SKILL,1,0,CardEffect(block=5,draw=1),"Gain 5 Block. Draw 1.",CardRarity.COMMON,SkillStats(defense=1,agility=2,mana=1,spirit=1)),
    "chaos_strike"  to GameCard("","chaos_strike","Chaos Strike","chaos",CardType.ATTACK,2,0,CardEffect(damage=15,loseHp=2),"Deal 15 damage. Lose 2 HP.",CardRarity.COMMON,SkillStats(attack=4,agility=1,spirit=1)),
    "chaos_barrier" to GameCard("","chaos_barrier","Chaos Barrier","chaos",CardType.SKILL,2,2,CardEffect(block=10,manaGain=3),"Gain 10 Block. Gain 3 Mana. Cost 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "chaos_wild"    to GameCard("","chaos_wild","Wild Surge","chaos",CardType.SKILL,1,0,CardEffect(draw=3),"Draw 3 cards chaotically.",CardRarity.COMMON,SkillStats(agility=4,mana=1)),
    "chaos_surge"   to GameCard("","chaos_surge","Chaos Surge","chaos",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "chaos_unravel" to GameCard("","chaos_unravel","Unravel","chaos",CardType.ATTACK,2,0,CardEffect(damage=12,vulnerable=2,weak=2),"Deal 12 damage. Apply 2 Vulnerable and 2 Weak.",CardRarity.UNCOMMON,SkillStats(attack=3,agility=2,mana=1)),
    "chaos_power"   to GameCard("","chaos_power","Chaos Empowerment","chaos",CardType.POWER,2,3,CardEffect(strength=3),"Gain 3 Strength permanently. Cost 3 Mana.",CardRarity.UNCOMMON,SkillStats(attack=4,mana=2,spirit=1)),
    "chaos_eruption" to GameCard("","chaos_eruption","Chaos Eruption","chaos",CardType.ATTACK,3,0,CardEffect(damageAll=11,vulnerable=2),"Deal 11 to all. Apply 2 Vulnerable to all.",CardRarity.RARE,SkillStats(attack=4,agility=2,spirit=1)),
    "chaos_god"     to GameCard("","chaos_god","God of Chaos","chaos",CardType.POWER,3,5,CardEffect(strength=4,draw=2,energyNext=1),"Gain 4 Strength. Draw 2 extra per turn. +1 Energy next turn. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=4,agility=2,mana=2,spirit=1)),

    // ---- COSMIC SWORDSMAN (ATK14 DEF10 AGI8 MAN14 SPI4 = 50) ----
    "cosmic_slash"    to GameCard("","cosmic_slash","Cosmic Slash","cosmic",CardType.ATTACK,1,2,CardEffect(damage=10),"Deal 10 damage with cosmic force. Cost 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "cosmic_ward"     to GameCard("","cosmic_ward","Cosmic Ward","cosmic",CardType.SKILL,1,0,CardEffect(block=7,manaGain=2),"Gain 7 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "cosmic_strike"   to GameCard("","cosmic_strike","Galaxy Strike","cosmic",CardType.ATTACK,2,3,CardEffect(damage=13,vulnerable=1),"Deal 13 damage. Apply 1 Vulnerable. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3,spirit=1)),
    "cosmic_barrier"  to GameCard("","cosmic_barrier","Cosmic Barrier","cosmic",CardType.SKILL,2,0,CardEffect(block=11,manaGain=3),"Gain 11 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=3,mana=3)),
    "cosmic_pull"     to GameCard("","cosmic_pull","Gravity Pull","cosmic",CardType.SKILL,1,2,CardEffect(draw=2,weak=1),"Draw 2. Apply 1 Weak. Cost 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=3)),
    "cosmic_surge"    to GameCard("","cosmic_surge","Cosmic Surge","cosmic",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=4),"Draw 1. Gain 4 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=4)),
    "cosmic_collapse" to GameCard("","cosmic_collapse","Cosmic Collapse","cosmic",CardType.ATTACK,2,4,CardEffect(damageAll=10),"Deal 10 to all enemies. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=3,mana=3,agility=1)),
    "cosmic_power"    to GameCard("","cosmic_power","Cosmic Empowerment","cosmic",CardType.POWER,2,4,CardEffect(strength=2,manaGain=5),"Gain 2 Strength. Gain 5 Mana. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,mana=4,spirit=1)),
    "cosmic_singularity" to GameCard("","cosmic_singularity","Singularity Blade","cosmic",CardType.ATTACK,3,6,CardEffect(damage=22,vulnerable=2),"Deal 22 damage. Apply 2 Vulnerable. Cost 6 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=4,spirit=1)),
    "cosmic_god"      to GameCard("","cosmic_god","Cosmic God Form","cosmic",CardType.POWER,3,8,CardEffect(strength=3,draw=1,energyNext=1,manaGain=6),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 6 Mana. Cost 8 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=2,mana=4,spirit=2)),

    // ---- IMMORTAL SWORDSMAN (ATK10 DEF18 AGI8 MAN8 SPI6 = 50) ----
    "immortal_slash"   to GameCard("","immortal_slash","Eternal Slash","immortal",CardType.ATTACK,1,0,CardEffect(damage=8,heal=2),"Deal 8 damage. Heal 2 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=2,defense=1)),
    "immortal_ward"    to GameCard("","immortal_ward","Immortal Ward","immortal",CardType.SKILL,1,0,CardEffect(block=9),"Gain 9 Block.",CardRarity.COMMON,SkillStats(defense=3,spirit=2)),
    "immortal_strike"  to GameCard("","immortal_strike","Timeless Strike","immortal",CardType.ATTACK,2,0,CardEffect(damage=12,heal=4),"Deal 12 damage. Heal 4 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=3,defense=1)),
    "immortal_barrier" to GameCard("","immortal_barrier","Immortal Barrier","immortal",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,spirit=1)),
    "immortal_endure"  to GameCard("","immortal_endure","Undying Resolve","immortal",CardType.SKILL,1,0,CardEffect(heal=8,block=4),"Heal 8 HP. Gain 4 Block.",CardRarity.COMMON,SkillStats(defense=2,spirit=3,mana=1)),
    "immortal_surge"   to GameCard("","immortal_surge","Immortal Surge","immortal",CardType.SKILL,0,0,CardEffect(heal=4,manaGain=2),"Heal 4 HP. Gain 2 Mana.",CardRarity.COMMON,SkillStats(spirit=2,mana=2,agility=1)),
    "immortal_rebirth" to GameCard("","immortal_rebirth","Phoenix Rebirth","immortal",CardType.SKILL,2,0,CardEffect(heal=16,draw=1),"Heal 16 HP. Draw 1.",CardRarity.UNCOMMON,SkillStats(defense=2,spirit=4,agility=1)),
    "immortal_power"   to GameCard("","immortal_power","Eternal Power","immortal",CardType.POWER,2,0,CardEffect(strength=2,heal=8),"Gain 2 Strength. Heal 8 HP.",CardRarity.UNCOMMON,SkillStats(attack=1,spirit=4,agility=1,defense=1)),
    "immortal_fortress" to GameCard("","immortal_fortress","Immortal Fortress","immortal",CardType.SKILL,3,0,CardEffect(block=22,heal=8),"Gain 22 Block. Heal 8 HP.",CardRarity.RARE,SkillStats(defense=5,spirit=3)),
    "immortal_ascension" to GameCard("","immortal_ascension","Immortal Ascension","immortal",CardType.POWER,3,0,CardEffect(strength=2,heal=10,draw=1,energyNext=1),"Gain 2 Strength. Heal 10 HP. Draw 1 extra per turn. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=1,defense=3,spirit=3,mana=1,agility=1)),

    // ---- DAO SWORD CULTIVATOR (ATK14 DEF12 AGI8 MAN12 SPI4 = 50) ----
    "dao_slash"    to GameCard("","dao_slash","Dao Edge","dao",CardType.ATTACK,1,0,CardEffect(damage=9,manaGain=2),"Deal 9 damage. Gain 2 Mana.",CardRarity.COMMON,SkillStats(attack=2,mana=3)),
    "dao_ward"     to GameCard("","dao_ward","Dao Ward","dao",CardType.SKILL,1,0,CardEffect(block=8,manaGain=2),"Gain 8 Block. Gain 2 Mana.",CardRarity.COMMON,SkillStats(defense=2,mana=3)),
    "dao_strike"   to GameCard("","dao_strike","Dao Strike","dao",CardType.ATTACK,2,3,CardEffect(damage=14),"Deal 14 damage. Dao-empowered. Cost 3 Mana.",CardRarity.COMMON,SkillStats(attack=3,mana=3)),
    "dao_barrier"  to GameCard("","dao_barrier","Dao Barrier","dao",CardType.SKILL,2,0,CardEffect(block=12,manaGain=3),"Gain 12 Block. Gain 3 Mana.",CardRarity.COMMON,SkillStats(defense=3,mana=3)),
    "dao_insight"  to GameCard("","dao_insight","Dao Insight","dao",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=3),"Draw 2. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=2,mana=4)),
    "dao_surge"    to GameCard("","dao_surge","Dao Surge","dao",CardType.SKILL,0,0,CardEffect(draw=1,manaGain=3),"Draw 1. Gain 3 Mana.",CardRarity.COMMON,SkillStats(agility=1,mana=4)),
    "dao_harmony"  to GameCard("","dao_harmony","Dao Harmony","dao",CardType.SKILL,2,4,CardEffect(heal=10,block=8,draw=1),"Heal 10 HP. Gain 8 Block. Draw 1. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(defense=2,spirit=2,agility=2,mana=2)),
    "dao_power"    to GameCard("","dao_power","Dao Empowerment","dao",CardType.POWER,2,4,CardEffect(strength=2,manaGain=5),"Gain 2 Strength. Gain 5 Mana. Cost 4 Mana.",CardRarity.UNCOMMON,SkillStats(attack=2,mana=4,spirit=2)),
    "dao_absolute" to GameCard("","dao_absolute","Absolute Dao","dao",CardType.ATTACK,3,5,CardEffect(damage=20,vulnerable=2,draw=1),"Deal 20 damage. Apply 2 Vulnerable. Draw 1. Cost 5 Mana.",CardRarity.RARE,SkillStats(attack=3,mana=4,agility=1,spirit=1)),
    "dao_enlighten" to GameCard("","dao_enlighten","Enlightenment","dao",CardType.POWER,3,7,CardEffect(strength=3,draw=1,energyNext=1,manaGain=6),"Gain 3 Strength. Draw 1 extra per turn. +1 Energy next turn. Gain 6 Mana. Cost 7 Mana.",CardRarity.RARE,SkillStats(attack=2,agility=2,mana=4,spirit=2)),

    // ---- SWORD SAGE (ATK12 DEF12 AGI8 MAN10 SPI8 = 50) ----
    "sage_slash"    to GameCard("","sage_slash","Sage's Edge","sage",CardType.ATTACK,1,0,CardEffect(damage=9,heal=2),"Deal 9 damage. Heal 2 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=2,defense=1)),
    "sage_ward"     to GameCard("","sage_ward","Sage's Ward","sage",CardType.SKILL,1,0,CardEffect(block=8,manaGain=1),"Gain 8 Block. Gain 1 Mana.",CardRarity.COMMON,SkillStats(defense=2,spirit=2,mana=1)),
    "sage_strike"   to GameCard("","sage_strike","Sage's Strike","sage",CardType.ATTACK,2,0,CardEffect(damage=13,heal=3),"Deal 13 damage. Heal 3 HP.",CardRarity.COMMON,SkillStats(attack=2,spirit=3,defense=1)),
    "sage_barrier"  to GameCard("","sage_barrier","Sage's Barrier","sage",CardType.SKILL,2,0,CardEffect(block=11,heal=4),"Gain 11 Block. Heal 4 HP.",CardRarity.COMMON,SkillStats(defense=3,spirit=2)),
    "sage_insight"  to GameCard("","sage_insight","Enlightened Insight","sage",CardType.SKILL,1,0,CardEffect(draw=2,manaGain=2),"Draw 2. Gain 2 Mana.",CardRarity.COMMON,SkillStats(agility=2,spirit=2,mana=1)),
    "sage_surge"    to GameCard("","sage_surge","Sage's Surge","sage",CardType.SKILL,0,0,CardEffect(draw=1,heal=3,manaGain=1),"Draw 1. Heal 3. Gain 1 Mana.",CardRarity.COMMON,SkillStats(agility=1,spirit=2,mana=1,defense=1)),
    "sage_enlighten" to GameCard("","sage_enlighten","Sage's Enlightenment","sage",CardType.SKILL,2,0,CardEffect(heal=12,draw=1,block=6),"Heal 12 HP. Draw 1. Gain 6 Block.",CardRarity.UNCOMMON,SkillStats(defense=2,spirit=3,agility=1,mana=1)),
    "sage_power"    to GameCard("","sage_power","Sage's Power","sage",CardType.POWER,2,0,CardEffect(strength=2,heal=6,draw=1),"Gain 2 Strength. Heal 6 HP. Draw 1 extra per turn.",CardRarity.UNCOMMON,SkillStats(attack=1,spirit=3,agility=2,mana=1)),
    "sage_transcend" to GameCard("","sage_transcend","Sage's Transcendence","sage",CardType.ATTACK,3,0,CardEffect(damage=18,heal=8,draw=1),"Deal 18 damage. Heal 8 HP. Draw 1.",CardRarity.RARE,SkillStats(attack=2,spirit=4,agility=1,mana=1,defense=1)),
    "sage_divinity"  to GameCard("","sage_divinity","Sword Sage Divinity","sage",CardType.POWER,3,0,CardEffect(strength=2,heal=10,draw=1,energyNext=1),"Gain 2 Strength. Heal 10 HP. Draw 1 extra per turn. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=1,spirit=4,agility=2,mana=1,defense=1)),

    // ---- SWORD MONARCH (ATK18 DEF15 AGI8 MAN5 SPI4 = 50) ----
    "monarch_slash"   to GameCard("","monarch_slash","Monarch Slash","monarch",CardType.ATTACK,1,0,CardEffect(damage=10),"Deal 10 damage. Royal authority.",CardRarity.COMMON,SkillStats(attack=3,defense=1,spirit=1)),
    "monarch_ward"    to GameCard("","monarch_ward","Monarch Ward","monarch",CardType.SKILL,1,0,CardEffect(block=9),"Gain 9 Block.",CardRarity.COMMON,SkillStats(defense=3,spirit=2)),
    "monarch_strike"  to GameCard("","monarch_strike","Monarch Strike","monarch",CardType.ATTACK,2,0,CardEffect(damage=17),"Deal 17 damage.",CardRarity.COMMON,SkillStats(attack=4,defense=1)),
    "monarch_barrier" to GameCard("","monarch_barrier","Monarch Barrier","monarch",CardType.SKILL,2,0,CardEffect(block=14),"Gain 14 Block.",CardRarity.COMMON,SkillStats(defense=4,spirit=1)),
    "monarch_decree"  to GameCard("","monarch_decree","Royal Decree","monarch",CardType.SKILL,1,0,CardEffect(weak=3,vulnerable=2),"Apply 3 Weak and 2 Vulnerable to enemy.",CardRarity.COMMON,SkillStats(attack=2,defense=1,agility=1,spirit=1)),
    "monarch_surge"   to GameCard("","monarch_surge","Monarch Surge","monarch",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=2,mana=2,defense=1)),
    "monarch_domain"  to GameCard("","monarch_domain","King's Domain","monarch",CardType.SKILL,2,0,CardEffect(block=12,weak=2),"Gain 12 Block. Apply 2 Weak.",CardRarity.UNCOMMON,SkillStats(defense=3,attack=2,spirit=1)),
    "monarch_power"   to GameCard("","monarch_power","Monarch's Power","monarch",CardType.POWER,2,0,CardEffect(strength=3),"Gain 3 Strength permanently.",CardRarity.UNCOMMON,SkillStats(attack=4,spirit=2)),
    "monarch_dominion" to GameCard("","monarch_dominion","Royal Dominion","monarch",CardType.ATTACK,3,0,CardEffect(damageAll=13,weak=2),"Deal 13 to all enemies. Apply 2 Weak to all.",CardRarity.RARE,SkillStats(attack=4,defense=1,agility=1,spirit=1)),
    "monarch_reign"   to GameCard("","monarch_reign","Eternal Reign","monarch",CardType.POWER,3,0,CardEffect(strength=4,energyNext=1),"Gain 4 Strength. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=4,defense=2,agility=1,spirit=1)),

    // ---- SWORD KING (ATK20 DEF15 AGI8 MAN5 SPI2 = 50) ----
    "king_slash"    to GameCard("","king_slash","King Slash","king",CardType.ATTACK,1,0,CardEffect(damage=11),"Deal 11 damage. The King's blow.",CardRarity.COMMON,SkillStats(attack=4,defense=1)),
    "king_ward"     to GameCard("","king_ward","King's Ward","king",CardType.SKILL,1,0,CardEffect(block=9),"Gain 9 Block.",CardRarity.COMMON,SkillStats(defense=3,spirit=1,agility=1)),
    "king_strike"   to GameCard("","king_strike","King's Strike","king",CardType.ATTACK,2,0,CardEffect(damage=18),"Deal 18 damage.",CardRarity.COMMON,SkillStats(attack=5,defense=1)),
    "king_barrier"  to GameCard("","king_barrier","King's Barrier","king",CardType.SKILL,2,0,CardEffect(block=15),"Gain 15 Block.",CardRarity.COMMON,SkillStats(defense=4,spirit=1)),
    "king_dominion" to GameCard("","king_dominion","King's Dominion","king",CardType.SKILL,1,0,CardEffect(weak=2,vulnerable=2,draw=1),"Apply 2 Weak, 2 Vulnerable. Draw 1.",CardRarity.COMMON,SkillStats(attack=2,agility=2,defense=1)),
    "king_surge"    to GameCard("","king_surge","King's Surge","king",CardType.SKILL,0,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON,SkillStats(agility=2,defense=1,mana=2)),
    "king_conquest" to GameCard("","king_conquest","King's Conquest","king",CardType.ATTACK,2,0,CardEffect(damage=14,vulnerable=2),"Deal 14 damage. Apply 2 Vulnerable.",CardRarity.UNCOMMON,SkillStats(attack=4,defense=1,spirit=1)),
    "king_power"    to GameCard("","king_power","King's Power","king",CardType.POWER,2,0,CardEffect(strength=3),"Gain 3 Strength permanently.",CardRarity.UNCOMMON,SkillStats(attack=5,spirit=1)),
    "king_judgment" to GameCard("","king_judgment","King's Judgment","king",CardType.ATTACK,3,0,CardEffect(damageAll=14,vulnerable=2),"Deal 14 to all enemies. Apply 2 Vulnerable to all.",CardRarity.RARE,SkillStats(attack=5,defense=2,agility=1)),
    "king_supremacy" to GameCard("","king_supremacy","Supreme Reign","king",CardType.POWER,3,0,CardEffect(strength=5,energyNext=1),"Gain 5 Strength. +1 Energy next turn.",CardRarity.RARE,SkillStats(attack=5,defense=2,agility=1,spirit=1))
)

fun makeCard(templateId: String): GameCard {
    val t = CARD_TEMPLATES[templateId] ?: error("Unknown card: $templateId")
    return t.copy(id = makeCardId())
}

fun buildStartingDeck(cultId: CultId): List<GameCard> =
    (CULT_START_CARDS[cultId] ?: emptyList()).map { makeCard(it) }

// =====================
// ENEMIES (Swordsmanship-themed)
// =====================

val ENEMIES: Map<String, EnemyTemplate> = mapOf(
    "bandit" to EnemyTemplate("bandit","Sword Bandit",28, listOf(
        EnemyMoveData("attack",6,"Quick Slash"),
        EnemyMoveData("block",4,"Defensive Stance"),
        EnemyMoveData("attack",9,"Reckless Strike")
    ), false),
    "dojo_student" to EnemyTemplate("dojo_student","Dojo Student",24, listOf(
        EnemyMoveData("attack",5,"Basic Slash"),
        EnemyMoveData("attack",8,"Lunge"),
        EnemyMoveData("buff",0,"Focus", EffectType.STRENGTH, 1)
    ), false),
    "iron_guard" to EnemyTemplate("iron_guard","Iron Guard",40, listOf(
        EnemyMoveData("attack",7,"Guard Strike"),
        EnemyMoveData("block",8,"Iron Wall"),
        EnemyMoveData("attack",10,"Heavy Blow")
    ), false),
    "mercenary" to EnemyTemplate("mercenary","Blade Mercenary",38, listOf(
        EnemyMoveData("attack",8,"Mercenary Slash"),
        EnemyMoveData("attack",7,"Opportunistic Strike", EffectType.VULNERABLE, 1),
        EnemyMoveData("buff",0,"Battle Hardened", EffectType.STRENGTH, 2)
    ), false),
    "rival_swordsman" to EnemyTemplate("rival_swordsman","Rival Swordsman",55, listOf(
        EnemyMoveData("attack",10,"Rival's Slash"),
        EnemyMoveData("block",8,"Guard Stance"),
        EnemyMoveData("attack",9,"Keen Strike", EffectType.WEAK, 1),
        EnemyMoveData("attack",14,"Signature Move")
    ), false),
    "elite_duelist" to EnemyTemplate("elite_duelist","Elite Duelist",70, listOf(
        EnemyMoveData("attack",12,"Precision Thrust"),
        EnemyMoveData("block",10,"Expert Parry"),
        EnemyMoveData("attack",10,"Flurry", EffectType.VULNERABLE, 1),
        EnemyMoveData("buff",0,"Battle Focus", EffectType.STRENGTH, 2)
    ), false),
    "shadow_assassin" to EnemyTemplate("shadow_assassin","Shadow Assassin",46, listOf(
        EnemyMoveData("attack",11,"Poison Strike", EffectType.POISON, 3),
        EnemyMoveData("attack",8,"Shadow Stab"),
        EnemyMoveData("buff",0,"Vanish", EffectType.STRENGTH, 2)
    ), false),
    "war_general" to EnemyTemplate("war_general","War General",82, listOf(
        EnemyMoveData("attack",13,"Commander's Slash"),
        EnemyMoveData("block",12,"Battle Guard"),
        EnemyMoveData("attack",11,"War Cry", EffectType.VULNERABLE, 2),
        EnemyMoveData("buff",0,"Rally Troops", EffectType.STRENGTH, 3)
    ), false),
    "sword_champion" to EnemyTemplate("sword_champion","Sword Champion",65, listOf(
        EnemyMoveData("attack",12,"Champion Strike"),
        EnemyMoveData("block",10,"Champion's Guard"),
        EnemyMoveData("attack",16,"Power Slash")
    ), false),
    "dragon_warrior" to EnemyTemplate("dragon_warrior","Dragon Warrior",90, listOf(
        EnemyMoveData("attack",14,"Dragon Fang"),
        EnemyMoveData("attack",11,"Flame Blade", EffectType.BURN, 3),
        EnemyMoveData("block",12,"Dragon Scale"),
        EnemyMoveData("buff",0,"Dragon's Rage", EffectType.STRENGTH, 3)
    ), false),
    "phantom_blade" to EnemyTemplate("phantom_blade","Phantom Blade",52, listOf(
        EnemyMoveData("attack",10,"Ghost Strike"),
        EnemyMoveData("attack",9,"Ethereal Slash", EffectType.WEAK, 2),
        EnemyMoveData("buff",0,"Phase", EffectType.STRENGTH, 2)
    ), false),
    "cursed_knight" to EnemyTemplate("cursed_knight","Cursed Knight",75, listOf(
        EnemyMoveData("attack",13,"Curse Slash"),
        EnemyMoveData("block",9,"Cursed Barrier"),
        EnemyMoveData("attack",11,"Soul Drain", EffectType.VULNERABLE, 2),
        EnemyMoveData("attack",17,"Cursed Execution")
    ), false),
    "sword_warlord" to EnemyTemplate("sword_warlord","Sword Warlord",130, listOf(
        EnemyMoveData("attack",14,"Warlord Slash"),
        EnemyMoveData("block",14,"Iron Fortress"),
        EnemyMoveData("attack",18,"Battle Surge", EffectType.VULNERABLE, 2),
        EnemyMoveData("buff",0,"Warlord's Will", EffectType.STRENGTH, 4)
    ), true),
    "fallen_king" to EnemyTemplate("fallen_king","The Fallen Sword King",170, listOf(
        EnemyMoveData("attack",16,"Kingsword Strike"),
        EnemyMoveData("block",18,"Royal Guard"),
        EnemyMoveData("attack",14,"Sovereign Slash", EffectType.VULNERABLE, 3),
        EnemyMoveData("buff",0,"King's Wrath", EffectType.STRENGTH, 4),
        EnemyMoveData("attack",24,"Execution Blow")
    ), true),
    "demon_sword_god" to EnemyTemplate("demon_sword_god","Demon Sword God",220, listOf(
        EnemyMoveData("attack",18,"Demon God Slash"),
        EnemyMoveData("block",20,"Demon Fortress"),
        EnemyMoveData("attack",16,"Cursed Barrage", EffectType.VULNERABLE, 3),
        EnemyMoveData("buff",0,"Ascended Rage", EffectType.STRENGTH, 5),
        EnemyMoveData("attack",28,"Demon God's Judgment"),
        EnemyMoveData("attack",14,"Soul Rend", EffectType.WEAK, 3)
    ), true)
)

// =====================
// STORY ACTS (Swordsmanship-themed)
// =====================

val STORY_ACTS: List<StoryAct> = listOf(
    StoryAct(1, "The Proving Grounds",
        "Your journey begins at the proving grounds where every swordsman earns their name. Duel your way through rivals and prove your blade is worthy.",
        listOf(
            StoryFloor(1, "battle", listOf("bandit"), false),
            StoryFloor(2, "battle", listOf("dojo_student", "dojo_student"), false),
            StoryFloor(3, "event", emptyList(), false,
                eventText = "A wise elder offers to teach you a forgotten sword technique. The knowledge carries a price — your blood must mark the contract.",
                eventReward = "relic"),
            StoryFloor(4, "battle", listOf("iron_guard", "mercenary"), false),
            StoryFloor(5, "boss", listOf("sword_warlord"), true,
                dialogue = listOf(
                    StoryDialogue("Sword Warlord", "Another hopeful challenger. How many years have I watched them fall?"),
                    StoryDialogue("Sword Warlord", "Your eyes burn with ambition. But ambition without mastery is just an open wound."),
                    StoryDialogue("You", "Then I'll show you mastery forged in every battle I've survived.")
                ))
        )
    ),
    StoryAct(2, "The Battlefield",
        "The proving grounds are behind you. Now you march to war-torn lands where veterans of a hundred battles sharpen their blades on each other.",
        listOf(
            StoryFloor(1, "battle", listOf("rival_swordsman"), false),
            StoryFloor(2, "battle", listOf("elite_duelist"), false),
            StoryFloor(3, "battle", listOf("shadow_assassin", "phantom_blade"), false),
            StoryFloor(4, "event", emptyList(), false,
                eventText = "A dying soldier holds a legendary blade. He offers it to whoever can prove worthy — but warns it carries a curse.",
                eventReward = "card"),
            StoryFloor(5, "boss", listOf("fallen_king"), true,
                dialogue = listOf(
                    StoryDialogue("The Fallen King", "I once held the title of Sword King. Then this curse took everything."),
                    StoryDialogue("The Fallen King", "If you defeat me, perhaps the curse will pass to you. Are you prepared for that price?"),
                    StoryDialogue("You", "I'll bear any price. Come — let's decide who deserves that title.")
                ))
        )
    ),
    StoryAct(3, "The Pinnacle",
        "At the summit of your journey stands the Demon Sword God — a being beyond mortal rank. Defeat them to claim the title of Sword King.",
        listOf(
            StoryFloor(1, "battle", listOf("dragon_warrior"), false),
            StoryFloor(2, "battle", listOf("cursed_knight"), false),
            StoryFloor(3, "battle", listOf("war_general", "sword_champion"), false),
            StoryFloor(4, "battle", listOf("sword_champion"), false),
            StoryFloor(5, "boss", listOf("demon_sword_god"), true,
                dialogue = listOf(
                    StoryDialogue("Demon Sword God", "I am the end of all swords. Every blade eventually breaks before me."),
                    StoryDialogue("Demon Sword God", "You carry the weight of every swordsman you've defeated. That weight will crush you."),
                    StoryDialogue("You", "No — it fuels me. Every sword I've faced has made mine sharper. Today, yours is next.")
                ))
        )
    )
)

fun getStoryAct(actId: Int): StoryAct? = STORY_ACTS.find { it.id == actId }
fun getStoryFloor(actId: Int, floorNum: Int): StoryFloor? = getStoryAct(actId)?.floors?.find { it.floor == floorNum }

// =====================
// RELICS (Swordsmanship-themed)
// =====================

val ALL_RELICS: List<Relic> = listOf(
    Relic("blade_oilstone",   "Blade Oilstone",   "Start each battle with 3 Block.",                          "battle_start", blockBonus = 3),
    Relic("warrior_bandage",  "Warrior's Bandage","Heal 6 HP after every battle.",                            "post_battle",  healAfterBattle = 6),
    Relic("spirit_crystal",   "Spirit Crystal",   "Gain 1 extra Energy each turn.",                           "turn_start",   energyBonus = 1),
    Relic("shadow_cloak_r",   "Shadow Cloak",     "First attack each battle deals double damage.",            "on_attack",    firstAttackDouble = true),
    Relic("sword_manual",     "Ancient Sword Manual","Draw 1 extra card each turn.",                          "turn_start",   extraDraw = 1),
    Relic("victory_coin",     "Victory Coin",     "Gain 15 Gold after each battle.",                          "post_battle",  goldAfterBattle = 15),
    Relic("dragon_core",      "Dragon Core",      "Permanently gain 10 Max HP.",                              "passive",      extraMaxHp = 10),
    Relic("edge_talisman",    "Edge Talisman",    "All attacks deal 2 extra damage.",                         "on_attack",    attackBonus = 2),
    Relic("mana_prism",       "Mana Prism",       "Start each battle with 10 extra Mana.",                    "battle_start", manaBonus = 10),
    Relic("dao_scroll",       "Dao Scroll",       "Gain 1 extra Energy and 5 Mana each turn.",               "turn_start",   energyBonus = 1, manaBonus = 5)
)

fun getRandomRelic(ownedIds: List<String>): Relic {
    val available = ALL_RELICS.filter { it.id !in ownedIds }
    return if (available.isEmpty()) ALL_RELICS[0] else available.random()
}

// =====================
// QUESTS (Swordsmanship-themed)
// =====================

val INITIAL_QUESTS: List<Quest> = listOf(
    Quest("first_blood",     "First Blood",       "Win your first duel.",                                 1,  "common_card",   0,  "battlesWon",         false),
    Quest("iron_will",       "Iron Will",         "Win a battle with 5 HP or less.",                     1,  "uncommon_card", 0,  "lowHpWins",          false),
    Quest("executioner",     "Executioner",       "Defeat 10 total enemies.",                             10, "gold",          50, "totalEnemiesKilled", false),
    Quest("blade_collector", "Blade Collector",   "Have 15+ cards in your deck.",                        15, "relic",         0,  "deckSize",           false),
    Quest("pure_force",      "Pure Force",        "Win 3 battles using only Attack cards.",               3,  "rare_card",     0,  "attackOnlyWins",     false),
    Quest("sword_novice",    "Sword Novice",      "Complete Act 1.",                                      1,  "uncommon_card", 0,  "actsCompleted",      false),
    Quest("blood_debt",      "Blood Debt",        "Deal 500 total damage across all battles.",            500,"rare_card",     0,  "totalDamageDealt",   false),
    Quest("endurance",       "Endurance",         "Survive 20 turns total in battle.",                   20, "uncommon_card", 0,  "totalTurns",         false)
)

// =====================
// REWARD HELPERS
// =====================

fun getRewardCards(cultId: CultId, count: Int = 3): List<GameCard> {
    val cultName = cultId.name.lowercase()
    val pool = CARD_TEMPLATES.values.filter { t ->
        (t.cult == cultName || t.cult == "neutral") && t.rarity != CardRarity.STARTER
    }
    return pool.shuffled().take(count).map { t -> t.copy(id = makeCardId()) }
}

fun getCultColor(cult: String): Long {
    return CultId.entries.find { it.name.lowercase() == cult }?.color ?: 0xFF888888
}

fun getRarityColor(rarity: CardRarity): Long = when (rarity) {
    CardRarity.STARTER   -> 0xFF666666
    CardRarity.COMMON    -> 0xFFAAAAAA
    CardRarity.UNCOMMON  -> 0xFF3498DB
    CardRarity.RARE      -> 0xFFF1C40F
}
