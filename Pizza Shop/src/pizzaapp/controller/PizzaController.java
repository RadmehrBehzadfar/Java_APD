package pizzaapp.controller;

import pizzaapp.model.Customer;
import pizzaapp.model.Order;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PizzaController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField quantityField;
    @FXML private RadioButton cheeseRadio;
    @FXML private RadioButton vegetarianRadio;
    @FXML private RadioButton meatLoverRadio;
    @FXML private ComboBox<String> sizeCombo;
    @FXML private TextArea orderSummaryArea;
    @FXML private Button placeOrderButton;
    @FXML private Button clearButton;
    @FXML private Label priceLabel;
    @FXML private Label priceSubLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label averageOrderLabel;
    @FXML private Label statusLabel;

    private ToggleGroup pizzaTypeGroup;
    private List<Order> orderList = new ArrayList<>();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private double totalRevenue = 0.0;

    @FXML
    public void initialize() {
        setupPizzaTypeGroup();
        setupSizeCombo();
        setupEventHandlers();
        setupRealTimeValidation();
        updatePriceDisplay();
        updateStatistics();
    }

    private void setupPizzaTypeGroup() {
        pizzaTypeGroup = new ToggleGroup();
        cheeseRadio.setToggleGroup(pizzaTypeGroup);
        vegetarianRadio.setToggleGroup(pizzaTypeGroup);
        meatLoverRadio.setToggleGroup(pizzaTypeGroup);
        cheeseRadio.setSelected(true);
        
        // Add listeners for real-time price updates
        pizzaTypeGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> updatePriceDisplay());
    }

    private void setupSizeCombo() {
        sizeCombo.getItems().addAll("Small", "Medium", "Large");
        sizeCombo.getSelectionModel().selectFirst();
        sizeCombo.valueProperty().addListener((obs, oldVal, newVal) -> updatePriceDisplay());
    }

    private void setupEventHandlers() {
        placeOrderButton.setOnAction(e -> placeOrder());
        clearButton.setOnAction(e -> clearForm());
        
        // Add hover effects
        placeOrderButton.setOnMouseEntered(e -> animateButton(placeOrderButton, 1.05));
        placeOrderButton.setOnMouseExited(e -> animateButton(placeOrderButton, 1.0));
        clearButton.setOnMouseEntered(e -> animateButton(clearButton, 1.05));
        clearButton.setOnMouseExited(e -> animateButton(clearButton, 1.0));
    }

    private void setupRealTimeValidation() {
        // Real-time quantity validation
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            updatePriceDisplay();
            validateQuantityField();
        });
        
        // Real-time name validation
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateTextField(nameField, newVal != null && !newVal.trim().isEmpty());
        });
        
        // Real-time phone validation
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateTextField(phoneField, newVal != null && !newVal.trim().isEmpty());
        });
    }

    private void validateQuantityField() {
        String text = quantityField.getText().trim();
        if (text.isEmpty()) {
            removeErrorStyle(quantityField);
            return;
        }
        
        try {
            int qty = Integer.parseInt(text);
            if (qty > 0 && qty <= 100) {
                removeErrorStyle(quantityField);
            } else {
                applyErrorStyle(quantityField);
            }
        } catch (NumberFormatException e) {
            applyErrorStyle(quantityField);
        }
    }

    private void validateTextField(TextField field, boolean isValid) {
        if (isValid) {
            removeErrorStyle(field);
        } else if (!field.getText().trim().isEmpty()) {
            applyErrorStyle(field);
        }
    }

    private void applyErrorStyle(TextField field) {
        field.getStyleClass().add("error");
        if (!field.getStyleClass().contains("text-field:error")) {
            field.setStyle(field.getStyle() + "-fx-border-color: #e74c3c; -fx-background-color: #ffeaea;");
        }
    }

    private void removeErrorStyle(TextField field) {
        field.getStyleClass().remove("error");
        field.setStyle(null);
    }

    private void updatePriceDisplay() {
        try {
            String pizzaType = getSelectedPizzaType();
            String pizzaSize = sizeCombo.getValue();
            String qtyText = quantityField.getText().trim();
            
            if (pizzaSize == null || qtyText.isEmpty()) {
                priceLabel.setText(currencyFormat.format(0.0));
                priceSubLabel.setText("Select options to see price");
                return;
            }
            
            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                priceLabel.setText(currencyFormat.format(0.0));
                priceSubLabel.setText("Quantity must be greater than 0");
                return;
            }
            
            // Create temporary order to calculate price
            Customer tempCustomer = new Customer("", "");
            Order tempOrder = new Order(tempCustomer, pizzaType, pizzaSize, quantity);
            
            double subtotal = tempOrder.calculateSubtotal();
            double total = tempOrder.calculateTotal();
            double tax = total - subtotal;
            
            priceLabel.setText(currencyFormat.format(total));
            priceSubLabel.setText(String.format("Subtotal: %s | Tax (13%%): %s", 
                currencyFormat.format(subtotal), currencyFormat.format(tax)));
            
            // Animate price change
            animatePriceUpdate();
            
        } catch (NumberFormatException e) {
            priceLabel.setText(currencyFormat.format(0.0));
            priceSubLabel.setText("Enter valid quantity");
        }
    }

    private String getSelectedPizzaType() {
        RadioButton selected = (RadioButton) pizzaTypeGroup.getSelectedToggle();
        return selected != null ? selected.getText().replaceAll("[^a-zA-Z ]", "").trim() : "Cheese";
    }

    private void animatePriceUpdate() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), priceLabel);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.1);
        scale.setToY(1.1);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void animateButton(Button button, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), button);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    private void placeOrder() {
        // Validate inputs
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String qtyText = quantityField.getText().trim();
        
        // Clear previous status
        hideStatusMessage();
        
        // Validation
        if (name.isEmpty()) {
            showError("Please enter your name.");
            applyErrorStyle(nameField);
            return;
        }
        
        if (phone.isEmpty()) {
            showError("Please enter your phone number.");
            applyErrorStyle(phoneField);
            return;
        }
        
        if (qtyText.isEmpty()) {
            showError("Please enter a quantity.");
            applyErrorStyle(quantityField);
            return;
        }
        
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                showError("Quantity must be greater than 0.");
                applyErrorStyle(quantityField);
                return;
            }
            if (quantity > 100) {
                showError("Maximum quantity is 100 pizzas per order.");
                applyErrorStyle(quantityField);
                return;
            }
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid number.");
            applyErrorStyle(quantityField);
            return;
        }
        
        // Create order
        String pizzaType = getSelectedPizzaType();
        String pizzaSize = sizeCombo.getValue();
        
        Customer customer = new Customer(name, phone);
        Order order = new Order(customer, pizzaType, pizzaSize, quantity);
        orderList.add(order);
        
        // Update revenue
        totalRevenue += order.calculateTotal();
        
        // Display order summary
        displayOrderSummary(order);
        
        // Update statistics
        updateStatistics();
        
        // Show success message
        showSuccessMessage("✅ Order placed successfully! Thank you for your order.");
        
        // Animate order button
        animateOrderSuccess();
    }

    private void displayOrderSummary(Order order) {
        double subtotal = order.calculateSubtotal();
        double tax = order.calculateTotal() - subtotal;
        double total = order.calculateTotal();
        
        StringBuilder summary = new StringBuilder();
        summary.append("═══════════════════════════════════════\n");
        summary.append("           ORDER CONFIRMATION           \n");
        summary.append("═══════════════════════════════════════\n\n");
        summary.append("CUSTOMER INFORMATION:\n");
        summary.append("  Name:  ").append(order.getCustomer().getName()).append("\n");
        summary.append("  Phone: ").append(order.getCustomer().getPhone()).append("\n\n");
        summary.append("ORDER DETAILS:\n");
        summary.append("  Pizza Type: ").append(order.getPizzaType()).append("\n");
        summary.append("  Size:       ").append(order.getPizzaSize()).append("\n");
        summary.append("  Quantity:   ").append(order.getQuantity()).append("\n\n");
        summary.append("PRICING:\n");
        summary.append("  Subtotal:   ").append(currencyFormat.format(subtotal)).append("\n");
        summary.append("  Tax (13%):  ").append(currencyFormat.format(tax)).append("\n");
        summary.append("  ───────────────────────────────\n");
        summary.append("  TOTAL:      ").append(currencyFormat.format(total)).append("\n");
        summary.append("═══════════════════════════════════════\n");
        summary.append("Order #").append(orderList.size()).append(" | Thank you!");
        
        orderSummaryArea.setText(summary.toString());
        
        // Animate text area appearance
        FadeTransition fade = new FadeTransition(Duration.millis(500), orderSummaryArea);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void updateStatistics() {
        int totalOrders = orderList.size();
        totalOrdersLabel.setText(String.valueOf(totalOrders));
        
        totalRevenueLabel.setText(currencyFormat.format(totalRevenue));
        
        double average = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;
        averageOrderLabel.setText(currencyFormat.format(average));
    }

    private void showSuccessMessage(String message) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().clear();
        statusLabel.getStyleClass().add("success-message");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);
        
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), statusLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        // Auto-hide after 5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> hideStatusMessage());
        pause.play();
    }

    private void hideStatusMessage() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), statusLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        fadeOut.play();
    }

    private void animateOrderSuccess() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), placeOrderButton);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(0.95);
        scale.setToY(0.95);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        quantityField.clear();
        cheeseRadio.setSelected(true);
        sizeCombo.getSelectionModel().selectFirst();
        orderSummaryArea.clear();
        
        // Remove error styles
        removeErrorStyle(nameField);
        removeErrorStyle(phoneField);
        removeErrorStyle(quantityField);
        
        // Hide status message
        hideStatusMessage();
        
        // Reset price display
        updatePriceDisplay();
        
        // Animate clear
        FadeTransition fade = new FadeTransition(Duration.millis(300), orderSummaryArea);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Please correct the following:");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
