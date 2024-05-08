/*
File: retrieve_employee_timesheets.sql
Description: Retrieves timesheets for a specific employee within a given date range.
Author: Jomari Abejo
Date Created: 04/05/2024
Last Modified: 04/05/2024
Version: 1.0
*/

-- Retrieves timesheets for a specific employee within a given date range
-- Parameters:
--   - 'employee_id': The ID of the employee whose timesheets are being retrieved.
--   - 'start_date': The start date of the date range.
--   - 'end_date': The end date of the date range.

SELECT *
FROM payroll_sys.timesheet
WHERE employee_id = ? AND payroll_sys.timesheet.date BETWEEN ? AND ?;
