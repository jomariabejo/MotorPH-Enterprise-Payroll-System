package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import com.jomariabejo.motorph.service.AllowanceService;
import com.jomariabejo.motorph.service.DepartmentService;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.PositionService;
import com.jomariabejo.motorph.utility.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

public class HRViewEmployeeProfile {
    private final EmployeeService employeeService = new EmployeeService();
    private final AllowanceService allowanceService = new AllowanceService();
    private final DepartmentService departmentService = new DepartmentService();
    private final PositionService positionService = new PositionService();

    @FXML
    private Label lbl_name;

    @FXML
    private Button btn_edit;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private ImageView image_path;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_basicSalary;

    @FXML
    private TextField tf_bday;

    @FXML
    private TextField tf_clothingAllowance;

    @FXML
    private ComboBox tf_dept;

    @FXML
    private TextField tf_eid;

    @FXML
    private TextField tf_fname;

    @FXML
    private TextField tf_grossSemiMonthlyRate;

    @FXML
    private TextField tf_hourlyRate;

    @FXML
    private TextField tf_lname;

    @FXML
    private TextField tf_pagibigNum;

    @FXML
    private TextField tf_philhealthNum;

    @FXML
    private TextField tf_phoneAllowance;

    @FXML
    private TextField tf_phoneNum;

    @FXML
    private ComboBox tf_position;

    @FXML
    private TextField tf_riceSubsidy;

    @FXML
    private TextField tf_sssNum;

    @FXML
    private TextField tf_status;

    @FXML
    private TextField tf_supervisor;

    @FXML
    private TextField tf_tinNum;


    public HRViewEmployeeProfile() throws SQLException {
    }


    public void initData(int employeeId) throws SQLException {
        tf_eid.setText(String.valueOf(employeeId));
        displayEmployeeInformation();

        // Setup COmbobox
        setUpComboBox();
    }

    private void setUpComboBox() throws SQLException {
        tf_position.setItems(FXCollections.observableList(positionService.getPositionsName()));

        tf_dept.setItems(FXCollections.observableList(departmentService.getDepartmentsName()));
    }
    public void initData(int employeeId, boolean isModifying) throws SQLException {
        tf_eid.setText(String.valueOf(employeeId));
        displayEmployeeInformation();
    }

    public void setLblEmployeeId(String employeeId) {
        this.tf_eid.setText(employeeId);
    }

    public void displayEmployeeInformation() throws SQLException {

        // Get the URL to the image resource
        String imagePath = "/img/profile.jpg";
        URL imageUrl = getClass().getResource(imagePath);

        // Create an Image object from the URL
        Image newImage = new Image(imageUrl.toExternalForm());

        // Set the new image to the ImageView
        image_path.setImage(newImage);

        EmployeeService employeeService = new EmployeeService();
        Employee employee = employeeService.fetchEmployee(Integer.parseInt(tf_eid.getText()));

        lbl_name.setText(employee.getFirstName() + " " + employee.getLastName());
        tf_address.setText(employee.getAddress());
        tf_basicSalary.setText(String.valueOf(employee.getBasicSalary()));
        tf_bday.setText(employee.getBirthday().toString());

        tf_eid.setText(String.valueOf(employee.getEmployeeId()));
        tf_fname.setText(employee.getFirstName());
        tf_grossSemiMonthlyRate.setText(employee.getGrossSemiMonthlyRate().toString());
        tf_hourlyRate.setText(employee.getHourlyRate().toString());
        tf_lname.setText(employee.getLastName());
        tf_pagibigNum.setText(employee.getPagibig());
        tf_philhealthNum.setText(employee.getPhilhealth());
        tf_phoneNum.setText(employee.getContactNumber());
        tf_sssNum.setText(employee.getSss());
        tf_status.setText(employee.getStatus().toString());
        tf_supervisor.setText(employee.getSupervisor());
        tf_tinNum.setText(employee.getTin());

        /** Display employee position */
        PositionService positionService = new PositionService();
        String position = String.valueOf(positionService.getPosition(employee.getPositionId()).getName());

        /** Display employee department */
        DepartmentService departmentService = new DepartmentService();
        String departmentName = departmentService.getDepartment(employee.getDeptId()).getName();

        /** Display employee allowance */
        AllowanceService allowanceService = new AllowanceService();
        Allowance allowance = allowanceService.getAllowanceByEmployeeId(Integer.parseInt(tf_eid.getText()));

        tf_clothingAllowance.setText(String.valueOf(allowance.getClothingAllowance()));
        tf_phoneAllowance.setText(String.valueOf(allowance.getPhoneAllowance()));
        tf_riceSubsidy.setText(String.valueOf(allowance.getRiceAllowance()));

        tf_dept.setValue(switchDeptIdToDepartmentName(employee.getDeptId()));

        tf_position.setValue(switchPositionIdToPositionName(employee.getPositionId()));
    }


    public void editClicked(ActionEvent event) {
        isEditTextfield(false);
        tf_fname.requestFocus();
    }

    public void cancelClicked(ActionEvent event) {
        isEditTextfield(true);
    }


    public void saveClicked(ActionEvent event) throws ParseException, SQLException {

        /**
         * TODO: We are now able to create employee, however there is null values for 'date_hired' & 'is_active'
         * Due: 04/31/2024
         */
        if (tf_eid.getText().equals("Auto generate employee id")) {
            {
                boolean isEmployeeRecordConfirmed = AlertUtility.showConfirmation("Save new employee", "Are you sure you want to save these changes? Once confirmed, the data will be saved.",null);

                if (isEmployeeRecordConfirmed) {
                    try {
                        Employee employee = new Employee();
                        employee.setFirstName(tf_fname.getText());
                        employee.setLastName(tf_lname.getText());
                        employee.setAddress(tf_address.getText());
                        employee.setContactNumber(tf_phoneNum.getText());

                        try {
                            employee.setStatus(EmployeeStatus.valueOf(tf_status.getText()));
                        }
                        catch (Exception e) {
                            AlertUtility.showErrorAlert("Invalid Input", "Employee status should only be (Regular,Probationary)",e.getMessage());
                            tf_status.requestFocus();
                        }

                        employee.setSupervisor(tf_supervisor.getText());
                        employee.setSss(tf_sssNum.getText());
                        employee.setPhilhealth(tf_philhealthNum.getText());
                        employee.setPagibig(tf_pagibigNum.getText());
                        employee.setTin(tf_tinNum.getText());

                        String [] birthday = tf_bday.getText().split("/");
                        try {
                            String yyyy = birthday[0], mm = birthday[1], dd= birthday[2];
                            employee.setBirthday(new Date(Integer.valueOf(yyyy)-1_900, Integer.valueOf(mm), Integer.valueOf(dd)));
                        }
                        catch (Exception e) {
                            AlertUtility.showErrorAlert("Invalid birthday format", "Format example (mm/dd/yyyy)",e.getMessage());
                        }
                        employee.setBasicSalary(BigDecimalUtility.createBigDecimal(tf_basicSalary.getText()));
                        employee.setGrossSemiMonthlyRate(BigDecimalUtility.createBigDecimal(tf_grossSemiMonthlyRate.getText()));
                        employee.setHourlyRate(BigDecimalUtility.createBigDecimal(tf_hourlyRate.getText()));
                        employee.setEmployeeId(AutoIncrementUtility.getNextAutoIncrementValue());
                        employee.setPositionId(switchPositionNameToInt((tf_position.getSelectionModel().getSelectedItem().toString())));
                        employee.setEmployeeId(AutoIncrementUtility.getNextAutoIncrementValue());
                        employee.setPositionId(switchPositionNameToInt(tf_position.getSelectionModel().getSelectedItem().toString()));
                        employee.setDeptId(switchDepartmentNameToInt(tf_dept.getSelectionModel().getSelectedItem().toString()));
                        // Save the employee record

                        employeeService.saveEmployee(employee);
                        System.out.println(employee);
                        /**
                         * Create Allowance
                         */
                        Allowance allowance = new Allowance();
                        allowance.setEmployeeID(employee.getEmployeeId());
                        allowance.setDateModified(CurrentTimestampUtility.getCurrentTimestamp());
                        allowance.setPhoneAllowance(Integer.valueOf(tf_phoneAllowance.getText()));
                        allowance.setRiceAllowance(Integer.valueOf(tf_riceSubsidy.getText()));
                        allowance.setClothingAllowance(Integer.valueOf(tf_clothingAllowance.getText()));
                        allowance.setDateCreated(DateConversionUtility.toSqlDate(Calendar.getInstance().getTime()));
                        allowance.setTotalAmount(
                                allowance.getPhoneAllowance() + allowance.getRiceAllowance() + allowance.getClothingAllowance()
                        );
                        allowanceService.createAllowance(allowance);

                        // Hide current fxml window
                        Button saveButton = (Button) event.getSource();
                        saveButton.getScene().getWindow().hide();
                    }
                    catch (SQLException sqlException) {
                            AlertUtility.showErrorAlert("Invalid input",sqlException.getCause().toString(), sqlException.getMessage());
                        }
                    }
                }
            }

        // editing employee record
        else {
            boolean isEmployeeRecordConfirmed = AlertUtility.showConfirmation("Save Changes", "Modify Employee Record", "Are you sure you want to save these changes? Once confirmed, the data will be updated.");

            if (isEmployeeRecordConfirmed) {
                /**
                 * Update EMployee Record
                 */
                Employee employee = new Employee(
                        Integer.valueOf(tf_eid.getText()),tf_fname.getText(),tf_lname.getText(), DateConverter.parseSqlDate(tf_bday.getText()),
                        tf_address.getText(),
                        tf_phoneNum.getText(),
                        EmployeeStatus.valueOf(tf_status.getText()),
                        tf_supervisor.getText(),tf_sssNum.getText(),tf_philhealthNum.getText(),tf_pagibigNum.getText(),tf_tinNum.getText(),
                        BigDecimal.valueOf(Double.parseDouble(tf_basicSalary.getText())),
                        BigDecimal.valueOf(Double.parseDouble(tf_grossSemiMonthlyRate.getText())),
                        BigDecimal.valueOf(Double.parseDouble(tf_hourlyRate.getText()))

                );
                // Save the employee record

                AlertUtility.showInformation(String.valueOf(switchDepartmentNameToInt(tf_dept.getSelectionModel().getSelectedItem().toString())),null,null);
                employee.setDeptId(switchDepartmentNameToInt(tf_dept.getSelectionModel().getSelectedItem().toString()));

                System.out.println("DEPT : " + employee.getDeptId());
                System.out.println("DEPT SELECTED : " + tf_dept.getSelectionModel().getSelectedItem());
                employee.setPositionId(switchPositionNameToInt(tf_position.getSelectionModel().getSelectedItem().toString()));
                System.out.println("Position : " + employee.getPositionId());
                System.out.println("Position Selected : " + tf_position.getSelectionModel().getSelectedItem());

                employeeService.modifyEmployee(employee);
                System.out.println(employee);
                /**
                 * Update Allowance
                 */
                Allowance allowance = new Allowance();
                allowance.setEmployeeID(Integer.valueOf(tf_eid.getText()));
                allowance.setRiceAllowance(Integer.valueOf(tf_riceSubsidy.getText()));
                allowance.setPhoneAllowance(Integer.valueOf(tf_phoneAllowance.getText()));
                allowance.setClothingAllowance(Integer.valueOf(tf_clothingAllowance.getText()));
                allowance.setTotalAmount(
                        Integer.valueOf(tf_riceSubsidy.getText()) +
                                Integer.valueOf(tf_phoneAllowance.getText()) +
                                Integer.valueOf(tf_clothingAllowance.getText())
                );
                allowance.setDateModified(CurrentTimestampUtility.getCurrentTimestamp());
                allowanceService.updateAllowance(allowance);

                // Hide current fxml window
                Button saveButton = (Button) event.getSource();
                saveButton.getScene().getWindow().hide();
            }
        }
    }

    private void isEditTextfield(boolean isEdit) {
        tf_address.setDisable(isEdit);
        tf_basicSalary.setDisable(isEdit);
        tf_bday.setDisable(isEdit);
        tf_clothingAllowance.setDisable(isEdit);
        tf_dept.setDisable(isEdit);
        tf_fname.setDisable(isEdit);
        tf_grossSemiMonthlyRate.setDisable(isEdit);
        tf_hourlyRate.setDisable(isEdit);
        tf_lname.setDisable(isEdit);
        tf_pagibigNum.setDisable(isEdit);
        tf_philhealthNum.setDisable(isEdit);
        tf_phoneAllowance.setDisable(isEdit);
        tf_phoneNum.setDisable(isEdit);
        tf_position.setDisable(isEdit);
        tf_riceSubsidy.setDisable(isEdit);
        tf_sssNum.setDisable(isEdit);
        tf_status.setDisable(isEdit);
        tf_supervisor.setDisable(isEdit);
        tf_tinNum.setDisable(isEdit);
    }

    public void moreinfoClicked(ActionEvent event) {
    }

    private String switchDeptIdToDepartmentName(int i) {
        String deptName = "";
        switch (i) {
            case 1:
                deptName = "Executive Management";
                break;
            case 2:
                deptName = "Information Technology";
                break;
            case 3:
                deptName = "Human Resources";
                break;
            case 4:
                deptName = "Accounting";
                break;
            case 5:
                deptName = "Sales and Marketing";
                break;
            case 6:
                deptName = "Supply Chain and Logistics";
                break;
            case 7:
                deptName = "Customer Service";
                break;
            case 8:
                deptName = "Finance";
                break;
        }
        return deptName;
    }
    private int switchDepartmentNameToInt(String deptName) {
        int deptId = 0;
        switch (deptName) {
            case "Executive Management":
                deptId = 1;
                break;
            case "Information Technology":
                deptId = 2;
                break;
            case "Human Resources":
                deptId = 3;
                break;
            case "Accounting":
                deptId = 4;
                break;
            case "Sales and Marketing":
                deptId = 5;
                break;
            case "Supply Chain and Logistics":
                deptId = 6;
                break;
            case "Customer Service":
                deptId = 7;
                break;
            case "Finance":
                deptId = 8;
                break;
        }
        return deptId;
    }


    private int switchPositionNameToInt(String positionName) {
        int positionId = 0;
        switch (positionName) {
            case "Chief Executive Officer":
                positionId = 1;
                break;
            case "Chief Operating Officer":
                positionId = 2;
                break;
            case "Chief Finance Officer":
                positionId = 3;
                break;
            case "Chief Marketing Officer":
                positionId = 4;
                break;
            case "IT Operations and Systems":
                positionId = 5;
                break;
            case "HR Manager":
                positionId = 6;
                break;
            case "HR Team Leader":
                positionId = 7;
                break;
            case "HR Rank and File":
                positionId = 8;
                break;
            case "Payroll Manager":
                positionId = 9;

                break;
            case "Payroll Team Leader":
                positionId = 10;
                break;
            case "Payroll Rank and File":
                positionId = 11;
                break;
            case "Accounting Head":
                positionId = 12;
                break;
            case "Account Manager":
                positionId = 13;
                break;
            case "Account Team Leader":
                positionId = 14;
                break;
            case "Account Rank and File":
                positionId = 15;
                break;
            case "Sales & Marketing":
                positionId = 16;
                break;
            case "Supply Chain and Logistics":
                positionId = 17;
                break;
            case "Customer Service and Relations":
                positionId = 18;
                break;
        }
        return positionId;
    }

    private String switchPositionIdToPositionName(int positionId) {
        String positionName = "";
        switch (positionId) {
            case 1:
                positionName = "Chief Executive Officer";
                break;
            case 2:
                positionName = "Chief Operating Officer";
                break;
            case 3:
                positionName = "Chief Finance Officer";
                break;
            case 4:
                positionName = "Chief Marketing Officer";
                break;
            case 5:
                positionName =  "IT Operations and Systems";
                break;
            case 6:
                positionName =  "HR Manager";
                break;
            case 7:
                positionName = "HR Team Leader";
                break;
            case 8:
                positionName =  "HR Rank and File";
                break;
            case 9:
                positionName =  "Payroll Manager";
                break;
            case 10:
                positionName = "Payroll Team Leader";
                break;
            case 11:
                positionName =  "Payroll Rank and File";
                break;
            case 12:
                positionName =  "Accounting Head";
                break;
            case 13:
                positionName =  "Account Manager";
                break;
            case 14:
                positionName = "Account Team Leader";
                break;
            case 15:
                positionName =  "Account Rank and File";
                break;
            case 16:
                positionName = "Sales & Marketing";
                break;
            case 17:
                positionName =  "Supply Chain and Logistics";
                break;
            case 18:
                positionName =  "Customer Service and Relations";
                break;
        }
        return positionName;
    }


    public void buttonVisible() {
        btn_save.setVisible(true);
        btn_cancel.setVisible(true);
        btn_edit.setVisible(true);
    }

    public void generateEmployeeIdNumber() {
        tf_eid.setText("Auto generate employee id");
    }

    public void setComboBox() throws SQLException {
        setUpComboBox();
    }
}
