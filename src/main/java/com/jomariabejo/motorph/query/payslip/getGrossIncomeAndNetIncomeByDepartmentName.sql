SELECT d.name AS department_name, SUM(p.gross_income) AS total_gross_income, SUM(p.net_income) AS total_net_income
FROM payslip p
JOIN employee e ON p.employee_id = e.employee_id
JOIN department d ON e.dept_id = d.dept_id
WHERE MONTH(p.pay_period_start) = 6
AND YEAR(p.pay_period_start) = 2024
GROUP BY d.name;


SELECT d.name AS department_name,
    SUM(p.gross_income) AS total_gross_income,
    SUM(p.net_income) AS total_net_income,
    SUM(a.total_amount) AS total_benefits,
    (SUM(dd.total_contribution) + SUM(t.withheld_tax)) AS total_deduction
FROM payslip p
JOIN employee e ON p.employee_id = e.employee_id
JOIN department d ON e.dept_id = d.dept_id
JOIN allowance a ON p.alw_id = a.alw_id
JOIN deduction dd ON p.deduction_id = dd.deduction_id
JOIN tax t ON p.tax_id = t.tax_id
WHERE p.pay_period_start = '2024-06-01'
    AND p.pay_period_end = '2024-06-30'
GROUP BY d.name;
