package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.RolePermission;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.repository.RolePermissionRepository;
import com.jomariabejo.motorph.utility.PermissionChecker;

import java.util.*;

public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public RolePermission getRolePermissionById(Integer id) {
        return rolePermissionRepository.findById(id);
    }

    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionRepository.findAll();
    }

    public void saveRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.save(rolePermission);
        // Clear cache when permissions change
        if (rolePermission.getRoleID() != null && rolePermission.getRoleID().getId() != null) {
            PermissionChecker.clearRoleCache(rolePermission.getRoleID().getId());
        }
    }

    public void updateRolePermission(RolePermission rolePermission) {
        rolePermissionRepository.update(rolePermission);
        // Clear cache when permissions change
        if (rolePermission.getRoleID() != null && rolePermission.getRoleID().getId() != null) {
            PermissionChecker.clearRoleCache(rolePermission.getRoleID().getId());
        }
    }

    public void deleteRolePermission(RolePermission rolePermission) {
        Integer roleId = rolePermission.getRoleID() != null ? rolePermission.getRoleID().getId() : null;
        rolePermissionRepository.delete(rolePermission);
        // Clear cache when permissions change
        if (roleId != null) {
            PermissionChecker.clearRoleCache(roleId);
        }
    }

    /**
     * Get all permissions for a role (including inherited permissions).
     * 
     * @param role The role
     * @return List of permissions
     */
    public List<Permission> getPermissionsForRole(Role role) {
        if (role == null) {
            return Collections.emptyList();
        }
        return rolePermissionRepository.findPermissionsByRole(role);
    }

    /**
     * Get all roles that have a specific permission.
     * 
     * @param permission The permission
     * @return List of roles
     */
    public List<Role> getRolesByPermission(Permission permission) {
        if (permission == null) {
            return Collections.emptyList();
        }
        return rolePermissionRepository.findRolesByPermission(permission);
    }

    /**
     * Check if a role has a specific permission.
     * 
     * @param role The role
     * @param permission The permission
     * @return true if role has the permission
     */
    public boolean hasPermission(Role role, Permission permission) {
        if (role == null || permission == null) {
            return false;
        }
        return rolePermissionRepository.hasPermission(role, permission);
    }

    /**
     * Assign a permission to a role.
     * 
     * @param role The role
     * @param permission The permission
     * @return true if assignment was successful, false if already assigned
     */
    public boolean assignPermissionToRole(Role role, Permission permission) {
        if (role == null || permission == null) {
            return false;
        }

        // Check if already assigned
        Optional<RolePermission> existing = rolePermissionRepository.findByRoleAndPermission(role, permission);
        if (existing.isPresent()) {
            return false; // Already assigned
        }

        // Create new assignment
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRoleID(role);
        rolePermission.setPermissionID(permission);
        saveRolePermission(rolePermission);
        
        return true;
    }

    /**
     * Remove a permission from a role.
     * 
     * @param role The role
     * @param permission The permission
     * @return true if removal was successful, false if not assigned
     */
    public boolean removePermissionFromRole(Role role, Permission permission) {
        if (role == null || permission == null) {
            return false;
        }

        Optional<RolePermission> existing = rolePermissionRepository.findByRoleAndPermission(role, permission);
        if (existing.isPresent()) {
            deleteRolePermission(existing.get());
            return true;
        }
        
        return false; // Not assigned
    }

    /**
     * Assign multiple permissions to a role.
     * 
     * @param role The role
     * @param permissions List of permissions to assign
     * @return Number of permissions successfully assigned
     */
    public int assignPermissionsToRole(Role role, List<Permission> permissions) {
        if (role == null || permissions == null || permissions.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Permission permission : permissions) {
            if (assignPermissionToRole(role, permission)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Remove multiple permissions from a role.
     * 
     * @param role The role
     * @param permissions List of permissions to remove
     * @return Number of permissions successfully removed
     */
    public int removePermissionsFromRole(Role role, List<Permission> permissions) {
        if (role == null || permissions == null || permissions.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Permission permission : permissions) {
            if (removePermissionFromRole(role, permission)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get all permissions for a user (through their role).
     * 
     * @param user The user
     * @return List of permissions
     */
    public List<Permission> getPermissionsForUser(User user) {
        if (user == null || user.getRoleID() == null) {
            return Collections.emptyList();
        }
        return getPermissionsForRole(user.getRoleID());
    }

    /**
     * Get all RolePermissions for a role.
     * 
     * @param role The role
     * @return List of RolePermissions
     */
    public List<RolePermission> getRolePermissionsByRole(Role role) {
        if (role == null) {
            return Collections.emptyList();
        }
        return rolePermissionRepository.findByRole(role);
    }
}
