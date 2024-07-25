package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Position;

public class PositionRepository extends _AbstractHibernateRepository<Position, Integer> {
    public PositionRepository() {
        super(Position.class);
    }
}
