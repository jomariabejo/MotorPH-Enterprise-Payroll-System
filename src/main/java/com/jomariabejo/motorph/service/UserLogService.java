package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.UserLog;
import com.jomariabejo.motorph.repository.UserLogRepository;

import java.time.LocalDateTime;
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

    /**
     * Get recent logs for real-time display.
     * 
     * @param limit Maximum number of logs to return
     * @return List of recent UserLogs
     */
    public List<UserLog> getRecentLogs(int limit) {
        return userLogRepository.findRecentLogs(limit);
    }

    /**
     * Get logs after a specific date/time for incremental updates.
     * 
     * @param dateTime The date/time threshold
     * @return List of UserLogs after the specified date/time
     */
    public List<UserLog> getLogsAfter(LocalDateTime dateTime) {
        return userLogRepository.findLogsAfter(dateTime);
    }
}
