package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.OvertimeRequest;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.sql.ast.tree.expression.Over;

import java.sql.Date;
import java.util.Optional;

public class OvertimeRequestRepository extends _AbstractHibernateRepository<OvertimeRequest, Integer> {
    public OvertimeRequestRepository() {
        super(OvertimeRequest.class);
    }

    public Optional<OvertimeRequest> hasOvertimeRequestForDate(Employee employee, Date date) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<OvertimeRequest> query = session.createNamedQuery("isEmployeeAlreadyHaveRequest", OvertimeRequest.class);

            query.setParameter("employee", employee);
            query.setParameter("date", date);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return Optional.empty();
    }
}
