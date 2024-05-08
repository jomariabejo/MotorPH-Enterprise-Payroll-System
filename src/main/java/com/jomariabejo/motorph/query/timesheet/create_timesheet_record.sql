SELECT
    *
FROM
    timesheet
WHERE
    date BETWEEN
        ? -- ex: 2024-01-01 00:00:00
        AND
        ? -- ex: 2024-01-31 23:59:59
  AND employee_id = ?;
