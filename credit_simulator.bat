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

if not exist "%JAR_PATH%" (
    echo [BUILD] Building credit_simulator with Maven...
    call mvn clean package -DskipTests
)

if "%~1"=="" (
    java -jar "%JAR_PATH%"
) else (
    java -jar "%JAR_PATH%" "%~1"
)
