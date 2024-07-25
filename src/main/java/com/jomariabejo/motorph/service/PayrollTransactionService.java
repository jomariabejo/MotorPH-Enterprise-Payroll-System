package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.PayrollTransaction;
import com.jomariabejo.motorph.repository.PayrollTransactionRepository;

import java.util.List;

public class PayrollTransactionService {

    private final PayrollTransactionRepository payrollTransactionRepository;

    public PayrollTransactionService(PayrollTransactionRepository payrollTransactionRepository) {
        this.payrollTransactionRepository = payrollTransactionRepository;
    }

    public PayrollTransaction getPayrollTransactionById(Integer id) {
        return payrollTransactionRepository.findById(id);
    }

    public List<PayrollTransaction> getAllPayrollTransactions() {
        return payrollTransactionRepository.findAll();
    }

    public void savePayrollTransaction(PayrollTransaction payrollTransaction) {
        payrollTransactionRepository.save(payrollTransaction);
    }

    public void updatePayrollTransaction(PayrollTransaction payrollTransaction) {
        payrollTransactionRepository.update(payrollTransaction);
    }

    public void deletePayrollTransaction(PayrollTransaction payrollTransaction) {
        payrollTransactionRepository.delete(payrollTransaction);
    }
}
