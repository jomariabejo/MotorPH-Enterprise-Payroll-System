package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class EmployeeRepository extends _AbstractHibernateRepository<Employee, Integer> {
    public EmployeeRepository() {
        super(Employee.class);
    }
}
