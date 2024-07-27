package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.LeaveRequest;
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
}
