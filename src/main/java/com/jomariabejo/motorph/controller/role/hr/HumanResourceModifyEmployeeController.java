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
import java.util.Optional;

@Getter
@Setter
public class HumanResourceModifyEmployeeController {

    private EmployeeController employeeController;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfBasicSalary;

    @FXML
    private TextField tfClothingAllowance;

    @FXML
    private TextField tfDateHired;

    @FXML
    private TextField tfEmployeeNumber;

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
        tfEmployeeNumber.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(Integer.valueOf(tfEmployeeNumber.getText()));
        employee.setFirstName(tfFirstName.getText());
        employee.setLastName(tfLastName.getText());
        employee.setBirthday(Date.valueOf(birthdayDatePicker.getValue()));
        employee.setAddress(tfAddress.getText());
        employee.setClothingAllowance((new BigDecimal(tfClothingAllowance.getText())));
        employee.setPhoneAllowance((new BigDecimal(tfPhoneAllowance.getText())));
        employee.setRiceSubsidy(new BigDecimal(tfRiceAllowance.getText()));
        employee.setPhoneNumber(tfPhoneNumber.getText());
        employee.setStatus(tfStatus.getText());
        employee.setSSSNumber(tfSSSNumber.getText());
        employee.setHourlyRate(new BigDecimal(tfHourlyRate.getText()));
        employee.setBasicSalary(new BigDecimal(tfBasicSalary.getText()));
        employee.setGrossSemiMonthlyRate(new BigDecimal(tfGrossSemiMonthlyRate.getText()));
        employee.setPositionID(findPositionByName());
        employee.setDateHired(Date.valueOf(tfDateHired.getText()));
        employee.setPagibigNumber(tfPagibigNumber.getText());
        employee.setPhilhealthNumber(tfPhilhealthNumber.getText());
        employee.setTINNumber(tfTINNumber.getText());

        executeModification(employee);
    }

    private void executeModification(Employee employee) {
        boolean isSuccessful = false;
        try {
            displayConfirmation(employee);
            isSuccessful = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            isSuccessful = false;
        }
        finally {
            if (isSuccessful) {
                displaySuccessModification();
            }
            else {
                displayFailedModification();
            }
        }
    }

    private void displayConfirmation(Employee employee) {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Modification confirmation", "Are you sure you want to modify employee record? ");
        Optional<ButtonType> result = customAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            this.employeeController
                    .getHumanResourceAdministratorNavigationController()
                    .getMainViewController()
                    .getServiceFactory()
                    .getEmployeeService()
                    .updateEmployee(employee);
        }
        else {
            System.out.println("Im not executed.");
        }
    }

    private void displayFailedModification() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Modification Failed", "Employee record modification failed.");
        customAlert.showAndWait();

    }

    private void displaySuccessModification() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Modified successfully", "Employee record modified successfully.");
        customAlert.showAndWait();
    }

    private Position findPositionByName() {
        return this.employeeController.getHumanResourceAdministratorNavigationController().getMainViewController()
                .getServiceFactory().getPositionService().getPositionByName(tfPosition.getText());
    }

    public void injectEmployee(Employee selectedEmployee) {
        birthdayDatePicker.setValue(selectedEmployee.getBirthday().toLocalDate());
        tfAddress.setText(selectedEmployee.getAddress());
        tfBasicSalary.setText(String.valueOf(selectedEmployee.getBasicSalary()));
        tfClothingAllowance.setText(String.valueOf(selectedEmployee.getClothingAllowance()));
        tfDateHired.setText(String.valueOf(selectedEmployee.getDateHired()));
        tfEmployeeNumber.setText(String.valueOf(selectedEmployee.getEmployeeNumber()));
        tfFirstName.setText(selectedEmployee.getFirstName());
        tfGrossSemiMonthlyRate.setText(String.valueOf(selectedEmployee.getGrossSemiMonthlyRate()));
        tfHourlyRate.setText(String.valueOf(selectedEmployee.getHourlyRate()));
        tfLastName.setText(selectedEmployee.getLastName());
        tfPagibigNumber.setText(selectedEmployee.getPhilhealthNumber());
        tfPhilhealthNumber.setText(selectedEmployee.getPhilhealthNumber());
        tfPhoneAllowance.setText(String.valueOf(selectedEmployee.getPhoneAllowance()));
        tfPhoneNumber.setText(selectedEmployee.getPhoneNumber());
        tfPosition.setText(selectedEmployee.getPositionID().getPositionName());
        tfRiceAllowance.setText(String.valueOf(selectedEmployee.getRiceSubsidy()));
        tfSSSNumber.setText(selectedEmployee.getSSSNumber());
        tfStatus.setText(selectedEmployee.getStatus());
        tfTINNumber.setText(selectedEmployee.getTINNumber());
    }
}
