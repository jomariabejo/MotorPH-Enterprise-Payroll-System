package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import com.jomariabejo.motorph.utility.AlertUtility;
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
    private boolean confirmedTimeout = false;
    java.util.Date utilCurrentDate = DateConverter.today();
    Date currentDate = new Date(utilCurrentDate.getTime());
    LocalTime currentTime = LocalTime.now().withSecond(0).withNano(0);
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:00");
    String formattedTime = currentTime.format(timeFormatter);

    @FXML
    private TableColumn<Date,Timesheet> date;

    @FXML
    private Label lbl_tv_total_result;


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

    @FXML
    void btnSetTimeIn(ActionEvent event) {
        try {
            System.out.println("Checking time in for employee id: " + this.employeeId);
            // Check if there's already a timesheet for the current day
            if (timesheetService.checkIfEmployeeIdExistToday(employeeId)) {
                AlertUtility.showErrorAlert("Time In Error", "Time In Already Recorded", "You have already timed in for today.");
            } else {
                // Create a new Timesheet object with the current date, time in, and employee ID
                Timesheet timesheet = new Timesheet();
                timesheet.setDate(currentDate);
                timesheet.setTimeIn(Time.valueOf(formattedTime));
                timesheet.setEmployeeId(employeeId);

                System.out.println("Timesheet Date: " + timesheet.getDate());
                System.out.println("Timesheet Time In: " + timesheet.getTimeIn());
                System.out.println("Timesheet Employee ID: " + timesheet.getEmployeeId());

                // Call the setTimeIn method from the TimesheetService to insert the new timesheet record
                if (timesheetService.setTimeIn(timesheet)) {

                    // Fetch the updated list of timesheets and update the TableView
                    List<Timesheet> updatedTimesheets = timesheetService.getMyTimesheetsAscending(employeeId);
                    tv_timesheets.setItems(FXCollections.observableList(updatedTimesheets));

                    // Show success message to the user
                    AlertUtility.showInformation("Time In Success", "Time In Recorded", "You have successfully timed in at " + formattedTime + " on " + currentDate.toString() + ".");
                    // Increment The no. result label
                    int currentResultNumberValue = Integer.parseInt(this.lbl_tv_total_result.getText());
                    int incrementResultNumberValue = currentResultNumberValue++;
                    this.lbl_tv_total_result.setText(String.valueOf(incrementResultNumberValue));
                } else {
                    AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to insert time in record.");
                }
            }
        } catch (SQLException e) {
            AlertUtility.showErrorAlert("Database Error", "Error Occurred", "Failed to connect to the database.");
        }
    }

    @FXML
    void btnSetTimeOut(ActionEvent event) {
        try {
            // Check if there's already a timesheet for the current day and employee
            if (!timesheetService.checkIfEmployeeIdExistToday(employeeId)) {
                AlertUtility.showErrorAlert("Time Out Error", "No Time In Recorded", "You haven't timed in for today.");
                return; // Exit the method if there's no time in record
            }
            // Confirm with the user before proceeding with the timeout action, only if not already confirmed
            if (!confirmedTimeout) {
                boolean confirmed = AlertUtility.showConfirmation("Confirm Time Out", "Confirm Timeout Action", "Are you sure you want to time out?");
                if (!confirmed) {
                    return; // Exit the method if the user cancels the confirmation
                }
                confirmedTimeout = true; // Set confirmedTimeout to true after confirming once
            }
            // Create a new Timesheet object for updating the time out record
            Timesheet timesheet = new Timesheet();
            timesheet.setEmployeeId(employeeId);
            timesheet.setDate(currentDate);
            timesheet.setTimeOut(Time.valueOf(formattedTime));

            // Call the setTimeOut method from the TimesheetService to update the time out record
            if (timesheetService.setTimeOut(employeeId, currentDate, Time.valueOf(formattedTime))) {

                // Fetch the updated list of timesheets and update the TableView
                List<Timesheet> updatedTimesheets = timesheetService.getMyTimesheetsAscending(employeeId);
                tv_timesheets.setItems(FXCollections.observableList(updatedTimesheets));

                // Show success message to the user
                AlertUtility.showInformation("Time Out Success", "Time Out Recorded", "You have successfully timed out at " + formattedTime + " on " + currentDate.toString() + ".");
            } else {
                AlertUtility.showErrorAlert("Timeout Error", "Oops!",
                        "You've already Timed Out");
                ;
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
        tv_timesheets.setItems(FXCollections.observableList(timesheetService.getMyTimesheets(employeeId)));
        displayTableViewSize();
        this.employeeId = employeeId;
    }

    private void displayTableViewSize() {
        lbl_tv_total_result.setText(String.valueOf(tv_timesheets.getItems().size()));
    }
}
