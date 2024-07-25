package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Permission;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PermissionRepository {

    private final HibernateUtil hibernateUtil;

    public PermissionRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    public Permission findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Permission.class, id);
        } finally {
            session.close();
        }
    }

    public List<Permission> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Permission", Permission.class).list();
        } finally {
            session.close();
        }
    }

    public void save(Permission permission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(permission);
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

    public void update(Permission permission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(permission);
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

    public void delete(Permission permission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(permission);
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
