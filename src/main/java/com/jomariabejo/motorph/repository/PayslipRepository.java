package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayslipRepository implements GenericRepository<Payslip, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayslipRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Payslip findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Payslip.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Payslip> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Payslip", Payslip.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Payslip payslip) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(payslip);
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
    public void update(Payslip payslip) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(payslip);
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
    public void delete(Payslip payslip) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(payslip);
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
