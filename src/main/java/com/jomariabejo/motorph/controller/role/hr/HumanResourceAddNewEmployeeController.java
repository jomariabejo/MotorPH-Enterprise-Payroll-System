package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Position;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class HumanResourceAddNewEmployeeController {

    private EmployeeController employeeController;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private DatePicker dpBirthday;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfBasicSalary;

    @FXML
    private TextField tfClothingAllowance;

    @FXML
    private TextField tfDateHired;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfGrossSemiMonthlyRate;

    @FXML
    private TextField tfHourlyRate;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPagibigNumber;

    @FXML
    private TextField tfPhilhealthNumber;

    @FXML
    private TextField tfPhoneAllowance;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private TextField tfPosition;

    @FXML
    private TextField tfRiceAllowance;

    @FXML
    private TextField tfSSSNumber;

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfTINNumber;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        cancelBtn.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (fieldsValidated()) {
            // The new employee
            Employee employee = mapFieldsToEmployee();

            // save employee to db
            this.getEmployeeController()
                .getHumanResourceAdministratorNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getEmployeeService()
                .saveEmployee(employee);
            
            // Initialize leave balances for the new employee
            // This automatically creates leave balances with default yearly allocations:
            // - Sick Leave: 5 days
            // - Vacation Leave: 10 days
            // - Emergency Leave: 5 days
            this.getEmployeeController()
                .getHumanResourceAdministratorNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getLeaveBalanceService()
                .initializeLeaveBalancesForNewEmployee(employee);
            
            // hide window
            submitBtn.getScene().getWindow().hide();
            // add employee to the tableview
            this.getEmployeeController().getTvEmployees().getItems().add(employee);
            // display employee added successfully
            CustomAlert customAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Employee saved",
                    "Employee saved successfully. Leave balances have been initialized."
            );
            customAlert.showAndWait();
        }
    }

    private Employee mapFieldsToEmployee() {
        Employee employee = new Employee();
        employee.setFirstName(tfFirstName.getText());
        employee.setLastName(tfLastName.getText());
        employee.setRiceSubsidy(new BigDecimal(tfRiceAllowance.getText()));
        employee.setPhoneAllowance(new BigDecimal(tfPhoneAllowance.getText()));
        employee.setPhoneNumber(tfPhoneNumber.getText());
        employee.setDateHired(Date.valueOf(tfDateHired.getText()));
        employee.setAddress(tfAddress.getText());
        employee.setPhoneNumber(tfPhoneNumber.getText());

        employee.setSSSNumber(tfSSSNumber.getText());
        employee.setPhilhealthNumber(tfPhilhealthNumber.getText());
        employee.setTINNumber(tfTINNumber.getText());
        employee.setPagibigNumber(tfPagibigNumber.getText());
        employee.setStatus(tfStatus.getText());
        employee.setPositionID(mapPositionNameToID());

        // salary
        employee.setBasicSalary(new BigDecimal(tfBasicSalary.getText()));
        employee.setGrossSemiMonthlyRate(new BigDecimal(tfGrossSemiMonthlyRate.getText()));
        employee.setHourlyRate(new BigDecimal(tfHourlyRate.getText()));

        // allowance
        employee.setRiceSubsidy(new BigDecimal(tfRiceAllowance.getText()));
        employee.setPhoneAllowance(new BigDecimal(tfPhoneAllowance.getText()));
        employee.setClothingAllowance(new BigDecimal(tfClothingAllowance.getText()));

        employee.setBirthday(Date.valueOf(dpBirthday.getValue()));
        return employee;
    }

    private Position mapPositionNameToID() {
        String positionName = tfPosition.getText();
        return this.employeeController
                .getHumanResourceAdministratorNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getPositionService()
                .getPositionByName(positionName);

    }

    public void addIcons() {
    }

    public void addButtonColor() {
    }

    /**
     * Implement Input Validation
     */

    /**
     * This method will verify if the inputs have correct input and make sures if all fields are filled out.
     *
     * @return True if all fields aren't empty and salary fields followed the number format. Otherwise, false.
     */
    private boolean fieldsValidated() {
        if (
                dpBirthday.getValue() == null ||
                        tfAddress.getText().isEmpty() ||
                        tfBasicSalary.getText().isEmpty() ||
                        tfClothingAllowance.getText().isEmpty() ||
                        tfDateHired.getText().isEmpty() ||
                        tfFirstName.getText().isEmpty() ||
                        tfGrossSemiMonthlyRate.getText().isEmpty() ||
                        tfHourlyRate.getText().isEmpty() ||
                        tfLastName.getText().isEmpty() ||
                        tfPagibigNumber.getText().isEmpty() ||
                        tfPhilhealthNumber.getText().isEmpty() ||
                        tfPhoneAllowance.getText().isEmpty() ||
                        tfPhoneNumber.getText().isEmpty() ||
                        tfPosition.getText().isEmpty() ||
                        tfRiceAllowance.getText().isEmpty() ||
                        tfSSSNumber.getText().isEmpty() ||
                        tfStatus.getText().isEmpty() ||
                        tfTINNumber.getText().isEmpty()
        ) {
            CustomAlert customAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Empty field",
                    "Please ensure that all fields are filled out"
            );
            customAlert.showAndWait();
            return false;
        }
        // Checks if the inputs of textfield only have numbers or floats
        else if (
                tfBasicSalary.getText().matches("^[0-9]+(\\.[0-9]+)?$")
                        || tfGrossSemiMonthlyRate.getText().matches("^[0-9]+(\\.[0-9]+)?$")
                        || tfHourlyRate.getText().matches("^[0-9]+(\\.[0-9]+)?$")
                        || tfRiceAllowance.getText().matches("^[0-9]+(\\.[0-9]+)?$")
                        || tfClothingAllowance.getText().matches("^[0-9]+(\\.[0-9]+)?$")
                        || tfPhoneAllowance.getText().matches("^[0-9]+(\\.[0-9]+)?$")
        ) {
            return true;
        }
        else {
            return false; // it means that the fields are falsy, many invalids lol :>
        }
    }

    private void initialize() {
        setupDateHired();
    }

    private void setupDateHired() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        tfDateHired.setText(dtf.format(now));
    }
}