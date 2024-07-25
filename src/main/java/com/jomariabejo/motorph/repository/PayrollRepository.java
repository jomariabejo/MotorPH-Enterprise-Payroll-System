package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Payroll;

public class PayrollRepository extends _AbstractHibernateRepository<Payroll, Integer> {
    public PayrollRepository() {
        super(Payroll.class);
    }
}
