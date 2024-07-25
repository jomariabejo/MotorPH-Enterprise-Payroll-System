package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReimbursementRequestRepository {

    private final HibernateUtil hibernateUtil;

    public ReimbursementRequestRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public ReimbursementRequest findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(ReimbursementRequest.class, id);
        } finally {
            session.close();
        }
    }

    public List<ReimbursementRequest> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM ReimbursementRequest", ReimbursementRequest.class).list();
        } finally {
            session.close();
        }
    }

    public void save(ReimbursementRequest request) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(request);
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

    public void update(ReimbursementRequest request) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(request);
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

    public void delete(ReimbursementRequest request) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(request);
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
