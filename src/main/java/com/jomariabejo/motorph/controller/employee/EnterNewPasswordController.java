package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class EnterNewPasswordController {

    @FXML
    private PasswordField tf_confirm_password;

    @FXML
    private PasswordField tf_enter_password;

    private int userId;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        tf_enter_password.getScene().getWindow().hide();
    }

    @FXML
    void confirmButtonClicked(ActionEvent event) {
        if (isPasswordSame()) {
            saveUserNewPassword();
        }
    }

    private void saveUserNewPassword() {
        UserService userService = new UserService();

        int userId = this.userId;
        String password = tf_confirm_password.getText();

        boolean isChangePasswordSuccess = userService.changePassword(userId, password);

        if (isChangePasswordSuccess) {
            AlertUtility.showInformation(
                    "Change password success",
                    "Your new password saved successfully",
                    "You can login now.");

            // set the verification code to its normal formal(null)
            userService.resetVerificationCode(this.userId);

            tf_enter_password.getScene().getWindow().hide();
        } else {
            AlertUtility.showErrorAlert(
                    "Change password failed",
                    "Your password and confirmation password is not equal",
                    "Please try again."
            );
        }
    }

    public boolean isPasswordSame() {
        return tf_confirm_password.getText().equals(tf_enter_password.getText());
    }

    public void initUserId(int userId) {
        this.userId = userId;
    }

}
