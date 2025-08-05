@echo off
echo Building BedrockCombat Plugin...

REM Create directories
if not exist "target" mkdir target

REM Use the existing Spigot API JAR
set "API_JAR=spigot-api-1.21.8-R0.1-20250729.092320-4.jar"

REM Check if the API JAR exists
if not exist "%API_JAR%" (
    echo ERROR: Spigot API JAR not found: %API_JAR%
    echo Please make sure the JAR file is in the project root directory.
    pause
    exit /b 1
)

echo Using API JAR: %API_JAR%

REM Compile Java source
echo Compiling Java source...
for /r src\main\java %%f in (*.java) do echo %%f >> sources.txt
javac -cp "%API_JAR%" -d target @sources.txt
del sources.txt
if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Copy all resources
echo Copying resources...
copy src\main\resources\plugin.yml target\
copy src\main\resources\config.yml target\

REM Create JAR
echo Creating JAR file...
cd target
jar cf BedrockCombat-1.0.1.jar plugin.yml config.yml io/
cd ..

echo BedrockCombatCompat plugin built successfully!
echo JAR file: target\BedrockCombat-1.0.1.jar
echo.
echo To use this plugin:
echo 1. Copy target\BedrockCombat-1.0.1.jar to your server's plugins folder
echo 2. Restart your server
echo 3. Use /bedrockcombat ^<player^> to toggle Bedrock combat for a player
echo 4. Players with names starting with '.' automatically get Bedrock combat
echo.
pause