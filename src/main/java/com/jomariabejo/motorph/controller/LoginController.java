package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.LoginManager;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button registerBtn;

    private final UserRepository userRepository;
    {
        try {
            userRepository = new UserRepository();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {

    }

    public void initManager(final LoginManager loginManager) {
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                Optional<User> verification = authorize();

                if (verification.isPresent()) {
                    User user = verification.get();
                    int userId = user.getUserID();
                    loginManager.authenticated(userId);
                    System.out.println("The user login is = " + userId);
                }
                else {
                    AlertUtility.showErrorAlert("Invalid Credentials", "Login Failed", "We couldn't find an account that matches what you entered. Please verify your credentials and try again.");
                }
            }
        });
    }

    private Optional<User> authorize() {
        try {
            return userRepository.getUserByUsernameANDPassword(usernameField.getText(), passwordField.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleLogin(ActionEvent event) {
        System.out.println("Login");
    }

    public void forgotPasswordClicked(MouseEvent mouseEvent) {
    }
}
