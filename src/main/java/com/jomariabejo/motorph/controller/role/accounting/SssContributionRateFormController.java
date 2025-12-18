package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.model.SssContributionRate;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class SssContributionRateFormController {

    private SssContributionRateController rateController;
    private SssContributionRate rate;

    @FXML
    private TextField tfSalaryBracketFrom;

    @FXML
    private TextField tfSalaryBracketTo;

    @FXML
    private TextField tfEmployeeShare;

    @FXML
    private TextField tfEmployerShare;

    @FXML
    private DatePicker dpEffectiveDate;

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
            if (rate == null) {
                createRate();
            } else {
                updateRate();
            }
        }
    }

    public void setup() {
        if (rate != null) {
            loadRateData();
        } else {
            dpEffectiveDate.setValue(LocalDate.now());
        }
    }

    private void loadRateData() {
        tfSalaryBracketFrom.setText(rate.getSalaryBracketFrom().toString());
        tfSalaryBracketTo.setText(rate.getSalaryBracketTo().toString());
        tfEmployeeShare.setText(rate.getEmployeeShare().toString());
        tfEmployerShare.setText(rate.getEmployerShare().toString());
        dpEffectiveDate.setValue(rate.getEffectiveDate());
    }

    private boolean validateFields() {
        if (tfSalaryBracketFrom.getText().isEmpty() ||
                tfSalaryBracketTo.getText().isEmpty() ||
                tfEmployeeShare.getText().isEmpty() ||
                tfEmployerShare.getText().isEmpty() ||
                dpEffectiveDate.getValue() == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }

        try {
            BigDecimal from = new BigDecimal(tfSalaryBracketFrom.getText());
            BigDecimal to = new BigDecimal(tfSalaryBracketTo.getText());
            if (from.compareTo(to) > 0) {
                CustomAlert alert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Validation Error",
                        "Salary bracket 'From' must be less than 'To'."
                );
                alert.showAndWait();
                return false;
            }
            new BigDecimal(tfEmployeeShare.getText());
            new BigDecimal(tfEmployerShare.getText());
        } catch (NumberFormatException e) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter valid numeric values."
            );
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void createRate() {
        SssContributionRate newRate = new SssContributionRate();
        mapFieldsToRate(newRate);
        
        rateController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getSssContributionRateService().saveSssContributionRate(newRate);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "SSS contribution rate created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        rateController.populateRates();
    }

    private void updateRate() {
        mapFieldsToRate(rate);
        
        rateController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getSssContributionRateService().updateSssContributionRate(rate);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "SSS contribution rate updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        rateController.populateRates();
    }

    private void mapFieldsToRate(SssContributionRate rate) {
        rate.setSalaryBracketFrom(new BigDecimal(tfSalaryBracketFrom.getText()));
        rate.setSalaryBracketTo(new BigDecimal(tfSalaryBracketTo.getText()));
        rate.setEmployeeShare(new BigDecimal(tfEmployeeShare.getText()));
        rate.setEmployerShare(new BigDecimal(tfEmployerShare.getText()));
        rate.setEffectiveDate(dpEffectiveDate.getValue());
    }
}












