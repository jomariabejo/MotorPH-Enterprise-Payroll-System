package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.PayrollApproval;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayrollApprovalRepository implements GenericRepository<PayrollApproval, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayrollApprovalRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public PayrollApproval findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PayrollApproval.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PayrollApproval> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PayrollApproval", PayrollApproval.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(PayrollApproval payrollApproval) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(payrollApproval);
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
    public void update(PayrollApproval payrollApproval) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(payrollApproval);
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
    public void delete(PayrollApproval payrollApproval) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(payrollApproval);
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
