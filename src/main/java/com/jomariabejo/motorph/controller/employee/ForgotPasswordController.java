package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordController {
    @FXML
    private TextField tf_username;

    @FXML
    void cancelButtonClicked() {
        tf_username.getScene().getWindow().hide();
    }

    @FXML
    void confirmButtonClicked() {
        UserService userService = new UserService();
        String username = tf_username.getText();

        boolean isConfirmed = AlertUtility.showConfirmation(
                "Forgot password request",
                "Submit a forgot password request",
                "Are you sure you want to submit forgot password request ?");

        if (isConfirmed) {
            if (userService.isUsernameExist(username)) {
                int code = generateCode();
                boolean isForgotPasswordRequestSubmittedSuccessfully = userService.saveVerificationCode(username, code);

                if (isForgotPasswordRequestSubmittedSuccessfully) {
                    AlertUtility.showInformation(
                            "Forgot password request sent successfully.",
                            "Please wait for the system admin to sent your verification code",
                            "Your system admin will provide the verification code.");

                    tf_username.getScene().getWindow().hide();
                }
                else {
                    AlertUtility.showErrorAlert(
                            "Forgot password failed",
                            "Your forgot password failed",
                            "Please try again.");
                }
            }
            else {
                AlertUtility.showErrorAlert(
                        "Username not found",
                        "Entered username not found",
                        "Please try again.");
            }
        }
        else {
            tf_username.getScene().getWindow().hide();
        }
    }

    private int generateCode() {
        String query = "SELECT COALESCE(MAX(user.verification_code)+1,100000) AS GENERATED_CODE FROM USER";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)
        ){
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                AlertUtility.showErrorAlert("Failed", "Duplicate entry", "Please try again.");
            }
            else {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
