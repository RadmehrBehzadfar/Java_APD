package model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:personal_finance.db";

    // Keep a single connection instance if desired (Singleton pattern)
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    // Insert Transaction
    public static void insertTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions(date, amount, category, description, type) VALUES(?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getDate().toString());
            pstmt.setDouble(2, transaction.getAmount());
            pstmt.setString(3, transaction.getCategory());
            pstmt.setString(4, transaction.getDescription());
            pstmt.setString(5, transaction.getType());
            pstmt.executeUpdate();
        }
    }

    // Update Transaction
    public static void updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET date=?, amount=?, category=?, description=?, type=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.executeUpdate();
        }
    }

    // Get All Transactions
    public static List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, budget.getCategory());
            pstmt.setDouble(2, budget.getLimit());
            pstmt.executeUpdate();
        }
    }

    // Update Budget
    public static void updateBudget(Budget budget) throws SQLException {
        String sql = "UPDATE budgets SET category=?, limit_amount=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, budget.getCategory());
            pstmt.setDouble(2, budget.getLimit());
            pstmt.setInt(3, budget.getId());
            pstmt.executeUpdate();
        }
    }

    // Delete Budget
    public static void deleteBudget(int budgetId) throws SQLException {
        String sql = "DELETE FROM budgets WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, budgetId);
            pstmt.executeUpdate();
        }
    }

    // Get All Budgets
    public static List<Budget> getAllBudgets() throws SQLException {
        List<Budget> list = new ArrayList<>();
        String sql = "SELECT * FROM budgets";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(expenseSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalExpenses = rs.getDouble("total_expenses");
            }
        }

        // Get the budget limit
        String budgetSql = "SELECT limit_amount FROM budgets WHERE category=?";
        double limit = 0;
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(budgetSql)) {
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(expenseSql)) {
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
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(budgetSql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                limit = rs.getDouble("limit_amount");
            }
        }
        return limit;
    }
}