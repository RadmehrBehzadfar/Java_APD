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
import javafx.stage.Stage;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleRegister(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();
        if(username.isEmpty() || password.isEmpty() || confirm.isEmpty()){
            new Alert(AlertType.ERROR, "All fields are required.").showAndWait();
            return;
        }
        if(!password.equals(confirm)){
            new Alert(AlertType.ERROR, "Passwords do not match.").showAndWait();
            return;
        }
        boolean registered = DBHelper.registerUser(username, password);
        if(registered){
            new Alert(AlertType.INFORMATION, "Registration successful.").showAndWait();
            ((Stage)usernameField.getScene().getWindow()).close();
        } else {
            new Alert(AlertType.ERROR, "Registration failed. Username may already exist.").showAndWait();
        }
    }
}