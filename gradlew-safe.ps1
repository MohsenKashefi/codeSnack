# PowerShell script to safely run Gradle with file lock handling
param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$GradleArgs
)

$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
$gradlewPath = Join-Path $scriptPath "gradlew.bat"

Write-Host "Stopping any running Gradle daemons..." -ForegroundColor Yellow
& $gradlewPath --stop 2>&1 | Out-Null
Start-Sleep -Seconds 3

# Kill any Java processes that might be holding locks (be more careful here)
Write-Host "Releasing file locks..." -ForegroundColor Yellow
$javaProcesses = Get-Process -Name "java" -ErrorAction SilentlyContinue
if ($javaProcesses) {
    # Only kill if they seem to be Gradle-related (check parent or path)
    $javaProcesses | ForEach-Object {
        try {
            $proc = Get-CimInstance Win32_Process -Filter "ProcessId = $($_.Id)" -ErrorAction SilentlyContinue
            if ($proc -and ($proc.CommandLine -like "*gradle*" -or $proc.ParentProcessId -eq 0)) {
                Stop-Process -Id $_.Id -Force -ErrorAction SilentlyContinue
            }
        } catch {
            # Ignore errors
        }
    }
}
Start-Sleep -Seconds 2

Write-Host "Building..." -ForegroundColor Green
& $gradlewPath $GradleArgs

if ($LASTEXITCODE -ne 0) {
    Write-Host "`n========================================" -ForegroundColor Red
    Write-Host "BUILD FAILED! Check errors above." -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    exit $LASTEXITCODE
}

exit 0

