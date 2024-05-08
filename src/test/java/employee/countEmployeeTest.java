package employee;

import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class countEmployeeTest {

    private EmployeeService employeeService = new EmployeeService();
    @Test
    public void EMPLOYEES_COUNT_SHOULD_BE_33() {
        assertTrue(employeeService.countActiveEmployees() == 33);
        assertTrue(employeeService.countActiveEmployees() >= 30 && employeeService.countActiveEmployees() < 40);
        assertFalse(employeeService.countActiveEmployees() == 30);
    }

    @Test
    public void INACTIVE_EMPLOYEES_COUNT_SHOULD_BE_2() {
        assertTrue(employeeService.countInactiveActiveEmployees() == 2);
    }
}
