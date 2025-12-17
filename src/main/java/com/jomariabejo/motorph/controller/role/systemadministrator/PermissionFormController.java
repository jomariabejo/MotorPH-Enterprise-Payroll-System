package com.jomariabejo.motorph.controller.role.systemadministrator;

import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.LoggingUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionFormController {

    private PermissionController permissionController;
    private Permission permission;

    @FXML
    private TextField tfPermissionName;

    @FXML
    private TextArea taDescription;

    @FXML
    private TextField tfCategory;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (permissionController == null || permissionController.getSystemAdministratorNavigationController() == null) {
            return;
        }

        boolean allowed = (permission == null)
                ? permissionController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_CREATE, "create permissions")
                : permissionController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_EDIT, "edit permissions");

        if (!allowed) {
            return;
        }

        if (validateFields()) {
            if (permission == null) {
                createPermission();
            } else {
                updatePermission();
            }
        }
    }

    public void setup() {
        if (permission != null) {
            loadPermissionData();
        }
    }

    private void loadPermissionData() {
        tfPermissionName.setText(permission.getPermissionName());
        taDescription.setText(permission.getDescription() != null ? permission.getDescription() : "");
        tfCategory.setText(permission.getCategory() != null ? permission.getCategory() : "");
    }

    private boolean validateFields() {
        if (tfPermissionName.getText().isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a permission name."
            );
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void createPermission() {
        Permission newPermission = new Permission();
        mapFieldsToPermission(newPermission);
        
        permissionController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getPermissionService().savePermission(newPermission);

        // Log permission creation
        User currentUser = permissionController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logPermissionCreated(currentUser, newPermission);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Permission created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        permissionController.populatePermissions();
    }

    private void updatePermission() {
        mapFieldsToPermission(permission);
        
        permissionController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getPermissionService().updatePermission(permission);

        // Log permission update
        User currentUser = permissionController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logPermissionUpdated(currentUser, permission);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Permission updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        permissionController.populatePermissions();
    }

    private void mapFieldsToPermission(Permission permission) {
        permission.setPermissionName(tfPermissionName.getText());
        permission.setDescription(taDescription.getText().isEmpty() ? null : taDescription.getText());
        permission.setCategory(tfCategory.getText().isEmpty() ? null : tfCategory.getText());
    }
}





