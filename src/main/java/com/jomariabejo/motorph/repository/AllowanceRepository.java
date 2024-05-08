package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;

public class AllowanceRepository {
    public void createAllowanceRecord(Allowance allowance) throws SQLException {

        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\allowance\\create_allowance.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, allowance.getClothingAllowance());
            ps.setInt(2, allowance.getRiceAllowance());
            ps.setInt(3, allowance.getPhoneAllowance());
            ps.setInt(4, allowance.getTotalAmount());
            ps.setDate(5, allowance.getDateCreated());
            ps.setTimestamp(6, allowance.getDateModified());
            ps.setInt(7, allowance.getEmployeeID());
            ps.executeUpdate();
            System.out.println("Record Created");
        }
    }

    public void updateAllowance(Allowance allowance) throws SQLException {
        String query = "UPDATE allowance SET clothing=?, rice=?, phone=?, total_amount=?, dateModified=? WHERE alw_id=?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, allowance.getClothingAllowance());
            ps.setInt(2, allowance.getRiceAllowance());
            ps.setInt(3, allowance.getPhoneAllowance());
            ps.setInt(4, allowance.getTotalAmount());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis())); // Set current timestamp
            ps.setInt(6, allowance.getAllowanceID());

            ps.executeUpdate();
        }
    }

    public Allowance getAllowanceByEmployeeId(int employeeId) throws SQLException {
        String query = "SELECT * FROM allowance WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
        PreparedStatement ps = connection.prepareStatement(query)){
            ps.setInt(1, employeeId);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Allowance(
                        resultSet.getInt("alw_id"),
                        resultSet.getInt("employee_id"),
                        resultSet.getInt("clothing"),
                        resultSet.getInt("rice"),
                        resultSet.getInt("phone"),
                        resultSet.getInt("total_amount"),
                        resultSet.getDate("dateCreated"),
                        resultSet.getTimestamp("dateModified")
                );
            }
            else return null;
        }
    }

    public void deleteAllowanceByAllowanceID(int allowance_id) throws SQLException {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\allowance\\delete_department.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, allowance_id);
            ps.executeUpdate();
            System.out.println("Record Removed");
        }
    }
}