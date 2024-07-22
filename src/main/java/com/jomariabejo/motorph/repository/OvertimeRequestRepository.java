package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class OvertimeRequestRepository implements GenericRepository<OvertimeRequest, Integer> {

    private final HibernateUtil hibernateUtil;

    public OvertimeRequestRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public OvertimeRequest findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(OvertimeRequest.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<OvertimeRequest> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM OvertimeRequest", OvertimeRequest.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(overtimeRequest);
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
    public void update(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(overtimeRequest);
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
    public void delete(OvertimeRequest overtimeRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(overtimeRequest);
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
