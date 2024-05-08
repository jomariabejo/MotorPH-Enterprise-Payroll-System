/*
File: delete_department.sql
Description: Deletes existing record from DEPARTMENT table by utilizing department_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Deletes a record from the DEPARTMENT table
-- Parameters:
--   dept_id: The ID of the department to be deleted

SELECT name FROM DEPARTMENT WHERE dept_id = ?