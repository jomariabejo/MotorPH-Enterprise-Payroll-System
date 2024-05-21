package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.repository.PayslipRepository;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;
import javafx.collections.ObservableList;

public class PayslipService {
    private final PayslipRepository payslipRepository;

    public PayslipService() {
        this.payslipRepository = new PayslipRepository();
    }

    public int getNextPrimaryKeyValue() {
        return AutoIncrementUtility.getNextAutoIncrementValueForPayslip();
    }

    public void saveGeneratedPayslip(ObservableList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        payslipRepository.saveMultiplePayslip(employeePayrollSummaryReports);
    }
}
