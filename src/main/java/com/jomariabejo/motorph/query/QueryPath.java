package com.jomariabejo.motorph.query;

import com.jomariabejo.motorph.utility.TextReader;

public class QueryPath {
    private static final String QUERY_BASE_PATH = "src\\main\\java\\com\\jomariabejo\\motorph\\query\\";
    public enum TABLE {
        ALLOWANCE,
        DEDUCTION,
        DEPARTMENT,
        employee,
        LEAVE_REQUEST,
        LEAVE_REQUEST_CATEGORY,
        PAYSLIP,
        PERMISSION,
        POSITION,
        ROLE,
        TAX,
        TAX_CATEGORY,
        TIMESHEET,
        USER
    }
    public class Employee {
        public static final String EMPLOYEE_QUERY_PATH = QUERY_BASE_PATH + TABLE.employee;

        public static String COUNT_ACTIVE_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\count_active_employees.sql").trim();
        public static String COUNT_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\count_employees.sql").trim();
        public static String COUNT_INACTIVE_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\count_inactive_employees.sql").trim();
        public static String CREATE_EMPLOYEE =  TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\create_employee.sql").trim();
        public static String DELETE_EMPLOYEE =  TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\delete_employee.sql").trim();
        public static String GET_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_employees.sql").trim();
        public static String GET_EMPLOYEE_BY_ID = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_employee_by_id.sql").trim();
        public static String GET_EMPLOYEE_BY_ID_ACTIVE = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_employee_by_id_active.sql").trim();
        public static String GET_EMPLOYEE_BY_ID_INACTIVE = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_employee_by_id_inactive.sql").trim();
        public static String GET_ACTIVE_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_active_employees.sql").trim();
        public static String GET_INACTIVE_EMPLOYEES = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_inactive_employees.sql").trim();
        public static String SET_EMPLOYEE_ACTIVE = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\set_employee_active.sql").trim();
        public static String SET_EMPLOYEE_INACTIVE = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\set_employee_inactive.sql").trim();
        public static String UPDATE_EMPLOYEE_RECORD = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\update_employee_record.sql").trim();

    }

    public class Timesheet {
        public static final String EMPLOYEE_QUERY_PATH = QUERY_BASE_PATH + TABLE.TIMESHEET;

        public static String GET_TIMESHEETS = TextReader.readTextFile(EMPLOYEE_QUERY_PATH + "\\get_timesheets.sql").trim();


    }

    public class User {
        public static String CREATE_USER_QUERY = "src/main/java/com/jomariabejo/motorph/query/employee/create_role.sql";
        public static String READ_USER_QUERY = "src/main/java/com/jomariabejo/motorph/query/employee/get_payslip_by_payslip_id.sql";
        public static String UPDATE_USER_QUERY = "src/main/java/com/jomariabejo/motorph/query/employee/update_payslip.sql";
        public static String DELETE_USER_QUERY = "src/main/java/com/jomariabejo/motorph/query/employee/delete_payslip.sql";
    }
}
