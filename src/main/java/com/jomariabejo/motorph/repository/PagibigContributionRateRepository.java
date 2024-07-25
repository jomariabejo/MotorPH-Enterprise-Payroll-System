package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.PagibigContributionRate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PagibigContributionRateRepository implements GenericRepository<PagibigContributionRate, Integer> {

    private final HibernateUtil hibernateUtil;

    public PagibigContributionRateRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public PagibigContributionRate findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PagibigContributionRate.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<PagibigContributionRate> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PagibigContributionRate", PagibigContributionRate.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(PagibigContributionRate pagibigContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(pagibigContributionRate);
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
    public void update(PagibigContributionRate pagibigContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(pagibigContributionRate);
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
    public void delete(PagibigContributionRate pagibigContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(pagibigContributionRate);
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
