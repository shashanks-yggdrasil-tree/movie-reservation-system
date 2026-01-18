@echo off
echo ========================================
echo   STARTING PYTHON AI ASSISTANT
echo ========================================
echo.

REM Check if in correct directory
if not exist "ai-assistant" (
    echo ❌ ai-assistant folder not found
    echo Run this script from project root directory
    pause
    exit /b 1
)

cd ai-assistant

REM Check if virtual environment exists
if not exist "venv" (
    echo ❌ Virtual environment not found
    echo Run setup-project.bat first
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

REM Check Docker containers are running
echo Checking Kafka is running...
docker ps | findstr "kafka" >nul
if %ERRORLEVEL% NEQ 0 (
    echo ⚠️ Kafka container may not be running
    echo Run: docker-compose up -d kafka
    echo.
)

echo Starting Python AI Assistant...
echo API: http://localhost:8000
echo Health: http://localhost:8000/health
echo WebSocket: ws://localhost:8000/ws/ai
echo.
echo Press Ctrl+C to stop
echo.

REM Run FastAPI server
uvicorn src.main:app --host 0.0.0.0 --port 8000 --reload

cd ..
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Python AI failed to start
    echo.
    echo Troubleshooting:
    echo 1. Check Python version: python --version
    echo 2. Check dependencies: pip list
    echo 3. Check ports: netstat -ano | findstr :8000
    pause
    exit /b 1
)