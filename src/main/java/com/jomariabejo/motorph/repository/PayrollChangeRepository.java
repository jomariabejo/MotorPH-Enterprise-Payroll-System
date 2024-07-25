package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Announcement;
import com.jomariabejo.motorph.model.PayrollChange;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayrollChangeRepository implements GenericRepository<PayrollChange, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayrollChangeRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public PayrollChange findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PayrollChange.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PayrollChange> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PayrollChange", PayrollChange.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(PayrollChange payrollChange) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(payrollChange);
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
    public void update(PayrollChange payrollChange) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(payrollChange);
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
    public void delete(PayrollChange payrollChange) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(payrollChange);
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
