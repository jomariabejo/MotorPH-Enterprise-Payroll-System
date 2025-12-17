package com.jomariabejo.motorph.sampler;

import com.jomariabejo.motorph.HibernateUtil;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.repository.EmployeeRepository;
import com.jomariabejo.motorph.repository.LeaveBalanceRepository;
import com.jomariabejo.motorph.repository.LeaveRequestTypeRepository;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.LeaveBalanceService;
import com.jomariabejo.motorph.service.LeaveRequestTypeService;

import java.util.List;

/**
 * Utility class to initialize leave balances for all existing employees.
 * 
 * This can be run as a standalone program to initialize leave balances
 * for employees who were created before the automatic initialization feature.
 * 
 * Usage:
 *   Run this class as a Java application, or execute:
 *   java -cp ... com.jomariabejo.motorph.sampler.InitializeLeaveBalances
 */
public class InitializeLeaveBalances {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Leave Balance Initialization Utility");
        System.out.println("==========================================");
        System.out.println();
        
        try {
            // Initialize Hibernate
            System.out.println("Initializing database connection...");
            HibernateUtil.getSessionFactory();
            System.out.println("✓ Database connection established");
            System.out.println();
            
            // Initialize services
            LeaveRequestTypeService leaveRequestTypeService = new LeaveRequestTypeService(new LeaveRequestTypeRepository());
            LeaveBalanceService leaveBalanceService = new LeaveBalanceService(
                    new LeaveBalanceRepository(), 
                    leaveRequestTypeService
            );
            EmployeeService employeeService = new EmployeeService(new EmployeeRepository());
            
            // Get all employees
            System.out.println("Fetching all employees...");
            List<Employee> employees = employeeService.getAllEmployees();
            System.out.println("✓ Found " + employees.size() + " employees");
            System.out.println();
            
            if (employees.isEmpty()) {
                System.out.println("⚠ No employees found in the database.");
                System.out.println("Nothing to initialize.");
                return;
            }
            
            // Check existing balances
            System.out.println("Checking existing leave balances...");
            int employeesWithBalances = 0;
            int employeesWithoutBalances = 0;
            
            for (Employee employee : employees) {
                var balances = leaveBalanceService.fetchEmployeeRemainingLeaveBalances(employee);
                if (balances.isPresent() && !balances.get().isEmpty()) {
                    employeesWithBalances++;
                } else {
                    employeesWithoutBalances++;
                }
            }
            
            System.out.println("✓ Employees with leave balances: " + employeesWithBalances);
            System.out.println("✓ Employees without leave balances: " + employeesWithoutBalances);
            System.out.println();
            
            if (employeesWithoutBalances == 0) {
                System.out.println("✓ All employees already have leave balances initialized!");
                System.out.println("No action needed.");
                return;
            }
            
            // Initialize leave balances
            System.out.println("Initializing leave balances for employees without them...");
            System.out.println("Default allocations:");
            System.out.println("  - Sick Leave: 5 days");
            System.out.println("  - Vacation Leave: 10 days");
            System.out.println("  - Emergency Leave: 5 days");
            System.out.println();
            
            leaveBalanceService.initializeLeaveBalancesForAllEmployees(employees);
            
            System.out.println("✓ Leave balances initialized successfully!");
            System.out.println();
            
            // Verify results
            System.out.println("Verifying results...");
            int successCount = 0;
            for (Employee employee : employees) {
                var balances = leaveBalanceService.fetchEmployeeRemainingLeaveBalances(employee);
                if (balances.isPresent() && !balances.get().isEmpty()) {
                    successCount++;
                }
            }
            
            System.out.println("✓ " + successCount + " out of " + employees.size() + " employees now have leave balances");
            System.out.println();
            
            // Display summary
            System.out.println("==========================================");
            System.out.println("Summary:");
            System.out.println("  Total employees: " + employees.size());
            System.out.println("  Employees with balances (before): " + employeesWithBalances);
            System.out.println("  Employees initialized: " + employeesWithoutBalances);
            System.out.println("  Employees with balances (after): " + successCount);
            System.out.println("==========================================");
            System.out.println();
            System.out.println("✓ Initialization complete!");
            System.out.println();
            System.out.println("Employees can now:");
            System.out.println("  - View their leave balances");
            System.out.println("  - File leave requests");
            System.out.println("  - See the leave balance chart");
            
        } catch (Exception e) {
            System.err.println("❌ Error occurred during initialization:");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            // Shutdown Hibernate
            System.out.println();
            System.out.println("Closing database connection...");
            HibernateUtil.shutdown();
            System.out.println("✓ Done");
        }
    }
}

