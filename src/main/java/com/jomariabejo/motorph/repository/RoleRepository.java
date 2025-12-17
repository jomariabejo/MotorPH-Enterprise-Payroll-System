package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Role;
import org.hibernate.Session;

public class RoleRepository extends _AbstractHibernateRepository<Role, Integer> {
    public RoleRepository() {
        super(Role.class);
    }

    public Role findByRoleName(String roleName) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "FROM Role WHERE lower(roleName) = lower(:name)",
                            Role.class
                    )
                    .setParameter("name", roleName)
                    .uniqueResult();
        } finally {
            session.close();
        }
    }
}
