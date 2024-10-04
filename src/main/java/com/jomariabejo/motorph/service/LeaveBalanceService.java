package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveBalance;
import com.jomariabejo.motorph.repository.LeaveBalanceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
    }

    public LeaveBalance getLeaveBalanceById(Integer id) {
        return leaveBalanceRepository.findById(id);
    }

    public List<LeaveBalance> getAllLeaveBalance() {
        return leaveBalanceRepository.findAll();
    }

    public void saveLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceRepository.save(leaveBalance);
    }

    public void updateLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceRepository.update(leaveBalance);
    }

    public void deleteLeaveBalance(LeaveBalance leaveBalance) {
        leaveBalanceRepository.delete(leaveBalance);
    }

    public Optional<Integer> fetchRemainingLeaveBalanceByLeaveTypeName(Employee employee, String leaveTypeName) {
        return leaveBalanceRepository.getEmployeeRemainingLeaveBalanceByLeaveType(employee, leaveTypeName);
    }

    public Optional<List<LeaveBalance>> fetchEmployeeRemainingLeaveBalances(Employee employee) {
        return leaveBalanceRepository.getEmployeeRemainingLeaveBalance(employee);
    }

}