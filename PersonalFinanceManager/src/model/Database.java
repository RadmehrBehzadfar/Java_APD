package model;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // Use absolute path to ensure database is always in the same location
    private static final String DB_FILE = "personal_finance.db";
    private static String DB_URL = null;

    // Keep a single connection instance (Singleton pattern)
    private static Connection connection = null;

    private static String getDbUrl() {
        if (DB_URL == null) {
            // Try to use the project root directory, fallback to current directory
            File dbFile = new File(DB_FILE);
            // If running from PortablePersonalFinance, go up one level
            File currentDir = new File(System.getProperty("user.dir"));
            if (currentDir.getName().equals("PortablePersonalFinance")) {
                dbFile = new File(currentDir.getParentFile(), DB_FILE);
            }
            DB_URL = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        }
        return DB_URL;
    }

    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Explicitly load the SQLite driver
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException e) {
                    throw new SQLException("SQLite JDBC driver not found. Make sure sqlite-jdbc.jar is in the classpath.", e);
                }
                String dbUrl = getDbUrl();
                System.out.println("Connecting to database: " + dbUrl);
                connection = DriverManager.getConnection(dbUrl);
                // Enable foreign keys and set other useful pragmas
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute("PRAGMA foreign_keys = ON");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
            // Reset connection on error
            connection = null;
            throw e;
        }
        return connection;
    }

    // Insert Transaction
    public static void insertTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions(date, amount, category, description, type) VALUES(?,?,?,?,?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getDate().toString());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setString(3, transaction.getCategory() != null ? transaction.getCategory() : "");
            pstmt.setString(4, transaction.getDescription() != null ? transaction.getDescription() : "");
            pstmt.setString(5, transaction.getType() != null ? transaction.getType() : "expense");
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert transaction - no rows affected");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting transaction: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Update Transaction
    public static void updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET date=?, amount=?, category=?, description=?, type=? WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getDate().toString());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setString(3, transaction.getCategory());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setString(5, transaction.getType());
            pstmt.setInt(6, transaction.getId());
            pstmt.executeUpdate();
        }
    }

    // Delete Transaction
    public static void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.executeUpdate();
        }
    }

    // Get All Transactions
    public static List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setDate(LocalDate.parse(rs.getString("date")));
                t.setAmount(rs.getDouble("amount"));
                t.setCategory(rs.getString("category"));
                t.setDescription(rs.getString("description"));
                t.setType(rs.getString("type"));
                list.add(t);
            }
        }
        return list;
    }

    // Insert Budget
    public static void insertBudget(Budget budget) throws SQLException {
        String sql = "INSERT INTO budgets(category, limit_amount) VALUES(?,?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, budget.getCategory());
            pstmt.setDouble(2, budget.getLimit());
            pstmt.executeUpdate();
        }
    }

    // Update Budget
    public static void updateBudget(Budget budget) throws SQLException {
        String sql = "UPDATE budgets SET category=?, limit_amount=? WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, budget.getCategory());
            pstmt.setDouble(2, budget.getLimit());
            pstmt.setInt(3, budget.getId());
            pstmt.executeUpdate();
        }
    }

    // Delete Budget
    public static void deleteBudget(int budgetId) throws SQLException {
        String sql = "DELETE FROM budgets WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetId);
            pstmt.executeUpdate();
        }
    }

    // Get All Budgets
    public static List<Budget> getAllBudgets() throws SQLException {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budgets";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Budget b = new Budget();
                b.setId(rs.getInt("id"));
                b.setCategory(rs.getString("category"));
                b.setLimit(rs.getDouble("limit_amount"));
                list.add(b);
            }
        }
        return list;
    }

    // Check if a category's spending exceeds its budget
    public static boolean isOverBudget(String category) throws SQLException {
        // Get total expenses for the category
        String expenseSql = "SELECT SUM(amount) as total_expenses FROM transactions WHERE category=? AND type='expense'";
        double totalExpenses = 0;
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(expenseSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalExpenses = rs.getDouble("total_expenses");
            }
        }

        // Get the budget limit
        String budgetSql = "SELECT limit_amount FROM budgets WHERE category=?";
        double limit = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(budgetSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                limit = rs.getDouble("limit_amount");
            }
        }

        return limit > 0 && totalExpenses > limit;
    }

    // New helper: Get total expenses for a given category
    public static double getTotalExpenses(String category) throws SQLException {
        String expenseSql = "SELECT SUM(amount) as total_expenses FROM transactions WHERE category=? AND type='expense'";
        double totalExpenses = 0;
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(expenseSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalExpenses = rs.getDouble("total_expenses");
            }
        }
        return totalExpenses;
    }

    // New helper: Get budget limit for a given category
    public static double getBudgetLimit(String category) throws SQLException {
        String budgetSql = "SELECT limit_amount FROM budgets WHERE category=?";
        double limit = 0;
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(budgetSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                limit = rs.getDouble("limit_amount");
            }
        }
        return limit;
    }

    // Get all unique categories from transactions
    public static List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM transactions ORDER BY category";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    // Bill Reminder methods
    public static void insertBillReminder(BillReminder bill) throws SQLException {
        String sql = "INSERT INTO bill_reminders(name, description, amount, due_date, category, is_paid, reminder_days) VALUES(?,?,?,?,?,?,?)";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bill.getName());
            pstmt.setString(2, bill.getDescription());
            pstmt.setDouble(3, bill.getAmount());
            pstmt.setString(4, bill.getDueDate().toString());
            pstmt.setString(5, bill.getCategory());
            pstmt.setBoolean(6, bill.isPaid());
            pstmt.setInt(7, bill.getReminderDays());
            pstmt.executeUpdate();
        }
    }

    public static void updateBillReminder(BillReminder bill) throws SQLException {
        String sql = "UPDATE bill_reminders SET name=?, description=?, amount=?, due_date=?, category=?, is_paid=?, reminder_days=? WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bill.getName());
            pstmt.setString(2, bill.getDescription());
            pstmt.setDouble(3, bill.getAmount());
            pstmt.setString(4, bill.getDueDate().toString());
            pstmt.setString(5, bill.getCategory());
            pstmt.setBoolean(6, bill.isPaid());
            pstmt.setInt(7, bill.getReminderDays());
            pstmt.setInt(8, bill.getId());
            pstmt.executeUpdate();
        }
    }

    public static void deleteBillReminder(int billId) throws SQLException {
        String sql = "DELETE FROM bill_reminders WHERE id=?";
        Connection conn = getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, billId);
            pstmt.executeUpdate();
        }
    }

    public static List<BillReminder> getAllBillReminders() throws SQLException {
        List<BillReminder> list = new ArrayList<>();
        String sql = "SELECT * FROM bill_reminders ORDER BY due_date ASC";
        Connection conn = getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BillReminder bill = new BillReminder();
                bill.setId(rs.getInt("id"));
                bill.setName(rs.getString("name"));
                bill.setDescription(rs.getString("description"));
                bill.setAmount(rs.getDouble("amount"));
                bill.setDueDate(LocalDate.parse(rs.getString("due_date")));
                bill.setCategory(rs.getString("category"));
                bill.setPaid(rs.getBoolean("is_paid"));
                bill.setReminderDays(rs.getInt("reminder_days"));
                list.add(bill);
            }
        }
        return list;
    }
}