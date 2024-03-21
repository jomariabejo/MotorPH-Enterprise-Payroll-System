package com.jomariabejo.motorph;

import com.jomariabejo.motorph.view.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private Stage loginStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = primaryStage;
        openLoginStage();
    }

    private void openLoginStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("MotorPH Payroll System");
            loginStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainLoginStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 768);
        ViewFactory mainController =fxmlLoader.getController();
        mainController.setMainApp(this);
        primaryStage.setResizable(true);
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch();
    }
}