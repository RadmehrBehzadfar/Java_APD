package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import model.Database;
import model.Transaction;
import util.DateUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML
    private TableColumn<Transaction, String> categoryColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private DatePicker datePicker;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> categoryFilterCombo;
    @FXML
    private ComboBox<String> typeFilterCombo;
    @FXML
    private TextField searchField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ChoiceBox<String> typeChoiceBox; // "income" or "expense"

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure table columns
        dateColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getDate())
        );
        categoryColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getCategory())
        );
        amountColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getAmount())
        );
        typeColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getType())
        );
        descriptionColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getDescription())
        );

        transactionsTable.setItems(transactionList);

        // Initialize type choice box
        typeChoiceBox.getItems().addAll("income", "expense");
        typeChoiceBox.setValue("expense");

        // Initialize date picker with current date
        datePicker.setValue(LocalDate.now());

        // Initialize filter controls
        initializeFilters();

        loadTransactions();
    }

    private void loadTransactions() {
        transactionList.clear();
        try {
            List<Transaction> all = Database.getAllTransactions();
            transactionList.addAll(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTransaction() {
        // Validate date
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            showAlert("Invalid Date", "Please select a date.");
            return;
        }
        // Validate amount
        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Amount", "Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid number for amount.");
            return;
        }
        // Validate category
        String category = categoryField.getText();
        if (category == null || category.trim().isEmpty()) {
            showAlert("Invalid Category", "Category cannot be empty.");
            return;
        }
        // Get description and type
        String description = descriptionField.getText();
        String type = typeChoiceBox.getValue();

        Transaction t = new Transaction();
        t.setDate(selectedDate);
        t.setAmount(amount);
        t.setCategory(category);
        t.setDescription(description);
        t.setType(type);

        try {
            Database.insertTransaction(t);
            loadTransactions();
            clearFields();
            showAlert("Success", "Transaction added successfully.");
            // Check budget status for the transaction's category
            checkBudgetNotification(category);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not add transaction.\nError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditTransaction() {
        Transaction selected = transactionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to edit.");
            return;
        }

        // Pre-fill fields
        datePicker.setValue(selected.getDate());
        amountField.setText(String.valueOf(selected.getAmount()));
        categoryField.setText(selected.getCategory());
        descriptionField.setText(selected.getDescription());
        typeChoiceBox.setValue(selected.getType());

        // Confirm editing
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Edit Transaction");
        confirmAlert.setHeaderText("Editing transaction with ID: " + selected.getId());
        confirmAlert.setContentText("Click OK to apply changes.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Validate & update
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate == null) {
                showAlert("Invalid Date", "Please select a date.");
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    showAlert("Invalid Amount", "Amount must be positive.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid number for amount.");
                return;
            }
            String category = categoryField.getText();
            if (category == null || category.trim().isEmpty()) {
                showAlert("Invalid Category", "Category cannot be empty.");
                return;
            }
            String description = descriptionField.getText();
            String type = typeChoiceBox.getValue();

            selected.setDate(selectedDate);
            selected.setAmount(amount);
            selected.setCategory(category);
            selected.setDescription(description);
            selected.setType(type);

            try {
                Database.updateTransaction(selected);
                loadTransactions();
                clearFields();
                showAlert("Success", "Transaction updated successfully.");
                // Check budget status after editing
                checkBudgetNotification(category);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not update transaction.");
            }
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selected = transactionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a transaction to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Transaction");
        confirmAlert.setHeaderText("Deleting transaction with ID: " + selected.getId());
        confirmAlert.setContentText("Are you sure you want to delete this transaction?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Database.deleteTransaction(selected.getId());
                loadTransactions();
                showAlert("Success", "Transaction deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not delete transaction.");
            }
        }
    }

    private void clearFields() {
        datePicker.setValue(LocalDate.now());
        amountField.clear();
        categoryField.clear();
        descriptionField.clear();
        typeChoiceBox.setValue("expense");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // New method to check budget notifications for a given category
    private void checkBudgetNotification(String category) {
        try {
            double totalExpenses = Database.getTotalExpenses(category);
            double limit = Database.getBudgetLimit(category);
            if (limit > 0) {
                double ratio = totalExpenses / limit;
                if (ratio >= 1.0) {
                    showAlert("Budget Alert", "Your spending for " + category + " has exceeded the budget limit!");
                } else if (ratio >= 0.8) {
                    int percent = (int)(ratio * 100);
                    showAlert("Budget Warning", "Warning: Your spending for " + category + " has reached " + percent + "% of your budget.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeFilters() {
        // Initialize type filter
        typeFilterCombo.getItems().addAll("All Types", "income", "expense");
        typeFilterCombo.setValue("All Types");

        // Load categories for filter
        try {
            List<String> categories = Database.getAllCategories();
            categoryFilterCombo.getItems().add("All Categories");
            categoryFilterCombo.getItems().addAll(categories);
            categoryFilterCombo.setValue("All Categories");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set default date range (current month)
        LocalDate now = LocalDate.now();
        fromDatePicker.setValue(now.withDayOfMonth(1));
        toDatePicker.setValue(now.withDayOfMonth(now.lengthOfMonth()));
    }

    @FXML
    private void applyFilters() {
        transactionList.clear();
        try {
            List<Transaction> allTransactions = Database.getAllTransactions();
            
            // Apply filters
            List<Transaction> filteredTransactions = allTransactions.stream()
                .filter(t -> {
                    // Date range filter
                    LocalDate fromDate = fromDatePicker.getValue();
                    LocalDate toDate = toDatePicker.getValue();
                    if (fromDate != null && t.getDate().isBefore(fromDate)) return false;
                    if (toDate != null && t.getDate().isAfter(toDate)) return false;
                    
                    // Category filter
                    String selectedCategory = categoryFilterCombo.getValue();
                    if (selectedCategory != null && !selectedCategory.equals("All Categories") && 
                        !selectedCategory.equals(t.getCategory())) return false;
                    
                    // Type filter
                    String selectedType = typeFilterCombo.getValue();
                    if (selectedType != null && !selectedType.equals("All Types") && 
                        !selectedType.equals(t.getType())) return false;
                    
                    // Search filter
                    String searchText = searchField.getText();
                    if (searchText != null && !searchText.trim().isEmpty()) {
                        String description = t.getDescription() != null ? t.getDescription().toLowerCase() : "";
                        String category = t.getCategory() != null ? t.getCategory().toLowerCase() : "";
                        if (!description.contains(searchText.toLowerCase()) && 
                            !category.contains(searchText.toLowerCase())) return false;
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
            
            transactionList.addAll(filteredTransactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFilters() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        categoryFilterCombo.setValue("All Categories");
        typeFilterCombo.setValue("All Types");
        searchField.clear();
        loadTransactions();
    }

    @FXML
    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Transactions to CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setInitialFileName("transactions_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv");

        Stage stage = (Stage) transactionsTable.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write CSV header
                writer.append("ID,Date,Amount,Category,Description,Type\n");

                // Write transaction data
                for (Transaction transaction : transactionList) {
                    writer.append(String.valueOf(transaction.getId())).append(",");
                    writer.append(transaction.getDate().toString()).append(",");
                    writer.append(String.valueOf(transaction.getAmount())).append(",");
                    writer.append(transaction.getCategory()).append(",");
                    writer.append(transaction.getDescription() != null ? transaction.getDescription() : "").append(",");
                    writer.append(transaction.getType()).append("\n");
                }

                showAlert("Export Successful", "Transactions exported to " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Export Error", "Could not export transactions to CSV file.");
            }
        }
    }
}