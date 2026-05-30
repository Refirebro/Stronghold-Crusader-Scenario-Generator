@echo off
chcp 65001 >nul
setlocal

echo ============================================
echo  Stronghold Crusader - Scenario Randomizer
echo ============================================

:: Sprawdz czy javac jest dostepny
where javac >nul 2>&1
if errorlevel 1 (
    echo.
    echo [BLAD] Nie znaleziono javac!
    echo [ERROR] javac not found!
    echo.
    echo Zainstaluj JDK 17+ i dodaj do PATH:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

:: Wyswietl wersje Javy
echo Wersja Java / Java version:
java -version
echo.

:: Stworz katalog build jesli nie istnieje
if not exist build\classes mkdir build\classes

:: Kompilacja
echo [1/3] Kompilacja / Compiling...
javac -encoding UTF-8 -source 17 -target 17 -d build\classes src\*.java
if errorlevel 1 (
    echo.
    echo [BLAD] Kompilacja nieudana! / Compilation failed!
    echo Upewnij sie ze masz JDK 17+
    echo Make sure you have JDK 17+
    echo.
    pause
    exit /b 1
)
echo     OK

:: Kopiowanie zasobow
echo [2/3] Kopiowanie zasobow / Copying resources...
copy /Y res\*.png build\classes\ >nul 2>&1
copy /Y res\*.ico build\classes\ >nul 2>&1
echo     OK

:: Uruchomienie
echo [3/3] Uruchamianie / Running...
echo.
java -cp build\classes Main

endlocal
