/*
File: count_total_employee_work_days_within_date_range.sql
Description: Counts the total number of days each active employee worked within a given date range.
Author: OpenAI ChatGPT
Date Created: N/A
Last Modified: May 4, 2024
Version: 1.0
*/

-- Counts the total number of days each active employee worked within a given date range
-- Parameters:
--   - 'start_date': The start date of the date range.
--   - 'end_date': The end date of the date range.

SELECT
    t.employee_id,
    COUNT(DISTINCT DATE(t.date)) AS total_days_worked
FROM
    timesheet t
        JOIN employee e ON t.employee_id = e.employee_id
WHERE
    e.isActive = 1
  AND t.date BETWEEN '2024-04-01' AND '2024-04-30'
GROUP BY
    t.employee_id;
