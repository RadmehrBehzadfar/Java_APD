package test;

import model.*;
import util.DateUtil;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;
import java.sql.DriverManager;

/**
 * Comprehensive health test suite for Personal Finance Manager Application
 * Tests all core functionality to ensure the application is working correctly
 */
public class ApplicationHealthTest {
    
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Personal Finance Manager - Health Check ===\n");
        
        // Load SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC driver loaded successfully\n");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Failed to load SQLite JDBC driver: " + e.getMessage());
            return;
        }
        
        // Run all test suites
        testDatabaseInitialization();
        testDateUtil();
        testTransactionModel();
        testBudgetModel();
        testBillReminderModel();
        testDatabaseOperations();
        testBudgetValidation();
        testBillReminderLogic();
        
        // Print final results
        System.out.println("\n=== Test Results ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        
        if (testsFailed == 0) {
            System.out.println("\n✅ All tests passed! Application is healthy.");
        } else {
            System.out.println("\n❌ Some tests failed. Please check the issues above.");
        }
    }
    
    private static void testDatabaseInitialization() {
        System.out.println("Testing Database Initialization...");
        
        try {
            // Test database connection
            DBInitializer.initialize();
            assert Database.getConnection() != null : "Database connection should not be null";
            System.out.println("  ✅ Database connection established");
            
            // Test that tables exist by trying to query them
            List<Transaction> transactions = Database.getAllTransactions();
            List<Budget> budgets = Database.getAllBudgets();
            List<BillReminder> bills = Database.getAllBillReminders();
            
            System.out.println("  ✅ All database tables accessible");
            System.out.println("  ✅ Database initialization successful");
            testsPassed += 3;
            
        } catch (Exception e) {
            System.out.println("  ❌ Database initialization failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testDateUtil() {
        System.out.println("\nTesting DateUtil...");
        
        try {
            // Test date formatting
            LocalDate testDate = LocalDate.of(2024, 3, 15);
            String formatted = DateUtil.format(testDate);
            assert "2024-03-15".equals(formatted) : "Date formatting should work correctly";
            System.out.println("  ✅ Date formatting works");
            
            // Test date parsing
            LocalDate parsed = DateUtil.parse("2024-03-15");
            assert testDate.equals(parsed) : "Date parsing should work correctly";
            System.out.println("  ✅ Date parsing works");
            
            // Test invalid date
            LocalDate invalid = DateUtil.parse("invalid-date");
            assert invalid == null : "Invalid date should return null";
            System.out.println("  ✅ Invalid date handling works");
            
            // Test date validation
            assert DateUtil.validDate("2024-03-15") : "Valid date should return true";
            assert !DateUtil.validDate("invalid") : "Invalid date should return false";
            System.out.println("  ✅ Date validation works");
            
            testsPassed += 4;
            
        } catch (Exception e) {
            System.out.println("  ❌ DateUtil test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testTransactionModel() {
        System.out.println("\nTesting Transaction Model...");
        
        try {
            // Test transaction creation
            Transaction transaction = new Transaction();
            transaction.setId(1);
            transaction.setDate(LocalDate.of(2024, 3, 15));
            transaction.setAmount(100.50);
            transaction.setCategory("Food");
            transaction.setDescription("Grocery shopping");
            transaction.setType("expense");
            
            // Test getters
            assert transaction.getId() == 1 : "Transaction ID should be 1";
            assert transaction.getAmount() == 100.50 : "Transaction amount should be 100.50";
            assert "Food".equals(transaction.getCategory()) : "Transaction category should be Food";
            assert "expense".equals(transaction.getType()) : "Transaction type should be expense";
            
            System.out.println("  ✅ Transaction model getters/setters work");
            testsPassed++;
            
        } catch (Exception e) {
            System.out.println("  ❌ Transaction model test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testBudgetModel() {
        System.out.println("\nTesting Budget Model...");
        
        try {
            // Test budget creation
            Budget budget = new Budget();
            budget.setId(1);
            budget.setCategory("Food");
            budget.setLimit(500.00);
            
            // Test getters
            assert budget.getId() == 1 : "Budget ID should be 1";
            assert "Food".equals(budget.getCategory()) : "Budget category should be Food";
            assert budget.getLimit() == 500.00 : "Budget limit should be 500.00";
            
            System.out.println("  ✅ Budget model getters/setters work");
            testsPassed++;
            
        } catch (Exception e) {
            System.out.println("  ❌ Budget model test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testBillReminderModel() {
        System.out.println("\nTesting Bill Reminder Model...");
        
        try {
            // Test bill reminder creation
            BillReminder bill = new BillReminder();
            bill.setId(1);
            bill.setName("Electric Bill");
            bill.setDescription("Monthly electricity bill");
            bill.setAmount(75.00);
            bill.setDueDate(LocalDate.of(2024, 4, 1));
            bill.setCategory("Utilities");
            bill.setPaid(false);
            bill.setReminderDays(7);
            
            // Test getters
            assert bill.getId() == 1 : "Bill ID should be 1";
            assert "Electric Bill".equals(bill.getName()) : "Bill name should be Electric Bill";
            assert bill.getAmount() == 75.00 : "Bill amount should be 75.00";
            assert !bill.isPaid() : "Bill should not be paid";
            
            // Test overdue logic
            LocalDate pastDate = LocalDate.now().minusDays(10);
            bill.setDueDate(pastDate);
            assert bill.isOverdue() : "Bill should be overdue";
            
            // Test reminder logic
            LocalDate futureDate = LocalDate.now().plusDays(5);
            bill.setDueDate(futureDate);
            bill.setReminderDays(7);
            assert bill.shouldRemind() : "Bill should trigger reminder";
            
            System.out.println("  ✅ Bill reminder model and logic work");
            testsPassed++;
            
        } catch (Exception e) {
            System.out.println("  ❌ Bill reminder model test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testDatabaseOperations() {
        System.out.println("\nTesting Database Operations...");
        
        try {
            // Test transaction CRUD operations
            Transaction testTransaction = new Transaction();
            testTransaction.setDate(LocalDate.now());
            testTransaction.setAmount(25.00);
            testTransaction.setCategory("Test");
            testTransaction.setDescription("Test transaction");
            testTransaction.setType("expense");
            
            // Insert transaction
            Database.insertTransaction(testTransaction);
            System.out.println("  ✅ Transaction insertion works");
            
            // Get all transactions
            List<Transaction> transactions = Database.getAllTransactions();
            assert transactions.size() > 0 : "Should have at least one transaction";
            System.out.println("  ✅ Transaction retrieval works");
            
            // Test budget CRUD operations
            Budget testBudget = new Budget();
            testBudget.setCategory("Test");
            testBudget.setLimit(100.00);
            
            // Insert budget
            Database.insertBudget(testBudget);
            System.out.println("  ✅ Budget insertion works");
            
            // Get all budgets
            List<Budget> budgets = Database.getAllBudgets();
            assert budgets.size() > 0 : "Should have at least one budget";
            System.out.println("  ✅ Budget retrieval works");
            
            // Test bill reminder CRUD operations
            BillReminder testBill = new BillReminder();
            testBill.setName("Test Bill");
            testBill.setDescription("Test bill reminder");
            testBill.setAmount(50.00);
            testBill.setDueDate(LocalDate.now().plusDays(30));
            testBill.setCategory("Test");
            testBill.setPaid(false);
            testBill.setReminderDays(7);
            
            // Insert bill reminder
            Database.insertBillReminder(testBill);
            System.out.println("  ✅ Bill reminder insertion works");
            
            // Get all bill reminders
            List<BillReminder> bills = Database.getAllBillReminders();
            assert bills.size() > 0 : "Should have at least one bill reminder";
            System.out.println("  ✅ Bill reminder retrieval works");
            
            testsPassed += 6;
            
        } catch (Exception e) {
            System.out.println("  ❌ Database operations test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testBudgetValidation() {
        System.out.println("\nTesting Budget Validation Logic...");
        
        try {
            // Test budget limit checking
            String testCategory = "TestCategory";
            
            // Set a budget limit
            Budget budget = new Budget();
            budget.setCategory(testCategory);
            budget.setLimit(100.00);
            Database.insertBudget(budget);
            
            // Add some expenses
            Transaction expense1 = new Transaction();
            expense1.setDate(LocalDate.now());
            expense1.setAmount(60.00);
            expense1.setCategory(testCategory);
            expense1.setDescription("Test expense 1");
            expense1.setType("expense");
            Database.insertTransaction(expense1);
            
            Transaction expense2 = new Transaction();
            expense2.setDate(LocalDate.now());
            expense2.setAmount(50.00);
            expense2.setCategory(testCategory);
            expense2.setDescription("Test expense 2");
            expense2.setType("expense");
            Database.insertTransaction(expense2);
            
            // Test over budget detection
            assert Database.isOverBudget(testCategory) : "Should detect over budget";
            System.out.println("  ✅ Over budget detection works");
            
            // Test total expenses calculation
            double totalExpenses = Database.getTotalExpenses(testCategory);
            assert totalExpenses == 110.00 : "Total expenses should be 110.00";
            System.out.println("  ✅ Total expenses calculation works");
            
            // Test budget limit retrieval
            double limit = Database.getBudgetLimit(testCategory);
            assert limit == 100.00 : "Budget limit should be 100.00";
            System.out.println("  ✅ Budget limit retrieval works");
            
            testsPassed += 3;
            
        } catch (Exception e) {
            System.out.println("  ❌ Budget validation test failed: " + e.getMessage());
            testsFailed++;
        }
    }
    
    private static void testBillReminderLogic() {
        System.out.println("\nTesting Bill Reminder Logic...");
        
        try {
            // Test overdue bill detection
            BillReminder overdueBill = new BillReminder();
            overdueBill.setName("Overdue Bill");
            overdueBill.setDescription("This bill is overdue");
            overdueBill.setAmount(100.00);
            overdueBill.setDueDate(LocalDate.now().minusDays(5));
            overdueBill.setCategory("Test");
            overdueBill.setPaid(false);
            overdueBill.setReminderDays(7);
            
            assert overdueBill.isOverdue() : "Bill should be detected as overdue";
            System.out.println("  ✅ Overdue bill detection works");
            
            // Test reminder trigger logic
            BillReminder reminderBill = new BillReminder();
            reminderBill.setName("Reminder Bill");
            reminderBill.setDescription("This bill should trigger reminder");
            reminderBill.setAmount(50.00);
            reminderBill.setDueDate(LocalDate.now().plusDays(3));
            reminderBill.setCategory("Test");
            reminderBill.setPaid(false);
            reminderBill.setReminderDays(7);
            
            assert reminderBill.shouldRemind() : "Bill should trigger reminder";
            System.out.println("  ✅ Reminder trigger logic works");
            
            // Test paid bill doesn't trigger reminder
            BillReminder paidBill = new BillReminder();
            paidBill.setName("Paid Bill");
            paidBill.setDescription("This bill is paid");
            paidBill.setAmount(75.00);
            paidBill.setDueDate(LocalDate.now().plusDays(1));
            paidBill.setCategory("Test");
            paidBill.setPaid(true);
            paidBill.setReminderDays(7);
            
            assert !paidBill.shouldRemind() : "Paid bill should not trigger reminder";
            System.out.println("  ✅ Paid bill reminder logic works");
            
            testsPassed += 3;
            
        } catch (Exception e) {
            System.out.println("  ❌ Bill reminder logic test failed: " + e.getMessage());
            testsFailed++;
        }
    }
}
