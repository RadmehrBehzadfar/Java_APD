# Car Loan Payment Calculator

A JavaFX application for calculating car loan payments with a modern, user-friendly interface.

## Features

- 🎨 Modern, professional UI with gradient design
- ✅ Real-time input validation
- 🎚️ Interactive interest rate slider with live feedback
- 📅 Multiple payment frequency options (Monthly, Bi-Weekly, Weekly)
- 🛡️ Comprehensive error handling and user feedback
- 📱 Scrollable interface for smaller windows
- 💰 Detailed payment breakdown including principal, interest, and total amounts

## Prerequisites

- **Java 21 or higher** - [Download Java](https://www.oracle.com/java/technologies/downloads/)
- **JavaFX SDK 23.0.1** - [Download JavaFX](https://openjfx.io/)

### Installing JavaFX

1. Download JavaFX SDK 23.0.1 from [openjfx.io](https://openjfx.io/)
2. Extract it to a location on your system (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\`)
3. Note the path to the `lib` folder for running the application

## How to Run the Application

### Method 1: Using VS Code/Cursor (Recommended)

1. Open the `CarLoanFX` folder in VS Code or Cursor
2. Press **F5** or go to **Run → Start Debugging**
3. The application will launch automatically

### Method 2: Using the Batch File (Windows)

1. Double-click `run.bat` in the project folder
2. The application will start

**Note:** You may need to edit `run.bat` to match your JavaFX installation path.

### Method 3: Using Command Line

1. Open Terminal/PowerShell/Command Prompt
2. Navigate to the project folder
3. Compile the project:
   ```bash
   javac --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -d bin src/application/*.java
   ```
4. Run the application:
   ```bash
   java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -cp bin application.Main
   ```

   Replace `PATH_TO_JAVAFX_LIB` with your JavaFX lib folder path (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\lib`)

### Method 4: Using Eclipse

1. Open Eclipse
2. Import the project (File → Import → Existing Projects into Workspace)
3. Right-click on `Main.java` → **Run As → Java Application**
4. Configure VM arguments: `--module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml`

## Project Structure

```
CarLoanFX/
├── src/
│   └── application/
│       ├── Main.java          # Application entry point
│       ├── Controller.java    # Business logic and event handlers
│       ├── layout.fxml        # UI layout definition
│       └── styles.css         # Styling and themes
├── bin/                       # Compiled classes (not committed)
├── .vscode/
│   ├── launch.json           # VS Code launch configuration
│   └── settings.json         # VS Code project settings
├── .classpath                 # Eclipse classpath configuration
├── .project                   # Eclipse project configuration
├── run.bat                    # Quick run script for Windows
└── README.md                  # This file
```

## Usage

1. Enter the **Vehicle Price** (e.g., 30000)
2. Enter the **Down Payment** amount (e.g., 5000)
3. Use the slider or enter the **Interest Rate** (e.g., 5.5%)
4. Enter the **Loan Term** in years (e.g., 5)
5. Select the **Payment Frequency** (Monthly, Bi-Weekly, or Weekly)
6. Click **Calculate** to see the payment breakdown
7. Click **Clear** to reset all fields

## Technologies Used

- **Java 21** - Programming language
- **JavaFX 23.0.1** - UI framework
- **FXML** - UI markup language
- **CSS** - Styling

## Author

**Radmehr Behzadfar**
- Student ID: 148786221
- Course: APD545 - Winter
- Section: NDD

## License

This project is part of a coursework assignment and is for educational purposes only.

## Contributing

This is a coursework project. Contributions are not expected, but suggestions and feedback are welcome!
