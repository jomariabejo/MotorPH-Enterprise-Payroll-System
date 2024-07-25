package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Payslip;

public class PayslipRepository extends _AbstractHibernateRepository<Payslip, Integer> {
    public PayslipRepository() {
        super(Payslip.class);
    }
}
