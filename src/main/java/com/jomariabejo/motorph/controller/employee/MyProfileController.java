package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import com.jomariabejo.motorph.record.SalaryStructure;
import com.jomariabejo.motorph.service.SalaryDetailService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileController {

    @FXML
    private PieChart chart_allowance;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_basic_salary;

    @FXML
    private TextField tf_birthday;

    @FXML
    private TextField tf_clothing_allowance;

    @FXML
    private TextField tf_employee_id;

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_hourly_rate;

    @FXML
    private TextField tf_last_name;

    @FXML
    private TextField tf_phone_allowance;

    @FXML
    private TextField tf_position;

    @FXML
    private TextField tf_rice_subsidy;

    @FXML
    private TextField tf_semi_monthly_rate;

    @FXML
    private TextField tf_status;

    @FXML
    private TextField tf_supervisor;

    private SalaryDetailService salaryDetailService;

    public MyProfileController() {
        this.salaryDetailService = new SalaryDetailService();
    }

    @FXML
    public void initialize(int employeeId) {
        this.tf_employee_id.setText(String.valueOf(employeeId));
        setupMyProfile();
        setupMyAllowance();
        setupMySalaryDetails();
        includeChangePasswordValidator();
    }

    private void setupMySalaryDetails() {
        int employeeId = Integer.parseInt(this.tf_employee_id.getText());
        SalaryStructure salaryStructureDetails = salaryDetailService.fetchSalaryDetails(employeeId);

        // Display the salary details in textfields
        tf_basic_salary.setText(String.valueOf(salaryStructureDetails.basicSalary()));
        tf_semi_monthly_rate.setText(String.valueOf(salaryStructureDetails.grossSemiMonthlyRate()));
        tf_hourly_rate.setText(String.valueOf(salaryStructureDetails.hourlyrate()));
    }


    private void setupMyAllowance() {
        String query = "SELECT rice, clothing, phone FROM allowance WHERE allowance.employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tf_rice_subsidy.setText(String.valueOf(resultSet.getInt("rice")));
                tf_clothing_allowance.setText(String.valueOf(resultSet.getInt("clothing")));
                tf_phone_allowance.setText(String.valueOf(resultSet.getInt("phone")));
                showAllowancePieChart();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAllowancePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Rice Subsidy", Integer.valueOf(this.tf_rice_subsidy.getText())),
                new PieChart.Data("Phone Allowance", Integer.valueOf(this.tf_phone_allowance.getText())),
                new PieChart.Data("Clothing Allowance", Integer.valueOf(this.tf_clothing_allowance.getText())));

        this.chart_allowance.setTitle("My Allowance Breakdown");
        this.chart_allowance.setData(pieChartData);
    }


    private void setupMyProfile() {
        String query = "SELECT e.first_name, e.last_name, e.birthday, e.address, e.employee_id, e.STATUS, p.name AS position_name, e.supervisor FROM employee e JOIN position p ON e.position_id = p.position_id WHERE e.employee_id = ?;";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_first_name.setText(resultSet.getString("first_name"));
                tf_last_name.setText(resultSet.getString("last_name"));
                tf_birthday.setText(resultSet.getDate("birthday").toString());
                tf_address.setText(resultSet.getString("address"));
                tf_status.setText(String.valueOf(EmployeeStatus.valueOf(resultSet.getString("STATUS"))));
                tf_position.setText(resultSet.getString("position_name"));
                tf_supervisor.setText(resultSet.getString("supervisor"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Controller for 'Change Password Tab'
     */

    private Validator newPasswordValidator = new Validator();
    private Validator confirmPasswordValidator = new Validator();

    @FXML
    private Button confirmNewPasswordButton;

    @FXML
    private PasswordField tf_existing_password;

    @FXML
    private PasswordField tf_new_password;

    @FXML
    private PasswordField tf_confirm_new_password;

    public void changePasswordCancelButtonClicked(ActionEvent event) {
        clearPasswordTabFields();
    }

    private void clearPasswordTabFields() {
        this.tf_existing_password.setText("");
        this.tf_new_password.setText("");
        this.tf_confirm_new_password.setText("");
    }

    public void confirmNewPasswordClicked(ActionEvent event) {
        String newPassword = tf_new_password.getText();
        String confirmPassword = tf_confirm_new_password.getText();
        String existingPassword = tf_existing_password.getText();

        if (employeePasswordExist()) {

        }

        else if (isPasswordSame(newPassword, confirmPassword)) {
            UserService userService = new UserService();
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());

            boolean isModified =
                userService.changePassword(
                    employeeId,
                    existingPassword,
                    newPassword);
            if (isModified) {
                AlertUtility.showInformation("Password Change Status", "Password Update Success", "Your password has been successfully updated.");
            }
            else {
                AlertUtility.showInformation("Password Change Status", "Password Update Failed", "Your password could not be updated. Please try again.");
            }
        }

        else {
            AlertUtility.showErrorAlert("Password Error", "Password Mismatch", "Your new password and confirm password do not match.");
        }
    }

    private boolean employeePasswordExist() {
        return false;
    }

    public void enableChangePasswordConfirmButton() {
        confirmNewPasswordButton.setDisable(false);
    }

    public void disableChangePasswordConfirmButton() {
        confirmNewPasswordButton.setDisable(true);
    }

    public boolean isPasswordSame(String newPassword, String confirmPassword) {
        return newPassword.equals(confirmPassword);
    }

    private void includeChangePasswordValidator() {
        disableChangePasswordConfirmButton();

        newPasswordValidator.createCheck().dependsOn("newPassword", tf_new_password.textProperty()).withMethod(c -> {
            String strNewPassword = c.get("newPassword");
            if (strNewPassword.isEmpty()) {
                c.error("New password is empty");
            }
            else if (isPasswordSame(strNewPassword, this.tf_confirm_new_password.getText())) {
                enableChangePasswordConfirmButton();
            }
            else {
                c.error("Your new password and confirm password do not match.");
                disableChangePasswordConfirmButton();
            }
        }).decorates(tf_new_password).immediate();
        confirmPasswordValidator.createCheck().dependsOn("confirmedNewPassword", tf_confirm_new_password.textProperty()).withMethod(c -> {
            String strConfirmedNewPassword = c.get("confirmedNewPassword");
            if (strConfirmedNewPassword.isEmpty()) {
                c.error("Confirmed new password is empty");
            }
            else if (isPasswordSame(strConfirmedNewPassword, this.tf_new_password.getText())) {
                enableChangePasswordConfirmButton();
            }
            else {
                c.error("Your new password and confirm password do not match.");
                disableChangePasswordConfirmButton();
            }
        }).decorates(tf_confirm_new_password).immediate();
    }
}
