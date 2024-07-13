package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.TextReader;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;
import java.sql.*;

public class TaxRepository {
    public void saveTax(Tax tax) {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\tax\\create_tax.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, tax.getTaxId());
            preparedStatement.setBigDecimal(2, tax.getTaxableIncome());
            preparedStatement.setInt(3, tax.getTaxCategoryId());
            preparedStatement.setBigDecimal(4, tax.getWithheldTax());
            preparedStatement.setInt(5, tax.getEmployeeId());
            preparedStatement.setDate(6, tax.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insert tax success");
            } else {
                System.out.println("Insert tax failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTaxByPayslipId(int payslipId, BigDecimal taxableIncome, int taxCategoryId, BigDecimal withheldTax) {
        String query = "UPDATE tax t " +
                "JOIN payslip p ON t.employee_id = p.employee_id " +
                "SET t.taxable_income = ?, t.tax_cat_id = ?, t.withheld_tax = ? " +
                "WHERE p.payslip_id = ?";

        try (Connection conn = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters for the prepared statement
            pstmt.setBigDecimal(1, taxableIncome);
            pstmt.setInt(2, taxCategoryId);
            pstmt.setBigDecimal(3, withheldTax);
            pstmt.setInt(4, payslipId);

            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyTax(Tax tax, int payslipId) {
        String query = "UPDATE tax t " +
                "JOIN payslip p ON t.tax_id = p.tax_id " +
                "SET " +
                "t.taxable_income = ?, " +
                "t.tax_cat_id = ?, " +
                "t.withheld_tax = ? " +
                "WHERE p.payslip_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setBigDecimal(1, tax.getTaxableIncome());
            pstmt.setInt(2, tax.getTaxCategoryId());
            pstmt.setBigDecimal(3, tax.getWithheldTax());
            pstmt.setInt(4, payslipId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Tax updated successfully");
                System.out.println(tax.toString());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
