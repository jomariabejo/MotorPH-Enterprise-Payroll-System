package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.List;

public class DepartmentRepository implements GenericRepository<Department, Integer> {

    private final SessionFactory sessionFactory;

    public DepartmentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Department findById(Integer id) {
        Session session = sessionFactory.openSession();
        Department department = session.get(Department.class, id);
        session.close();
        return department;
    }

    @Override
    public List<Department> findAll() {
        Session session = sessionFactory.openSession();
        List<Department> departments = session.createQuery("FROM Department", Department.class).list();
        session.close();
        return departments;
    }

    @Override
    public void save(Department department) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(department);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    @Override
    public void update(Department department) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(department);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Department department) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(department);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            session.close();
        }
    }
}
