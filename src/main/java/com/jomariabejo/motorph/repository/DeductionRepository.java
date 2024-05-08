package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;

public class DeductionRepository {
    public void saveDeduction(Deduction deduction) throws SQLException {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\deduction\\create_deduction.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, deduction.getEmployeeID());
            preparedStatement.setBigDecimal(2, deduction.getSss());
            preparedStatement.setBigDecimal(3, deduction.getPhilhealth());
            preparedStatement.setBigDecimal(4, deduction.getPagibig());
            preparedStatement.setBigDecimal(5, deduction.totalContributions());
            preparedStatement.setDate(6, deduction.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Insert deduction success");
            } else {
                System.out.println("Insert deduction failed");
            }
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
