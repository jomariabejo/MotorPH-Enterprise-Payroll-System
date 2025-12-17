package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.RolePermission;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class RolePermissionRepository extends _AbstractHibernateRepository<RolePermission, Integer> {
    public RolePermissionRepository() {
        super(RolePermission.class);
    }

    /**
     * Find all permissions assigned to a role (eagerly loads relationships).
     * 
     * @param role The role
     * @return List of permissions for the role
     */
    public List<Permission> findPermissionsByRole(Role role) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Permission> query = session.createQuery(
                    "SELECT DISTINCT p FROM RolePermission rp " +
                    "JOIN rp.permissionID p " +
                    "WHERE rp.roleID = :role",
                    Permission.class
            );
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Find all roles that have a specific permission.
     * 
     * @param permission The permission
     * @return List of roles with the permission
     */
    public List<Role> findRolesByPermission(Permission permission) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Role> query = session.createQuery(
                    "SELECT DISTINCT rp.roleID FROM RolePermission rp " +
                    "WHERE rp.permissionID = :permission",
                    Role.class
            );
            query.setParameter("permission", permission);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Check if a role has a specific permission.
     * 
     * @param role The role
     * @param permission The permission
     * @return true if role has the permission, false otherwise
     */
    public boolean hasPermission(Role role, Permission permission) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(rp) FROM RolePermission rp " +
                    "WHERE rp.roleID = :role AND rp.permissionID = :permission",
                    Long.class
            );
            query.setParameter("role", role);
            query.setParameter("permission", permission);
            Long count = query.getSingleResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Find a RolePermission by role and permission.
     * 
     * @param role The role
     * @param permission The permission
     * @return Optional RolePermission if found
     */
    public Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<RolePermission> query = session.createQuery(
                    "SELECT rp FROM RolePermission rp " +
                    "JOIN FETCH rp.roleID " +
                    "JOIN FETCH rp.permissionID " +
                    "WHERE rp.roleID = :role AND rp.permissionID = :permission",
                    RolePermission.class
            );
            query.setParameter("role", role);
            query.setParameter("permission", permission);
            RolePermission result = query.getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Find all RolePermissions for a role (eagerly loads relationships).
     * 
     * @param role The role
     * @return List of RolePermissions
     */
    public List<RolePermission> findByRole(Role role) {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Query<RolePermission> query = session.createQuery(
                    "SELECT rp FROM RolePermission rp " +
                    "JOIN FETCH rp.roleID " +
                    "JOIN FETCH rp.permissionID " +
                    "WHERE rp.roleID = :role",
                    RolePermission.class
            );
            query.setParameter("role", role);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
