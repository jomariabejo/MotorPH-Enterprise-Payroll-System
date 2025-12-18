package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public class TimesheetRepository extends _AbstractHibernateRepository<Timesheet, Integer> {
    public TimesheetRepository() {
        super(Timesheet.class);
    }

    public Optional<List<Integer>> fetchYearsOfTimesheetByEmployee(Employee employee) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createNamedQuery("getYearsOfTimesheet", Integer.class);
            query.setParameter("employee", employee);
            List<Integer> resultList = query.getResultList();
            return Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<Timesheet> fetchTimesheet(Employee employee, LocalDate date) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Timesheet> query = session.createNamedQuery("fetchTimesheetByEmployeeAndDate", Timesheet.class);
            query.setParameter("employee", employee);
            query.setParameter("date", date);
            List<Timesheet> results = query.getResultList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            // Return the first result if multiple exist (shouldn't happen in production, but handles test scenarios)
            return Optional.of(results.get(0));
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<List<Timesheet>> fetchEmployeeTimesheetsByYearAndMonth(Employee employee, Year year, Month month) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            int yearValue = year.getValue();
            int monthValue = month.getValue();

            Query<Timesheet> query = session.createNamedQuery("fetchEmployeeTimesheetsByMonthAndYear", Timesheet.class);
            query.setParameter("EMPLOYEE", employee);
            query.setParameter("YEAR", yearValue);
            query.setParameter("MONTH", monthValue);

            List<Timesheet> result = query.getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Integer> fetchAllYearsFromTimesheets() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createQuery(
                    "SELECT DISTINCT FUNCTION('YEAR', ts.date) FROM Timesheet ts ORDER BY FUNCTION('YEAR', ts.date) DESC",
                    Integer.class
            );
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Timesheet> fetchTimesheetsWithFilters(Employee employee, Year year, Month month, LocalDate specificDate, String status) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            // Use LEFT JOIN FETCH to eagerly load Employee, and filter out timesheets with null employees
            StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT ts FROM Timesheet ts LEFT JOIN FETCH ts.employeeID WHERE ts.employeeID IS NOT NULL");
            
            if (employee != null) {
                queryBuilder.append(" AND ts.employeeID = :employee");
            }
            if (year != null) {
                queryBuilder.append(" AND FUNCTION('YEAR', ts.date) = :year");
            }
            if (month != null) {
                queryBuilder.append(" AND FUNCTION('MONTH', ts.date) = :month");
            }
            if (specificDate != null) {
                queryBuilder.append(" AND ts.date = :specificDate");
            }
            if (status != null && !status.isEmpty() && !status.equals("All")) {
                queryBuilder.append(" AND ts.status = :status");
            }
            
            queryBuilder.append(" ORDER BY ts.date DESC, ts.employeeID.employeeNumber");
            
            Query<Timesheet> query = session.createQuery(queryBuilder.toString(), Timesheet.class);
            
            if (employee != null) {
                query.setParameter("employee", employee);
            }
            if (year != null) {
                query.setParameter("year", year.getValue());
            }
            if (month != null) {
                query.setParameter("month", month.getValue());
            }
            if (specificDate != null) {
                query.setParameter("specificDate", specificDate);
            }
            if (status != null && !status.isEmpty() && !status.equals("All")) {
                query.setParameter("status", status);
            }
            
            List<Timesheet> results = query.getResultList();
            // Initialize employee data to avoid lazy loading issues
            results.forEach(ts -> {
                if (ts.getEmployeeID() != null) {
                    ts.getEmployeeID().getFirstName();
                    ts.getEmployeeID().getLastName();
                    ts.getEmployeeID().getEmployeeNumber();
                }
            });
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
