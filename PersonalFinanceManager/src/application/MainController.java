package application;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainController {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        // Load the logo image
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/budget_781760.png"));
            logoImageView.setImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}