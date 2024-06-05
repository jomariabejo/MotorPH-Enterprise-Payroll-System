package com.jomariabejo.motorph.repository.RoleRepository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRepository {

    public int fetchRoleId(Role role) {
        String query = "SELECT role_id FROM role WHERE role.name = ?";

        try(Connection connection = DatabaseConnectionUtility.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, role.getName());

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // role name not found
    }
}
