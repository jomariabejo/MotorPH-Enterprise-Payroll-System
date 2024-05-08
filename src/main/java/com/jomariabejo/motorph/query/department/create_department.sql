/*
File: insert_department.sql
Description: Inserts a new record into the DEPARTMENT table.
Author: Jomari Abejo
Date Created: April 10, 2024
Last Modified: April 10,2024
Version: 1.0
*/
-- Inserts a new record into the DEPARTMENT table
-- Columns:
--   - 'name': Represents name of department.
--   - 'description': The details of department.
--   - 'date_created': The department's founding date.
INSERT INTO DEPARTMENT (name, description, date_created)
VALUES
    (
        ?,  -- name
        ?,  -- description
        ?,  -- date_created
    );
