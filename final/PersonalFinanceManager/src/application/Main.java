package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DBInitializer;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        DBInitializer.initialize();

        try {
            Main.primaryStage = primaryStage;

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);

            InputStream iconStream = getClass().getResourceAsStream("/images/budget_781760.png");
            if (iconStream != null) {
                Image appIcon = new Image(iconStream);
                primaryStage.getIcons().add(appIcon);
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Personal Finance Management");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}