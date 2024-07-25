package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Bonus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class BonusRepository implements GenericRepository<Bonus, Integer> {

    private final HibernateUtil hibernateUtil;

    public BonusRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Bonus findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Bonus.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Bonus> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Bonus", Bonus.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Bonus bonus) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(bonus);
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
    public void update(Bonus bonus) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(bonus);
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
    public void delete(Bonus bonus) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(bonus);
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
