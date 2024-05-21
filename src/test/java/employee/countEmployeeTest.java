package employee;

import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class countEmployeeTest {

    private final EmployeeService employeeService = new EmployeeService();
    @Test
    public void EMPLOYEES_COUNT_SHOULD_BE_33() {
        assertEquals(33, employeeService.countActiveEmployees());
        assertTrue(employeeService.countActiveEmployees() >= 30 && employeeService.countActiveEmployees() < 40);
        assertNotEquals(30, employeeService.countActiveEmployees());
    }

    @Test
    public void INACTIVE_EMPLOYEES_COUNT_SHOULD_BE_2() {
        assertEquals(2, employeeService.countInactiveActiveEmployees());
    }
}
