@echo off
echo Building Portable Personal Finance Manager...
echo.

cd /d "%~dp0"

echo Step 1: Compiling application...
javac --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin -d bin src\application\*.java src\controller\*.java src\model\*.java src\util\*.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Step 2: Creating JAR file...
jar cfm PersonalFinanceManager.jar MANIFEST.MF -C bin .

if %errorlevel% neq 0 (
    echo JAR creation failed!
    pause
    exit /b 1
)

echo Step 3: Creating portable executable...
jpackage ^
    --input . ^
    --main-jar PersonalFinanceManager.jar ^
    --main-class application.Main ^
    --name "Personal Finance Manager" ^
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
    --resource-dir resources

if %errorlevel% neq 0 (
    echo Executable creation failed!
    pause
    exit /b 1
)

echo.
echo ✅ Portable executable created successfully!
echo Location: dist\Personal Finance Manager.exe
echo.
echo You can now distribute this .exe file to your friends!
echo.
pause

