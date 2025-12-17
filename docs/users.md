# Demo Users (Seeded Logins)

These accounts come from the seed SQL: `src/main/resources/data-old.sql` (table: `user`).

## Database setup (required for login)

This project is configured in **dev-friendly** mode (`hibernate.hbm2ddl.auto=update`), so Hibernate can add missing columns and the app can start even if your schema is slightly out of date.

- Fresh setup (recommended):
  - Import: `src/main/resources/data-old.sql` into database `payroll_system`
- Existing `payroll_system` DB:
  - Apply patch: `docs/sql/schema-patch-payroll_system.sql`
  - This fixes known mismatches (e.g., `employee.DateHired`, `leave_request.isPaid`, `overtime_request.OvertimeDate`)

## Roles (from seed)

When using `src/main/resources/data-old.sql`, the seed includes these demo roles:

- `Employee`
- `HR Administrator`
- `Payroll Administrator`
- `System Administrator`

## Demo login accounts

Use the following credentials to log in to the app:

| Role | Username | Password |
|------|----------|----------|
| Employee | `emp_demo` | `emp123` |
| HR Administrator | `hr_demo` | `hr123` |
| Payroll Administrator | `payroll_demo` | `payroll123` |
| System Administrator | `sysadmin_demo` | `sysadmin123` |

## Security note

These are **demo credentials** intended for local testing only. Change passwords / remove seeded users before any real deployment.


