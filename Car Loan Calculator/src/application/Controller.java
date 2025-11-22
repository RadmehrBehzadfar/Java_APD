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
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.NumberFormat;
import java.util.Locale;

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
    @FXML private Label lblLoanPeriod;
    @FXML private Label lblErrorMessage;
    @FXML private Label lblSuccessMessage;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    @FXML
    public void initialize() {
        // Initialize ComboBox
        ObservableList<String> paymentOptions = FXCollections.observableArrayList("Monthly", "Bi-Weekly", "Weekly");
        cmbFrequency.setItems(paymentOptions);
        cmbFrequency.setValue("Monthly"); // Default selection

        // Slider with real-time value display
        sldLoanPeriod.valueProperty().addListener((observable, oldValue, newValue) -> {
            int roundedValue = (int) Math.round(newValue.doubleValue() / 12) * 12;
            sldLoanPeriod.setValue(roundedValue);
            updateLoanPeriodLabel(roundedValue);
        });
        updateLoanPeriodLabel(12); // Initial value

        // Real-time input validation and formatting
        setupInputValidation();
        
        // Explicitly set Event Handlers for Buttons
        btnCalculate.setOnAction(event -> calculatePayment());
        btnClear.setOnAction(event -> clearFields());
        
        // Enter key support for calculation
        txtPrice.setOnAction(event -> calculatePayment());
        txtDownPayment.setOnAction(event -> calculatePayment());
        txtInterestRate.setOnAction(event -> calculatePayment());
    }
    
    private void updateLoanPeriodLabel(int months) {
        if (lblLoanPeriod == null) return; // Safety check
        int years = months / 12;
        if (years > 0) {
            lblLoanPeriod.setText(months + " months\n(" + years + " year" + (years > 1 ? "s" : "") + ")");
        } else {
            lblLoanPeriod.setText(months + " months");
        }
    }
    
    private void setupInputValidation() {
        // Price field validation
        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            validateNumericField(txtPrice, newValue);
            clearMessages();
        });
        
        // Down payment field validation
        txtDownPayment.textProperty().addListener((observable, oldValue, newValue) -> {
            validateNumericField(txtDownPayment, newValue);
            clearMessages();
        });
        
        // Interest rate field validation
        txtInterestRate.textProperty().addListener((observable, oldValue, newValue) -> {
            validateNumericField(txtInterestRate, newValue);
            clearMessages();
        });
        
        // Age of vehicle validation (optional but should be numeric if provided)
        txtAgeOfVehicle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*\\.?\\d*")) {
                txtAgeOfVehicle.setText(oldValue);
            }
        });
    }
    
    private void validateNumericField(TextField field, String value) {
        if (value.isEmpty()) {
            field.getStyleClass().removeAll("error");
            return;
        }
        
        // Allow numbers and one decimal point
        if (!value.matches("\\d*\\.?\\d*")) {
            field.setText(value.replaceAll("[^\\d.]", ""));
            return;
        }
        
        // Remove error styling if valid
        try {
            double num = Double.parseDouble(value);
            if (num < 0) {
                field.getStyleClass().add("error");
            } else {
                field.getStyleClass().removeAll("error");
            }
        } catch (NumberFormatException e) {
            field.getStyleClass().add("error");
        }
    }
    
    private void clearMessages() {
        lblErrorMessage.setVisible(false);
        lblErrorMessage.setManaged(false);
        lblSuccessMessage.setVisible(false);
        lblSuccessMessage.setManaged(false);
    }
    
    private void showError(String message) {
        lblErrorMessage.setText(message);
        lblErrorMessage.setVisible(true);
        lblErrorMessage.setManaged(true);
        lblSuccessMessage.setVisible(false);
        lblSuccessMessage.setManaged(false);
    }
    
    private void showSuccess(String message) {
        lblSuccessMessage.setText(message);
        lblSuccessMessage.setVisible(true);
        lblSuccessMessage.setManaged(true);
        lblErrorMessage.setVisible(false);
        lblErrorMessage.setManaged(false);
    }

    @FXML
    private void calculatePayment() {
        try {
            clearMessages();
            
            // Validate Inputs
            if (txtPrice.getText().trim().isEmpty() || txtDownPayment.getText().trim().isEmpty() ||
                txtInterestRate.getText().trim().isEmpty()) {
                showError("⚠️ Please fill in all required fields (Price, Down Payment, Interest Rate)");
                txtResult.setText("");
                return;
            }

            double price = Double.parseDouble(txtPrice.getText().trim());
            double downPayment = Double.parseDouble(txtDownPayment.getText().trim());
            double annualRate = Double.parseDouble(txtInterestRate.getText().trim());
            int months = (int) sldLoanPeriod.getValue();
            String frequency = cmbFrequency.getValue();
            
            if (frequency == null || frequency.isEmpty()) {
                showError("⚠️ Please select a payment frequency");
                txtResult.setText("");
                return;
            }

            // Validate values
            if (price <= 0) {
                showError("❌ Vehicle price must be greater than $0");
                txtPrice.getStyleClass().add("error");
                txtResult.setText("");
                return;
            }
            
            if (downPayment < 0) {
                showError("❌ Down payment cannot be negative");
                txtDownPayment.getStyleClass().add("error");
                txtResult.setText("");
                return;
            }
            
            if (downPayment >= price) {
                showError("❌ Down payment must be less than vehicle price");
                txtDownPayment.getStyleClass().add("error");
                txtResult.setText("");
                return;
            }
            
            if (annualRate < 0 || annualRate > 100) {
                showError("❌ Interest rate must be between 0% and 100%");
                txtInterestRate.getStyleClass().add("error");
                txtResult.setText("");
                return;
            }

            double principal = price - downPayment;
            
            if (principal <= 0) {
                showError("❌ Loan amount must be greater than $0");
                txtResult.setText("");
                return;
            }

            // Calculate payment based on frequency
            double payment = 0.0;
            int totalPayments = 0;
            
            switch (frequency) {
                case "Monthly":
                    payment = calculateLoan(principal, annualRate / 12 / 100, months);
                    totalPayments = months;
                    break;
                case "Bi-Weekly":
                    int biWeeklyPayments = (int) (months / 12.0 * 26);
                    payment = calculateLoan(principal, annualRate / 26 / 100, biWeeklyPayments);
                    totalPayments = biWeeklyPayments;
                    break;
                case "Weekly":
                    int weeklyPayments = (int) (months / 12.0 * 52);
                    payment = calculateLoan(principal, annualRate / 52 / 100, weeklyPayments);
                    totalPayments = weeklyPayments;
                    break;
            }

            // Format and display result
            String formattedPayment = currencyFormat.format(payment);
            txtResult.setText(formattedPayment);
            
            // Show success message with additional info
            double totalAmount = payment * totalPayments;
            String successMsg = String.format("✓ Calculated! Total amount: %s over %d %s payments", 
                currencyFormat.format(totalAmount), totalPayments, frequency.toLowerCase());
            showSuccess(successMsg);
            
            // Remove error styling from all fields
            txtPrice.getStyleClass().removeAll("error");
            txtDownPayment.getStyleClass().removeAll("error");
            txtInterestRate.getStyleClass().removeAll("error");
            
        } catch (NumberFormatException e) {
            showError("❌ Invalid input! Please enter valid numbers.");
            txtResult.setText("");
        } catch (Exception e) {
            showError("❌ An error occurred: " + e.getMessage());
            txtResult.setText("");
            e.printStackTrace();
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
        updateLoanPeriodLabel(12);
        clearMessages();
        
        // Remove error styling
        txtPrice.getStyleClass().removeAll("error");
        txtDownPayment.getStyleClass().removeAll("error");
        txtInterestRate.getStyleClass().removeAll("error");
        
        // Focus on first field for better UX
        txtTypeOfVehicle.requestFocus();
    }

    private double calculateLoan(double principal, double rate, int numPayments) {
        if (rate == 0) return principal / numPayments;
        return principal * (rate * Math.pow(1 + rate, numPayments)) /
               (Math.pow(1 + rate, numPayments) - 1);
    }
}