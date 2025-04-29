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
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || password.isEmpty()){
            new Alert(AlertType.ERROR, "Please enter username and password.").showAndWait();
            return;
        }
        User user = DBHelper.loginUser(username, password);
        if(user != null){
            new Alert(AlertType.INFORMATION, "Welcome " + user.getUsername() + "!").showAndWait();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
                Parent root = loader.load();
                Cart cartController = loader.getController();
                cartController.setLoggedInUser(user);
                Stage stage = new Stage();
                stage.setTitle("Grocery Cart");
                stage.setScene(new Scene(root));
                stage.show();
                ((Stage)usernameField.getScene().getWindow()).close();
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            new Alert(AlertType.ERROR, "Invalid credentials.").showAndWait();
        }
    }

    @FXML
    private void openRegister(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Register");
            stage.setScene(new Scene(root));
            stage.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}