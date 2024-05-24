SELECT
  p.employee_id,
  CONCAT(e.first_name, ' ', e.last_name) AS employee_name,
  pos.name AS position,
  dep.name AS department,
  p.pay_period_start,
  p.pay_period_end,
  e.basic_salary AS monthly_rate,
  e.hourly_rate,
  p.total_hours_worked,
  ot.total_overtime_hours,
  ot.total_overtime_hours_worked_salary,
  p.total_hours_worked * e.hourly_rate AS GROSS_INCOME,
  alw.rice,
  alw.clothing,
  alw.phone,
  alw.total_amount AS BENEFITS,
  deduct.sss,
  deduct.philhealth,
  deduct.pagibig,
  tx.taxable_income,
  tx.withheld_tax,
  deduct.sss + deduct.philhealth + deduct.pagibig + tx.withheld_tax AS TOTAL_DEDUCTION,
  p.net_income AS TAKE_HOME_PAY
FROM
  payslip p
  INNER JOIN employee e ON p.employee_id = e.employee_id
  INNER JOIN position pos ON e.position_id = pos.position_id
  INNER JOIN department dep ON e.dept_id = dep.dept_id
  INNER JOIN allowance alw ON p.alw_id = alw.alw_id
  INNER JOIN deduction deduct ON p.deduction_id = deduct.deduction_id
  INNER JOIN tax tx ON p.tax_id = tx.tax_id
  LEFT JOIN (
    SELECT
      t.employee_id,
      SUM(
        CASE WHEN t.time_out <= '17:00:00' THEN 0 ELSE (
          TIME_TO_SEC(
            TIMEDIFF(t.time_out, '17:00:00')
          ) / 3600.0
        ) END
      ) AS total_overtime_hours,
      ROUND(
        SUM(
          CASE WHEN t.time_out <= '17:00:00' THEN 0 ELSE (
            TIME_TO_SEC(
              TIMEDIFF(t.time_out, '17:00:00')
            ) / 3600.0
          ) END
        ) * e.hourly_rate,
        2
      ) AS total_overtime_hours_worked_salary
    FROM
      timesheet t
      JOIN employee e ON t.employee_id = e.employee_id
      JOIN department d ON e.dept_id = d.dept_id
      JOIN position p ON e.position_id = p.position_id
      JOIN allowance a ON t.employee_id = a.alw_id
    WHERE
      e.isActive = 1
      AND t.date BETWEEN ?
      AND ? -- start pay date and end pay date
    GROUP BY
      t.employee_id
  ) AS ot ON p.employee_id = ot.employee_id
WHERE
  p.employee_id = ?;
