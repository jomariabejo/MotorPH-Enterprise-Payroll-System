package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Role;
import org.hibernate.exception.ConstraintViolationException;

public class RoleRepository extends _AbstractHibernateRepository<Role, Integer> {
    public RoleRepository() {
        super(Role.class);
    }
}
