package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;

public class TaxRepository {
    public void saveTax(Tax tax) throws SQLException {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\tax\\create_tax.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, tax.getTaxableIncome()   );
            preparedStatement.setInt(2, tax.getTaxCategoryId());
            preparedStatement.setBigDecimal(3, tax.getWithheldTax());
            preparedStatement.setInt(4, tax.getEmployeeId());
            preparedStatement.setDate(5, tax.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insert tax success");
            } else {
                System.out.println("Insert tax failed");
            }
        }
    }

    private Tax fetchTaxByEmployeeIdAndDate(int employeeId, Date dateCreated) throws SQLException {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\deduction\\get_deduction.sql");
        Tax tax = new Tax();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1,dateCreated);
            preparedStatement.setInt(2,employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tax.setEmployeeId(resultSet.getInt("employee_id"));
                tax.setWithheldTax(resultSet.getBigDecimal("withheld_tax"));
                tax.setDateCreated(resultSet.getDate("date_created"));
                tax.setTaxableIncome(resultSet.getBigDecimal("taxable_income"));
                tax.setTaxCategoryId(resultSet.getInt("tax_cat_id"));
                tax.setTaxId(resultSet.getInt("tax_id"));

                return tax;
            }
        }
        return tax;
    }
}
