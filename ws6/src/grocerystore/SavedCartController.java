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
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
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
        for (File f : cartFiles) {
            savedCartList.getItems().add(f.getName());
        }
    }

    @FXML
    private void handleLoad() {
        int index = savedCartList.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            File file = cartFiles.get(index);
            List<ItemInCart> loadedCart = CartFileHandler.loadCart(file);
            if (loadedCart != null) {
                mainController.loadCartData(loadedCart);
            }
            Stage stage = (Stage) loadButton.getScene().getWindow();
            stage.close();
        }
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