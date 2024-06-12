package employee;

import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

public class testSetEmployeeInactive {

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void setEmployeeInactive_ShouldSetEmployeeToInactive() throws SQLException {
        int employeeId = 9;

        doNothing().when(employeeService).setEmployeeInactive(employeeId);

        assertDoesNotThrow(() -> {
            employeeService.setEmployeeInactive(employeeId);
        }, "The setEmployeeInactive method should set the employee to inactive without throwing an exception");

        verify(employeeService, times(1)).setEmployeeInactive(employeeId);
    }
}

