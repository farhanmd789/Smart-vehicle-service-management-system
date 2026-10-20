@echo off
echo ============================================
echo  Smart Vehicle Service Management System
echo  Build and Run Script
echo ============================================

:: Create output directory
if not exist "out" mkdir out
if not exist "data" mkdir data

:: Compile all Java source files
echo Compiling...
javac -d out -sourcepath src src\interfaces\*.java src\exceptions\*.java src\model\*.java src\util\*.java src\service\*.java src\menu\*.java src\Main.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [SUCCESS] Compilation complete.
echo.
echo Running application...
echo ============================================
java -cp out Main

pause
