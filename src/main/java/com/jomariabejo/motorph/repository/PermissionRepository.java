package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Permission;
import org.hibernate.Session;

public class PermissionRepository extends _AbstractHibernateRepository<Permission, Integer> {
    public PermissionRepository() {
        super(Permission.class);
    }

    public Permission findByPermissionName(String permissionName) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                    "FROM Permission WHERE permissionName = :name", Permission.class)
                    .setParameter("name", permissionName)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
