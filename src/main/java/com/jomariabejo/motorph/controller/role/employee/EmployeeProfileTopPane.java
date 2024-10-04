package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;

@Getter
@Setter
public class EmployeeProfileTopPane {

    private EmployeeProfileController employeeProfileController;

    @FXML
    private Label actionLabel;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button lastButton;

    public EmployeeProfileTopPane() {

    }

    @FXML
    private void initialize() {
        lastButton.setGraphic(new FontIcon(Material.PERSON));
        changePasswordButton.setGraphic(new FontIcon(Material.SECURITY));
    }

    @FXML
    void changePasswordClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/change-password-view.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setScene(new Scene(formPane));

            EmployeeChangePasswordController employeeChangePasswordController = loader.getController();
            employeeChangePasswordController.setEmployeeProfileTopPane(this);
            employeeChangePasswordController.addIcons();
            employeeChangePasswordController.addButtonColor();
            FontIcon fontIcon = new FontIcon(Material.SECURITY);
            fontIcon.setIconSize(150);
            employeeChangePasswordController.getLblChangePassword().setGraphic(fontIcon);

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void moreInfoClicked() {

        switch (this.lastButton.getText()) {
            case "Profile":
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/my-profile-view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    this.getEmployeeProfileController().getEmployeeProfileBorderPane().setCenter(anchorPane);
                    lastButton.setText("More Info");
                    this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().
                            rewriteLabel("/ Employee / Profile / ");
                    EmployeeProfileController employeeProfileController = fxmlLoader.getController();
                    employeeProfileController.setEmployeeRoleNavigationController(this.getEmployeeProfileController().getEmployeeRoleNavigationController());
                    employeeProfileController.rewriteTextField();
                    lastButton.setGraphic(new FontIcon(Material.PERSON));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            case "More Info":
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-more-info.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    employeeProfileController.getEmployeeProfileBorderPane().setCenter(anchorPane);
                    lastButton.setText("Profile");
                    this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().
                            rewriteLabel("/ Employee / More Info / ");

                    MoreInfoController moreInfoController = fxmlLoader.getController();
                    moreInfoController.setEmployeeProfileController(employeeProfileController);
                    moreInfoController.personalInformationClicked();

                    lastButton.setGraphic(new FontIcon(Material.MORE));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
        }
    }
}
