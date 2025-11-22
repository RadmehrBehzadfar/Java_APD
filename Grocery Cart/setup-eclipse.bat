@echo off
echo Setting up Eclipse project for JavaFX...
echo.
echo Please do the following in Eclipse:
echo 1. Right-click on the project APDWorkshop5
echo 2. Select "Run As" -^> "Run Configurations..."
echo 3. Create a new "Java Application" configuration
echo 4. Set Main class to: grocerystore.Cart
echo 5. Go to Arguments tab and add these VM arguments:
echo    --module-path "C:\Users\radme\OneDrive\Desktop\Desktop\Seneca College\05\APD\ws01\new\CarLoanFX\javafx-sdk-21.0.2\lib"
echo    --add-modules javafx.controls,javafx.fxml
echo 6. Click Apply and Run
echo.
echo OR import the Cart.launch file:
echo 1. Right-click on Cart.launch in the project
echo 2. Select "Run As" -^> "Run Configurations..."
echo 3. The configuration should be imported automatically
echo.
pause


