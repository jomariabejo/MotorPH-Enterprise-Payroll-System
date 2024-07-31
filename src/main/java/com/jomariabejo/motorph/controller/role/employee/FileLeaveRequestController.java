package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.LeaveBalance;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.repository.LeaveBalanceRepository;
import com.jomariabejo.motorph.repository.LeaveRequestTypeRepository;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.LeaveBalanceService;
import com.jomariabejo.motorph.service.LeaveRequestTypeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FileLeaveRequestController {

    private LeaveBalanceService leaveBalanceService = new LeaveBalanceService(new LeaveBalanceRepository());
    private LeaveRequestTypeService leaveRequestTypeService =  new LeaveRequestTypeService(new LeaveRequestTypeRepository());

    @FXML
    private ComboBox<String> cbLeaveTypes;

    @FXML
    private DatePicker dpEndLeaveDate;

    @FXML
    private DatePicker dpStartLeaveDate;

    @FXML
    private HBox endOfLeave;

    @FXML
    private Label lblLeaveApplicationName;

    @FXML
    private Label lblStartLeaveDate;

    @FXML
    private RadioButton radioMultiDayLeave;

    @FXML
    private RadioButton radioSingleLeave;

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

    public void configDatePicker() {
        // Disable natin yung mga nakaraang days :>
        dpStartLeaveDate.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    // Disable previous days
                    setDisable(date.isBefore(LocalDate.now()));

                    // Disable weekends (Saturday and Sunday)
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });

        // Optional: Set initial date to today or later
        dpStartLeaveDate.setValue(LocalDate.now());


        dpEndLeaveDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    // Disable previous days
                    setDisable(date.isBefore(LocalDate.now()));

                    // Disable weekends (Saturday and Sunday)
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });

        // Optional: Set initial date to today or later
        dpEndLeaveDate.setValue(LocalDate.now());
    }


    @FXML
    void multiDayLeaveClicked() {
        this.getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Multi-Day Leave Application");
        lblLeaveApplicationName.setText("Multi-Day Leave Application");
        lblStartLeaveDate.setText("Start Leave Date");
        radioMultiDayLeave.setSelected(true);
        radioSingleLeave.setSelected(false);
        makeVisibleEndOfLeaveDate();
    }

    @FXML
    void singleLeaveClicked() {
        this.getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Single-Day Leave Application");
        lblLeaveApplicationName.setText("Single-Day Leave Application");
        lblStartLeaveDate.setText("Leave Date");
        radioSingleLeave.setSelected(true);
        radioMultiDayLeave.setSelected(false);
        makeInvisibleEndOfLeaveDate();
    }

    @FXML
    void vbLeaveTypeSelected() {

    }

    private void makeInvisibleEndOfLeaveDate() {
        endOfLeave.setVisible(false);
        endOfLeave.setManaged(false);
    }

    private void makeVisibleEndOfLeaveDate() {
        endOfLeave.setVisible(true);
        endOfLeave.setManaged(true);
    }

    public void setSingleLeave() {
        singleLeaveClicked();
    }

    private void setupComboBox() {
        List<String> leaveTypesList = leaveRequestTypeService.fetchAllLeaveTypesName();
        ObservableList<String> observableLeaveTypesList = FXCollections.observableArrayList(leaveTypesList);
        cbLeaveTypes.setItems(observableLeaveTypesList);
    }

    @FXML
    private void initialize() {
        setupComboBox();
    }
}
