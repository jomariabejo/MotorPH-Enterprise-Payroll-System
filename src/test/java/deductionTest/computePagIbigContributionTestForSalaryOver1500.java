package deductionTest;

import com.jomariabejo.motorph.service.DeductionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class computePagIbigContributionTestForSalaryOver1500 {
    private BigDecimal monthlyBasicSalary;
    private BigDecimal expectedPagibigComputation;
    private DeductionService deductionService;

    @BeforeEach
    void setUp() {
        monthlyBasicSalary = new BigDecimal("2000.00");
        expectedPagibigComputation = new BigDecimal("40.00"); // 2% Contribution
        deductionService = new DeductionService();
    }
    @Test
    void pagibigComputationShouldBe40() {
        assertEquals(expectedPagibigComputation, deductionService.deductPagIbig(monthlyBasicSalary), "The Pag-ibig computation for salaries over 1,500 accurately meets the desired contribution outcome.");
    }
}

