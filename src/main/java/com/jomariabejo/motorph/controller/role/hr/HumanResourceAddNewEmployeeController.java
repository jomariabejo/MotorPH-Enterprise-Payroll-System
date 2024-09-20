package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

public class HumanResourceAddNewEmployeeController {

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
        submitBtn.getScene().getWindow().hide();
    }

    private Employee mapEmployeeFields() {
        Employee employee = new Employee();
        return employee;
    }

    public void setEmployeeController(EmployeeController employeeController) {
    }

    public void addIcons() {
    }

    public void addButtonColor() {
    }
}
