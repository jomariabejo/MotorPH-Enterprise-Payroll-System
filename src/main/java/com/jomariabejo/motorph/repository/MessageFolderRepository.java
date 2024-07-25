package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.MessageFolder;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MessageFolderRepository implements GenericRepository<MessageFolder, Integer> {

    private final HibernateUtil hibernateUtil;

    public MessageFolderRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public MessageFolder findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(MessageFolder.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<MessageFolder> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM MessageFolder", MessageFolder.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(MessageFolder folder) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(folder);
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
    public void update(MessageFolder folder) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(folder);
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
    public void delete(MessageFolder folder) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(folder);
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
