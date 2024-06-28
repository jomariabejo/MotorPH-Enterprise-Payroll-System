package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.repository.AllowanceRepository;

import java.sql.SQLException;

public class AllowanceService {
    AllowanceRepository allowanceRepository = new AllowanceRepository();
    public void createAllowance(Allowance allowance) throws SQLException {
        allowanceRepository.createAllowanceRecord(allowance);
    }

    public Allowance getAllowanceByEmployeeId(int employeeId) throws SQLException {
        return allowanceRepository.getAllowanceByEmployeeId(employeeId);
    }

    public int getAllowanceIdByEmployeeId(int employeeId) throws SQLException {
        return allowanceRepository.getAllowanceIdByEmployeeId(employeeId);
    }

    public void updateAllowance(Allowance allowance) throws SQLException {
        allowanceRepository.updateAllowance(allowance);
    }
    public void deleteAllowance() {

    }
}
