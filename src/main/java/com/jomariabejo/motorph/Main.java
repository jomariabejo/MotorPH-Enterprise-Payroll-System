package com.jomariabejo.motorph;

import com.jomariabejo.motorph.service.LoginManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Standard entry point for Java applications.
     * It calls launch(args), which starts the JavaFX application.
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(Main.class);
    }

    /**
     * This method is overridden from the Application class.
     * It's called when the JavaFX application starts.
     * It receives a Stage parameter,
     * which represents the primary stage (window) of the application.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        /** A Scene represents the content of the window.  */
        Scene scene = new Scene(new StackPane());

        /**
         * Initializes a LoginManager object, passing the Scene object as a parameter.
         * It is a LoginManager that is responsible for managing the login screen.
         */
        LoginManager loginManager = new LoginManager(scene);
        loginManager.showLoginScreen();
        /**
         * Set the title of the window, and then show the window to the user.
         */
        primaryStage.setScene(scene);
        primaryStage.setTitle("MotorPH Payroll System | V1");
        primaryStage.show();
    }
}