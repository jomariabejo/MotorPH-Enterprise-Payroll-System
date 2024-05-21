package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;

import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeePayrollSummaryReportService {
    /**
     * Class needed to generate the report
     */
    private final EmployeeService employeeService = new EmployeeService();
    private final DeductionService deductionService = new DeductionService();
    private final DepartmentService departmentService = new DepartmentService();
    private final PositionService positionService = new PositionService();
    private final TaxService taxService = new TaxService();
    private final TimesheetService timesheetService = new TimesheetService();
    private final PayslipService payslipService = new PayslipService();


    public EmployeePayrollSummaryReportService() throws SQLException {
    }

    public ArrayList<EmployeePayrollSummaryReport> getEmployeesPayrollSummaryReportByMonthAndYear(int month, int year) {
        return null;
    }
}
