package com.jomariabejo.motorph;

import atlantafx.base.theme.PrimerDark;
import com.jomariabejo.motorph.controller.LoginViewController;
import com.jomariabejo.motorph.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {

    private static Stage primaryStage; // Store the primary stage

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage; // Store the primary stage for later use

        // Set the default stylesheet for the application
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // Load the login view FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/login-view.fxml"));
        Parent root = loader.load();

        // Pass the primary stage to the controller to access later
        LoginViewController controller = loader.getController();
        controller.setStage(primaryStage);

        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Set the scene on the stage (primary window)
        stage.setScene(scene);
        stage.setTitle("MotorPH V5");

        // Show the stage (primary window)
        stage.show();
    }

    public static void switchToMainView(String username) {
        // Load the main view FXML file
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("/com/jomariabejo/motorph/main-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Pass parameters to the main view controller if necessary
        MainViewController controller = loader.getController();
        controller.initializeData(username); // Example method to initialize data

        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage with the main view
        primaryStage.show();
    }
}
