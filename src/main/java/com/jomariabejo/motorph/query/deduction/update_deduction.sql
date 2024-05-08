/*
File: update_deduction.sql
Description: Updates a record in the DEDUCTION table based on deduction_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Updates a record in the DEDUCTION table
-- Columns to be updated:
--   - 'employee_id': Updates the employee ID associated with the deduction.
--   - 'sss': Updates the Social Security System (SSS) deduction for the employee.
--   - 'philhealth': Updates the PhilHealth deduction for the employee.
--   - 'pagibig': Updates the Pag-IBIG Fund deduction for the employee.
--   - 'total_deduction': Updates the total deduction amount for the employee.
--   - 'date_created': Updates the timestamp for when the deduction record was created.
-- Condition:
--   - 'deduction_id': Specifies the unique identifier of the deduction record to be updated.

UPDATE
    DEDUCTION
SET
    employee_id = ?,
    sss = ?,
    philhealth = ?,
    pagibig = ?,
    total_deduction = ?,
    date_created = ?
WHERE
    deduction_id = ?;
