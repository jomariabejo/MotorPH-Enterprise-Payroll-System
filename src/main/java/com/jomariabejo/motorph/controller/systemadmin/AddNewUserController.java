package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.entity.Role;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.RoleService.RoleService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class AddNewUserController {

    private EmployeeService employeeService;
    private UserService userService;
    private RoleService roleService;

    public AddNewUserController() {
        this.employeeService = new EmployeeService();
        this.userService = new UserService();
    }

    @FXML
    private ComboBox<Role> cb_access_level;

    @FXML
    private TextField tf_employeeId;

    @FXML
    private PasswordField tf_password;

    @FXML
    private TextField tf_username;

    @FXML
    void cancelClicked(MouseEvent event) {

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
                .parseInt(this.tf_employeeId.getText())))
            {
                userService.saveUser(
                        new User(
                               Integer.valueOf(this.tf_employeeId.getText()),
                               roleService.fetchRoleId(this.cb_access_level.getValue()),
                               this.tf_username.getText(),
                               this.tf_password.getText()
                        )
                );
            }
        }
    }
}
