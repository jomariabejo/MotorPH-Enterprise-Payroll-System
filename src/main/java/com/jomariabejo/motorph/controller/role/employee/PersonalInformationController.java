package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalInformationController {

    private MoreInfoController moreInfoController;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfDateOfBirth;

    @FXML
    private TextField tfDepartment;

    @FXML
    private TextField tfEmployeeNumber;

    @FXML
    private TextField tfEmployeeStatus;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private TextField tfPosition;

    public PersonalInformationController() {
    }

    public void rewriteTextfields() {
        tfAddress.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee().getAddress()
        );
        tfDateOfBirth.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee().getBirthday().toString()
        );
        tfDepartment.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee().getPositionID().getDepartmentID().getDepartmentName()
        );
        tfEmployeeNumber.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getEmployeeNumber()
                        .toString()
        );
        tfEmployeeStatus.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getStatus()
        );
        tfFirstName.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getFirstName()
        );
        tfLastName.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getLastName()
        );
        tfPhoneNumber.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getPhoneNumber()
        );
        tfPosition.setText(
                this.getMoreInfoController()
                        .getEmployeeProfileController()
                        .getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getEmployee()
                        .getPositionID()
                        .getPositionName()
        );
    }
}
