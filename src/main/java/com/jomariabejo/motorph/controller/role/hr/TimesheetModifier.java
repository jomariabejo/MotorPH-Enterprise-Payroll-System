package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
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

import java.sql.Time;
import java.time.LocalDate;

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
        if (modifyBtn.getText().equals("Save")) {
            displayModificationConfirmation();
            displayModifyIcon();
        }
        else {
            enableTextfields();
            displaySaveIcon();
        }
    }

    private void displayModificationConfirmation() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Modification confirmation", "Are you sure you want to save the modification? ");
        ButtonType result = customAlert.showAndWait().get();
        if (result.equals(ButtonType.OK)) {
            executeTimesheetModification();
        }
    }

    private void executeTimesheetModification() {
        Timesheet timesheet = new Timesheet();
        timesheet.setId(Integer.valueOf(tfTimesheetId.getText()));
        timesheet.setDate(LocalDate.parse(tfDate.getText()));
        timesheet.setStatus(tfStatus.getText());
        timesheet.setRemarks(timesheet.getRemarks());
        timesheet.setTimeOut(Time.valueOf(tfTimeOut.getText()));
        timesheet.setTimeIn(Time.valueOf(tfTimeIn.getText()));

        this.getTimesheetController().getHumanResourceAdministratorNavigationController().getMainViewController()
                .getServiceFactory().getTimesheetService().updateTimesheet(timesheet);
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
        tfApprover.setText(String.valueOf(timesheet.getApprover().getFirstName() + " " + timesheet.getApprover().getLastName()));
    }



    private void displayCancelIcon() {
        FontIcon fontIcon = new FontIcon(Feather.X);
        fontIcon.setIconSize(96);
        cancelBtn.setGraphic(fontIcon);
        cancelBtn.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void displayModifyIcon() {
        FontIcon fontIcon = new FontIcon(Feather.EDIT);
        fontIcon.setIconSize(96);
        modifyBtn.setGraphic(fontIcon);
        modifyBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }


    private void displaySaveIcon() {
        FontIcon fontIcon = new FontIcon(Feather.SAVE);
        fontIcon.setIconSize(96);
        modifyBtn.setGraphic(fontIcon);
        modifyBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    @FXML
    private void initialize() {
        displayCancelIcon();
        displayModifyIcon();
    }
}
