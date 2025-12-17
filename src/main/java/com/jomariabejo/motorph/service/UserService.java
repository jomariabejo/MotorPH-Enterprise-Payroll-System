package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.UserRepository;
import com.jomariabejo.motorph.repository.UserRoleRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRoleService = new UserRoleService(new UserRoleRepository());
    }

    public UserService(UserRepository userRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public Optional<User> fetchUser(String username, String password) {
        Optional<User> userOpt = userRepository.findUserByUsernameAndPassword(username, password);
        userOpt.ifPresent(u -> {
            if (u.getId() != null && u.getRoleID() != null && u.getRoleID().getId() != null) {
                // Ensure join table contains the primary/default role assignment
                userRoleService.ensureUserHasRole(u.getId(), u.getRoleID().getId());
            }
        });
        return userOpt;
    }

    public Optional<User> fetchEmailByEmployeeId(Employee employeeID) {
        return userRepository.findEmailByEmployeeId(employeeID);
    }
}
