package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
public class FileOvertimeRequestController {

    private OvertimeController overtimeController;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker dpDateOfRequestedOvertime;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField tfHoursRequested;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        cancelBtn.getScene().getWindow().hide();
    }

    @FXML
    void dateOfRequestedOvertimeClicked(ActionEvent event) {

    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (isDateAlreadyRequested().isEmpty()) {
            if (hasTimesheetForDate()) {
                if (isOvertimeRequestValid()) {
                    if (isHoursWorkedFormatValid()) {
                        saveOvertimeRequest();
                    } else {
                        displayHoursWorkedFormatInvalid();
                    }
                } else {
                    displayInvalidOvertimeRequest();
                }
            } else {
                displayNoTimesheetForDate();
            }
        } else {
            displayDateAlreadyRequested();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        CustomAlert customAlert = new CustomAlert(alertType, title, message);
        customAlert.showAndWait();
    }

    private void displayDateAlreadyRequested() {
        showAlert(Alert.AlertType.ERROR, "Request failed.", "You have already requested for this date.");
    }

    private void displayInvalidOvertimeRequest() {
        showAlert(Alert.AlertType.ERROR, "Invalid overtime request", "Your overtime request is invalid.");
    }

    private void displayHoursWorkedFormatInvalid() {
        showAlert(Alert.AlertType.ERROR, "Invalid hours worked format", "Please check if the hours worked format is correct.");
    }

    private void displayNoTimesheetForDate() {
        showAlert(Alert.AlertType.ERROR, 
                "No Timesheet Found", 
                "You must have a timesheet entry for " + dpDateOfRequestedOvertime.getValue() + 
                " before filing an overtime request. Please submit your timesheet first.");
    }


    private boolean isOvertimeRequestValid() {
        return dpDateOfRequestedOvertime.getValue() != null && !tfHoursRequested.getText().isEmpty();
    }

    private Optional<OvertimeRequest> isDateAlreadyRequested() {
        return this.getOvertimeController()
                .getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getOvertimeRequestService()
                .hasOvertimeRequestForDate
                        (
                                this.getOvertimeController()
                                        .getEmployeeRoleNavigationController()
                                        .getMainViewController()
                                        .getEmployee(),
                                Date.valueOf(dpDateOfRequestedOvertime.getValue())
                        );
    }

    private boolean isHoursWorkedFormatValid() {
        String hoursText = tfHoursRequested.getText().trim();
        String hoursPattern = "^(\\d{1,2})(\\.\\d{2})?$";
        return hoursText.matches(hoursPattern);
    }

    /**
     * Checks if the employee has a timesheet entry for the overtime date.
     * Overtime requests can only be filed if a timesheet exists for that date.
     * 
     * @return true if timesheet exists for the overtime date, false otherwise
     */
    private boolean hasTimesheetForDate() {
        if (dpDateOfRequestedOvertime.getValue() == null) {
            return false;
        }
        
        Optional<com.jomariabejo.motorph.model.Timesheet> timesheet = this.getOvertimeController()
                .getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getTimesheetService()
                .getTimesheetByEmployeeAndDate(
                        this.getOvertimeController()
                                .getEmployeeRoleNavigationController()
                                .getMainViewController()
                                .getEmployee(),
                        dpDateOfRequestedOvertime.getValue()
                );
        
        return timesheet.isPresent();
    }


    private void saveOvertimeRequest() {
        OvertimeRequest overtimeRequest = new OvertimeRequest();
        overtimeRequest.setEmployeeID(this.getOvertimeController().getEmployeeRoleNavigationController().getMainViewController().getEmployee());
        overtimeRequest.setDateRequested(Date.valueOf(LocalDate.now()));
        overtimeRequest.setOvertimeDate(Date.valueOf(dpDateOfRequestedOvertime.getValue()));
        overtimeRequest.setHoursRequested(new BigDecimal(tfHoursRequested.getText()).setScale(2, RoundingMode.HALF_UP));
        overtimeRequest.setStatus("Requested");
        this.getOvertimeController()
                .getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getOvertimeRequestService()
                .saveOvertimeRequest(overtimeRequest);
        
        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Overtime request has been submitted successfully.");
        
        // Refresh the table view
        this.overtimeController.populateOvertimeTableView();
        
        hideWindow();
    }

    private void hideWindow() {
        Stage stage = (Stage) tfHoursRequested.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        cancelBtn.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
        cancelBtn.setGraphic(new FontIcon(Material.CANCEL));
        submitBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        submitBtn.setGraphic(new FontIcon(Material.ADD));
    }

    public void disableFutureDates() {
        dpDateOfRequestedOvertime.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(empty || date.isAfter(LocalDate.now()));
                    }
                };
            }
        });
    }

    public FileOvertimeRequestController() {
    }
}
