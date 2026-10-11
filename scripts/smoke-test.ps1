$ErrorActionPreference = 'Stop'

$containerName = 'sscrm-midterm-smoke'
$address = 'http://127.0.0.1:18080/'

try {
    #  Clear any leftover temporary test container from an earlier attempt.
    $existing = docker ps -aq --filter "name=^/$containerName$"
    if ($existing) { docker rm -f $containerName | Out-Null }

    #  The image comes from the Docker Build stage, not a manual build.
    docker run -d --name $containerName -p 127.0.0.1:18080:8080 sscrm:midterm | Out-Null
    if ($LASTEXITCODE -ne 0) { throw 'Docker could not start the test container.' }

    $passed = $false

    for ($attempt = 1; $attempt -le 20; $attempt++) {
        Start-Sleep -Seconds 2

        try {
            $response = Invoke-WebRequest -Uri $address -UseBasicParsing -TimeoutSec 3

            if ($response.StatusCode -eq 200 -and $response.Content -match 'Pipeline Health') {
                Write-Host 'HTTP smoke test PASSED: status 200, Pipeline Health found.'
                $passed = $true
                break
            }
        }
        catch {
            Write-Host "Waiting for SSCRM to respond (attempt $attempt of 20)..."
        }
    }

    if (-not $passed) {
        docker logs $containerName
        throw 'HTTP smoke test FAILED: no successful SSCRM response.'
    }
}
finally {
    #  Always remove the disposable container, even if the test fails.
    $existing = docker ps -aq --filter "name=^/$containerName$"
    if ($existing) { docker rm -f $containerName | Out-Null }
}
