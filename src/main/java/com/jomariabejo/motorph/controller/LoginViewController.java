package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.Launcher;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.LoggingUtility;
import com.jomariabejo.motorph.utility.SessionManager;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


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
        
        // Check for saved session and auto-login
        checkAndAutoLogin();
    }
    
    /**
     * Check for saved session and attempt auto-login
     */
    private void checkAndAutoLogin() {
        if (SessionManager.hasSession()) {
            String savedUsername = SessionManager.getSavedUsername();
            String savedPassword = SessionManager.getSavedPassword();
            
            if (savedUsername != null && savedPassword != null) {
                // Pre-fill the form
                inputUserIdentifier.setText(savedUsername);
                inputPassword.setText(savedPassword);
                
                // Attempt auto-login
                Optional<User> userOptional = verifyUser(savedUsername, savedPassword);
                
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    LoggingUtility.logLogin(user);
                    Launcher.switchToMainView(user);
                } else {
                    // Clear invalid session
                    SessionManager.clearSession();
                }
            }
        }
    }
    
    private Optional<User> verifyUser(String username, String password) {
        UserService userService = new UserService(new UserRepository());
        return userService.fetchUser(username, password);
    }

    public void loginClicked(ActionEvent actionEvent) {
        // Handle login action
        Optional<User> userOptional = verifyUser();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LoggingUtility.logLogin(user);
            
            // Save session for auto-login (always remember for now, can add checkbox later)
            SessionManager.saveSession(
                inputUserIdentifier.getText(),
                inputPassword.getText(),
                user.getId(),
                true
            );
            
            Launcher.switchToMainView(user);
        } else {
            displayInvalidCredentials();
        }
    }

    private void displayInvalidCredentials() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Invalid username/password",
                "The email or username you entered isn’t connected to an account."
        );
        customAlert.showAndWait();
    }

    private Optional<User> verifyUser() {
        String username = inputUserIdentifier.getText();
        String password = inputPassword.getText();
        return verifyUser(username, password);
    }

}
