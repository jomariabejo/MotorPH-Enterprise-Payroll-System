package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Timesheet;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TimesheetRepository implements GenericRepository<Timesheet, Integer> {

    private final HibernateUtil hibernateUtil;

    public TimesheetRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Timesheet findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Timesheet.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Timesheet> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Timesheet", Timesheet.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Timesheet timesheet) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(timesheet);
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
    public void update(Timesheet timesheet) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(timesheet);
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
    public void delete(Timesheet timesheet) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(timesheet);
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
