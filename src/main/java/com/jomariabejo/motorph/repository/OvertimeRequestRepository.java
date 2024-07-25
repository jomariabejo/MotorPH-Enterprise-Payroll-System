package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.OvertimeRequest;

public class OvertimeRequestRepository extends _AbstractHibernateRepository<OvertimeRequest, Integer> {
    public OvertimeRequestRepository() {
        super(OvertimeRequest.class);
    }
}
