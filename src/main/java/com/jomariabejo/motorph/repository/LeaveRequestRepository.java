package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.LeaveRequest;

public class LeaveRequestRepository extends _AbstractHibernateRepository<LeaveRequest, Integer> {
    public LeaveRequestRepository() {
        super(LeaveRequest.class);
    }
}
