package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LeaveRequestCategoryRepository {

    public String fetchLeaveRequestCategoryName(int leaveRequestCategoryId) throws SQLException {
        String query = "SELECT CATEGORYNAME FROM LEAVE_REQUEST_CATEGORY WHERE LEAVE_REQ_CAT_ID = ?";
        String categoryName = "";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, leaveRequestCategoryId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoryName = rs.getString("categoryName");
            }
        }
        return categoryName;
    }
}
