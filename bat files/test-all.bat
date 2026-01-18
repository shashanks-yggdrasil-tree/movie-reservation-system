@echo off
echo ========================================
echo   TESTING ALL SERVICES
echo ========================================
echo.

echo 1. Testing Docker containers...
docker ps
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker not running
    goto fail
)
echo ✅ Docker running
echo.

echo 2. Testing PostgreSQL...
timeout /t 5 /nobreak >nul
docker exec spring-postgres pg_isready -U admin -d userdb
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ PostgreSQL not ready yet
) else (
    echo ✅ PostgreSQL ready
)
echo.

echo 3. Testing Redis...
docker exec booking-redis redis-cli ping | findstr "PONG" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Redis not responding
) else (
    echo ✅ Redis ready
)
echo.

echo 4. Testing Kafka...
docker exec kafka kafka-topics --bootstrap-server localhost:9092 --list >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Kafka not ready
) else (
    echo ✅ Kafka ready
)
echo.

echo 5. Testing Python AI API...
curl -s -o nul -w "%%{http_code}" http://localhost:8000/health
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Python AI not responding
) else (
    echo ✅ Python AI ready
)
echo.

echo ========================================
echo   TEST COMPLETE
echo ========================================
echo.
echo Services Status:
echo - PostgreSQL: %postgres_status%
echo - Redis: %redis_status%
echo - Kafka: %kafka_status%
echo - Python AI: %ai_status%
echo.
echo Run individual services if any failed:
echo - run-springboot.bat
echo - run-python.bat
echo - docker-manage.bat
echo.
pause
exit /b 0

:fail
echo.
echo ❌ Some services failed
echo Run setup-project.bat first or check Docker
pause
exit /b 1