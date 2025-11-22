@echo off
echo Starting Personal Finance Manager (Simple Mode)...
echo.

REM Simple approach with basic JavaFX options
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=sw -Djava.awt.headless=false application.Main

echo.
pause










