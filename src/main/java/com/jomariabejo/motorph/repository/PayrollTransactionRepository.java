package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Announcement;
import com.jomariabejo.motorph.model.PayrollTransaction;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayrollTransactionRepository implements GenericRepository<PayrollTransaction, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayrollTransactionRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public PayrollTransaction findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PayrollTransaction.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PayrollTransaction> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PayrollTransaction", PayrollTransaction.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(PayrollTransaction payrollTransaction) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(payrollTransaction);
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
    public void update(PayrollTransaction payrollTransaction) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(payrollTransaction);
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
    public void delete(PayrollTransaction payrollTransaction) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(payrollTransaction);
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
