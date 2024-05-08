/*
File: create_employee.sql
Description: Deletes a record from the EMPLOYEE table where employee_id matches the provided value.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Deletes a record from the EMPLOYEE table where employee_id matches the provided value.
-- Parameters:
--   employee_id: The ID of the employee to be deleted


DELETE FROM employee WHERE employee_id = ?;
