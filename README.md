# ⚔ Stronghold Crusader – Scenario Generator

Aplikacja do losowania scenariuszy do gry **Stronghold Crusader**. Losuje mapę, liczbę graczy, tryb gry, balans i układ drużyn.

![Build](https://github.com/TWOJ_USERNAME/TWOJ_REPO/actions/workflows/build.yml/badge.svg)

---

## Pobieranie / Download

Przejdź do zakładki [**Releases**](../../releases) i pobierz najnowszą wersję:

| Plik | Opis |
|------|------|
| `StrongholdCrusader-windows.zip` | Wersja Windows — rozpakuj i uruchom `.exe` |
| `StrongholdCrusader.jar` | Wersja JAR — wymaga Java 17+ |

---

## Uruchamianie / Running

**Windows EXE** (brak wymagań — Java wbudowana):
```
Rozpakuj zip → uruchom StrongholdCrusader\StrongholdCrusader.exe
```

**JAR (Windows / Linux / Mac)**:
```bash
java -jar StrongholdCrusader.jar
```

---

## Kompilacja ze źródeł / Build from source

Wymagania: **JDK 17+**

**Windows:**
```
build_and_run.bat
```

**Linux / Mac:**
```bash
chmod +x build_and_run.sh
./build_and_run.sh
```

---

## Funkcje / Features

- 🗺 Losowanie mapy z pełną listą map Stronghold Crusader
- 👥 Losowanie liczby graczy (dopasowane do mapy)
- ⚔ Losowanie trybu gry (Normalna / Krucjata / Potyczka)
- ⚖ Losowanie balansu AI (1–5)
- 🤝 Losowanie układu drużyn / sojuszy
- 🇵🇱 🇬🇧 Obsługa języka polskiego i angielskiego

---

## Struktura projektu

```
src/
  Main.java             # punkt wejścia
  ScenarioGUI.java      # główne okno GUI
  ScenarioGenerator.java# logika losowania
  MapLoader.java        # lista map PL/EN
  GameMap.java          # model mapy
  Lang.java             # tłumaczenia
res/
  pl.png                # flaga PL
  uk.png                # flaga UK
  twierdza.ico          # ikona aplikacji
.github/workflows/
  build.yml             # GitHub Actions: buduje JAR + EXE
```
