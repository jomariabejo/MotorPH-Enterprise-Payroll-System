package com.jomariabejo.motorph.controller.role.systemadministrator;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.LoggingUtility;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserFormController {

    private UserController userController;
    private User user;

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfFullName;

    @FXML
    private ComboBox<Role> cbRole;

    @FXML
    private ListView<Role> lvAdditionalRoles;

    @FXML
    private ComboBox<Employee> cbEmployee;

    @FXML
    private ComboBox<String> cbStatus;

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
        if (userController == null || userController.getSystemAdministratorNavigationController() == null) {
            return;
        }

        boolean allowed = (user == null)
                ? userController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_CREATE, "create users")
                : userController.getSystemAdministratorNavigationController()
                    .requirePermission(PermissionConstants.SYSTEM_ADMIN_USERS_EDIT, "edit users");

        if (!allowed) {
            return;
        }

        if (validateFields()) {
            if (user == null) {
                createUser();
            } else {
                updateUser();
            }
        }
    }

    public void setup() {
        populateRoles();
        populateEmployees();
        populateStatus();
        
        if (user != null) {
            loadUserData();
        }
    }

    private void populateRoles() {
        List<Role> roles = userController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory().getRoleService().getAllRoles();
        cbRole.setItems(FXCollections.observableList(roles));
        cbRole.setCellFactory(param -> new ListCell<Role>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                setText(empty || role == null ? null : role.getRoleName());
            }
        });
        cbRole.setButtonCell(new ListCell<Role>() {
            @Override
            protected void updateItem(Role role, boolean empty) {
                super.updateItem(role, empty);
                setText(empty || role == null ? null : role.getRoleName());
            }
        });

        if (lvAdditionalRoles != null) {
            lvAdditionalRoles.setItems(FXCollections.observableList(roles));
            lvAdditionalRoles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            lvAdditionalRoles.setCellFactory(param -> new ListCell<>() {
                private final CheckBox checkBox = new CheckBox();

                {
                    checkBox.setOnAction(e -> {
                        Role r = getItem();
                        if (r == null) return;
                        if (checkBox.isSelected()) {
                            lvAdditionalRoles.getSelectionModel().select(r);
                        } else {
                            lvAdditionalRoles.getSelectionModel().clearSelection(getIndex());
                        }
                    });
                }

                @Override
                protected void updateItem(Role role, boolean empty) {
                    super.updateItem(role, empty);
                    if (empty || role == null) {
                        setGraphic(null);
                        return;
                    }
                    checkBox.setText(role.getRoleName());
                    checkBox.setSelected(lvAdditionalRoles.getSelectionModel().getSelectedItems().contains(role));
                    setGraphic(checkBox);
                }
            });
        }
    }

    private void populateEmployees() {
        List<Employee> employees = userController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory().getEmployeeService().getAllEmployees();
        cbEmployee.setItems(FXCollections.observableList(employees));
        cbEmployee.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
        cbEmployee.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    private void populateStatus() {
        cbStatus.setItems(FXCollections.observableArrayList("Active", "Inactive"));
        cbStatus.getSelectionModel().select("Active");
    }

    private void loadUserData() {
        tfUsername.setText(user.getUsername());
        tfPassword.setText(user.getPassword());
        tfEmail.setText(user.getEmail());
        tfFullName.setText(user.getFullName());
        cbRole.getSelectionModel().select(user.getRoleID());
        cbEmployee.getSelectionModel().select(user.getEmployee());
        cbStatus.getSelectionModel().select(user.getStatus());

        // Preselect additional roles from join table mapping
        if (lvAdditionalRoles != null && user.getRoles() != null) {
            lvAdditionalRoles.getSelectionModel().clearSelection();
            for (Role r : user.getRoles()) {
                if (r == null) continue;
                lvAdditionalRoles.getItems().stream()
                        .filter(item -> item != null && item.getId() != null && item.getId().equals(r.getId()))
                        .findFirst()
                        .ifPresent(item -> lvAdditionalRoles.getSelectionModel().select(item));
            }
        }
    }

    private boolean validateFields() {
        if (tfUsername.getText().isEmpty() ||
                tfPassword.getText().isEmpty() ||
                tfEmail.getText().isEmpty() ||
                tfFullName.getText().isEmpty() ||
                cbRole.getSelectionModel().getSelectedItem() == null ||
                cbEmployee.getSelectionModel().getSelectedItem() == null ||
                cbStatus.getSelectionModel().getSelectedItem() == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }

        if (!tfEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a valid email address."
            );
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void createUser() {
        User newUser = new User();
        mapFieldsToUser(newUser);
        
        userController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getUserService().saveUser(newUser);

        syncUserRoles(newUser);

        // Log user creation
        User currentUser = userController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logUserCreated(currentUser, newUser);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "User created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        userController.populateUsers();
    }

    private void updateUser() {
        mapFieldsToUser(user);
        
        userController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getUserService().updateUser(user);

        syncUserRoles(user);

        // Log user update
        User currentUser = userController.getSystemAdministratorNavigationController()
                .getMainViewController().getUser();
        if (currentUser != null) {
            LoggingUtility.logUserUpdated(currentUser, user);
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "User updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        userController.populateUsers();
    }

    private void syncUserRoles(User targetUser) {
        if (targetUser == null || targetUser.getId() == null) {
            return;
        }
        var serviceFactory = userController.getSystemAdministratorNavigationController()
                .getMainViewController().getServiceFactory();

        Set<Integer> roleIds = new HashSet<>();
        Set<Role> selectedRoles = new HashSet<>();
        // Always include primary/default role
        Role primary = cbRole.getSelectionModel().getSelectedItem();
        if (primary != null && primary.getId() != null) {
            roleIds.add(primary.getId());
            selectedRoles.add(primary);
        }

        if (lvAdditionalRoles != null) {
            Set<Role> extra = lvAdditionalRoles.getSelectionModel().getSelectedItems().stream()
                    .filter(r -> r != null && r.getId() != null)
                    .collect(Collectors.toSet());
            selectedRoles.addAll(extra);
            roleIds.addAll(extra.stream().map(Role::getId).collect(Collectors.toSet()));
        }

        serviceFactory.getUserRoleService().setRolesForUser(targetUser.getId(), roleIds);

        // Keep in-memory user aligned so permission checks/menu updates reflect immediately.
        targetUser.setRoles(selectedRoles);

        // If we edited the currently logged-in user, refresh role menu immediately.
        User current = userController.getSystemAdministratorNavigationController().getMainViewController().getUser();
        if (current != null && current.getId() != null && current.getId().equals(targetUser.getId())) {
            userController.getSystemAdministratorNavigationController().getMainViewController().refreshRoleMenu();
        }
    }

    private void mapFieldsToUser(User user) {
        user.setUsername(tfUsername.getText());
        user.setPassword(tfPassword.getText());
        user.setEmail(tfEmail.getText());
        user.setFullName(tfFullName.getText());
        user.setRoleID(cbRole.getSelectionModel().getSelectedItem());
        user.setEmployee(cbEmployee.getSelectionModel().getSelectedItem());
        user.setStatus(cbStatus.getSelectionModel().getSelectedItem());
    }
}

