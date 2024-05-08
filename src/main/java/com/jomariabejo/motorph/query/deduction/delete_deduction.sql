/*
File: delete_deduction.sql
Description: Deletes a record from the DEDUCTION table based on deduction_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Deletes a record from the DEDUCTION table
-- Condition:
--   - 'deduction_id': Specifies the unique identifier of the deduction record to be deleted.

DELETE FROM
    DEDUCTION
WHERE
    deduction_id = ?;
