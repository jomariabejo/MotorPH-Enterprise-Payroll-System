package com.jomariabejo.motorph.controller.role.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class MoreInfoController {

    private EmployeeProfileController employeeProfileController;

    @FXML
    private BorderPane employeeMoreInfoBorderPane;


    public MoreInfoController() {

    }

    @FXML
    private BorderPane moreInfoBorderPane;

    @FXML
    void allowanceClicked() {
        this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / Profile / Allowance Structure /");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-allowance-structure.fxml"));
            VBox vBox = fxmlLoader.load();

            this.employeeMoreInfoBorderPane.setCenter(vBox);

            AllowanceStructureController allowanceStructureController = fxmlLoader.getController();
            allowanceStructureController.setMoreInfoController(this);
            allowanceStructureController.rewriteTextfields();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    @FXML
    void personalInformationClicked() throws IOException {
        this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / Profile / Personal Information");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-personal-information.fxml"));
        VBox vBox = fxmlLoader.load();

        this.employeeMoreInfoBorderPane.setCenter(vBox);

        PersonalInformationController personalInformationController = fxmlLoader.getController();
        personalInformationController.setMoreInfoController(this);
        personalInformationController.rewriteTextfields();
    }

    @FXML
    void salaryStructureClicked() {
        this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / Profile / Salary Structure");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-salary-structure.fxml"));
            VBox vBox = fxmlLoader.load();

            this.employeeMoreInfoBorderPane.setCenter(vBox);

            SalaryStructureController salaryStructureController = fxmlLoader.getController();
            salaryStructureController.setMoreInfoController(this);
            salaryStructureController.rewriteTextfields();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void accountNumbersClicked() {
        this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / Profile / Account Numbers");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-account-numbers.fxml"));
            VBox vBox = fxmlLoader.load();

            this.employeeMoreInfoBorderPane.setCenter(vBox);

            AccountNumbersController accountNumbersController = fxmlLoader.getController();
            accountNumbersController.setMoreInfoController(this);
            accountNumbersController.rewriteTextfields();
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void otherInfoClicked(ActionEvent actionEvent) {
        this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / Profile / Other Info /");



    }
}
