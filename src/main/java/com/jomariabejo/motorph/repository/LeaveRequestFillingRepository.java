package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class LeaveRequestFillingRepository {

    public static void main(String[] args) {
        // Example input values
        int userId = getUserInputId();
        int requestCategory = getRequestCategoryId();
        int leaveCredits = getLeaveCreditsId();
        String leaveStart = getLeaveStart();
        String leaveEnd = getLeaveEnd();
        int totalDays = calculateTotalDays(leaveStart, leaveEnd);
        String leaveReason = getLeaveReason();

        // SQL insert statement
        String sql = "INSERT INTO leave_request (user_id, request_category, leave_credits, leave_start, leave_end, total_days, leave_reason) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, requestCategory);
            pstmt.setInt(3, leaveCredits);
            pstmt.setDate(4, Date.valueOf(leaveStart));
            pstmt.setDate(5, Date.valueOf(leaveEnd));
            pstmt.setInt(6, totalDays);
            pstmt.setString(7, leaveReason);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new leave request was inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // These methods should be implemented to get the actual input values
    private static int getUserInputId() {
        // Implement this method to retrieve the user_id value
        return 0; // Placeholder
    }

    private static int getRequestCategoryId() {
        // Implement this method to retrieve the request_category value
        return 0; // Placeholder
    }

    private static int getLeaveCreditsId() {
        // Implement this method to retrieve the leave_credits value
        return 0; // Placeholder
    }

    private static String getLeaveStart() {
        // Implement this method to retrieve the leave_start date value
        return "YYYY-MM-DD"; // Placeholder
    }

    private static String getLeaveEnd() {
        // Implement this method to retrieve the leave_end date value
        return "YYYY-MM-DD"; // Placeholder
    }

    private static int calculateTotalDays(String leaveStart, String leaveEnd) {
        // Implement this method to calculate the total days of leave
        return 0; // Placeholder
    }

    private static String getLeaveReason() {
        // Implement this method to retrieve the leave_reason value
        return ""; // Placeholder
    }
}
