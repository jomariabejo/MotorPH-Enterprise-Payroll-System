package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Department;
import com.jomariabejo.motorph.repository.DepartmentRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService() throws SQLException {
        // Establish database connection
        Connection connection = DatabaseConnectionUtility.getConnection();
        // Initialize repository with connection
        this.departmentRepository = new DepartmentRepository(connection);
    }

    // CREATE operation
    public void createDepartment(String name, String description) throws SQLException {
        Department department = new Department(name, description, null); // Assuming dateCreated will be set automatically
        departmentRepository.create(department);
    }

    // GET operation by ID
    public Department getDepartment(int departmentID) throws SQLException {
        return departmentRepository.get(departmentID);
    }

    // GET operation for all departments
    public List<Department> getAllDepartments() throws SQLException {
        return departmentRepository.getAll();
    }

    public List<String> getDepartmentsName() throws SQLException {
        return departmentRepository.getDepartmentsName();
    }

    // UPDATE operation
    public void updateDepartment(int departmentID, String name, String description) throws SQLException {
        Department department = departmentRepository.get(departmentID);
        if (department != null) {
            department.setName(name);
            department.setDescription(description);
            departmentRepository.update(department);
        } else {
            System.out.println("Department not found.");
        }
    }

    // DELETE operation
    public void deleteDepartment(int departmentID) throws SQLException {
        departmentRepository.delete(departmentID);
    }
}
