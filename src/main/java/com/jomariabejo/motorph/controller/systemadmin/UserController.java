package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.enums.AccessType;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.RoleService.RoleService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    private EmployeeService employeeService;
    private UserService userService;
    private RoleService roleService;
    private AccessType accessType;

    public UserController() {
        this.employeeService = new EmployeeService();
        this.userService = new UserService();
        this.roleService = new RoleService();
    }

    @FXML
    private Button saveBtn;

    @FXML
    private ComboBox < String > cb_access_level;

    @FXML
    private TextField tf_employeeId;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML private Label lbl_text;

    @FXML
    void cancelClicked(MouseEvent event) {
        tf_username.getScene().getWindow().hide();
    }

    @FXML
    void rightButtonClicked(MouseEvent event) {

        switch (this.accessType) {

            case AccessType.ADD: {
                boolean isAddNewUserConfirmed = AlertUtility.showConfirmation(
                        "Save user confirmation",
                        "Are you sure you want to save this new user?",
                        "After saving it will be added to your database"
                );

                boolean isEmployeeExist = employeeService.checkIfEmployeeExist(Integer.parseInt(this.tf_employeeId.getText()));

                if (isAddNewUserConfirmed && isEmployeeExist) {
                    userService.saveUser(
                            new User(
                                    Integer.valueOf(this.tf_employeeId.getText()),
                                    roleService.fetchRoleId(this.cb_access_level.getValue()),
                                    this.tf_username.getText(),
                                    this.tf_password.getText()
                            )
                    );
                    AlertUtility.showInformation("User created successfully", null, null);
                } else {
                    AlertUtility.showErrorAlert("Employee not found", null, null);
                }
                break;
            }

            case AccessType.UPDATE: {

            }

            case AccessType.DELETE: {
                boolean isDeletingUserConfirmed = AlertUtility.showConfirmation(
                        "Deletion confirmation",
                        "Are you sure you want to delete this user?",
                        "After deleting it will be removed to your database"
                );

                if (isDeletingUserConfirmed) {
                    boolean isDeletionSuccess = userService.deleteUser(Integer.valueOf(tf_employeeId.getText()));
                    if (isDeletionSuccess) {
                        AlertUtility.showInformation("Deleted user successfully", null, null);
                        this.lbl_text.getScene().getWindow().hide();
                    }
                    else {
                        AlertUtility.showInformation("Failed to delete user", null, null);
                    }
                }
                break;
            }
        }
    }

    private void setUpComboBox() {
        String query = "SELECT name FROM Role";

        try (
                Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement pstmt = connection.prepareStatement(query)) {

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                this.cb_access_level.getItems().add(
                        resultSet.getString(1)
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpComboBox();
    }

    public void initData(int userID, AccessType accessType) {
        this.accessType = accessType;

        User user = userService.fetchUser(userID);

        switch (accessType) {
            case VIEW -> {
                mapUserData(user);
                disableFields();
                this.lbl_text.setText(AccessType.VIEW.name()+ " user");
            }
            case UPDATE -> {
                mapUserData(user);
                this.lbl_text.setText(AccessType.UPDATE.name()+ " user");
            }
            case DELETE -> {
                mapUserData(user);
                disableFieldsExceptRightButton();
                this.lbl_text.setText(AccessType.DELETE.name()+ " user");
                this.saveBtn.setText("Delete");
            }
        }
    }

    private void disableFields() {
        this.saveBtn.setVisible(true);
        this.cb_access_level.setDisable(true);
        this.tf_password.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_employeeId.setDisable(true);
        this.saveBtn.setVisible(false);
        this.saveBtn.setText("Close");
    }

    private void disableFieldsExceptRightButton() {
        this.saveBtn.setVisible(true);
        this.cb_access_level.setDisable(true);
        this.tf_password.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_employeeId.setDisable(true);
        this.saveBtn.setText("Close");
    }

    private void mapUserData(User user) {
        this.tf_employeeId.setText(String.valueOf(user.getEmployeeID()));
        this.tf_username.setText(user.getUsername());
        this.tf_password.setText(user.getPassword());
        this.cb_access_level.setValue(roleService.fetchRoleName(user.getRoleID()));
    }
}