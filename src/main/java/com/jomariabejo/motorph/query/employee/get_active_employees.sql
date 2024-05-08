/*
File: get_active_employees.sql
Description: Extract all of the active employees from the EMPLOYEE table.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Get all records from the EMPLOYEE table.
SELECT * FROM EMPLOYEE WHERE payroll_sys.employee.isActive = 1;
