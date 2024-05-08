package com.jomariabejo.motorph.utility;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AutoIncrementUtility {


    public static int getNextAutoIncrementValue() {
        try {

            // Connect to the database
            try (Connection connection = DatabaseConnectionUtility.getConnection()) {
                // Create a statement
                try (Statement statement = connection.createStatement()) {
                    // Execute a query to get the maximum value of the auto-increment column
                    ResultSet resultSet = statement.executeQuery("SELECT MAX( employee_id ) FROM payroll_sys.employee");

                    // If there are no records in the table, return 1
                    if (!resultSet.next()) {
                        return 1;
                    }

                    // Get the maximum value and increment it by 1
                    int maxId = resultSet.getInt(1);
                    return maxId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 to indicate an error
        }
    }

    public static int getNextAutoIncrementValueForPayslip() {
        try {

            // Connect to the database
            try (Connection connection = DatabaseConnectionUtility.getConnection()) {
                // Create a statement
                try (Statement statement = connection.createStatement()) {
                    // Execute a query to get the maximum value of the auto-increment column
                    ResultSet resultSet = statement.executeQuery("SELECT MAX(payroll_sys.payslip.payslip_id) AS next_payslip_number FROM payroll_sys.payslip");

                    // If there are no records in the table, return 1
                    if (!resultSet.next()) {
                        return 1;
                    }

                    // Get the maximum value and increment it by 1
                    int maxId = resultSet.getInt(1);
                    return maxId + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 1; // Return -1 to indicate an error
        }
    }
}
