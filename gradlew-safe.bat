@echo off
REM Safe Gradle wrapper that stops daemons and handles file locks on Windows
REM Use PowerShell script if available, otherwise fallback to batch
if exist "%~dp0gradlew-safe.ps1" (
    powershell.exe -ExecutionPolicy Bypass -File "%~dp0gradlew-safe.ps1" %*
    exit /b %ERRORLEVEL%
)

REM Fallback to basic batch script
echo Stopping any running Gradle daemons...
if exist gradlew.bat (
    gradlew.bat --stop >nul 2>&1
    timeout /t 3 /nobreak >nul 2>&1
)
echo Building...
gradlew.bat %*
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo BUILD FAILED! Check errors above.
    echo ========================================
    echo.
    echo TIP: Close Android Studio and file explorers, then try again.
    echo.
    exit /b %ERRORLEVEL%
)

