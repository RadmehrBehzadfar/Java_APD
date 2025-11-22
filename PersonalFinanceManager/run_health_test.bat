@echo off
echo Running Personal Finance Manager Health Tests...
echo.

cd /d "%~dp0"

echo Compiling test...
javac --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -d bin src\test\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Running health tests...
java -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" test.ApplicationHealthTest

echo.
pause

