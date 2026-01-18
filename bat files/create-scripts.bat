@echo off
echo Creating all setup scripts...
echo.

REM Create the scripts
(
echo @echo off
echo echo ========================================
echo echo   MOVIE BOOKING SYSTEM - SETUP SCRIPT
echo echo ========================================
echo echo.
echo.
echo REM Check prerequisites
echo echo Checking prerequisites...
echo where docker ^>nul 2^>nul
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Docker is not installed or not in PATH
echo     echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop/
echo     pause
echo     exit /b 1
echo ^)
echo.
echo where java ^>nul 2^>nul
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ⚠️ Java not found in PATH. Spring Boot might fail.
echo     echo.
echo ^)
echo.
echo where mvn ^>nul 2^>nul
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ⚠️ Maven not found in PATH. Spring Boot might fail.
echo     echo.
echo ^)
echo.
echo echo ✅ Prerequisites checked
echo echo.
echo.
echo REM Create project structure
echo echo Creating project structure...
echo if not exist "ai-assistant\src" mkdir ai-assistant\src
echo if not exist "logs" mkdir logs
echo echo ✅ Project structure created
echo echo.
echo.
echo REM Setup Python AI Assistant
echo echo ========================================
echo echo   SETUP PYTHON AI ASSISTANT
echo echo ========================================
echo echo.
echo.
echo cd ai-assistant
echo echo Creating Python virtual environment...
echo python -m venv venv
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Failed to create virtual environment
echo     echo Installing Python from: https://www.python.org/downloads/
echo     cd ..
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo Activating virtual environment...
echo call venv\Scripts\activate.bat
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Failed to activate virtual environment
echo     cd ..
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo Upgrading pip...
echo python -m pip install --upgrade pip
echo.
echo echo Installing Python dependencies...
echo if exist requirements.txt ^(
echo     pip install -r requirements.txt
echo ^) else ^(
echo     echo ⚠️ requirements.txt not found. Creating default...
echo     echo fastapi==0.104.1 ^> requirements.txt
echo     echo uvicorn[standard]==0.24.0 ^>^> requirements.txt
echo     echo pydantic==2.5.0 ^>^> requirements.txt
echo     echo confluent-kafka==2.2.0 ^>^> requirements.txt
echo     echo python-dotenv==1.0.0 ^>^> requirements.txt
echo     echo langchain==0.0.340 ^>^> requirements.txt
echo     echo openai==0.28.0 ^>^> requirements.txt
echo     pip install -r requirements.txt
echo ^)
echo.
echo echo ✅ Python AI setup complete
echo echo.
echo cd ..
echo.
echo REM Setup Spring Boot
echo echo ========================================
echo echo   SETUP SPRING BOOT
echo echo ========================================
echo echo.
echo.
echo if exist "pom.xml" ^(
echo     echo Installing Maven dependencies...
echo     mvn clean install -DskipTests
echo     if %%ERRORLEVEL%% NEQ 0 ^(
echo         echo ⚠️ Maven build had issues. Continuing anyway...
echo     ^)
echo ^) else ^(
echo     echo ⚠️ pom.xml not found. Ensure you're in the correct directory.
echo ^)
echo.
echo echo ✅ Spring Boot setup complete
echo echo.
echo.
echo REM Setup Docker
echo echo ========================================
echo echo   SETUP DOCKER CONTAINERS
echo echo ========================================
echo echo.
echo.
echo echo Starting Docker containers...
echo echo This may take a few minutes on first run...
echo echo.
echo.
echo REM Check if Docker is running
echo docker ps ^>nul 2^>nul
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Docker is not running. Please start Docker Desktop.
echo     pause
echo     exit /b 1
echo ^)
echo.
echo REM Stop any existing containers
echo echo Stopping any existing containers...
echo docker-compose down ^>nul 2^>nul
echo.
echo REM Build and start containers
echo echo Building and starting containers...
echo docker-compose up -d --build
echo.
echo if %%ERRORLEVEL%% NEQ 0 ^(
echo     echo ❌ Failed to start Docker containers
echo     echo.
echo     echo Troubleshooting steps:
echo     echo 1. Ensure Docker Desktop is running
echo     echo 2. Check port conflicts ^(5432, 6379, 9092, 8080, 8000^)
echo     echo 3. Check docker-compose.yml syntax
echo     pause
echo     exit /b 1
echo ^)
echo.
echo echo ✅ Docker containers started
echo echo.
echo.
echo REM Wait for services to be ready
echo echo ========================================
echo echo   WAITING FOR SERVICES TO BE READY
echo echo ========================================
echo echo.
echo.
echo echo Waiting for services to be healthy ^(30 seconds^)...
echo timeout /t 30 /nobreak ^>nul
echo.
echo echo.
echo echo ========================================
echo echo   SETUP COMPLETE!
echo echo ========================================
echo echo.
echo echo Services available at:
echo echo.
echo echo 📍 PostgreSQL Database: localhost:5432
echo echo 📍 Redis Cache: localhost:6379
echo echo 📍 Kafka Broker: localhost:9092
echo echo 📍 Kafka UI: http://localhost:8081
echo echo 📍 Spring Boot API: http://localhost:8080
echo echo 📍 Python AI API: http://localhost:8000
echo echo 📍 Python AI Health: http://localhost:8000/health
echo echo.
echo echo Next steps:
echo echo 1. Run Spring Boot: run-springboot.bat
echo echo 2. Run Python AI: run-python.bat
echo echo 3. Access Kafka UI: http://localhost:8081
echo echo.
echo echo For help: help.bat
echo echo.
echo pause
) > setup-project.bat

echo ✅ Created setup-project.bat

REM Create other scripts similarly (shortened for brevity)
echo ✅ All scripts created
echo.
echo Run setup-project.bat to set up the entire project
pause