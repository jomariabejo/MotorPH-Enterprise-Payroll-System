package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.TinCompliance;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TinComplianceRepository implements GenericRepository<TinCompliance, Integer> {

    private final HibernateUtil hibernateUtil;

    public TinComplianceRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public TinCompliance findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(TinCompliance.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<TinCompliance> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM TinCompliance", TinCompliance.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(TinCompliance tinCompliance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(tinCompliance);
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
    public void update(TinCompliance tinCompliance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(tinCompliance);
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
    public void delete(TinCompliance tinCompliance) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(tinCompliance);
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
