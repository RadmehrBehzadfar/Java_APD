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
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import java.io.File;
import java.util.List;
public class SavedCartController {
    @FXML private ListView<String> savedCartList;
    @FXML private Button loadButton;
    @FXML private Button cancelButton;
    private Cart mainController;
    private List<File> cartFiles;
    @FXML
    private void initialize() {
        cartFiles = CartFileHandler.listSavedCarts();
        if (cartFiles.isEmpty()) {
            savedCartList.getItems().add("No saved carts available");
            loadButton.setDisable(true);
        } else {
            for (File f : cartFiles) {
                // Format the filename to be more user-friendly
                String displayName = formatCartName(f.getName());
                savedCartList.getItems().add(displayName);
            }
        }
        
        // Setup tooltips
        loadButton.setTooltip(new Tooltip("Load the selected saved cart"));
        cancelButton.setTooltip(new Tooltip("Close this dialog without loading"));
    }
    
    private String formatCartName(String filename) {
        // Remove .ser extension and format timestamp if it's a timestamp-based name
        String name = filename.replace(".ser", "");
        try {
            long timestamp = Long.parseLong(name);
            java.util.Date date = new java.util.Date(timestamp);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy 'at' HH:mm");
            return "Cart saved on " + sdf.format(date);
        } catch (NumberFormatException e) {
            return filename;
        }
    }
    @FXML
    private void handleLoad() {
        int index = savedCartList.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= cartFiles.size()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a saved cart to load.");
            alert.showAndWait();
            return;
        }
        
        File file = cartFiles.get(index);
        List<ItemInCart> loadedCart = CartFileHandler.loadCart(file);
        if (loadedCart == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Load Error");
            alert.setHeaderText("Failed to Load Cart");
            alert.setContentText("There was an error loading the selected cart. The file may be corrupted.");
            alert.showAndWait();
            return;
        }
        
        mainController.loadCartData(loadedCart);
        Stage stage = (Stage) loadButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void setMainController(Cart c) {
        mainController = c;
    }
}