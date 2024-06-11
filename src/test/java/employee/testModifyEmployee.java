package employee;

import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class testModifyEmployee {
    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void modifyEmployee_ShouldUpdateEmployeeRecord() throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(7);
        employee.setFirstName("UpdatedFirstName");
        employee.setLastName("UpdatedLastName");

        assertDoesNotThrow(() -> {
            doNothing().when(employeeService).modifyEmployee(any(Employee.class));
            employeeService.modifyEmployee(employee);
        }, "The modifyEmployee method should update the employee record without throwing an exception");

        verify(employeeService, times(1)).modifyEmployee(any(Employee.class));
    }
}
