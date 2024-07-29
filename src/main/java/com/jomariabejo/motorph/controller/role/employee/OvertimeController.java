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
    }

    public void cbYearClicked(ActionEvent actionEvent) {
    }

    public void cbClockInClicked(ActionEvent actionEvent) {
    }

    public void cbClockOutClicked(ActionEvent actionEvent) {
    }

    public void paginationOnDragDetected(MouseEvent mouseEvent) {
    }

    public void cbStatusChanged(ActionEvent actionEvent) {
    }

    public void fileOvertimeRequestClicked(ActionEvent actionEvent) {

    }

    public void enhanceOvertimeBtn() {
        overtimeRequestBtn.setGraphic(new FontIcon(Material.FILE_UPLOAD));
        overtimeRequestBtn.getStyleClass().addAll(
                Styles.SUCCESS
        );
    }
}
