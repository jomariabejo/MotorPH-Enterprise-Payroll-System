/*
File: get_employee_by_id_inactive.sql
Description: Retrieves inactive employee information from the EMPLOYEE table by employee ID.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Retrieve active employee information from the EMPLOYEE table by employee ID.
SELECT *FROM EMPLOYEE WHERE employee_id = ? AND payroll_sys.employee.isActive = 0;