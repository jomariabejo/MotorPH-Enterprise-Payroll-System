package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class EmployeeRepository extends _AbstractHibernateRepository<Employee, Integer> {
    public EmployeeRepository() {
        super(Employee.class);
    }

    public boolean verifyEmployeeName(String name) {
        try {
            Session session = null;
            session = HibernateUtil.openSession();

            Query<Employee> query = session.createNamedQuery("findEmployeeName", Employee.class);
            query.setParameter("NAME", name);

            return query.getSingleResult() != null;
        }
        catch (NoResultException noResultException) {
            return false;
        }
    }
}
