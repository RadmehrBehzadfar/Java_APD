package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import model.Database;
import model.Transaction;
import util.DateUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private TextField dateField;
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

        transactionsTable.setItems(transactionList);

        // Initialize type choice box
        typeChoiceBox.getItems().addAll("income", "expense");
        typeChoiceBox.setValue("expense");

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
        String dateText = dateField.getText();
        if (!DateUtil.validDate(dateText)) {
            showAlert("Invalid Date", "Please enter a valid date (yyyy-MM-dd).");
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
        t.setDate(LocalDate.parse(dateText));
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
            showAlert("Database Error", "Could not add transaction.");
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
        dateField.setText(selected.getDate().toString());
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
            if (!DateUtil.validDate(dateField.getText())) {
                showAlert("Invalid Date", "Please enter a valid date (yyyy-MM-dd).");
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

            selected.setDate(LocalDate.parse(dateField.getText()));
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
        dateField.clear();
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
}