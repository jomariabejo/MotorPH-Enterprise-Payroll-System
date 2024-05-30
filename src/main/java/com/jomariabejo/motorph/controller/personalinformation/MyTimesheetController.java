package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.CurrentTimestampUtility;
import com.jomariabejo.motorph.utility.DateConverter;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyTimesheetController {
    private final TimesheetService timesheetService;
    private int employeeId;
    Date currentDate = DateConverter.today();
    Time currentTime = CurrentTimestampUtility.getCurrentTime();
    String formattedTime = CurrentTimestampUtility.getCurrentTimeFormatted();

    @FXML
    private TableColumn<Date,Timesheet> date;

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private Label lbl_tv_total_result1;

    @FXML
    private TableColumn<BigDecimal,Timesheet> overtimeHoursWorked;


    @FXML
    private TableColumn<BigDecimal,Timesheet> tc_regularHoursWorked;

    @FXML
    private TableColumn<Time,Timesheet> tc_timeIn;

    @FXML
    private TableColumn<Time,Timesheet> tc_timeOut;

    @FXML
    private TableColumn<Integer,Timesheet> tc_timesheetId;


    @FXML
    private TableView<Timesheet> tv_timesheets;

    // TODO: Implement 'set time in' feature here.
    //      Guide:
    //          [1] Check mo muna ang database if may timesheet na si employee sa current day
    //          [2] Insert new 'timesheet' including 'date','time_in','time_out' // You can use this method to implement(com.jomariabejo.motorph.service.TimesheetService.setTimeIn)
    //          [3] Display to user kung anong oras siya nakapag time in using this method. (com.jomariabejo.motorph.utility.AlertUtility)
    //      Expected output:
    //          [1] Once the time in is clicked, the payroll_sys.timesheet should insert a new data including the
    @FXML
    void btnSetTimeIn(ActionEvent event) {
        try {
            System.out.println("Checking time in for employee id: " + this.employeeId);

            if (timesheetService.checkIfEmployeeIdTimeinExistToday(employeeId)) {
                AlertUtility.showErrorAlert("Time In Error", "Time In Already Recorded", "You have already timed in for today.");
            } else {
                Timesheet timesheet = new Timesheet();
                timesheet.setDate(currentDate);
                timesheet.setTimeIn(currentTime);
                timesheet.setEmployeeId(employeeId);


                if (timesheetService.setTimeIn(timesheet)) {
                    List<Timesheet> updatedTimesheets = timesheetService.getMyTimesheetsAscending(employeeId);
                    tv_timesheets.setItems(FXCollections.observableList(updatedTimesheets));


                    AlertUtility.showInformation("Time In Success", "Time In Recorded", "You have successfully timed in at " + formattedTime + " on " + currentDate + ".");
                } else {
                    AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to insert time in record.");
                }
            }
        } catch (SQLException e) {
            AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to connect to the database.");
        }
    }


    // TODO: Implement 'set time out' feature here.
    //       Expected Output
    //          [1] Display confirmation for set time out
    //          [2] Update the Timesheet(Lagyan na natin ng time_out and timesheet na walang timeout.
    @FXML
    void btnSetTimeOut(ActionEvent event) {
        try {
            System.out.println("Checking time out for employee id: " + this.employeeId);


            if (!timesheetService.checkIfEmployeeIdTimeinExistToday(employeeId)) {
                AlertUtility.showErrorAlert("Time Out Error", "No Time In Recorded", "You haven't timed in for today.");
                return;
            }

            if (timesheetService.checkIfEmployeeIDAlreadyTimedOutToday(employeeId)) {
                AlertUtility.showErrorAlert("Time Out Error", "Time Out Already Recorded", "You have already timed out for today.");
                return;
            }

            boolean confirmed = AlertUtility.showConfirmation("Confirm Time Out", "Confirm Timeout Action", "Are you sure you want to time out?");
            if (!confirmed) {
                return;
            }

            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(employeeId);
            timesheet.setDate(currentDate);
            timesheet.setTimeOut(currentTime);


            if (timesheetService.setTimeOut(employeeId, currentDate, currentTime)) {
                List<Timesheet> updatedTimesheets = timesheetService.getMyTimesheetsAscending(employeeId);
                tv_timesheets.setItems(FXCollections.observableList(updatedTimesheets));


                AlertUtility.showInformation("Time Out Success", "Time Out Recorded", "You have successfully timed out at " + formattedTime + " on " + currentDate + ".");
            } else {
                AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to update time out record.");
            }
        } catch (SQLException e) {
            AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to connect to the database.");
        }
    }

    @FXML
    void viewEmployeePerformanceButtonClicked(ActionEvent event) {

    }

    public MyTimesheetController() {
        this.timesheetService = new TimesheetService();
    }

    public void initData(int employeeId) {
        tv_timesheets.setItems(FXCollections.observableList(timesheetService.getMyTimesheetsAscending(employeeId)));
        displayTableViewSize();
        this.employeeId = employeeId;
    }

    private void displayTableViewSize() {
        lbl_tv_total_result.setText(String.valueOf(tv_timesheets.getItems().size()));
    }
}
