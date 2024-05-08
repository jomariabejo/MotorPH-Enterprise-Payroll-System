SELECT COALESCE(lrc.maxCredits, 0) - IFNULL(SUM(DATEDIFF(
        CASE WHEN lr.start_date = lr.end_date THEN lr.start_date ELSE DATE_ADD(lr.end_date, INTERVAL 1 DAY) END,
        lr.start_date)), 0) AS remaining_leave_balance
FROM leave_request_category lrc
         LEFT JOIN leave_request lr ON lr.leave_request_category_id = lrc.leave_req_cat_id
    AND lr.employee_id = ?
    AND lr.status = 'Approved'
    AND YEAR(lr.start_date) = YEAR(CURDATE())
WHERE lrc.leave_req_cat_id = ?;
