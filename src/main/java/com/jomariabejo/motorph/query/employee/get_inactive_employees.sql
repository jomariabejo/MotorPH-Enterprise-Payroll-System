/*
File: get_inactive_employees.sql
Description: Extract all of the inactive employees from the EMPLOYEE table..
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Get all records from the EMPLOYEE table.
SELECT * FROM EMPLOYEE where payroll_sys.employee.isActive = 0;
