package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.RolePermission;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RolePermissionRepository implements GenericRepository<RolePermission, Integer> {

    private final HibernateUtil hibernateUtil;

    public RolePermissionRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public RolePermission findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(RolePermission.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<RolePermission> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM RolePermission", RolePermission.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(RolePermission rolePermission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(rolePermission);
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
    public void update(RolePermission rolePermission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(rolePermission);
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
    public void delete(RolePermission rolePermission) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(rolePermission);
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
