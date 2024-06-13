package allowance;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.service.AllowanceService;
import com.jomariabejo.motorph.utility.CurrentTimestampUtility;
import com.jomariabejo.motorph.utility.DateConversionUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Calendar;


public class testCreateAllowance {
    @Mock
    private AllowanceService allowanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldCreateAllowanceSuccessfully() throws SQLException {
        Allowance allowance = new Allowance();
        allowance.setClothingAllowance(100);
        allowance.setRiceAllowance(200);
        allowance.setPhoneAllowance(50);
        allowance.setTotalAmount(350);
        allowance.setDateCreated(DateConversionUtility.toSqlDate(Calendar.getInstance().getTime()));
        allowance.setDateModified(CurrentTimestampUtility.getCurrentTimestamp());
        allowance.setEmployeeID(8);

        assertDoesNotThrow(() -> {
            doNothing().when(allowanceService).createAllowance(any(Allowance.class));
            allowanceService.createAllowance(allowance);
        }, "The createAllowance method should not throw an exception");

        verify(allowanceService, times(1)).createAllowance(any(Allowance.class));
    }
}
