@echo off
echo Building Standalone Portable Personal Finance Manager...
echo.

cd /d "%~dp0"

echo Step 1: Compiling application...
javac --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin -d bin src\application\*.java src\controller\*.java src\model\*.java src\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Step 2: Creating standalone executable (no installation required)...
jpackage ^
    --input . ^
    --main-jar PersonalFinanceManager.jar ^
    --main-class application.Main ^
    --name "PersonalFinanceManager" ^
    --app-version "1.0.0" ^
    --description "Personal Finance Management Application" ^
    --vendor "Radmehr Behzadfar" ^
    --type exe ^
    --dest dist ^
    --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" ^
    --add-modules javafx.controls,javafx.fxml ^
    --java-options "-Dfile.encoding=UTF-8" ^
    --win-console ^
    --win-shortcut ^
    --win-menu ^
    --resource-dir resources ^
    --runtime-image "C:\Program Files\Java\jdk-21" ^
    --java-options "-Xmx512m" ^
    --java-options "-Djava.library.path=." ^
    --java-options "-Dprism.order=sw"

if %errorlevel% neq 0 (
    echo Standalone executable creation failed!
    pause
    exit /b 1
)

echo.
echo ✅ Standalone portable executable created successfully!
echo Location: dist\PersonalFinanceManager.exe
echo.
echo This version runs directly without installation!
echo.
pause

