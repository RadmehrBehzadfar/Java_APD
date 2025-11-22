@echo off
set JAVAFX_PATH=C:\Users\radme\OneDrive\Desktop\Desktop\Seneca College\05\APD\ws01\new\CarLoanFX\javafx-sdk-21.0.2\lib

echo Compiling application...
javac --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -d bin -sourcepath src src\grocerystore\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Copying resources...
xcopy /Y /I resources\* bin\ >nul

echo Running application...
java --module-path %JAVAFX_PATH% --add-modules javafx.controls,javafx.fxml -cp bin grocerystore.Cart

pause


