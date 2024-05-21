package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public UserService() throws NoSuchAlgorithmException {
    }

    public String fetchEmployeeRoleName(int userId) throws SQLException {
        return userRepository.getRoleOfTheUser(userId);
    }

    public int fetchEmployeeIdByUserId(int userId) throws SQLException {
        return userRepository.getUserEmployeeId(userId);
    }
}
