package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}