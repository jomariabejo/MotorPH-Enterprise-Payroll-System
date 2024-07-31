package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public abstract class _AbstractHibernateRepository<T, ID extends Serializable> implements _GenericRepository<T, ID> {

    private final Class<T> entityClass;
    private HibernateUtil hibernateUtil;

    public _AbstractHibernateRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.hibernateUtil = new HibernateUtil();
    }

    @Override
    public T findById(ID id) {
        Session session = HibernateUtil.openSession();
        try {
            return session.get(entityClass, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<T> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM " + entityClass.getSimpleName(), entityClass).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(T entity) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(entity);
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
    public void update(T entity) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(entity);
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
    public void delete(T entity) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(entity);
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

    protected List<?> runQuery(String hql, Class<?> resultClass) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(hql, resultClass).list();
        } finally {
            session.close();
        }
    }
}
