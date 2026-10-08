@echo off
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0

if exist "%SCRIPT_DIR%target\" (
    set ROOT_DIR=%SCRIPT_DIR%
) else if exist "%SCRIPT_DIR%..\target\" (
    set ROOT_DIR=%SCRIPT_DIR%..\
) else (
    set ROOT_DIR=%SCRIPT_DIR%
)

cd /d "%ROOT_DIR%"
set JAR_PATH=target\credit-simulator.jar

set NEED_BUILD=0
if not exist "%JAR_PATH%" set NEED_BUILD=1
if "%NEED_BUILD%"=="0" (
    powershell -NoProfile -Command "$jar='target\credit-simulator.jar'; $src=Get-ChildItem -Recurse -File src,pom.xml | Sort-Object LastWriteTime -Descending | Select-Object -First 1; if ($src.LastWriteTime -gt (Get-Item $jar).LastWriteTime) { exit 1 } else { exit 0 }"
    if errorlevel 1 set NEED_BUILD=1
)
if "%NEED_BUILD%"=="1" (
    echo [BUILD] Menyusun ulang credit_simulator...
    call mvn -q package -DskipTests
    if errorlevel 1 (
        echo [ERROR] Build gagal. Cek Maven sudah terpasang, lalu jalankan lagi.
        exit /b 1
    )
)

if "%~1"=="" (
    java -jar "%JAR_PATH%"
) else (
    java -jar "%JAR_PATH%" "%~1"
)
