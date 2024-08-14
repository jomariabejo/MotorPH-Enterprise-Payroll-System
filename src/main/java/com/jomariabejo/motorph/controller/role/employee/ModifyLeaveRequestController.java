package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
public class ModifyLeaveRequestController {

    private LeaveHistoryController leaveHistoryController;
    private LeaveRequest leaveRequest;
    private LeaveRequest modifiedLeaveRequest;
    private int tableViewIndex;

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

    // required by javafx
    public ModifyLeaveRequestController() {
    }

    @FXML
    void submitButtonClicked() {
        if (hasRemainingLeaveBalance()) {
            if (validateFields()) {
                modifiedLeaveRequest = leaveRequest;
                modifiedLeaveRequest.setStartDate(dpLeaveFrom.getValue());
                modifiedLeaveRequest.setEndDate(dpLeaveTo.getValue());
                modifiedLeaveRequest.setLeaveTypeID(cbLeaveTypes.getSelectionModel().getSelectedItem());
                modifiedLeaveRequest.setDescription(taReason.getText());
                if (!hasOverlappingLeaveDates() || negateOverlappingIfLeaveDatesIsNotModified()) {
                    saveLeaveRequestModification(leaveRequest);
                    displayLeaveRequestModifiedSuccessfully();

                    updateModifiedLeaveRequest();
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

    private void updateModifiedLeaveRequest() {
        this.getLeaveHistoryController().getTvLeaveRequests().getItems().set(tableViewIndex, modifiedLeaveRequest);
    }

    // Negate the overlapping if the leave dates are still the same
    private boolean negateOverlappingIfLeaveDatesIsNotModified() {
        return (modifiedLeaveRequest.getStartDate().isEqual(leaveRequest.getStartDate()))
                &&
                (modifiedLeaveRequest.getEndDate().isEqual(leaveRequest.getEndDate()));
    }

    private void displayLeaveRequestModifiedSuccessfully() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.INFORMATION, "Leave request modified successfully", "Your leave request has been modified successfully.");
        customAlert.showAndWait();
        taReason.getScene().getWindow().hide();
    }

    private void saveLeaveRequestModification(LeaveRequest leaveRequest) {
        this.getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveRequestService().updateLeaveRequest(leaveRequest);
    }

    private void displayLeaveRequestDateOverlap() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Overlap Leave Request Date", "Leave request submission failed, you have overlap dates, please try again.");
        customAlert.showAndWait();
    }

    private void displayFieldBlank() {CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR,"Field blank", "Please fill all fields");
        customAlert.showAndWait();
    }

    private boolean hasOverlappingLeaveDates() {
        return this.getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveRequestService().
                isEmployeeHasOverlapLeaveDates(
                        this.getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().getEmployee().getId(), dpLeaveFrom.getValue(), dpLeaveTo.getValue());
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
        this.getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Multi-Day Leave Application");
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
            return 0; // Or handle this case as appropriate for your application
        }

        if (radioSingleLeave.isSelected()) {
            return 1;
        } else {
            return (int) ChronoUnit.DAYS.between(startDate, endDate);
        }
    }


    @FXML
    void singleLeaveClicked() {
        this.getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().rewriteLabel("/ Employee / File Leave Request / Single-Day Leave Application");
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
        Employee employee = getLeaveHistoryController().getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        String leaveBalanceLeft = getLeaveHistoryController()
                .getEmployeeRoleNavigationController().getMainViewController()
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

    public void setupComboBoxLeaveTypes() {
        List<LeaveRequestType> leaveTypesList = this.getLeaveHistoryController()
                .getEmployeeRoleNavigationController().getMainViewController()
                .getServiceFactory()
                .getLeaveRequestTypeService()
                .getAllLeaveRequestTypes();
        ObservableList<LeaveRequestType> observableLeaveTypesList = FXCollections.observableArrayList(leaveTypesList);
        cbLeaveTypes.setItems(observableLeaveTypesList);
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

    public void setupComponents() {
        setupComboBoxLeaveTypes();
        setupRadio();
        setupLeaveDuration();
        configDatePicker();
        mapLeaveRequest();
    }

    private void setupLeaveDuration() {
        lblLeaveDuration.setText(String.valueOf(calculateDifference()));
    }

    public void setupRadio() {
        if (leaveRequest.getEndDate().isEqual(leaveRequest.getEndDate())) {
            radioSingleLeave.setSelected(true);
            radioMultiDayLeave.setSelected(false);
            lblStartLeaveDate.setText("Leave Date");
            endOfLeave.setVisible(false);
            endOfLeave.setManaged(false);
        }
        else {
            radioSingleLeave.setSelected(false);
            radioMultiDayLeave.setSelected(true);
            lblStartLeaveDate.setText("Start Leave Date");
            endOfLeave.setVisible(true);
            endOfLeave.setManaged(true);
        }
    }

    public void mapLeaveRequest() {
        taReason.setText(leaveRequest.getDescription());
        dpLeaveFrom.setValue(leaveRequest.getStartDate());
        dpLeaveTo.setValue(leaveRequest.getEndDate());

        String targetLeaveTypeName = leaveRequest.getLeaveTypeID().getLeaveTypeName();
        ObservableList<LeaveRequestType> leaveTypes = cbLeaveTypes.getItems();

        for (LeaveRequestType leaveType : leaveTypes) {
            if (leaveType.getLeaveTypeName().equals(targetLeaveTypeName)) {
                cbLeaveTypes.getSelectionModel().select(leaveType);
                break;
            }
        }
    }
}
