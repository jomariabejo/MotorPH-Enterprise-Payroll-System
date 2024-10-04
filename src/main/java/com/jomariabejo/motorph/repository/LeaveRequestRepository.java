package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public class LeaveRequestRepository extends _AbstractHibernateRepository<LeaveRequest, Integer> {
    public LeaveRequestRepository() {
        super(LeaveRequest.class);
    }

    public boolean isEmployeeHasOverlapLeaveDates(Integer employeeId, LocalDate leaveFrom, LocalDate leaveTo) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Boolean> query = session.createNamedQuery("isLeaveRequestDatesOverlap", Boolean.class);

            query.setParameter("employeeId", employeeId);
            query.setParameter("startDate", leaveFrom);
            query.setParameter("endDate", leaveTo);
            return query.getSingleResult();
        } catch (NoResultException e) {
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false; // there is no overlap leave dates
    }

    public Optional<List<Integer>> getYearsOfLeaveRequestByEmployeeId(Employee employee) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createNamedQuery("getYearsOfLeaveRequest", Integer.class);
            query.setParameter("employeeID", employee);
            List<Integer> resultList = query.getResultList();
            return Optional.of(resultList).get().size() > 0 ? Optional.of(resultList) : Optional.of(List.of(LocalDate.now().getYear()));
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    public List<LeaveRequest> fetchLeaveRequestsForEmployee(Employee employee, String monthName, int year, String status, String leaveTypeName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<LeaveRequest> query = session.createNamedQuery("fetchLeaveRequestForEmployee", LeaveRequest.class);
            query.setParameter("employee", employee);
            query.setParameter("month", monthName);
            query.setParameter("year", year);
            query.setParameter("status", status);
            query.setParameter("leaveTypeName", leaveTypeName);
            return query.getResultList();
        } catch (NoResultException e) {
            return List.of(); // Return an empty list if no results are found
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


}