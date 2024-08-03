package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.LeaveRequest;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
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
            System.out.println("The result is " + query.getResultList());
            return query.getSingleResult();
        } catch (NoResultException e) {
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false; // there is no overlap leave dates
    }
}