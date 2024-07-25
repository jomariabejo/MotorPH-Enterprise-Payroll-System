package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Department;

public class DepartmentRepository extends _AbstractHibernateRepository<Department, Integer> {
    public DepartmentRepository() {
        super(Department.class);
    }
}
