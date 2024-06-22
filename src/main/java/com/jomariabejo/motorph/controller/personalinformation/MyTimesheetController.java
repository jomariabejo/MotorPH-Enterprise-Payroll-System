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

    @FXML
    void btnSetTimeIn(ActionEvent event) {
        String query = "INSERT INTO timesheet (date,time_in,employee_id) VALUES(?,?,?)";
        System.out.println("Should insert new time in of employee into database");
    }

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
