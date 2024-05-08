/*
File: update_allowance.sql
Description: Updates a record in the ALLOWANCE table based on alw_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/
-- Updates a record in the ALLOWANCE table
-- Columns to be updated:
--   - 'rice': Updates the rice allowance for the employee.
--   - 'phone': Updates the phone allowance for the employee.
--   - 'total_amount': Updates the total sum of allowances provided.
--   - 'date_created': Updates the timestamp for when the record was initially created.
--   - 'date_modified': Updates the timestamp for the last modification of the record.
--   - 'employee_id': Updates the foreign key referencing the employee associated with these allowances.
-- Condition:
--   - 'alw_id': Specifies the unique identifier of the allowance record to be updated.
UPDATE
    ALLOWANCE
SET
    rice = ?,
    phone = ?,
    total_amount = ?,
    dateCreated = ?,
    dateModified = ?,
    employee_id = ?
WHERE
    alw_id = ?;
