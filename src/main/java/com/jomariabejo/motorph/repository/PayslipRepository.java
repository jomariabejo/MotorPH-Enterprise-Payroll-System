package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.service.DeductionService;
import com.jomariabejo.motorph.service.TaxCategoryService;
import com.jomariabejo.motorph.service.TaxService;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PayslipRepository {
    private final TaxService taxService;
    private final TaxCategoryService taxCategoryService;
    private final DeductionService deductionService;
    private int counter;

    public PayslipRepository() {
        this.taxService = new TaxService();
        this.deductionService = new DeductionService();
        this.taxCategoryService = new TaxCategoryService();
    }

    /**
     * Save multiple payslip into database.
     * @param employeePayrollSummaryReports
     */
    public void saveMultiplePayslip(ObservableList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        // Iterate through the list of payroll summary reports
        employeePayrollSummaryReports.stream().forEach(x -> {
            // Create a new Payslip instance
            Payslip payslip = new Payslip();

            // Get the next value of payslip unique identifier
            int payslipId = AutoIncrementUtility.getNextAutoIncrementValueForPayslip();

            // Create a new Deduction instance
            Deduction deduction = new Deduction();
            deduction.setDeductionID(payslipId);
            deduction.setEmployeeID(x.getEmployeeNumber());
            deduction.setPagibig(x.getPagIbigContribution());
            deduction.setPhilhealth(x.getPhilhealthContribution());
            deduction.setSss(x.getSocialSecurityContribution());
            deduction.setDateCreated(DateUtility.getDate());

            // Save the deduction
            this.deductionService.saveDeduction(deduction);

            // Create a new Tax instance
            Tax tax = new Tax();
            tax.setTaxId(payslipId);
            tax.setEmployeeId(x.getEmployeeNumber());
            tax.setWithheldTax(x.getWithholdingTax());
            tax.setTaxableIncome(x.getTaxableIncome());
            tax.setDateCreated(DateUtility.getDate());

            // Fetch tax category ID by monthly rate and save tax
            tax.setTaxCategoryId(taxCategoryService.fetchTaxCategoryIdByMonthlyRate(x.getGrossIncome()));
            this.taxService.saveTax(tax);

            // Set payslip attributes
            payslip.setPayslipID(payslipId);
            payslip.setTaxID(payslipId); //
            payslip.setDeductionID(payslipId);
            payslip.setEmployeeID(x.getEmployeeNumber());
            payslip.setAllowanceID(x.getAllowanceId());
            payslip.setPayPeriodStart(x.getStartPayDate());
            payslip.setPayPeriodEnd(x.getEndPayDate());
            payslip.setTotalHoursWorked(x.getTotalOvertimeHoursWorked().add(x.getTotalRegularHoursWorked()));
            payslip.setGrossIncome(x.getGrossIncome());
            payslip.setNetIncome(x.getNetPay());
            payslip.setDateCreated(DateUtility.getDate());
            // Save the payslip
            savePayslip(payslip);
        });
    }


    private void savePayslip(Payslip payslip) {
        String query = "INSERT INTO payslip (payslip_id, employee_id, alw_id, deduction_id, tax_id, pay_period_start, pay_period_end, total_hours_worked, gross_income, net_income, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
        counter++;

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,payslip.getPayslipID());
            preparedStatement.setInt(2,payslip.getEmployeeID());
            preparedStatement.setInt(3,payslip.getAllowanceID());
            preparedStatement.setInt(4,payslip.getDeductionID());
            preparedStatement.setInt(5,payslip.getTaxID());
            preparedStatement.setDate(6,payslip.getPayPeriodStart());
            preparedStatement.setDate(7,payslip.getPayPeriodEnd());
            preparedStatement.setBigDecimal(8,payslip.getTotalHoursWorked());
            preparedStatement.setBigDecimal(9,payslip.getGrossIncome());
            preparedStatement.setBigDecimal(10,payslip.getNetIncome());
            preparedStatement.setDate(11,payslip.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println((rowsAffected > 0) ? ("Save payslip success " + counter)  : ("Not saved " + counter));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Payslip> fetchPayslipByPayslipId(int employeeId) {
        String query = "SELECT payslip_id, total_hours_worked, gross_income, net_income, pay_period_start, pay_period_end FROM payslip WHERE employee_id = ?";
        ArrayList<Payslip> myPayslips = new ArrayList<>();
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                myPayslips.add(
                    new Payslip(
                            resultSet.getInt(1),
                            resultSet.getBigDecimal(2).setScale(4),
                            resultSet.getBigDecimal(3).setScale(4),
                            resultSet.getBigDecimal(4).setScale(4),
                            resultSet.getDate(5),
                            resultSet.getDate(6)
                    )
                );
            }
            return myPayslips;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 1st process get the payslip
    // 2nd process get the regular hours worked and overtime hours worked.
    public Payslip.PayslipViewer fetchPayslipBreakdown(int payslipId) {


        String query = "SELECT\n" +
                "    p.employee_id,\n" +
                "    CONCAT(e.first_name, ' ', e.last_name) AS EMPLOYEE_NAME,\n" +
                "    p.pay_period_start,\n" +
                "    p.pay_period_end,\n" +
                "    pstn.name,\n" +
                "    e.basic_salary as MONTHLY_RATE,\n" +
                "    e.hourly_rate as HOURLY_RATE,\n" +
                "    p.gross_income,\n" +
                "    d.sss,\n" +
                "    d.philhealth,\n" +
                "    d.pagibig,\n" +
                "    t.withheld_tax,\n" +
                "    ((d.total_contribution) + (t.withheld_tax)) AS TOTAL_DEDUCTION,\n" +
                "    a.rice,\n" +
                "    a.phone,\n" +
                "    a.clothing,\n" +
                "    (a.total_amount) AS TOTAL_BENEFITS,\n" +
                "    p.net_income AS TAKE_HOME_PAY,\n" +
                "    ts.total_regular_hours_worked,\n" +
                "    ts.total_overtime_hours_worked,\n" +
                "    p.total_hours_worked\n" +
                "FROM\n" +
                "    payslip p\n" +
                "    INNER JOIN employee e ON p.employee_id = e.employee_id\n" +
                "    INNER JOIN position pstn ON e.position_id = pstn.position_id\n" +
                "    INNER JOIN allowance a ON p.alw_id = a.alw_id\n" +
                "    INNER JOIN deduction d ON p.deduction_id = d.deduction_id\n" +
                "    INNER JOIN tax t ON p.tax_id = t.tax_id\n" +
                "    INNER JOIN (\n" +
                "        SELECT\n" +
                "            t.employee_id,\n" +
                "            SUM(\n" +
                "                CASE WHEN TIME(t.time_in) < '08:00:00' THEN (\n" +
                "                    TIME_TO_SEC(TIMEDIFF('08:00:00', t.time_in)) / 3600.0\n" +
                "                ) WHEN TIME(t.time_out) > '17:00:00' THEN (\n" +
                "                    TIME_TO_SEC(TIMEDIFF('17:00:00', t.time_in)) / 3600.0\n" +
                "                ) ELSE (\n" +
                "                    TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)) / 3600.0\n" +
                "                ) END\n" +
                "            ) AS total_regular_hours_worked,\n" +
                "            SUM(\n" +
                "                CASE WHEN t.time_out <= '17:00:00' THEN 0 ELSE (\n" +
                "                    TIME_TO_SEC(TIMEDIFF(t.time_out, '17:00:00')) / 3600.0\n" +
                "                ) END\n" +
                "            ) AS total_overtime_hours_worked\n" +
                "        FROM\n" +
                "            timesheet t\n" +
                "            JOIN employee e ON t.employee_id = e.employee_id\n" +
                "            JOIN department d ON e.dept_id = d.dept_id\n" +
                "            JOIN payslip p ON e.employee_id = p.employee_id\n" +
                "        WHERE\n" +
                "            e.isActive = 1\n" +
                "            AND t.date BETWEEN p.pay_period_start AND p.pay_period_end\n" +
                "            AND t.employee_id = p.employee_id\n" +
                "        GROUP BY\n" +
                "            t.employee_id\n" +
                "    ) AS ts ON p.employee_id = ts.employee_id\n" +
                "WHERE\n" +
                "    p.payslip_id = ?;\n";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, payslipId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Payslip.PayslipViewer(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("EMPLOYEE_NAME"),
                        resultSet.getDate("pay_period_start"),
                        resultSet.getDate("pay_period_end"),
                        resultSet.getString("name"),
                        resultSet.getDouble("MONTHLY_RATE"),
                        resultSet.getDouble("HOURLY_RATE"),
                        resultSet.getDouble("gross_income"),
                        resultSet.getDouble("sss"),
                        resultSet.getDouble("philhealth"),
                        resultSet.getDouble("pagibig"),
                        resultSet.getDouble("withheld_tax"),
                        resultSet.getDouble("TOTAL_DEDUCTION"),
                        resultSet.getDouble("rice"),
                        resultSet.getDouble("phone"),
                        resultSet.getDouble("clothing"),
                        resultSet.getDouble("TOTAL_BENEFITS"),
                        resultSet.getDouble("TAKE_HOME_PAY"),
                        resultSet.getDouble("total_regular_hours_worked"),
                        resultSet.getDouble("total_overtime_hours_worked"),
                        resultSet.getDouble("total_hours_worked")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
