package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Notification;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class NotificationRepository implements GenericRepository<Notification, Integer> {

    private final HibernateUtil hibernateUtil;

    public NotificationRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Notification findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Notification.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Notification> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Notification", Notification.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Notification notification) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(notification);
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
    public void update(Notification notification) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(notification);
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
    public void delete(Notification notification) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(notification);
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
}
