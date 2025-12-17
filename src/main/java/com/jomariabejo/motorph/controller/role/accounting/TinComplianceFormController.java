package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.TinCompliance;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TinComplianceFormController {

    private TinComplianceController tinComplianceController;
    private TinCompliance tinCompliance;

    @FXML
    private ComboBox<Employee> cbEmployee;

    @FXML
    private TextField tfTINNumber;

    @FXML
    private DatePicker dpDateRegistered;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (validateFields()) {
            if (tinCompliance == null) {
                createTinCompliance();
            } else {
                updateTinCompliance();
            }
        }
    }

    public void setup() {
        populateEmployees();
        
        if (tinCompliance != null) {
            loadTinComplianceData();
        } else {
            dpDateRegistered.setValue(LocalDate.now());
        }
    }

    private void populateEmployees() {
        List<Employee> employees = tinComplianceController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory().getEmployeeService().getAllEmployees();
        cbEmployee.setItems(FXCollections.observableList(employees));
        cbEmployee.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
        cbEmployee.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    private void loadTinComplianceData() {
        cbEmployee.getSelectionModel().select(tinCompliance.getEmployeeID());
        tfTINNumber.setText(tinCompliance.getTINNumber());
        dpDateRegistered.setValue(tinCompliance.getDateRegistered());
    }

    private boolean validateFields() {
        if (cbEmployee.getSelectionModel().getSelectedItem() == null ||
                tfTINNumber.getText().isEmpty() ||
                dpDateRegistered.getValue() == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void createTinCompliance() {
        TinCompliance newTinCompliance = new TinCompliance();
        mapFieldsToTinCompliance(newTinCompliance);
        
        tinComplianceController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getTinComplianceService().saveTinCompliance(newTinCompliance);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "TIN Compliance created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        tinComplianceController.populateTinCompliance();
    }

    private void updateTinCompliance() {
        mapFieldsToTinCompliance(tinCompliance);
        
        tinComplianceController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getTinComplianceService().updateTinCompliance(tinCompliance);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "TIN Compliance updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        tinComplianceController.populateTinCompliance();
    }

    private void mapFieldsToTinCompliance(TinCompliance tinCompliance) {
        tinCompliance.setEmployeeID(cbEmployee.getSelectionModel().getSelectedItem());
        tinCompliance.setTINNumber(tfTINNumber.getText());
        tinCompliance.setDateRegistered(dpDateRegistered.getValue());
    }
}










