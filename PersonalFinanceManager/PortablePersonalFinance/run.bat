@echo off
echo Starting Personal Finance Manager...
echo.

REM Try different JavaFX rendering options
echo Attempting to start with software rendering...
java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=sw -Dprism.verbose=true -Djava.awt.headless=false application.Main

if %errorlevel% neq 0 (
    echo.
    echo Software rendering failed, trying with D3D...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=d3d -Dprism.verbose=true -Djava.awt.headless=false application.Main
)

if %errorlevel% neq 0 (
    echo.
    echo D3D failed, trying with ES2...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=es2 -Dprism.verbose=true -Djava.awt.headless=false application.Main
)

if %errorlevel% neq 0 (
    echo.
    echo ES2 failed, trying with fallback options...
    java --module-path "lib" --add-modules javafx.controls,javafx.fxml -cp "bin;sqlite-jdbc.jar;slf4j-api.jar" -Dprism.order=sw -Dprism.verbose=true -Djava.awt.headless=false -Djavafx.animation.fullspeed=true -Djavafx.animation.pulse=60 application.Main
)

echo.
pause
