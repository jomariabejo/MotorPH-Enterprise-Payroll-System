package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public boolean isEmployeeHasOverlapLeaveDates(Integer employeeId, LocalDate leaveFrom, LocalDate leaveTo) {
        return leaveRequestRepository.isEmployeeHasOverlapLeaveDates(employeeId,leaveFrom, leaveTo);
    }

    public List<LeaveRequest> fetchLeaveRequestsForEmployee(Employee employee, String month, String year, String status, String leaveType) {
        return leaveRequestRepository.fetchLeaveRequestsForEmployee(employee,month,Integer.valueOf(year),status,leaveType);
    }

    public Optional<List<Integer>> getYearsOfLeaveRequestOfEmployee(Employee employee) {
        return leaveRequestRepository.getYearsOfLeaveRequestByEmployeeId(employee);
    }
}
