package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.TextReader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class UserRepository {
    public final String QUERY_BASE_PATH = "src/main/java/com/jomariabejo/motorph/query/user";

    public UserRepository() {
    }

    public void createUser(User user) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/create_user.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, user.getUsername());
            prepStatement.setString(2, user.getPassword());
            prepStatement.setInt(3, user.getEmployeeID());
            prepStatement.setInt(4, user.getRoleID());
            prepStatement.executeUpdate();
            System.out.println("User record created...");
        }
    }


    public void deleteUserByEmployeeId(String username) throws SQLException {
        String query = "DELETE FROM user WHERE username = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            int isDelete = ps.executeUpdate();

            if (isDelete == 0) {
                System.out.println("Nothing to delete");
            } else {
                System.out.println("Delete username: " + username + " success :)");
            }
        }
    }

    public boolean deleteUserByEmployeeId(int employeeId) {
        String query = "DELETE FROM user WHERE user.employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, employeeId);
            int isDelete = ps.executeUpdate();
            return isDelete == 1; // One means user deleted successfully
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> getUserByUsernameANDPassword(String username, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE username=? AND password=?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) return Optional.of(
                        new User(
                                resultSet.getInt("user_id"),
                                resultSet.getInt("employee_id"),
                                resultSet.getInt("role_id"),
                                resultSet.getString("username"),
                                resultSet.getString("password")
                        ));
            }
        }
        return Optional.empty();
    }


    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> userList = new ArrayList<User>();
        String query = "SELECT * FROM user";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(
                            new User(
                                    resultSet.getInt("user_id"),
                                    resultSet.getInt("employee_id"),
                                    resultSet.getInt("role_id"),
                                    resultSet.getString("username"),
                                    resultSet.getString("password")
                            )
                    );
                }
            }
        }
        return userList;
    }

    public String getRoleOfTheUser(int userId) {
        // Read the SQL query from file
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\user\\get_user_full_info.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            // Set the user ID parameter in the prepared statement
            ps.setInt(1, userId);

            // Execute the query
            ResultSet rs = ps.executeQuery();

            // If the query returns a result, retrieve the role of the user
            if (rs.next()) {
                // Return the role of the user
                return rs.getString("name");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // If the user ID is not found or there's an error, return null
        return null;
    }

    public int getUserEmployeeId(int userID) {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_user_employee_id.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                return result.getInt("employee_id");
            } else return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean insertUser(User user) {
        String query = "INSERT INTO USER(username, password, employee_id, role_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getEmployeeID());
            ps.setInt(4, user.getRoleID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                AlertUtility.showErrorAlert("Duplicate Entry", "Employee has active user account", "Employee have already user account ");
            } else {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public User fetchUser(int userID) {
        String query = "SELECT u.employee_id, u.role_id, u.username, u.password, u.user_id FROM user u WHERE u.user_id = ?;";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, userID);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt(5), // user id
                        resultSet.getInt(1), // employee id
                        resultSet.getInt(2), // role id
                        resultSet.getString(3), // username
                        resultSet.getString(4)); // password
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean modifyUser(User user) {
        String query = "UPDATE user SET username = ?, PASSWORD = ?, employee_id = ?, role_id = ? WHERE user_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getEmployeeID());
            pstmt.setInt(4, user.getRoleID());
            pstmt.setInt(5, user.getUserID());

            return pstmt.executeUpdate() == 1; // 1 means updated successfully

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public boolean changePassword(int employeeId, String existingPassword, String newPassword) {
        String query = "UPDATE user SET PASSWORD = ? WHERE user.employee_id = ? AND user.password = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, employeeId);
            pstmt.setString(3, existingPassword);

            return pstmt.executeUpdate() == 1; // 1 means updated successfully

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveVerificationCode(String username, int code) {
        String query = "UPDATE USER SET verification_code = ? WHERE username = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(2, username);
            pstmt.setInt(1, code);

            int rowAffected = pstmt.executeUpdate();

            return rowAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUsernameExist(String username) {
        String query = "SELECT COUNT(USER.username) FROM USER WHERE username = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}