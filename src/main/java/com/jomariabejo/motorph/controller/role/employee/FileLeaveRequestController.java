package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileLeaveRequestController {


    @FXML
    private ComboBox<?> cbLeaveTypes;

    @FXML
    private DatePicker dpEndLeaveDate;

    @FXML
    private DatePicker dpStartLeaveDate;

    @FXML
    private TextArea taReason;

    @FXML
    void cancelButtonClicked() {

    }

    @FXML
    void submitButtonClicked() {

    }


    private EmployeeRoleNavigationController employeeRoleNavigationController;

    public FileLeaveRequestController() {
    }
}
