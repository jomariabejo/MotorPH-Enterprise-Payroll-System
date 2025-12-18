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
<<<<<<< HEAD
                    "SELECT e FROM Employee e WHERE (e.isDeleted = false OR e.isDeleted IS NULL) AND e.status IN ('Regular', 'Probationary') ORDER BY e.employeeNumber",
=======
                    "SELECT e FROM Employee e WHERE e.status IN ('Regular', 'Contractual', 'Probationary') ORDER BY e.employeeNumber",
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
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
