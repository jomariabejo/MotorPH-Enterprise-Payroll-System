SELECT
  p.employee_id,
  CONCAT(e.first_name, ' ', e.last_name) AS EMPLOYEE_NAME,
  p.pay_period_start,
  p.pay_period_end,
  pstn.name,
  e.basic_salary as MONTHLY_RATE,
  e.hourly_rate as HOURLY_RATE,
  p.gross_income,
  d.sss,
  d.philhealth,
  d.pagibig,
  t.withheld_tax,
  (
    (d.total_contribution) + (t.withheld_tax)
  ) AS TOTAL_DEDUCTION,
  a.rice,
  a.phone,
  a.clothing,
  (a.total_amount) AS TOTAL_BENEFITS,
  p.net_income AS TAKE_HOME_PAY
FROM
  payslip p
  INNER JOIN employee e ON p.employee_id = e.employee_id
  INNER JOIN position pstn ON e.employee_id = pstn.position_id
  INNER JOIN allowance a ON p.alw_id = a.alw_id
  INNER JOIN deduction d ON p.deduction_id = d.deduction_id
  INNER JOIN tax t ON p.tax_id = t.tax_id
WHERE
  p.payslip_id = ? -- payslip id


  -- Another query to get the regular hours worked and overtime hours worked

SELECT
	t.employee_id,
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
    ) AS total_overtime_hours_worked

FROM
    timesheet t
    JOIN employee e ON t.employee_id = e.employee_id
    JOIN department d ON e.dept_id = d.dept_id -- Joining the department table
    JOIN position p ON e.position_id = p.position_id

WHERE
  t.date BETWEEN
    ? AND ? -- start pay date & end pay date
    AND t.employee_id = ? -- employee id
GROUP BY
    t.employee_id;

