package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;

public class DeductionRepository {
    public void saveDeduction(Deduction deduction) {
        String query = "";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, deduction.getDeductionID());
            preparedStatement.setInt(2, deduction.getEmployeeID());
            preparedStatement.setBigDecimal(3, deduction.getSss());
            preparedStatement.setBigDecimal(4, deduction.getPhilhealth());
            preparedStatement.setBigDecimal(5, deduction.getPagibig());
            preparedStatement.setBigDecimal(6, deduction.totalContributions());
            preparedStatement.setDate(7, deduction.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insert deduction success");
            } else {
                System.out.println("Insert deduction failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Deduction fetchDeductionByEmployeeIdAndDate(Date date, int employeeId) throws SQLException {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\deduction\\get_deduction.sql");
        Deduction deduction = new Deduction();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1,date);
            preparedStatement.setInt(2,employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                deduction.setSss(resultSet.getBigDecimal("sss"));
                deduction.setPhilhealth(resultSet.getBigDecimal("philhealth"));
                deduction.setPagibig(resultSet.getBigDecimal("pagbig"));
                deduction.setDateCreated(resultSet.getDate("date_created"));
               return deduction;
            }
        }
        return deduction;
    }
}
