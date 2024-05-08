/*
File: check_leave_balance.sql
Description: Checks if the employee has enough leave balance for a leave request.
Author: Jomari Abejo
Date Created: March 30, 2024
Last Modified: April 12, 2024
Version: 1.0
*/

SELECT CASE
           WHEN total_leave_days >= ? THEN 'insufficient balance'
           ELSE 'sufficient balance'
           END AS is_balance_sufficient
FROM (SELECT SUM(
                     CASE
                         WHEN end_date = start_date
                             THEN 1 -- Count 1 day for leave requests with the same start and end dates
                         ELSE DATEDIFF(end_date, start_date) -- Calculate the number of days for leave requests with different start and end dates
                         END
             ) AS total_leave_days
      FROM payroll_sys.leave_request
      WHERE employee_id = ? -- ID of the employee making the leave request
        AND leave_request_category_id = ? -- ID of the leave request category (e.g., Sick = 5, Emergency = 5, Vacation = 10)
     ) AS total_days;