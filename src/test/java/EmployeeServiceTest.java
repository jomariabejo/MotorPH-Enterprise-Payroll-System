
import com.jomariabejo.motorph.repository.EmployeeRepository;
import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveEmployee() {
        // Create a sample Employee
        Employee employee = new Employee();
        employee.setId(1);
        employee.setLastName("Doe");
        employee.setFirstName("John");
        employee.setBirthday(LocalDate.of(1990, 1, 1));
        employee.setAddress("123 Main St");
        employee.setPhoneNumber("123-456-7890");
        employee.setSSSNumber("123-456-789");
        employee.setPhilhealthNumber("987-654-321");
        employee.setTINNumber("456-789-123");
        employee.setPagibigNumber("321-654-987");
        employee.setStatus("Active");

        // Mock positionID
        Position position = new Position();
        position.setId(1);
        employee.setPositionID(position);

        employee.setBasicSalary(new BigDecimal("50000.0000"));
        employee.setRiceSubsidy(new BigDecimal("2000.0000"));
        employee.setPhoneAllowance(new BigDecimal("1000.0000"));
        employee.setClothingAllowance(new BigDecimal("1500.0000"));
        employee.setGrossSemiMonthlyRate(new BigDecimal("27500.0000"));
        employee.setHourlyRate(new BigDecimal("200.0000"));

        // Mock repository method save to capture argument
        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        doNothing().when(employeeRepository).save(employeeCaptor.capture());

        // Call service method to save the employee
        employeeService.saveEmployee(employee);

        // Verify that repository's save method was called once with the correct argument
        verify(employeeRepository, times(1)).save(employeeCaptor.getValue());

        // Assert that the captured employee matches the expected employee
        Employee capturedEmployee = employeeCaptor.getValue();
        assertEquals(employee.getId(), capturedEmployee.getId());
        assertEquals(employee.getLastName(), capturedEmployee.getLastName());
        assertEquals(employee.getFirstName(), capturedEmployee.getFirstName());
        assertEquals(employee.getBirthday(), capturedEmployee.getBirthday());
        assertEquals(employee.getAddress(), capturedEmployee.getAddress());
        assertEquals(employee.getPhoneNumber(), capturedEmployee.getPhoneNumber());
        assertEquals(employee.getSSSNumber(), capturedEmployee.getSSSNumber());
        assertEquals(employee.getPhilhealthNumber(), capturedEmployee.getPhilhealthNumber());
        assertEquals(employee.getTINNumber(), capturedEmployee.getTINNumber());
        assertEquals(employee.getPagibigNumber(), capturedEmployee.getPagibigNumber());
        assertEquals(employee.getStatus(), capturedEmployee.getStatus());
        assertEquals(employee.getPositionID(), capturedEmployee.getPositionID());
        assertEquals(employee.getBasicSalary(), capturedEmployee.getBasicSalary());
        assertEquals(employee.getRiceSubsidy(), capturedEmployee.getRiceSubsidy());
        assertEquals(employee.getPhoneAllowance(), capturedEmployee.getPhoneAllowance());
        assertEquals(employee.getClothingAllowance(), capturedEmployee.getClothingAllowance());
        assertEquals(employee.getGrossSemiMonthlyRate(), capturedEmployee.getGrossSemiMonthlyRate());
        assertEquals(employee.getHourlyRate(), capturedEmployee.getHourlyRate());
    }
}
