package allowance;

import com.jomariabejo.motorph.entity.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaxTest {

    @Test
    public void testDefaultConstructor() {
        Tax tax = new Tax();
        assertEquals(0, tax.getTaxId());
        assertEquals(0, tax.getEmployeeId());
        assertNull(tax.getTaxableIncome());
        assertEquals(0, tax.getTaxCategoryId());
        assertNull(tax.getWithheldTax());
        assertNull(tax.getDateCreated());
    }

    @Test
    public void testConstructorWithThreeParams() {
        BigDecimal taxableIncome = new BigDecimal("1000.00");
        int taxCategoryId = 1;
        BigDecimal withheldTax = new BigDecimal("100.00");

        Tax tax = new Tax(taxableIncome, taxCategoryId, withheldTax);

        assertEquals(taxableIncome, tax.getTaxableIncome());
        assertEquals(taxCategoryId, tax.getTaxCategoryId());
        assertEquals(withheldTax, tax.getWithheldTax());
    }

    @Test
    public void testConstructorWithAllParams() {
        int employeeId = 123;
        BigDecimal taxableIncome = new BigDecimal("2000.00");
        int taxCategoryId = 2;
        BigDecimal withheldTax = new BigDecimal("200.00");
        Date dateCreated = Date.valueOf("2023-06-15");

        Tax tax = new Tax(employeeId, taxableIncome, taxCategoryId, withheldTax, dateCreated);

        assertEquals(employeeId, tax.getEmployeeId());
        assertEquals(taxableIncome, tax.getTaxableIncome());
        assertEquals(taxCategoryId, tax.getTaxCategoryId());
        assertEquals(withheldTax, tax.getWithheldTax());
        assertEquals(dateCreated, tax.getDateCreated());
    }

    @Test
    public void testSettersAndGetters() {
        Tax tax = new Tax();

        tax.setTaxId(1);
        assertEquals(1, tax.getTaxId());

        tax.setEmployeeId(456);
        assertEquals(456, tax.getEmployeeId());

        BigDecimal taxableIncome = new BigDecimal("3000.00");
        tax.setTaxableIncome(taxableIncome);
        assertEquals(taxableIncome, tax.getTaxableIncome());

        tax.setTaxCategoryId(3);
        assertEquals(3, tax.getTaxCategoryId());

        BigDecimal withheldTax = new BigDecimal("300.00");
        tax.setWithheldTax(withheldTax);
        assertEquals(withheldTax, tax.getWithheldTax());

        Date dateCreated = Date.valueOf("2024-01-01");
        tax.setDateCreated(dateCreated);
        assertEquals(dateCreated, tax.getDateCreated());
    }

    @Test
    public void testToString() {
        int employeeId = 123;
        BigDecimal taxableIncome = new BigDecimal("4000.00");
        int taxCategoryId = 4;
        BigDecimal withheldTax = new BigDecimal("400.00");
        Date dateCreated = Date.valueOf("2024-06-15");

        Tax tax = new Tax(employeeId, taxableIncome, taxCategoryId, withheldTax, dateCreated);

        String expected = "Tax{taxId=0, taxableIncome=4000.00, taxCategoryId=4, withheldTax=400.00}";
        assertEquals(expected, tax.toString());
    }
}
