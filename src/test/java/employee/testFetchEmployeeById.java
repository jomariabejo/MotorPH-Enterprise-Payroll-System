package employee;

import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class testFetchEmployeeById {

    @Mock
    private EmployeeService employeeService;


    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService();
    }

    @Test
    void fetchEmployee_ShouldFetchEmployeeById() throws SQLException {
        int employeeId = 9;
        Employee employee = employeeService.fetchEmployee(employeeId);
        assertNotNull(employee, "The fetchEmployee method should return a non-null employee");
        assertEquals(employeeId, employee.getEmployeeId(), "The fetched employee ID should match the requested ID");
    }
}





