package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class MyTimesheetController {
    private final TimesheetService timesheetService;

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
        String query = "INSERT INTO timesheet (date,time_in,employee_id) VALUES(?,?,?)";
        System.out.println("Should insert new time in of employee into database");
    }
    // TODO: Implement 'set time out' feature here.
    //       Expected Output
    //          [1] Display confirmation for set time out
    //          [2] Update the Timesheet(Lagyan na natin ng time_out and timesheet na walang timeout.
    @FXML
    void btnSetTimeOut(ActionEvent event) {
        System.out.println("Should insert ");
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
    }

    private void displayTableViewSize() {
        lbl_tv_total_result.setText(String.valueOf(tv_timesheets.getItems().size()));
    }
}
