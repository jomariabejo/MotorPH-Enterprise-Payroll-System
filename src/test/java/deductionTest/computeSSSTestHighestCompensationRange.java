package deductionTest;

import com.jomariabejo.motorph.service.DeductionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class computeSSSTestHighestCompensationRange {
    private DeductionService deductionService;
    private BigDecimal expectedSSSComputation;
    private BigDecimal grossSalary;

    @BeforeEach
    void setUp() {
        deductionService = new DeductionService();
        expectedSSSComputation = new BigDecimal("1125.00");
        grossSalary = new BigDecimal("50000");
    }

    @Test
    void shouldBe1125() {
        assertEquals(expectedSSSComputation, deductionService.deductSSS(grossSalary), "The SSS Computation with Highest Compensation meets the desired contribution outcome");
    }
}
