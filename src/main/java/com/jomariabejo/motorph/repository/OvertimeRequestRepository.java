package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.exception.*;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Timesheet;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class OvertimeRequestRepository implements GenericRepository<OvertimeRequest, Integer> {

    private final HibernateUtil hibernateUtil;

    public OvertimeRequestRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public OvertimeRequest findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            OvertimeRequest overtimeRequest = session.get(OvertimeRequest.class, id);
            if (overtimeRequest == null) {
                throw new OvertimeRequestNotFoundException("Overtime request with ID " + id + " not found.");
            }
            return overtimeRequest;
        } finally {
            session.close();
        }
    }

    @Override
    public List<OvertimeRequest> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM OvertimeRequest", OvertimeRequest.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(overtimeRequest);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void update(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(overtimeRequest);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(overtimeRequest);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    // Additional methods for handling timesheet-related exceptions
    public void processOvertimeRequest(OvertimeRequest overtimeRequest) {
        // Check if timesheet exists and is approved
        Timesheet timesheet = findTimesheetByDateAndEmployee(overtimeRequest.getDate(), overtimeRequest.getEmployeeID());
        if (timesheet == null) {
            throw new TimesheetNotFoundException("Timesheet not found for employee on date: " + overtimeRequest.getDate());
        }

        Timesheet.Status status = Timesheet.Status.valueOf(timesheet.getStatus());

        if (status != Timesheet.Status.NOT_SUBMITTED) {
            throw new TimesheetNotSubmittedException("Timesheet not submitted for employee on date: " + overtimeRequest.getDate());
        }
        if (status != Timesheet.Status.DISAPPROVED) {
            throw new TimesheetNotApprovedException("Timesheet not approved for employee on date: " + overtimeRequest.getDate());
        }

        // Process overtime request
        // ...
    }

    private Timesheet findTimesheetByDateAndEmployee(LocalDate date, Employee employee) {
        // Implement logic to find timesheet by date and employee
        // This is just a placeholder method for demonstration
        return null; // Replace with actual implementation
    }
}
