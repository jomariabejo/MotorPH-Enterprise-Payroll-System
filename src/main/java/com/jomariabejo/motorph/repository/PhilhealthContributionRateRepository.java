package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.PhilhealthContributionRate;

public class PhilhealthContributionRateRepository extends _AbstractHibernateRepository<PhilhealthContributionRate, Integer> {
    public PhilhealthContributionRateRepository() {
        super(PhilhealthContributionRate.class);
    }
}
