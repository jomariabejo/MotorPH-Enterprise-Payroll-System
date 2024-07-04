package withholdTaxTest;

import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class withHoldTaxTest {

    private TaxService taxService;
    private Deduction deduction;

    @BeforeEach
    void setUp() {
        taxService = new TaxService();

    }

    @Test
    public void shouldReturnZeroWithholdingTax() {
        deduction = new Deduction();
        deduction.setPhilhealth(BigDecimal.valueOf(1125));
        deduction.setSss(BigDecimal.valueOf(375));
        deduction.setPagibig(BigDecimal.valueOf(100));

        BigDecimal  withholdTaxComputationResult = taxService.computeTax(
                BigDecimal.valueOf(25000),
                deduction);


        BigDecimal expectedResult = BigDecimal.valueOf(513.4).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedResult, withholdTaxComputationResult);
    }

    @Test
    public void shouldReturnWithheldTax() {


        deduction = new Deduction();
        deduction.setPhilhealth(BigDecimal.valueOf(900));
        deduction.setSss(BigDecimal.valueOf(1125));
        deduction.setPagibig(BigDecimal.valueOf(100));

        BigDecimal  withholdingtax = taxService.computeTax(
                BigDecimal.valueOf(80648),
                deduction);

        BigDecimal expectedResult = BigDecimal.valueOf(14389.863).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedResult, withholdingtax);
    }
}
