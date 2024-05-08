/*
File: insert_allowance.sql
Description: Inserts a new record into the ALLOWANCE table.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10, 2024
Version: 1.0
*/
-- Inserts a new record into the ALLOWANCE table
-- Columns:
--   - 'clothing': Represents the clothing allowance provided to the employee.
--   - 'rice': Denotes the allowance for rice allocation.
--   - 'phone': Signifies the phone allowance for the employee.
--   - 'total_amount': Indicates the total sum of allowances provided.
--   - 'dateCreated': Timestamp for when the record was initially created.
--   - 'dateModified': Timestamp for the last modification of the record.
--   - 'employee_id': Foreign key referencing the employee associated with these allowances.
INSERT INTO ALLOWANCE (
    clothing, rice, phone, total_amount,
    dateCreated, dateModified, employee_id
)
VALUES
    (
     ?, --  clothing
     ?, --  rice
     ?, --  phone
     ?, --  total_amount
     ?, --  dateCreated
     ?, --  dateModified
     ?  --  employee_id
    );
