package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.UserLog;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class UserLogRepository extends _AbstractHibernateRepository<UserLog, Integer> {
    public UserLogRepository() {
        super(UserLog.class);
    }

    /**
     * Find recent logs with a limit (for real-time display).
     * Eagerly loads User relationship to avoid LazyInitializationException.
     * 
     * @param limit Maximum number of logs to return
     * @return List of recent UserLogs ordered by date descending
     */
    public List<UserLog> findRecentLogs(int limit) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<UserLog> query = session.createQuery(
                    "SELECT ul FROM UserLog ul " +
                    "JOIN FETCH ul.userID " +
                    "ORDER BY ul.logDateTime DESC",
                    UserLog.class
            );
            query.setMaxResults(limit);
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

    /**
     * Find logs after a specific date/time (for incremental updates).
     * Eagerly loads User relationship.
     * 
     * @param dateTime The date/time threshold
     * @return List of UserLogs after the specified date/time
     */
    public List<UserLog> findLogsAfter(LocalDateTime dateTime) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<UserLog> query = session.createQuery(
                    "SELECT ul FROM UserLog ul " +
                    "JOIN FETCH ul.userID " +
                    "WHERE ul.logDateTime > :dateTime " +
                    "ORDER BY ul.logDateTime DESC",
                    UserLog.class
            );
            query.setParameter("dateTime", dateTime);
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

    /**
     * Override findAll() to eagerly load User relationship.
     */
    @Override
    public List<UserLog> findAll() {
        Session session = HibernateUtil.openSession();
        try {
            Query<UserLog> query = session.createQuery(
                    "SELECT DISTINCT ul FROM UserLog ul " +
                    "LEFT JOIN FETCH ul.userID " +
                    "ORDER BY ul.logDateTime DESC",
                    UserLog.class
            );
            return query.getResultList();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
