# Java APD545 - JavaFX Applications Portfolio

A comprehensive collection of JavaFX desktop applications developed as part of the **APD545 - Advanced Programming and Design** course at Seneca College. This portfolio demonstrates various JavaFX concepts, including UI design, event handling, data persistence, and modern desktop application development.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Projects](#projects)
  - [1. Car Loan Calculator](#1-car-loan-calculator)
  - [2. Grocery Cart](#2-grocery-cart)
  - [3. Personal Finance Manager](#3-personal-finance-manager)
  - [4. Pizza Shop](#4-pizza-shop)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Features Summary](#features-summary)
- [Running the Applications](#running-the-applications)
- [Author Information](#author-information)
- [License](#license)

---

## 🎯 Overview

This repository contains four distinct JavaFX applications, each showcasing different aspects of desktop application development:

- **Financial Tools**: Loan calculation and personal finance management
- **E-Commerce**: Shopping cart with persistence capabilities
- **Business Application**: Pizza shop order management system

All applications feature modern, user-friendly interfaces with comprehensive input validation, error handling, and professional UI design.

---

## 🚀 Projects

### 1. Car Loan Calculator

**Workshop #02** | A sophisticated car loan payment calculator with multiple payment frequency options.

#### Features
- 💰 **Loan Payment Calculation**: Calculate monthly, bi-weekly, or weekly payments
- 🎚️ **Interactive Slider**: Visual loan term selection with real-time feedback
- ✅ **Real-time Validation**: Input validation with visual feedback
- 📊 **Payment Breakdown**: Detailed calculation including principal, interest, and total amounts
- 🎨 **Modern UI**: Professional gradient design with responsive layout
- 📱 **Scrollable Interface**: Optimized for various window sizes

#### Key Components
- `Main.java`: Application entry point
- `Controller.java`: Business logic and loan calculation algorithms
- `layout.fxml`: UI layout definition
- `styles.css`: Modern styling with gradients and animations

#### Running the Application
```bash
cd "Car Loan Calculator"
run.bat
```

Or manually:
```bash
javac --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -d bin src/application/*.java
java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -cp bin application.Main
```

---

### 2. Grocery Cart

**Workshop #05** | A feature-rich grocery shopping cart application with cart persistence and management.

#### Features
- 🛒 **Shopping Cart Management**: Add, remove, and modify items in your cart
- 💾 **Cart Persistence**: Save and load shopping carts using Java serialization
- 📊 **Item Master List**: Browse available items from CSV data file
- 🎯 **Real-time Calculations**: Automatic total calculation with binding
- 📋 **Interactive Tables**: Double-click items to select, view cart details
- 💵 **Currency Formatting**: Professional price display with proper formatting
- 🔄 **Quantity Management**: Slider-based quantity selection (1-20 units)
- 💡 **User Guidance**: Comprehensive tooltips and user feedback

#### Key Components
- `Cart.java`: Main application controller with cart logic
- `Model.java`: Data model for item management
- `Item.java`: Item entity with observable properties
- `ItemInCart.java`: Cart item representation
- `CartFileHandler.java`: Serialization for cart persistence
- `SavedCartController.java`: Load saved carts interface

#### Data Files
- `ItemsMaster.csv`: Master list of available grocery items with prices
- `saved_carts/`: Directory containing serialized cart files

#### Running the Application
```bash
cd "Grocery Cart"
run.bat
```

---

### 3. Personal Finance Manager

**Comprehensive Finance Management System** | A full-featured personal finance application with database persistence, budgeting, bill reminders, and financial analytics.

#### Features

##### 💰 Transaction Management
- Add, edit, and delete income and expense transactions
- Categorize transactions with custom categories
- Date-based filtering and search functionality
- Export transactions to CSV format
- Real-time transaction table updates

##### 📊 Budget Management
- Set budget limits for different categories
- Automatic budget vs. spending comparison
- Visual indicators for over-budget categories
- Track spending against budgets in real-time

##### 📅 Bill Reminders
- Create bill reminders with due dates
- Customizable reminder days before due date
- Mark bills as paid/unpaid
- Sort by due date for easy tracking

##### 📈 Financial Summary
- Income vs. expense overview
- Category-wise spending analysis
- Monthly and yearly summaries
- Visual financial health indicators

##### 📆 Calendar View
- Monthly view of transactions and bills
- Date-based navigation
- Quick access to transactions by date

#### Key Components

**Application Layer:**
- `Main.java`: Application entry point with database initialization
- `MainController.java`: Main window controller

**Controllers:**
- `TransactionsController.java`: Transaction CRUD operations and filtering
- `BudgetController.java`: Budget management
- `BillRemindersController.java`: Bill reminder management
- `SummaryController.java`: Financial summary and analytics
- `CalendarController.java`: Calendar view of transactions

**Model Layer:**
- `Database.java`: SQLite database operations (Singleton pattern)
- `Transaction.java`: Transaction entity
- `Budget.java`: Budget entity
- `BillReminder.java`: Bill reminder entity
- `DBInitializer.java`: Database schema initialization

**Utilities:**
- `DateUtil.java`: Date formatting and manipulation utilities

**Testing:**
- `ApplicationHealthTest.java`: Comprehensive health check suite

#### Database Schema
- `transactions`: Stores all financial transactions
- `budgets`: Stores budget limits by category
- `bill_reminders`: Stores bill reminders with due dates

#### Running the Application

**Standard Mode:**
```bash
cd PersonalFinanceManager
RUN_APP.bat
```

**Health Check:**
```bash
run_health_test.bat
```

**Portable Build:**
The application includes portable builds with bundled JavaFX libraries for distribution without requiring JavaFX installation.

---

### 4. Pizza Shop

**Pizza Order Management System** | A professional pizza ordering application with real-time pricing, order management, and business statistics.

#### Features
- 🍕 **Pizza Selection**: Choose from Cheese, Vegetarian, or Meat Lover pizzas
- 📏 **Size Options**: Small, Medium, and Large sizes with dynamic pricing
- 🔢 **Quantity Management**: Order 1-100 pizzas with real-time price updates
- 👤 **Customer Information**: Collect customer name and phone number
- 💵 **Real-time Pricing**: Live price calculation with subtotal and tax (13%)
- 📊 **Business Statistics**: Track total orders, revenue, and average order value
- 📝 **Order Summary**: Detailed order confirmation with full breakdown
- ✨ **Animations**: Smooth UI animations for better user experience
- ✅ **Input Validation**: Real-time validation with visual feedback

#### Key Components
- `PizzaShopApp.java`: Application entry point
- `PizzaController.java`: Main controller with order logic
- `Order.java`: Order entity with pricing calculations
- `Customer.java`: Customer information model

#### Pricing Structure
- **Cheese**: Small ($8), Medium ($10), Large ($12)
- **Vegetarian**: Small ($9), Medium ($11), Large ($13)
- **Meat Lover**: Small ($10), Medium ($13), Large ($15)
- **Tax**: 13% HST applied to all orders

#### Running the Application
```bash
cd "Pizza Shop"
java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -cp bin pizzaapp.PizzaShopApp
```

---

## 📦 Prerequisites

### Required Software

1. **Java Development Kit (JDK) 21 or higher**
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

2. **JavaFX SDK 23.0.1 or compatible version**
   - Download from: [OpenJFX](https://openjfx.io/)
   - Extract to a location on your system (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\`)

3. **SQLite JDBC Driver** (Required for Personal Finance Manager)
   - Included in the Personal Finance Manager project
   - Alternatively download from: [SQLite JDBC](https://github.com/xerial/sqlite-jdbc)

### System Requirements

- **Operating System**: Windows 10/11 (batch files provided), macOS, or Linux
- **Memory**: Minimum 2GB RAM
- **Disk Space**: ~500MB for all applications and dependencies

---

## 🛠️ Installation & Setup

### Step 1: Install Java JDK

1. Download and install JDK 21 or higher
2. Verify installation:
   ```bash
   java -version
   javac -version
   ```

### Step 2: Install JavaFX SDK

1. Download JavaFX SDK 23.0.1 from [OpenJFX](https://openjfx.io/)
2. Extract to a directory (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\`)
3. Note the path to the `lib` folder (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1\lib`)

### Step 3: Configure JavaFX Path

**For Windows Batch Files:**
Edit the `.bat` files and update the `--module-path` to match your JavaFX installation:

```batch
--module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib"
```

**For IDE Setup (VS Code/Cursor):**
1. Open project in VS Code/Cursor
2. Press `F5` or go to `Run → Start Debugging`
3. The application should launch if JavaFX is properly configured

**For Eclipse:**
1. Import project: `File → Import → Existing Projects into Workspace`
2. Configure VM arguments: `--module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml`
3. Run: `Right-click Main.java → Run As → Java Application`

---

## 📁 Project Structure

```
Java_APD/
│
├── Car Loan Calculator/          # Car loan payment calculator
│   ├── src/
│   │   └── application/
│   │       ├── Main.java
│   │       ├── Controller.java
│   │       ├── layout.fxml
│   │       └── styles.css
│   ├── bin/                      # Compiled classes
│   ├── run.bat                   # Quick run script
│   └── README.md                 # Project-specific README
│
├── Grocery Cart/                 # Shopping cart application
│   ├── src/
│   │   └── grocerystore/
│   │       ├── Cart.java
│   │       ├── Model.java
│   │       ├── Item.java
│   │       ├── ItemInCart.java
│   │       ├── CartFileHandler.java
│   │       └── SavedCartController.java
│   ├── resources/
│   │   ├── ItemsMaster.csv       # Item master data
│   │   ├── layout.fxml
│   │   ├── SavedCart.fxml
│   │   └── styles.css
│   ├── saved_carts/              # Serialized cart files
│   ├── run.bat
│   └── setup-eclipse.bat
│
├── PersonalFinanceManager/       # Comprehensive finance manager
│   ├── src/
│   │   ├── application/
│   │   │   ├── Main.java
│   │   │   └── MainController.java
│   │   ├── controller/
│   │   │   ├── TransactionsController.java
│   │   │   ├── BudgetController.java
│   │   │   ├── BillRemindersController.java
│   │   │   ├── SummaryController.java
│   │   │   └── CalendarController.java
│   │   ├── model/
│   │   │   ├── Database.java
│   │   │   ├── Transaction.java
│   │   │   ├── Budget.java
│   │   │   ├── BillReminder.java
│   │   │   └── DBInitializer.java
│   │   ├── util/
│   │   │   └── DateUtil.java
│   │   └── test/
│   │       └── ApplicationHealthTest.java
│   ├── resources/
│   │   ├── fxml/                 # FXML layouts
│   │   └── images/               # Application icons
│   ├── personal_finance.db       # SQLite database
│   ├── RUN_APP.bat
│   ├── run_health_test.bat
│   └── PortablePersonalFinance/  # Portable build
│
├── Pizza Shop/                   # Pizza ordering system
│   ├── src/
│   │   └── pizzaapp/
│   │       ├── PizzaShopApp.java
│   │       ├── controller/
│   │       │   └── PizzaController.java
│   │       └── model/
│   │           ├── Order.java
│   │           └── Customer.java
│   ├── resources/
│   │   ├── Pizza.fxml
│   │   ├── pizza1.jpg
│   │   └── styles.css
│   └── bin/
│
└── README.md                     # This file
```

---

## 🎨 Technologies Used

### Core Technologies
- **Java 21**: Modern Java features and performance
- **JavaFX 23.0.1**: Rich client application framework
- **FXML**: Declarative UI markup
- **CSS**: Styling and theming

### Additional Libraries
- **SQLite JDBC**: Database connectivity (Personal Finance Manager)
- **Java Serialization**: Data persistence (Grocery Cart)

### Design Patterns
- **MVC (Model-View-Controller)**: Separation of concerns
- **Singleton**: Database connection management
- **Observer**: JavaFX property bindings
- **Factory**: UI component creation

---

## ✨ Features Summary

| Feature | Car Loan Calculator | Grocery Cart | Personal Finance Manager | Pizza Shop |
|---------|-------------------|--------------|------------------------|------------|
| Real-time Validation | ✅ | ✅ | ✅ | ✅ |
| Data Persistence | ❌ | ✅ (Serialization) | ✅ (SQLite) | ❌ |
| Export Functionality | ❌ | ❌ | ✅ (CSV) | ❌ |
| Statistics/Analytics | ❌ | ❌ | ✅ | ✅ |
| Modern UI/Animations | ✅ | ✅ | ✅ | ✅ |
| Search/Filter | ❌ | ❌ | ✅ | ❌ |
| Calendar Integration | ❌ | ❌ | ✅ | ❌ |
| Reminder System | ❌ | ❌ | ✅ | ❌ |
| Multi-tab Interface | ❌ | ❌ | ✅ | ❌ |

---

## 🚀 Running the Applications

### Quick Start (Windows)

Each project includes a `run.bat` file for quick execution:

1. **Car Loan Calculator:**
   ```bash
   cd "Car Loan Calculator"
   run.bat
   ```

2. **Grocery Cart:**
   ```bash
   cd "Grocery Cart"
   run.bat
   ```

3. **Personal Finance Manager:**
   ```bash
   cd PersonalFinanceManager
   RUN_APP.bat
   ```

4. **Pizza Shop:**
   ```bash
   cd "Pizza Shop"
   # Compile first, then run
   javac --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -d bin src/pizzaapp/**/*.java
   java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -cp bin pizzaapp.PizzaShopApp
   ```

### Manual Compilation and Execution

#### Compile
```bash
javac --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -d bin src/**/*.java
```

#### Run
```bash
java --module-path "PATH_TO_JAVAFX_LIB" --add-modules javafx.controls,javafx.fxml -cp bin package.MainClass
```

**Replace:**
- `PATH_TO_JAVAFX_LIB` with your JavaFX lib directory path
- `package.MainClass` with the actual main class (e.g., `application.Main`)

---

## 👤 Author Information

**Radmehr Behzadfar**
- **Student ID**: 148786221
- **Course**: APD545 - Advanced Programming and Design
- **Semester**: Winter
- **Section**: NDD
- **Institution**: Seneca College

### Academic Integrity

All work in this repository represents my own work in accordance with Seneca Academic Policy.

**Signature**: RadmehrBehzadfar

---

## 📄 License

This project is part of a coursework assignment and is for **educational purposes only**. 

All code is intended for learning and demonstration of JavaFX application development concepts. Please respect academic integrity policies when referencing this work.

---

## 📚 Additional Resources

### JavaFX Documentation
- [Official JavaFX Documentation](https://openjfx.io/)
- [JavaFX API Documentation](https://openjfx.io/javadoc/23/)
- [JavaFX Tutorial](https://openjfx.io/openjfx-docs/)

### Learning Resources
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [JavaFX Scene Builder](https://gluonhq.com/products/scene-builder/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)

---

## 🐛 Troubleshooting

### Common Issues

**Issue: "Error: JavaFX runtime components are missing"**
- **Solution**: Ensure JavaFX SDK is installed and the `--module-path` points to the correct `lib` directory

**Issue: Database connection errors (Personal Finance Manager)**
- **Solution**: Ensure `sqlite-jdbc.jar` is in the classpath. Check that the database file is not locked by another process.

**Issue: FXML file not found**
- **Solution**: Ensure FXML files are in the `resources` folder and properly referenced in the code

**Issue: Styles not applying**
- **Solution**: Verify CSS file path and ensure stylesheet is loaded in the Scene

---

## 📝 Notes

- All applications require JavaFX to be properly installed and configured
- The Personal Finance Manager creates a SQLite database file (`personal_finance.db`) in the project directory
- Grocery Cart saves serialized cart files in the `saved_carts/` directory
- Batch files (`.bat`) are configured for Windows. Linux/Mac users should use equivalent shell scripts or run commands manually

---

## 🎓 Course Context

This portfolio was developed as part of **APD545 - Advanced Programming and Design** at Seneca College, focusing on:
- Desktop application development with JavaFX
- User interface design and user experience
- Data persistence and file I/O
- Database integration
- Software design patterns
- Event-driven programming
- Input validation and error handling

---

**Last Updated**: 2025

**Repository**: Java_APD - Advanced Programming and Design Portfolio

