package com.jomariabejo.motorph.controller.role.systemadministrator;

import com.jomariabejo.motorph.model.Role;
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
public class RoleFormController {

    private RoleController roleController;
    private Role role;

    @FXML
    private TextField tfRoleName;

    @FXML
    private TextArea taDescription;

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
        if (roleController == null || roleController.getSystemAdministratorNavigationController() == null) {
            return;
        }

        boolean allowed = (role == null)
                ? roleController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_CREATE, "create roles")
                : roleController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_ROLES_EDIT, "edit roles");

        if (!allowed) {
            return;
        }

        if (validateFields()) {
            if (role == null) {
                createRole();
            } else {
                updateRole();
            }
        }
    }

    public void setup() {
        if (role != null) {
            loadRoleData();
        }
    }

    private void loadRoleData() {
        tfRoleName.setText(role.getRoleName());
        taDescription.setText(role.getDescription() != null ? role.getDescription() : "");
    }

    private boolean validateFields() {
        if (tfRoleName.getText().isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a role name."
            );
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void createRole() {
        Role newRole = new Role();
        mapFieldsToRole(newRole);
        
        roleController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getRoleService().saveRole(newRole);

        // Log role creation
        User currentUser = roleController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logRoleCreated(currentUser, newRole);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Role created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        roleController.populateRoles();
    }

    private void updateRole() {
        mapFieldsToRole(role);
        
        roleController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getRoleService().updateRole(role);

        // Log role update
        User currentUser = roleController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logRoleUpdated(currentUser, role);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Role updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        roleController.populateRoles();
    }

    private void mapFieldsToRole(Role role) {
        role.setRoleName(tfRoleName.getText());
        role.setDescription(taDescription.getText().isEmpty() ? null : taDescription.getText());
    }
}





