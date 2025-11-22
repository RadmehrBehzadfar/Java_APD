/**********************************************
Workshop #02
Course: APD545 - Winter
Last Name: BEHZADFAR
First Name: RADMEHR
ID: 148786221
Section:NDD
This assignment represents my own work in accordance with Seneca Academic Policy.
Signature: RadmehrBehzadfar
Date:2025-02-11
**********************************************/
package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlLocation = getClass().getResource("layout.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("Cannot find layout.fxml! Make sure it's inside the 'application' package.");
            }

            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root, 700, 850);
            
            // Apply CSS stylesheet
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            primaryStage.setTitle("Car Loan Payment Calculator");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(650);
            primaryStage.setMinHeight(750);
            primaryStage.setResizable(true);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}