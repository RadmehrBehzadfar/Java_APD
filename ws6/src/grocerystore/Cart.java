/**********************************************
Workshop #06
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-29
**********************************************/
package grocerystore;
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
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class Cart {
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
    @FXML private Button loadCartFromDBButton;
    @FXML private Button checkOutButton;
    @FXML private Label totalValueLabel;

    private Model model;
    private ObservableList<ItemInCart> cartObservableList = FXCollections.observableArrayList();
    private User loggedInUser;

    @FXML
    private void initialize() {
        model = new Model();
        String csvPath = getClass().getResource("/ItemsMaster.csv").getPath();
        model.loadData(csvPath);
        setupAction();
    }

    public void setupAction() {
        itemsComboBox.setItems(model.getItemsObservableList());
        itemsComboBox.getSelectionModel().clearSelection();

        purchaseUnitsLabel.textProperty().bind(Bindings.format("%.0f", quantitySlider.valueProperty()));
        unitValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Item selectedItem = itemsComboBox.getValue();
            return selectedItem != null ? selectedItem.getName() : "";
        }, itemsComboBox.valueProperty()));
        unitPriceValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Item selectedItem = itemsComboBox.getValue();
            return selectedItem != null ? String.format("%.2f", selectedItem.getUnitPrice()) : "";
        }, itemsComboBox.valueProperty()));

        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        unitPriceColumn.setCellValueFactory(cellData -> cellData.getValue().unitPriceProperty());
        itemsTableView.setItems(model.getItemsObservableList());

        cartTableView.setItems(cartObservableList);
        cartItemNameColumn.setCellValueFactory(data -> data.getValue().itemNameProperty());
        cartUnitPriceColumn.setCellValueFactory(data -> data.getValue().unitPriceProperty());
        cartQuantityColumn.setCellValueFactory(data -> data.getValue().quantityProperty());
        cartTotalPriceColumn.setCellValueFactory(data -> data.getValue().totalPriceProperty());

        addButton.setOnAction(event -> {
            Item selectedItem = itemsComboBox.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int quantity = (int) quantitySlider.getValue();
                ItemInCart itemInCart = new ItemInCart(selectedItem.getName(), selectedItem.getUnitPrice(), quantity);
                cartObservableList.add(itemInCart);
            }
        });

        DoubleBinding totalBinding = Bindings.createDoubleBinding(() ->
            cartObservableList.stream().mapToDouble(ItemInCart::getTotalPrice).sum(),
            cartObservableList
        );
        totalValueLabel.textProperty().bind(Bindings.format("%.2f", totalBinding));

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
            if (selectedIndex >= 0) {
                cartObservableList.remove(selectedIndex);
            }
        });
        
        saveCartButton.setOnAction(event -> {
            boolean successDB = DBHelper.saveCart(new java.util.ArrayList<>(cartObservableList), loggedInUser.getId());
            CartFileHandler.saveCart(new java.util.ArrayList<>(cartObservableList));
            if(successDB){
                new Alert(Alert.AlertType.INFORMATION, "Cart saved to DB and file.").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save cart to DB.").showAndWait();
            }
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
                stage.setTitle("Load Saved Cart (File)");
                stage.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        loadCartFromDBButton.setOnAction(event -> {
            List<ItemInCart> loadedCart = DBHelper.loadCart(loggedInUser.getId());
            if(loadedCart != null){
                loadCartData(loadedCart);
                new Alert(Alert.AlertType.INFORMATION, "Cart loaded from DB.").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "No saved cart found in DB.").showAndWait();
            }
        });

        checkOutButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you done with your groceries?");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    cartObservableList.clear();
                }
            });
        });
    }

    public void loadCartData(List<ItemInCart> loadedItems) {
        cartObservableList.clear();
        cartObservableList.addAll(loadedItems);
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }
}