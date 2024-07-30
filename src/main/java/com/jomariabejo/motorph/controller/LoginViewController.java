package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.Launcher;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Setter
@Getter
public class LoginViewController {

    public Stage stage;

    @FXML
    private Button loginBtn;

    @FXML
    private TextField inputUserIdentifier;

    @FXML
    private PasswordField inputPassword;

    @FXML
    public void initialize() {
        // Initialize any necessary components or bindings
        loginBtn.getStyleClass().add(Styles.SUCCESS);
    }

    public void loginClicked(ActionEvent actionEvent) {
        // Handle login action
        Optional<User> userOptional = verifyUser();

        if (userOptional.isPresent()) {
            Launcher.switchToMainView(userOptional.get());
        } else {
            CustomAlert customAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Invalid username/password",
                    "The email or mobile number you entered isn’t connected to an account."
            );
            customAlert.showAndWait();
        }
    }

    private Optional<User> verifyUser() {
        UserService userService = new UserService(new UserRepository());
        String username = inputUserIdentifier.getText();
        String password = inputPassword.getText();
        return userService.fetchUser(username, password);
    }
}
