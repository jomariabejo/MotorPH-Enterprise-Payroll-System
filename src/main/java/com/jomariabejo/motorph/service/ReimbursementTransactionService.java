package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.ReimbursementTransaction;
import com.jomariabejo.motorph.repository.ReimbursementTransactionRepository;

import java.util.List;

public class ReimbursementTransactionService {

    private final ReimbursementTransactionRepository reimbursementTransactionRepository;

    public ReimbursementTransactionService(ReimbursementTransactionRepository reimbursementTransactionRepository) {
        this.reimbursementTransactionRepository = reimbursementTransactionRepository;
    }

    public ReimbursementTransaction getReimbursementTransactionById(Integer id) {
        return reimbursementTransactionRepository.findById(id);
    }

    public List<ReimbursementTransaction> getAllReimbursementTransactions() {
        return reimbursementTransactionRepository.findAll();
    }

    public void saveReimbursementTransaction(ReimbursementTransaction reimbursementTransaction) {
        reimbursementTransactionRepository.save(reimbursementTransaction);
    }

    public void updateReimbursementTransaction(ReimbursementTransaction reimbursementTransaction) {
        reimbursementTransactionRepository.update(reimbursementTransaction);
    }

    public void deleteReimbursementTransaction(ReimbursementTransaction reimbursementTransaction) {
        reimbursementTransactionRepository.delete(reimbursementTransaction);
    }
}
