package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class FileLeaveRequestController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private ComboBox<LeaveRequestType> cbLeaveTypes;

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

    Dialog dialog = new Dialog();

    @FXML
    void submitButtonClicked() {
        if (hasRemainingLeaveBalance()) {
            if (validateFields()) {
                try {
                    CustomAlert customAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "Save leave request", "Saving now..."
                    );
                    LeaveRequest leaveRequest = new LeaveRequest();
                    leaveRequest.setLeaveTypeID(cbLeaveTypes.getSelectionModel().getSelectedItem());
                    leaveRequest.setStartDate(dpLeaveFrom.getValue());
                    leaveRequest.setEndDate(dpLeaveTo.getValue());
                    leaveRequest.setStatus("Pending");
                    leaveRequest.setEmployeeID(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee());
                    leaveRequest.setDateRequested(Timestamp.from(Instant.now()));
                    leaveRequest.setDescription(taReason.getText());

                    customAlert.showAndWait();
                    this
                            .getEmployeeRoleNavigationController()
                            .getMainViewController()
                            .getLeaveRequestService()
                            .saveLeaveRequest(leaveRequest);
                    System.out.println("Saved successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                CustomAlert alert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Field blank",
                        "Please ensure that all fields are filled out.");
                alert.showAndWait();
            }
        } else {
            displaySorryYouDontHaveRemainingLeaveBalanceLeft();
        }
    }

    private void displaySorryYouDontHaveRemainingLeaveBalanceLeft() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Depleted Remaining Leave Balance.",
                "You don't have enough leave days left.");
        customAlert.showAndWait();
    }

    private boolean hasRemainingLeaveBalance() {
        int remainingLeaveDaysLeft = Integer.parseInt(lblLeaveDaysLeft.getText());
        int leaveDuration = Integer.parseInt(lblLeaveDuration.getText());
        return remainingLeaveDaysLeft >= leaveDuration;
    }


    private boolean validateFields() {
        return (dpLeaveFrom.getValue() != null || dpLeaveTo.getValue() != null
                || !taReason.getText().isEmpty());
    }

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
        } else {
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
        dpLeaveTo.setValue(dpLeaveFrom.getValue());
    }

    @FXML
    void cbLeaveTypeSelected() {
        String leaveTypeName = cbLeaveTypes.getSelectionModel().getSelectedItem().getLeaveTypeName();
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
        List<LeaveRequestType> leaveTypesList = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveRequestTypeService()
                .getAllLeaveRequestTypes();
        ObservableList<LeaveRequestType> observableLeaveTypesList = FXCollections.observableArrayList(leaveTypesList);
        cbLeaveTypes.setItems(observableLeaveTypesList);
        // assume that the employee selected the first item
        cbLeaveTypes.getSelectionModel().selectFirst();
    }


    @FXML
    public void datePickerLeaveFromClicked() {

        if (dpLeaveFrom.getValue() == null) {
            dpLeaveTo.setDisable(true);
        } else {
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
