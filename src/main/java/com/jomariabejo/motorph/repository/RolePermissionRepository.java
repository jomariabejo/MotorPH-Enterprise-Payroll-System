package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.model.RolePermission;

public class RolePermissionRepository extends _AbstractHibernateRepository<RolePermission, Integer> {
    public RolePermissionRepository() {
        super(RolePermission.class);
    }
}
