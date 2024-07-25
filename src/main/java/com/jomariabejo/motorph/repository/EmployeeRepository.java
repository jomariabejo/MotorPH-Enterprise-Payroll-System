package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Employee;

public class EmployeeRepository extends _AbstractHibernateRepository<Employee, Integer> {
    public EmployeeRepository() {
        super(Employee.class);
    }
}
