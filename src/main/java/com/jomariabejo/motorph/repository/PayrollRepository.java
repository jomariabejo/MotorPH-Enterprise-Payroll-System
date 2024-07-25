package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Payroll;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PayrollRepository implements GenericRepository<Payroll, Integer> {

    private final HibernateUtil hibernateUtil;

    public PayrollRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Payroll findById(Integer id) {
        try (Session session = hibernateUtil.openSession()) {
            return session.get(Payroll.class, id);
        }
    }

    @Override
    public List<Payroll> findAll() {
        try (Session session = hibernateUtil.openSession()) {
            return session.createQuery("FROM Payroll", Payroll.class).list();
        }
    }

    @Override
    public void save(Payroll payroll) {
        performDatabaseOperation(session -> session.save(payroll));
    }

    @Override
    public void update(Payroll payroll) {
        performDatabaseOperation(session -> session.update(payroll));
    }

    @Override
    public void delete(Payroll payroll) {
        performDatabaseOperation(session -> session.delete(payroll));
    }

    private void performDatabaseOperation(DatabaseOperation operation) {
        Transaction tx = null;
        try (Session session = hibernateUtil.openSession()) {
            tx = session.beginTransaction();
            operation.execute(session);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @FunctionalInterface
    private interface DatabaseOperation {
        void execute(Session session);
    }
}
