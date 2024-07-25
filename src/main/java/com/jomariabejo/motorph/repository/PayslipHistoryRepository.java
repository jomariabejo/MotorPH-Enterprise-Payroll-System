package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.PayslipHistory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayslipHistoryRepository implements GenericRepository<PayslipHistory, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayslipHistoryRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public PayslipHistory findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PayslipHistory.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PayslipHistory> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PayslipHistory", PayslipHistory.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(PayslipHistory payslipHistory) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(payslipHistory);
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
    public void update(PayslipHistory payslipHistory) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(payslipHistory);
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
    public void delete(PayslipHistory payslipHistory) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(payslipHistory);
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
