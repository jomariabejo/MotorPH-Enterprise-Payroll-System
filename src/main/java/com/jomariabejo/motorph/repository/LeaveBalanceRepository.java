package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveBalance;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LeaveBalanceRepository extends _AbstractHibernateRepository<LeaveBalance, Integer> {
    public LeaveBalanceRepository() {
        super(LeaveBalance.class);
    }

    public Optional<Integer> getEmployeeRemainingLeaveBalanceByLeaveType(Employee employee, String leaveTypeName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createNamedQuery("LeaveBalance.findBalanceByEmployeeAndLeaveType", Integer.class);

            query.setParameter("employeeId", employee.getEmployeeNumber());
            query.setParameter("leaveTypeName", leaveTypeName);

            Integer leaveBalance = query.getSingleResult();
            return Optional.ofNullable(leaveBalance);
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<List<LeaveBalance>> getEmployeeRemainingLeaveBalance(Employee employee) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<LeaveBalance> query = session.createNamedQuery("LeaveBalance.findEmployeeRemainingLeaveBalance", LeaveBalance.class);

            query.setParameter("employeeId", employee.getEmployeeNumber());

            return Optional.ofNullable(query.getResultList());
        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logging framework
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return Optional.empty();
    }
}
