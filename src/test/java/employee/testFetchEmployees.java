package employee;

import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;

public class testFetchEmployees {
    private EmployeeService employeeService;


    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void fetchEmployees_ShouldReturnNonNullList() {
        assertDoesNotThrow(() -> {
            ArrayList<Employee> employees = employeeService.fetchEmployees();
            assertNotNull(employees, "The fetchEmployees method should return a non-null list");
            assertEquals(34, employees.size(), "The fetchEmployees method should return a list with 36 employees");
        });
    }

    @Test
    void fetchEmployees_ShouldHandleSQLException() {
        try {
            employeeService.fetchEmployees();
        } catch (SQLException e) {
            fail("The fetchEmployees method should handle SQLException properly");
        }
    }

    @Test
    void fetchActiveEmployees_ShouldReturnNonNullList() {
        assertDoesNotThrow(() -> {
            ArrayList<Employee> activeEmployees = employeeService.fetchActiveEmployees();
            assertNotNull(activeEmployees, "The fetchActiveEmployees method should return a non-null list");
            assertEquals(34, activeEmployees.size(), "The fetchActiveEmployees method should return a list with 36 employees");
        });
    }

    @Test
    void fetchActiveEmployees_ShouldHandleSQLException() {
        try {
            employeeService.fetchActiveEmployees();
        } catch (SQLException e) {
            fail("The fetchActiveEmployees method should handle SQLException properly");
        }
    }

    @Test
    void fetchInactiveEmployees_ShouldReturnNonNullList() {
        assertDoesNotThrow(() -> {
            ArrayList<Employee> inactiveEmployees = employeeService.fetchInactiveEmployees();
            assertNotNull(inactiveEmployees, "The fetchInactiveEmployees method should return a non-null list");
            assertEquals(0, inactiveEmployees.size(), "The fetchInactiveEmployees method should return a list with 0 employee");
        });
    }

    @Test
    void fetchInactiveEmployees_ShouldHandleSQLException() {
        try {
            employeeService.fetchInactiveEmployees();
        } catch (SQLException e) {
            fail("The fetchInactiveEmployees method should handle SQLException properly");
        }
    }
}

