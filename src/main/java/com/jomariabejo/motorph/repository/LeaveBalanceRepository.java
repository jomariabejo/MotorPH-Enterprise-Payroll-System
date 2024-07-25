package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.LeaveBalance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LeaveBalanceRepository implements GenericRepository<LeaveBalance, Integer> {

    private final HibernateUtil hibernateUtil;

    public LeaveBalanceRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public LeaveBalance findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(LeaveBalance.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<LeaveBalance> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM LeaveBalance", LeaveBalance.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(LeaveBalance leaveBalance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(leaveBalance);
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
    public void update(LeaveBalance leaveBalance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(leaveBalance);
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
    public void delete(LeaveBalance leaveBalance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(leaveBalance);
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
