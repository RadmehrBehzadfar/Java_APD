package pizzaapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PizzaShopApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pizza.fxml"));
        Scene scene = new Scene(loader.load(), 800, 900);
        
        // Set window properties
        primaryStage.setTitle("🍕 Pizza Shop - Order Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(800);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        
        // Show window with fade-in effect
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}