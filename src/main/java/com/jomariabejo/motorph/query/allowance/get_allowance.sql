/*
File: select_allowance.sql
Description: Retrieves a record from the ALLOWANCE table based on alw_id.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/

-- Retrieves a record from the ALLOWANCE table
-- Columns:
--   - '*': Selects all columns from the ALLOWANCE table.
-- Condition:
--   - 'alw_id': Specifies the unique identifier of the allowance record to be retrieved.

SELECT
    *
FROM
    ALLOWANCE
WHERE
    alw_id = ?;
