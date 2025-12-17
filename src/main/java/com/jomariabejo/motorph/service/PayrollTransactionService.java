package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollTransaction;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.repository.PayrollTransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<PayrollTransaction> getTransactionsByPayroll(Payroll payroll) {
        return payrollTransactionRepository.findByPayroll(payroll);
    }

    public List<PayrollTransaction> getTransactionsByPayslip(Payslip payslip) {
        return payrollTransactionRepository.findByPayslip(payslip);
    }

    public List<PayrollTransaction> getTransactionsByType(String type) {
        return payrollTransactionRepository.findByTransactionType(type);
    }

    public List<PayrollTransaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        return payrollTransactionRepository.findByDateRange(start, end);
    }

    public List<PayrollTransaction> searchTransactions(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllPayrollTransactions();
        }
        return payrollTransactionRepository.searchTransactions(searchTerm.trim());
    }

    public List<PayrollTransaction> getPaginatedTransactions(int page, int pageSize) {
        return payrollTransactionRepository.findAllWithPagination(page, pageSize);
    }

    public long getTotalTransactionCount() {
        return payrollTransactionRepository.countAll();
    }

    public PayrollTransaction createTransaction(Payroll payroll, Payslip payslip, String type, 
                                                BigDecimal amount, String description) {
        PayrollTransaction transaction = new PayrollTransaction();
        transaction.setPayrollID(payroll);
        transaction.setPayslipID(payslip);
        transaction.setTransactionType(type);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setDescription(description);
        savePayrollTransaction(transaction);
        return transaction;
    }

    public List<String> getTransactionTypes() {
        return Arrays.asList("Deduction", "Bonus", "Adjustment", "Payment", "Refund", "Other");
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

    public List<PayrollTransaction> filterTransactions(Payroll payroll, Payslip payslip, String type,
                                                      LocalDate startDate, LocalDate endDate, String searchTerm) {
        List<PayrollTransaction> results = getAllPayrollTransactions();

        // Apply filters
        if (payroll != null) {
            results = results.stream()
                    .filter(t -> t.getPayrollID() != null && t.getPayrollID().getId().equals(payroll.getId()))
                    .collect(Collectors.toList());
        }

        if (payslip != null) {
            results = results.stream()
                    .filter(t -> t.getPayslipID() != null && t.getPayslipID().getId().equals(payslip.getId()))
                    .collect(Collectors.toList());
        }

        if (type != null && !type.isEmpty() && !type.equals("All")) {
            results = results.stream()
                    .filter(t -> t.getTransactionType().equals(type))
                    .collect(Collectors.toList());
        }

        if (startDate != null) {
            results = results.stream()
                    .filter(t -> !t.getTransactionDate().isBefore(startDate))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            results = results.stream()
                    .filter(t -> !t.getTransactionDate().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            String searchLower = searchTerm.toLowerCase();
            results = results.stream()
                    .filter(t -> (t.getDescription() != null && t.getDescription().toLowerCase().contains(searchLower)) ||
                                 (t.getTransactionType() != null && t.getTransactionType().toLowerCase().contains(searchLower)))
                    .collect(Collectors.toList());
        }

        return results;
    }
}
