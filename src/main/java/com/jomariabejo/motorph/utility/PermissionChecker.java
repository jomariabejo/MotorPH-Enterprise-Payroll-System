package com.jomariabejo.motorph.utility;

import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.service.RolePermissionService;
import com.jomariabejo.motorph.service.RoleService;
import com.jomariabejo.motorph.service.ServiceFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for checking user permissions.
 * Provides enterprise-level permission checking with caching for performance.
 */
public class PermissionChecker {
    
    // Cache for user permissions to avoid repeated database queries
    private static final Map<Integer, Set<String>> USER_PERMISSION_CACHE = new ConcurrentHashMap<>();
    private static final Map<Integer, Set<String>> ROLE_PERMISSION_CACHE = new ConcurrentHashMap<>();
    
    // Cache expiration time (5 minutes)
    private static final long CACHE_EXPIRATION_MS = 5L * 60 * 1000;
    private static final Map<Integer, Long> CACHE_TIMESTAMPS = new ConcurrentHashMap<>();
    
    // Private constructor to prevent instantiation
    private PermissionChecker() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Check if a user has a specific permission.
     * 
     * @param user The user to check
     * @param permissionName The permission name to check
     * @param serviceFactory Service factory for accessing services
     * @return true if user has the permission, false otherwise
     */
    public static boolean hasPermission(User user, String permissionName, ServiceFactory serviceFactory) {
        if (user == null || permissionName == null || serviceFactory == null) {
            return false;
        }
        
        Set<String> userPermissions = getUserPermissions(user, serviceFactory);
        return userPermissions.contains(permissionName);
    }
    
    /**
     * Check if a user has any of the specified permissions.
     * 
     * @param user The user to check
     * @param permissionNames The permission names to check (at least one must match)
     * @param serviceFactory Service factory for accessing services
     * @return true if user has at least one of the permissions, false otherwise
     */
    public static boolean hasAnyPermission(User user, ServiceFactory serviceFactory, String... permissionNames) {
        if (user == null || permissionNames == null || permissionNames.length == 0 || serviceFactory == null) {
            return false;
        }
        
        Set<String> userPermissions = getUserPermissions(user, serviceFactory);
        for (String permissionName : permissionNames) {
            if (userPermissions.contains(permissionName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a user has all of the specified permissions.
     * 
     * @param user The user to check
     * @param permissionNames The permission names to check (all must match)
     * @param serviceFactory Service factory for accessing services
     * @return true if user has all of the permissions, false otherwise
     */
    public static boolean hasAllPermissions(User user, ServiceFactory serviceFactory, String... permissionNames) {
        if (user == null || permissionNames == null || permissionNames.length == 0 || serviceFactory == null) {
            return false;
        }
        
        Set<String> userPermissions = getUserPermissions(user, serviceFactory);
        for (String permissionName : permissionNames) {
            if (!userPermissions.contains(permissionName)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Get all permissions for a user (including inherited permissions from parent roles).
     * 
     * @param user The user
     * @param serviceFactory Service factory for accessing services
     * @return Set of permission names
     */
    public static Set<String> getUserPermissions(User user, ServiceFactory serviceFactory) {
        if (user == null || user.getId() == null || serviceFactory == null) {
            return Collections.emptySet();
        }
        
        Integer userId = user.getId();
        
        // Check cache first
        if (isCacheValid(userId)) {
            Set<String> cached = USER_PERMISSION_CACHE.get(userId);
            if (cached != null) {
                return new HashSet<>(cached);
            }
        }
        
        // Get permissions from database (union of all roles)
        Set<String> permissions = new HashSet<>();

        Role primaryRole = user.getRoleID();
        if (primaryRole != null) {
            permissions.addAll(getRolePermissions(primaryRole, serviceFactory));
        }

        Set<Role> extraRoles = user.getRoles();
        if (extraRoles != null) {
            for (Role r : extraRoles) {
                if (r != null) {
                    permissions.addAll(getRolePermissions(r, serviceFactory));
                }
            }
        }

        if (permissions.isEmpty()) {
            return Collections.emptySet();
        }
        
        // Cache the result
        USER_PERMISSION_CACHE.put(userId, permissions);
        CACHE_TIMESTAMPS.put(userId, System.currentTimeMillis());
        
        return permissions;
    }
    
    /**
     * Get all permissions for a role (including inherited permissions from parent roles).
     * 
     * @param role The role
     * @param serviceFactory Service factory for accessing services
     * @return Set of permission names
     */
    public static Set<String> getRolePermissions(Role role, ServiceFactory serviceFactory) {
        if (role == null || role.getId() == null || serviceFactory == null) {
            return Collections.emptySet();
        }
        
        Integer roleId = role.getId();
        
        // Check cache first
        if (isCacheValid(roleId)) {
            Set<String> cached = ROLE_PERMISSION_CACHE.get(roleId);
            if (cached != null) {
                return new HashSet<>(cached);
            }
        }
        
        RolePermissionService rolePermissionService = serviceFactory.getRolePermissionService();
        Set<String> permissions = new HashSet<>();
        
        // Get direct permissions for this role
        List<Permission> directPermissions = rolePermissionService.getPermissionsForRole(role);
        for (Permission permission : directPermissions) {
            if (permission != null && permission.getPermissionName() != null) {
                permissions.add(permission.getPermissionName());
            }
        }
        
        // Get inherited permissions from parent role (recursive)
        // Need to fetch parent role with its parentRole loaded
        Role parentRole = role.getParentRole();
        if (parentRole != null && parentRole.getId() != null) {
            // Fetch parent role to ensure it's loaded
            RoleService roleService = serviceFactory.getRoleService();
            Role loadedParentRole = roleService.getRoleById(parentRole.getId());
            if (loadedParentRole != null) {
                Set<String> parentPermissions = getRolePermissions(loadedParentRole, serviceFactory);
                permissions.addAll(parentPermissions);
            }
        }
        
        // Cache the result
        ROLE_PERMISSION_CACHE.put(roleId, permissions);
        CACHE_TIMESTAMPS.put(roleId, System.currentTimeMillis());
        
        return permissions;
    }
    
    /**
     * Clear the permission cache for a specific user.
     * 
     * @param userId The user ID
     */
    public static void clearUserCache(Integer userId) {
        if (userId != null) {
            USER_PERMISSION_CACHE.remove(userId);
            CACHE_TIMESTAMPS.remove(userId);
        }
    }
    
    /**
     * Clear the permission cache for a specific role.
     * 
     * @param roleId The role ID
     */
    public static void clearRoleCache(Integer roleId) {
        if (roleId != null) {
            ROLE_PERMISSION_CACHE.remove(roleId);
            CACHE_TIMESTAMPS.remove(roleId);
            
            // Also clear all user caches that might be affected
            USER_PERMISSION_CACHE.clear();
        }
    }
    
    /**
     * Clear all permission caches.
     */
    public static void clearAllCaches() {
        USER_PERMISSION_CACHE.clear();
        ROLE_PERMISSION_CACHE.clear();
        CACHE_TIMESTAMPS.clear();
    }
    
    /**
     * Check if cache is still valid (not expired).
     * 
     * @param id The ID (user or role)
     * @return true if cache is valid, false if expired or not found
     */
    private static boolean isCacheValid(Integer id) {
        Long timestamp = CACHE_TIMESTAMPS.get(id);
        if (timestamp == null) {
            return false;
        }
        
        long now = System.currentTimeMillis();
        return (now - timestamp) < CACHE_EXPIRATION_MS;
    }
}

