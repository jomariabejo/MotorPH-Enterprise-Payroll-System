package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.*;
import com.jomariabejo.motorph.repository.PayrollRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    public Payroll getPayrollById(Integer id) {
        return payrollRepository.findById(id);
    }

    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();
    }

    public void savePayroll(Payroll payroll) {
        payrollRepository.save(payroll);
    }

    public void updatePayroll(Payroll payroll) {
        payrollRepository.update(payroll);
    }

    public void deletePayroll(Payroll payroll) {
        payrollRepository.delete(payroll);
    }

    public void generatePayslipsForPayroll(Payroll payroll, ServiceFactory serviceFactory) {
        if (payroll == null || serviceFactory == null) {
            throw new IllegalArgumentException("Payroll and ServiceFactory cannot be null");
        }

        // Check if payslips already exist for this payroll
        Optional<List<Payslip>> existingPayslipsOpt = serviceFactory.getPayslipService().getPayslipsByPayroll(payroll);
        if (existingPayslipsOpt.isPresent() && !existingPayslipsOpt.get().isEmpty()) {
            throw new IllegalStateException("Payslips have already been generated for this payroll");
        }

        // Get all active employees
        Optional<List<Employee>> activeEmployeesOpt = serviceFactory.getEmployeeService().getActiveEmployees();
        if (activeEmployeesOpt.isEmpty() || activeEmployeesOpt.get().isEmpty()) {
            throw new IllegalStateException("No active employees found");
        }

        LocalDate periodStart = payroll.getPeriodStartDate().toLocalDate();
        LocalDate periodEnd = payroll.getPeriodEndDate().toLocalDate();
        YearMonth periodYearMonth = YearMonth.from(periodStart);

        // Generate payslip for each active employee
        for (Employee employee : activeEmployeesOpt.get()) {
            Payslip payslip = createPayslipForEmployee(employee, payroll, periodStart, periodEnd, periodYearMonth, serviceFactory);
            serviceFactory.getPayslipService().savePayslip(payslip);
        }
    }

                                             LocalDate periodEnd, YearMonth periodYearMonth, ServiceFactory serviceFactory) {
        Payslip payslip = new Payslip();
        
        // Set basic payroll and employee information
        payslip.setPayrollID(payroll);
        payslip.setEmployeeID(employee);
        payslip.setPeriodStartDate(Date.valueOf(periodStart));
        payslip.setPeriodEndDate(Date.valueOf(periodEnd));
        payslip.setPayrollRunDate(payroll.getPayrollRunDate());
        payslip.setPaymentDate(payroll.getPayrollRunDate());
        payslip.setPaymentMethod("Bank Transfer"); // Default payment method
        
        // Set employee position and department
        if (employee.getPositionID() != null) {
            payslip.setEmployeePosition(employee.getPositionID());
            if (employee.getPositionID().getDepartmentID() != null) {
                payslip.setEmployeeDepartment(employee.getPositionID().getDepartmentID());
            }
        }

        // Set employee name (required field)
        String employeeFullName = employee.getFirstName() + " " + employee.getLastName();
        payslip.setEmployeeName(employeeFullName);

        // Calculate hours worked from timesheet
        BigDecimal totalHoursWorked = calculateTotalHoursWorked(employee, periodStart, periodEnd, serviceFactory);
        BigDecimal overtimeHours = calculateOvertimeHours(employee, periodStart, periodEnd, serviceFactory);
        payslip.setTotalHoursWorked(totalHoursWorked);
        payslip.setOvertimeHours(overtimeHours);

        // Set rates from employee (with null checks)
        BigDecimal hourlyRate = employee.getHourlyRate() != null && employee.getHourlyRate().compareTo(BigDecimal.ZERO) > 0
                ? employee.getHourlyRate()
                : calculateHourlyRateFromSalary(employee);
        payslip.setHourlyRate(hourlyRate);
        
        BigDecimal monthlyRate = employee.getGrossSemiMonthlyRate() != null && employee.getGrossSemiMonthlyRate().compareTo(BigDecimal.ZERO) > 0
                ? employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2))
                : calculateMonthlyRateFromBasicSalary(employee);
        payslip.setMonthlyRate(monthlyRate);

        // Calculate daily rate (monthly rate / 22 working days)
        BigDecimal dailyRate = monthlyRate.divide(BigDecimal.valueOf(22), 4, RoundingMode.HALF_UP);
        payslip.setDailyRate(dailyRate);

        // Calculate days worked (total hours / 8 hours per day)
        int daysWorked = totalHoursWorked.divide(BigDecimal.valueOf(8), 0, RoundingMode.HALF_UP).intValue();
        payslip.setDaysWorked(daysWorked);

        // Calculate gross income
        BigDecimal grossIncome = calculateGrossIncome(employee, hourlyRate, totalHoursWorked, overtimeHours);
        payslip.setGrossIncome(grossIncome);

        // Set benefits from employee
        BigDecimal riceSubsidy = employee.getRiceSubsidy();
        BigDecimal phoneAllowance = employee.getPhoneAllowance();
        BigDecimal clothingAllowance = employee.getClothingAllowance();
        BigDecimal totalBenefits = riceSubsidy.add(phoneAllowance).add(clothingAllowance);
        
        payslip.setRiceSubsidy(riceSubsidy);
        payslip.setPhoneAllowance(phoneAllowance);
        payslip.setClothingAllowance(clothingAllowance);
        payslip.setTotalBenefits(totalBenefits);

    }

    private BigDecimal calculatePagibigDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        // Get the latest Pag-IBIG contribution rate
        List<PagibigContributionRate> rates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        
        if (rates.isEmpty()) {
            // Default calculation: 2% of monthly salary (employee share is 1%)
            return taxableIncome.multiply(BigDecimal.valueOf(0.01)).setScale(4, RoundingMode.HALF_UP);
        }
        
        // Simplified BIR withholding tax calculation (2023 rates)
        // This is a basic implementation - should be enhanced with proper tax brackets
        
        if (taxableIncome.compareTo(BigDecimal.valueOf(20833)) <= 0) {
            return BigDecimal.ZERO;
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(33333)) <= 0) {
            return taxableIncome.subtract(BigDecimal.valueOf(20833))
                    .multiply(BigDecimal.valueOf(0.20))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(66667)) <= 0) {
            return BigDecimal.valueOf(2500)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(33333))
                            .multiply(BigDecimal.valueOf(0.25)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(166667)) <= 0) {
            return BigDecimal.valueOf(10833)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(66667))
                            .multiply(BigDecimal.valueOf(0.30)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(666667)) <= 0) {
            return BigDecimal.valueOf(40833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(166667))
                            .multiply(BigDecimal.valueOf(0.32)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf(200833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(666667))
                            .multiply(BigDecimal.valueOf(0.35)))
                    .setScale(4, RoundingMode.HALF_UP);
        }
    }

    private String generatePayslipNumber(Employee employee, YearMonth periodYearMonth) {
        // Format: EMP-YYYYMM-XXXXX (Employee Number - YearMonth - Sequential)
        return String.format("EMP-%d-%04d%02d-%05d",
                employee.getEmployeeNumber(),
                periodYearMonth.getYear(),
                periodYearMonth.getMonthValue(),
                System.currentTimeMillis() % 100000); // Simple sequential number
    }
}
