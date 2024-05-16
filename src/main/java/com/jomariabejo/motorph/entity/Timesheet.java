package com.jomariabejo.motorph.entity;

import java.sql.Date;
import java.sql.Time;

public class Timesheet {

    private int timesheetId;
    private Date date;
    private Time timeIn;
    private Time timeOut;
    private int employeeId;

    // Constructors
    public Timesheet() {}

    public Timesheet(int timesheetId, Date date, Time timeIn, Time timeOut, int employeeId) {
        this.timesheetId = timesheetId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.employeeId = employeeId;
    }

    public Timesheet(Date date, Time timeIn, Time timeOut, Integer employeeId) {
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

    public Time getTimeIn() {
        return timeIn;
    }

    public void Time(Time timeIn) {
        this.timeIn = timeIn;
    }

    public Time getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Time timeOut) {
        this.timeOut = timeOut;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
