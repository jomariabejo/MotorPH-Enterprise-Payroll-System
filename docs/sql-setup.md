# SQL Setup (MySQL) + Schema/Seed Import

This project uses **MySQL** with **Hibernate**.

- Hibernate connection config: `src/main/resources/hibernate.cfg.xml`
- Seed SQL dump (schema + demo data, matches current Hibernate mappings): `src/main/resources/data-old.sql`
- Optional patch SQL for an existing DB (safe to re-run): `docs/sql/schema-patch-payroll_system.sql`

## Prerequisites

- MySQL Server (8.x recommended)
- MySQL client (`mysql`) available in your PATH

## Database name (important)

This repo contains **two different SQL dumps**:

- `src/main/resources/data-old.sql` (entity-matching schema; uses `EmployeeNumber`, `DepartmentID`, `UserID`, etc)
- `src/main/resources/data.sql` (legacy/alternate schema; uses snake_case like `employee_id`)

The Hibernate entity mappings in this project match **`data-old.sql`**.

### Recommended: use `payroll_system` (matches Hibernate mappings)

1. Ensure `src/main/resources/hibernate.cfg.xml` points to:
   - `jdbc:mysql://localhost:3306/payroll_system`
2. Import the schema + demo data:
   - `mysql -u root -p < src/main/resources/data-old.sql`

## Import the seed SQL

From the project root, run:

```bash
mysql -u root -p < src/main/resources/data-old.sql
```

If you use a different MySQL user, replace `root` accordingly.

## Patch an existing database to match Hibernate (validate mode)

This project can be run in two modes:

- **Dev mode (recommended to \"just run\")**: `hibernate.hbm2ddl.auto=update`
  - Hibernate will try to create/alter missing columns so the app starts.
- **Strict mode**: `hibernate.hbm2ddl.auto=validate`
  - Hibernate will **refuse to start** if your DB schema doesn’t match the JPA entities.

If you already have a `payroll_system` database and you see schema validation errors (missing columns / wrong types), apply the patch:

```bash
mysql -u root -p payroll_system < docs/sql/schema-patch-payroll_system.sql
```

## Notes about Hibernate schema updates

In `src/main/resources/hibernate.cfg.xml`, the setting:

- `hibernate.hbm2ddl.auto=update`

means Hibernate may **create/alter tables** at application startup. If you imported the full schema via `seed-data.sql`, you typically want Hibernate to only update as needed (or set it to `validate` in stricter environments).


