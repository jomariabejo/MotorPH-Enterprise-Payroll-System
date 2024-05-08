/*
File: update_department.sql
Description: Modifies an existing record in the DEPARTMENT table by updating the name and description fields using the department_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Updates a record in the DEPARTMENT table
-- Parameters:
--   dept_id: The ID of the department to be modified.
--   name: The new name for the department.
--   description: The new description for the department.


UPDATE DEPARTMENT
SET
    name = ?,
    description = ?,

WHERE
    dept_id = ?;
