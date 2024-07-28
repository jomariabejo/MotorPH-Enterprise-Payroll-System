package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.LeaveRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaveHistoryController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Pagination paginationLeaveRequests;

    @FXML
    private TableView<LeaveRequest> tvLeaveRequests;

    public LeaveHistoryController() {

    }
    @FXML
    public void comboboxMonthClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void comboBoxYearClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void comboBoxLeaveTypeClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void comboBoxStatusClicked(ActionEvent actionEvent) {
    }
}
