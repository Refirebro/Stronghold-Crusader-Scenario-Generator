#!/bin/bash
set -euo pipefail

echo "============================================"
echo " Stronghold Crusader - Scenario Randomizer"
echo "============================================"

# Sprawdz czy javac jest dostepny
if ! command -v javac &>/dev/null; then
    echo ""
    echo "[BLAD] Nie znaleziono javac!"
    echo "[ERROR] javac not found!"
    echo ""
    echo "Zainstaluj JDK 17+:"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  macOS:         brew install openjdk@17"
    echo "  Inne:          https://adoptium.net/"
    echo ""
    exit 1
fi

# Wyswietl wersje Javy
echo "Wersja Java / Java version:"
java -version
echo ""

# Stworz katalog build
mkdir -p build/classes

# Kompilacja
echo "[1/3] Kompilacja / Compiling..."
javac -encoding UTF-8 -source 17 -target 17 -d build/classes src/*.java
echo "      OK"

# Kopiowanie zasobow
echo "[2/3] Kopiowanie zasobow / Copying resources..."
cp res/*.png res/*.ico build/classes/ 2>/dev/null || true
echo "      OK"

# Uruchomienie
echo "[3/3] Uruchamianie / Running..."
echo ""
java -cp build/classes Main
