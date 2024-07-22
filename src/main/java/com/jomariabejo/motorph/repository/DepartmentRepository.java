package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Department;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DepartmentRepository implements GenericRepository<Department, Integer> {

    private final HibernateUtil hibernateUtil;

    public DepartmentRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Department findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Department.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Department> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Department", Department.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Department department) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(department);
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
    public void update(Department department) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(department);
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
    public void delete(Department department) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(department);
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
