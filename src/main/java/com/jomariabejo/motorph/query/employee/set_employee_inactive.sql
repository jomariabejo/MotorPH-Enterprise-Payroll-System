/**
 * File: set_employee_inactive.sql
 * Description: Updates the isActive status to 0 for a specific employee in the EMPLOYEE table.
 * Author: Jomari Abejo
 * Date Created: April 29, 2024
 * Last Modified: April 29, 2024
 * Version: 1.0
 */

UPDATE EMPLOYEE SET isActive = 0 WHERE employee_id = ?;
