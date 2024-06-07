package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.enums.AccessType;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.RoleService.RoleService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.fxml.FXMLLoader;
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

    public UserController(AccessType accessType) {
        this.employeeService = new EmployeeService();
        this.userService = new UserService();
        this.roleService = new RoleService();
        this.accessType = accessType;
    }

    @FXML
    private Button rightBtn;

    @FXML
    private ComboBox<String> cb_access_level;

    @FXML
    private TextField tf_employeeId;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    private TextField tf_userId;

    @FXML
    private Label lbl_text;

    @FXML
    void cancelClicked(MouseEvent event) {
        tf_username.getScene().getWindow().hide();
    }

    @FXML
    void rightButtonClicked(MouseEvent event) {
        switch (rightBtn.getText()) {
            case "Save" -> saveUser();
            case "Delete" -> deleteUser();
            case "Update" -> updateUser();
        }
    }

private void saveUser() {
    boolean isAddNewUserConfirmed = AlertUtility.showConfirmation(
            "Save user confirmation",
            "Are you sure you want to save this new user?",
            "After saving it will be added to your database"
    );

    boolean isEmployeeExist = employeeService.checkIfEmployeeExist(Integer.parseInt(this.tf_employeeId.getText()));

    if (!isAddNewUserConfirmed) {

    }
    else if (isAddNewUserConfirmed && isEmployeeExist) {
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
}

private void deleteUser() {
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
        } else {
            AlertUtility.showInformation("Failed to delete user", null, null);
        }
    }
}

private void updateUser() {
    boolean isModificationConfirmed = AlertUtility.showConfirmation(
            "User modified confirmation",
            "Are you sure you want to update this user credentials?",
            "The modification will appear in the database once it has been confirmed."
    );


    if (isModificationConfirmed) {
        User user = new User(
                Integer.valueOf(tf_userId.getText()),
                Integer.valueOf(tf_employeeId.getText()),
                roleService.fetchRoleId(cb_access_level.getSelectionModel().getSelectedItem()),
                this.tf_username.getText(),
                this.tf_password.getText()
        );
        System.out.print("Maybee this time");
        System.out.printf(user.toString());;

        boolean isModificationSuccess = userService.modifyUser(
                new User(
                    Integer.valueOf(tf_userId.getText()),
                    Integer.valueOf(tf_employeeId.getText()),
                    roleService.fetchRoleId(cb_access_level.getSelectionModel().getSelectedItem()),
                    this.tf_username.getText(),
                    this.tf_password.getText()
                )
        );

        if (isModificationSuccess) {
            AlertUtility.showInformation("User modified successfully", null, null);
        } else {
            AlertUtility.showInformation("User modification failed", null, null);
        }

        this.lbl_text.getScene().getWindow().hide();
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
            this.lbl_text.setText(AccessType.VIEW.name() + " " + tf_username.getText().toUpperCase());
        }
        case UPDATE -> {
            mapUserData(user);
            this.lbl_text.setText(AccessType.UPDATE.name() + " " + tf_username.getText().toUpperCase());
            this.rightBtn.setText("Update");
        }
        case DELETE -> {
            mapUserData(user);
            disableFieldsExceptRightButton();
            this.lbl_text.setText(AccessType.DELETE.name() + " " + tf_username.getText().toUpperCase());
            this.rightBtn.setText("Delete");
        }
    }
}

private void disableFields() {
    this.rightBtn.setVisible(true);
    this.cb_access_level.setDisable(true);
    this.tf_password.setDisable(true);
    this.tf_username.setDisable(true);
    this.tf_employeeId.setDisable(true);
    this.rightBtn.setVisible(false);
    this.rightBtn.setText("Close");
}

private void disableFieldsExceptRightButton() {
    this.rightBtn.setVisible(true);
    this.cb_access_level.setDisable(true);
    this.tf_password.setDisable(true);
    this.tf_username.setDisable(true);
    this.tf_employeeId.setDisable(true);
    this.rightBtn.setText("Close");
}

private void mapUserData(User user) {
    System.out.printf("Am i mapuserdata" + user.getUserID() + "This is my user id");
    this.tf_userId.setText(String.valueOf(user.getUserID()));
    this.tf_employeeId.setText(String.valueOf(user.getEmployeeID()));
    this.tf_username.setText(user.getUsername());
    this.tf_password.setText(user.getPassword());
    this.cb_access_level.setValue(roleService.fetchRoleName(user.getRoleID()));
    this.tf_employeeId.setDisable(true);
}
}