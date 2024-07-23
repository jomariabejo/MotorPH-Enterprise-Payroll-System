package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
@Getter
public class LoginViewController implements Initializable {

    public Stage stage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize any necessary components or bindings
    }

    public void loginClicked(ActionEvent actionEvent) {
        // Handle login action
        Launcher.switchToMainView("Helloworld");
    }
}
