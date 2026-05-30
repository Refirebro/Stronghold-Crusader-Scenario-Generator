#  Stronghold Crusader – Scenario Generator PL/EN

A desktop app for randomizing game scenarios in **Stronghold Crusader** - it's helpfull when you finished the game and you have no idea what scenario create. Rolls a map, player count, game mode, AI balance and team alliances - step by step. Hope you enjoy!

![Build](https://github.com/Refirebro/Stronghold-Crusader-Scenario-Generator/actions/workflows/build.yml/badge.svg)

---

## Download

Go to [**Releases**](../../releases) and grab the latest version:

| File | Description |
|------|-------------|
| `StrongholdCrusader-windows.zip` | Windows — unzip and run `.exe` (no Java needed) |
| `StrongholdCrusader.jar` | Any OS — requires Java 17+ |

---

## Running

**Windows EXE** (Java bundled, no install needed):
```
Unzip → run StrongholdCrusader\StrongholdCrusader.exe
```

**JAR (Windows / Linux / Mac)**:
```bash
java -jar StrongholdCrusader.jar
```

---

## Build from source

Requirements: **JDK 17+**

**Windows:**
```
build_and_run.bat
```

**Linux / Mac:**
```bash
chmod +x build_and_run.sh && ./build_and_run.sh
```

---

## Features

- 🗺 Random map from all 75 official Stronghold Crusader maps
- 👥 Random player count (matched to map capacity)
- ⚔ Random game mode (Normal / Crusade / Skirmish)
- ⚖ Random AI balance (1–5)
- 🤝 Random team / alliance layout
- PL/EN Full Polish / English language support

---

## Project structure

```
src/
  Main.java               entry point
  ScenarioGUI.java        main GUI window
  ScenarioGenerator.java  randomization logic
  MapLoader.java          map list PL/EN
  GameMap.java            map model
  Lang.java               translations
res/
  pl.png                  Polish flag
  uk.png                  UK flag
  twierdza.ico            app icon
.github/workflows/
  build.yml               GitHub Actions: builds JAR + EXE automatically
```
