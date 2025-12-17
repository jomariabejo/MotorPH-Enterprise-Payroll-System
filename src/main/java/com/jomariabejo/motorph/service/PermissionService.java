package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.repository.PermissionRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;
    private static final String SYSTEM_ADMIN_PREFIX = "system.admin.";

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.roleService = null;
        this.rolePermissionService = null;
    }

    public PermissionService(PermissionRepository permissionRepository, RoleService roleService, RolePermissionService rolePermissionService) {
        this.permissionRepository = permissionRepository;
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
    }

    public Permission getPermissionById(Integer id) {
        return permissionRepository.findById(id);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public void savePermission(Permission permission) {
        permissionRepository.save(permission);
    }

    public void updatePermission(Permission permission) {
        permissionRepository.update(permission);
    }

    public void deletePermission(Permission permission) {
        permissionRepository.delete(permission);
    }

    public Permission findByPermissionName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName);
    }

    /**
     * Synchronizes all permission constants from PermissionConstants class to the database.
     * Creates missing permissions with appropriate categories and descriptions.
     * 
     * @return SyncResult containing counts of created and existing permissions
     */
    public SyncResult syncPermissionsFromConstants() {
        int created = 0;
        int existing = 0;
        List<String> createdPermissions = new ArrayList<>();
        List<String> existingPermissions = new ArrayList<>();
        int systemAdminAssigned = 0;
        int employeeAssigned = 0;
        int hrAssigned = 0;
        int payrollAssigned = 0;

        try {
            // Get all permission constants using reflection
            Field[] fields = PermissionConstants.class.getDeclaredFields();
            Map<String, PermissionInfo> permissionMap = new HashMap<>();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                boolean isPublicStaticFinal = java.lang.reflect.Modifier.isPublic(modifiers) &&
                                             java.lang.reflect.Modifier.isStatic(modifiers) &&
                                             java.lang.reflect.Modifier.isFinal(modifiers);
                
                if (field.getType() == String.class && isPublicStaticFinal) {
                    try {
                        String constantName = field.getName();
                        String permissionConstant = (String) field.get(null);
                        
                        // Skip navigation and menu maps
                        if (constantName.contains("MAP") || constantName.contains("NAVIGATION") || constantName.contains("MENU")) {
                            continue;
                        }

                        PermissionInfo info = createPermissionInfo(permissionConstant);
                        permissionMap.put(permissionConstant, info);
                    } catch (IllegalAccessException e) {
                        // Skip fields we can't access
                        continue;
                    }
                }
            }

            // Sync each permission
            for (Map.Entry<String, PermissionInfo> entry : permissionMap.entrySet()) {
                String permissionConstant = entry.getKey();
                PermissionInfo info = entry.getValue();

                // Check if permission already exists
                Permission existingPermission = findByPermissionName(permissionConstant);
                
                if (existingPermission == null) {
                    // Create new permission
                    Permission newPermission = new Permission();
                    newPermission.setPermissionName(permissionConstant);
                    newPermission.setDescription(info.description);
                    newPermission.setCategory(info.category);
                    
                    savePermission(newPermission);
                    created++;
                    createdPermissions.add(info.displayName);
                } else {
                    // Update category and description if they're missing
                    boolean updated = false;
                    if (existingPermission.getCategory() == null || existingPermission.getCategory().isEmpty()) {
                        existingPermission.setCategory(info.category);
                        updated = true;
                    }
                    String desc = existingPermission.getDescription();
                    if (desc == null || desc.isEmpty()) {
                        existingPermission.setDescription(info.description);
                        updated = true;
                    }
                    if (updated) {
                        updatePermission(existingPermission);
                    }
                    existing++;
                    existingPermissions.add(info.displayName);
                }
            }

            // After ensuring all permissions exist, auto-grant System Administrator all System Admin permissions
            // (best-effort; skips if role/services are unavailable).
            systemAdminAssigned = ensureSystemAdministratorHasAllPermissions(permissionMap);

            // Best-effort: keep base roles aligned by prefix so multi-role users can switch views immediately.
            employeeAssigned = ensureRoleHasAllPermissionsByPrefix("Employee", "employee.", permissionMap);
            hrAssigned = ensureRoleHasAllPermissionsByPrefix("HR Administrator", "hr.", permissionMap);
            payrollAssigned = ensureRoleHasAllPermissionsByPrefix("Payroll Administrator", "payroll.", permissionMap);

        } catch (Exception e) {
            throw new RuntimeException("Error syncing permissions: " + e.getMessage(), e);
        }

        return new SyncResult(created, existing, systemAdminAssigned, employeeAssigned, hrAssigned, payrollAssigned, createdPermissions, existingPermissions);
    }

    /**
     * Ensure the role named "System Administrator" has all System Admin permission constants assigned
     * (i.e., permission names starting with "system.admin.").
     *
     * @return number of newly-created role-permission assignments
     */
    private int ensureSystemAdministratorHasAllPermissions(Map<String, PermissionInfo> permissionMap) {
        if (roleService == null || rolePermissionService == null || permissionMap == null || permissionMap.isEmpty()) {
            return 0;
        }

        Role systemAdminRole = roleService.getRoleByName("System Administrator");
        if (systemAdminRole == null) {
            return 0;
        }

        int assigned = 0;
        for (String permissionName : permissionMap.keySet()) {
            if (permissionName == null || !permissionName.startsWith(SYSTEM_ADMIN_PREFIX)) {
                continue;
            }
            Permission permission = findByPermissionName(permissionName);
            if (permission != null) {
                boolean didAssign = rolePermissionService.assignPermissionToRole(systemAdminRole, permission);
                if (didAssign) {
                    assigned++;
                }
            }
        }

        // Clear cache so changes apply immediately
        if (systemAdminRole.getId() != null) {
            com.jomariabejo.motorph.utility.PermissionChecker.clearRoleCache(systemAdminRole.getId());
        }

        return assigned;
    }

    private int ensureRoleHasAllPermissionsByPrefix(String roleName, String prefix, Map<String, PermissionInfo> permissionMap) {
        if (roleService == null || rolePermissionService == null || permissionMap == null || permissionMap.isEmpty()) {
            return 0;
        }
        if (roleName == null || roleName.isBlank() || prefix == null || prefix.isBlank()) {
            return 0;
        }

        Role role = roleService.getRoleByName(roleName);
        if (role == null) {
            return 0;
        }

        int assigned = 0;
        for (String permissionName : permissionMap.keySet()) {
            if (permissionName == null || !permissionName.startsWith(prefix)) {
                continue;
            }
            Permission permission = findByPermissionName(permissionName);
            if (permission != null) {
                boolean didAssign = rolePermissionService.assignPermissionToRole(role, permission);
                if (didAssign) {
                    assigned++;
                }
            }
        }

        if (role.getId() != null) {
            com.jomariabejo.motorph.utility.PermissionChecker.clearRoleCache(role.getId());
        }
        return assigned;
    }

    /**
     * Creates PermissionInfo from a permission constant string.
     */
    private PermissionInfo createPermissionInfo(String permissionConstant) {
        String category;
        String displayName;
        String description;

        // Determine category based on prefix
        if (permissionConstant.startsWith("employee.")) {
            category = "Employee";
            displayName = formatPermissionName(permissionConstant.replace("employee.", ""));
            description = "Employee permission: " + displayName;
        } else if (permissionConstant.startsWith("hr.")) {
            category = "Human Resources";
            displayName = formatPermissionName(permissionConstant.replace("hr.", ""));
            description = "Human Resources permission: " + displayName;
        } else if (permissionConstant.startsWith("payroll.")) {
            category = "Payroll";
            displayName = formatPermissionName(permissionConstant.replace("payroll.", ""));
            description = "Payroll permission: " + displayName;
        } else if (permissionConstant.startsWith(SYSTEM_ADMIN_PREFIX)) {
            category = "System Administration";
            displayName = formatPermissionName(permissionConstant.replace(SYSTEM_ADMIN_PREFIX, ""));
            description = "System Administration permission: " + displayName;
        } else {
            category = "General";
            displayName = formatPermissionName(permissionConstant);
            description = "System permission: " + displayName;
        }

        return new PermissionInfo(category, displayName, description);
    }

    /**
     * Formats a permission constant name into a human-readable display name.
     * Example: "employee.profile.view" -> "View Employee Profile"
     */
    private String formatPermissionName(String name) {
        String[] parts = name.split("\\.");
        StringBuilder result = new StringBuilder();
        
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];
            if (part.length() > 0) {
                result.append(Character.toUpperCase(part.charAt(0)));
                result.append(part.substring(1));
                if (i > 0) {
                    result.append(" ");
                }
            }
        }
        
        return result.toString();
    }

    /**
     * Result class for sync operation
     */
    public static class SyncResult {
        private final int created;
        private final int existing;
        private final int systemAdminAssigned;
        private final int employeeAssigned;
        private final int hrAssigned;
        private final int payrollAssigned;
        private final List<String> createdPermissions;
        private final List<String> existingPermissions;

        public SyncResult(int created, int existing, int systemAdminAssigned, int employeeAssigned, int hrAssigned, int payrollAssigned, List<String> createdPermissions, List<String> existingPermissions) {
            this.created = created;
            this.existing = existing;
            this.systemAdminAssigned = systemAdminAssigned;
            this.employeeAssigned = employeeAssigned;
            this.hrAssigned = hrAssigned;
            this.payrollAssigned = payrollAssigned;
            this.createdPermissions = createdPermissions;
            this.existingPermissions = existingPermissions;
        }

        public int getCreated() {
            return created;
        }

        public int getExisting() {
            return existing;
        }

        public int getSystemAdminAssigned() {
            return systemAdminAssigned;
        }

        public int getEmployeeAssigned() {
            return employeeAssigned;
        }

        public int getHrAssigned() {
            return hrAssigned;
        }

        public int getPayrollAssigned() {
            return payrollAssigned;
        }

        public List<String> getCreatedPermissions() {
            return createdPermissions;
        }

        public List<String> getExistingPermissions() {
            return existingPermissions;
        }
    }

    /**
     * Internal class to hold permission information
     */
    private static class PermissionInfo {
        final String category;
        final String displayName;
        final String description;

        PermissionInfo(String category, String displayName, String description) {
            this.category = category;
            this.displayName = displayName;
            this.description = description;
        }
    }
}
