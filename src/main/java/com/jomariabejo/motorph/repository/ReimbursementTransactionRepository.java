package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.ReimbursementTransaction;

public class ReimbursementTransactionRepository extends _AbstractHibernateRepository<ReimbursementTransaction, Integer> {
    public ReimbursementTransactionRepository() {
        super(ReimbursementTransaction.class);
    }
}
