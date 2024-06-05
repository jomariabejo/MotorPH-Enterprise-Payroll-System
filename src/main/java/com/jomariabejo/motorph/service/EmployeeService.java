package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.record.AccountNumber;
import com.jomariabejo.motorph.repository.EmployeeRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class EmployeeService {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    public void saveEmployee(Employee employee) throws SQLException {
        employeeRepository.createNewEmployeeRecord(employee);
    }

    public ArrayList<Employee> fetchEmployees() throws SQLException {
        return employeeRepository.getEmployees();
    }

    public ArrayList<Employee> fetchActiveEmployees() throws SQLException {
        return employeeRepository.getActiveEmployees();
    }

    public ArrayList<Employee> fetchInactiveEmployees() throws SQLException {
        return employeeRepository.getInactiveEmployees();
    }
    public Employee fetchEmployee(int employeeId) throws SQLException {
        return employeeRepository.getEmployeeById(employeeId);
    }

    public Optional<String> fetchEmployeeName(int employeeId) throws SQLException {
        return employeeRepository.getEmployeeNameById(employeeId);
    }

    public int countEmployees() throws SQLException {
        return employeeRepository.getTotalNumberOfEmployees();
    }


    public int countActiveEmployees() {
        try {
            return employeeRepository.getTotalNumberOfActiveEmployees();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void modifyEmployee(Employee employee) throws SQLException {
        employeeRepository.updateEmployeeRecord(employee);
    }

    public void deleteEmployee(int employeeId) {
        try {
            employeeRepository.deleteEmployeeRecord(employeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setEmployeeInactive(int employeeId) {
        try {
            employeeRepository.setEmployeeInactive(employeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int countInactiveActiveEmployees() {
        try {
            return employeeRepository.countInactiveEmployees();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public Employee fetchActiveEmployee(int employeeId) {
        try {
            return employeeRepository.getActiveEmployeeById(employeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Employee fetchInActiveEmployee(int employeeId) {
        try {
            return employeeRepository.getInActiveEmployeeById(employeeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AccountNumber fetchEmployeeAccountNumber(int employeeId) {
        return employeeRepository.fetchEmployeeAccountNumber(employeeId);
    }

    public boolean checkIfEmployeeExist(int employeeId) {
        return employeeRepository.checkIfEmployeeExist(employeeId);
    }
}
