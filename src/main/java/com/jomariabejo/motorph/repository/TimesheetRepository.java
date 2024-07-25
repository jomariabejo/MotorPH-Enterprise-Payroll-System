package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Timesheet;

public class TimesheetRepository extends _AbstractHibernateRepository<Timesheet, Integer> {
    public TimesheetRepository() {
        super(Timesheet.class);
    }
}
