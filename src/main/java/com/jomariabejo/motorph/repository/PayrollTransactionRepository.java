package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.PayrollTransaction;

public class PayrollTransactionRepository extends _AbstractHibernateRepository<PayrollTransaction, Integer> {
    public PayrollTransactionRepository() {
        super(PayrollTransaction.class);
    }
}
