package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Database;
import model.BillReminder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BillRemindersController {

    @FXML
    private TableView<BillReminder> billsTable;
    @FXML
    private TableColumn<BillReminder, String> nameColumn;
    @FXML
    private TableColumn<BillReminder, Double> amountColumn;
    @FXML
    private TableColumn<BillReminder, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<BillReminder, String> categoryColumn;
    @FXML
    private TableColumn<BillReminder, String> statusColumn;
    @FXML
    private TableColumn<BillReminder, Integer> reminderDaysColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField reminderDaysField;
    @FXML
    private TextField descriptionField;

    @FXML
    private VBox overdueAlertBox;
    @FXML
    private ListView<BillReminder> overdueBillsList;
    @FXML
    private VBox upcomingAlertBox;
    @FXML
    private ListView<BillReminder> upcomingBillsList;

    private ObservableList<BillReminder> billList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure table columns
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getName())
        );
        amountColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getAmount())
        );
        dueDateColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getDueDate())
        );
        categoryColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCategory())
        );
        statusColumn.setCellValueFactory(cellData -> {
            BillReminder bill = cellData.getValue();
            String status = bill.isPaid() ? "Paid" : 
                           bill.isOverdue() ? "Overdue" : "Pending";
            return new SimpleStringProperty(status);
        });
        reminderDaysColumn.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getReminderDays())
        );

        billsTable.setItems(billList);

        // Initialize date picker with current date
        dueDatePicker.setValue(LocalDate.now());

        loadBills();
        updateAlerts();
    }

    private void loadBills() {
        billList.clear();
        try {
            List<BillReminder> all = Database.getAllBillReminders();
            billList.addAll(all);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAlerts() {
        // Update overdue bills
        List<BillReminder> overdueBills = billList.stream()
            .filter(BillReminder::isOverdue)
            .collect(Collectors.toList());
        
        if (!overdueBills.isEmpty()) {
            overdueAlertBox.setVisible(true);
            overdueBillsList.setItems(FXCollections.observableArrayList(overdueBills));
        } else {
            overdueAlertBox.setVisible(false);
        }

        // Update upcoming bills (due within reminder period)
        List<BillReminder> upcomingBills = billList.stream()
            .filter(bill -> !bill.isPaid() && bill.shouldRemind() && !bill.isOverdue())
            .collect(Collectors.toList());
        
        if (!upcomingBills.isEmpty()) {
            upcomingAlertBox.setVisible(true);
            upcomingBillsList.setItems(FXCollections.observableArrayList(upcomingBills));
        } else {
            upcomingAlertBox.setVisible(false);
        }
    }

    @FXML
    private void handleAddBill() {
        // Validate inputs
        String name = nameField.getText();
        if (name == null || name.trim().isEmpty()) {
            showAlert("Invalid Input", "Bill name cannot be empty.");
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

        LocalDate dueDate = dueDatePicker.getValue();
        if (dueDate == null) {
            showAlert("Invalid Date", "Please select a due date.");
            return;
        }

        String category = categoryField.getText();
        if (category == null || category.trim().isEmpty()) {
            showAlert("Invalid Category", "Category cannot be empty.");
            return;
        }

        int reminderDays;
        try {
            reminderDays = Integer.parseInt(reminderDaysField.getText());
            if (reminderDays < 0) {
                showAlert("Invalid Reminder Days", "Reminder days must be non-negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Reminder Days", "Please enter a valid number for reminder days.");
            return;
        }

        String description = descriptionField.getText();

        BillReminder bill = new BillReminder();
        bill.setName(name);
        bill.setAmount(amount);
        bill.setDueDate(dueDate);
        bill.setCategory(category);
        bill.setDescription(description);
        bill.setReminderDays(reminderDays);
        bill.setPaid(false);

        try {
            Database.insertBillReminder(bill);
            loadBills();
            clearFields();
            updateAlerts();
            showAlert("Success", "Bill reminder added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not add bill reminder.");
        }
    }

    @FXML
    private void handleEditBill() {
        BillReminder selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a bill to edit.");
            return;
        }

        // Pre-fill fields
        nameField.setText(selected.getName());
        amountField.setText(String.valueOf(selected.getAmount()));
        dueDatePicker.setValue(selected.getDueDate());
        categoryField.setText(selected.getCategory());
        reminderDaysField.setText(String.valueOf(selected.getReminderDays()));
        descriptionField.setText(selected.getDescription());

        // Confirm editing
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Edit Bill");
        confirmAlert.setHeaderText("Editing bill: " + selected.getName());
        confirmAlert.setContentText("Click OK to apply changes.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Validate & update
            String name = nameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showAlert("Invalid Input", "Bill name cannot be empty.");
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

            LocalDate dueDate = dueDatePicker.getValue();
            if (dueDate == null) {
                showAlert("Invalid Date", "Please select a due date.");
                return;
            }

            String category = categoryField.getText();
            if (category == null || category.trim().isEmpty()) {
                showAlert("Invalid Category", "Category cannot be empty.");
                return;
            }

            int reminderDays;
            try {
                reminderDays = Integer.parseInt(reminderDaysField.getText());
                if (reminderDays < 0) {
                    showAlert("Invalid Reminder Days", "Reminder days must be non-negative.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Reminder Days", "Please enter a valid number for reminder days.");
                return;
            }

            String description = descriptionField.getText();

            selected.setName(name);
            selected.setAmount(amount);
            selected.setDueDate(dueDate);
            selected.setCategory(category);
            selected.setDescription(description);
            selected.setReminderDays(reminderDays);

            try {
                Database.updateBillReminder(selected);
                loadBills();
                clearFields();
                updateAlerts();
                showAlert("Success", "Bill reminder updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not update bill reminder.");
            }
        }
    }

    @FXML
    private void handleDeleteBill() {
        BillReminder selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a bill to delete.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Bill");
        confirmAlert.setHeaderText("Deleting bill: " + selected.getName());
        confirmAlert.setContentText("Are you sure you want to delete this bill reminder?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Database.deleteBillReminder(selected.getId());
                loadBills();
                updateAlerts();
                showAlert("Success", "Bill reminder deleted successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Database Error", "Could not delete bill reminder.");
            }
        }
    }

    @FXML
    private void handleMarkPaid() {
        BillReminder selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a bill to mark as paid.");
            return;
        }

        selected.setPaid(true);
        try {
            Database.updateBillReminder(selected);
            loadBills();
            updateAlerts();
            showAlert("Success", "Bill marked as paid.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not update bill status.");
        }
    }

    @FXML
    private void handleMarkUnpaid() {
        BillReminder selected = billsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a bill to mark as unpaid.");
            return;
        }

        selected.setPaid(false);
        try {
            Database.updateBillReminder(selected);
            loadBills();
            updateAlerts();
            showAlert("Success", "Bill marked as unpaid.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not update bill status.");
        }
    }

    private void clearFields() {
        nameField.clear();
        amountField.clear();
        dueDatePicker.setValue(LocalDate.now());
        categoryField.clear();
        reminderDaysField.setText("7");
        descriptionField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
