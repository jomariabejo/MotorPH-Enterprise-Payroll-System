package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Bonus;

public class BonusRepository extends _AbstractHibernateRepository<Bonus, Integer> {
    public BonusRepository() {
        super(Bonus.class);
    }
}
