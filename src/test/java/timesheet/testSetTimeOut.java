package timesheet;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;


import static org.junit.jupiter.api.Assertions.assertTrue;


public class testSetTimeOut {
    private TimesheetService timesheetService;
    private Timesheet timesheet;


    @BeforeEach
    void setUp() {
        timesheetService = new TimesheetService();
    }
    @Test
    void shouldSetTimeOutSuccessfully() throws SQLException {
        int employeeId = 12;
        Date date = Date.valueOf("2024-06-07");
        Time timeOut = Time.valueOf("17:00:00");


        boolean result = timesheetService.setTimeOut(employeeId, date, timeOut);
        assertTrue(result, "The setTimeOut method should return true for successful time out operation");
    }
}

