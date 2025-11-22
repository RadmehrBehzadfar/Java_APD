@echo off
cd /d "%~dp0"
java --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp bin application.Main
pause

