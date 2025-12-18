package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Position;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import javafx.stage.Stage;

@Getter
@Setter
public class HumanResourceAddNewEmployeeController {

    private static final String NUMBER_PATTERN = "^[0-9]+(\\.[0-9]+)?$";
    
    private EmployeeController employeeController;
    private ObservableList<Position> allPositionsList;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private DatePicker dpBirthday;

    @FXML
    private DatePicker dpDateHired;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfBasicSalary;

    @FXML
    private TextField tfClothingAllowance;

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
    private ComboBox<Position> cbPosition;

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

    private Employee editingEmployee;

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (fieldsValidated()) {
            try {
                // The new/updated employee
                Employee employee = mapFieldsToEmployee();

                if (editingEmployee != null) {
                    // Update existing employee
                    employee.setEmployeeNumber(editingEmployee.getEmployeeNumber());
                    this.getEmployeeController()
                        .getHumanResourceAdministratorNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getEmployeeService()
                        .updateEmployee(employee);
                    
                    // hide window
                    submitBtn.getScene().getWindow().hide();
                    
                    // display success message
                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "SUCCESS",
                            "Employee has been updated successfully!\n\n" +
                            "Employee: " + employee.getFirstName() + " " + employee.getLastName() + "\n" +
                            "Employee Number: " + employee.getEmployeeNumber()
                    );
                    successAlert.showAndWait();
                } else {
                    // Save new employee to db
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
                    
                    // display success message
                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "SUCCESS",
                            "Employee has been saved successfully!\n\n" +
                            "Employee: " + employee.getFirstName() + " " + employee.getLastName() + "\n" +
                            "Employee Number: " + employee.getEmployeeNumber() + "\n\n" +
                            "Leave balances have been initialized."
                    );
                    successAlert.showAndWait();
                }
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "ERROR",
                        "Failed to save employee:\n\n" + e.getMessage() + "\n\n" +
                        "Please check all fields and try again."
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private Employee mapFieldsToEmployee() {
        Employee employee = new Employee();
        employee.setFirstName(tfFirstName.getText());
        employee.setLastName(tfLastName.getText());
        employee.setRiceSubsidy(new BigDecimal(tfRiceAllowance.getText()));
        employee.setPhoneAllowance(new BigDecimal(tfPhoneAllowance.getText()));
        employee.setPhoneNumber(tfPhoneNumber.getText());
        
        // Convert LocalDate from DatePicker to java.sql.Date
        LocalDate dateHired = dpDateHired.getValue();
        if (dateHired != null) {
            employee.setDateHired(Date.valueOf(dateHired));
        }
        
        employee.setAddress(tfAddress.getText());
        employee.setPhoneNumber(tfPhoneNumber.getText());

        employee.setSSSNumber(tfSSSNumber.getText());
        employee.setPhilhealthNumber(tfPhilhealthNumber.getText());
        employee.setTINNumber(tfTINNumber.getText());
        employee.setPagibigNumber(tfPagibigNumber.getText());
        employee.setStatus(tfStatus.getText());
        employee.setPositionID(cbPosition.getValue());

        // salary
        employee.setBasicSalary(new BigDecimal(tfBasicSalary.getText()));
        employee.setGrossSemiMonthlyRate(new BigDecimal(tfGrossSemiMonthlyRate.getText()));
        employee.setHourlyRate(new BigDecimal(tfHourlyRate.getText()));

        // allowance
        employee.setRiceSubsidy(new BigDecimal(tfRiceAllowance.getText()));
        employee.setPhoneAllowance(new BigDecimal(tfPhoneAllowance.getText()));
        employee.setClothingAllowance(new BigDecimal(tfClothingAllowance.getText()));

        // Convert LocalDate from DatePicker to java.sql.Date
        LocalDate birthday = dpBirthday.getValue();
        if (birthday != null) {
            employee.setBirthday(Date.valueOf(birthday));
        }
        
        return employee;
    }

    public void addIcons() {
        // Icons will be added here if needed in the future
    }

    public void addButtonColor() {
        // Button colors will be added here if needed in the future
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
                        dpDateHired.getValue() == null ||
                        tfAddress.getText().isEmpty() ||
                        tfBasicSalary.getText().isEmpty() ||
                        tfClothingAllowance.getText().isEmpty() ||
                        tfFirstName.getText().isEmpty() ||
                        tfGrossSemiMonthlyRate.getText().isEmpty() ||
                        tfHourlyRate.getText().isEmpty() ||
                        tfLastName.getText().isEmpty() ||
                        tfPagibigNumber.getText().isEmpty() ||
                        tfPhilhealthNumber.getText().isEmpty() ||
                        tfPhoneAllowance.getText().isEmpty() ||
                        tfPhoneNumber.getText().isEmpty() ||
                        cbPosition.getValue() == null ||
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
                tfBasicSalary.getText().matches(NUMBER_PATTERN)
                        && tfGrossSemiMonthlyRate.getText().matches(NUMBER_PATTERN)
                        && tfHourlyRate.getText().matches(NUMBER_PATTERN)
                        && tfRiceAllowance.getText().matches(NUMBER_PATTERN)
                        && tfClothingAllowance.getText().matches(NUMBER_PATTERN)
                        && tfPhoneAllowance.getText().matches(NUMBER_PATTERN)
        ) {
            return true;
        }
        else {
            CustomAlert customAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Invalid number format",
                    "Please ensure that salary and allowance fields contain valid numbers"
            );
            customAlert.showAndWait();
            return false;
        }
    }

    @FXML
    private void initialize() {
        setupDateHired();
        setupPositionComboBox();
    }

    private void setupDateHired() {
        // Set default date hired to today
        dpDateHired.setValue(LocalDate.now());
    }

    private void populatePositions() {
        if (employeeController != null && cbPosition != null) {
            List<Position> positions = this.employeeController
                    .getHumanResourceAdministratorNavigationController()
                    .getMainViewController()
                    .getServiceFactory()
                    .getPositionService()
                    .getAllPositions();
            
            allPositionsList = FXCollections.observableArrayList(positions);
            cbPosition.setItems(allPositionsList);
        }
    }

    private void setupPositionComboBox() {
        if (cbPosition == null) {
            return;
        }
        
        // Set up StringConverter to display position names
        cbPosition.setConverter(new StringConverter<Position>() {
            @Override
            public String toString(Position position) {
                return position == null ? "" : position.getPositionName();
            }

            @Override
            public Position fromString(String string) {
                // When user types in editable ComboBox, try to find matching position
                if (string == null || string.isEmpty()) {
                    return null;
                }
                
                // Search for position by name (case-insensitive) in current items
                if (cbPosition.getItems() != null && !cbPosition.getItems().isEmpty()) {
                    return cbPosition.getItems().stream()
                            .filter(p -> p.getPositionName().equalsIgnoreCase(string))
                            .findFirst()
                            .orElse(null);
                }
                return null;
            }
        });
    }

    /**
     * Call this method after employeeController is set to populate positions and set up filtering
     */
    public void setupAfterControllerSet() {
        populatePositions();
        setupPositionFiltering();
    }

    /**
     * Populate form fields with employee data for editing
     */
    public void populateFormWithEmployee(Employee employee) {
        if (employee == null) {
            return;
        }
        
        editingEmployee = employee;
        
        // Set text fields
        tfFirstName.setText(employee.getFirstName());
        tfLastName.setText(employee.getLastName());
        tfAddress.setText(employee.getAddress());
        tfPhoneNumber.setText(employee.getPhoneNumber());
        tfSSSNumber.setText(employee.getSSSNumber());
        tfPhilhealthNumber.setText(employee.getPhilhealthNumber());
        tfTINNumber.setText(employee.getTINNumber());
        tfPagibigNumber.setText(employee.getPagibigNumber());
        tfStatus.setText(employee.getStatus());
        
        // Set date pickers
        if (employee.getBirthday() != null) {
            dpBirthday.setValue(employee.getBirthday().toLocalDate());
        }
        if (employee.getDateHired() != null) {
            dpDateHired.setValue(employee.getDateHired().toLocalDate());
        }
        
        // Set position
        if (employee.getPositionID() != null) {
            cbPosition.setValue(employee.getPositionID());
        }
        
        // Set salary fields
        if (employee.getBasicSalary() != null) {
            tfBasicSalary.setText(employee.getBasicSalary().toString());
        }
        if (employee.getGrossSemiMonthlyRate() != null) {
            tfGrossSemiMonthlyRate.setText(employee.getGrossSemiMonthlyRate().toString());
        }
        if (employee.getHourlyRate() != null) {
            tfHourlyRate.setText(employee.getHourlyRate().toString());
        }
        
        // Set allowance fields
        if (employee.getRiceSubsidy() != null) {
            tfRiceAllowance.setText(employee.getRiceSubsidy().toString());
        }
        if (employee.getPhoneAllowance() != null) {
            tfPhoneAllowance.setText(employee.getPhoneAllowance().toString());
        }
        if (employee.getClothingAllowance() != null) {
            tfClothingAllowance.setText(employee.getClothingAllowance().toString());
        }
        
        // Update window title
        if (submitBtn != null && submitBtn.getScene() != null && submitBtn.getScene().getWindow() != null) {
            ((Stage) submitBtn.getScene().getWindow()).setTitle("Edit Employee");
        }
    }

    private void setupPositionFiltering() {
        if (cbPosition == null || employeeController == null || allPositionsList == null) {
            return;
        }

        // Add listener to filter positions as user types
        cbPosition.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (allPositionsList == null || allPositionsList.isEmpty()) {
                return;
            }
            
            if (newValue == null || newValue.isEmpty()) {
                // Show all positions when text is cleared
                cbPosition.setItems(allPositionsList);
            } else {
                // Filter positions based on typed text
                ObservableList<Position> filtered = FXCollections.observableArrayList(
                        allPositionsList.stream()
                                .filter(p -> p.getPositionName().toLowerCase().contains(newValue.toLowerCase()))
                                .toList()
                );
                // Only update if we have filtered results or if we're clearing the filter
                cbPosition.setItems(filtered.isEmpty() ? allPositionsList : filtered);
            }
        });
    }
}