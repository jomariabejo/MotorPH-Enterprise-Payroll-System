package com.jomariabejo.motorph;

import atlantafx.base.theme.PrimerDark;
import com.jomariabejo.motorph.controller.LoginViewController;
import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.PagibigContributionRateRepository;
import com.jomariabejo.motorph.repository.PhilhealthContributionRateRepository;
import com.jomariabejo.motorph.service.PagibigContributionRateService;
import com.jomariabejo.motorph.service.PhilhealthContributionRateService;
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

        // Initialize Hibernate
        HibernateUtil.getSessionFactory();

        // Initialize default contribution rates if they don't exist
        initializeDefaultContributionRates();

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

    public static void switchToMainView(User user) {
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
        controller.setUser(user);
        controller.setEmployee(user.getEmployee());
        controller.initializeUserNavigation();
        controller.displayEmployeeName();

        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage with the main view
        primaryStage.show();
    }

    /**
     * Initialize default contribution rates (Pagibig and Philhealth) if they don't exist in the database.
     * This ensures the application has the necessary rate data for payroll calculations.
     */
    private static void initializeDefaultContributionRates() {
        try {
            // Initialize Pagibig rates
            PagibigContributionRateService pagibigService = new PagibigContributionRateService(new PagibigContributionRateRepository());
            pagibigService.populateStandardRates();
            
            // Initialize Philhealth rates
            PhilhealthContributionRateService philhealthService = new PhilhealthContributionRateService(new PhilhealthContributionRateRepository());
            philhealthService.populateStandardRates();
        } catch (Exception e) {
            // Log error but don't prevent application startup
            System.err.println("Warning: Failed to initialize default contribution rates: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void switchToLoginView() {
        // Load the login view FXML file
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("/com/jomariabejo/motorph/login-view.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Pass the primary stage to the controller
        LoginViewController controller = loader.getController();
        controller.setStage(primaryStage);

        // Clear the input fields
        controller.getInputUserIdentifier().clear();
        controller.getInputPassword().clear();

        // Create a new scene with the loaded FXML file
        Scene scene = new Scene(root);

        // Set the scene on the primary stage
        primaryStage.setScene(scene);

        // Show the primary stage with the login view
        primaryStage.show();
    }
}
