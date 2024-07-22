package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Position;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PositionRepository implements GenericRepository<Position, Integer> {

    private final HibernateUtil hibernateUtil;

    public PositionRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Position findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Position.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Position> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Position", Position.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Position position) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(position);
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
    public void update(Position position) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(position);
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
    public void delete(Position position) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(position);
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
