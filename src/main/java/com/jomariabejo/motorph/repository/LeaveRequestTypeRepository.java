package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.exception.LeaveRequestTypeNotFoundException;
import com.jomariabejo.motorph.exception.DuplicateLeaveRequestTypeException;
import com.jomariabejo.motorph.exception.InvalidLeaveRequestTypeException;
import com.jomariabejo.motorph.exception.LeaveRequestTypeDeletionException;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LeaveRequestTypeRepository implements GenericRepository<LeaveRequestType, Integer> {

    private final HibernateUtil hibernateUtil;

    public LeaveRequestTypeRepository(HibernateUtil hibernateUtil) {
        this.hibernateUtil = hibernateUtil;
    }

    @Override
    public LeaveRequestType findById(Integer id) {
        Session session = hibernateUtil.openSession();
        try {
            LeaveRequestType leaveRequestType = session.get(LeaveRequestType.class, id);
            if (leaveRequestType == null) {
                throw new LeaveRequestTypeNotFoundException("Leave request type with ID " + id + " not found.");
            }
            return leaveRequestType;
        } finally {
            session.close();
        }
    }

    @Override
    public List<LeaveRequestType> findAll() {
        Session session = hibernateUtil.openSession();
        try {
            return session.createQuery("FROM LeaveRequestType", LeaveRequestType.class).list();
        } finally {
            session.close();
        }
    }

    @Override
    public void save(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(leaveRequestType);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
                throw new DuplicateLeaveRequestTypeException("Leave request type with name " + leaveRequestType.getLeaveTypeName() + " already exists.");
            } else {
                throw new InvalidLeaveRequestTypeException("Failed to save leave request type.");
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void update(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(leaveRequestType);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new InvalidLeaveRequestTypeException("Failed to update leave request type.");
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(LeaveRequestType leaveRequestType) {
        Session session = hibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(leaveRequestType);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new LeaveRequestTypeDeletionException("Failed to delete leave request type.");
        } finally {
            session.close();
        }
    }
}
