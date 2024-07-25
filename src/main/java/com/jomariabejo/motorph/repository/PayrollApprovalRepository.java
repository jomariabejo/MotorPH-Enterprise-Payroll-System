package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.PayrollApproval;

public class PayrollApprovalRepository extends _AbstractHibernateRepository<PayrollApproval, Integer> {
    public PayrollApprovalRepository() {
        super(PayrollApproval.class);
    }
}
