package deductionTest;

import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.service.DeductionService;
import com.jomariabejo.motorph.utility.DateUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TestSaveDeduction {
    private DeductionService deductionService;

    @BeforeEach
    void setUp() {
        deductionService = new DeductionService();
    }

    @Test
    void saveDeductionTest() {
        Deduction deduction = new Deduction();
        deduction.setDeductionID(1);
        deduction.setEmployeeID(3);
        deduction.setPagibig(new BigDecimal("500"));
        deduction.setPhilhealth(new BigDecimal("500"));
        deduction.setSss(new BigDecimal("500"));
        deduction.setDateCreated(DateUtility.getDate());

        deductionService.saveDeduction(deduction);
    }
}
