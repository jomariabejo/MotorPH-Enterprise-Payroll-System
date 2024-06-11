package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import com.jomariabejo.motorph.query.QueryPath;
import com.jomariabejo.motorph.record.AccountNumber;
import com.jomariabejo.motorph.utility.TextReader;
import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class EmployeeRepository {

    public final String QUERY_BASE_PATH = "src/main/java/com/jomariabejo/motorph/query/employee";

    public void createNewEmployeeRecord(Employee employee) {
        String query = "INSERT INTO EMPLOYEE (\n" +
                "    first_name, last_name, birthday, address,\n" +
                "    contact_number, status, date_hired,\n" +
                "    position_id, supervisor, dept_id,\n" +
                "    sss, philhealth, pagibig, tin, basic_salary,\n" +
                "    gross_semi_monthly_rate, hourly_rate\n" +
                ")\n" +
                "VALUES\n" +
                "    (\n" +
                "        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,\n" +
                "        ?\n" +
                "    );";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            preparedStatementSetter(employee, ps);

            ps.executeUpdate();
            System.out.println("Record Created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateEmployeeRecord(Employee employee) {
        String query = QueryPath.Employee.UPDATE_EMPLOYEE_RECORD;
        System.out.println(query);
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            preparedStatementSetter(employee, ps);
            System.out.println("My firstname is = " + employee.getFirstName());
            ps.setInt(18, employee.getEmployeeId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record Updated");
            } else {
                System.out.println("Record not Updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteEmployeeRecord(int employee_id) throws SQLException {
        String query = TextReader.readTextFile(QueryPath.Employee.DELETE_EMPLOYEE);
        System.out.println(query);
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employee_id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record Removed");
            } else {
                System.out.println("Record not Removed");
            }
        }
    }

    private void preparedStatementSetter(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setString(1, employee.getFirstName());
        ps.setString(2, employee.getLastName());
        ps.setDate(3, employee.getBirthday());
        ps.setString(4, employee.getAddress());
        ps.setString(5, employee.getContactNumber());
        ps.setString(6, String.valueOf(employee.getStatus()));
        ps.setDate(7, employee.getDateHired());
        ps.setInt(8, employee.getPositionId());
        ps.setString(9, employee.getSupervisor());
        ps.setInt(10, employee.getDeptId());
        ps.setString(11, employee.getSss());
        ps.setString(12, employee.getPhilhealth());
        ps.setString(13, employee.getPagibig());
        ps.setString(14, employee.getTin());
        ps.setBigDecimal(15, employee.getBasicSalary());
        ps.setBigDecimal(16, employee.getGrossSemiMonthlyRate());
        ps.setBigDecimal(17, employee.getHourlyRate());
    }

    public Employee getEmployeeById(int employeeId) throws SQLException {
        String query = "SELECT * FROM EMPLOYEE WHERE employee_id = ?;";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, employeeId);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("birthday"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number"),
                            EmployeeStatus.valueOf(resultSet.getString("status")),
                            resultSet.getDate("date_hired"),
                            resultSet.getInt("position_id"),
                            resultSet.getString("supervisor"),
                            resultSet.getInt("dept_id"),
                            resultSet.getString("sss"),
                            resultSet.getString("philhealth"),
                            resultSet.getString("pagibig"),
                            resultSet.getString("tin"),
                            resultSet.getBigDecimal("basic_salary"),
                            resultSet.getBigDecimal("gross_semi_monthly_rate"),
                            resultSet.getBigDecimal("hourly_rate")
                    );
                }
            }
        }
        return null;
    }

    public Employee getActiveEmployeeById(int employeeId) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_employee_by_id_active.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, employeeId);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("birthday"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number"),
                            EmployeeStatus.valueOf(resultSet.getString("status")),
                            resultSet.getDate("date_hired"),
                            resultSet.getInt("position_id"),
                            resultSet.getString("supervisor"),
                            resultSet.getInt("dept_id"),
                            resultSet.getString("sss"),
                            resultSet.getString("philhealth"),
                            resultSet.getString("pagibig"),
                            resultSet.getString("tin"),
                            resultSet.getBigDecimal("basic_salary"),
                            resultSet.getBigDecimal("gross_semi_monthly_rate"),
                            resultSet.getBigDecimal("hourly_rate")
                    );
                }
            }
        }
        return null;
    }

    public Employee getInActiveEmployeeById(int employeeId) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_employee_by_id_inactive.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, employeeId);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getDate("birthday"),
                            resultSet.getString("address"),
                            resultSet.getString("contact_number"),
                            EmployeeStatus.valueOf(resultSet.getString("status")),
                            resultSet.getDate("date_hired"),
                            resultSet.getInt("position_id"),
                            resultSet.getString("supervisor"),
                            resultSet.getInt("dept_id"),
                            resultSet.getString("sss"),
                            resultSet.getString("philhealth"),
                            resultSet.getString("pagibig"),
                            resultSet.getString("tin"),
                            resultSet.getBigDecimal("basic_salary"),
                            resultSet.getBigDecimal("gross_semi_monthly_rate"),
                            resultSet.getBigDecimal("hourly_rate")
                    );
                }
            }
        }
        return null;
    }

    public Optional<String> getEmployeeNameById(int employeeId) {
        String query = "SELECT first_name, last_name FROM EMPLOYEE WHERE employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1, employeeId);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            (resultSet.getString("first_name")) +
                                    " " // space
                                    +
                                    (resultSet.getString("last_name")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public int getTotalNumberOfEmployees() throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/count_employees.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("NumberOfEmployees");
                }
            }
        }
        return 0;
    }


    public int getTotalNumberOfActiveEmployees() throws SQLException {
//        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/count_active_employees.sql");
        String query = QueryPath.Employee.COUNT_ACTIVE_EMPLOYEES;
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("NumberOfActiveEmployees");
                }
            }
        }
        return 0;
    }

    public ArrayList<Employee> getEmployees() throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_employees.sql");
        ArrayList<Employee> employeeList = new ArrayList<Employee>();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query);
             ResultSet resultSet = prepStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setBirthday(resultSet.getDate("birthday"));
                employee.setAddress(resultSet.getString("address"));
                employee.setContactNumber(resultSet.getString("contact_number"));
                employee.setStatus(EmployeeStatus.valueOf(resultSet.getString("status"))); // Assuming "status" is a string representing EmployeeStatus enum value
                employee.setDateHired(resultSet.getDate("date_hired"));
                employee.setPositionId(resultSet.getInt("position_id"));
                employee.setSupervisor(resultSet.getString("supervisor"));
                employee.setDeptId(resultSet.getInt("dept_id"));
                employee.setSss(resultSet.getString("sss"));
                employee.setPhilhealth(resultSet.getString("philhealth"));
                employee.setPagibig(resultSet.getString("pagibig"));
                employee.setTin(resultSet.getString("tin"));
                employee.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
                employee.setGrossSemiMonthlyRate(resultSet.getBigDecimal("gross_semi_monthly_rate"));
                employee.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    public ArrayList<Employee> getActiveEmployees() throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_active_employees.sql");
        ArrayList<Employee> employeeList = new ArrayList<Employee>();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query);
             ResultSet resultSet = prepStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setBirthday(resultSet.getDate("birthday"));
                employee.setAddress(resultSet.getString("address"));
                employee.setContactNumber(resultSet.getString("contact_number"));
                employee.setStatus(EmployeeStatus.valueOf(resultSet.getString("status"))); // Assuming "status" is a string representing EmployeeStatus enum value
                employee.setDateHired(resultSet.getDate("date_hired"));
                employee.setPositionId(resultSet.getInt("position_id"));
                employee.setSupervisor(resultSet.getString("supervisor"));
                employee.setDeptId(resultSet.getInt("dept_id"));
                employee.setSss(resultSet.getString("sss"));
                employee.setPhilhealth(resultSet.getString("philhealth"));
                employee.setPagibig(resultSet.getString("pagibig"));
                employee.setTin(resultSet.getString("tin"));
                employee.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
                employee.setGrossSemiMonthlyRate(resultSet.getBigDecimal("gross_semi_monthly_rate"));
                employee.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                employeeList.add(employee);
            }
        }
        return employeeList;
    }


    public ArrayList<Employee> getInactiveEmployees() throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_inactive_employees.sql");
        ArrayList<Employee> employeeList = new ArrayList<Employee>();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(query);
             ResultSet resultSet = prepStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployeeId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setBirthday(resultSet.getDate("birthday"));
                employee.setAddress(resultSet.getString("address"));
                employee.setContactNumber(resultSet.getString("contact_number"));
                employee.setStatus(EmployeeStatus.valueOf(resultSet.getString("status"))); // Assuming "status" is a string representing EmployeeStatus enum value
                employee.setDateHired(resultSet.getDate("date_hired"));
                employee.setPositionId(resultSet.getInt("position_id"));
                employee.setSupervisor(resultSet.getString("supervisor"));
                employee.setDeptId(resultSet.getInt("dept_id"));
                employee.setSss(resultSet.getString("sss"));
                employee.setPhilhealth(resultSet.getString("philhealth"));
                employee.setPagibig(resultSet.getString("pagibig"));
                employee.setTin(resultSet.getString("tin"));
                employee.setBasicSalary(resultSet.getBigDecimal("basic_salary"));
                employee.setGrossSemiMonthlyRate(resultSet.getBigDecimal("gross_semi_monthly_rate"));
                employee.setHourlyRate(resultSet.getBigDecimal("hourly_rate"));
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    public void setEmployeeInactive(int employeeId) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/set_employee_inactive.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();

             PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setInt(1,employeeId);
            int isUpdated = prepStatement.executeUpdate();
            if (isUpdated > 0) {
                System.out.println("Updated success");
            } else {
                System.out.println("Update failed");
            }
        }
    }

    public int countInactiveEmployees() {
        String query = QueryPath.Employee.COUNT_INACTIVE_EMPLOYEES;
        int totalInactiveEmployees = 0;

        try(Connection connection = DatabaseConnectionUtility.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalInactiveEmployees = rs.getInt("NumberOfInactiveEmployees");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalInactiveEmployees;
    }

    public AccountNumber fetchEmployeeAccountNumber(int employeeId) {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\employee\\get_employee_bank_accounts.sql");

        try(Connection connection = DatabaseConnectionUtility.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1,employeeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new AccountNumber(
                        rs.getString("sss"),
                        rs.getString("philhealth"),
                        rs.getString("pagibig"),
                        rs.getString("tin")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean checkIfEmployeeExist(int employeeId) {
        String query = "SELECT employee_id FROM EMPLOYEE WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}