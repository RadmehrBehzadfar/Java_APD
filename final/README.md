# Personal Finance Management Application

## Overview
This desktop application is designed to help you manage your personal finances. Built with Java, JavaFX, and SQLite, it allows you to add, edit, and delete transactions and budgets. You can view summary reports with visual charts to better understand your spending habits and income trends.

## Features
- **Transaction Management:**  
  Add, edit, and delete income and expense transactions. Each transaction includes details such as date, amount, category, description, and type.
- **Budget Management:**  
  Set and manage monthly budget limits by category. The app alerts you if spending in any category nears or exceeds your budget.
- **Summary Reports:**  
  Visualize your finances with pie charts (expense breakdown by category) and bar charts (monthly net totals).
- **Data Validation:**  
  Robust validation ensures that inputs like dates (format: yyyy-MM-dd) and amounts are correct.
- **Database Integration:**  
  An SQLite database is used to store all transaction and budget data. The database is automatically initialized on first run.
- **Concurrency:**  
  Background tasks (using JavaFX `Task` and `Platform.runLater`) ensure that the user interface remains responsive even during data-intensive operations.

## Prerequisites
- Java Development Kit (JDK) 8 or higher.
- Eclipse IDE (or any other Java IDE that supports JavaFX).
- SQLite JDBC driver (included in the project libraries).

## Installation and Setup
1. **Download the Project:**  
   Unzip the provided ZIP file containing the complete source code, resources, and configuration files.
2. **Open the Project in Eclipse:**  
   - Go to **File > Import > Existing Projects into Workspace**.
   - Navigate to the unzipped folder and import the project.
3. **Ensure Dependencies Are Set Up:**  
   - Verify that JavaFX is configured in your project settings.
   - Confirm that the SQLite JDBC driver is added to your build path.
4. **Run the Application:**  
   - Locate `Main.java` in the `application` package.
   - Right-click and select **Run As > Java Application**.
   - The application window should launch, displaying the Personal Finance Management dashboard.

## How to Use the Application
- **Transactions Tab:**  
  Enter your transaction details (date, amount, category, description, type) and use the provided buttons to add, edit, or delete transactions.
- **Budget Tab:**  
  Set your budget limits for various categories. Edit or remove budgets as necessary.
- **Summary Tab:**  
  Click the *Load Summary* button to generate visual reports of your expenses (pie chart) and monthly net totals (bar chart).
- **Notifications:**  
  The app will display alerts if your spending exceeds or approaches your set budget limits.

## Additional Information
- **Database:**  
  The application creates an SQLite database file (`personal_finance.db`) automatically in the project directory.
- **Error Handling:**  
  Runtime errors and exceptions are logged to the console, and user-friendly alerts are shown when issues occur.

## Contact
For any issues or further assistance, please contact Radmehr Behzadfar at [radmehr.behzadfar.rbaa] or [rbehzadfar@myseneca.ca].