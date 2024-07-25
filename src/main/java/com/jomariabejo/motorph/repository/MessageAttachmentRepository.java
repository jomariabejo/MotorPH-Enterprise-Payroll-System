package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.MessageAttachment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MessageAttachmentRepository implements GenericRepository<MessageAttachment, Integer> {

    private final HibernateUtil hibernateUtil;

    public MessageAttachmentRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public MessageAttachment findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(MessageAttachment.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<MessageAttachment> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM MessageAttachment", MessageAttachment.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(MessageAttachment attachment) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(attachment);
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
    public void update(MessageAttachment attachment) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(attachment);
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
    public void delete(MessageAttachment attachment) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(attachment);
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
