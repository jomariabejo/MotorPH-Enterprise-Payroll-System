package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.utility.DateUtility;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;

public class AllowanceRepository {
    public void createAllowanceRecord(Allowance allowance) {
        System.out.println("ALLOWANCE INSERTING : " + allowance.toString());
        ;
        String query = "INSERT INTO ALLOWANCE( clothing, rice, phone, total_amount, dateCreated, dateModified, employee_id) VALUES ( ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, allowance.getClothingAllowance());
            ps.setInt(2, allowance.getRiceAllowance());
            ps.setInt(3, allowance.getPhoneAllowance());
            ps.setInt(4, allowance.getTotalAmount());
            ps.setDate(5, allowance.getDateCreated());
            ps.setTimestamp(6, allowance.getDateModified());
            ps.setInt(7, allowance.getEmployeeID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAllowance(Allowance allowance) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAllowance(Allowance allowance, int employee_id) {
        String query = "UPDATE allowance SET clothing=?, rice=?, phone=?, total_amount=?, dateModified=? WHERE employee_id=?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, allowance.getClothingAllowance());
            ps.setInt(2, allowance.getRiceAllowance());
            ps.setInt(3, allowance.getPhoneAllowance());
            ps.setInt(4, allowance.getTotalAmount());
            ps.setTimestamp(5, allowance.getDateModified());
            ps.setInt(6, employee_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Allowance getAllowanceByEmployeeId(int employeeId) {
        String query = "SELECT * FROM allowance WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
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
            } else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getAllowanceIdByEmployeeId(int employeeId) {
        String query = "SELECT alw_id FROM allowance WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("alw_id"); // allowance id
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}