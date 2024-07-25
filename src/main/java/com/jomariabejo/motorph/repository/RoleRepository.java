package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Role;

public class RoleRepository extends _AbstractHibernateRepository<Role, Integer> {
    public RoleRepository() {
        super(Role.class);
    }
}
