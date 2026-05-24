#!/bin/bash
echo "Kompilacja / Compiling..."
mkdir -p build/classes
javac -encoding UTF-8 -d build/classes src/*.java || { echo "Błąd kompilacji!"; exit 1; }
cp res/* build/classes/
echo "Uruchamianie / Running..."
java -cp build/classes Main
