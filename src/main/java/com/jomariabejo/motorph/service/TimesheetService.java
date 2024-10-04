package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.repository.TimesheetRepository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public class TimesheetService {

    private final TimesheetRepository timesheetRepository;

    public TimesheetService(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    public Timesheet getTimesheetById(Integer id) {
        return timesheetRepository.findById(id);
    }

    public List<Timesheet> getAllTimesheets() {
        return timesheetRepository.findAll();
    }

    public void saveTimesheet(Timesheet timesheet) {
        timesheetRepository.save(timesheet);
    }

    public void updateTimesheet(Timesheet timesheet) {
        timesheetRepository.update(timesheet);
    }

    public void deleteTimesheet(Timesheet timesheet) {
        timesheetRepository.delete(timesheet);
    }

    public Optional<List<Integer>> getYearsOfLeaveRequestOfEmployee(Employee employee) {
        return timesheetRepository.fetchYearsOfTimesheetByEmployee(employee);
    }

    public Optional<Timesheet> getTimesheetByEmployeeAndDate(Employee employee, LocalDate date) {
        return timesheetRepository.fetchTimesheet(employee, date);
    }

    public Optional<List<Timesheet>> getTimesheetsByEmployeeAndDate(Employee employee, Year year, Month month) {
        return timesheetRepository.fetchEmployeeTimesheetsByYearAndMonth(employee, year, month);
    }
}
