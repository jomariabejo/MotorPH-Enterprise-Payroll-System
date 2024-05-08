package deductionTest;

import com.jomariabejo.motorph.service.DeductionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class computePhilhealthContributionTestWithMonthlyBasicSalary_60000 {

    private BigDecimal monthlyBasicSalary;
    private BigDecimal expectedResult;
    private DeductionService deductionService;

    @BeforeEach
    void setUp() {
        monthlyBasicSalary = new BigDecimal("60000.00");
        expectedResult = new BigDecimal("1800.00").divide(BigDecimal.valueOf(2)); // // we divide as the payments are equally shared between the employee and employer.
        deductionService = new DeductionService();
    }

    @Test
    void philhealthComputationShouldBe900() {
        BigDecimal computationResult = deductionService.deductPhilhealth(monthlyBasicSalary);
        assertEquals(expectedResult, computationResult);
    }
}
