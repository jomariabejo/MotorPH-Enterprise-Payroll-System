package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollApproval;
import com.jomariabejo.motorph.repository.PayrollApprovalRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    public Optional<PayrollApproval> getApprovalByPayroll(Payroll payroll) {
        return payrollApprovalRepository.findByPayroll(payroll);
    }

    public List<PayrollApproval> getApprovalsByStatus(String status) {
        return payrollApprovalRepository.findByStatus(status);
    }

    public PayrollApproval createApprovalRequest(Payroll payroll, Integer approverId) {
        // Check if approval already exists for this payroll
        Optional<PayrollApproval> existing = getApprovalByPayroll(payroll);
        if (existing.isPresent()) {
            return existing.get();
        }

        PayrollApproval approval = new PayrollApproval();
        approval.setPayrollID(payroll);
        approval.setApproverID(approverId);
        approval.setStatus("Pending");
        approval.setApprovalDate(Instant.now()); // Set initial date, will be updated when approved/rejected
        
        savePayrollApproval(approval);
        return approval;
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
