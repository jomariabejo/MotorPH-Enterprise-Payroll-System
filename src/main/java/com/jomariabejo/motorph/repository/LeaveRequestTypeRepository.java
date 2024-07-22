package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LeaveRequestTypeRepository implements GenericRepository<LeaveRequestType, Integer> {

    private final HibernateUtil hibernateUtil;

    public LeaveRequestTypeRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public LeaveRequestType findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(LeaveRequestType.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<LeaveRequestType> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM LeaveRequestType", LeaveRequestType.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(leaveRequestType);
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
    public void update(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(leaveRequestType);
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
    public void delete(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(leaveRequestType);
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
