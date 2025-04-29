/**********************************************
Workshop #04
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-03-02
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
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

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
    @FXML private Label totalValueLabel;
    
    private Model model;
    private ObservableList<ItemInCart> cartObservableList = FXCollections.observableArrayList();
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        primaryStage.setTitle("Grocery Cart Application");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }
    
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
            return (selectedItem != null) ? selectedItem.getName() : "";
        }, itemsComboBox.valueProperty()));
        
        unitPriceValueLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Item selectedItem = itemsComboBox.getValue();
            return (selectedItem != null) ? String.format("%.2f", selectedItem.getUnitPrice()) : "";
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}