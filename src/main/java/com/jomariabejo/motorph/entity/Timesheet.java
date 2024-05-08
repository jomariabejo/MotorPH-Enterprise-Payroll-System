package com.jomariabejo.motorph.entity;

import java.sql.Date;
import java.sql.Timestamp;

public class Timesheet {

    private int timesheetId;
    private Date date;
    private Timestamp timeIn;
    private Timestamp timeOut;
    private int employeeId;

    // Constructors
    public Timesheet() {}

    public Timesheet(int timesheetId, Date date, Timestamp timeIn, Timestamp timeOut, int employeeId) {
        this.timesheetId = timesheetId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.employeeId = employeeId;
    }

    public Timesheet(Date date, Timestamp timeIn, Timestamp timeOut, Integer employeeId) {
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.employeeId = employeeId;
    }

    // Getters and setters

    public int getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(int timesheetId) {
        this.timesheetId = timesheetId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Timestamp getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Timestamp timeIn) {
        this.timeIn = timeIn;
    }

    public Timestamp getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Timestamp timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
