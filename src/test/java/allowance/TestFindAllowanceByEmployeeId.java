package allowance;

import com.jomariabejo.motorph.service.AllowanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFindAllowanceByEmployeeId {
    private AllowanceService allowanceService;

    @BeforeEach
    void setUp() {
        allowanceService = new AllowanceService();
    }

    @Test
    void FindJomariAllowance() throws SQLException {
        int theAllowonceIdOfJomariFromDB = allowanceService.getAllowanceIdByEmployeeId(3);
        int expectedAllowanceId = 2;
        assertEquals(expectedAllowanceId, theAllowonceIdOfJomariFromDB, "Nice one!");
    }
}
