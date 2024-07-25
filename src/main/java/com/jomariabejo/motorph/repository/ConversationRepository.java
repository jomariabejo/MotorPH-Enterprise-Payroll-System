package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Conversation;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ConversationRepository implements GenericRepository<Conversation, Integer> {

    private final HibernateUtil hibernateUtil;

    public ConversationRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Conversation findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Conversation.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Conversation> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Conversation", Conversation.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Conversation conversation) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(conversation);
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
    public void update(Conversation conversation) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(conversation);
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
    public void delete(Conversation conversation) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(conversation);
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
