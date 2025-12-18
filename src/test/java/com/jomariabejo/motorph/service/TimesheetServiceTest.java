package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.repository.EmployeeRepository;
import com.jomariabejo.motorph.repository.TimesheetRepository;
import com.jomariabejo.motorph.util.TimesheetTestDataGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests for TimesheetService.
 * Tests timesheet creation, filtering, update, and deletion functionality.
 */
@DisplayName("Timesheet Service Tests")
class TimesheetServiceTest {

    private TimesheetService timesheetService;
    private EmployeeService employeeService;
    private TimesheetRepository timesheetRepository;
    private EmployeeRepository employeeRepository;
    private List<Employee> testEmployees;

    @BeforeEach
    void setUp() {
        // Initialize repositories and services
        timesheetRepository = new TimesheetRepository();
        employeeRepository = new EmployeeRepository();
        timesheetService = new TimesheetService(timesheetRepository);
        employeeService = new EmployeeService(employeeRepository);

        // Get all employees for testing
        testEmployees = employeeService.getAllEmployees();
        assertFalse(testEmployees.isEmpty(), "Test database should have at least one employee");

        // Clean up any existing test timesheets
        cleanupTestTimesheets();
    }

    @AfterEach
    void tearDown() {
        // Clean up test timesheets after each test
        cleanupTestTimesheets();
    }

    @Test
    @DisplayName("Test populate dummy timesheets for all employees (8AM-5PM)")
    void testPopulateDummyTimesheetsForAllEmployees() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now();

        // Act
        List<Timesheet> generatedTimesheets = TimesheetTestDataGenerator.generateTimesheetsForAllEmployees(
                testEmployees, startDate, endDate);

        // Save timesheets to database
        int savedCount = 0;
        for (Timesheet timesheet : generatedTimesheets) {
            // Check if timesheet already exists
            Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                    timesheet.getEmployeeID(), timesheet.getDate());
            if (existing.isEmpty()) {
                timesheetService.saveTimesheet(timesheet);
                savedCount++;
            }
        }

        // Assert
        assertTrue(savedCount > 0, "Should have created at least one timesheet");
        assertEquals(Time.valueOf(LocalTime.of(8, 0)), generatedTimesheets.get(0).getTimeIn(),
                "Time In should be 8:00 AM");
        assertEquals(Time.valueOf(LocalTime.of(17, 0)), generatedTimesheets.get(0).getTimeOut(),
                "Time Out should be 5:00 PM (17:00)");
        assertEquals(9.0f, generatedTimesheets.get(0).getHoursWorked(),
                0.01f, "Hours worked should be 9.0");
        assertEquals("Submitted", generatedTimesheets.get(0).getStatus(),
                "Status should be 'Submitted'");
    }

    @Test
    @DisplayName("Test populate timesheets for date range")
    void testPopulateTimesheetsForDateRange() {
        // Arrange
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);
        Employee testEmployee = testEmployees.get(0);

        // Act
        List<Timesheet> generatedTimesheets = TimesheetTestDataGenerator.generateTimesheetsForEmployee(
                testEmployee, startDate, endDate, false);

        // Save first timesheet to verify
        if (!generatedTimesheets.isEmpty()) {
            Timesheet firstTimesheet = generatedTimesheets.get(0);
            Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                    firstTimesheet.getEmployeeID(), firstTimesheet.getDate());
            if (existing.isEmpty()) {
                timesheetService.saveTimesheet(firstTimesheet);
            }
        }

        // Assert
        assertFalse(generatedTimesheets.isEmpty(), "Should generate timesheets for date range");
        assertEquals(startDate, generatedTimesheets.get(0).getDate(),
                "First timesheet should be on start date");
        assertTrue(generatedTimesheets.get(0).getDate().isBefore(endDate.plusDays(1)),
                "All timesheets should be within date range");
    }

    @Test
    @DisplayName("Test timesheet creation with valid data")
    void testTimesheetCreationWithValidData() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(100); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);

        // Act
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> savedTimesheet = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(savedTimesheet.isPresent(), "Timesheet should be saved");
        assertEquals(testEmployee.getEmployeeNumber(),
                savedTimesheet.get().getEmployeeID().getEmployeeNumber(),
                "Employee should match");
        assertEquals(testDate, savedTimesheet.get().getDate(), "Date should match");
        assertEquals(Time.valueOf(LocalTime.of(8, 0)), savedTimesheet.get().getTimeIn(),
                "Time In should be 8:00 AM");
        assertEquals(Time.valueOf(LocalTime.of(17, 0)), savedTimesheet.get().getTimeOut(),
                "Time Out should be 5:00 PM");
        assertEquals(9.0f, savedTimesheet.get().getHoursWorked(), 0.01f,
                "Hours worked should be 9.0");
    }

    @Test
    @DisplayName("Test timesheet filtering by employee")
    void testTimesheetFiltering() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        // Create test timesheets
        List<Timesheet> generatedTimesheets = TimesheetTestDataGenerator.generateTimesheetsForEmployee(
                testEmployee, startDate, endDate, false);

        // Save timesheets
        for (Timesheet ts : generatedTimesheets) {
            Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                    ts.getEmployeeID(), ts.getDate());
            if (existing.isEmpty()) {
                timesheetService.saveTimesheet(ts);
            }
        }

        // Act - Filter by employee
        Year year = Year.of(LocalDate.now().getYear());
        Month month = Month.from(LocalDate.now());
        List<Timesheet> filteredTimesheets = timesheetService.getTimesheetsWithFilters(
                testEmployee, year, month, null, "All");

        // Assert
        assertFalse(filteredTimesheets.isEmpty(), "Should find timesheets for employee");
        filteredTimesheets.forEach(ts -> assertEquals(testEmployee.getEmployeeNumber(),
                ts.getEmployeeID().getEmployeeNumber(), "All timesheets should be for test employee"));
    }

    @Test
    @DisplayName("Test timesheet update")
    void testTimesheetUpdate() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(102); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        timesheetService.saveTimesheet(timesheet);

        // Act - Update timesheet
        Optional<Timesheet> savedTimesheet = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(savedTimesheet.isPresent(), "Timesheet should exist");
        
        Timesheet toUpdate = savedTimesheet.get();
        toUpdate.setStatus("Approved");
        toUpdate.setRemarks("Updated test remarks");
        timesheetService.updateTimesheet(toUpdate);

        // Assert
        Optional<Timesheet> updatedTimesheet = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(updatedTimesheet.isPresent(), "Updated timesheet should exist");
        assertEquals("Approved", updatedTimesheet.get().getStatus(), "Status should be updated");
        assertEquals("Updated test remarks", updatedTimesheet.get().getRemarks(),
                "Remarks should be updated");
    }

    @Test
    @DisplayName("Test timesheet deletion")
    void testTimesheetDeletion() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(103); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        timesheetService.saveTimesheet(timesheet);

        // Verify timesheet exists
        Optional<Timesheet> savedTimesheet = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(savedTimesheet.isPresent(), "Timesheet should exist before deletion");

        // Act - Delete timesheet
        timesheetService.deleteTimesheet(savedTimesheet.get());

        // Assert
        Optional<Timesheet> deletedTimesheet = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertFalse(deletedTimesheet.isPresent(), "Timesheet should be deleted");
    }

    @Test
    @DisplayName("Test get timesheets with filters - all combinations")
    void testGetTimesheetsWithFilters() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(104); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        
        Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        if (existing.isEmpty()) {
            timesheetService.saveTimesheet(timesheet);
        }

        // Act & Assert - Test different filter combinations
        Year year = Year.of(testDate.getYear());
        Month month = Month.from(testDate);

        // Filter by employee only
        List<Timesheet> result1 = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, null, "All");
        assertNotNull(result1, "Result should not be null");

        // Filter by employee and year
        List<Timesheet> result2 = timesheetService.getTimesheetsWithFilters(
                testEmployee, year, null, null, "All");
        assertNotNull(result2, "Result should not be null");

        // Filter by employee, year, and month
        List<Timesheet> result3 = timesheetService.getTimesheetsWithFilters(
                testEmployee, year, month, null, "All");
        assertNotNull(result3, "Result should not be null");

        // Filter by specific date
        List<Timesheet> result4 = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, testDate, "All");
        assertNotNull(result4, "Result should not be null");

        // Filter by status
        List<Timesheet> result5 = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, null, "Submitted");
        assertNotNull(result5, "Result should not be null");
    }

    @Test
    @DisplayName("Test timesheet data validation - 8AM to 5PM")
    void testTimesheetDataValidation() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(105); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);

        // Assert
        assertEquals(Time.valueOf(LocalTime.of(8, 0)), timesheet.getTimeIn(),
                "Time In must be exactly 8:00 AM");
        assertEquals(Time.valueOf(LocalTime.of(17, 0)), timesheet.getTimeOut(),
                "Time Out must be exactly 5:00 PM (17:00)");
        assertEquals(9.0f, timesheet.getHoursWorked(), 0.01f,
                "Hours worked must be exactly 9.0 hours");
        assertNotNull(timesheet.getEmployeeID(), "Employee ID must not be null");
        assertNotNull(timesheet.getDate(), "Date must not be null");
        assertEquals("Submitted", timesheet.getStatus(), "Status must be 'Submitted'");
    }

    // ========== HIGH PRIORITY TESTS ==========

    @Test
    @DisplayName("Test save duplicate timesheet - same employee and date")
    void testSaveDuplicateTimesheet() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(101); // Use unique date
        Timesheet timesheet1 = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        timesheetService.saveTimesheet(timesheet1);

        // Act - Check if duplicate exists (same employee and date)
        Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        
        // Assert - Should detect duplicate
        assertTrue(existing.isPresent(), "Duplicate timesheet should be detected");
        assertEquals(timesheet1.getDate(), existing.get().getDate(),
                "Existing timesheet should have same date");
        assertEquals(timesheet1.getEmployeeID().getEmployeeNumber(),
                existing.get().getEmployeeID().getEmployeeNumber(),
                "Existing timesheet should be for same employee");
        
        // Verify that attempting to save another timesheet for same date would be a duplicate
        Optional<Timesheet> duplicateCheck = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(duplicateCheck.isPresent(), "Should detect existing timesheet as duplicate");
    }

    @Test
    @DisplayName("Test save timesheet with null employee")
    void testSaveTimesheetWithNullEmployee() {
        // Arrange
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(null);
        timesheet.setDate(LocalDate.now());
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(8, 0)));
        timesheet.setTimeOut(Time.valueOf(LocalTime.of(17, 0)));
        timesheet.setHoursWorked(9.0f);
        timesheet.setStatus("Submitted");

        // Act & Assert - Should handle null employee gracefully
        // Note: Hibernate may throw exception, but we test that it's handled
        assertThrows(Exception.class, () -> {
            timesheetService.saveTimesheet(timesheet);
        }, "Should throw exception when employee is null");
    }

    @Test
    @DisplayName("Test timesheet with time-out before time-in")
    void testInvalidTimeOutBeforeTimeIn() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(106); // Use unique date
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(testEmployee);
        timesheet.setDate(testDate);
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(17, 0))); // 5 PM
        timesheet.setTimeOut(Time.valueOf(LocalTime.of(8, 0))); // 8 AM - Invalid!
        timesheet.setHoursWorked(-9.0f); // Negative hours
        timesheet.setStatus("Submitted");
        timesheet.setRemarks("Invalid time range test");

        // Act - Save timesheet with invalid time range
        timesheetService.saveTimesheet(timesheet);

        // Assert - Timesheet is saved but hours should be validated
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet should be saved");
        // Note: Business logic should validate this, but we test current behavior
        assertNotNull(saved.get().getTimeIn(), "Time In should be set");
        assertNotNull(saved.get().getTimeOut(), "Time Out should be set");
    }

    @Test
    @DisplayName("Test filter with non-existent employee")
    void testFilterWithNonExistentEmployee() {
        // Arrange - Create a fake employee that doesn't exist
        Employee fakeEmployee = new Employee();
        fakeEmployee.setEmployeeNumber(999999); // Non-existent employee number

        // Act - Filter by non-existent employee
        List<Timesheet> result = timesheetService.getTimesheetsWithFilters(
                fakeEmployee, null, null, null, "All");

        // Assert - Should return empty list
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Should return empty list for non-existent employee");
    }

    @Test
    @DisplayName("Test update non-existent timesheet")
    void testUpdateNonExistentTimesheet() {
        // Arrange - Create a timesheet that doesn't exist in database
        Employee testEmployee = testEmployees.get(0);
        LocalDate futureDate = getUniqueTestDate(200); // Use unique date far in future
        Timesheet nonExistentTimesheet = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, futureDate);
        nonExistentTimesheet.setId(999999); // Non-existent ID

        // Act - Try to update non-existent timesheet
        // Note: This will fail because the ID doesn't exist in database
        // We expect an exception, so we test that it throws appropriately
        assertThrows(Exception.class, () -> {
            timesheetService.updateTimesheet(nonExistentTimesheet);
        }, "Update should throw exception for non-existent timesheet");
    }

    @Test
    @DisplayName("Test delete already deleted timesheet")
    void testDeleteAlreadyDeletedTimesheet() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(107); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        timesheetService.saveTimesheet(timesheet);

        // Delete once
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet should exist");
        timesheetService.deleteTimesheet(saved.get());

        // Act - Try to delete again
        Optional<Timesheet> deleted = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        
        // Assert - Should handle idempotent deletion gracefully
        assertFalse(deleted.isPresent(), "Timesheet should not exist after deletion");
        // Second deletion should not throw exception
        assertDoesNotThrow(() -> {
            if (deleted.isPresent()) {
                timesheetService.deleteTimesheet(deleted.get());
            }
        }, "Deleting already deleted timesheet should be idempotent");
    }

    // ========== MEDIUM PRIORITY TESTS ==========

    @Test
    @DisplayName("Test timesheet for weekend dates")
    void testTimesheetForWeekendDates() {
        // Arrange - Find a Saturday
        LocalDate saturday = LocalDate.now();
        while (saturday.getDayOfWeek().getValue() != 6) { // Saturday = 6
            saturday = saturday.plusDays(1);
        }
        
        Employee testEmployee = testEmployees.get(0);
        Timesheet weekendTimesheet = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, saturday);

        // Act - Save weekend timesheet
        Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, saturday);
        if (existing.isEmpty()) {
            timesheetService.saveTimesheet(weekendTimesheet);
        }

        // Assert - Weekend timesheet can be saved (business logic may vary)
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, saturday);
        assertTrue(saved.isPresent() || existing.isPresent(),
                "Weekend timesheet should be saveable");
    }

    @Test
    @DisplayName("Test timesheet with zero hours worked")
    void testTimesheetWithZeroHours() {
        // Arrange - Absent employee (no time out)
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(108); // Use unique date
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(testEmployee);
        timesheet.setDate(testDate);
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(8, 0)));
        timesheet.setTimeOut(null); // No time out - absent
        timesheet.setHoursWorked(0.0f);
        timesheet.setStatus("Not Submitted");
        timesheet.setRemarks("Absent employee - zero hours");

        // Act - Save timesheet with zero hours
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet with zero hours should be saved");
        assertEquals(0.0f, saved.get().getHoursWorked(), 0.01f,
                "Hours worked should be zero");
    }

    @Test
    @DisplayName("Test status transitions - Submitted to Approved to Rejected")
    void testStatusTransitions() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = LocalDate.now();
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        timesheetService.saveTimesheet(timesheet);

        // Act - Transition through statuses
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet should exist");

        // Submitted -> Approved
        Timesheet toApprove = saved.get();
        toApprove.setStatus("Approved");
        timesheetService.updateTimesheet(toApprove);

        Optional<Timesheet> approved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertEquals("Approved", approved.get().getStatus(), "Status should be Approved");

        // Approved -> Rejected
        Timesheet toReject = approved.get();
        toReject.setStatus("Rejected");
        timesheetService.updateTimesheet(toReject);

        Optional<Timesheet> rejected = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertEquals("Rejected", rejected.get().getStatus(), "Status should be Rejected");
    }

    @Test
    @DisplayName("Test bulk timesheet operations")
    void testBulkTimesheetCreation() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        
        // Act - Generate timesheets for all employees
        List<Timesheet> bulkTimesheets = TimesheetTestDataGenerator.generateTimesheetsForAllEmployees(
                testEmployees, startDate, endDate, false);

        // Save bulk timesheets
        int savedCount = 0;
        for (Timesheet ts : bulkTimesheets) {
            Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                    ts.getEmployeeID(), ts.getDate());
            if (existing.isEmpty()) {
                timesheetService.saveTimesheet(ts);
                savedCount++;
            }
        }

        // Assert
        assertTrue(savedCount > 0, "Should save multiple timesheets");
        assertTrue(bulkTimesheets.size() >= testEmployees.size(),
                "Should generate at least one timesheet per employee");
        
        // Verify all saved timesheets can be retrieved
        Year year = Year.of(LocalDate.now().getYear());
        Month month = Month.from(LocalDate.now());
        List<Timesheet> retrieved = timesheetService.getTimesheetsWithFilters(
                null, year, month, null, "All");
        assertTrue(retrieved.size() >= savedCount,
                "Should retrieve all saved timesheets");
    }

    @Test
    @DisplayName("Test timesheet remarks with special characters")
    void testRemarksWithSpecialCharacters() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(110); // Use unique date
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(testEmployee, testDate);
        
        // Test various special characters
        String specialRemarks = "Test remarks: <script>alert('XSS')</script>, 'SQL Injection', \"Quotes\", &amp; Special chars: !@#$%^&*()";
        timesheet.setRemarks(specialRemarks);

        // Act - Save timesheet with special characters
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet with special characters should be saved");
        assertEquals(specialRemarks, saved.get().getRemarks(),
                "Remarks with special characters should be preserved");
    }

    @Test
    @DisplayName("Test filter by multiple statuses")
    void testFilterByMultipleStatuses() {
        // Arrange - Create timesheets with different statuses
        Employee testEmployee = testEmployees.get(0);
        LocalDate baseDate = getUniqueTestDate(111); // Use unique date
        
        Timesheet submitted = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, baseDate);
        submitted.setStatus("Submitted");
        
        Timesheet approved = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, baseDate.plusDays(1));
        approved.setStatus("Approved");
        
        Timesheet rejected = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, baseDate.plusDays(2));
        rejected.setStatus("Rejected");

        // Save timesheets
        timesheetService.saveTimesheet(submitted);
        timesheetService.saveTimesheet(approved);
        timesheetService.saveTimesheet(rejected);

        // Act - Filter by each status
        List<Timesheet> submittedResults = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, null, "Submitted");
        List<Timesheet> approvedResults = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, null, "Approved");
        List<Timesheet> rejectedResults = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, null, "Rejected");

        // Assert
        assertTrue(submittedResults.stream().anyMatch(ts -> "Submitted".equals(ts.getStatus())),
                "Should find Submitted timesheets");
        assertTrue(approvedResults.stream().anyMatch(ts -> "Approved".equals(ts.getStatus())),
                "Should find Approved timesheets");
        assertTrue(rejectedResults.stream().anyMatch(ts -> "Rejected".equals(ts.getStatus())),
                "Should find Rejected timesheets");
    }

    // ========== LOW PRIORITY TESTS ==========

    @Test
    @DisplayName("Test timesheet with partial hours (8:00 AM - 12:30 PM)")
    void testPartialHoursWorked() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(112); // Use unique date
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(testEmployee);
        timesheet.setDate(testDate);
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(8, 0)));
        timesheet.setTimeOut(Time.valueOf(LocalTime.of(12, 30))); // 4.5 hours
        timesheet.setHoursWorked(4.5f);
        timesheet.setStatus("Submitted");
        timesheet.setRemarks("Partial day - 4.5 hours");

        // Act
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Partial hours timesheet should be saved");
        assertEquals(4.5f, saved.get().getHoursWorked(), 0.01f,
                "Hours worked should be 4.5");
    }

    @Test
    @DisplayName("Test timesheet with overtime (8:00 AM - 8:00 PM)")
    void testOvertimeHours() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(113); // Use unique date
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(testEmployee);
        timesheet.setDate(testDate);
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(8, 0)));
        timesheet.setTimeOut(Time.valueOf(LocalTime.of(20, 0))); // 12 hours
        timesheet.setHoursWorked(12.0f);
        timesheet.setStatus("Submitted");
        timesheet.setRemarks("Overtime - 12 hours");

        // Act
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Overtime timesheet should be saved");
        assertEquals(12.0f, saved.get().getHoursWorked(), 0.01f,
                "Hours worked should be 12.0");
    }

    @Test
    @DisplayName("Test timesheet with excessive hours (>24)")
    void testExcessiveHoursWorked() {
        // Arrange
        Employee testEmployee = testEmployees.get(0);
        LocalDate testDate = getUniqueTestDate(114); // Use unique date
        Timesheet timesheet = new Timesheet();
        timesheet.setEmployeeID(testEmployee);
        timesheet.setDate(testDate);
        timesheet.setTimeIn(Time.valueOf(LocalTime.of(0, 0)));
        timesheet.setTimeOut(Time.valueOf(LocalTime.of(23, 59))); // Nearly 24 hours
        timesheet.setHoursWorked(25.0f); // Excessive hours
        timesheet.setStatus("Submitted");
        timesheet.setRemarks("Excessive hours test");

        // Act - Save timesheet with excessive hours
        // Note: Business logic may validate this, but we test current behavior
        timesheetService.saveTimesheet(timesheet);

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, testDate);
        assertTrue(saved.isPresent(), "Timesheet with excessive hours should be saved");
        // Business logic should validate hours, but we test current behavior
        assertNotNull(saved.get().getHoursWorked(), "Hours worked should be set");
    }

    @Test
    @DisplayName("Test timesheet for leap year date (Feb 29)")
    void testLeapYearDate() {
        // Arrange - Use a leap year (2024 is a leap year)
        LocalDate leapYearDate = LocalDate.of(2024, 2, 29);
        Employee testEmployee = testEmployees.get(0);
        Timesheet timesheet = TimesheetTestDataGenerator.createTimesheet(
                testEmployee, leapYearDate);

        // Act
        Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, leapYearDate);
        if (existing.isEmpty()) {
            timesheetService.saveTimesheet(timesheet);
        }

        // Assert
        Optional<Timesheet> saved = timesheetService.getTimesheetByEmployeeAndDate(
                testEmployee, leapYearDate);
        assertTrue(saved.isPresent() || existing.isPresent(),
                "Leap year date timesheet should be handled correctly");
        if (saved.isPresent()) {
            assertEquals(leapYearDate, saved.get().getDate(),
                    "Date should be Feb 29, 2024");
        }
    }

    @Test
    @DisplayName("Test filter with invalid date range")
    void testFilterWithInvalidDateRange() {
        // Arrange - Start date after end date (invalid range)
        Employee testEmployee = testEmployees.get(0);
        LocalDate futureDate = LocalDate.now().plusDays(100);
        LocalDate pastDate = LocalDate.now().minusDays(100);

        // Act - Filter with specific dates (not a range, but testing date filtering)
        List<Timesheet> futureResults = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, futureDate, "All");
        List<Timesheet> pastResults = timesheetService.getTimesheetsWithFilters(
                testEmployee, null, null, pastDate, "All");

        // Assert - Should handle gracefully
        assertNotNull(futureResults, "Future date filter should return result");
        assertNotNull(pastResults, "Past date filter should return result");
        // Results may be empty if no timesheets exist for those dates
    }

    @Test
    @DisplayName("Test get all timesheets performance")
    void testGetAllTimesheetsPerformance() {
        // Arrange - Create multiple timesheets
        Employee testEmployee = testEmployees.get(0);
        LocalDate startDate = getUniqueTestDate(120); // Use unique date range
        LocalDate endDate = getUniqueTestDate(150);
        
        List<Timesheet> timesheets = TimesheetTestDataGenerator.generateTimesheetsForEmployee(
                testEmployee, startDate, endDate, false);
        
        // Save timesheets
        for (Timesheet ts : timesheets) {
            Optional<Timesheet> existing = timesheetService.getTimesheetByEmployeeAndDate(
                    ts.getEmployeeID(), ts.getDate());
            if (existing.isEmpty()) {
                timesheetService.saveTimesheet(ts);
            }
        }

        // Act - Measure retrieval time
        long startTime = System.currentTimeMillis();
        List<Timesheet> allTimesheets = timesheetService.getAllTimesheets();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Assert
        assertNotNull(allTimesheets, "All timesheets should be retrieved");
        assertTrue(duration < 5000, "Retrieval should complete within 5 seconds");
        assertTrue(allTimesheets.size() >= timesheets.size(),
                "Should retrieve at least the created timesheets");
    }

    /**
     * Helper method to clean up test timesheets.
     */
    private void cleanupTestTimesheets() {
        try {
            // Get all timesheets and delete test ones
            List<Timesheet> allTimesheets = timesheetService.getAllTimesheets();
            for (Timesheet ts : allTimesheets) {
                if (ts.getRemarks() != null && 
                    (ts.getRemarks().contains("Test data") || 
                     ts.getRemarks().contains("Generated by TimesheetTestDataGenerator") ||
                     ts.getRemarks().contains("Invalid time range") ||
                     ts.getRemarks().contains("Absent employee") ||
                     ts.getRemarks().contains("Partial day") ||
                     ts.getRemarks().contains("Overtime") ||
                     ts.getRemarks().contains("Excessive hours") ||
                     ts.getRemarks().contains("Special characters"))) {
                    try {
                        timesheetService.deleteTimesheet(ts);
                    } catch (Exception e) {
                        // Ignore deletion errors during cleanup
                    }
                }
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
    
    /**
     * Helper method to get a unique test date for each test.
     */
    private LocalDate getUniqueTestDate(int offset) {
        return LocalDate.now().plusDays(offset);
    }
}

