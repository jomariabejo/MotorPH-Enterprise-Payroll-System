package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoleRepository implements GenericRepository<Role, Integer> {

    private final HibernateUtil hibernateUtil;

    public RoleRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Role findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Role.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Role> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Role", Role.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Role role) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(role);
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
    public void update(Role role) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(role);
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
    public void delete(Role role) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(role);
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
