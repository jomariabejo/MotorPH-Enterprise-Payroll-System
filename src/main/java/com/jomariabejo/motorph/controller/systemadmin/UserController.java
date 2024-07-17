package com.jomariabejo.motorph.controller.systemadmin;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.enums.AccessType;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.RoleService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final RoleService roleService;
    private AccessType accessType;

    @FXML
    private Button tf_resetPasswordBtn;

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
    private Button actionBtn;

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
        switch (actionBtn.getText()) {
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

        } else if (isAddNewUserConfirmed && isEmployeeExist) {
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
            System.out.printf(user.toString());

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

    public void initUserIdAndAccessType(int userID, AccessType accessType) {
        this.accessType = accessType;

        mapUserData(userService.fetchUser(userID));
        switch (accessType) {
            case VIEW -> {
                disableFields();
            }
            case UPDATE -> {
                displayUpdateButtonAndComboBoxAndDisplayResetPasswordBtn();
            }
            case DELETE -> {
                disableFieldsAndDisplayDeleteButton();
            }
        }
    }

    private void disableFields() {
        this.actionBtn.setVisible(true);
        this.cb_access_level.setDisable(true);
        this.tf_password.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_employeeId.setDisable(true);
        this.actionBtn.setVisible(false);
        this.actionBtn.setText("Close");
        this.lbl_text.setText(AccessType.VIEW.name() + " " + tf_username.getText().toUpperCase());
    }

    private void disableFieldsAndDisplayDeleteButton() {
        this.actionBtn.setVisible(true);
        this.cb_access_level.setDisable(true);
        this.tf_password.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_employeeId.setDisable(true);
        this.lbl_text.setText(AccessType.DELETE.name() + " " + tf_username.getText().toUpperCase());
        this.actionBtn.setText("Delete");
    }

    private void displayUpdateButtonAndComboBoxAndDisplayResetPasswordBtn() {
        this.actionBtn.setVisible(true);
        this.cb_access_level.setDisable(false);
        this.tf_password.setDisable(true);
        this.tf_username.setDisable(true);
        this.tf_employeeId.setDisable(true);
        this.tf_resetPasswordBtn.setVisible(true);
        this.tf_resetPasswordBtn.setManaged(true);
        this.lbl_text.setText(AccessType.UPDATE.name() + " " + tf_username.getText().toUpperCase());
        this.actionBtn.setText("Update");
    }

    private void mapUserData(User user) {
        this.tf_userId.setText(String.valueOf(user.getUserID()));
        this.tf_employeeId.setText(String.valueOf(user.getEmployeeID()));
        this.tf_username.setText(user.getUsername());
        this.tf_password.setText(user.getPassword());
        this.cb_access_level.setValue(roleService.fetchRoleName(user.getRoleID()));
        this.tf_employeeId.setDisable(true);
    }

    public boolean resetPasswordConfirmed() {
        return AlertUtility.showConfirmation("Are you sure you want to reset password to this user", null, null);
    }

    public int executeResetPassword(String password) {
        String query = "UPDATE USER SET user.password = ? WHERE user_id = ? AND user.employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, password);
            pstmt.setInt(2, Integer.parseInt(tf_userId.getText()));
            pstmt.setInt(3, Integer.parseInt(tf_employeeId.getText()));

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateNewPassword() {
        String generatedPassword = "changePasswordOnceYouAlreadyLoggedIn" + DateUtility.getDate().toString();
        this.tf_password.setText(generatedPassword); // update password value
        return generatedPassword;
    }

    public void resetPasswordClicked(MouseEvent mouseEvent) {
        if (resetPasswordConfirmed()) {
            int rowsAffected = executeResetPassword(generateNewPassword());
            displayResetPasswordResult(rowsAffected);
        }
    }

    private void displayResetPasswordResult(int rowsAffected) {
        switch (rowsAffected) {
            case 0:
                AlertUtility.showInformation("Reset Password Unsuccessful", null, null);
                break;
            case 1:
                AlertUtility.showInformation(
                        "Reset Password Successful",
                        tf_username.getText() + " new password",
                        tf_password.getText());
                downloadUserCredentials();
                break;
            default:
                break;
        }
    }

    private void downloadUserCredentials() {
        // Data to be written to the CSV file
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Reminder   ", "Please change your password as soon as possible"});
        data.add(new String[]{"User Id    ", tf_userId.getText()});
        data.add(new String[]{"Employee Id", tf_employeeId.getText()});
        data.add(new String[]{"Username   ", tf_username.getText()});
        data.add(new String[]{"Password   ", tf_password.getText()});

        // Directory path for the CSV file
        String directoryPath = "src/main/resources/reset_password/";

        // Ensure directory exists
        Path directory = Paths.get(directoryPath);
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            System.out.println("Error creating directory: " + e.getMessage());
            return;
        }

        // Name of the CSV file
        String filename = DateUtility.getDate().toString() + "_" + "UserCredentials for " + employeeService.fetchEmployeeName(Integer.parseInt(tf_employeeId.getText())).get();

        // Writing to the CSV file
        try (FileWriter fileWriter = new FileWriter(directory.resolve(filename).toString())) {
            for (String[] row : data) {
                fileWriter.append(String.join("\t:\t", row));
                fileWriter.append("\n");
            }
            System.out.println(filename + " created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}