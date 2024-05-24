-- This SQL query calculates the remaining leave balance for a specific employee (`employee_id = 8`)
-- and a particular type of leave (`leave_req_cat_id = 1`) for the current year.

-- Selecting data from the tables `leave_request_category` and `leave_request`
SELECT
    -- Calculating the remaining leave balance
    COALESCE(lrc.maxCredits, 0) - IFNULL(SUM(DATEDIFF(
        -- Calculating the difference between start and end dates for each leave request
        CASE WHEN lr.start_date = lr.end_date THEN lr.start_date -- If start date equals end date, count as 1 day
             ELSE DATE_ADD(lr.end_date, INTERVAL 1 DAY) END, lr.start_date)), 0) AS remaining_leave_balance
-- From clause: Joining the `leave_request_category` table aliased as `lrc` with the `leave_request` table aliased as `lr`
FROM leave_request_category lrc
         -- Left join to include all leave categories, even if no leave requests are associated
         LEFT JOIN leave_request lr ON lr.leave_request_category_id = lrc.leave_req_cat_id
    -- Additional conditions for the join
    AND lr.employee_id = 8 -- Filtering leave requests for the employee with ID 8
    AND lr.status = 'Approved' -- Filtering only the leave requests that have been approved
    AND YEAR(lr.start_date) = YEAR(CURDATE()) -- Filtering leave requests that occurred in the current year
-- Where clause: Further filtering the data to include only records for the specific leave type
WHERE lrc.leave_req_cat_id = 1; -- Filtering for the leave type with ID 1
