package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleObjectProperty;
import model.Budget;
import model.Database;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BudgetController {

    @FXML
    private TableView<Budget> budgetTable;
    @FXML
    private TableColumn<Budget, String> categoryColumn;
    @FXML
    private TableColumn<Budget, Double> limitColumn;

    @FXML
    private TextField categoryField;
    @FXML
    private TextField limitField;

    private ObservableList<Budget> budgetList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getCategory())
        );
        limitColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getLimit())
        );

        budgetTable.setItems(budgetList);

        loadBudgets();
    }

    private void loadBudgets() {
        budgetList.clear();
        try {
            List<Budget> all = Database.getAllBudgets();
            budgetList.addAll(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBudget() {
        String category = categoryField.getText();
        if (category == null || category.trim().isEmpty()) {
            showAlert("Invalid Category", "Category cannot be empty.");
            return;
        }
        double limit;
        try {
            limit = Double.parseDouble(limitField.getText());
            if (limit <= 0) {
                showAlert("Invalid Limit", "Budget limit must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Limit", "Please enter a valid number for budget limit.");
            return;
        }

        Budget b = new Budget();
        b.setCategory(category);
        b.setLimit(limit);

        try {
            Database.insertBudget(b);
            loadBudgets();
            clearFields();
            showAlert("Success", "Budget added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not add budget.");
        }
    }

    @FXML
    private void handleEditBudget() {
        Budget selected = budgetTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a budget to edit.");
            return;
        }

        categoryField.setText(selected.getCategory());
        limitField.setText(String.valueOf(selected.getLimit()));

        // Confirm editing
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Edit Budget");
        confirmAlert.setHeaderText("Editing budget with ID: " + selected.getId());
        confirmAlert.setContentText("Click OK to apply changes.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Validate
            String category = categoryField.getText();
            if (category == null || category.trim().isEmpty()) {
                showAlert("Invalid Category", "Category cannot be empty.");
                return;
            }
            double limit;
            try {
                limit = Double.parseDouble(limitField.getText());
                if (limit <= 0) {
                    showAlert("Invalid Limit", "Budget limit must be positive.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Limit", "Please enter a valid number for budget limit.");
                return;
            }

            selected.setCategory(category);
            selected.setLimit(limit);

            try {
                Database.updateBudget(selected);
                loadBudgets();
                clearFields();
                showAlert("Success", "Budget updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not update budget.");
            }
        }
    }

    @FXML
    private void handleDeleteBudget() {
        Budget selected = budgetTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a budget to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Budget");
        confirmAlert.setHeaderText("Deleting budget with ID: " + selected.getId());
        confirmAlert.setContentText("Are you sure you want to delete this budget?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Database.deleteBudget(selected.getId());
                loadBudgets();
                showAlert("Success", "Budget deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not delete budget.");
            }
        }
    }

    private void clearFields() {
        categoryField.clear();
        limitField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}