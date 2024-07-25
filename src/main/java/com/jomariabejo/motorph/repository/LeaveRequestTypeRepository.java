package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.LeaveRequestType;

public class LeaveRequestTypeRepository extends _AbstractHibernateRepository<LeaveRequestType, Integer> {
    public LeaveRequestTypeRepository() {
        super(LeaveRequestType.class);
    }
}
