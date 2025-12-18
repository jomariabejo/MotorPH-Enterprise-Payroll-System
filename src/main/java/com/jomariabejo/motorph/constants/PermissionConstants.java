package com.jomariabejo.motorph.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Constants for all system permissions.
 * Maps permissions to navigation items and UI actions for enterprise-level RBAC.
 */
public class PermissionConstants {
    
    // Private constructor to prevent instantiation
    private PermissionConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
    
    // ==================== EMPLOYEE PERMISSIONS ====================
    public static final String EMPLOYEE_PROFILE_VIEW = "employee.profile.view";
    public static final String EMPLOYEE_LEAVE_REQUEST = "employee.leave.request";
    public static final String EMPLOYEE_LEAVE_BALANCE_VIEW = "employee.leave.balance.view";
    public static final String EMPLOYEE_LEAVE_HISTORY_VIEW = "employee.leave.history.view";
    public static final String EMPLOYEE_TIMESHEET_VIEW = "employee.timesheet.view";
    public static final String EMPLOYEE_OVERTIME_REQUEST = "employee.overtime.request";
    public static final String EMPLOYEE_PAYSLIP_VIEW = "employee.payslip.view";
    public static final String EMPLOYEE_REIMBURSEMENT_REQUEST = "employee.reimbursement.request";
    public static final String EMPLOYEE_CONTACT_SUPPORT = "employee.contact.support";
    public static final String EMPLOYEE_NOTIFICATIONS_VIEW = "employee.notifications.view";
    public static final String EMPLOYEE_MESSAGES_VIEW = "employee.messages.view";
    
    // ==================== HR ADMINISTRATOR PERMISSIONS ====================
    public static final String HR_DASHBOARD_VIEW = "hr.dashboard.view";
    public static final String HR_EMPLOYEES_MANAGE = "hr.employees.manage";
    public static final String HR_EMPLOYEES_VIEW = "hr.employees.view";
    public static final String HR_EMPLOYEES_CREATE = "hr.employees.create";
    public static final String HR_EMPLOYEES_EDIT = "hr.employees.edit";
    public static final String HR_EMPLOYEES_DELETE = "hr.employees.delete";
    public static final String HR_TIMESHEETS_VIEW = "hr.timesheets.view";
    public static final String HR_TIMESHEETS_MANAGE = "hr.timesheets.manage";
    public static final String HR_LEAVE_REQUESTS_VIEW = "hr.leave.requests.view";
    public static final String HR_LEAVE_REQUESTS_APPROVE = "hr.leave.requests.approve";
    public static final String HR_LEAVE_REQUESTS_REJECT = "hr.leave.requests.reject";
    public static final String HR_OVERTIME_REQUESTS_VIEW = "hr.overtime.requests.view";
    public static final String HR_OVERTIME_REQUESTS_APPROVE = "hr.overtime.requests.approve";
    public static final String HR_ANNOUNCEMENTS_MANAGE = "hr.announcements.manage";
    
    // ==================== PAYROLL/ACCOUNTING ADMINISTRATOR PERMISSIONS ====================
    public static final String PAYROLL_DASHBOARD_VIEW = "payroll.dashboard.view";
    public static final String PAYROLL_MANAGE = "payroll.manage";
    public static final String PAYROLL_APPROVE = "payroll.approve";
    public static final String PAYROLL_TRANSACTIONS_VIEW = "payroll.transactions.view";
    public static final String PAYROLL_TRANSACTIONS_MANAGE = "payroll.transactions.manage";
    public static final String PAYROLL_OVERTIME_APPROVE = "payroll.overtime.approve";
    public static final String PAYROLL_PAYSLIP_VIEW = "payroll.payslip.view";
    public static final String PAYROLL_PAYSLIP_HISTORY_VIEW = "payroll.payslip.history.view";
    public static final String PAYROLL_BONUS_MANAGE = "payroll.bonus.manage";
    public static final String PAYROLL_TIN_COMPLIANCE_MANAGE = "payroll.tin.compliance.manage";
    public static final String PAYROLL_PAGIBIG_RATES_MANAGE = "payroll.pagibig.rates.manage";
    public static final String PAYROLL_PHILHEALTH_RATES_MANAGE = "payroll.philhealth.rates.manage";
    public static final String PAYROLL_SSS_RATES_MANAGE = "payroll.sss.rates.manage";
    public static final String PAYROLL_CHANGES_MANAGE = "payroll.changes.manage";
    public static final String PAYROLL_REIMBURSEMENT_MANAGE = "payroll.reimbursement.manage";
    public static final String PAYROLL_REIMBURSEMENT_TRANSACTIONS_VIEW = "payroll.reimbursement.transactions.view";
    public static final String PAYROLL_REIMBURSEMENT_TRANSACTIONS_MANAGE = "payroll.reimbursement.transactions.manage";
    
    // ==================== SYSTEM ADMINISTRATOR PERMISSIONS ====================
    public static final String SYSTEM_ADMIN_DASHBOARD_VIEW = "system.admin.dashboard.view";
    public static final String SYSTEM_ADMIN_USERS_MANAGE = "system.admin.users.manage";
    public static final String SYSTEM_ADMIN_USERS_VIEW = "system.admin.users.view";
    public static final String SYSTEM_ADMIN_USERS_CREATE = "system.admin.users.create";
    public static final String SYSTEM_ADMIN_USERS_EDIT = "system.admin.users.edit";
    public static final String SYSTEM_ADMIN_USERS_DELETE = "system.admin.users.delete";
    public static final String SYSTEM_ADMIN_LOGS_VIEW = "system.admin.logs.view";
    public static final String SYSTEM_ADMIN_ROLES_MANAGE = "system.admin.roles.manage";
    public static final String SYSTEM_ADMIN_ROLES_VIEW = "system.admin.roles.view";
    public static final String SYSTEM_ADMIN_ROLES_CREATE = "system.admin.roles.create";
    public static final String SYSTEM_ADMIN_ROLES_EDIT = "system.admin.roles.edit";
    public static final String SYSTEM_ADMIN_ROLES_DELETE = "system.admin.roles.delete";
    public static final String SYSTEM_ADMIN_PERMISSIONS_MANAGE = "system.admin.permissions.manage";
    public static final String SYSTEM_ADMIN_PERMISSIONS_VIEW = "system.admin.permissions.view";
    public static final String SYSTEM_ADMIN_PERMISSIONS_CREATE = "system.admin.permissions.create";
    public static final String SYSTEM_ADMIN_PERMISSIONS_EDIT = "system.admin.permissions.edit";
    public static final String SYSTEM_ADMIN_PERMISSIONS_DELETE = "system.admin.permissions.delete";
    public static final String SYSTEM_ADMIN_ROLE_PERMISSIONS_MANAGE = "system.admin.role.permissions.manage";
    public static final String SYSTEM_ADMIN_ROLE_PERMISSIONS_VIEW = "system.admin.role.permissions.view";
    public static final String SYSTEM_ADMIN_ROLE_PERMISSIONS_ASSIGN = "system.admin.role.permissions.assign";
    public static final String SYSTEM_ADMIN_ROLE_PERMISSIONS_REMOVE = "system.admin.role.permissions.remove";
    public static final String SYSTEM_ADMIN_ANNOUNCEMENTS_MANAGE = "system.admin.announcements.manage";
    
    // ==================== NAVIGATION PERMISSION MAPPINGS ====================
    // Maps navigation action methods to required permissions
    private static final Map<String, String> NAVIGATION_PERMISSION_MAP = new HashMap<>();
    
    static {
        // Employee Navigation
        NAVIGATION_PERMISSION_MAP.put("myProfileOnAction", EMPLOYEE_PROFILE_VIEW);
        NAVIGATION_PERMISSION_MAP.put("fileLeaveRequestOnAction", EMPLOYEE_LEAVE_REQUEST);
        NAVIGATION_PERMISSION_MAP.put("viewLeaveBalanceOnAction", EMPLOYEE_LEAVE_BALANCE_VIEW);
        NAVIGATION_PERMISSION_MAP.put("viewLeaveHistoryOnAction", EMPLOYEE_LEAVE_HISTORY_VIEW);
        NAVIGATION_PERMISSION_MAP.put("viewTimesheetOnAction", EMPLOYEE_TIMESHEET_VIEW);
        NAVIGATION_PERMISSION_MAP.put("overtimeOnAction", EMPLOYEE_OVERTIME_REQUEST);
        NAVIGATION_PERMISSION_MAP.put("payslipOnAction", EMPLOYEE_PAYSLIP_VIEW);
        NAVIGATION_PERMISSION_MAP.put("reimbursementOnAction", EMPLOYEE_REIMBURSEMENT_REQUEST);
        NAVIGATION_PERMISSION_MAP.put("contactSupportOnAction", EMPLOYEE_CONTACT_SUPPORT);
        NAVIGATION_PERMISSION_MAP.put("notificationOnAction", EMPLOYEE_NOTIFICATIONS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("conversationOnAction", EMPLOYEE_MESSAGES_VIEW);
        
        // HR Navigation
        NAVIGATION_PERMISSION_MAP.put("humanResourceDashboardOnAction", HR_DASHBOARD_VIEW);
        NAVIGATION_PERMISSION_MAP.put("employeesOnAction", HR_EMPLOYEES_VIEW);
        NAVIGATION_PERMISSION_MAP.put("timesheetsOnAction", HR_TIMESHEETS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("leaveRequestsOnAction", HR_LEAVE_REQUESTS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("overtimeRequestsOnAction", HR_OVERTIME_REQUESTS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("announcementOnAction", HR_ANNOUNCEMENTS_MANAGE);
        
        // Payroll/Accounting Navigation
        NAVIGATION_PERMISSION_MAP.put("dashboardOnActtion", PAYROLL_DASHBOARD_VIEW);
        NAVIGATION_PERMISSION_MAP.put("payrollOnAction", PAYROLL_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("payrollApprovalOnAction", PAYROLL_APPROVE);
        NAVIGATION_PERMISSION_MAP.put("payrollTransactionOnAction", PAYROLL_TRANSACTIONS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("overtimeOnAction", PAYROLL_OVERTIME_APPROVE);
        NAVIGATION_PERMISSION_MAP.put("payslipOnAction", PAYROLL_PAYSLIP_VIEW);
        // Payslip History button removed
        NAVIGATION_PERMISSION_MAP.put("bonusOnAction", PAYROLL_BONUS_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("tinComplianceOnAction", PAYROLL_TIN_COMPLIANCE_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("pagibigRateOnAction", PAYROLL_PAGIBIG_RATES_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("philhealthRateOnAction", PAYROLL_PHILHEALTH_RATES_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("sssRateOnAction", PAYROLL_SSS_RATES_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("payrollChangeOnAction", PAYROLL_CHANGES_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("reimbursementOnAction", PAYROLL_REIMBURSEMENT_MANAGE);
        NAVIGATION_PERMISSION_MAP.put("reimbursementTransactionOnAction", PAYROLL_REIMBURSEMENT_TRANSACTIONS_VIEW);
        
        // System Admin Navigation
        NAVIGATION_PERMISSION_MAP.put("dashboard", SYSTEM_ADMIN_DASHBOARD_VIEW);
        NAVIGATION_PERMISSION_MAP.put("users", SYSTEM_ADMIN_USERS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("logs", SYSTEM_ADMIN_LOGS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("roles", SYSTEM_ADMIN_ROLES_VIEW);
        NAVIGATION_PERMISSION_MAP.put("permissions", SYSTEM_ADMIN_PERMISSIONS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("rolePermission", SYSTEM_ADMIN_ROLE_PERMISSIONS_VIEW);
        NAVIGATION_PERMISSION_MAP.put("announcements", SYSTEM_ADMIN_ANNOUNCEMENTS_MANAGE);
    }
    
    // ==================== MAIN MENU PERMISSION MAPPINGS ====================
    // Maps main menu items to required permissions
    private static final Map<String, String> MAIN_MENU_PERMISSION_MAP = new HashMap<>();
    
    static {
        MAIN_MENU_PERMISSION_MAP.put("menuItemEmployeeOnAction", EMPLOYEE_PROFILE_VIEW);
        MAIN_MENU_PERMISSION_MAP.put("menuItemHumanResourceOnAction", HR_DASHBOARD_VIEW);
        MAIN_MENU_PERMISSION_MAP.put("menuItemAccountingOnAction", PAYROLL_DASHBOARD_VIEW);
        MAIN_MENU_PERMISSION_MAP.put("menuISystemAdminOnAction", SYSTEM_ADMIN_DASHBOARD_VIEW);
    }
    
    /**
     * Get the required permission for a navigation action method name.
     * 
     * @param actionMethodName The method name (e.g., "myProfileOnAction")
     * @return The required permission, or null if not found
     */
    public static String getPermissionForNavigation(String actionMethodName) {
        return NAVIGATION_PERMISSION_MAP.get(actionMethodName);
    }
    
    /**
     * Get the required permission for a main menu item.
     * 
     * @param menuItemMethodName The menu item method name (e.g., "menuItemEmployeeOnAction")
     * @return The required permission, or null if not found
     */
    public static String getPermissionForMainMenu(String menuItemMethodName) {
        return MAIN_MENU_PERMISSION_MAP.get(menuItemMethodName);
    }
    
    /**
     * Get all navigation permission mappings.
     * 
     * @return Map of navigation actions to permissions
     */
    public static Map<String, String> getNavigationPermissionMap() {
        return new HashMap<>(NAVIGATION_PERMISSION_MAP);
    }
    
    /**
     * Get all main menu permission mappings.
     * 
     * @return Map of main menu items to permissions
     */
    public static Map<String, String> getMainMenuPermissionMap() {
        return new HashMap<>(MAIN_MENU_PERMISSION_MAP);
    }
}






