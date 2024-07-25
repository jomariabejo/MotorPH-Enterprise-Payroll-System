package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Message;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MessageRepository implements GenericRepository<Message, Integer> {

    private final HibernateUtil hibernateUtil;

    public MessageRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Message findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Message.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Message> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Message", Message.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Message message) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(message);
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
    public void update(Message message) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(message);
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
    public void delete(Message message) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(message);
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
