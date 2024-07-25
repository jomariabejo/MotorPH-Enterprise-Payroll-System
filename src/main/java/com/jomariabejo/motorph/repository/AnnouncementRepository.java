package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Announcement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class AnnouncementRepository implements GenericRepository<Announcement, Integer> {

    private final HibernateUtil hibernateUtil;

    public AnnouncementRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public Announcement findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            return session.get(Announcement.class, id);
        } finally {
            session.close();
        }
    }

    @Override
    public List<Announcement> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM Announcement", Announcement.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(Announcement announcement) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(announcement);
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
    public void update(Announcement announcement) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(announcement);
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
    public void delete(Announcement announcement) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(announcement);
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
