package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.LeaveRequest;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class LeaveRequestService {

    private LeaveRequestRepository leaveRequestRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public void createLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.createLeaveRequest(leaveRequest);
    }

    public LeaveRequest getLeaveRequestById(int leaveRequestId) {
        return leaveRequestRepository.getLeaveRequestById(leaveRequestId);
    }

    public void updateLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequestRepository.updateLeaveRequest(leaveRequest);
    }

    public void updateLeaveRequestStatus(int leaveRequestId, LeaveRequest.LeaveRequestStatus leaveRequestStatus) {
        leaveRequestRepository.updateLeaveRequestStatus(leaveRequestId, leaveRequestStatus);
    }

    public void deleteLeaveRequest(int leaveRequestId) {
        leaveRequestRepository.deleteLeaveRequest(leaveRequestId);
    }

    public ArrayList<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.getAllLeaveRequests();
    }

    public int countAllLeaveRequests() throws SQLException {
        return leaveRequestRepository.countLeaveRequests();
    }

    public double calculateRemainingLeaveBalance(int employeeId, int categoryId) {
        return leaveRequestRepository.calculateRemainingLeaveBalance(employeeId, categoryId);
    }

    public int countEmployeeLeaveRequest(int employeeId) throws SQLException {
        return leaveRequestRepository.countEmployeeLeaveRequest(employeeId);
    }

    public boolean checkIfEmployeeHasLeaveRequestRecords(int employeeId, String status) {
        return leaveRequestRepository.checkIfEmployeeHasLeaveRequestRecords(employeeId,status);
    }


    public ArrayList<LeaveRequest> fetchLeaveRequestForPage(int pageIndex, int rowsPerPage, String status) throws SQLException {
        return leaveRequestRepository.fetchLeaveRequestForPage(pageIndex, rowsPerPage, status);
    }

    public ArrayList<LeaveRequest> fetchLeaveRequestForPage(int pageIndex, int rowsPerPage, String status, int employeeId) throws SQLException {
        return leaveRequestRepository.fetchLeaveRequestForPage(pageIndex, rowsPerPage, status, employeeId);
    }

    public int getLeaveRequestsPageCount() throws SQLException {
        return leaveRequestRepository.countLeaveRequestsPageCount();
    }

    public int countEmployeeLeaveRequests(int employeeId) throws SQLException {
        return leaveRequestRepository.countEmployeeLeaveRequests(employeeId);
    }

    public int countLeaveRequestPage() throws SQLException {
        return leaveRequestRepository.countLeaveRequestsPageCount();
    }

    public int countLeaveRequestPage(String status) throws SQLException {
        return leaveRequestRepository.countLeaveRequestsPageCount(status);
    }
    public int countLeaveRequestPage(int employeeId, String status) throws SQLException {
        return leaveRequestRepository.countLeaveRequestsPageCount(employeeId, status);
    }

    public String getLeaveRequestMessage(int employeeId) {
        return leaveRequestRepository.getLeaveRequestMessage(employeeId);
    }
}
