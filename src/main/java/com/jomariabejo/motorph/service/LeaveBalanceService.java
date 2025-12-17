package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveBalance;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.repository.LeaveBalanceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestTypeService leaveRequestTypeService;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository, LeaveRequestTypeService leaveRequestTypeService) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveRequestTypeService = leaveRequestTypeService;
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

    /**
     * Initialize leave balances for a new employee with default yearly allocations.
     * Default allocations:
     * - Sick Leave: 5 days
     * - Vacation Leave: 10 days
     * - Emergency Leave: 5 days
     * 
     * @param employee The employee to initialize leave balances for
     */
    public void initializeLeaveBalancesForNewEmployee(Employee employee) {
        // Default yearly leave allocations
        Map<String, Integer> defaultAllocations = new HashMap<>();
        defaultAllocations.put("Sick", 5);
        defaultAllocations.put("Vacation", 10);
        defaultAllocations.put("Emergency", 5);

        // Get all leave types
        List<LeaveRequestType> leaveTypes = leaveRequestTypeService.getAllLeaveRequestTypes();

        // Create leave balance for each leave type
        for (LeaveRequestType leaveType : leaveTypes) {
            String leaveTypeName = leaveType.getLeaveTypeName();
            
            // Check if we have a default allocation for this leave type
            Integer allocation = defaultAllocations.getOrDefault(leaveTypeName, leaveType.getMaxCredits());
            
            // If no default and no maxCredits, use 0
            if (allocation == null) {
                allocation = leaveType.getMaxCredits() != null ? leaveType.getMaxCredits() : 0;
            }

            // Check if leave balance already exists for this employee and leave type
            Optional<Integer> existingBalance = fetchRemainingLeaveBalanceByLeaveTypeName(employee, leaveTypeName);
            
            if (existingBalance.isEmpty()) {
                // Create new leave balance
                LeaveBalance leaveBalance = new LeaveBalance();
                leaveBalance.setEmployee(employee);
                leaveBalance.setLeaveTypeID(leaveType);
                leaveBalance.setBalance(allocation);
                
                saveLeaveBalance(leaveBalance);
            }
        }
    }

    /**
     * Reset leave balances for an employee to their yearly maximum allocations.
     * This should be called at the beginning of each year.
     * 
     * @param employee The employee whose leave balances should be reset
     */
    public void resetLeaveBalancesForNewYear(Employee employee) {
        List<LeaveRequestType> leaveTypes = leaveRequestTypeService.getAllLeaveRequestTypes();
        Optional<List<LeaveBalance>> existingBalancesOpt = fetchEmployeeRemainingLeaveBalances(employee);

        for (LeaveRequestType leaveType : leaveTypes) {
            Integer maxCredits = leaveType.getMaxCredits() != null ? leaveType.getMaxCredits() : 0;
            boolean balanceFound = false;

            // Check if balance exists and update it
            if (existingBalancesOpt.isPresent()) {
                for (LeaveBalance balance : existingBalancesOpt.get()) {
                    if (balance.getLeaveTypeID().getId().equals(leaveType.getId())) {
                        balance.setBalance(maxCredits);
                        updateLeaveBalance(balance);
                        balanceFound = true;
                        break;
                    }
                }
            }

            // Create new balance if it doesn't exist
            if (!balanceFound) {
                LeaveBalance leaveBalance = new LeaveBalance();
                leaveBalance.setEmployee(employee);
                leaveBalance.setLeaveTypeID(leaveType);
                leaveBalance.setBalance(maxCredits);
                saveLeaveBalance(leaveBalance);
            }
        }
    }

    /**
     * Initialize leave balances for all existing employees who don't have them.
     * Useful for migrating existing employees or fixing missing data.
     */
    public void initializeLeaveBalancesForAllEmployees(List<Employee> employees) {
        for (Employee employee : employees) {
            Optional<List<LeaveBalance>> existingBalances = fetchEmployeeRemainingLeaveBalances(employee);
            if (existingBalances.isEmpty() || existingBalances.get().isEmpty()) {
                initializeLeaveBalancesForNewEmployee(employee);
            }
        }
    }

}