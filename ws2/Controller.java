/**********************************************
Workshop #02
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-02-11
**********************************************/
package application;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Controller {

    @FXML private ComboBox<String> cmbFrequency;
    @FXML private TextField txtTypeOfVehicle;
    @FXML private TextField txtAgeOfVehicle;
    @FXML private TextField txtPrice;
    @FXML private TextField txtDownPayment;
    @FXML private TextField txtInterestRate;
    @FXML private Slider sldLoanPeriod;
    @FXML private TextField txtResult;
    @FXML private Button btnCalculate;
    @FXML private Button btnClear;

    @FXML
    public void initialize() {
        // Initialize ComboBox
        ObservableList<String> paymentOptions = FXCollections.observableArrayList("Monthly", "Bi-Weekly", "Weekly");
        cmbFrequency.setItems(paymentOptions);
        cmbFrequency.setValue("Monthly"); // Default selection

        // Implement ChangeListener for Slider
        sldLoanPeriod.valueProperty().addListener((observable, oldValue, newValue) -> {
            sldLoanPeriod.setValue(Math.round(newValue.doubleValue() / 12) * 12);
        });

        // Explicitly set Event Handlers for Buttons
        btnCalculate.setOnAction(event -> calculatePayment());
        btnClear.setOnAction(event -> clearFields());
    }

    @FXML
    private void calculatePayment() {
        try {
            // Validate Inputs
            if (txtPrice.getText().isEmpty() || txtDownPayment.getText().isEmpty() ||
                txtInterestRate.getText().isEmpty()) {
                txtResult.setText("Please fill all fields!");
                return;
            }

            double price = Double.parseDouble(txtPrice.getText());
            double downPayment = Double.parseDouble(txtDownPayment.getText());
            double annualRate = Double.parseDouble(txtInterestRate.getText());
            int months = (int) sldLoanPeriod.getValue();
            String frequency = cmbFrequency.getValue();
            double principal = price - downPayment;

            if (price <= 0 || downPayment < 0 || annualRate < 0) {
                txtResult.setText("Invalid values entered!");
                return;
            }

            double payment = 0.0;
            switch (frequency) {
                case "Monthly":
                    payment = calculateLoan(principal, annualRate / 12 / 100, months);
                    break;
                case "Bi-Weekly":
                    payment = calculateLoan(principal, annualRate / 26 / 100, months / 12 * 26);
                    break;
                case "Weekly":
                    payment = calculateLoan(principal, annualRate / 52 / 100, months / 12 * 52);
                    break;
            }

            txtResult.setText(String.format("$%.2f", payment));
        } catch (NumberFormatException e) {
            txtResult.setText("Invalid input! Please enter numbers.");
        }
    }

    @FXML
    private void clearFields() {
        txtTypeOfVehicle.clear();
        txtAgeOfVehicle.clear();
        txtPrice.clear();
        txtDownPayment.clear();
        txtInterestRate.clear();
        sldLoanPeriod.setValue(12);
        cmbFrequency.setValue("Monthly");
        txtResult.clear();
    }

    private double calculateLoan(double principal, double rate, int numPayments) {
        if (rate == 0) return principal / numPayments;
        return principal * (rate * Math.pow(1 + rate, numPayments)) /
               (Math.pow(1 + rate, numPayments) - 1);
    }
}