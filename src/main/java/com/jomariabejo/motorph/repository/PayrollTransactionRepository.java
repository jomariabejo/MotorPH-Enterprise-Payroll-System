package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollTransaction;
import com.jomariabejo.motorph.model.Payslip;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PayrollTransactionRepository extends _AbstractHibernateRepository<PayrollTransaction, Integer> {
    public PayrollTransactionRepository() {
        super(PayrollTransaction.class);
    }

    public List<PayrollTransaction> findByPayroll(Payroll payroll) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE pt.payrollID = :payroll",
                    PayrollTransaction.class
            );
            query.setParameter("payroll", payroll);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> findByPayslip(Payslip payslip) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE pt.payslipID = :payslip",
                    PayrollTransaction.class
            );
            query.setParameter("payslip", payslip);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> findByTransactionType(String transactionType) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE pt.transactionType = :type",
                    PayrollTransaction.class
            );
            query.setParameter("type", transactionType);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> findByDateRange(LocalDate startDate, LocalDate endDate) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE pt.transactionDate BETWEEN :startDate AND :endDate",
                    PayrollTransaction.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> findByPayrollAndType(Payroll payroll, String type) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE pt.payrollID = :payroll AND pt.transactionType = :type",
                    PayrollTransaction.class
            );
            query.setParameter("payroll", payroll);
            query.setParameter("type", type);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> findAllWithPagination(int page, int pageSize) {
        Session session = HibernateUtil.openSession();
        try {
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt ORDER BY pt.transactionDate DESC, pt.id DESC",
                    PayrollTransaction.class
            );
            query.setFirstResult(page * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    public long countAll() {
        Session session = HibernateUtil.openSession();
        try {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(pt) FROM PayrollTransaction pt",
                    Long.class
            );
            return query.uniqueResult() != null ? query.uniqueResult() : 0L;
        } finally {
            session.close();
        }
    }

    public List<PayrollTransaction> searchTransactions(String searchTerm) {
        Session session = HibernateUtil.openSession();
        try {
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            Query<PayrollTransaction> query = session.createQuery(
                    "FROM PayrollTransaction pt WHERE " +
                    "LOWER(pt.transactionType) LIKE :search OR " +
                    "LOWER(pt.description) LIKE :search " +
                    "ORDER BY pt.transactionDate DESC, pt.id DESC",
                    PayrollTransaction.class
            );
            query.setParameter("search", searchPattern);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
}
