package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.model.Bonus;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BonusFormController {

    private BonusController bonusController;
    private Bonus bonus;

    @FXML
    private ComboBox<Employee> cbEmployee;

    @FXML
    private TextField tfBonusAmount;

    @FXML
    private DatePicker dpBonusDate;

    @FXML
    private TextArea taDescription;

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
            if (bonus == null) {
                createBonus();
            } else {
                updateBonus();
            }
        }
    }

    public void setup() {
        populateEmployees();
        
        if (bonus != null) {
            loadBonusData();
        } else {
            dpBonusDate.setValue(LocalDate.now());
            tfCreatedBy.setText(getCurrentUserFullName());
        }
    }

    private String getCurrentUserFullName() {
        return bonusController.getPayrollAdministratorNavigationController()
                .getMainViewController().getUser().getFullName();
    }

    private void populateEmployees() {
        List<Employee> employees = bonusController.getPayrollAdministratorNavigationController()
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

    private void loadBonusData() {
        cbEmployee.getSelectionModel().select(bonus.getEmployeeNumber());
        tfBonusAmount.setText(bonus.getBonusAmount().toString());
        dpBonusDate.setValue(bonus.getBonusDate());
        taDescription.setText(bonus.getDescription());
        tfCreatedBy.setText(bonus.getCreatedBy());
    }

    private boolean validateFields() {
        if (cbEmployee.getSelectionModel().getSelectedItem() == null ||
                tfBonusAmount.getText().isEmpty() ||
                dpBonusDate.getValue() == null ||
                taDescription.getText().isEmpty() ||
                tfCreatedBy.getText().isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }

        try {
            new BigDecimal(tfBonusAmount.getText());
        } catch (NumberFormatException e) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a valid bonus amount."
            );
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void createBonus() {
        Bonus newBonus = new Bonus();
        mapFieldsToBonus(newBonus);
        newBonus.setCreatedDate(Instant.now());
        
        bonusController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getBonusService().saveBonus(newBonus);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Bonus created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        bonusController.populateBonuses();
    }

    private void updateBonus() {
        mapFieldsToBonus(bonus);
        bonus.setLastModifiedBy(getCurrentUserFullName());
        bonus.setLastModifiedDate(Instant.now());
        
        bonusController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getBonusService().updateBonus(bonus);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Bonus updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        bonusController.populateBonuses();
    }

    private void mapFieldsToBonus(Bonus bonus) {
        bonus.setEmployeeNumber(cbEmployee.getSelectionModel().getSelectedItem());
        bonus.setBonusAmount(new BigDecimal(tfBonusAmount.getText()));
        bonus.setBonusDate(dpBonusDate.getValue());
        bonus.setDescription(taDescription.getText());
        bonus.setCreatedBy(tfCreatedBy.getText());
    }
}










