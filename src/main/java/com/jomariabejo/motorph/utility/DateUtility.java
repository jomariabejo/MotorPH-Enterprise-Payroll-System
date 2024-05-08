package com.jomariabejo.motorph.utility;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;

import java.sql.*;
import java.time.LocalDate;

public class DateUtility {


    public static LocalDate getLocalDate() {
        try {
            // Connect to the database
            try (Connection connection = DatabaseConnectionUtility.getConnection()) {
                // Create a statement
                try (Statement statement = connection.createStatement()) {
                    // Execute a query to get the current date
                    ResultSet resultSet = statement.executeQuery("SELECT CURDATE()");

                    // Move to the first row of the result set
                    resultSet.first();

                    // Get the current date from the result set
                    return resultSet.getDate(1).toLocalDate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDate() {
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT CURDATE() AS current_date");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDate("current_date");
            } else {
                throw new SQLException("Failed to retrieve current date from the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle exception according to your application's logic
        }
    }
}
