package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.record.GrossIncome;
import com.jomariabejo.motorph.repository.TimesheetRepository;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class TimesheetService {
    private TimesheetRepository timesheetRepository = new TimesheetRepository();

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


    public ArrayList<GrossIncome> fetchGrossIncome(Date startDate, Date endDate) {
        return timesheetRepository.fetchGrossIncome(startDate, endDate);
    }
}
