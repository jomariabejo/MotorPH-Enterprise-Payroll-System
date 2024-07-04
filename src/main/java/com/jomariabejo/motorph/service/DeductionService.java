package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.repository.DeductionRepository;
import com.jomariabejo.motorph.utility.TextReader;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeductionService {

    private final DeductionRepository deductionRepository;

    public DeductionService() {
        this.deductionRepository = new DeductionRepository();
    }

    /**
     * Computes the PhilHealth deduction for an employee based on their basic salary.
     *
     * @param basicSalary The basic salary of the employee.
     * @return The computed PhilHealth deduction.
     */
    public BigDecimal deductPhilhealth(BigDecimal basicSalary) {
        double premiumRate = 0.03;
        double employeeShare = 2;

        BigDecimal deduction;

        if (basicSalary.compareTo(BigDecimal.valueOf(10000)) <= 0) {
            deduction = BigDecimal.valueOf(300).divide(BigDecimal.valueOf(employeeShare), 2, RoundingMode.HALF_UP);
        } else if (basicSalary.compareTo(BigDecimal.valueOf(60000)) < 0) {
            deduction = basicSalary.multiply(BigDecimal.valueOf(premiumRate))
                    .divide(BigDecimal.valueOf(employeeShare), 2, RoundingMode.HALF_UP);
        } else {
            deduction = BigDecimal.valueOf(1800).divide(BigDecimal.valueOf(employeeShare), 2, RoundingMode.HALF_UP);
        }

        return deduction;
    }

    /**
     * Deducts Pag-IBIG contribution from the employee's basic salary.
     * Pag-IBIG contribution rates are based on the employee's basic salary.
     *
     * @param basicSalary The basic salary of the employee.
     * @return The deducted Pag-IBIG contribution.
     */
    public BigDecimal deductPagIbig(BigDecimal basicSalary) {
        // Determine the contribution based on the basic salary
        double contribution = (basicSalary.compareTo(BigDecimal.valueOf(1500)) >= 0) ? basicSalary.multiply(BigDecimal.valueOf(0.02)).doubleValue()
                : (basicSalary.compareTo(BigDecimal.valueOf(1000)) >= 0) ? basicSalary.multiply(BigDecimal.valueOf(0.01)).doubleValue()
                : 0.00;

        // Ensure that the contribution does not exceed the maximum limit of 100
        return (contribution >= 100) ? BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP) : BigDecimal.valueOf(contribution).setScale(2, RoundingMode.HALF_UP);
    }


    public BigDecimal deductSSS(BigDecimal grossSalary) {
        BigDecimal contribution;

        // Define contribution and salary limits
        BigDecimal minimumSalaryLimit = new BigDecimal("3250");
        BigDecimal maximumSalaryLimit = new BigDecimal("3750");

        // Define contribution values
        BigDecimal initialContribution = new BigDecimal("135.00");
        BigDecimal highestContribution = new BigDecimal("1125.00");
        BigDecimal contributionIncrement = new BigDecimal("22.50");

        // Check if gross salary is below the first threshold
        if (grossSalary.compareTo(minimumSalaryLimit) < 0) {
            contribution = initialContribution.setScale(2);
        }
        // Check if gross salary is at or above the highest threshold
        else if (grossSalary.compareTo(new BigDecimal("24750")) >= 0) {
            contribution = highestContribution.setScale(2);
        }
        // Calculate contribution based on gross salary within the range
        else {
            contribution = new BigDecimal("157.50");

            // Loop to find the correct contribution within the range
            while (contribution.compareTo(highestContribution) < 0) {
                // Check if gross salary is within the current salary range
                if (grossSalary.compareTo(minimumSalaryLimit) >= 0 &&
                        grossSalary.compareTo(maximumSalaryLimit) < 0) {
                    return contribution.setScale(2);
                } else {
                    // Adjust salary limits and contribution for the next range
                    minimumSalaryLimit = minimumSalaryLimit.add(new BigDecimal("500"));
                    maximumSalaryLimit = maximumSalaryLimit.add(new BigDecimal("500"));
                    contribution = contribution.add(contributionIncrement);
                }
            }
            // Return 0 if the contribution calculation exceeds the maximum contribution
            return BigDecimal.ZERO.setScale(2);
        }
        return contribution.setScale(2);
    }

    public void saveDeduction(Deduction deduction) {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\deduction\\create_deduction.sql");

        try(Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, deduction.getDeductionID());
            preparedStatement.setInt(2, deduction.getEmployeeID());
            preparedStatement.setBigDecimal(3, deduction.getSss());
            preparedStatement.setBigDecimal(4, deduction.getPhilhealth());
            preparedStatement.setBigDecimal(5, deduction.getPagibig());
            preparedStatement.setBigDecimal(6, deduction.totalContributions());
            preparedStatement.setDate(7, deduction.getDateCreated());
            int rowAffected = preparedStatement.executeUpdate();

            if (rowAffected > 0) {
                System.out.println("Insert deduction success.");
            }
            else {
                System.out.println("Insert deduction failed.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyDeduction(Deduction deduction, int payslipNumber) {
        String query = "UPDATE deduction d " +
                "JOIN payslip p ON d.deduction_id = p.deduction_id " +
                "SET d.sss = ?, d.philhealth = ?, d.pagibig = ?, d.total_contribution = ? " +
                "WHERE p.payslip_id = ?";

        try (Connection conn = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the prepared statement
            pstmt.setBigDecimal(1, deduction.getSss());
            pstmt.setBigDecimal(2, deduction.getPhilhealth());
            pstmt.setBigDecimal(3, deduction.getPagibig());

            // Calculate and set total contribution
            BigDecimal totalContribution = deduction.calculateTotalContribution();
            pstmt.setBigDecimal(4, totalContribution);

            pstmt.setInt(5, payslipNumber);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any potential errors
        }

    }
}
