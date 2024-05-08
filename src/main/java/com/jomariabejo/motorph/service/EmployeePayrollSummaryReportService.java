package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;

import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeePayrollSummaryReportService {
    /**
     * Class needed to generate the report
     */
    private EmployeeService employeeService = new EmployeeService();
    private DeductionService deductionService = new DeductionService();
    private DepartmentService departmentService = new DepartmentService();
    private PositionService positionService = new PositionService();
    private TaxService taxService = new TaxService();
    private TimesheetService timesheetService = new TimesheetService();
    private PayslipService payslipService = new PayslipService();


    public EmployeePayrollSummaryReportService() throws SQLException {
    }

    public ArrayList<EmployeePayrollSummaryReport> getEmployeesPayrollSummaryReportByMonthAndYear(int month, int year) {
        return null;
    }
}
