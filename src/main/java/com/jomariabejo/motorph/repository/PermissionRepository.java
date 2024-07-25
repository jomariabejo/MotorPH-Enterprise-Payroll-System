package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.Permission;

public class PermissionRepository extends _AbstractHibernateRepository<Permission, Integer> {
    public PermissionRepository() {
        super(Permission.class);
    }
}
