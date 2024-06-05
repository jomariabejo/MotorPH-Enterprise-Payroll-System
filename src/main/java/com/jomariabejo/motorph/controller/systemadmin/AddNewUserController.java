package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.RoleService.RoleService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddNewUserController implements Initializable {

    private EmployeeService employeeService;
    private UserService userService;
    private RoleService roleService;

    public AddNewUserController() {
        this.employeeService = new EmployeeService();
        this.userService = new UserService();
        this.roleService = new RoleService();
    }

    @FXML
    private Button saveBtn;

    @FXML
    private ComboBox<String> cb_access_level;

    @FXML
    private TextField tf_employeeId;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    void cancelClicked(MouseEvent event) {
        tf_username.getScene().getWindow().hide();
    }

    @FXML
    void saveClicked(MouseEvent event) {
        boolean isConfirmed = AlertUtility.showConfirmation(
                "Save user confirmation",
                "Are you sure you want to save this new user?",
                "After saving it will be added to your database"
        );

        if (isConfirmed) {
            if (employeeService
                    .checkIfEmployeeExist(Integer
                            .parseInt(this.tf_employeeId.getText()))) {
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
    }

    private void setUpComboBox() {
        String query = "SELECT name FROM Role";

        try (
                Connection connection = DatabaseConnectionUtility.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

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

    public void initData(int userID) {
        User user = userService.fetchUser(userID);

        mapUserData(user);
    }

    private void mapUserData(User user) {
        this.tf_employeeId.setText(String.valueOf(user.getEmployeeID()));
        this.tf_username.setText(user.getUsername());
        this.tf_password.setText(user.getPassword());
        this.cb_access_level.setValue(roleService.fetchRoleName(user.getRoleID()));

        this.tf_employeeId.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_password.setDisable(true);
        this.cb_access_level.setDisable(true);
        this.saveBtn.setVisible(false);
    }
}
