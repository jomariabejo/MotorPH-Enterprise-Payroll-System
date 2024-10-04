package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.PayrollChange;

public class PayrollChangeRepository extends _AbstractHibernateRepository<PayrollChange, Integer> {
    public PayrollChangeRepository() {
        super(PayrollChange.class);
    }
}
