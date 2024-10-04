package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.YearToDateFigures;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PayslipRepository extends _AbstractHibernateRepository<Payslip, Integer> {
    public PayslipRepository() {
        super(Payslip.class);
    }

    public Optional<List<Payslip>> findPayslipByEmployeeAndYear(Employee employee, Integer year) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Payslip> query = session.createNamedQuery("fetchPayslipByEmployeeAndYear", Payslip.class);
            query.setParameter("employeeId", employee);
            query.setParameter("year", year);
            List<Payslip> resultList = query.getResultList();
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

    public Optional<List<Integer>> findEmployeeYearsOfPayslip(Employee employee) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Integer> query = session.createNamedQuery("fetchEmployeeYearsOfPayslip", Integer.class);
            query.setParameter("employeeId", employee);
            List<Integer> resultList = query.getResultList();
            return resultList.isEmpty()
                    // ibalik ang current year kahit walang fetched yr.
                    ? Optional.of(
                        Collections.singletonList(LocalDateTime.now().getYear()))
                    // ibalik ang mga taon
                    : Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Optional<YearToDateFigures> findYearToDateFiguresByEmployee(Employee employee, Year year) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<YearToDateFigures> query = session.createNamedQuery("fetchYearToDateFigures", YearToDateFigures.class);
            query.setParameter("employeeId", employee);
            query.setParameter("year", year.getValue());

            // Assuming there could be no result; handle accordingly
            YearToDateFigures result = query.getSingleResult();
            return Optional.ofNullable(result);

        } catch (NoResultException e) {
            // Log or handle the exception if needed
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
