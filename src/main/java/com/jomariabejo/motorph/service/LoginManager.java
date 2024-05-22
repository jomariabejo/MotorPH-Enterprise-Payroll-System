package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.controller.LoginController;
import com.jomariabejo.motorph.controller.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages control flow for logins
 */
public class LoginManager {
    private final String FXML_BASE_PATH = "/com/jomariabejo/motorph";
    private final Scene scene;
    private final UserService userService = new UserService();

    public LoginManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(int user_id) {
        showMainView(user_id);
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_BASE_PATH + "/login-view.fxml"));
            scene.setRoot(loader.load());
            LoginController controller = loader.getController();
            controller.initManager(this);
        } catch (IOException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showMainView(int userID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_BASE_PATH + "/main-view.fxml"));

            scene.setRoot(loader.load());

            MainViewController controller = loader.getController();

            String roleString = userService.fetchEmployeeRoleName(userID);

            int employeeId = userService.fetchEmployeeIdByUserId(userID);
            System.out.printf("My employee id : " + employeeId);
            controller.initSessionId(this, userID, employeeId, roleString);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}