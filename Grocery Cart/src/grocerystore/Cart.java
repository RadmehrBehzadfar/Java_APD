/**********************************************
Workshop #05
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-16
**********************************************/
package grocerystore;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
public class Cart extends Application {
    @FXML private ComboBox<Item> itemsComboBox;
    @FXML private Slider quantitySlider;
    @FXML private Label purchaseUnitsLabel;
    @FXML private Label unitValueLabel;
    @FXML private Label unitPriceValueLabel;
    @FXML private TableView<Item> itemsTableView;
    @FXML private TableColumn<Item, String> itemNameColumn;
    @FXML private TableColumn<Item, Number> unitPriceColumn;
    @FXML private TableView<ItemInCart> cartTableView;
    @FXML private TableColumn<ItemInCart, String> cartItemNameColumn;
    @FXML private TableColumn<ItemInCart, Number> cartUnitPriceColumn;
    @FXML private TableColumn<ItemInCart, Number> cartQuantityColumn;
    @FXML private TableColumn<ItemInCart, Number> cartTotalPriceColumn;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button saveCartButton;
    @FXML private Button loadCartButton;
    @FXML private Button checkOutButton;
    @FXML private Label totalValueLabel;
    private Model model;
    private ObservableList<ItemInCart> cartObservableList = FXCollections.observableArrayList();
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("🛒 Grocery Cart Application - Professional Shopping Experience");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    @FXML
    private void initialize() {
        model = new Model();
        java.io.InputStream csvStream = getClass().getResourceAsStream("/ItemsMaster.csv");
        if (csvStream != null) {
            model.loadData(csvStream);
        } else {
            System.err.println("Could not load ItemsMaster.csv");
        }
        setupAction();
    }
    public void setupAction() {
        // Setup ComboBox
        itemsComboBox.setItems(model.getItemsObservableList());
        itemsComboBox.getSelectionModel().clearSelection();
        itemsComboBox.setPromptText("Select an item...");
        itemsComboBox.setTooltip(new Tooltip("Choose an item from the list to add to your cart"));
        
        // Setup Slider
        quantitySlider.setShowTickLabels(true);
        quantitySlider.setShowTickMarks(true);
        quantitySlider.setMajorTickUnit(5);
        quantitySlider.setMinorTickCount(1);
        quantitySlider.setTooltip(new Tooltip("Adjust the quantity you want to purchase (1-20)"));
        
        // Setup Labels with formatting
        purchaseUnitsLabel.textProperty().bind(Bindings.format("%.0f", quantitySlider.valueProperty()));
        unitValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Item selectedItem = itemsComboBox.getValue();
            return selectedItem != null ? selectedItem.getName() : "None";
        }, itemsComboBox.valueProperty()));
        unitPriceValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Item selectedItem = itemsComboBox.getValue();
            return selectedItem != null ? String.format("$%.2f", selectedItem.getUnitPrice()) : "$0.00";
        }, itemsComboBox.valueProperty()));
        
        // Setup Table Columns with formatting
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        unitPriceColumn.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());
        unitPriceColumn.setCellFactory(column -> new TableCell<Item, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item.doubleValue()));
                }
            }
        });
        itemsTableView.setItems(model.getItemsObservableList());
        itemsTableView.setTooltip(new Tooltip("Double-click an item to select it"));
        
        // Setup Cart Table
        cartTableView.setItems(cartObservableList);
        cartItemNameColumn.setCellValueFactory(data -> data.getValue().itemNameProperty());
        cartUnitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        cartUnitPriceColumn.setCellFactory(column -> new TableCell<ItemInCart, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item.doubleValue()));
                }
            }
        });
        cartQuantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        cartTotalPriceColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty());
        cartTotalPriceColumn.setCellFactory(column -> new TableCell<ItemInCart, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item.doubleValue()));
                }
            }
        });
        cartTableView.setTooltip(new Tooltip("Select an item to remove or view details"));
        
        // Setup Tooltips for buttons
        addButton.setTooltip(new Tooltip("Add the selected item to your shopping cart"));
        removeButton.setTooltip(new Tooltip("Remove the selected item from your cart"));
        saveCartButton.setTooltip(new Tooltip("Save your current cart for later"));
        loadCartButton.setTooltip(new Tooltip("Load a previously saved cart"));
        checkOutButton.setTooltip(new Tooltip("Complete your purchase and clear the cart"));
        addButton.setOnAction(event -> {
            Item selectedItem = itemsComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                showAlert(AlertType.WARNING, "No Item Selected", 
                    "Please select an item from the dropdown before adding to cart.");
                return;
            }
            
            int quantity = (int) quantitySlider.getValue();
            if (quantity <= 0) {
                showAlert(AlertType.WARNING, "Invalid Quantity", 
                    "Please select a quantity greater than 0.");
                return;
            }
            
            // Check if item already exists in cart and update quantity instead
            boolean itemExists = false;
            for (ItemInCart item : cartObservableList) {
                if (item.getItemName().equals(selectedItem.getName())) {
                    int newQuantity = item.getQuantity() + quantity;
                    item.setQuantity(newQuantity);
                    itemExists = true;
                    showAlert(AlertType.INFORMATION, "Item Updated", 
                        String.format("Updated quantity of %s. New total: %d", 
                            selectedItem.getName(), newQuantity));
                    break;
                }
            }
            
            if (!itemExists) {
                ItemInCart itemInCart = new ItemInCart(selectedItem.getName(), 
                    selectedItem.getUnitPrice(), quantity);
                cartObservableList.add(itemInCart);
                showAlert(AlertType.INFORMATION, "Item Added", 
                    String.format("Added %d x %s to your cart!", quantity, selectedItem.getName()));
            }
            
            // Reset selection
            itemsComboBox.getSelectionModel().clearSelection();
            quantitySlider.setValue(1);
        });
        DoubleBinding totalBinding = Bindings.createDoubleBinding(() ->
            cartObservableList.stream().mapToDouble(ItemInCart::getTotalPrice).sum(),
            cartObservableList
        );
        totalValueLabel.textProperty().bind(Bindings.createStringBinding(() -> 
            String.format("$%.2f", totalBinding.get()),
            totalBinding
        ));
        cartTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                for (int i = 0; i < model.getItemsObservableList().size(); i++) {
                    if (model.getItemsObservableList().get(i).getName().equals(newSelection.getItemName())) {
                        itemsComboBox.getSelectionModel().select(i);
                        quantitySlider.setValue(newSelection.getQuantity());
                        break;
                    }
                }
            }
        });
        removeButton.setOnAction(event -> {
            int selectedIndex = cartTableView.getSelectionModel().getSelectedIndex();
            if (selectedIndex < 0) {
                showAlert(AlertType.WARNING, "No Item Selected", 
                    "Please select an item from your cart to remove.");
                return;
            }
            
            ItemInCart itemToRemove = cartObservableList.get(selectedIndex);
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Remove Item");
            confirmAlert.setHeaderText("Confirm Removal");
            confirmAlert.setContentText(String.format("Are you sure you want to remove %s from your cart?", 
                itemToRemove.getItemName()));
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cartObservableList.remove(selectedIndex);
                showAlert(AlertType.INFORMATION, "Item Removed", 
                    String.format("%s has been removed from your cart.", itemToRemove.getItemName()));
            }
        });
        saveCartButton.setOnAction(event -> {
            if (cartObservableList.isEmpty()) {
                showAlert(AlertType.WARNING, "Empty Cart", 
                    "Your cart is empty. Add some items before saving.");
                return;
            }
            
            CartFileHandler.saveCart(new java.util.ArrayList<>(cartObservableList));
            showAlert(AlertType.INFORMATION, "Cart Saved Successfully", 
                String.format("Your cart with %d item(s) has been saved successfully!", 
                    cartObservableList.size()));
        });
        loadCartButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SavedCart.fxml"));
                Parent root = loader.load();
                SavedCartController controller = loader.getController();
                controller.setMainController(this);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(root));
                stage.setTitle("Load Saved Cart");
                stage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        checkOutButton.setOnAction(event -> {
            if (cartObservableList.isEmpty()) {
                showAlert(AlertType.WARNING, "Empty Cart", 
                    "Your cart is empty. Add some items before checkout.");
                return;
            }
            
            double total = cartObservableList.stream()
                .mapToDouble(ItemInCart::getTotalPrice)
                .sum();
            
            Alert checkoutAlert = new Alert(AlertType.CONFIRMATION);
            checkoutAlert.setTitle("Checkout");
            checkoutAlert.setHeaderText("Complete Your Purchase?");
            checkoutAlert.setContentText(String.format(
                "Total Amount: $%.2f\n\nAre you ready to complete your purchase?",
                total));
            
            Optional<ButtonType> result = checkoutAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int itemCount = cartObservableList.size();
                cartObservableList.clear();
                showAlert(AlertType.INFORMATION, "Purchase Complete!", 
                    String.format("Thank you for your purchase!\n\nTotal: $%.2f\nItems: %d", 
                        total, itemCount));
            }
        });
    }
    public void loadCartData(List<ItemInCart> loadedItems) {
        if (loadedItems == null || loadedItems.isEmpty()) {
            showAlert(AlertType.WARNING, "Empty Cart", 
                "The selected cart is empty.");
            return;
        }
        
        cartObservableList.clear();
        cartObservableList.addAll(loadedItems);
        showAlert(AlertType.INFORMATION, "Cart Loaded", 
            String.format("Successfully loaded cart with %d item(s)!", loadedItems.size()));
    }
    
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}