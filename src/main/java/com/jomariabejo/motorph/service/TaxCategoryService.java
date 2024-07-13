package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.utility.TextReader;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaxCategoryService {
    public int fetchTaxCategoryIdByMonthlyRate(BigDecimal monthlyRate) {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\tax_category\\get_tax_category_id_by_monthly_rate.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, monthlyRate);
            preparedStatement.setBigDecimal(2, monthlyRate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("tax_cat_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 1; // no tax category in default
    }
}
