package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

@Setter
@Getter
public class LoginViewController {

    public Stage stage;

    @FXML
    private Button loginBtn;

    @FXML
    public void initialize() {
        // Initialize any necessary components or bindings
        loginBtn.getStyleClass().add(Styles.SUCCESS);
    }

    public void loginClicked(ActionEvent actionEvent) {
        // Handle login action
        Launcher.switchToMainView("Helloworld");
    }
}
