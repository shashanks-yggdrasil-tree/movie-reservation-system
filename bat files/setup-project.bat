@echo off
echo ========================================
echo   MOVIE BOOKING SYSTEM - SETUP SCRIPT
echo ========================================
echo.

REM Check prerequisites
echo Checking prerequisites...
where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker is not installed or not in PATH
    echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop/
    pause
    exit /b 1
)

where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Java not found in PATH. Spring Boot might fail.
    echo.
)

where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Maven not found in PATH. Spring Boot might fail.
    echo.
)

echo ✅ Prerequisites checked
echo.

REM Create project structure
echo Creating project structure...
if not exist "ai-assistant\src" mkdir ai-assistant\src
if not exist "logs" mkdir logs
echo ✅ Project structure created
echo.

REM Setup Python AI Assistant
echo ========================================
echo   SETUP PYTHON AI ASSISTANT
echo ========================================
echo.

cd ai-assistant
echo Creating Python virtual environment...
python -m venv venv
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to create virtual environment
    echo Installing Python from: https://www.python.org/downloads/
    cd ..
    pause
    exit /b 1
)

echo Activating virtual environment...
call venv\Scripts\activate.bat
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to activate virtual environment
    cd ..
    pause
    exit /b 1
)

echo Upgrading pip...
python -m pip install --upgrade pip

echo Installing Python dependencies...
if exist requirements.txt (
    pip install -r requirements.txt
) else (
    echo ⚠️ requirements.txt not found. Creating default...
    echo fastapi==0.104.1 > requirements.txt
    echo uvicorn[standard]==0.24.0 >> requirements.txt
    echo pydantic==2.5.0 >> requirements.txt
    echo confluent-kafka==2.2.0 >> requirements.txt
    echo python-dotenv==1.0.0 >> requirements.txt
    echo langchain==0.0.340 >> requirements.txt
    echo openai==0.28.0 >> requirements.txt
    pip install -r requirements.txt
)

echo ✅ Python AI setup complete
echo.
cd ..

REM Setup Spring Boot
echo ========================================
echo   SETUP SPRING BOOT
echo ========================================
echo.

if exist "pom.xml" (
    echo Installing Maven dependencies...
    mvn clean install -DskipTests
    if %ERRORLEVEL% NEQ 0 (
        echo ⚠️ Maven build had issues. Continuing anyway...
    )
) else (
    echo ⚠️ pom.xml not found. Ensure you're in the correct directory.
)

echo ✅ Spring Boot setup complete
echo.

REM Setup Docker
echo ========================================
echo   SETUP DOCKER CONTAINERS
echo ========================================
echo.

echo Starting Docker containers...
echo This may take a few minutes on first run...
echo.

REM Check if Docker is running
docker ps >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Docker is not running. Please start Docker Desktop.
    pause
    exit /b 1
)

REM Stop any existing containers
echo Stopping any existing containers...
docker-compose down >nul 2>nul

REM Build and start containers
echo Building and starting containers...
docker-compose up -d --build

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Failed to start Docker containers
    echo.
    echo Troubleshooting steps:
    echo 1. Ensure Docker Desktop is running
    echo 2. Check port conflicts (5432, 6379, 9092, 8080, 8000)
    echo 3. Check docker-compose.yml syntax
    pause
    exit /b 1
)

echo ✅ Docker containers started
echo.

REM Wait for services to be ready
echo ========================================
echo   WAITING FOR SERVICES TO BE READY
echo ========================================
echo.

echo Waiting for services to be healthy (30 seconds)...
timeout /t 30 /nobreak >nul

echo.
echo ========================================
echo   SETUP COMPLETE!
echo ========================================
echo.
echo Services available at:
echo.
echo 📍 PostgreSQL Database: localhost:5432
echo 📍 Redis Cache: localhost:6379
echo 📍 Kafka Broker: localhost:9092
echo 📍 Kafka UI: http://localhost:8081
echo 📍 Spring Boot API: http://localhost:8080
echo 📍 Python AI API: http://localhost:8000
echo 📍 Python AI Health: http://localhost:8000/health
echo.
echo Next steps:
echo 1. Run Spring Boot: run-springboot.bat
echo 2. Run Python AI: run-python.bat
echo 3. Access Kafka UI: http://localhost:8081
echo.
echo For help: help.bat
echo.
pause