/*
File: calculate_total_employee_gross_salary_within_date_range.sql
Description: Calculates the total gross salary earned by all active employees within a given date range based on their hourly rate and hours worked.
Author: OpenAI ChatGPT
Date Created: N/A
Last Modified: May 4, 2024
Version: 1.1
*/

-- Calculates the total gross salary earned by all active employees within a given date range
-- Parameters:
--   - 'start_date': The start date of the date range.
--   - 'end_date': The end date of the date range.

SELECT
    t.employee_id,
    SEC_TO_TIME(SUM(TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)))) AS total_hours_worked,
    ROUND(SUM(TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in))) / 3600 * e.hourly_rate, 2) AS total_gross_salary
FROM
    timesheet t
        JOIN employee e ON t.employee_id = e.employee_id
WHERE
    e.isActive = 1
  AND t.DATE BETWEEN '2024-04-01' AND '2024-04-30'
GROUP BY
    t.employee_id;
