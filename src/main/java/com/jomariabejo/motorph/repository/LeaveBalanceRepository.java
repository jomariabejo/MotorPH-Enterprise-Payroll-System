package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveBalance;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

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

            query.setParameter("employeeId", employee.getId());
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
}
