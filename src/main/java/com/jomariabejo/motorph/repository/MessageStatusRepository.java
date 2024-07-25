package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.MessageStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MessageStatusRepository implements GenericRepository<MessageStatus, Integer> {

    private final HibernateUtil hibernateUtil;

    public MessageStatusRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public MessageStatus findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(MessageStatus.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<MessageStatus> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM MessageStatus", MessageStatus.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(MessageStatus status) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(status);
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
    public void update(MessageStatus status) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(status);
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
    public void delete(MessageStatus status) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(status);
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
