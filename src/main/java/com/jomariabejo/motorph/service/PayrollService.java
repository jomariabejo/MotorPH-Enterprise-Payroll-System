package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.repository.PayrollRepository;

import java.util.List;

public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public Payroll getPayrollById(Integer id) {
        return payrollRepository.findById(id);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    public void savePayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }

    public void updatePayroll(Payroll payroll) {
        payrollRepository.update(payroll);
    }

    public void deletePayroll(Payroll payroll) {
        payrollRepository.delete(payroll);
    }
}
