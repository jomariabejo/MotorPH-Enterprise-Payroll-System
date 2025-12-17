package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Notification;
import org.hibernate.Session;

import java.util.List;

public class NotificationRepository extends _AbstractHibernateRepository<Notification, Integer> {
    public NotificationRepository() {
        super(Notification.class);
    }

    public List<Notification> findByEmployee(Employee employee) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Notification WHERE employee = :employee ORDER BY timestamp DESC", Notification.class)
                    .setParameter("employee", employee)
                    .setMaxResults(10)
                    .list();
        } finally {
            session.close();
        }
    }

    public List<Notification> findUnreadByEmployee(Employee employee) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Notification WHERE employee = :employee AND readStatus = false ORDER BY timestamp DESC", Notification.class)
                    .setParameter("employee", employee)
                    .list();
        } finally {
            session.close();
        }
    }

    public long countUnreadByEmployee(Employee employee) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "SELECT COUNT(*) FROM Notification WHERE employee = :employee AND readStatus = false", Long.class)
                    .setParameter("employee", employee)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
