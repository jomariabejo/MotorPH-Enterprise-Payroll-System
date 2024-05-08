/*
File: create_employee.sql
Description: Inserts a new record into the EMPLOYEE table.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/
-- Inserts a new record into the EMPLOYEE table
-- Columns:
--   - 'first_name': The first name of the employee.
--   - 'last_name': The last name of the employee.
--   - 'birthday': The birthday of the employee.
--   - 'address': The address of the employee.
--   - 'contact_number': The contact number of the employee.
--   - 'status': The status of the employee (Regular, Probationary, Contract).
--   - 'date_hired': The date when the employee was hired.
--   - 'position_id': The foreign key referencing the position ID of the employee.
--   - 'supervisor': The supervisor of the employee.
--   - 'dept_id': The foreign key referencing the department ID of the employee.
--   - 'sss': The Social Security System number of the employee.
--   - 'philhealth': The PhilHealth number of the employee.
--   - 'pagibig': The Pag-IBIG number of the employee.
--   - 'tin': The Tax Identification Number of the employee.
--   - 'basic_salary': The basic salary of the employee.
--   - 'gross_semi_monthly_rate': The gross semi-monthly rate of the employee.
--   - 'hourly_rate': The hourly rate of the employee.

INSERT INTO EMPLOYEE (
    first_name, last_name, birthday, address,
    contact_number, status, date_hired,
    position_id, supervisor, dept_id,
    sss, philhealth, pagibig, tin, basic_salary,
    gross_semi_monthly_rate, hourly_rate
)
VALUES
    (
        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
        ?
    );
