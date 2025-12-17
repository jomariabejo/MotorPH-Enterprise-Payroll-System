package com.jomariabejo.motorph.utility;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.service.ServiceFactory;
import com.jomariabejo.motorph.service.TimesheetService;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Utility class for generating test timesheet data.
 * Generates timesheet entries for employees for a specified month/year.
 */
public class TimesheetDataGenerator {

    // Private constructor to prevent instantiation
    private TimesheetDataGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final LocalTime DEFAULT_TIME_IN = LocalTime.of(8, 0); // 8:00 AM
    private static final LocalTime DEFAULT_TIME_OUT = LocalTime.of(17, 0); // 5:00 PM
    private static final float STANDARD_HOURS_WORKED = 8.0f; // 8 hours for 8 AM to 5 PM

    /**
     * Generates timesheet entries for all active employees for December 2025.
     * Creates entries for weekdays (Monday-Friday) with time in at 8:00 AM and time out at 5:00 PM.
     *
     * @param serviceFactory The service factory to access services
     * @return Number of timesheet entries created
     */
    public static int generateDecember2025Timesheets(ServiceFactory serviceFactory) {
        return generateTimesheetsForMonth(2025, 12, serviceFactory);
    }

    /**
     * Generates timesheet entries for all active employees for a specific month and year.
     * Creates entries for weekdays (Monday-Friday) with time in at 8:00 AM and time out at 5:00 PM.
     *
     * @param year The year
     * @param month The month (1-12)
     * @param serviceFactory The service factory to access services
     * @return Number of timesheet entries created
     */
    public static int generateTimesheetsForMonth(int year, int month, ServiceFactory serviceFactory) {
        if (serviceFactory == null) {
            throw new IllegalArgumentException("ServiceFactory cannot be null");
        }

        // Get all active employees
        Optional<List<Employee>> activeEmployeesOpt = serviceFactory.getEmployeeService().getActiveEmployees();
        if (activeEmployeesOpt.isEmpty() || activeEmployeesOpt.get().isEmpty()) {
            System.out.println("No active employees found. Cannot generate timesheets.");
            return 0;
        }

        TimesheetService timesheetService = serviceFactory.getTimesheetService();
        List<Employee> employees = activeEmployeesOpt.get();
        int totalCreated = 0;

        // Get the first and last day of the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        System.out.println(String.format("Generating timesheets for %d employees from %s to %s",
                employees.size(), startDate, endDate));

        // Generate timesheets for each employee
        for (Employee employee : employees) {
            int createdForEmployee = generateTimesheetsForEmployee(
                    employee,
                    startDate,
                    endDate,
                    timesheetService
            );
            totalCreated += createdForEmployee;
            System.out.println(String.format("Created %d timesheets for employee #%d (%s %s)",
                    createdForEmployee, employee.getEmployeeNumber(),
                    employee.getFirstName(), employee.getLastName()));
        }

        System.out.println(String.format("Total timesheet entries created: %d", totalCreated));
        return totalCreated;
    }

    /**
     * Generates timesheet entries for a specific employee for the given date range.
     * Only creates entries for weekdays (Monday-Friday).
     *
     * @param employee The employee
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @param timesheetService The timesheet service
     * @return Number of timesheet entries created
     */
    private static int generateTimesheetsForEmployee(
            Employee employee,
            LocalDate startDate,
            LocalDate endDate,
            TimesheetService timesheetService) {

        int created = 0;
        LocalDate currentDate = startDate;

        // Iterate through each day in the range
        while (!currentDate.isAfter(endDate)) {
            // Only create timesheets for weekdays (Monday = 1, Friday = 5)
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek.getValue() >= DayOfWeek.MONDAY.getValue() &&
                dayOfWeek.getValue() <= DayOfWeek.FRIDAY.getValue()) {

                // Check if timesheet already exists for this date
                Optional<Timesheet> existingTimesheetOpt = timesheetService
                        .getTimesheetByEmployeeAndDate(employee, currentDate);

                if (existingTimesheetOpt.isEmpty()) {
                    // Create new timesheet entry
                    Timesheet timesheet = createTimesheetEntry(employee, currentDate);
                    timesheetService.saveTimesheet(timesheet);
                    created++;
                } else {
                    System.out.println(String.format("Timesheet already exists for employee #%d on %s. Skipping.",
                            employee.getEmployeeNumber(), currentDate));
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return created;
    }

    /**
     * Creates a timesheet entry for an employee on a specific date.
     * Uses default time in (8:00 AM) and time out (5:00 PM).
     *
     * @param employee The employee
     * @param date The date
     * @return The created timesheet entry
     */
    private static Timesheet createTimesheetEntry(Employee employee, LocalDate date) {
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(employee);
        timesheet.setDate(date);
        timesheet.setTimeIn(Time.valueOf(DEFAULT_TIME_IN));
        timesheet.setTimeOut(Time.valueOf(DEFAULT_TIME_OUT));
        timesheet.setHoursWorked(STANDARD_HOURS_WORKED);
        timesheet.setStatus("Approved"); // Set to approved for test data
        timesheet.setRemarks("Generated test data");

        return timesheet;
    }

    /**
     * Generates timesheet entries with random time variations for more realistic test data.
     * Time in varies between 7:45 AM and 8:15 AM.
     * Time out varies between 4:45 PM and 5:15 PM.
     * 
     * NOTE: This method is reserved for future use when more realistic time variations are needed.
     *
     * @param employee The employee
     * @param date The date
     * @param random Random number generator
     * @return The created timesheet entry with random times
     */
    @SuppressWarnings("unused")
    private static Timesheet createTimesheetEntryWithVariation(Employee employee, LocalDate date, Random random) {
        // Random time in between 7:45 AM and 8:15 AM
        int timeInMinutes = 7 * 60 + 45 + random.nextInt(31); // 7:45 to 8:15 (30 minutes range)
        LocalTime timeIn = LocalTime.of(timeInMinutes / 60, timeInMinutes % 60);

        // Random time out between 4:45 PM and 5:15 PM
        int timeOutMinutes = 16 * 60 + 45 + random.nextInt(31); // 16:45 to 17:15 (30 minutes range)
        LocalTime timeOut = LocalTime.of(timeOutMinutes / 60, timeOutMinutes % 60);

        // Calculate hours worked
        float hoursWorked = calculateHoursWorked(timeIn, timeOut);

        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(employee);
        timesheet.setDate(date);
        timesheet.setTimeIn(Time.valueOf(timeIn));
        timesheet.setTimeOut(Time.valueOf(timeOut));
        timesheet.setHoursWorked(hoursWorked);
        timesheet.setStatus("Approved");
        timesheet.setRemarks("Generated test data with variation");

        return timesheet;
    }

    /**
     * Calculates hours worked between time in and time out.
     * Caps the calculation between 8:00 AM and 5:00 PM as per business rules.
     *
     * @param timeIn Time in
     * @param timeOut Time out
     * @return Hours worked
     */
    private static float calculateHoursWorked(LocalTime timeIn, LocalTime timeOut) {
        LocalTime effectiveTimeIn = timeIn.isBefore(DEFAULT_TIME_IN) ? DEFAULT_TIME_IN : timeIn;
        LocalTime effectiveTimeOut = timeOut.isAfter(DEFAULT_TIME_OUT) ? DEFAULT_TIME_OUT : timeOut;

        if (effectiveTimeOut.isBefore(effectiveTimeIn)) {
            return 0.0f;
        }

        long minutesWorked = java.time.Duration.between(effectiveTimeIn, effectiveTimeOut).toMinutes();
        return minutesWorked / 60.0f;
    }
}

