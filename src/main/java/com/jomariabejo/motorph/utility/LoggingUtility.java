package com.jomariabejo.motorph.utility;

import com.jomariabejo.motorph.model.Permission;
import com.jomariabejo.motorph.model.Role;
import com.jomariabejo.motorph.model.User;
import com.jomariabejo.motorph.model.UserLog;
import com.jomariabejo.motorph.repository.UserLogRepository;
import com.jomariabejo.motorph.service.UserLogService;

/**
 * Centralized utility class for logging user actions across the application.
 * Provides convenient methods for logging common actions with automatic IP capture.
 */
public class LoggingUtility {

    private static final UserLogService userLogService = new UserLogService(new UserLogRepository());
    private static final String ID_PREFIX = " (ID: ";
    private static final String ID_SUFFIX = ")";

    // Private constructor to prevent instantiation
    private LoggingUtility() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Log a generic user action with IP capture.
     * 
     * @param user The user performing the action
     * @param action The action description
     * @param details Optional additional details (can be null)
     */
    public static void logAction(User user, String action, String details) {
        try {
            if (user == null) {
                return; // Cannot log without user
            }

            UserLog userLog = new UserLog();
            userLog.setUserID(user);
            userLog.setAction(details != null && !details.isEmpty() ? action + ": " + details : action);
            userLog.setIPAddress(NetworkUtils.getLocalIPAddress());

            userLogService.saveUserLog(userLog);
        } catch (Exception e) {
            // Log error but don't break application flow
            // Using System.err as this is a utility class and logging framework may not be initialized
            System.err.println("Failed to log action: " + action + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Log user login action.
     * 
     * @param user The user who logged in
     */
    public static void logLogin(User user) {
        logAction(user, "User logged in", null);
    }

    /**
     * Log user logout action.
     * 
     * @param user The user who logged out
     */
    public static void logLogout(User user) {
        logAction(user, "User logged out", null);
    }

    /**
     * Log user creation action.
     * 
     * @param creator The user who created the new user
     * @param createdUser The user that was created
     */
    public static void logUserCreated(User creator, User createdUser) {
        String details = createdUser.getUsername() + ID_PREFIX + createdUser.getId() + ID_SUFFIX;
        logAction(creator, "User created", details);
    }

    /**
     * Log user update action.
     * 
     * @param updater The user who updated the user
     * @param updatedUser The user that was updated
     */
    public static void logUserUpdated(User updater, User updatedUser) {
        String details = updatedUser.getUsername() + ID_PREFIX + updatedUser.getId() + ID_SUFFIX;
        logAction(updater, "User updated", details);
    }

    /**
     * Log user deletion action.
     * 
     * @param deleter The user who deleted the user
     * @param deletedUser The user that was deleted
     */
    public static void logUserDeleted(User deleter, User deletedUser) {
        String details = deletedUser.getUsername() + ID_PREFIX + deletedUser.getId() + ID_SUFFIX;
        logAction(deleter, "User deleted", details);
    }

    /**
     * Log role creation action.
     * 
     * @param creator The user who created the role
     * @param role The role that was created
     */
    public static void logRoleCreated(User creator, Role role) {
        String details = role.getRoleName() + ID_PREFIX + role.getId() + ID_SUFFIX;
        logAction(creator, "Role created", details);
    }

    /**
     * Log role update action.
     * 
     * @param updater The user who updated the role
     * @param role The role that was updated
     */
    public static void logRoleUpdated(User updater, Role role) {
        String details = role.getRoleName() + ID_PREFIX + role.getId() + ID_SUFFIX;
        logAction(updater, "Role updated", details);
    }

    /**
     * Log role deletion action.
     * 
     * @param deleter The user who deleted the role
     * @param role The role that was deleted
     */
    public static void logRoleDeleted(User deleter, Role role) {
        String details = role.getRoleName() + ID_PREFIX + role.getId() + ID_SUFFIX;
        logAction(deleter, "Role deleted", details);
    }

    /**
     * Log permission creation action.
     * 
     * @param creator The user who created the permission
     * @param permission The permission that was created
     */
    public static void logPermissionCreated(User creator, Permission permission) {
        String details = permission.getPermissionName() + ID_PREFIX + permission.getId() + ID_SUFFIX;
        logAction(creator, "Permission created", details);
    }

    /**
     * Log permission update action.
     * 
     * @param updater The user who updated the permission
     * @param permission The permission that was updated
     */
    public static void logPermissionUpdated(User updater, Permission permission) {
        String details = permission.getPermissionName() + ID_PREFIX + permission.getId() + ID_SUFFIX;
        logAction(updater, "Permission updated", details);
    }

    /**
     * Log permission deletion action.
     * 
     * @param deleter The user who deleted the permission
     * @param permission The permission that was deleted
     */
    public static void logPermissionDeleted(User deleter, Permission permission) {
        String details = permission.getPermissionName() + ID_PREFIX + permission.getId() + ID_SUFFIX;
        logAction(deleter, "Permission deleted", details);
    }

    /**
     * Log permission assignment to role.
     * 
     * @param assigner The user who assigned the permission
     * @param role The role that received the permission
     * @param permission The permission that was assigned
     */
    public static void logPermissionAssigned(User assigner, Role role, Permission permission) {
        String details = permission.getPermissionName() + " to role " + role.getRoleName();
        logAction(assigner, "Permission assigned", details);
    }

    /**
     * Log permission removal from role.
     * 
     * @param remover The user who removed the permission
     * @param role The role that lost the permission
     * @param permission The permission that was removed
     */
    public static void logPermissionRemoved(User remover, Role role, Permission permission) {
        String details = permission.getPermissionName() + " from role " + role.getRoleName();
        logAction(remover, "Permission removed", details);
    }

    /**
     * Log permission synchronization action.
     * 
     * @param syncer The user who performed the sync
     * @param createdCount Number of permissions created
     * @param existingCount Number of permissions that already existed
     */
    public static void logPermissionSync(User syncer, int createdCount, int existingCount) {
        String details = "Created: " + createdCount + ", Existing: " + existingCount;
        logAction(syncer, "Permissions synchronized", details);
    }

    /**
     * Log clock in action.
     * 
     * @param user The user who clocked in
     * @param employeeNumber The employee number
     * @param timeIn The clock in time
     */
    public static void logClockIn(User user, Integer employeeNumber, String timeIn) {
        String details = "Employee #" + employeeNumber + " clocked in at " + timeIn;
        logAction(user, "Clock in", details);
    }

    /**
     * Log clock out action.
     * 
     * @param user The user who clocked out
     * @param employeeNumber The employee number
     * @param timeOut The clock out time
     */
    public static void logClockOut(User user, Integer employeeNumber, String timeOut) {
        String details = "Employee #" + employeeNumber + " clocked out at " + timeOut;
        logAction(user, "Clock out", details);
    }
}

