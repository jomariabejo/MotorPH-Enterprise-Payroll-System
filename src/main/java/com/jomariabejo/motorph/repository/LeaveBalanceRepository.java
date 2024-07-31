package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.LeaveBalance;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

public class LeaveBalanceRepository extends _AbstractHibernateRepository<LeaveBalance, Integer> {
    public LeaveBalanceRepository() {
        super(LeaveBalance.class);
    }

    public Optional<LeaveBalance> findLeaveBalanceByEmployeeIdAndLeaveTypeName(String employeeId, String leaveTypeName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<LeaveBalance> query = session.createNamedQuery("findEmployeeLeaveBalanceByLeaveTypeName", LeaveBalance.class);
            query.setParameter("employeeId", employeeId);
            query.setParameter("leaveTypeName", leaveTypeName);

            LeaveBalance leaveBalance = query.getSingleResult();
            return Optional.of(leaveBalance);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace(); // Logging can be enhanced
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
