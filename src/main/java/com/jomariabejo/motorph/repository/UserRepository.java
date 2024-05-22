package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.utility.TextReader;

import java.security.NoSuchAlgorithmException;
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

    public void updateUser(User user) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/update_user.sql");
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getEmployeeID());
            ps.setInt(4, user.getRoleID());
            ps.setInt(5, user.getUserID());
        }
    }

    public void deleteUser(String username) throws SQLException {
        String query = "DELETE FROM user WHERE username = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            int isDelete = ps.executeUpdate();

            if (isDelete == 0) {
                System.out.println("Nothing to delete");
            }
            else {
                System.out.println("Delete username: " + username + " success :)");
            }
        }
    }

    public Optional<User> getUserByUsernameANDPassword(String username, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE username=? AND password=?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement prepStatement = connection.prepareStatement(query)) {
            System.out.println("===================");
            System.out.println(username);
            System.out.println(password);
            System.out.println("===================");
            prepStatement.setString(1, username);
            prepStatement.setString(2, password);
            try (ResultSet resultSet = prepStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(
                            new User(
                                    resultSet.getInt("user_id"),
                                    resultSet.getInt("employee_id"),
                                    resultSet.getInt("role_id"),
                                    resultSet.getString("username"),
                                    resultSet.getString("password")
                            ));
                }
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

    public String getRoleOfTheUser(int userId) throws SQLException {
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
        }
        // If the user ID is not found or there's an error, return null
        return null;
    }

    public int getUserEmployeeId(int userID) throws SQLException {
        String query = TextReader.readTextFile(QUERY_BASE_PATH + "/get_user_employee_id.sql");

        try(Connection connection = DatabaseConnectionUtility.getConnection();
        PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1,userID);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                return result.getInt("employee_id");
            }
            else return 0;
        }
    }
}