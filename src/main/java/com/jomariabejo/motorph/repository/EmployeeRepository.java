package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class EmployeeRepository extends _AbstractHibernateRepository<Employee, Integer> {
    public EmployeeRepository() {
        super(Employee.class);
    }

    public Optional<List<Employee>> findActiveEmployees() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Employee> query = session.createQuery(
                    "SELECT e FROM Employee e WHERE e.status != 'INACTIVE' OR e.status IS NULL",
                    Employee.class
            );
            List<Employee> resultList = query.getResultList();
            return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList);
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
