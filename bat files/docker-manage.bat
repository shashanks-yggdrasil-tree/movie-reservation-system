@echo off
echo ========================================
echo   DOCKER MANAGEMENT
echo ========================================
echo.
echo 1. Start all containers
echo 2. Stop all containers
echo 3. Restart all containers
echo 4. View logs
echo 5. Rebuild and restart
echo 6. Clean up (remove containers, volumes)
echo.
set /p choice="Select option (1-6): "

if "%choice%"=="1" (
    echo Starting all containers...
    docker-compose up -d
    echo ✅ Containers started
    goto end
)

if "%choice%"=="2" (
    echo Stopping all containers...
    docker-compose down
    echo ✅ Containers stopped
    goto end
)

if "%choice%"=="3" (
    echo Restarting all containers...
    docker-compose restart
    echo ✅ Containers restarted
    goto end
)

if "%choice%"=="4" (
    echo.
    echo Select service to view logs:
    echo 1. Spring Boot Postgres
    echo 2. Redis
    echo 3. Kafka
    echo 4. Zookeeper
    echo 5. Kafka UI
    echo 6. Python AI
    echo 7. All logs (follow)
    echo.
    set /p logchoice="Select (1-7): "

    if "%logchoice%"=="1" docker logs -f spring-postgres
    if "%logchoice%"=="2" docker logs -f booking-redis
    if "%logchoice%"=="3" docker logs -f kafka
    if "%logchoice%"=="4" docker logs -f zookeeper
    if "%logchoice%"=="5" docker logs -f kafka-ui
    if "%logchoice%"=="6" docker logs -f paperclip-ai
    if "%logchoice%"=="7" docker-compose logs -f
    goto end
)

if "%choice%"=="5" (
    echo Rebuilding and restarting containers...
    docker-compose up -d --build
    echo ✅ Containers rebuilt and restarted
    goto end
)

if "%choice%"=="6" (
    echo ⚠️ WARNING: This will remove ALL containers and volumes!
    set /p confirm="Type 'yes' to confirm: "
    if "%confirm%"=="yes" (
        echo Cleaning up...
        docker-compose down -v
        docker system prune -f
        echo ✅ Cleanup complete
    ) else (
        echo ❌ Cleanup cancelled
    )
    goto end
)

echo ❌ Invalid choice
:end
echo.
pause