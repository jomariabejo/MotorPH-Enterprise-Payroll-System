SELECT CONCAT(EMPLOYEE.first_name, ' ', EMPLOYEE.last_name) AS full_name, USER.user_id, USER.employee_id, USER.role_id, ROLE.name
FROM USER
         INNER JOIN EMPLOYEE ON USER.employee_id = EMPLOYEE.employee_id
         INNER JOIN ROLE ON USER.role_id = ROLE.role_id
WHERE USER.user_id = ?;