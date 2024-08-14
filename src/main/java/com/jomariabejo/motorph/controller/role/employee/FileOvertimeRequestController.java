package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        if (isDateAlreadyRequested().isEmpty())
            if (isOvertimeRequestValid())
                if (isHoursWorkedFormatValid())
                    saveOvertimeRequest();
                else
                    displayHoursWorkedFormatInvalid();
            else
                displayInvalidOvertimeRequest();
        else
            displayDateAlreadyRequested();

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
        addOvertimeRequestToTableView(overtimeRequest);
        hideWindow();
    }

    private void hideWindow() {
        tfHoursRequested.getScene().getWindow().hide();
    }

    private void addOvertimeRequestToTableView(OvertimeRequest overtimeRequest) {
        this.overtimeController.getTvOvertime().getItems().add(overtimeRequest);
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

    public FileOvertimeRequestController() {
    }
}
