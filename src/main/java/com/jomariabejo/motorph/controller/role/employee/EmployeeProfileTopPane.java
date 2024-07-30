package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.ModalBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

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
    void changePasswordClicked(ActionEvent event) {
// https://stackoverflow.com/questions/53825323/javafx-textinputdialog-for-password-masking
//        Dialog<String> dialog = new Dialog<>();
//        dialog.setWidth(600);
//        dialog.setHeight(450);
//        dialog.setTitle("Change Password");
//        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//
//        PasswordField pwd = new PasswordField();
//        pwd.setPromptText("Enter old password");
//
//        Label oldPasswordLabel = new Label("Enter your password");
//        oldPasswordLabel.setStyle("-fx-font-size: 24px;"); // Set font size
//
//        Label newPasswordLabel = new Label("Enter your new password");
//        oldPasswordLabel.setStyle("-fx-font-size: 24px;"); // Set font size
//
//        Label confirmPasswordLabel = new Label("Re enter your new password");
//        oldPasswordLabel.setStyle("-fx-font-size: 24px;"); // Set font size
//
//        VBox content = new VBox();
//        content.setAlignment(Pos.CENTER); // Center content in VBox
//        content.setSpacing(10); // Space between elements
//        content.getChildren().addAll(oldPasswordLabel, pwd);
//
//        content.setStyle("-fx-font-size: 24px;");
//
//        dialog.getDialogPane().setContent(content);
//
//        dialog.show();
//
//        dialog.setResultConverter(dialogButton -> {
//            if (dialogButton == ButtonType.OK) {
//                if (verifyEmployeeCurrentPassword(pwd.getText())) {
//                    if (newPasswordLabel.getText().equals(confirmPasswordLabel.getText())) {
//                        // TODO
//                    }
//                }
//                return pwd.getText();
//            } else if (dialogButton == ButtonType.CANCEL) {
//                dialog.hide();
//            }
//            return "";
//        });
    }

//    /**
//     * E checheck natin kung nag eexist nga ba ang password ng employee number
//     * Kasi doon sa database natin, may table doon na user which has a column foreign key to employee number,
//     * we will use that to verify (employeeNumber + password). However we can also use the username, but based on my
//     * user interface for the main view controller, it doesn't store any username data. That is the reason we use employee number and password.
//     *
//     * @param password
//     * @return True if password exist, false if not
//     */
//    private boolean verifyEmployeeCurrentPassword(String password) {
//        try {
//            int employeeNumber = Integer.parseInt(this.employeeProfileController.getEmployeeNumber().getText());
//
//            return false;
//        }
//        catch (NumberFormatException numberFormatException) {
//            return false;
//        }
//    }
//
//    private boolean userPasswodExist(int userId, String password) {
//        /**
//         * TODO: Is user password exist
//         */
//        return false;
//    }
// COMMENT END

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
