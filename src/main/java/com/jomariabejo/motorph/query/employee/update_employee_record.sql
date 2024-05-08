/*
Title: Update Employee Information
File: update_employee_info.sql
Description: Modifies employee information in the EMPLOYEE table based on the employee ID.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 10, 2024
Version: 1.0
*/
-- Update employee information in the EMPLOYEE table.
-- Parameters:
--   - first_name: The updated first name of the employee.
--   - last_name: The updated last name of the employee.
--   - birthday: The updated birthday of the employee.
--   - address: The updated address of the employee.
--   - contact_number: The updated contact number of the employee.
--   - status: The updated status of the employee.
--   - date_hired: The updated date when the employee was hired.
--   - position_id: The updated position ID of the employee.
--   - supervisor: The updated supervisor of the employee.
--   - dept_id: The updated department ID of the employee.
--   - sss: The updated Social Security System number of the employee.
--   - philhealth: The updated PhilHealth number of the employee.
--   - pagibig: The updated Pag-IBIG number of the employee.
--   - tin: The updated Tax Identification Number of the employee.
--   - basic_salary: The updated basic salary of the employee.
--   - gross_semi_monthly_rate: The updated gross semi-monthly rate of the employee.
--   - hourly_rate: The updated hourly rate of the employee.
--   - employee_id: The ID of the employee to be updated.
UPDATE
    employee
SET
    first_name = ?,
    last_name = ?,
    birthday = ?,
    address = ?,
    contact_number = ?,
    status = ?,
    date_hired = ?,
    position_id = ?,
    supervisor = ?,
    dept_id = ?,
    sss = ?,
    philhealth = ?,
    pagibig = ?,
    tin = ?,
    basic_salary = ?,
    gross_semi_monthly_rate = ?,
    hourly_rate = ?
WHERE
    employee_id = ?;
