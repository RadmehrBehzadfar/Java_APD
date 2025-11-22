package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Database;
import model.Transaction;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Label selectedDateLabel;
    @FXML
    private ListView<Transaction> transactionsListView;

    @FXML
    public void initialize() {
        // Initialize date picker with current date
        datePicker.setValue(LocalDate.now());
        
        // Add listener to date picker
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSelectedDateInfo(newValue);
            }
        });
        
        // Load initial data
        updateSelectedDateInfo(LocalDate.now());
    }

    private void updateSelectedDateInfo(LocalDate selectedDate) {
        selectedDateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        
        try {
            List<Transaction> allTransactions = Database.getAllTransactions();
            List<Transaction> dayTransactions = allTransactions.stream()
                .filter(t -> t.getDate().equals(selectedDate))
                .toList();
            
            ObservableList<Transaction> observableList = FXCollections.observableArrayList(dayTransactions);
            transactionsListView.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}