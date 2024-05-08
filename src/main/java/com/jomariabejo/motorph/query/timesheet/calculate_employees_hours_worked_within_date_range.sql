/*
File: calculate_employees_hours_worked_within_date_range.sql
Description: Calculates the total hours worked by all active employees within a given date range.
Author: OpenAI ChatGPT
Date Created: N/A
Last Modified: May 4, 2024
Version: 1.0
*/

-- Calculates the total hours worked by all active employees within a given date range
-- Parameters:
--   - 'start_date': The start date of the date range.
--   - 'end_date': The end date of the date range.

SELECT
    t.employee_id,
    SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)))) AS total_hours_worked
FROM
    timesheet t
        JOIN employee e ON t.employee_id = e.employee_id
WHERE
    e.isActive = 1
  AND t.DATE BETWEEN ? AND ?
GROUP BY
    t.employee_id;
