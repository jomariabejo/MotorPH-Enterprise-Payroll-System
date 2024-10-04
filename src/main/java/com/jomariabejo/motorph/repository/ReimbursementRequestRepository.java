package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.service.ReimbursementRequestService;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class ReimbursementRequestRepository extends _AbstractHibernateRepository<ReimbursementRequest, Integer> {
    public ReimbursementRequestRepository() {
        super(ReimbursementRequest.class);
    }

    public Optional<List<Integer>> findEmployeeYearsOfReimbursement(Employee owner) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createNamedQuery("fetchEmployeeYearsOfReimbursement", Integer.class);
            query.setParameter("reimbursementOwner", owner);
            List<Integer> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<List<ReimbursementRequest>> findReimbursementByEmployeeAndYear(Employee reimbursementOwner, Integer year) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<ReimbursementRequest> query = session.createNamedQuery("fetchReimbursementsByEmployeeAndYear", ReimbursementRequest.class);
            query.setParameter("reimbursementOwner", reimbursementOwner);
            query.setParameter("year", year);
            return query.getResultList().isEmpty() ? Optional.empty() : Optional.of(query.getResultList());
        } catch (NoResultException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
