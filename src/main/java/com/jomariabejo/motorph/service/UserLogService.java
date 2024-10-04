package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.UserLog;
import com.jomariabejo.motorph.repository.UserLogRepository;

import java.util.List;

public class UserLogService {

    private final UserLogRepository userLogRepository;

    public UserLogService(UserLogRepository userLogRepository) {
        this.userLogRepository = userLogRepository;
    }

    public UserLog getUserLogById(Integer id) {
        return userLogRepository.findById(id);
    }

    public List<UserLog> getAllUserLog() {
        return userLogRepository.findAll();
    }

    public void saveUserLog(UserLog userLog) {
        userLogRepository.save(userLog);
    }

    public void updateUserLog(UserLog userLog) {
        userLogRepository.update(userLog);
    }

    public void deleteUserLog(UserLog userLog) {
        userLogRepository.delete(userLog);
    }
}
