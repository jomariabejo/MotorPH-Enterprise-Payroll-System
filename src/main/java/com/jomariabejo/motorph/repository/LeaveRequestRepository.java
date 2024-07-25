package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.LeaveRequest;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LeaveRequestRepository implements GenericRepository<LeaveRequest, Integer> {

    private final HibernateUtil hibernateUtil;

    public LeaveRequestRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public LeaveRequest findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(LeaveRequest.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<LeaveRequest> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM LeaveRequest", LeaveRequest.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(LeaveRequest leaveRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(leaveRequest);
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
    public void update(LeaveRequest leaveRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(leaveRequest);
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
    public void delete(LeaveRequest leaveRequest) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(leaveRequest);
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
