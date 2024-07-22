package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;

import java.util.List;

public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public LeaveRequest getLeaveRequestById(Integer id) {
        return leaveRequestRepository.findById(id);
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public void saveLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    public void updateLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.update(leaveRequest);
    }

    public void deleteLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.delete(leaveRequest);
    }
}
