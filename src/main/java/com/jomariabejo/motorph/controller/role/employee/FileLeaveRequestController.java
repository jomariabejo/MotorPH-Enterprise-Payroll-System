package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class FileLeaveRequestController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private ComboBox<String> cbLeaveTypes;

    @FXML
    private DatePicker dpLeaveTo;

    @FXML
    private DatePicker dpLeaveFrom;

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
    private Label lblLeaveDuration;

    @FXML
    private Label lblLeaveDaysLeft;


    @FXML
    void cancelButtonClicked() {

    }

    @FXML
    void submitButtonClicked() {

    };

    public FileLeaveRequestController() {
    }

    public void configDatePicker() {
        // Disable natin yung mga nakaraang days :>
        dpLeaveFrom.setDayCellFactory(datePicker -> new DateCell() {
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
        dpLeaveFrom.setValue(LocalDate.now());


        dpLeaveTo.setDayCellFactory(picker -> new DateCell() {
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
        dpLeaveTo.setValue(LocalDate.now());
    }


    @FXML
    void multiDayLeaveClicked() {
        this.getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Multi-Day Leave Application");
        lblLeaveApplicationName.setText("Multi-Day Leave Application");
        lblStartLeaveDate.setText("Start Leave Date");
        radioMultiDayLeave.setSelected(true);
        radioSingleLeave.setSelected(false);
        makeVisibleEndOfLeaveDate();
        dpLeaveTo.setDisable(false);
        // add one day to the (leave-to datepicker)
        dpLeaveTo.setValue(
                dpLeaveFrom.getValue().plusDays(1)
        );
        disablePreviousDaysOfLeaveEndDatePicker();

        lblLeaveDuration.setText(String.valueOf(calculateDifference()));


    }

    private int calculateDifference() {

        if (radioSingleLeave.isSelected()) {
            return 1;
        }
        else {
            LocalDate startDate = dpLeaveFrom.getValue();
            LocalDate endDate = dpLeaveTo.getValue();

            return (int) ChronoUnit.DAYS.between(startDate, endDate);
        }
    }

    @FXML
    void singleLeaveClicked() {
        this.getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Single-Day Leave Application");
        lblLeaveApplicationName.setText("Single-Day Leave Application");
        lblStartLeaveDate.setText("Leave Date");
        radioSingleLeave.setSelected(true);
        radioMultiDayLeave.setSelected(false);
        makeInvisibleEndOfLeaveDate();

        cbLeaveTypeSelected();
    }

    @FXML
    void cbLeaveTypeSelected() {
        String leaveTypeName = cbLeaveTypes.getSelectionModel().getSelectedItem();
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        String leaveBalanceLeft = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveBalanceService()
                .fetchRemainingLeaveBalanceByLeaveTypeName(
                        employee,
                        leaveTypeName
                ).get().toString();
        lblLeaveDaysLeft.setText(leaveBalanceLeft);

        System.out.println("The leaveTypeName is " + leaveTypeName);
        System.out.println("The Employee is : " + employee.getId());
        System.out.println("leaveBalanceLeft: " + leaveBalanceLeft);
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
        lblLeaveDuration.setText("1");
    }

    public void setupComboBox() {
        List<String> leaveTypesList = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveRequestTypeService()
                .fetchAllLeaveTypesName();
        ObservableList<String> observableLeaveTypesList = FXCollections.observableArrayList(leaveTypesList);
        cbLeaveTypes.setItems(observableLeaveTypesList);

        // assume that the employee selected the first item
        cbLeaveTypes.getSelectionModel().selectFirst();
    }

    private boolean hasRemainingLeaveBalance() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer employeeId = employee.getId();
        String leaveTypeName = cbLeaveTypes.getSelectionModel().getSelectedItem();

        Optional<Integer> remainingLeaveBalanceOfSelectedLeaveType =  this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveBalanceService()
                .fetchRemainingLeaveBalanceByLeaveTypeName(
                        employee,
                        leaveTypeName
                );
        return remainingLeaveBalanceOfSelectedLeaveType.isPresent()
                ? remainingLeaveBalanceOfSelectedLeaveType.get() > 0
                : false;
    }

    private Integer maxCredits() {
        String leaveTypeName = cbLeaveTypes.getSelectionModel().getSelectedItem();
        return this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveRequestTypeService()
                .getLeaveRequestTypeMaxCreditsByName(
                        leaveTypeName
                ).get();
    }

    private Integer leaveDurationInDays() {

        // This one is for single leave application
        if (radioSingleLeave.isSelected()) {
            return 1;
        }
        // This one is for multi leave application
        else {
            LocalDate leaveFrom = dpLeaveFrom.getValue();
            LocalDate leaveTo = dpLeaveTo.getValue();

            long daysBetween = ChronoUnit.DAYS.between(leaveFrom, leaveTo);

            return (int) daysBetween;
        }
    }


    @FXML
    public void datePickerLeaveFromClicked() {

        if (dpLeaveFrom.getValue() == null) {
            dpLeaveTo.setDisable(true);
        }

        else {
            // add one day sa ating (leave-to) gamit ang ating (leave-from)
            dpLeaveTo.setValue(
                    dpLeaveFrom.getValue().plusDays(1)
            );
            // ma eenable ang ating datepicker for (leave to) kung mayroon na tayong (leave from)
            dpLeaveTo.setDisable(false);

           disablePreviousDaysOfLeaveEndDatePicker();
           lblLeaveDuration.setText(String.valueOf(calculateDifference()));
        }
    }

    private void disablePreviousDaysOfLeaveEndDatePicker() {
        dpLeaveTo.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    // Disable previous days
                    setDisable(date.isBefore(dpLeaveFrom.getValue()));

                    // Disable weekends (Saturday and Sunday)
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });
    }

    public void leaveToChanged(ActionEvent actionEvent) {
        lblLeaveDuration.setText(
                String.valueOf(calculateDifference())
        );
    }
}
