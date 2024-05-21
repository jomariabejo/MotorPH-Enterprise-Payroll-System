package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.repository.LeaveRequestCategoryRepository;

import java.sql.SQLException;

public class LeaveRequestCategoryService {
    private final LeaveRequestCategoryRepository leaveRequestCategoryRepository;

    public LeaveRequestCategoryService() {
        this.leaveRequestCategoryRepository = new LeaveRequestCategoryRepository();
    }

    public String fetchLeaveRequestCategoryName(int leaveRequestCategoryId) throws SQLException {
        return leaveRequestCategoryRepository.fetchLeaveRequestCategoryName(leaveRequestCategoryId);
    }
}
