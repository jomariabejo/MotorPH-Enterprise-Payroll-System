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

    private Payslip createPayslipForEmployee(Employee employee, Payroll payroll, LocalDate periodStart, 
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

        // Calculate taxable income (gross income + benefits)
        BigDecimal taxableIncome = grossIncome.add(totalBenefits);
        payslip.setTaxableIncome(taxableIncome);
        payslip.setTaxableBenefits(totalBenefits);

        // Calculate deductions
        BigDecimal sssDeduction = calculateSSSDeduction(taxableIncome, serviceFactory);
        BigDecimal philhealthDeduction = calculatePhilhealthDeduction(taxableIncome, serviceFactory);
        BigDecimal pagibigDeduction = calculatePagibigDeduction(taxableIncome, serviceFactory);
        BigDecimal withholdingTax = calculateWithholdingTax(taxableIncome);
        
        payslip.setSss(sssDeduction);
        payslip.setPhilhealth(philhealthDeduction);
        payslip.setPagIbig(pagibigDeduction);
        payslip.setWithholdingTax(withholdingTax);

        // Calculate total deductions
        BigDecimal totalDeductions = sssDeduction.add(philhealthDeduction)
                .add(pagibigDeduction).add(withholdingTax);
        payslip.setTotalDeductions(totalDeductions);

        // Calculate net pay
        BigDecimal netPay = grossIncome.add(totalBenefits).subtract(totalDeductions);
        payslip.setNetPay(netPay.max(BigDecimal.ZERO)); // Ensure net pay is not negative

        // Set bonus and other deductions (default to zero)
        payslip.setBonus(BigDecimal.ZERO);
        payslip.setOtherDeductions(BigDecimal.ZERO);

        // Set leave and absence days (simplified - can be enhanced later)
        payslip.setLeaveDays(0);
        payslip.setAbsenceDays(0);

        // Generate payslip number
        String payslipNumber = generatePayslipNumber(employee, periodYearMonth);
        payslip.setPayslipNumber(payslipNumber);

        return payslip;
    }

    private BigDecimal calculateTotalHoursWorked(Employee employee, LocalDate periodStart, LocalDate periodEnd, ServiceFactory serviceFactory) {
        BigDecimal totalHours = BigDecimal.ZERO;
        
        // Handle periods that may span multiple months
        YearMonth startYearMonth = YearMonth.from(periodStart);
        YearMonth endYearMonth = YearMonth.from(periodEnd);
        
        // Query timesheets for each month in the period
        YearMonth currentYearMonth = startYearMonth;
        while (!currentYearMonth.isAfter(endYearMonth)) {
            Year year = Year.of(currentYearMonth.getYear());
            Optional<List<Timesheet>> timesheetsOpt = serviceFactory.getTimesheetService()
                    .getTimesheetsByEmployeeAndDate(employee, year, currentYearMonth.getMonth());
            
            if (timesheetsOpt.isPresent()) {
                for (Timesheet timesheet : timesheetsOpt.get()) {
                    LocalDate timesheetDate = timesheet.getDate();
                    // Check if timesheet is within the period
                    if (!timesheetDate.isBefore(periodStart) && !timesheetDate.isAfter(periodEnd)) {
                        // Use hoursWorked if available, otherwise calculate from timeIn/timeOut
                        if (timesheet.getHoursWorked() != null && timesheet.getHoursWorked() > 0) {
                            totalHours = totalHours.add(BigDecimal.valueOf(timesheet.getHoursWorked()));
                        } else if (timesheet.getTimeIn() != null && timesheet.getTimeOut() != null) {
                            // Calculate hours worked from time in and time out
                            BigDecimal calculatedHours = calculateHoursFromTimesheet(timesheet);
                            totalHours = totalHours.add(calculatedHours);
                        }
                    }
                }
            }
            
            // Move to next month
            currentYearMonth = currentYearMonth.plusMonths(1);
        }
        
        // If no timesheet data found, calculate based on working days
        if (totalHours.compareTo(BigDecimal.ZERO) == 0) {
            long workingDays = periodStart.datesUntil(periodEnd.plusDays(1))
                    .filter(date -> {
                        int dayOfWeek = date.getDayOfWeek().getValue();
                        return dayOfWeek >= DayOfWeek.MONDAY.getValue() && dayOfWeek <= DayOfWeek.FRIDAY.getValue();
                    })
                    .count();
            totalHours = BigDecimal.valueOf(workingDays * 8);
        }
        
        return totalHours.setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateHoursFromTimesheet(Timesheet timesheet) {
        if (timesheet.getTimeIn() == null || timesheet.getTimeOut() == null) {
            return BigDecimal.ZERO;
        }
        
        // Convert Time to LocalTime for calculation
        LocalTime timeIn = timesheet.getTimeIn().toLocalTime();
        LocalTime timeOut = timesheet.getTimeOut().toLocalTime();
        
        // Cap times between 8:00 AM and 5:00 PM as per business rules
        LocalTime effectiveTimeIn = timeIn.isBefore(LocalTime.of(8, 0)) 
                ? LocalTime.of(8, 0) 
                : timeIn;
        LocalTime effectiveTimeOut = timeOut.isAfter(LocalTime.of(17, 0)) 
                ? LocalTime.of(17, 0) 
                : timeOut;
        
        if (effectiveTimeOut.isBefore(effectiveTimeIn)) {
            return BigDecimal.ZERO;
        }
        
        // Calculate hours worked
        long minutesWorked = java.time.Duration.between(effectiveTimeIn, effectiveTimeOut).toMinutes();
        return BigDecimal.valueOf(minutesWorked).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateOvertimeHours(Employee employee, LocalDate periodStart, LocalDate periodEnd, ServiceFactory serviceFactory) {
        // Overtime hours calculation can be enhanced later
        // For now, return zero as overtime requests need to be looked up by date range
        return BigDecimal.ZERO.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateGrossIncome(Employee employee, BigDecimal hourlyRate, BigDecimal totalHoursWorked, BigDecimal overtimeHours) {
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            // Fallback: calculate from basic salary if hourly rate is not set
            hourlyRate = calculateHourlyRateFromSalary(employee);
        }
        
        if (hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            // If still zero, return zero to avoid errors
            return BigDecimal.ZERO;
        }
        
        BigDecimal regularPay = hourlyRate.multiply(totalHoursWorked);
        BigDecimal overtimePay = hourlyRate
                .multiply(BigDecimal.valueOf(1.25)) // 25% overtime premium
                .multiply(overtimeHours);
        
        return regularPay.add(overtimePay).setScale(4, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateHourlyRateFromSalary(Employee employee) {
        // Calculate hourly rate from monthly salary
        // Assumption: 8 hours/day, 22 working days/month = 176 hours/month
        BigDecimal monthlySalary = BigDecimal.ZERO;
        
        if (employee.getGrossSemiMonthlyRate() != null && employee.getGrossSemiMonthlyRate().compareTo(BigDecimal.ZERO) > 0) {
            monthlySalary = employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2));
        } else if (employee.getBasicSalary() != null && employee.getBasicSalary().compareTo(BigDecimal.ZERO) > 0) {
            monthlySalary = employee.getBasicSalary();
        }
        
        if (monthlySalary.compareTo(BigDecimal.ZERO) > 0) {
            // 176 hours per month (8 hours * 22 working days)
            return monthlySalary.divide(BigDecimal.valueOf(176), 4, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateMonthlyRateFromBasicSalary(Employee employee) {
        if (employee.getGrossSemiMonthlyRate() != null && employee.getGrossSemiMonthlyRate().compareTo(BigDecimal.ZERO) > 0) {
            return employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2));
        } else if (employee.getBasicSalary() != null && employee.getBasicSalary().compareTo(BigDecimal.ZERO) > 0) {
            return employee.getBasicSalary();
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateSSSDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        // Get the latest SSS contribution rate
        List<SssContributionRate> rates = serviceFactory.getSssContributionRateService().getAllSssContributionRates();
        
        if (rates.isEmpty()) {
            // Default calculation: 11% of monthly salary (employee share is 4.5%)
            return taxableIncome.multiply(BigDecimal.valueOf(0.045)).setScale(4, RoundingMode.HALF_UP);
        }
        
        // Find the appropriate rate bracket
        SssContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1)); // Use highest bracket if exceeds
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePhilhealthDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        // Get the latest Philhealth contribution rate
        List<PhilhealthContributionRate> rates = serviceFactory.getPhilhealthContributionRateService().getAllRates();
        
        if (rates.isEmpty()) {
            // Default calculation: 3% of monthly salary (employee share is 1.5%)
            return taxableIncome.multiply(BigDecimal.valueOf(0.015)).setScale(4, RoundingMode.HALF_UP);
        }
        
        // Find the appropriate rate bracket
        PhilhealthContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1)); // Use highest bracket if exceeds
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePagibigDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        // Get the latest Pag-IBIG contribution rate
        List<PagibigContributionRate> rates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        
        if (rates.isEmpty()) {
            // Default calculation: 2% of monthly salary (employee share is 1%)
            return taxableIncome.multiply(BigDecimal.valueOf(0.01)).setScale(4, RoundingMode.HALF_UP);
        }
        
        // Find the appropriate rate bracket
        PagibigContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1)); // Use highest bracket if exceeds
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateWithholdingTax(BigDecimal taxableIncome) {
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
            return BigDecimal.valueOf(10833.33)
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
