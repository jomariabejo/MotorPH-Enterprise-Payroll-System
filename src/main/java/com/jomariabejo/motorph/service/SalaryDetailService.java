package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.record.Salary;
import com.jomariabejo.motorph.repository.SalaryRepository;

public class SalaryDetailService {
    private SalaryRepository salaryRepository;

    public SalaryDetailService() {
        this.salaryRepository = new SalaryRepository();
    }

    public Salary fetchSalaryDetails(int employeeId) {
        return salaryRepository.fetchSalaryDetailsByEmployeeId(employeeId);
    }
}
