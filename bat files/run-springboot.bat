@echo off
echo ========================================
echo   STARTING SPRING BOOT APPLICATION
echo ========================================
echo.

REM Check if Maven is available
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven not found in PATH
    echo Please install Maven or add to PATH
    pause
    exit /b 1
)

REM Check if Java is available
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Java not found in PATH
    echo Please install Java JDK 17+
    pause
    exit /b 1
)

REM Check Docker containers are running
echo Checking Docker containers...
docker ps | findstr "postgres redis kafka" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Some Docker containers may not be running
    echo Run: docker-compose up -d
    echo.
)

echo Starting Spring Boot application...
echo Press Ctrl+C to stop
echo.

REM Run with Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Spring Boot failed to start
    echo.
    echo Troubleshooting:
    echo 1. Check Docker containers: docker ps
    echo 2. Check ports: netstat -ano | findstr :8080
    echo 3. Check Java version: java -version
    pause
    exit /b 1
)