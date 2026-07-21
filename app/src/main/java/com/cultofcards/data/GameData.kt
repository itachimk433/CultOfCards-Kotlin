package com.cultofcards.data

import kotlin.random.Random

// =====================
// ENUMS & TYPES
// =====================

enum class CultId(val displayName: String, val subtitle: String, val description: String, val color: Long, val icon: String) {
    BONE("Bone Cult", "Masters of the Undead",
        "Command skeletal armies and raise bone shields. Each kill feeds your dark power.",
        0xFFD4C5A9, "💀"),
    FIRE("Fire Cult", "Bringers of the Pyre",
        "Ignite enemies and watch them burn turn after turn. Burn never fades.",
        0xFFE67E22, "🔥"),
    SHADOW("Shadow Cult", "Whisperers of the Void",
        "Strike from darkness, poison your foes, and draw power from the abyss.",
        0xFF9B59B6, "👁"),
    ARCANE("Arcane Cult", "Wielders of Forbidden Magic",
        "Bend reality with colossal spells. Massive damage and energy manipulation.",
        0xFF2980B9, "✦")
}

enum class CardType { ATTACK, SKILL, POWER }
enum class CardRarity { STARTER, COMMON, UNCOMMON, RARE }
enum class EffectType { POISON, BURN, WEAK, VULNERABLE, STRENGTH }

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
    val weak: Int = 0,
    val vulnerable: Int = 0,
    val strength: Int = 0,
    val energyNext: Int = 0,
    val loseHp: Int = 0
)

data class GameCard(
    val id: String,
    val templateId: String,
    val name: String,
    val cult: String, // cult id or "neutral"
    val type: CardType,
    val cost: Int,
    val effects: CardEffect,
    val description: String,
    val rarity: CardRarity
)

data class EffectStack(val type: EffectType, val stacks: Int)

data class EnemyMoveData(
    val type: String, // "attack", "block", "buff"
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
    val goldAfterBattle: Int = 0
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
    val type: String, // "battle", "event", "boss"
    val enemyIds: List<String>,
    val isBoss: Boolean,
    val dialogue: List<StoryDialogue> = emptyList(),
    val eventText: String = "",
    val eventReward: String = "" // "relic", "card", "heal"
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
// CULT STARTING DECKS
// =====================

val CULT_START_CARDS: Map<CultId, List<String>> = mapOf(
    CultId.BONE to listOf("strike","strike","strike","strike","defend","defend","defend","defend","bone_strike","bone_wall"),
    CultId.FIRE to listOf("strike","strike","strike","strike","defend","defend","defend","defend","flame_slash","ignite"),
    CultId.SHADOW to listOf("strike","strike","strike","strike","defend","defend","defend","defend","shadow_strike","vanish"),
    CultId.ARCANE to listOf("strike","strike","strike","strike","defend","defend","defend","defend","arcane_bolt","mana_shield")
)

// =====================
// CARD TEMPLATES
// =====================

val CARD_TEMPLATES: Map<String, GameCard> = mapOf(
    // NEUTRAL STARTER
    "strike" to GameCard("","strike","Strike","neutral",CardType.ATTACK,1,CardEffect(damage=6),"Deal 6 damage.",CardRarity.STARTER),
    "defend" to GameCard("","defend","Defend","neutral",CardType.SKILL,1,CardEffect(block=5),"Gain 5 Block.",CardRarity.STARTER),

    // BONE CULT
    "bone_strike" to GameCard("","bone_strike","Bone Strike","bone",CardType.ATTACK,1,CardEffect(damage=8),"Deal 8 damage.",CardRarity.COMMON),
    "bone_wall" to GameCard("","bone_wall","Bone Wall","bone",CardType.SKILL,1,CardEffect(block=8),"Gain 8 Block.",CardRarity.COMMON),
    "soul_drain" to GameCard("","soul_drain","Soul Drain","bone",CardType.ATTACK,2,CardEffect(damage=5,draw=1),"Deal 5 damage. Draw 1 card.",CardRarity.COMMON),
    "ossify" to GameCard("","ossify","Ossify","bone",CardType.SKILL,1,CardEffect(block=4,vulnerable=1),"Gain 4 Block. Apply 1 Vulnerable.",CardRarity.COMMON),
    "revenant_grasp" to GameCard("","revenant_grasp","Revenant's Grasp","bone",CardType.ATTACK,2,CardEffect(damage=14),"Deal 14 damage.",CardRarity.UNCOMMON),
    "grave_robber" to GameCard("","grave_robber","Grave Robber","bone",CardType.SKILL,1,CardEffect(draw=3),"Draw 3 cards.",CardRarity.UNCOMMON),
    "skeletal_army" to GameCard("","skeletal_army","Skeletal Army","bone",CardType.POWER,2,CardEffect(strength=2),"Gain 2 Strength permanently.",CardRarity.UNCOMMON),
    "deaths_embrace" to GameCard("","deaths_embrace","Death's Embrace","bone",CardType.SKILL,2,CardEffect(block=16),"Gain 16 Block.",CardRarity.RARE),
    "lich_form" to GameCard("","lich_form","Lich Form","bone",CardType.POWER,3,CardEffect(strength=3,energyNext=1),"Gain 3 Strength. +1 Energy each turn.",CardRarity.RARE),

    // FIRE CULT
    "flame_slash" to GameCard("","flame_slash","Flame Slash","fire",CardType.ATTACK,1,CardEffect(damage=7),"Deal 7 damage.",CardRarity.COMMON),
    "ignite" to GameCard("","ignite","Ignite","fire",CardType.SKILL,1,CardEffect(burn=4),"Apply 4 Burn.",CardRarity.COMMON),
    "cinder_wall" to GameCard("","cinder_wall","Cinder Wall","fire",CardType.SKILL,1,CardEffect(block=5,burn=1),"Gain 5 Block. Apply 1 Burn.",CardRarity.COMMON),
    "fireball" to GameCard("","fireball","Fireball","fire",CardType.ATTACK,2,CardEffect(damage=12),"Deal 12 damage.",CardRarity.COMMON),
    "immolation" to GameCard("","immolation","Immolation","fire",CardType.ATTACK,2,CardEffect(damage=10,loseHp=2),"Deal 10 damage. Lose 2 HP.",CardRarity.UNCOMMON),
    "inferno" to GameCard("","inferno","Inferno","fire",CardType.ATTACK,3,CardEffect(damage=20),"Deal 20 damage.",CardRarity.UNCOMMON),
    "ember_step" to GameCard("","ember_step","Ember Step","fire",CardType.SKILL,0,CardEffect(draw=1,burn=1),"Draw 1 card. Apply 1 Burn.",CardRarity.UNCOMMON),
    "wildfire" to GameCard("","wildfire","Wildfire","fire",CardType.POWER,2,CardEffect(burn=3,strength=1),"Apply 3 Burn. Gain 1 Strength.",CardRarity.UNCOMMON),
    "phoenix_ash" to GameCard("","phoenix_ash","Phoenix Ash","fire",CardType.SKILL,2,CardEffect(heal=12),"Heal 12 HP.",CardRarity.RARE),
    "volcanic_fury" to GameCard("","volcanic_fury","Volcanic Fury","fire",CardType.ATTACK,3,CardEffect(damageAll=8,burn=2),"Deal 8 to all enemies. Apply 2 Burn to all.",CardRarity.RARE),

    // SHADOW CULT
    "shadow_strike" to GameCard("","shadow_strike","Shadow Strike","shadow",CardType.ATTACK,1,CardEffect(damage=7),"Deal 7 damage.",CardRarity.COMMON),
    "vanish" to GameCard("","vanish","Vanish","shadow",CardType.SKILL,1,CardEffect(block=4,draw=1),"Gain 4 Block. Draw 1 card.",CardRarity.COMMON),
    "poison_dart" to GameCard("","poison_dart","Poison Dart","shadow",CardType.SKILL,1,CardEffect(poison=5),"Apply 5 Poison.",CardRarity.COMMON),
    "dark_pact" to GameCard("","dark_pact","Dark Pact","shadow",CardType.SKILL,1,CardEffect(loseHp=4,draw=3),"Lose 4 HP. Draw 3 cards.",CardRarity.COMMON),
    "death_mark" to GameCard("","death_mark","Death Mark","shadow",CardType.SKILL,2,CardEffect(vulnerable=2,weak=2),"Apply 2 Vulnerable and 2 Weak.",CardRarity.UNCOMMON),
    "eclipse" to GameCard("","eclipse","Eclipse","shadow",CardType.ATTACK,3,CardEffect(damage=15,poison=5),"Deal 15 damage. Apply 5 Poison.",CardRarity.UNCOMMON),
    "miasma" to GameCard("","miasma","Miasma","shadow",CardType.ATTACK,2,CardEffect(damage=6,poison=3),"Deal 6 damage. Apply 3 Poison.",CardRarity.UNCOMMON),
    "night_shroud" to GameCard("","night_shroud","Night Shroud","shadow",CardType.SKILL,2,CardEffect(block=10),"Gain 10 Block.",CardRarity.UNCOMMON),
    "thousand_cuts" to GameCard("","thousand_cuts","Thousand Cuts","shadow",CardType.POWER,2,CardEffect(strength=1,draw=1),"Gain 1 Strength. Draw 1 extra each turn.",CardRarity.RARE),
    "void_pulse" to GameCard("","void_pulse","Void Pulse","shadow",CardType.ATTACK,2,CardEffect(damage=8,poison=4,draw=1),"Deal 8. Apply 4 Poison. Draw 1.",CardRarity.RARE),

    // ARCANE CULT
    "arcane_bolt" to GameCard("","arcane_bolt","Arcane Bolt","arcane",CardType.ATTACK,1,CardEffect(damage=8),"Deal 8 damage.",CardRarity.COMMON),
    "mana_shield" to GameCard("","mana_shield","Mana Shield","arcane",CardType.SKILL,1,CardEffect(block=7),"Gain 7 Block.",CardRarity.COMMON),
    "arcane_surge" to GameCard("","arcane_surge","Arcane Surge","arcane",CardType.SKILL,0,CardEffect(draw=2),"Draw 2 cards.",CardRarity.COMMON),
    "runic_barrier" to GameCard("","runic_barrier","Runic Barrier","arcane",CardType.SKILL,1,CardEffect(block=5,energyNext=2),"Gain 5 Block. Gain 2 Energy next turn.",CardRarity.COMMON),
    "arcane_armor" to GameCard("","arcane_armor","Arcane Armor","arcane",CardType.SKILL,2,CardEffect(block=14),"Gain 14 Block.",CardRarity.UNCOMMON),
    "force_wave" to GameCard("","force_wave","Force Wave","arcane",CardType.ATTACK,2,CardEffect(damageAll=10),"Deal 10 damage to all enemies.",CardRarity.UNCOMMON),
    "transmute" to GameCard("","transmute","Transmute","arcane",CardType.SKILL,1,CardEffect(draw=1,energyNext=1),"Draw 1. Gain 1 Energy next turn.",CardRarity.UNCOMMON),
    "mind_control" to GameCard("","mind_control","Mind Control","arcane",CardType.SKILL,2,CardEffect(weak=3,vulnerable=2),"Apply 3 Weak and 2 Vulnerable.",CardRarity.UNCOMMON),
    "meteor" to GameCard("","meteor","Meteor","arcane",CardType.ATTACK,3,CardEffect(damage=28),"Deal 28 damage.",CardRarity.RARE),
    "spell_echo" to GameCard("","spell_echo","Spell Echo","arcane",CardType.POWER,3,CardEffect(strength=2,draw=1),"Gain 2 Strength. Draw 1 extra each turn.",CardRarity.RARE)
)

fun makeCard(templateId: String): GameCard {
    val t = CARD_TEMPLATES[templateId] ?: error("Unknown card: $templateId")
    return t.copy(id = makeCardId())
}

fun buildStartingDeck(cultId: CultId): List<GameCard> =
    (CULT_START_CARDS[cultId] ?: emptyList()).map { makeCard(it) }

// =====================
// ENEMIES
// =====================

val ENEMIES: Map<String, EnemyTemplate> = mapOf(
    "skeleton_warrior" to EnemyTemplate("skeleton_warrior","Skeleton Warrior",32, listOf(
        EnemyMoveData("attack",7,"Bone Slash"),
        EnemyMoveData("block",5,"Braces"),
        EnemyMoveData("attack",10,"Heavy Strike")
    ), false),
    "bone_archer" to EnemyTemplate("bone_archer","Bone Archer",26, listOf(
        EnemyMoveData("attack",6,"Arrow Shot"),
        EnemyMoveData("attack",9,"Volley"),
        EnemyMoveData("buff",0,"Aims", EffectType.STRENGTH, 1)
    ), false),
    "zombie" to EnemyTemplate("zombie","Zombie",38, listOf(
        EnemyMoveData("attack",7,"Shambles"),
        EnemyMoveData("attack",7,"Bites"),
        EnemyMoveData("attack",11,"Overwhelms")
    ), false),
    "cryptkeeper" to EnemyTemplate("cryptkeeper","Cryptkeeper",58, listOf(
        EnemyMoveData("attack",10,"Crypt Slash"),
        EnemyMoveData("block",8,"Bone Armor"),
        EnemyMoveData("attack",7,"Curse", EffectType.WEAK, 1),
        EnemyMoveData("attack",15,"Death Strike")
    ), false),
    "lich_lord" to EnemyTemplate("lich_lord","Lich Lord",120, listOf(
        EnemyMoveData("attack",12,"Soul Strike"),
        EnemyMoveData("block",12,"Bone Shield"),
        EnemyMoveData("attack",18,"Death Wave", EffectType.VULNERABLE, 2),
        EnemyMoveData("buff",0,"Unholy Rage", EffectType.STRENGTH, 3)
    ), true),
    "fire_imp" to EnemyTemplate("fire_imp","Fire Imp",36, listOf(
        EnemyMoveData("attack",6,"Flame Claw", EffectType.BURN, 2),
        EnemyMoveData("attack",9,"Fire Bolt"),
        EnemyMoveData("buff",0,"Ignites", EffectType.STRENGTH, 1)
    ), false),
    "lava_golem" to EnemyTemplate("lava_golem","Lava Golem",68, listOf(
        EnemyMoveData("attack",14,"Magma Fist"),
        EnemyMoveData("block",10,"Lava Skin"),
        EnemyMoveData("attack",11,"Eruption", EffectType.BURN, 3)
    ), false),
    "hellhound" to EnemyTemplate("hellhound","Hellhound",32, listOf(
        EnemyMoveData("attack",9,"Savage Bite"),
        EnemyMoveData("attack",7,"Howl", EffectType.WEAK, 1),
        EnemyMoveData("attack",13,"Pounce")
    ), false),
    "infernal_knight" to EnemyTemplate("infernal_knight","Infernal Knight",78, listOf(
        EnemyMoveData("attack",13,"Hellblade"),
        EnemyMoveData("block",12,"Infernal Guard"),
        EnemyMoveData("attack",11,"Flame Lance", EffectType.BURN, 2),
        EnemyMoveData("buff",0,"Rallies", EffectType.STRENGTH, 2)
    ), false),
    "demon_overlord" to EnemyTemplate("demon_overlord","Demon Overlord",155, listOf(
        EnemyMoveData("attack",15,"Hellfire Strike"),
        EnemyMoveData("attack",10,"Inferno Whip", EffectType.BURN, 4),
        EnemyMoveData("block",16,"Demonic Shield"),
        EnemyMoveData("buff",0,"Demonic Fury", EffectType.STRENGTH, 4),
        EnemyMoveData("attack",22,"Hellgate")
    ), true),
    "shadow_wraith" to EnemyTemplate("shadow_wraith","Shadow Wraith",46, listOf(
        EnemyMoveData("attack",10,"Phantasm Strike"),
        EnemyMoveData("attack",8,"Life Drain", EffectType.WEAK, 2),
        EnemyMoveData("buff",0,"Ethereal Form", EffectType.STRENGTH, 2)
    ), false),
    "void_stalker" to EnemyTemplate("void_stalker","Void Stalker",58, listOf(
        EnemyMoveData("attack",12,"Void Claw"),
        EnemyMoveData("attack",9,"Silence", EffectType.VULNERABLE, 2),
        EnemyMoveData("block",9,"Phase Shift"),
        EnemyMoveData("attack",16,"Annihilation")
    ), false),
    "dark_cultist" to EnemyTemplate("dark_cultist","Dark Cultist",52, listOf(
        EnemyMoveData("attack",10,"Ritual Stab"),
        EnemyMoveData("buff",0,"Dark Prayer", EffectType.STRENGTH, 2),
        EnemyMoveData("attack",13,"Soul Rend", EffectType.VULNERABLE, 1)
    ), false),
    "nightmare" to EnemyTemplate("nightmare","Nightmare",82, listOf(
        EnemyMoveData("attack",14,"Terror Strike"),
        EnemyMoveData("attack",11,"Haunting", EffectType.WEAK, 2),
        EnemyMoveData("block",11,"Shadow Veil"),
        EnemyMoveData("attack",19,"Nightmare Surge")
    ), false),
    "dark_god" to EnemyTemplate("dark_god","The Dark God",200, listOf(
        EnemyMoveData("attack",18,"Divine Smite"),
        EnemyMoveData("block",20,"Godly Ward"),
        EnemyMoveData("attack",15,"Void Beam", EffectType.VULNERABLE, 3),
        EnemyMoveData("buff",0,"Ascension", EffectType.STRENGTH, 5),
        EnemyMoveData("attack",26,"Apocalypse"),
        EnemyMoveData("attack",13,"Entropy Wave", EffectType.WEAK, 3)
    ), true)
)

// =====================
// STORY ACTS
// =====================

val STORY_ACTS: List<StoryAct> = listOf(
    StoryAct(1,"The Catacombs",
        "The Church of Light hunts you for your forbidden rituals. Your only refuge lies in the depths — the ancient catacombs where the dead still serve.",
        listOf(
            StoryFloor(1,"battle", listOf("skeleton_warrior"), false),
            StoryFloor(2,"battle", listOf("bone_archer","zombie"), false),
            StoryFloor(3,"event", emptyList(), false,
                eventText="A crumbling altar radiates dark power. Touching it fills you with forbidden strength — but at what cost?",
                eventReward="relic"),
            StoryFloor(4,"battle", listOf("cryptkeeper"), false),
            StoryFloor(5,"boss", listOf("lich_lord"), true,
                dialogue= listOf(
                    StoryDialogue("Lich Lord","You dare enter my domain, cultist?"),
                    StoryDialogue("Lich Lord","I have outlived empires. Your bones will join my collection."),
                    StoryDialogue("You","Then let me add mine to the pile.")
                ))
        )
    ),
    StoryAct(2,"The Infernal Wastes",
        "The catacombs give way to smoldering wastes. The warmth here is not comfort — it is malice. The Demon Overlord feasts on the weak.",
        listOf(
            StoryFloor(1,"battle", listOf("fire_imp"), false),
            StoryFloor(2,"battle", listOf("lava_golem"), false),
            StoryFloor(3,"battle", listOf("hellhound","hellhound"), false),
            StoryFloor(4,"event", emptyList(), false,
                eventText="A wounded demon offers you forbidden fire power in exchange for mercy. Take the deal?",
                eventReward="card"),
            StoryFloor(5,"boss", listOf("demon_overlord"), true,
                dialogue= listOf(
                    StoryDialogue("Demon Overlord","Another mortal crawls into my realm."),
                    StoryDialogue("Demon Overlord","I'll roast your soul slowly. Scream for me."),
                    StoryDialogue("You","Your fire is cold compared to my fury.")
                ))
        )
    ),
    StoryAct(3,"The Shadow Realm",
        "Beyond the wastes lies the Shadow Realm. Even demons fear this place. The Dark God slumbers at its core — but not for long.",
        listOf(
            StoryFloor(1,"battle", listOf("shadow_wraith"), false),
            StoryFloor(2,"battle", listOf("void_stalker"), false),
            StoryFloor(3,"battle", listOf("dark_cultist","shadow_wraith"), false),
            StoryFloor(4,"battle", listOf("nightmare"), false),
            StoryFloor(5,"boss", listOf("dark_god"), true,
                dialogue= listOf(
                    StoryDialogue("The Dark God","I am the end of all cults. All roads lead to me."),
                    StoryDialogue("The Dark God","Surrender your cards, and I will make your death quick."),
                    StoryDialogue("You","My cult did not come this far to kneel.")
                ))
        )
    )
)

fun getStoryAct(actId: Int): StoryAct? = STORY_ACTS.find { it.id == actId }
fun getStoryFloor(actId: Int, floorNum: Int): StoryFloor? = getStoryAct(actId)?.floors?.find { it.floor == floorNum }

// =====================
// RELICS
// =====================

val ALL_RELICS: List<Relic> = listOf(
    Relic("skull_charm","Skull Charm","Start each battle with 3 Block.","battle_start", blockBonus=3),
    Relic("blood_vial","Blood Vial","Heal 6 HP after every battle.","post_battle", healAfterBattle=6),
    Relic("bone_dice","Bone Dice","Gain 1 extra Energy each turn.","turn_start", energyBonus=1),
    Relic("shadow_cloak","Shadow Cloak","First attack each battle deals double damage.","on_attack", firstAttackDouble=true),
    Relic("tome_of_secrets","Tome of Secrets","Draw 1 extra card each turn.","turn_start", extraDraw=1),
    Relic("dark_idol","Dark Idol","Gain 15 Gold after each battle.","post_battle", goldAfterBattle=15),
    Relic("arcane_crystal","Arcane Crystal","Permanently gain 10 Max HP.","passive", extraMaxHp=10),
    Relic("fire_gem","Fire Gem","All attacks deal 2 extra damage.","on_attack", attackBonus=2)
)

fun getRandomRelic(ownedIds: List<String>): Relic {
    val available = ALL_RELICS.filter { it.id !in ownedIds }
    return if (available.isEmpty()) ALL_RELICS[0] else available.random()
}

// =====================
// QUESTS
// =====================

val INITIAL_QUESTS: List<Quest> = listOf(
    Quest("first_blood","First Blood","Win your first battle.",1,"common_card",0,"battlesWon",false),
    Quest("iron_will","Iron Will","Win a battle with 5 HP or less.",1,"uncommon_card",0,"lowHpWins",false),
    Quest("executioner","Executioner","Defeat 10 total enemies.",10,"gold",50,"totalEnemiesKilled",false),
    Quest("collector","Collector","Have 15+ cards in your deck.",15,"relic",0,"deckSize",false),
    Quest("brute","Brute Force","Win 3 battles using only Attack cards.",3,"rare_card",0,"attackOnlyWins",false)
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
    return when (cult) {
        "bone" -> 0xFFD4C5A9
        "fire" -> 0xFFE67E22
        "shadow" -> 0xFF9B59B6
        "arcane" -> 0xFF2980B9
        else -> 0xFF888888
    }
}

fun getRarityColor(rarity: CardRarity): Long = when (rarity) {
    CardRarity.STARTER -> 0xFF666666
    CardRarity.COMMON -> 0xFFAAAAAA
    CardRarity.UNCOMMON -> 0xFF3498DB
    CardRarity.RARE -> 0xFFF1C40F
}
