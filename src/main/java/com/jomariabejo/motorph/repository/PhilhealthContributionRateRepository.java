package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.PhilhealthContributionRate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PhilhealthContributionRateRepository {

    private final HibernateUtil hibernateUtil;

    public PhilhealthContributionRateRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public PhilhealthContributionRate findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(PhilhealthContributionRate.class, id);
        } finally {
            session.close();
        }
    }

    public List<PhilhealthContributionRate> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM PhilhealthContributionRate", PhilhealthContributionRate.class).list();
        } finally {
            session.close();
        }
    }

    public void save(PhilhealthContributionRate rate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(rate);
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

    public void update(PhilhealthContributionRate rate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(rate);
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

    public void delete(PhilhealthContributionRate rate) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(rate);
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
