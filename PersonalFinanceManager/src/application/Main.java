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
        try {
            DBInitializer.initialize();
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
            e.printStackTrace();
            // Continue anyway - database might already be initialized
        }

        try {
            Main.primaryStage = primaryStage;

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/MainView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Could not find MainView.fxml resource");
            }
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
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            // Show error dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load application");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Unexpected error occurred");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}