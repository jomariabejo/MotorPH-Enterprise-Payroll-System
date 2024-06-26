package deductionTest;

import com.jomariabejo.motorph.service.DeductionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class computePagIbigContributionTestForSalaryStructure1000To1500 {
    private BigDecimal monthlyBasicSalary;
    private BigDecimal expectedPagibigComputation;
    private DeductionService deductionService;

    @BeforeEach
    void setUp() {
        monthlyBasicSalary = new BigDecimal("1000.00");
        expectedPagibigComputation = new BigDecimal("10.00"); // 1% Contribution
        deductionService = new DeductionService();
    }
    @Test
    void pagibigComputationShouldBe10() {
        assertEquals(expectedPagibigComputation, deductionService.deductPagIbig(monthlyBasicSalary), "The Pag-ibig computation for salaries between 1,000 to 1,500 meets the desired contribution outcome");
    }
}