package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollApproval;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PayrollApprovalRepository extends _AbstractHibernateRepository<PayrollApproval, Integer> {
    public PayrollApprovalRepository() {
        super(PayrollApproval.class);
    }

    public Optional<PayrollApproval> findByPayroll(Payroll payroll) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollApproval> query = session.createQuery(
                    "FROM PayrollApproval pa WHERE pa.payrollID = :payroll",
                    PayrollApproval.class
            );
            query.setParameter("payroll", payroll);
            PayrollApproval result = query.uniqueResult();
            return Optional.ofNullable(result);
        } finally {
            session.close();
        }
    }

    public List<PayrollApproval> findByStatus(String status) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollApproval> query = session.createQuery(
                    "FROM PayrollApproval pa WHERE pa.status = :status ORDER BY pa.approvalDate DESC",
                    PayrollApproval.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
