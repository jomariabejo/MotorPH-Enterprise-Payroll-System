-- Filename: get_timesheets_by_month_and_year.sql
-- This query retrieves all timesheets for a specific month and year
SELECT
    *
FROM
    timesheet
WHERE
    MONTH(date) = ?
  AND YEAR(date) = ?;
