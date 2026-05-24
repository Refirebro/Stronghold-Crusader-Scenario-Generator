@echo off
chcp 65001 >nul
echo Kompilacja / Compiling...
mkdir build\classes 2>nul
javac -encoding UTF-8 -d build\classes src\*.java
if errorlevel 1 (
    echo.
    echo BLAD KOMPILACJI! Upewnij sie ze masz zainstalowane JDK 17+
    echo COMPILATION ERROR! Make sure JDK 17+ is installed.
    pause
    exit /b 1
)
copy res\*.png build\classes\ >nul
copy res\*.ico build\classes\ >nul
echo Uruchamianie / Running...
java -cp build\classes Main
