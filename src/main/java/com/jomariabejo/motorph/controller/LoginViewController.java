package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.Launcher;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.model.UserLog;
import com.jomariabejo.motorph.repository.UserLogRepository;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.UserLogService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.DateTimeUtil;
import com.jomariabejo.motorph.utility.NetworkUtils;
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
    }

    public void loginClicked(ActionEvent actionEvent) {
        // Handle login action
        Optional<User> userOptional = verifyUser();

        if (userOptional.isPresent()) {
            saveUserLog(userOptional.get());
            Launcher.switchToMainView(userOptional.get());
        } else {
            displayInvalidCredentials();
        }
    }

    private void displayInvalidCredentials() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Invalid username/password",
                "The email or mobile number you entered isn’t connected to an account."
        );
        customAlert.showAndWait();
    }

    private Optional<User> verifyUser() {
        UserService userService = new UserService(new UserRepository());
        String username = inputUserIdentifier.getText();
        String password = inputPassword.getText();
        return userService.fetchUser(username, password);
    }

    private void saveUserLog(User user) {
        UserLogService userLogService = new UserLogService(new UserLogRepository());

        UserLog userLog = new UserLog();
        userLog.setUserID(user);
        userLog.setAction("User logged in");
        userLog.setIPAddress(NetworkUtils.getLocalIPAddress());

        userLogService.saveUserLog(userLog);
    }

}
