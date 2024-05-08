/*
File: delete_allowance.sql
Description: Deletes a record from the ALLOWANCE table based on alw_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Deletes a record from the ALLOWANCE table
-- Condition:
--   - 'alw_id': Specifies the unique identifier of the allowance record to be deleted.

DELETE FROM
    ALLOWANCE
WHERE
    alw_id = ?;
