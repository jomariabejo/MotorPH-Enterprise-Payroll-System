package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.findUser(username,password);
    }
}
