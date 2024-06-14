package timesheet;

import com.jomariabejo.motorph.service.TimesheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class testSetTimeOut {
    @Mock
    private TimesheetService timesheetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldSetTimeOutSuccessfully() throws Exception {
        Time timeOut = Time.valueOf("17:00:00");
        int employeeId = 12;
        Date date = Date.valueOf("2024-06-07");

        when(timesheetService.setTimeOut(employeeId, date, timeOut)).thenReturn(true);
        boolean result = timesheetService.setTimeOut(employeeId, date, timeOut);
        assertTrue(result, "The setTimeOut method should return true for successful time out operation");
    }
}