package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
public class ModifyOvertimeRequestController {

    private OvertimeController overtimeController;

    private OvertimeRequest overtimeRequest;

    private OvertimeRequest modifiedOvertimeRequest;

    private int tableViewIndex;

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
        mapModification();
        if (isDateAlreadyRequested().isEmpty() || isOvertimeDateSame()) {
            if (hasTimesheetForDate()) {
                if (isOvertimeRequestValid()) {
                    if (isHoursWorkedFormatValid()) {
                        saveOvertimeRequestModification();
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

    private void mapModification() {
        modifiedOvertimeRequest = overtimeRequest;
        modifiedOvertimeRequest.setHoursRequested(new BigDecimal(tfHoursRequested.getText()).setScale(2, RoundingMode.HALF_UP));
        modifiedOvertimeRequest.setOvertimeDate(Date.valueOf(dpDateOfRequestedOvertime.getValue()));
    }

    private boolean isOvertimeDateSame() {
        return overtimeRequest.getOvertimeDate().equals(modifiedOvertimeRequest.getOvertimeDate());    }

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



    private void saveOvertimeRequestModification() {
        this.getOvertimeController()
                .getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getOvertimeRequestService()
                .updateOvertimeRequest(modifiedOvertimeRequest);
        
        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Success", "Overtime request has been updated successfully.");
        
        // Refresh the table view
        this.getOvertimeController().populateOvertimeTableView();
        
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
        disableFutureDates();
    }

    private void disableFutureDates() {
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

    public ModifyOvertimeRequestController() {
    }

    public void setupComponents() {
        dpDateOfRequestedOvertime.setValue(overtimeRequest.getOvertimeDate().toLocalDate());
        tfHoursRequested.setText(overtimeRequest.getHoursRequested().toString());
    }
}
