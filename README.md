# Cult of Cards — Android

A **Slay the Spire**-style deck-building roguelite, rebuilt natively in **Kotlin + Jetpack Compose**. No Unity, no React Native — pure Android.

## Gameplay

- **4 Cults** to choose from: Bone, Fire, Shadow, Arcane — each with a unique starting deck and play style
- **Turn-based card combat**: spend energy to play Attack, Skill, and Power cards
- **Status effects**: Poison, Burn, Weak, Vulnerable, Strength
- **3-Act campaign**: 15 floors with regular battles, story events, and boss fights
- **Relics**: passive items that change your run permanently
- **Quests**: cross-run progression goals with card/relic rewards
- **Persistent save**: your run is saved automatically

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| State | ViewModel + StateFlow |
| Persistence | SharedPreferences + Gson |
| Build | Gradle 8.9 + KTS |
| Min SDK | Android 8.0 (API 26) |
| Target SDK | Android 15 (API 35) |

## Building

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17

### Run
```bash
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

Or open the project in Android Studio and click **Run**.

### CI
Every push to `main` triggers a GitHub Actions build that uploads both Debug and Release APKs as artifacts.

## Project Structure

```
app/src/main/java/com/cultofcards/
├── data/
│   ├── GameData.kt        # All cards, enemies, relics, story acts
│   └── GameRepository.kt  # Save/load via SharedPreferences
├── viewmodel/
│   ├── GameViewModel.kt   # Campaign state (run, quests, progress)
│   └── BattleViewModel.kt # Battle turn logic
├── ui/
│   ├── theme/             # Colors, typography, Material3 theme
│   ├── navigation/        # NavGraph
│   ├── screens/           # All game screens (Compose)
│   └── components/        # CardView, EnemyView
└── MainActivity.kt
```

## Screens

| Screen | Description |
|--------|-------------|
| Main Menu | Start/continue run, access quests |
| Cult Select | Pick one of 4 cults to start a run |
| Campaign | Floor-by-floor map of the current act |
| Story | Boss pre-battle dialogue |
| Battle | Full card combat |
| Reward | Pick a card to add to your deck |
| Relic Reward | Boss reward — permanent relic |
| Deck Viewer | Browse your current deck |
| Quests | Track cross-run goals |
| Game Over | Death screen |
| Victory | Campaign complete |
