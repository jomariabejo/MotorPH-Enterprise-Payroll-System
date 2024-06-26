/*
File: calculate_employee_regular_hours_worked.sql
Description: Calculates the total regular hours worked by an employee within a given date range.
Date Created: N/A
Last Modified: May 4, 2024
Version: 1.0
*/

-- Calculates the total regular hours worked by an employee within a given date range
-- Parameters:
--   - 'employee_id': The ID of the employee whose hours are being calculated.
--   - 'start_date': The start date of the date range.
--   - 'end_date': The end date of the date range.

-- Query for calculating regular hours worked

SELECT
    t.employee_id,
    SUM(
            CASE
                WHEN TIME(t.time_in) < '08:00:00' THEN (TIME_TO_SEC(TIMEDIFF('08:00:00', t.time_in)) / 3600.0)
            WHEN TIME(t.time_out) > '17:00:00' THEN (TIME_TO_SEC(TIMEDIFF('17:00:00', t.time_in)) / 3600.0)
            ELSE (TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)) / 3600.0)
            END
    ) AS regular_hours_worked
FROM
    timesheet t
        JOIN
    employee e ON t.employee_id = e.employee_id
WHERE
    e.isActive = 1
  AND t.DATE BETWEEN ? AND ?
GROUP BY
    t.employee_id;