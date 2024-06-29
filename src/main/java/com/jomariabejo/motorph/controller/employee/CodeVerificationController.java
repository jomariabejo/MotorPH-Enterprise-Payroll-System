package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class CodeVerificationController {

    @FXML
    private TextField tf_verification_code;

    @FXML
    void cancelButtonClicked() {
        tf_verification_code.getScene().getWindow().hide();
    }

    @FXML
    void confirmButtonClicked() throws IOException {
        if (verificationCodeExist() != 0) {

        }
    }

    private int verificationCodeExist() throws IOException {
        UserService userService = new UserService();
        int verificationCodeInput = 0;

        try {
            verificationCodeInput = Integer.parseInt(this.tf_verification_code.getText());
        } catch (Exception e) {
            AlertUtility.showErrorAlert("Invalid code", "Your code is invalid", "Please try again.");
            return 0;
        }

        int userId = userService.fetchUserIdByVerificationCode(verificationCodeInput);

        if (userId > 0) {
            
            displayNewPasswordProcessing(userId);

        } else {
            AlertUtility.showErrorAlert(
                    "Input code failed",
                    "Verification code not found.",
                    "Please try again."
            );
        }

        return 0;
    }

    private void displayNewPasswordProcessing(int userId) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/enter_new_password.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Process new password");
        stage.setScene(new Scene(root));
        stage.show();
        EnterNewPasswordController enterNewPasswordController = fxmlLoader.getController();
        enterNewPasswordController.initUserId(userId);

        // hide this window
        this.tf_verification_code.getScene().getWindow().hide();
    }

}
