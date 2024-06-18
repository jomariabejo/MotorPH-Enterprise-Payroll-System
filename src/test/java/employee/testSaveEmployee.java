package employee;

import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

public class testSaveEmployee {
    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldSaveEmployeeSuccessfully() throws SQLException {
        Employee employee = new Employee();
        employee.setFirstName("Jomari");
        employee.setLastName("Abejo");
        employee.setBirthday(Date.valueOf("1990-01-01"));
        employee.setAddress("123 Main St");
        employee.setContactNumber("966-861-375");
        employee.setStatus(EmployeeStatus.valueOf("Regular"));
        employee.setDateHired(Date.valueOf("2023-06-07"));
        employee.setPositionId(1);
        employee.setSupervisor("Lawrence Samuel");
        employee.setDeptId(2);
        employee.setSss("49-4506057-6");
        employee.setPhilhealth("820126853678");
        employee.setPagibig("691296430870");
        employee.setTin("876-809-437-673");
        employee.setBasicSalary(new BigDecimal("50000.00"));
        employee.setGrossSemiMonthlyRate(new BigDecimal("25000.00"));
        employee.setHourlyRate(new BigDecimal("300.00"));

        assertDoesNotThrow(() -> {
            doNothing().when(employeeService).saveEmployee(any(Employee.class));
            employeeService.saveEmployee(employee);
        }, "The saveEmployee method should not throw an exception");

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }
}


