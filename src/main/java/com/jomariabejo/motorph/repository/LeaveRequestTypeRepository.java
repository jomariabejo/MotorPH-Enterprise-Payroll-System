package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.LeaveBalance;
import com.jomariabejo.motorph.model.LeaveRequestType;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LeaveRequestTypeRepository extends _AbstractHibernateRepository<LeaveRequestType, Integer> {
    public LeaveRequestTypeRepository() {
        super(LeaveRequestType.class);
    }

    public Optional<Integer> fetchMaxCredits(String leaveTypeName) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<LeaveRequestType> query = session.createNamedQuery("findMaxCreditsByLeaveTypeName", LeaveRequestType.class);
            query.setParameter("leaveTypeName", leaveTypeName);
            return Optional.of(query.getSingleResult().getMaxCredits());
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
