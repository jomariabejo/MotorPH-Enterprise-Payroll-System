package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.User;
import com.jomariabejo.motorph.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService()  {
        this.userRepository = new UserRepository();
    }

    public String fetchEmployeeRoleName(int userId) {
        return userRepository.getRoleOfTheUser(userId);
    }

    public int fetchEmployeeIdByUserId(int userId) {
        return userRepository.getUserEmployeeId(userId);
    }

    public boolean saveUser(User user) {
        return userRepository.insertUser(user);
    }

    public User fetchUser(int userID) {
        return userRepository.fetchUser(userID);
    }

    public boolean deleteUser(int employeeId) {
        return userRepository.deleteUserByEmployeeId(employeeId);
    }

    public boolean modifyUser(User user) {
        return userRepository.modifyUser(user);
    }

    public boolean changePassword(int employeeId, String existingPassword, String newPassword) {
        return userRepository.changePassword(employeeId, existingPassword, newPassword);
    }

    public boolean changePassword(int userId, String newPassword) {
        return userRepository.changePassword(userId, newPassword);
    }

    public boolean saveVerificationCode(String username, int code) {
        return userRepository.saveVerificationCode(username, code);
    }

    public boolean isUsernameExist(String username) {
        return userRepository.isUsernameExist(username);
    }

    public int fetchUserIdByVerificationCode(int verificationCode) {
        return userRepository.fetchUserIdByVerificationCode(verificationCode);
    }

    public void resetVerificationCode(int userId) {
        userRepository.resetVerificationCode(userId);
    }
}
