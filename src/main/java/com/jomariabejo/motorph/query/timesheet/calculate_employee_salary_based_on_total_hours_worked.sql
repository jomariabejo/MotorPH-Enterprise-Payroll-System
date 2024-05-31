SELECT
    t.employee_id,
    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
    p.name AS position ,
    d.name AS department ,
    e.basic_salary,
    e.hourly_rate,
        SUM(
                CASE WHEN TIME(t.time_in) < '08:00:00' THEN (
            TIME_TO_SEC(
                    TIMEDIFF('08:00:00', t.time_in)
            ) / 3600.0
            ) WHEN TIME(t.time_out) > '17:00:00' THEN (
                    TIME_TO_SEC(
                            TIMEDIFF('17:00:00', t.time_in)
                    ) / 3600.0
                    ) ELSE (
                    TIME_TO_SEC(
                            TIMEDIFF(t.time_out, t.time_in)
                    ) / 3600.0
                    ) END
        ) AS total_regular_hours_worked,
        SUM(
                CASE WHEN t.time_out <= '17:00:00' THEN 0 ELSE (
                    TIME_TO_SEC(
                            TIMEDIFF(t.time_out, '17:00:00')
                    ) / 3600.0
                    ) END
        ) AS total_overtime_hours,
        ROUND(
                SUM(
                        CASE WHEN TIME(t.time_in) < '08:00:00' THEN (
                    TIME_TO_SEC(
                            TIMEDIFF('08:00:00', t.time_in)
                    ) / 3600.0
                    ) WHEN TIME(t.time_out) > '17:00:00' THEN (
                            TIME_TO_SEC(
                                    TIMEDIFF('17:00:00', t.time_in)
                            ) / 3600.0
                            ) ELSE (
                            TIME_TO_SEC(
                                    TIMEDIFF(t.time_out, t.time_in)
                            ) / 3600.0
                            ) END
                ) * e.hourly_rate,
                2
        ) AS total_regular_hours_worked_salary,
        ROUND(
                SUM(
                        CASE WHEN t.time_out <= '17:00:00' THEN 0 ELSE (
                            TIME_TO_SEC(
                                    TIMEDIFF(t.time_out, '17:00:00')
                            ) / 3600.0
                            ) END
                ) * e.hourly_rate,
                2
        ) AS total_overtime_hours_worked_salary,
        a.rice AS rice_subsidy,
        a.clothing AS clothing_allowance,
        a.phone AS phone_allowance,
        (a.rice + a.clothing + a.phone) AS total_benefits
        FROM
            timesheet t
            JOIN employee e ON t.employee_id = e.employee_id
            JOIN department d ON e.dept_id = d.dept_id -- Joining the department table
            JOIN position p ON e.position_id = p.position_id
            JOIN allowance a ON t.employee_id = a.alw_id
            WHERE
                e.isActive = 1
              AND t.date BETWEEN ? AND ?
              GROUP BY
                  t.employee_id;