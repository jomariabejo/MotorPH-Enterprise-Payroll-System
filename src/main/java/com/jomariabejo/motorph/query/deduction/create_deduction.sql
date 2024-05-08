/*
File: insert_deduction.sql
Description: Inserts a new record into the DEDUCTION table.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10,2024
Version: 1.0
*/

-- Inserts a new record into the DEDUCTION table
-- Columns:
--   - 'employee_id': Specifies the employee ID associated with the deduction.
--   - 'sss': Represents the Social Security System (SSS) deduction for the employee.
--   - 'philhealth': Represents the PhilHealth deduction for the employee.
--   - 'pagibig': Represents the Pag-IBIG Fund deduction for the employee.
--   - 'total_deduction': Represents the total deduction amount for the employee.
--   - 'date_created': Timestamp for when the deduction record was created.

INSERT INTO DEDUCTION (
    employee_id, sss, philhealth, pagibig,
    total_deduction, date_created
)
VALUES (
           ?, -- employee_id
           ?, -- sss
           ?, -- philhealth
           ?, -- pagibig
           ?, -- total_deduction
           ?  -- date_created
       );
