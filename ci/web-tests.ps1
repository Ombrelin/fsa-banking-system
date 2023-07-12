docker compose up -d --build

cd ..
$pwd = pwd;

Start-Sleep -Seconds 5;

docker run `
    --env APP_URL = "http://fsabank" `
    --env IS_CI = "true" `
    --net ci_default `
    --name fsabank-e2e-tests-exec `
    -v "${pwd}:/app" `
    -w /app `
    gradle:8.2.0-jdk17 `
    /bin/bash -c "gradle :web:test"

$result = docker inspect fsabank-e2e-tests-exec --format = '{{.State.ExitCode}}'

docker rm fsabank-e2e-tests-exec
cd ci
docker compose down

Exit $result