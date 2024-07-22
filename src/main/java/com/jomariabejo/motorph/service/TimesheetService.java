package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.repository.TimesheetRepository;

import java.util.List;

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
}
