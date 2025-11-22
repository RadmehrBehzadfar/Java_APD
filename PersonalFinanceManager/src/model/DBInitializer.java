package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {
        try {
            Connection conn = Database.getConnection();
            try (Statement stmt = conn.createStatement()) {

                // Create Transactions table if it doesn't exist
                String createTransactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "date TEXT NOT NULL, "
                        + "amount REAL NOT NULL, "
                        + "category TEXT NOT NULL, "
                        + "description TEXT, "
                        + "type TEXT NOT NULL"
                        + ");";
                stmt.execute(createTransactionsTable);

                // Create Budgets table if it doesn't exist
                String createBudgetsTable = "CREATE TABLE IF NOT EXISTS budgets ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "category TEXT NOT NULL, "
                        + "limit_amount REAL NOT NULL"
                        + ");";
                stmt.execute(createBudgetsTable);

                // Create Bill Reminders table if it doesn't exist
                String createBillRemindersTable = "CREATE TABLE IF NOT EXISTS bill_reminders ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "description TEXT, "
                        + "amount REAL NOT NULL, "
                        + "due_date TEXT NOT NULL, "
                        + "category TEXT NOT NULL, "
                        + "is_paid BOOLEAN DEFAULT FALSE, "
                        + "reminder_days INTEGER DEFAULT 7"
                        + ");";
                stmt.execute(createBillRemindersTable);
            }
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}