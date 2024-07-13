package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.LeaveRequest;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.*;
import java.util.ArrayList;

public class LeaveRequestRepository {

    public void createLeaveRequest(LeaveRequest leaveRequest) {
        String query = "INSERT INTO leave_request (employee_id, leave_request_category_id, start_date, end_date, date_created, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, leaveRequest.getEmployeeID());
            ps.setInt(2, leaveRequest.getLeaveRequestCategoryId());
            ps.setDate(3, leaveRequest.getStartDate());
            ps.setDate(4, leaveRequest.getEndDate());
            ps.setTimestamp(5, leaveRequest.getDateCreated());
            ps.setString(6, leaveRequest.getReason());
            ps.setString(7, leaveRequest.getStatus().toString());

            ps.executeUpdate();
            System.out.println("Leave Request Created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LeaveRequest getLeaveRequestById(int leaveRequestId) {
        String query = "SELECT * FROM leave_request WHERE leave_request_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, leaveRequestId);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new LeaveRequest(
                            resultSet.getInt("leave_request_id"),
                            resultSet.getInt("employee_id"),
                            resultSet.getInt("leave_request_category_id"),
                            resultSet.getDate("start_date"),
                            resultSet.getDate("end_date"),
                            resultSet.getTimestamp("date_created"),
                            resultSet.getString("reason"),
                            LeaveRequest.LeaveRequestStatus.valueOf(resultSet.getString("status"))
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateLeaveRequest(LeaveRequest leaveRequest) {
        String query = "UPDATE leave_request SET employee_id=?, leave_request_category_id=?, start_date=?, end_date=?, date_created=?, reason=?, status=? WHERE leave_request_id=?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, leaveRequest.getEmployeeID());
            ps.setInt(2, leaveRequest.getLeaveRequestCategoryId());
            ps.setDate(3, leaveRequest.getStartDate());
            ps.setDate(4, leaveRequest.getEndDate());
            ps.setTimestamp(5, leaveRequest.getDateCreated());
            ps.setString(6, leaveRequest.getReason());
            ps.setString(7, leaveRequest.getStatus().toString());
            ps.setInt(8, leaveRequest.getLeaveRequestID());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave Request Updated");
            } else {
                System.out.println("Leave Request not Updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLeaveRequestStatus(int leaveRequestId, LeaveRequest.LeaveRequestStatus leaveRequestStatus) {
        String query = "UPDATE leave_request SET status=? WHERE leave_request_id=?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, leaveRequestStatus.toString());
            ps.setInt(2, leaveRequestId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave Request Updated");
            } else {
                System.out.println("Leave Request not Updated");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteLeaveRequest(int leaveRequestId) {
        String query = "DELETE FROM leave_request WHERE leave_request_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, leaveRequestId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Leave Request Removed");
            } else {
                System.out.println("Leave Request not Removed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<LeaveRequest> getAllLeaveRequests() {
        String query = "SELECT * FROM leave_request";
        ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query); ResultSet resultSet = ps.executeQuery()) {
            while (resultSet.next()) {
                LeaveRequest leaveRequest = new LeaveRequest(
                        resultSet.getInt("leave_request_id"),
                        resultSet.getInt("employee_id"),
                        resultSet.getInt("leave_request_category_id"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getTimestamp("date_created"),
                        resultSet.getString("reason"),
                        LeaveRequest.LeaveRequestStatus.valueOf(resultSet.getString("status"))
                );
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return leaveRequests;
    }


    public double calculateRemainingLeaveBalance(int employeeId, int categoryId) {
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\leave_request\\calculate_remaining_leave_balance.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, categoryId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("remaining_leave_balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0; // Return 0 if there is any error or no result found
    }

//    public String switchCategoryIdToName(int categoryId) {
//        String categoryName = "";
//        switch (categoryId) {
//            case 1:
//                categoryName= "Sick";
//                break;
//            case 2:
//                categoryName = "Emergency";
//                break;
//            case 3:
//                categoryName = "Vacation";
//                break;
//        }
//        return categoryName;
//    }

    public int countEmployeeLeaveRequest(int employeeId) throws SQLException {
        String query = "SELECT COUNT(*) FROM LEAVE_REQUEST WHERE EMPLOYEE_ID = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public boolean checkIfEmployeeHasLeaveRequestRecords(int employeeId, String status) {
        String query = "SELECT COUNT(*) FROM LEAVE_REQUEST WHERE employee_id = ? && status = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ps.setString(2, status);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            // Handle any potential exceptions, log or rethrow if necessary
            e.printStackTrace();
        }

        // Default return false if there's an exception or no records found
        return false;
    }

    public ArrayList<LeaveRequest> fetchLeaveRequestForPage(int pageIndex, int rowsPerPage, String leaveRequestStatus) throws SQLException {
        ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT * FROM LEAVE_REQUEST WHERE status = ? ORDER BY DATE_CREATED LIMIT ? OFFSET ? ";

        int offset = pageIndex * rowsPerPage;

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, leaveRequestStatus);
            ps.setInt(2, rowsPerPage);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(new LeaveRequest(
                            rs.getInt("leave_request_id"),
                            rs.getInt("employee_id"),
                            rs.getInt("leave_request_category_id"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getTimestamp("date_created"),
                            rs.getString("reason"),
                            LeaveRequest.LeaveRequestStatus.valueOf(rs.getString("status"))
                    ));
                }
            }
        }
        return leaveRequests;
    }

    public ArrayList<LeaveRequest> fetchLeaveRequestForPage(int pageIndex, int rowsPerPage, String leaveRequestStatus, int employeeId) throws SQLException {
        ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT * FROM LEAVE_REQUEST WHERE status = ? && employee_id = ? ORDER BY DATE_CREATED LIMIT ? OFFSET ? ";

        int offset = pageIndex * rowsPerPage;

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, leaveRequestStatus);
            ps.setInt(2, employeeId);
            ps.setInt(3, rowsPerPage);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(new LeaveRequest(
                            rs.getInt("leave_request_id"),
                            rs.getInt("employee_id"),
                            rs.getInt("leave_request_category_id"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getTimestamp("date_created"),
                            rs.getString("reason"),
                            LeaveRequest.LeaveRequestStatus.valueOf(rs.getString("status"))
                    ));
                }
            }
        }
        return leaveRequests;
    }


    public int fetchLeaveRequestForPage(int employeeId, int pageIndex, int rowsPerPage) throws SQLException {
        ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
        String query = "SELECT * FROM LEAVE_REQUEST WHERE employee_id = ? ORDER BY DATE_CREATED LIMIT ? OFFSET ?";

        int offset = pageIndex * rowsPerPage;

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, rowsPerPage);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaveRequests.add(new LeaveRequest(
                            rs.getInt("leave_request_id"),
                            rs.getInt("employee_id"),
                            rs.getInt("leave_request_category_id"),
                            rs.getDate("start_date"),
                            rs.getDate("end_date"),
                            rs.getTimestamp("date_created"),
                            rs.getString("reason"),
                            LeaveRequest.LeaveRequestStatus.valueOf(rs.getString("status"))
                    ));
                }
            }
        }
        return leaveRequests.size();
    }

    public int countLeaveRequestsPageCount() throws SQLException {
        int pageCount = 0;
        String query = "SELECT COUNT(*) AS LEAVE_REQUESTS_COUNT FROM LEAVE_REQUEST";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int totalCount = rs.getInt("LEAVE_REQUESTS_COUNT");
                    pageCount = (int) Math.ceil((double) totalCount / 100);
                }
            }
        }
        return pageCount;
    }

    public int countLeaveRequestsPageCount(String status) throws SQLException {
        int pageCount = 0;
        String query = "SELECT COUNT(*) AS LEAVE_REQUESTS_COUNT FROM LEAVE_REQUEST WHERE STATUS = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int totalCount = rs.getInt("LEAVE_REQUESTS_COUNT");
                    pageCount = (int) Math.ceil((double) totalCount / 100);
                }
            }
        }
        return pageCount;
    }

    public int countLeaveRequestsPageCount(int employeeId, String status) throws SQLException {
        int pageCount = 0;
        String query = "SELECT COUNT(*) AS LEAVE_REQUESTS_COUNT FROM LEAVE_REQUEST WHERE STATUS = ? AND EMPLOYEE_ID = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setInt(2, employeeId);


            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int totalCount = rs.getInt("LEAVE_REQUESTS_COUNT");
                    pageCount = (int) Math.ceil((double) totalCount / 100);
                }
            }
        }
        return pageCount;
    }

    public int countEmployeeLeaveRequests(int employeeId) throws SQLException {
        String query = "SELECT COUNT(*) AS NumberOfLeaveRequests FROM LEAVE_REQUEST WHERE employee_id = ?";
        int numberOfLeaveRequests = 0;

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    numberOfLeaveRequests = rs.getInt("NumberOfLeaveRequests");
                }
            }
        }
        return numberOfLeaveRequests;
    }

    public int countLeaveRequests() throws SQLException {
        String query = "SELECT COUNT(*) AS NumberOfLeaveRequests FROM LEAVE_REQUEST";
        int numberOfLeaveRequests = 0;

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    numberOfLeaveRequests = rs.getInt("NumberOfLeaveRequests");
                }
            }
        }
        return numberOfLeaveRequests;
    }

    public String getLeaveRequestMessage(int leaveRequestId) {
        String query = "SELECT REASON FROM LEAVE_REQUEST WHERE LEAVE_REQUEST_ID = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, leaveRequestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reason");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
