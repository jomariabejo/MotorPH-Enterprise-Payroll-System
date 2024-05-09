package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.repository.PayslipRepository;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;

import java.util.ArrayList;

public class PayslipService {
    private PayslipRepository payslipRepository;

    public PayslipService() {
        this.payslipRepository = payslipRepository;
    }

    public int getNextPrimaryKeyValue() {
        return AutoIncrementUtility.getNextAutoIncrementValueForPayslip();
    }

    public void saveGeneratedPayslip(ArrayList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        payslipRepository.saveMultiplePayslip(employeePayrollSummaryReports);
    }
}
