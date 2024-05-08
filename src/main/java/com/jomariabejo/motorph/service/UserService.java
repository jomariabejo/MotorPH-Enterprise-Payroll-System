package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.repository.UserRepository;

import java.sql.SQLException;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    public String fetchEmployeeRoleName(int userId) throws SQLException {
        return userRepository.getRoleOfTheUser(userId);
    }

    public int fetchEmployeeIdByUserId(int userId) throws SQLException {
        return userRepository.getUserEmployeeId(userId);
    }
}
