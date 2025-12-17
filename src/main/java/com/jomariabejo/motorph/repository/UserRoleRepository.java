package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.model.UserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;

public class UserRoleRepository extends _AbstractHibernateRepository<UserRole, Integer> {
    public UserRoleRepository() {
        super(UserRole.class);
    }

    public boolean assignmentExists(Integer userId, Integer roleId) {
        Session session = HibernateUtil.openSession();
        try {
            Long count = session.createQuery(
                            "SELECT COUNT(ur) FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId",
                            Long.class
                    )
                    .setParameter("userId", userId)
                    .setParameter("roleId", roleId)
                    .uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }

    /**
     * Ensure a (user, role) assignment exists; creates it if missing.
     *
     * @return true if created, false if already existed or invalid input
     */
    public boolean ensureAssignment(Integer userId, Integer roleId) {
        if (userId == null || roleId == null) {
            return false;
        }
        if (assignmentExists(userId, roleId)) {
            return false;
        }

        Session session = HibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            User userRef = session.get(User.class, userId);
            Role roleRef = session.get(Role.class, roleId);
            if (userRef == null || roleRef == null) {
                return false;
            }
            UserRole userRole = new UserRole();
            userRole.setUser(userRef);
            userRole.setRole(roleRef);
            session.persist(userRole);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Role> findRolesForUser(Integer userId) {
        Session session = HibernateUtil.openSession();
        try {
            return session.createQuery(
                            "SELECT DISTINCT ur.role FROM UserRole ur JOIN ur.role r WHERE ur.user.id = :userId",
                            Role.class
                    )
                    .setParameter("userId", userId)
                    .list();
        } finally {
            session.close();
        }
    }

    /**
     * Replace the user's assigned roles to exactly match the given role IDs.
     * This does NOT touch user.RoleID (primary role), only the join table.
     */
    public void setRolesForUser(Integer userId, Collection<Integer> roleIds) {
        if (userId == null) {
            return;
        }

        Session session = HibernateUtil.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            // Remove roles not in desired set
            if (roleIds == null || roleIds.isEmpty()) {
                session.createMutationQuery("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
                        .setParameter("userId", userId)
                        .executeUpdate();
            } else {
                session.createMutationQuery(
                                "DELETE FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id NOT IN (:roleIds)"
                        )
                        .setParameter("userId", userId)
                        .setParameterList("roleIds", roleIds)
                        .executeUpdate();
            }

            // Add missing roles
            if (roleIds != null) {
                for (Integer roleId : roleIds) {
                    if (roleId == null) continue;
                    Long count = session.createQuery(
                                    "SELECT COUNT(ur) FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId",
                                    Long.class
                            )
                            .setParameter("userId", userId)
                            .setParameter("roleId", roleId)
                            .uniqueResult();
                    if (count != null && count > 0) {
                        continue;
                    }

                    User userRef = session.get(User.class, userId);
                    Role roleRef = session.get(Role.class, roleId);
                    if (userRef == null || roleRef == null) {
                        continue;
                    }
                    UserRole userRole = new UserRole();
                    userRole.setUser(userRef);
                    userRole.setRole(roleRef);
                    session.persist(userRole);
                }
            }

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }
}


