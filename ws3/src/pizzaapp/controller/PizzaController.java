package pizzaapp.controller;

import pizzaapp.model.Customer;
import pizzaapp.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.List;

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

    private ToggleGroup pizzaTypeGroup;
    
    private List<Order> orderList = new ArrayList<>();

    @FXML
    public void initialize() {
        pizzaTypeGroup = new ToggleGroup();
        cheeseRadio.setToggleGroup(pizzaTypeGroup);
        vegetarianRadio.setToggleGroup(pizzaTypeGroup);
        meatLoverRadio.setToggleGroup(pizzaTypeGroup);
        cheeseRadio.setSelected(true);

        sizeCombo.getItems().addAll("Small", "Medium", "Large");
        sizeCombo.getSelectionModel().selectFirst();

        placeOrderButton.setOnAction(e -> placeOrder());
        clearButton.setOnAction(e -> clearForm());
    }

    private void placeOrder() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        RadioButton selectedRadio = (RadioButton) pizzaTypeGroup.getSelectedToggle();
        String pizzaType = (selectedRadio != null) ? selectedRadio.getText() : "Cheese";
        String pizzaSize = sizeCombo.getValue();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            showError("Quantity must be a valid integer.");
            return;
        }

        if (name.isEmpty() || phone.isEmpty()) {
            showError("Name and Phone cannot be empty.");
            return;
        }
        if (quantity <= 0) {
            showError("Quantity must be greater than 0.");
            return;
        }

        Customer customer = new Customer(name, phone);
        Order order = new Order(customer, pizzaType, pizzaSize, quantity);
        orderList.add(order);

        displayOrderSummary(order);
    }

    private void clearForm() {
        nameField.clear();
        phoneField.clear();
        quantityField.clear();
        cheeseRadio.setSelected(true);
        sizeCombo.getSelectionModel().selectFirst();
        orderSummaryArea.clear();
    }

    private void displayOrderSummary(Order order) {
        double subTotal = order.calculateSubtotal();
        double total = order.calculateTotal();

        String summary = String.format(
            "Customer Name: %s%n" +
            "Customer Phone: %s%n" +
            "Pizza Type: %s%n" +
            "Pizza Size: %s%n" +
            "Quantity: %d%n" +
            "Total before tax: $%.2f%n" +
            "Total to be paid: $%.2f",
            order.getCustomer().getName(),
            order.getCustomer().getPhone(),
            order.getPizzaType(),
            order.getPizzaSize(),
            order.getQuantity(),
            subTotal,
            total
        );

        orderSummaryArea.setText(summary);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}