package com.jomariabejo.motorph.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        // Attempt to establish a database connection
        try (Connection connection = DatabaseConnectionUtility.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database.");

                // Example query
                String query = "SELECT * FROM employee";

                // Prepare and execute the query
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    // Process the query result
                    while (resultSet.next()) {
                        // Example: Print the contents of each row
//                        Employee employee = new Employee(
//                                Integer.valueOf(resultSet.getString("employee_id")),
//                                String.valueOf(resultSet.getString("firstname")),
//                                String.valueOf(resultSet.getString("lastname")),
//                                Date.valueOf(resultSet.getString("birthday")),
//                                resultSet.getString("address"),
//                                EmployeeStatus.valueOf(resultSet.getString("status"))
//                        );

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
