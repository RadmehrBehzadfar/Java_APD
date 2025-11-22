@echo off
cd /d "%~dp0PortablePersonalFinance"
java --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics --add-reads org.xerial.sqlitejdbc=ALL-UNNAMED --add-opens org.xerial.sqlitejdbc/org.sqlite.util=ALL-UNNAMED -cp "bin;resources;sqlite-jdbc.jar;slf4j-api.jar" application.Main
pause


