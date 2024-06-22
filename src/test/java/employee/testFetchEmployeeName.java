package employee;

import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class testFetchEmployeeName {
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void fetchEmployeeName_ShouldReturnCorrectName() throws SQLException {
        int employeeId = 9;
        Optional<String> employeeName = employeeService.fetchEmployeeName(employeeId);
        assertTrue(employeeName.isPresent(), "The fetchEmployeeName method should return a non-empty optional");
        assertEquals("Isabella Reyes", employeeName.get(), "The fetched employee name should match the expected name");
    }
}
