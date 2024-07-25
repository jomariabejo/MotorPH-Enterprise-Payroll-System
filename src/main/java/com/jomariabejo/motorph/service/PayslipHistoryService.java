package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PayslipHistory;
import com.jomariabejo.motorph.repository.PayslipHistoryRepository;

import java.util.List;

public class PayslipHistoryService {

    private final PayslipHistoryRepository payslipHistoryRepository;

    public PayslipHistoryService(PayslipHistoryRepository payslipHistoryRepository) {
        this.payslipHistoryRepository = payslipHistoryRepository;
    }

    public PayslipHistory getPayslipHistoryById(Integer id) {
        return payslipHistoryRepository.findById(id);
    }

    public List<PayslipHistory> getAllPayslipHistory() {
        return payslipHistoryRepository.findAll();
    }

    public void savePayslipHistory(PayslipHistory payslipHistory) {
        payslipHistoryRepository.save(payslipHistory);
    }

    public void updatePayslipHistory(PayslipHistory payslipHistory) {
        payslipHistoryRepository.update(payslipHistory);
    }

    public void deletePayslipHistory(PayslipHistory payslipHistory) {
        payslipHistoryRepository.delete(payslipHistory);
    }
}
