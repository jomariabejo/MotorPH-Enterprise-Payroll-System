package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PayrollApproval;
import com.jomariabejo.motorph.repository.PayrollApprovalRepository;

import java.util.List;

public class PayrollApprovalService {

    private final PayrollApprovalRepository payrollApprovalRepository;

    public PayrollApprovalService(PayrollApprovalRepository payrollApprovalRepository) {
        this.payrollApprovalRepository = payrollApprovalRepository;
    }

    public PayrollApproval getPayrollApprovalById(Integer id) {
        return payrollApprovalRepository.findById(id);
    }

    public List<PayrollApproval> getAllPayrollApprovals() {
        return payrollApprovalRepository.findAll();
    }

    public void savePayrollApproval(PayrollApproval payrollApproval) {
        payrollApprovalRepository.save(payrollApproval);
    }

    public void updatePayrollApproval(PayrollApproval payrollApproval) {
        payrollApprovalRepository.update(payrollApproval);
    }

    public void deletePayrollApproval(PayrollApproval payrollApproval) {
        payrollApprovalRepository.delete(payrollApproval);
    }
}
