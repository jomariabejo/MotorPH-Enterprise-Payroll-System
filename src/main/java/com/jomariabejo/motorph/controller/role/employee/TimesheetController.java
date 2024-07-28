package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimesheetController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;



    @FXML
    private TableView<?> tvLeaveRequests;

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
