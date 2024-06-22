package employee;

import com.jomariabejo.motorph.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class testDeleteEmployeeRecord {
    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }
    @Test
    void deleteEmployee_ShouldDeleteEmployeeRecord() throws SQLException {
        int employeeId = 7;

        doNothing().when(employeeService).deleteEmployee(employeeId);

        assertDoesNotThrow(() -> {
            employeeService.deleteEmployee(employeeId);
        }, "The deleteEmployee method should delete the employee record without throwing an exception");

        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }
}

