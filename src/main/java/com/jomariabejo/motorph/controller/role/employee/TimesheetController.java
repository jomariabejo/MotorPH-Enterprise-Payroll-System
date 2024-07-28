package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimesheetController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Button clockInBtn;

    @FXML
    private Button clockOutBtn;

    @FXML
    private TableView<?> tvLeaveRequests;

    @FXML
    private void initialize() {
        clockInBtn.getStyleClass().add(Styles.SUCCESS);
        clockOutBtn.getStyleClass().add(Styles.DANGER);
    }

    @FXML
    void clockInClicked(ActionEvent event) {

    }

    @FXML
    void clockOutClicked(ActionEvent event) {

    }

    @FXML
    void paginationOnDragDetected(MouseEvent event) {

    }

    public TimesheetController() {

    }
    @FXML
    public void cbMonthClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void cbYearClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void cbClockInClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void cbClockOutClicked(ActionEvent actionEvent) {
    }
}
