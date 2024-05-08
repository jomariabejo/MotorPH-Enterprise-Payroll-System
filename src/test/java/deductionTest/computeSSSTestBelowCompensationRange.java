package deductionTest;

import com.jomariabejo.motorph.service.DeductionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class computeSSSTestBelowCompensationRange {
    private DeductionService deductionService;
    private BigDecimal expectedSSSComputation;
    private BigDecimal grossSalary;

    @BeforeEach
    void setUp() {
        deductionService = new DeductionService();
        expectedSSSComputation = new BigDecimal("135.00");
        grossSalary = new BigDecimal("3249");
    }

    @Test
    void shouldBe135() {
        assertEquals(expectedSSSComputation, deductionService.deductSSS(grossSalary), "The SSS Computation Below 3,250 meets the desired contribution outcome");
    }
}
