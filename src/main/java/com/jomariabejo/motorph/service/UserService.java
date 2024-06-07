package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.repository.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public UserService()  {
    }

    public String fetchEmployeeRoleName(int userId) throws SQLException {
        return userRepository.getRoleOfTheUser(userId);
    }

    public int fetchEmployeeIdByUserId(int userId) throws SQLException {
        return userRepository.getUserEmployeeId(userId);
    }

    public boolean saveUser(User user) {
        return userRepository.insertUser(user);
    }

    public User fetchUser(int userID) {
        return userRepository.fetchUser(userID);
    }

    public boolean deleteUser(int userId) {
        return userRepository.deleteUser(userId);
    }
}
