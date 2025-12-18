package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.sql.Date;

@Getter
@Setter
public class PayrollFormController {

    private PayrollController payrollController;
    private Payroll payroll;

    @FXML
    private DatePicker dpPayrollRunDate;

    @FXML
    private DatePicker dpPeriodStartDate;

    @FXML
    private DatePicker dpPeriodEndDate;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private TextField tfCreatedBy;

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
            if (payroll == null) {
                createPayroll();
            } else {
                updatePayroll();
            }
        }
    }

    public void setup() {
        populateStatus();
        
        if (payroll != null) {
            loadPayrollData();
        } else {
            dpPayrollRunDate.setValue(LocalDate.now());
            tfCreatedBy.setText(getCurrentUserFullName());
            cbStatus.getSelectionModel().select("Draft");
        }
    }

    private String getCurrentUserFullName() {
        return payrollController.getPayrollAdministratorNavigationController()
                .getMainViewController().getUser().getFullName();
    }

    private void populateStatus() {
        cbStatus.setItems(javafx.collections.FXCollections.observableArrayList(
                "Draft", "Pending", "Approved", "Processed", "Cancelled"
        ));
    }

    private void loadPayrollData() {
        if (payroll.getPayrollRunDate() != null) {
            dpPayrollRunDate.setValue(payroll.getPayrollRunDate().toLocalDate());
        }
        if (payroll.getPeriodStartDate() != null) {
            dpPeriodStartDate.setValue(payroll.getPeriodStartDate().toLocalDate());
        }
        if (payroll.getPeriodEndDate() != null) {
            dpPeriodEndDate.setValue(payroll.getPeriodEndDate().toLocalDate());
        }
        cbStatus.getSelectionModel().select(payroll.getStatus());
        if (payroll.getCreatedBy() != null) {
            tfCreatedBy.setText(payroll.getCreatedBy());
        }
    }

    private boolean validateFields() {
        if (dpPayrollRunDate.getValue() == null ||
                dpPeriodStartDate.getValue() == null ||
                dpPeriodEndDate.getValue() == null ||
                cbStatus.getSelectionModel().getSelectedItem() == null ||
                tfCreatedBy.getText().isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }

        if (dpPeriodStartDate.getValue().isAfter(dpPeriodEndDate.getValue())) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Period start date must be before period end date."
            );
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void createPayroll() {
        Payroll newPayroll = new Payroll();
        mapFieldsToPayroll(newPayroll);
        newPayroll.setCreatedDate(Instant.now());
        
        payrollController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getPayrollService().savePayroll(newPayroll);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Payroll created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        payrollController.populatePayrolls();
    }

    private void updatePayroll() {
        mapFieldsToPayroll(payroll);
        payroll.setLastModifiedBy(getCurrentUserFullName());
        payroll.setLastModifiedDate(Instant.now());
        
        payrollController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getPayrollService().updatePayroll(payroll);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Payroll updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        payrollController.populatePayrolls();
    }

    private void mapFieldsToPayroll(Payroll payroll) {
        payroll.setPayrollRunDate(Date.valueOf(dpPayrollRunDate.getValue()));
        payroll.setPeriodStartDate(Date.valueOf(dpPeriodStartDate.getValue()));
        payroll.setPeriodEndDate(Date.valueOf(dpPeriodEndDate.getValue()));
        payroll.setStatus(cbStatus.getSelectionModel().getSelectedItem());
        payroll.setCreatedBy(tfCreatedBy.getText());
    }
}










