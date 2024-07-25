package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.UserLog;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserLogRepository implements GenericRepository<UserLog, Integer> {

    private final HibernateUtil hibernateUtil;

    public UserLogRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public UserLog findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(UserLog.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<UserLog> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM UserLog", UserLog.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(UserLog userlog) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(userlog);
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
    public void update(UserLog userlog) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(userlog);
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
    public void delete(UserLog userlog) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(userlog);
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
