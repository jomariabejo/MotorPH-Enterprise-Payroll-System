package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimesheetModifier {

    public TimesheetController timesheetController;

    @FXML
    private Button cancelBtn;

    @FXML
    private Button modifyBtn;

    @FXML
    private TextField tfApprover;

    @FXML
    private TextField tfDate;

    @FXML
    private TextField tfEmployeeName;

    @FXML
    private TextField tfHoursWorked;

    @FXML
    private TextField tfStatus;

    @FXML
    private TextField tfTimesheetId;

    @FXML
    private TextField tfTimeIn;

    @FXML
    private TextField tfTimeOut;

    @FXML
    private TextField tfRemarks;

    @FXML
    private void cancelBtnClicked(ActionEvent actionEvent) {
        cancelBtn.getScene().getWindow().hide();
    }

    @FXML
    private void modifyBtnClicked(ActionEvent actionEvent) {
        displayModificationConfirmation();
    }

    private void displayModificationConfirmation() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Modification confirmation", "Are you sure you want to save the modification? ");
        ButtonType result = customAlert.showAndWait().get();
        if (result.equals(ButtonType.OK)) {
            executeTimesheetModification();
        }
    }


    @FXML
    private TextField tfEmployeeId;

    private void executeTimesheetModification() {
        Timesheet timesheet = new Timesheet();
        timesheet.setId(Integer.valueOf(tfTimesheetId.getText()));
        timesheet.setDate(LocalDate.parse(tfDate.getText()));
        timesheet.setStatus(tfStatus.getText());
        timesheet.setRemarks(tfRemarks.getText());
        timesheet.setTimeOut(Time.valueOf(tfTimeOut.getText()));
        timesheet.setTimeIn(Time.valueOf(tfTimeIn.getText()));
        timesheet.setStatus(timesheet.getStatus());
        timesheet.setEmployeeID(this.getTimesheetController().getHumanResourceAdministratorNavigationController().getMainViewController().getServiceFactory().getEmployeeService().getEmployeeById(Integer.valueOf(tfEmployeeId.getText())));

        // Convert java.sql.Time to LocalTime
        LocalTime timeIn = timesheet.getTimeIn().toLocalTime();
        LocalTime timeOut = timesheet.getTimeOut().toLocalTime();

        // Calculate the duration between TimeIn and TimeOut
        Duration duration = Duration.between(timeIn, timeOut);

        // Convert the duration to hours
        double hoursWorked = duration.toMillis() / 3600000.0; // Convert milliseconds to hours

        // Round to 4 decimal places and ensure it's positive
        hoursWorked = Math.abs(hoursWorked); // Ensure positive value
        BigDecimal roundedHours = new BigDecimal(hoursWorked).setScale(4, RoundingMode.HALF_UP); // 4 decimal places rounding

        // Set the hours worked in the timesheet
        timesheet.setHoursWorked(roundedHours.floatValue()); // Set the rounded value as a float

        // Update the timesheet using the service
        this.getTimesheetController().getHumanResourceAdministratorNavigationController().getMainViewController()
                .getServiceFactory().getTimesheetService().updateTimesheet(timesheet);

        // Hide tableview
        tfEmployeeId.getScene().getWindow().hide();
        // Update the tableview
        this.getTimesheetController().getTvTimesheets().getSelectionModel().select(timesheet);
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setId(timesheet.getId());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setDate(timesheet.getDate());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setRemarks(timesheet.getRemarks());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setStatus(timesheet.getStatus());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setApprover(timesheet.getApprover());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setHoursWorked(timesheet.getHoursWorked());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setTimeOut(timesheet.getTimeOut());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setTimeIn(timesheet.getTimeIn());
        this.getTimesheetController().getTvTimesheets().getSelectionModel().getSelectedItem().setEmployeeID(timesheet.getEmployeeID());
        this.getTimesheetController().getTvTimesheets().refresh();

    }

    private void enableTextfields() {
        tfDate.setDisable(true);
        tfEmployeeName.setDisable(true);
        tfHoursWorked.setDisable(true);
        tfStatus.setDisable(true);
        tfTimeIn.setDisable(true);
        tfTimeOut.setDisable(true);
        tfRemarks.setDisable(true);
    }

    private void disableTextfields() {
        tfDate.setDisable(false);
        tfEmployeeName.setDisable(false);
        tfHoursWorked.setDisable(false);
        tfStatus.setDisable(false);
        tfTimeIn.setDisable(false);
        tfTimeOut.setDisable(false);
        tfRemarks.setDisable(false);
    }

    public void injectTimesheet(Timesheet timesheet) {
        tfTimesheetId.setText(String.valueOf(timesheet.getId()));
        tfEmployeeName.setText(timesheet.getEmployeeName());
        tfHoursWorked.setText(String.valueOf(timesheet.getHoursWorked()));
        tfStatus.setText(String.valueOf(timesheet.getStatus()));
        tfTimeIn.setText(String.valueOf(timesheet.getTimeIn()));
        tfTimeOut.setText(String.valueOf(timesheet.getTimeOut()));
        tfDate.setText(String.valueOf(timesheet.getDate()));
        tfRemarks.setText(String.valueOf(timesheet.getRemarks()));
        tfEmployeeId.setText(String.valueOf(timesheet.getEmployeeID().getEmployeeNumber()));
        try {
            tfApprover.setText(String.valueOf(timesheet.getApprover().getFirstName() + " " + timesheet.getApprover().getLastName()));
        }
        catch (NullPointerException nullPointerException) {
            tfApprover.setText("No approver");
        }
    }



    private void displayCancelIcon() {
        FontIcon fontIcon = new FontIcon(Feather.X);
        fontIcon.setIconSize(96);
        cancelBtn.setGraphic(fontIcon);
        cancelBtn.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void displaySaveIcon() {
        FontIcon fontIcon = new FontIcon(Feather.SAVE);
        fontIcon.setIconSize(96);
        modifyBtn.setGraphic(fontIcon);
        modifyBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    @FXML
    private void initialize() {
        displayIcons();
    }

    private void displayIcons() {
        displayCancelIcon();
        displaySaveIcon();
    }

    @FXML
    private int selectedIndex;

}
