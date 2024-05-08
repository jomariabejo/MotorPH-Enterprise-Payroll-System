package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.repository.LeaveRequestCategoryRepository;

import java.sql.SQLException;

public class LeaveRequestCategoryService {
    private LeaveRequestCategoryRepository leaveRequestCategoryRepository;

    public LeaveRequestCategoryService(LeaveRequestCategoryRepository leaveRequestCategoryRepository) {
        this.leaveRequestCategoryRepository = leaveRequestCategoryRepository;
    }

    public String fetchLeaveRequestCategoryName(int leaveRequestCategoryId) throws SQLException {
        return leaveRequestCategoryRepository.fetchLeaveRequestCategoryName(leaveRequestCategoryId);
    }
}
