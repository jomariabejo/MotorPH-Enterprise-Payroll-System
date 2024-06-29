package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
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

    @FXML
    public void possessAVerificationCodeAlreadyButtonClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/process_generated_code.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Process generated code");
        stage.setScene(new Scene(root));
        stage.show();
        this.tf_username.getScene().getWindow().hide();
    }
}
