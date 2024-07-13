package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.entity.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    private final Connection connection;

    public DepartmentRepository(Connection connection) {
        this.connection = connection;
    }

    public void create(Department department) throws SQLException {
        String query = "INSERT INTO department (name, description, date_created) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getDescription());
            statement.setDate(3, department.getDateCreated());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                int departmentID = resultSet.getInt(1);
                department.setDepartmentID(departmentID);
            }
        }
    }

    // GET operation by ID
    public Department get(int departmentID) throws SQLException {
        String query = "SELECT * FROM department WHERE dept_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return extractDepartment(resultSet);
            }
            return null;
        }
    }

    // GET operation for all departments
    public List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM department";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                departments.add(extractDepartment(resultSet));
            }
        }
        return departments;
    }

    // UPDATE operation
    public void update(Department department) throws SQLException {
        String query = "UPDATE department SET name = ?, description = ? WHERE dept_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getDescription());
            statement.setInt(3, department.getDepartmentID());
            statement.executeUpdate();
        }
    }

    // DELETE operation
    public void delete(int departmentID) throws SQLException {
        String query = "DELETE FROM department WHERE dept_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, departmentID);
            statement.executeUpdate();
        }
    }

    // Helper method to extract Department object from ResultSet
    private Department extractDepartment(ResultSet resultSet) throws SQLException {
        int departmentID = resultSet.getInt("dept_id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        java.sql.Date dateCreated = resultSet.getDate("date_created");
        return new Department(departmentID, name, description, dateCreated);
    }

    public List<String> getDepartmentsName() throws SQLException {
        List<String> departments = new ArrayList<>();
        String query = "SELECT name FROM department";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                departments.add(resultSet.getString("name"));
            }
        }
        return departments;
    }
}
