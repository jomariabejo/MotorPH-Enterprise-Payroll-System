package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PayrollChange;
import com.jomariabejo.motorph.repository.PayrollChangeRepository;

import java.util.List;

public class PayrollChangeService {

    private final PayrollChangeRepository payrollChangeRepository;

    public PayrollChangeService(PayrollChangeRepository payrollChangeRepository) {
        this.payrollChangeRepository = payrollChangeRepository;
    }

    public PayrollChange getPayrollChangeById(Integer id) {
        return payrollChangeRepository.findById(id);
    }

    public List<PayrollChange> getAllPayrollChanges() {
        return payrollChangeRepository.findAll();
    }

    public void savePayrollChange(PayrollChange payrollChange) {
        payrollChangeRepository.save(payrollChange);
    }

    public void updatePayrollChange(PayrollChange payrollChange) {
        payrollChangeRepository.update(payrollChange);
    }

    public void deletePayrollChange(PayrollChange payrollChange) {
        payrollChangeRepository.delete(payrollChange);
    }
}
