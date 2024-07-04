package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.service.DeductionService;
import com.jomariabejo.motorph.service.TaxCategoryService;
import com.jomariabejo.motorph.service.TaxService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.math.RoundingMode;
import java.sql.*;
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
     *
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

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, payslip.getPayslipID());
            preparedStatement.setInt(2, payslip.getEmployeeID());
            preparedStatement.setInt(3, payslip.getAllowanceID());
            preparedStatement.setInt(4, payslip.getDeductionID());
            preparedStatement.setInt(5, payslip.getTaxID());
            preparedStatement.setDate(6, payslip.getPayPeriodStart());
            preparedStatement.setDate(7, payslip.getPayPeriodEnd());
            preparedStatement.setBigDecimal(8, payslip.getTotalHoursWorked());
            preparedStatement.setBigDecimal(9, payslip.getGrossIncome());
            preparedStatement.setBigDecimal(10, payslip.getNetIncome());
            preparedStatement.setDate(11, payslip.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println((rowsAffected > 0) ? ("Save payslip success " + counter) : ("Not saved " + counter));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Payslip> fetchPayslipByEmployeeId(int employeeId) {
        String query = "SELECT payslip_id, total_hours_worked, gross_income, net_income, pay_period_start, pay_period_end FROM payslip WHERE employee_id = ?";
        ArrayList<Payslip> myPayslips = new ArrayList<>();
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                myPayslips.add(
                        new Payslip(
                                resultSet.getInt(1),
                                resultSet.getBigDecimal(2).setScale(2, RoundingMode.HALF_UP),
                                resultSet.getBigDecimal(3).setScale(2, RoundingMode.HALF_UP),
                                resultSet.getBigDecimal(4).setScale(2, RoundingMode.HALF_UP),
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
                "    e.basic_salary AS MONTHLY_RATE,\n" +
                "    e.hourly_rate AS HOURLY_RATE,\n" +
                "    p.gross_income,\n" +
                "    d.sss,\n" +
                "    d.philhealth,\n" +
                "    d.pagibig,\n" +
                "    t.withheld_tax,\n" +
                "    (d.total_contribution + t.withheld_tax) AS TOTAL_DEDUCTION,\n" +
                "    a.rice,\n" +
                "    a.phone,\n" +
                "    a.clothing,\n" +
                "    a.total_amount AS TOTAL_BENEFITS,\n" +
                "    p.net_income AS TAKE_HOME_PAY,\n" +
                "    COALESCE(ts.total_regular_hours_worked, 0) AS total_regular_hours_worked,\n" +
                "    COALESCE(ts.total_overtime_hours_worked, 0) AS total_overtime_hours_worked,\n" +
                "    p.total_hours_worked\n" +
                "FROM\n" +
                "    payslip p\n" +
                "    INNER JOIN employee e ON p.employee_id = e.employee_id\n" +
                "    INNER JOIN position pstn ON e.position_id = pstn.position_id\n" +
                "    INNER JOIN allowance a ON p.alw_id = a.alw_id\n" +
                "    INNER JOIN deduction d ON p.deduction_id = d.deduction_id\n" +
                "    INNER JOIN tax t ON p.tax_id = t.tax_id\n" +
                "    LEFT JOIN (\n" +
                "        SELECT\n" +
                "            t.employee_id,\n" +
                "            SUM(\n" +
                "                CASE\n" +
                "                    WHEN TIME(t.time_out) <= '17:00:00' THEN (TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)) / 3600.0)\n" +
                "                    ELSE (TIME_TO_SEC(TIMEDIFF('17:00:00', t.time_in)) / 3600.0)\n" +
                "                END\n" +
                "            ) AS total_regular_hours_worked,\n" +
                "            SUM(\n" +
                "                CASE\n" +
                "                    WHEN TIME(t.time_out) > '17:00:00' THEN (TIME_TO_SEC(TIMEDIFF(t.time_out, '17:00:00')) / 3600.0)\n" +
                "                    ELSE 0\n" +
                "                END\n" +
                "            ) AS total_overtime_hours_worked\n" +
                "        FROM\n" +
                "            timesheet t\n" +
                "        WHERE\n" +
                "            t.date BETWEEN (SELECT pay_period_start FROM payslip WHERE payslip_id = ?) AND \n" +
                "            (SELECT pay_period_end FROM payslip WHERE payslip_id = ?)\n" +
                "        GROUP BY\n" +
                "            t.employee_id\n" +
                "    ) AS ts ON p.employee_id = ts.employee_id\n" +
                "WHERE\n" +
                "    p.payslip_id = ?;\n";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, payslipId);
            preparedStatement.setInt(2, payslipId);
            preparedStatement.setInt(3, payslipId);


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

    public ObservableList<Payslip> fetchPayslipSummary() {
        String query = "SELECT payslip.pay_period_start, \n" +
                "       payslip.pay_period_end,\n" +
                "       SUM(payslip.gross_income) AS total_gross_income,\n" +
                "       SUM(payslip.net_income) AS total_net_income\n" +
                "FROM payslip\n" +
                "GROUP BY payslip.pay_period_start, payslip.pay_period_end\n";


        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            ArrayList<Payslip> payslips = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Payslip payslip = new Payslip();
                payslip.setPayPeriodStart(rs.getDate(1));
                payslip.setPayPeriodEnd(rs.getDate(2));
                payslip.setGrossIncome(rs.getBigDecimal(3));
                payslip.setNetIncome(rs.getBigDecimal(4));
                payslips.add(payslip);
            }
            return FXCollections.observableList(payslips);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Payslip> fetchPayslipBetweenPayStartAndPayEnd(Date payStartDate, Date payEndDate) {
        String query = "SELECT payslip_id, total_hours_worked, gross_income, net_income, pay_period_start, pay_period_end FROM payslip WHERE pay_period_start = ? AND pay_period_end = ?";
        ArrayList<Payslip> myPayslips = new ArrayList<>();
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, payStartDate);
            preparedStatement.setDate(2, payEndDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                myPayslips.add(
                        new Payslip(
                                resultSet.getInt(1),
                                resultSet.getBigDecimal(2).setScale(2, RoundingMode.HALF_UP),
                                resultSet.getBigDecimal(3).setScale(2, RoundingMode.HALF_UP),
                                resultSet.getBigDecimal(4).setScale(2, RoundingMode.HALF_UP),
                                resultSet.getDate(5),
                                resultSet.getDate(6)
                        )
                );
            }
            return FXCollections.observableList(myPayslips);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfCanCreatePayslipForPayPeriod(Date startPayDate, Date endPayDate) {
        String query = "SELECT COUNT(*) FROM payslip " +
                "WHERE (? BETWEEN pay_period_start AND pay_period_end " +
                "OR ? BETWEEN pay_period_start AND pay_period_end " +
                "OR pay_period_start BETWEEN ? AND ? " +
                "OR pay_period_end BETWEEN ? AND ?)";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, startPayDate);
            preparedStatement.setDate(2, endPayDate);
            preparedStatement.setDate(3, startPayDate);
            preparedStatement.setDate(4, endPayDate);
            preparedStatement.setDate(5, startPayDate);
            preparedStatement.setDate(6, endPayDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int payslipsCount = resultSet.getInt(1);
                if (payslipsCount >= 0) {
                    AlertUtility.showErrorAlert("Failed", "Pay period exist", "Pay period exist\\nThe pay period that you've inputted is already generated, please check on payslips");
                }
                return payslipsCount == 0; // Returns true if no overlapping payslip found
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking payslip overlap", e);
        }

        return true; // Default to true if an exception occurs
    }

    public void modifyPayslip(Payslip payslip) {
        String query = "UPDATE PAYSLIP SET " +
                "total_hours_worked = ?," +
                "gross_income = ?," +
                "net_income = ? " +
                "WHERE payslip_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setBigDecimal(1, payslip.getTotalHoursWorked());
            pstmt.setBigDecimal(2, payslip.getGrossIncome());
            pstmt.setBigDecimal(3, payslip.getNetIncome());
            pstmt.setInt(4,payslip.getPayslipID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
