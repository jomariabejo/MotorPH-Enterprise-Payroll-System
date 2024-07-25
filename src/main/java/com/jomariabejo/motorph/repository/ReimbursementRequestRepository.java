package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.ReimbursementRequest;

public class ReimbursementRequestRepository extends _AbstractHibernateRepository<ReimbursementRequest, Integer> {
    public ReimbursementRequestRepository() {
        super(ReimbursementRequest.class);
    }
}
