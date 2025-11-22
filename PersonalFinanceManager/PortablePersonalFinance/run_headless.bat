@echo off
echo Starting Personal Finance Manager (Headless Mode)...
echo.

REM Headless mode with software rendering
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=sw -Djava.awt.headless=true -Djavafx.animation.fullspeed=true application.Main

echo.
pause








