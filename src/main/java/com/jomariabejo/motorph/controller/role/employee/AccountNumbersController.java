package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountNumbersController {

    private MoreInfoController moreInfoController;

    @FXML
    private TextField tfPagibigNumber;

    @FXML
    private TextField tfPhilhealthNumber;

    @FXML
    private TextField tfSSSNumber;

    @FXML
    private TextField tfTaxPayerIdentificationNumber;

    public AccountNumbersController() {
    }

    public void rewriteTextfields() {
        tfPagibigNumber.setText(
                this.moreInfoController
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getPagibigNumber()
        );

        tfPhilhealthNumber.setText(
                this.moreInfoController
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getPhilhealthNumber()
        );
        tfSSSNumber.setText(
                this.moreInfoController
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getSSSNumber()
        );
        tfTaxPayerIdentificationNumber.setText(
                this.moreInfoController
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getTINNumber()
        );
    }
}
