UPDATE tax SET
    taxable_income = ?,
    tax_cat_id = ?,
   withheld_tax = ?
WHERE employee_id = ?