package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.repository.AllowanceRepository;

import java.sql.SQLException;

public class AllowanceService {
    AllowanceRepository allowanceRepository = new AllowanceRepository();
    public void createAllowance(Allowance allowance) throws SQLException {
        allowanceRepository.createAllowanceRecord(allowance);
    }

    public Allowance getAllowanceByEmployeeId(int employeeId) {
        return allowanceRepository.getAllowanceByEmployeeId(employeeId);
    }

    public int getAllowanceIdByEmployeeId(int employeeId) {
        return allowanceRepository.getAllowanceIdByEmployeeId(employeeId);
    }

    public void updateAllowance(Allowance allowance) {
        allowanceRepository.updateAllowance(allowance);
    }

    public void updateAllowance(Allowance allowance, int employeeId) {
        allowanceRepository.updateAllowance(allowance, employeeId);
    }

    public void deleteAllowance() {

    }
}
