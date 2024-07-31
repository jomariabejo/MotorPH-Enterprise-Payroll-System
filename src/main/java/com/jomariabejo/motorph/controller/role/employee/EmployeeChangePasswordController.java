package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.util.Optional;

@Getter
@Setter
public class EmployeeChangePasswordController {

    private EmployeeProfileTopPane employeeProfileTopPane;

    private UserService userService = new UserService(new UserRepository());

    @FXML
    private Button cancelBtn;

    @FXML
    private Label lblChangePassword;

    @FXML
    private Button saveBtn;

    @FXML
    private PasswordField tfConfirmNewPassword;

    @FXML
    private PasswordField tfCurrentPassword;

    @FXML
    private PasswordField tfNewPassword;

    @FXML
    public void cancelClicked() {
        getLblChangePassword().getScene().getWindow().hide();
    }

    @FXML
    public void saveClicked() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Save new password",
                "Are you sure you want to save this new password ?"
        );
        Optional<ButtonType> userAction = customAlert.showAndWait();

        if (userAction.isPresent() && userAction.get() == ButtonType.OK) {
            String currentPassword = this.getEmployeeProfileTopPane().getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().getUser().getPassword();
            if (currentPassword.equals(tfCurrentPassword.getText())) {
                // The user that logged in
                User user = this.getEmployeeProfileTopPane().getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().getUser();
                user.setPassword(tfConfirmNewPassword.getText());
                userService.updateUser(user);
                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Password changed",
                        "Password saved successfully."
                );
                successAlert.showAndWait();
            } else {
                CustomAlert failedAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Change password failed",
                        "Incorrect password. Please try again."
                );
                failedAlert.showAndWait();
            }
        } else if (userAction.get() == ButtonType.CANCEL) {
            getLblChangePassword().getScene().getWindow().hide();
        }
    }

    public void addIcons() {
        saveBtn.setGraphic(new FontIcon(Material.SAVE));
        cancelBtn.setGraphic(new FontIcon(Material.CANCEL));
    }

    public void addButtonColor() {
        saveBtn.getStyleClass().addAll(
                Styles.SUCCESS,
                Styles.ROUNDED
        );

        cancelBtn.getStyleClass().addAll(
                Styles.DANGER,
                Styles.ROUNDED
        );
    }
}
