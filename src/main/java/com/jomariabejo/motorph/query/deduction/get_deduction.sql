/*
File: select_deduction.sql
Description: Retrieves a record from the DEDUCTION table based on deduction_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Retrieves a record from the DEDUCTION table
-- Columns:
--   - '*': Selects all columns from the DEDUCTION table.
-- Condition:
--   - 'employee_id': Specifies the unique identifier of the deduction record to be retrieved.
--   - 'date_created': Specifies the date when the deduction generated.

SELECT
    *
FROM
    DEDUCTION
WHERE
    employee_id = ? AND date_created = ?;
