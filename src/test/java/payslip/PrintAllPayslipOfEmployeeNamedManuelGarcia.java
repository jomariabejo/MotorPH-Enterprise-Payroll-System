package payslip;

import com.jomariabejo.motorph.service.PayslipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrintAllPayslipOfEmployeeNamedManuelGarcia {
    private PayslipService payslipService;
    private final byte EXPECTED_RESULT = 1;
    @BeforeEach
    void setUp() {
        payslipService = new PayslipService();
    }

    @Test
    void MANUEL_GARCIA_PAYSLIP_COUNT_SHOULD_BE_ONE() {
        int countManuelGarciaPayslips = payslipService.fetchPayslipByEmployeeId(6).size();
        assertEquals(EXPECTED_RESULT, countManuelGarciaPayslips, "Nice kaayo!!");
    }
}
