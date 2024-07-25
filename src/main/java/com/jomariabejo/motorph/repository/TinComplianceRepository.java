package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.TinCompliance;

public class TinComplianceRepository extends _AbstractHibernateRepository<TinCompliance, Integer> {
    public TinComplianceRepository() {
        super(TinCompliance.class);
    }
}
