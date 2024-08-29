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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
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

    private CustomAlert customAlert;

    @FXML
    void submitButtonClicked() {
        if (hasRemainingLeaveBalance()) {
            if (validateFields()) {
                if (!hasOverlappingLeaveDates()) {
                    displayLeaveRequestSavedSuccessfully();
                    saveLeaveRequest(getLeaveRequest());
                } else {
                    displayLeaveRequestDateOverlap();
                }
            } else {
                displayFieldBlank();
            }
        } else {
            displaySorryYouDontHaveRemainingLeaveBalanceLeft();
        }
    }

    private void displayLeaveRequestSavedSuccessfully() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Saved successfully.", "Your leave request has been submitted."
        );
        customAlert.showAndWait();
    }

    private void saveLeaveRequest(LeaveRequest leaveRequest) {
        this
                .getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getLeaveRequestService()
                .saveLeaveRequest(leaveRequest);
    }

    private void displayLeaveRequestDateOverlap() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Overlap Leave Request Date", "Leave request submission failed, you have overlap dates, please try again."
        );
        customAlert.showAndWait();
    }

    private void displayFieldBlank() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Field blank", "Please fill all fields"
        );
        customAlert.showAndWait();
    }

    private LeaveRequest getLeaveRequest() {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveTypeID(cbLeaveTypes.getSelectionModel().getSelectedItem());
        leaveRequest.setStartDate(dpLeaveFrom.getValue());
        leaveRequest.setEndDate(dpLeaveTo.getValue());
        leaveRequest.setStatus("Pending");
        leaveRequest.setEmployeeID(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee());
        leaveRequest.setDescription(taReason.getText());
        return leaveRequest;
    }

    private boolean hasOverlappingLeaveDates() {
        return this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveRequestService().
                isEmployeeHasOverlapLeaveDates(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee().getId(), dpLeaveFrom.getValue(), dpLeaveTo.getValue());
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
        boolean isValidatedFields = (dpLeaveFrom.getValue() != null || dpLeaveTo.getValue() != null
                || !taReason.getText().isEmpty());

        if (isValidatedFields) {
            return true;
        } else {
            displayFieldBlank();
            return false;
        }
    }

    public FileLeaveRequestController() {
    }

    public void configDatePicker() {
        configureDatePicker(dpLeaveFrom);
        configureDatePicker(dpLeaveTo);
    }

    private void configureDatePicker(DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date == null || isPastDate(date) || isWeekend(date));
            }

            private boolean isPastDate(LocalDate date) {
                return date.isBefore(LocalDate.now());
            }

            private boolean isWeekend(LocalDate date) {
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
            }
        });
        datePicker.setValue(LocalDate.now());
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
        dpLeaveTo.setValue(
                dpLeaveFrom.getValue().plusDays(1)
        );
        disablePreviousDaysOfLeaveEndDatePicker();
        lblLeaveDuration.setText(String.valueOf(calculateDifference()));
    }

    private int calculateDifference() {
        LocalDate startDate = dpLeaveFrom.getValue();
        LocalDate endDate = dpLeaveTo.getValue();

        if (startDate == null || endDate == null) {
            return 0;
        }

        if (radioSingleLeave.isSelected()) {
            return 1;
        } else {
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            return (int) (daysBetween + 1);
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
        Employee employee = getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        String leaveBalanceLeft = getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getLeaveBalanceService()
                .fetchRemainingLeaveBalanceByLeaveTypeName(
                        employee,
                        leaveTypeName
                ).get().toString();
        lblLeaveDaysLeft.setText(leaveBalanceLeft);
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

    public void setupLeaveTypesComboBox() {
        List<LeaveRequestType> leaveTypesList = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getLeaveRequestTypeService()
                .getAllLeaveRequestTypes();
        ObservableList<LeaveRequestType> observableLeaveTypesList = FXCollections.observableArrayList(leaveTypesList);
        cbLeaveTypes.setItems(observableLeaveTypesList);
        cbLeaveTypes.getSelectionModel().selectFirst();
    }


    @FXML
    public void datePickerLeaveFromClicked() {

        if (dpLeaveFrom.getValue() == null) {
            dpLeaveTo.setDisable(true);
        } else {
            dpLeaveTo.setValue(dpLeaveFrom.getValue().plusDays(1));
            dpLeaveTo.setDisable(false);

            disablePreviousDaysOfLeaveEndDatePicker();
            lblLeaveDuration.setText(String.valueOf(calculateDifference()));
        }
    }

    private void disablePreviousDaysOfLeaveEndDatePicker() {
        dpLeaveTo.setDayCellFactory(_ -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    setDisable(date.isBefore(dpLeaveFrom.getValue()));

                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });
    }

    public void leaveToChanged(ActionEvent actionEvent) {
        lblLeaveDuration.setText(String.valueOf(calculateDifference()));
    }
}
