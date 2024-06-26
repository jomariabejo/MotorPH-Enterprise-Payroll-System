package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.record.SalaryStructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalaryRepository {

    /**
     * Retrieves the salary details of the specified employee by their ID.
     *
     * @param employeeId The ID of the employee whose salary details are to be fetched.
     * @return The salary details of the specified employee.
     * @throws RuntimeException If there is an error during database operation.
     */
    public SalaryStructure fetchSalaryDetailsByEmployeeId(int employeeId) {
        String query = "SELECT basic_salary, gross_semi_monthly_rate, hourly_rate FROM employee WHERE employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new SalaryStructure(
                            resultSet.getBigDecimal("basic_salary"),
                            resultSet.getBigDecimal("gross_semi_monthly_rate"),
                            resultSet.getBigDecimal("hourly_rate"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching salary details for employee ID: " + employeeId, e);
        }
        return null; // No record found for the specified employee ID
    }

}
