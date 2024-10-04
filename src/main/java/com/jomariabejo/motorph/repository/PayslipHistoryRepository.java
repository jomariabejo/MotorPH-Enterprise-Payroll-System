package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.PayslipHistory;

public class PayslipHistoryRepository extends _AbstractHibernateRepository<PayslipHistory, Integer> {
    public PayslipHistoryRepository() {
        super(PayslipHistory.class);
    }
}
