package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.LeaveRequestType;

import java.util.List;

public class LeaveRequestTypeRepository extends _AbstractHibernateRepository<LeaveRequestType, Integer> {
    public LeaveRequestTypeRepository() {
        super(LeaveRequestType.class);
    }

    public List<String> findAllLeaveTypeNames() {
        String hql = "SELECT leaveTypeName FROM LeaveRequestType";
        return (List<String>) runQuery(hql, String.class);
    }
}
