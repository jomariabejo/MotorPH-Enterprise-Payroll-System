package timesheet;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class testSetTimeIn {
    private TimesheetService timesheetService;
    private Timesheet timesheet;


    @BeforeEach
    void setUp() {
        timesheetService = new TimesheetService();

    }
    @Test
    void shouldSetTimeInSuccessfully() throws SQLException {
        timesheetService = new TimesheetService();
        Date date = Date.valueOf("2024-06-07");
        Time timeIn = Time.valueOf("09:00:00");
        int employeeId = 12;

        timesheet = new Timesheet();
        timesheet.setDate(date);
        timesheet.setTimeIn(timeIn);
        timesheet.setEmployeeId(employeeId);

        boolean result = timesheetService.setTimeIn(timesheet);
        assertTrue(result, "The setTimeIn method should return true for successful time in operation");

    }
}
