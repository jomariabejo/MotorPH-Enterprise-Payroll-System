package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.LeaveBalance;

public class LeaveBalanceRepository extends _AbstractHibernateRepository<LeaveBalance, Integer> {
    public LeaveBalanceRepository() {
        super(LeaveBalance.class);
    }
}
