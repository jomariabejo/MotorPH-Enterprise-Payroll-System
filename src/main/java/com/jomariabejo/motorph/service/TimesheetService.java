package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.record.GrossIncome;
import com.jomariabejo.motorph.repository.TimesheetRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class TimesheetService {
    private final TimesheetRepository timesheetRepository = new TimesheetRepository();

    public int getTimesheetPageCount() {
        return timesheetRepository.getTimesheetPageCount();
    }

    public int getTimesheetPageCount(int employeeId) {
        return timesheetRepository.getTimesheetPageCount(employeeId);
    }

    public ArrayList<Timesheet> getTimesheets() throws SQLException {
        return timesheetRepository.getTimesheets();
    }

    public int countEmployeeTimesheets(int employeeId) throws SQLException {
        return timesheetRepository.countEmployeeTimesheets(employeeId);
    }

    public ArrayList<Timesheet> fetchTimesheetsForPage(int pageIndex, int rowsPerPage) throws SQLException {
        return timesheetRepository.fetchTimesheetsForPage(pageIndex, rowsPerPage);
    }

    public ArrayList<Timesheet> fetchTimesheetsForPage(int employeeId, int pageIndex, int rowsPerPage) throws SQLException {
        return timesheetRepository.fetchTimesheetsForPage(employeeId, pageIndex, rowsPerPage);
    }

    public boolean checkIfEmployeeIdExist(int employeeId) {
        return timesheetRepository.checkIfEmployeeHasTimesheetRecords(employeeId);
    }

    public boolean checkIfEmployeeIdExistToday(int employeeId) {
        return timesheetRepository.checkIfEmployeeHasTimesheetRecordsToday(employeeId);
    }

    public ArrayList<GrossIncome> fetchGrossIncome(Date startDate, Date endDate) {
        return timesheetRepository.fetchGrossIncome(startDate, endDate);
    }

    public ArrayList<Timesheet> getMyTimesheets(int employeeId) {
        return timesheetRepository.fetchMyTimesheetRecords(employeeId);
    }

    public ArrayList<Timesheet> getMyTimesheetsAscending(int employeeId) {
        return timesheetRepository.fetchMyTimesheetAscending(employeeId);
    }

    public boolean setTimeIn(Timesheet timesheet) throws SQLException {
        return timesheetRepository.createTimesheet(timesheet);
    }

    public boolean setTimeOut(int employeeId, Date date, Time timeOut) throws SQLException {
        return timesheetRepository.updateTimesheet(employeeId, date, timeOut);
    }

    public boolean checkIfEmployeeIdTimeinExistToday(int employeeId) {
        return timesheetRepository.checkIfEmployeeIdTimeinExistToday(employeeId);
    }

    public boolean checkIfEmployeeIDAlreadyTimedOutToday(int employeeId) {
        return timesheetRepository.checkIfEmployeeIDAlreadyTimedOutToday(employeeId);
    }
}
