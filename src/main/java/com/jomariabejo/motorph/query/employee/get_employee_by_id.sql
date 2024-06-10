/*
File: get_employee_by_id.sql
Description: Retrieves employee information from the EMPLOYEE table based on the employee ID.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Retrieve employee information from the EMPLOYEE table by employee ID.
SELECT *
FROM EMPLOYEE
WHERE employee_id = ?;
