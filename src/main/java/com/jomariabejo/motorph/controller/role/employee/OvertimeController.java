package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

@Getter
@Setter
public class OvertimeController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Button overtimeRequestBtn;

    @FXML
    private void initialize() {
        enhanceOvertimeBtn();
    }

    public OvertimeController() {
    }

    public void cbMonthClicked(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void cbYearClicked(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void cbStatusChanged(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void fileOvertimeRequestClicked(ActionEvent actionEvent) {
        /**
         * TODO: Display the overtime application form
         * 1. Create overtime application form FXML
         * 2. Name the controller of the form to (FileOvertimeRequestController)
         * 3. Create functionality of the form
         * 4. Make sure the required fields is visible
         * 5. You can only submit one overtime request daily.
         *
         */
    }

    public void enhanceOvertimeBtn() {
        overtimeRequestBtn.setGraphic(new FontIcon(Material.FILE_UPLOAD));
        overtimeRequestBtn.getStyleClass().addAll(
                Styles.SUCCESS
        );
    }

    private void populateOvertimeTableView() {
    }
}
