package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.SssContributionRate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SssContributionRateRepository implements GenericRepository<SssContributionRate, Integer> {

    private final HibernateUtil hibernateUtil;

    public SssContributionRateRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public SssContributionRate findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(SssContributionRate.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<SssContributionRate> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM SssContributionRate", SssContributionRate.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(SssContributionRate sssContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(sssContributionRate);
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
    public void update(SssContributionRate sssContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(sssContributionRate);
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
    public void delete(SssContributionRate sssContributionRate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(sssContributionRate);
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
