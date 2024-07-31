package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
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
                Alert.AlertType.INFORMATION,
                "Save new password",
                "Are you sure you want to save this new password ?"
        );
        Optional<ButtonType> userAction = customAlert.showAndWait();

        if (userAction.isPresent() && userAction.get() == ButtonType.OK) {
            // TODO 1 verify if the current password is correct
            //      2 process the change password
        }
        else if (userAction.get() == ButtonType.CANCEL) {
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
