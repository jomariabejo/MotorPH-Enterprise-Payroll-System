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

    // Made package-private for testing
    Payslip createPayslipForEmployee(Employee employee, Payroll payroll, LocalDate periodStart,
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

        // Calculate base taxable income (gross income + benefits)
        BigDecimal baseTaxableIncome = grossIncome.add(totalBenefits);
        
        // Calculate deductions from monthly rate
        BigDecimal sssDeduction = calculateSSSDeduction(monthlyRate, serviceFactory);
        BigDecimal philhealthDeduction = calculatePhilhealthDeduction(monthlyRate, serviceFactory);
        BigDecimal pagibigDeduction = calculatePagibigDeduction(monthlyRate, serviceFactory);
        
        payslip.setSss(sssDeduction);
        payslip.setPhilhealth(philhealthDeduction);
        payslip.setPagIbig(pagibigDeduction);
        
        // Calculate taxable income (base taxable income - mandatory deductions)
        BigDecimal taxableIncome = baseTaxableIncome
                .subtract(sssDeduction)
                .subtract(philhealthDeduction)
                .subtract(pagibigDeduction);
        payslip.setTaxableIncome(taxableIncome);
        
        // Calculate withholding tax
        BigDecimal withholdingTax = calculateWithholdingTax(taxableIncome);
        payslip.setWithholdingTax(withholdingTax);
        
        // Calculate total deductions
        BigDecimal totalDeductions = sssDeduction.add(philhealthDeduction)
                .add(pagibigDeduction)
                .add(withholdingTax);
        payslip.setTotalDeductions(totalDeductions);
        
        // Calculate net pay
        BigDecimal netPay = grossIncome.add(totalBenefits).subtract(totalDeductions);
        payslip.setNetPay(netPay);

        return payslip;
    }

    /**
     * Calculates withholding tax based on BIR tax brackets (2023 rates).
     * 
     * @param taxableIncome The taxable income amount
     * @return The withholding tax amount rounded to 4 decimal places
     */
    public BigDecimal calculateWithholdingTax(BigDecimal taxableIncome) {
        if (taxableIncome == null || taxableIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        }
        
        // BIR withholding tax brackets (2023 rates)
        if (taxableIncome.compareTo(BigDecimal.valueOf(20833)) <= 0) {
            return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(33333)) <= 0) {
            // 20% in excess of 20,833
            return taxableIncome.subtract(BigDecimal.valueOf(20833))
                    .multiply(BigDecimal.valueOf(0.20))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(66667)) <= 0) {
            // 2,500 plus 25% in excess of 33,333
            return BigDecimal.valueOf(2500)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(33333))
                            .multiply(BigDecimal.valueOf(0.25)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(166667)) <= 0) {
            // 10,833 plus 30% in excess of 66,667
            return BigDecimal.valueOf(10833)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(66667))
                            .multiply(BigDecimal.valueOf(0.30)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(666667)) <= 0) {
            // 40,833.33 plus 32% in excess of 166,667
            return BigDecimal.valueOf(40833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(166667))
                            .multiply(BigDecimal.valueOf(0.32)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else {
            // 200,833.33 plus 35% in excess of 666,667
            return BigDecimal.valueOf(200833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(666667))
                            .multiply(BigDecimal.valueOf(0.35)))
                    .setScale(4, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calculateSSSDeduction(BigDecimal monthlySalary, ServiceFactory serviceFactory) {
        // Get all SSS contribution rates
        List<SssContributionRate> rates = serviceFactory.getSssContributionRateService().getAllSssContributionRates();
        
        if (rates.isEmpty()) {
            // Default calculation: For 25,000 monthly salary, SSS is approximately 1,125
            // This is a simplified default - actual SSS uses bracket tables
            if (monthlySalary.compareTo(BigDecimal.valueOf(25000)) <= 0) {
                return BigDecimal.valueOf(1125).setScale(4, RoundingMode.HALF_UP);
            }
            // For higher salaries, use approximate calculation
            return monthlySalary.multiply(BigDecimal.valueOf(0.045)).setScale(4, RoundingMode.HALF_UP);
        }
        
        // Find the appropriate rate bracket based on monthly salary
        for (SssContributionRate rate : rates) {
            if (monthlySalary.compareTo(rate.getSalaryBracketFrom()) >= 0 && 
                monthlySalary.compareTo(rate.getSalaryBracketTo()) <= 0) {
                // Employee share is stored as a fixed amount (not percentage)
                return rate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
            }
        }
        
        // Default fallback
        if (monthlySalary.compareTo(BigDecimal.valueOf(25000)) <= 0) {
            return BigDecimal.valueOf(1125).setScale(4, RoundingMode.HALF_UP);
        }
        return monthlySalary.multiply(BigDecimal.valueOf(0.045)).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePhilhealthDeduction(BigDecimal monthlySalary, ServiceFactory serviceFactory) {
        // Get all Philhealth contribution rates
        List<PhilhealthContributionRate> rates = serviceFactory.getPhilhealthContributionRateService().getAllRates();
        
        if (rates.isEmpty()) {
            // Default calculation: 3% of salary, minimum 300, maximum 1800, split 50/50
            BigDecimal premium = monthlySalary.multiply(BigDecimal.valueOf(0.03));
            premium = premium.max(BigDecimal.valueOf(300)).min(BigDecimal.valueOf(1800));
            return premium.divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP)
                    .setScale(4, RoundingMode.HALF_UP);
        }
        
        // Find the appropriate rate bracket based on monthly salary
        for (PhilhealthContributionRate rate : rates) {
            if (monthlySalary.compareTo(rate.getSalaryBracketFrom()) >= 0 && 
                monthlySalary.compareTo(rate.getSalaryBracketTo()) <= 0) {
                
                // If EmployeeShare is less than 10, treat it as a percentage (for variable calculation)
                // Otherwise, treat it as a fixed amount
                if (rate.getEmployeeShare().compareTo(BigDecimal.valueOf(10)) < 0) {
                    // Variable bracket: Calculate 3% premium rate with min 300, max 1800, then split 50/50
                    // Employee share = 1.5% of salary, min 150, max 900
                    BigDecimal employeeShare = monthlySalary.multiply(rate.getEmployeeShare().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
                    employeeShare = employeeShare.max(BigDecimal.valueOf(150)).min(BigDecimal.valueOf(900));
                    return employeeShare.setScale(4, RoundingMode.HALF_UP);
                } else {
                    // Fixed amounts for other brackets (minimum and maximum)
                    return rate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
                }
            }
        }
        
        // Default fallback: 3% of salary, minimum 300, maximum 1800, split 50/50
        BigDecimal premium = monthlySalary.multiply(BigDecimal.valueOf(0.03));
        premium = premium.max(BigDecimal.valueOf(300)).min(BigDecimal.valueOf(1800));
        return premium.divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_UP)
                .setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePagibigDeduction(BigDecimal monthlySalary, ServiceFactory serviceFactory) {
        // Get the latest Pag-IBIG contribution rate
        List<PagibigContributionRate> rates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        
        if (rates.isEmpty()) {
            // Default calculation: For 25,000 monthly salary, Pagibig is 100 (capped)
            // Pagibig is typically 1-2% but capped at 100 for employee share
            if (monthlySalary.compareTo(BigDecimal.valueOf(1500)) <= 0) {
                return monthlySalary.multiply(BigDecimal.valueOf(0.01)).setScale(4, RoundingMode.HALF_UP);
            } else {
                // For salaries over 1,500, employee share is 2% but capped at 100
                BigDecimal calculated = monthlySalary.multiply(BigDecimal.valueOf(0.02));
                return calculated.min(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);
            }
        }
        
        // Find the appropriate rate bracket based on monthly salary
        for (PagibigContributionRate rate : rates) {
            if (monthlySalary.compareTo(rate.getSalaryBracketFrom()) >= 0 && 
                monthlySalary.compareTo(rate.getSalaryBracketTo()) <= 0) {
                // Calculate employee share based on the rate percentage
                // Employee share is stored as a percentage (e.g., 1% = 1.0)
                BigDecimal deduction = monthlySalary.multiply(rate.getEmployeeShare().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
                // Cap at 100 for employee share
                return deduction.min(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);
            }
        }
        
        // Default fallback
        if (monthlySalary.compareTo(BigDecimal.valueOf(1500)) <= 0) {
            return monthlySalary.multiply(BigDecimal.valueOf(0.01)).setScale(4, RoundingMode.HALF_UP);
        } else {
            BigDecimal calculated = monthlySalary.multiply(BigDecimal.valueOf(0.02));
            return calculated.min(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP);
        }
    }

    private BigDecimal calculateTotalHoursWorked(Employee employee, LocalDate periodStart, LocalDate periodEnd, ServiceFactory serviceFactory) {
        // Get all timesheets and filter by date range
        List<Timesheet> allTimesheets = serviceFactory.getTimesheetService().getAllTimesheets();
        
        BigDecimal totalHours = BigDecimal.ZERO;
        for (Timesheet timesheet : allTimesheets) {
            if (timesheet.getEmployeeID() != null && 
                timesheet.getEmployeeID().getEmployeeNumber().equals(employee.getEmployeeNumber()) &&
                timesheet.getDate() != null &&
                !timesheet.getDate().isBefore(periodStart) &&
                !timesheet.getDate().isAfter(periodEnd) &&
                timesheet.getHoursWorked() != null) {
                totalHours = totalHours.add(BigDecimal.valueOf(timesheet.getHoursWorked()));
            }
        }
        
        return totalHours.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateOvertimeHours(Employee employee, LocalDate periodStart, LocalDate periodEnd, ServiceFactory serviceFactory) {
        // Get all overtime requests and filter by date range
        List<OvertimeRequest> allOvertimeRequests = serviceFactory.getOvertimeRequestService().getAllOvertimeRequests();
        
        BigDecimal totalOvertimeHours = BigDecimal.ZERO;
        for (OvertimeRequest request : allOvertimeRequests) {
            if (request.getEmployeeID() != null &&
                request.getEmployeeID().getEmployeeNumber().equals(employee.getEmployeeNumber()) &&
                "Approved".equals(request.getStatus()) &&
                request.getOvertimeDate() != null &&
                !request.getOvertimeDate().toLocalDate().isBefore(periodStart) &&
                !request.getOvertimeDate().toLocalDate().isAfter(periodEnd) &&
                request.getHoursRequested() != null) {
                totalOvertimeHours = totalOvertimeHours.add(request.getHoursRequested());
            }
        }
        
        return totalOvertimeHours.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateHourlyRateFromSalary(Employee employee) {
        if (employee.getBasicSalary() == null || employee.getBasicSalary().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Calculate hourly rate: (Monthly Salary / 22 working days) / 8 hours per day
        BigDecimal monthlySalary = employee.getBasicSalary().multiply(BigDecimal.valueOf(2)); // Semi-monthly to monthly
        BigDecimal dailyRate = monthlySalary.divide(BigDecimal.valueOf(22), 4, RoundingMode.HALF_UP);
        return dailyRate.divide(BigDecimal.valueOf(8), 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateMonthlyRateFromBasicSalary(Employee employee) {
        if (employee.getBasicSalary() == null || employee.getBasicSalary().compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        
        // Convert semi-monthly basic salary to monthly rate
        return employee.getBasicSalary().multiply(BigDecimal.valueOf(2));
    }

    private BigDecimal calculateGrossIncome(Employee employee, BigDecimal hourlyRate, BigDecimal totalHoursWorked, BigDecimal overtimeHours) {
        // Regular hours income
        BigDecimal regularIncome = hourlyRate.multiply(totalHoursWorked.subtract(overtimeHours));
        
        // Overtime income (1.25x regular rate)
        BigDecimal overtimeRate = hourlyRate.multiply(BigDecimal.valueOf(1.25));
        BigDecimal overtimeIncome = overtimeRate.multiply(overtimeHours);
        
        return regularIncome.add(overtimeIncome).setScale(2, RoundingMode.HALF_UP);
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
