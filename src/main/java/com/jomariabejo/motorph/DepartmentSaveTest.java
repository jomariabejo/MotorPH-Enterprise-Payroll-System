package com.jomariabejo.motorph;

import com.jomariabejo.motorph.model.Department;
import com.jomariabejo.motorph.repository.DepartmentRepository;
import com.jomariabejo.motorph.service.DepartmentService;

public class DepartmentSaveTest {
    public static void main(String[] args) {
        DepartmentRepository departmentRepository = new DepartmentRepository();
        DepartmentService departmentService = new DepartmentService(departmentRepository);
        Department department = new Department();
        department.setDepartmentName("Department Name");
        departmentService.saveDepartment(department);
    }
}