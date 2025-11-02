# PowerShell script to safely clean Gradle build with file lock handling
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$gradlewPath = Join-Path $scriptPath "gradlew.bat"

Write-Host "Stopping Gradle daemons..." -ForegroundColor Yellow
& $gradlewPath --stop 2>&1 | Out-Null
Start-Sleep -Seconds 3

Write-Host "Checking for file locks..." -ForegroundColor Yellow
# Don't kill all Java processes - just wait a bit for locks to release
Start-Sleep -Seconds 2

Write-Host "Cleaning build directory..." -ForegroundColor Yellow
$buildDir = Join-Path $scriptPath "app\build"
if (Test-Path $buildDir) {
    # Try to remove read-only files
    Get-ChildItem -Path $buildDir -Recurse -Force -ErrorAction SilentlyContinue | ForEach-Object {
        $_.Attributes = 'Normal'
    }
    Start-Sleep -Seconds 1
}

# Run the clean command
& $gradlewPath clean

if ($LASTEXITCODE -ne 0) {
    Write-Host "`n========================================" -ForegroundColor Red
    Write-Host "CLEAN FAILED! Files may still be locked." -ForegroundColor Red
    Write-Host "Try closing Android Studio and any file explorers." -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    exit $LASTEXITCODE
}

exit 0

