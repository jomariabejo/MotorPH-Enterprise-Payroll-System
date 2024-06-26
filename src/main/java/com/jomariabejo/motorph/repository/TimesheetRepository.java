package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.record.GrossIncome;
import com.jomariabejo.motorph.record.RegularHoursWorked;
import com.jomariabejo.motorph.utility.TextReader;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class TimesheetRepository {
    public final String ALLOWANCE_QUERY_BASE_PATH = "src/main/java/com/jomariabejo/motorph/query/timesheet";

    public boolean createTimesheet(Timesheet timesheet) {
        String query = "INSERT INTO payroll_sys.timesheet (date, time_in, employee_id) VALUES (?,?,?);";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, timesheet.getDate());
            ps.setTime(2, timesheet.getTimeIn());
            ps.setInt(3, timesheet.getEmployeeId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0; // returns true if insert data success
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTimesheetPageCount() {
        int pageCount = 0;
        try (Connection conn = DatabaseConnectionUtility.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM payroll_sys.timesheet");
            if (rs.next()) {
                int totalCount = rs.getInt("total");
                pageCount = (int) Math.ceil((double) totalCount / 100);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageCount;
    }

    public int getTimesheetPageCount(int employeeId) {
        int pageCount = 0;
        try (Connection conn = DatabaseConnectionUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS total FROM payroll_sys.timesheet WHERE employee_id = ?")) {
            stmt.setInt(1, employeeId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int totalCount = rs.getInt("total");
                pageCount = (int) Math.ceil((double) totalCount / 100);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pageCount;
    }


    public ArrayList<Timesheet> getTimesheets() throws SQLException {
        String query = "SELECT * FROM payroll_sys.timesheet";
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    timesheets.add(new Timesheet(
                            rs.getInt("timesheet_id"),
                            rs.getDate("date"),
                            rs.getTime("time_in"),
                            rs.getTime("time_out"),
                            rs.getInt("employee_id")
                    ));
                }
            }
        }
        return timesheets;
    }

    public Optional<Timesheet> getTimesheetsById(int timesheetId) throws SQLException {
        String query = TextReader.readTextFile(ALLOWANCE_QUERY_BASE_PATH + "/get_timesheets_by_employeeID.sql");
        Optional<Timesheet> timesheet = Optional.empty();

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, timesheetId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    timesheet = Optional.of(new Timesheet(
                            rs.getInt("timesheet_id"),
                            rs.getDate("date"),
                            rs.getTime("time_in"),
                            rs.getTime("time_out"),
                            rs.getInt("employee_id")
                    ));
                }
            }
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
            throw e;
        }

        return timesheet;
    }

    public ArrayList<Timesheet> getTimesheetsByEmployeeId(int employeeId) throws SQLException {
        String query = TextReader.readTextFile(ALLOWANCE_QUERY_BASE_PATH + "/get_timesheets_by_employeeID.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            ArrayList<Timesheet> timesheets = new ArrayList<>();

            while (rs.next()) {
                timesheets.add(
                        new Timesheet(
                                rs.getInt("timesheet_id"),
                                rs.getDate("date"),
                                rs.getTime("time_in"),
                                rs.getTime("time_out"),
                                rs.getInt("employee_id")
                        )
                );
            }
            return timesheets;
        }
    }

    public ArrayList<Timesheet> getTimesheetsByPayPeriodAndEmployeeId(Date startPayPeriod, Date endPayPeriod, int employeeId) throws SQLException {
        String query = TextReader.readTextFile(ALLOWANCE_QUERY_BASE_PATH + "/get_timesheet_by_payperiod_and_employeeId.sql");
        ArrayList<Timesheet> timesheets = new ArrayList<>();

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDate(1, startPayPeriod);
            ps.setDate(2, endPayPeriod);
            ps.setInt(3, employeeId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                timesheets.add(
                        new Timesheet(
                                rs.getInt("timesheet_id"),
                                rs.getDate("date"),
                                rs.getTime("time_in"),
                                rs.getTime("time_out"),
                                rs.getInt("employee_id")
                        )
                );
            }
        }
        return timesheets;
    }

    public ArrayList<Timesheet> getAllTimesheets() throws SQLException {
        String query = "SELECT * FROM timsheet";
        ArrayList<Timesheet> timesheets = new ArrayList<>();

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                timesheets.add(
                        new Timesheet(
                                rs.getInt("timesheet_id"),
                                rs.getDate("date"),
                                rs.getTime("time_in"),
                                rs.getTime("time_out"),
                                rs.getInt("employee_id")
                        )
                );
            }
        }
        return timesheets;
    }

    public ArrayList<Timesheet> getTimesheetsByMonthAndYear(int month, int year) {
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        String query = TextReader.readTextFile(ALLOWANCE_QUERY_BASE_PATH + "/getTimesheetsByMonthAndYear.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timesheet timesheet = new Timesheet(
                        rs.getInt("timesheet_id"),
                        rs.getDate("date"),
                        rs.getTime("time_in"),
                        rs.getTime("time_out"),
                        rs.getInt("employee_id")
                );
                timesheets.add(timesheet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timesheets;
    }

    public void updateTimesheet(Timesheet timesheet) {
        String query = TextReader.readTextFile(ALLOWANCE_QUERY_BASE_PATH + "/update_timesheet.sql");

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setDate(1, timesheet.getDate());
            ps.setTime(2, timesheet.getTimeIn());
            ps.setTime(3, timesheet.getTimeOut());
            ps.setInt(4, timesheet.getEmployeeId());
            ps.setInt(5, timesheet.getTimesheetId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Timesheet updated successfully!");
            } else {
                System.out.println("Failed to update timesheet.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateTimesheet(int employeeId, Date date, Time timeOut) throws SQLException {

        String query = "UPDATE TIMESHEET SET time_out = ? WHERE employee_id = ? AND date = ? AND time_out IS NULL;";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setTime(1, timeOut);
            ps.setInt(2, employeeId);
            ps.setDate(3, date);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected == 1; // one means updated successfully
        }
    }

    public ArrayList<Timesheet> fetchTimesheetsForPage(int pageIndex, int rowsPerPage) throws SQLException {
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        String query = "SELECT * FROM payroll_sys.timesheet ORDER BY date LIMIT ? OFFSET ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            int offset = pageIndex * rowsPerPage;

            ps.setInt(1, rowsPerPage);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    timesheets.add(new Timesheet(
                            rs.getInt("timesheet_id"),
                            rs.getDate("date"),
                            rs.getTime("time_in"),
                            rs.getTime("time_out"),
                            rs.getInt("employee_id")
                    ));
                }
            }
        }
        return timesheets;
    }

    public ArrayList<Timesheet> fetchTimesheetsForPage(int employeeId, int pageIndex, int rowsPerPage) throws SQLException {
        ArrayList<Timesheet> timesheets = new ArrayList<>();
        String query = "SELECT * FROM payroll_sys.timesheet WHERE employee_id = ? ORDER BY date LIMIT ? OFFSET ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            int offset = pageIndex * rowsPerPage;

            ps.setInt(1, employeeId);
            ps.setInt(2, rowsPerPage);
            ps.setInt(3, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    timesheets.add(new Timesheet(
                            rs.getInt("timesheet_id"),
                            rs.getDate("date"),
                            rs.getTime("time_in"),
                            rs.getTime("time_out"),
                            rs.getInt("employee_id")
                    ));
                }
            }
        }
        return timesheets;
    }


    public boolean checkIfEmployeeHasTimesheetRecords(int employeeId) {
        String query = "SELECT COUNT(*) FROM payroll_sys.timesheet WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            // Handle any potential exceptions, log or rethrow if necessary
            e.printStackTrace();
        }

        // Default return false if there's an exception or no records found
        return false;
    }

    public boolean checkIfEmployeeHasTimesheetRecordsToday(int employeeId) {
        String query = "SELECT COUNT(*) FROM payroll_sys.timesheet WHERE employee_id = ? AND date = ? AND time_in IS NOT NULL";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ps.setDate(2, Date.valueOf(LocalDate.now())); // Set the current date

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            // Handle any potential exceptions, log or rethrow if necessary
            e.printStackTrace();
        }

        // Default return false if there's an exception or no records found
        return false;
    }

    public int countEmployeeTimesheets(int employeeId) {
        String query = "SELECT COUNT(*) AS NumberOfTimesheets FROM payroll_sys.timesheet WHERE employee_id = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, employeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<RegularHoursWorked> fetchRegularHoursWorked(Date startDate, Date endDate) {
        ArrayList<RegularHoursWorked> regularHoursWorkedList = new ArrayList<>();
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\timesheet\\calculate_employee_regular_hours_worked.sql");

        try
                (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Retrieve employee_id and regular_hours_worked from the result set
                int employeeId = resultSet.getInt("employee_id");
                BigDecimal regularHoursWorked = resultSet.getBigDecimal("regular_hours_worked");
                // Add the result to the list
                regularHoursWorkedList.add(new RegularHoursWorked(employeeId, regularHoursWorked));
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regularHoursWorkedList;
    }

    public ArrayList<GrossIncome> fetchGrossIncome(Date startDate, Date endDate) {
        ArrayList<GrossIncome> grossIncomes = new ArrayList<>();
        String query = TextReader.readTextFile("src\\main\\java\\com\\jomariabejo\\motorph\\query\\timesheet\\calculate_employee_salary_based_on_total_hours_worked.sql");

        try
                (Connection connection = DatabaseConnectionUtility.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                grossIncomes.add(
                        new GrossIncome(
                                rs.getInt("employee_id"),
                                rs.getString("employee_name"),
                                rs.getString("position"),
                                rs.getString("department"),
                                rs.getBigDecimal("basic_salary"),
                                rs.getBigDecimal("hourly_rate"),
                                rs.getBigDecimal("total_regular_hours_worked"),
                                rs.getBigDecimal("total_overtime_hours"),
                                rs.getBigDecimal("total_regular_hours_worked_salary"),
                                rs.getBigDecimal("total_overtime_hours_worked_salary"),
                                rs.getInt("rice_subsidy"),
                                rs.getInt("clothing_allowance"),
                                rs.getInt("phone_allowance"),
                                rs.getInt("total_benefits")
                        )
                );
                System.out.println("TOTAL_REGULAR_HOURS_WORKED_SALARY = " + rs.getBigDecimal("total_regular_hours_worked_salary"));
            }

            // Close the resources
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grossIncomes;
    }

    public ArrayList<Timesheet> fetchMyTimesheetRecords(int employeeId) {
        String query = "SELECT \n" +
                "        t.employee_id,\n" +
                "        t.timesheet_id,\n" +
                "        t.date,\n" +
                "        t.time_in,\n" +
                "        t.time_out,\n" +
                "        TIMEDIFF(\n" +
                "            CASE WHEN TIME(t.time_out) > '17:00:00' THEN '17:00:00' ELSE t.time_out END,\n" +
                "            CASE WHEN TIME(t.time_in) < '08:00:00' THEN '08:00:00' ELSE t.time_in END\n" +
                "        ) AS regular_hours_worked,\n" +
                "        CASE WHEN TIME(t.time_out) > '17:00:00' THEN \n" +
                "            TIMEDIFF(TIME(t.time_out), '17:00:00')\n" +
                "        ELSE '00:00:00' END AS overtime_hours_worked\n" +
                "    FROM timesheet t\n" +
                "    WHERE t.employee_id = ?\n ORDER BY t.date DESC";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Timesheet> myTimesheetRecords = new ArrayList<>();
            while (resultSet.next()) {
                Timesheet timesheet = new Timesheet();

                timesheet.setTimesheetId(resultSet.getInt("timesheet_id"));
                timesheet.setEmployeeId(resultSet.getInt("employee_id"));
                timesheet.setDate(resultSet.getDate("date"));
                timesheet.setTimeIn(resultSet.getTime("time_in"));
                timesheet.setTimeOut(resultSet.getTime("time_out"));

                try {
                    // Retrieve regular_hours_worked as a Time object
                    Time regularHoursWorked = resultSet.getTime("regular_hours_worked");

                    // Check if regular hours worked is negative or null
                    if (regularHoursWorked==null || regularHoursWorked.getTime() < Time.valueOf("00:00:00").getTime()) {
                        regularHoursWorked = Time.valueOf("00:00:00"); // Set regular hours worked to zero
                    }

                    // Retrieve overtime_hours_worked
                    Time overtimeHoursWorked = resultSet.getTime("overtime_hours_worked");

                    // Set regular_hours_worked and overtime_hours_worked
                    timesheet.setRegularHoursWorked(regularHoursWorked);
                    timesheet.setOvertimeHoursWorked(overtimeHoursWorked);
                } catch (SQLException e) {

                }
                myTimesheetRecords.add(timesheet);
            }

            return myTimesheetRecords;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Timesheet> fetchMyTimesheetDescending(int employeeId) {
        String query = "SELECT \n" +
                "        t.employee_id,\n" +
                "        t.timesheet_id,\n" +
                "        t.date,\n" +
                "        t.time_in,\n" +
                "        t.time_out,\n" +
                "        TIMEDIFF(\n" +
                "            CASE WHEN TIME(t.time_out) > '17:00:00' THEN '17:00:00' ELSE t.time_out END,\n" +
                "            CASE WHEN TIME(t.time_in) < '08:00:00' THEN '08:00:00' ELSE t.time_in END\n" +
                "        ) AS regular_hours_worked,\n" +
                "        CASE WHEN TIME(t.time_out) > '17:00:00' THEN \n" +
                "            TIMEDIFF(TIME(t.time_out), '17:00:00')\n" +
                "        ELSE '00:00:00' END AS overtime_hours_worked\n" +
                "    FROM timesheet t\n" +
                "    WHERE t.employee_id = ?\n" +
                "    ORDER BY t.date DESC"; // Order by date in descending order

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Timesheet> myTimesheetRecords = new ArrayList<>();
            while (resultSet.next()) {
                Timesheet timesheet = new Timesheet();

                timesheet.setTimesheetId(resultSet.getInt("timesheet_id"));
                timesheet.setEmployeeId(resultSet.getInt("employee_id"));
                timesheet.setDate(resultSet.getDate("date"));
                timesheet.setTimeIn(resultSet.getTime("time_in"));
                timesheet.setTimeOut(resultSet.getTime("time_out"));

                try {
                    // Retrieve regular_hours_worked as a Time object
                    Time regularHoursWorked = resultSet.getTime("regular_hours_worked");

                    // Check if regular hours worked is negative or null
                    if (regularHoursWorked==null || regularHoursWorked.getTime() < Time.valueOf("00:00:00").getTime()) {
                        regularHoursWorked = Time.valueOf("00:00:00"); // Set regular hours worked to zero
                    }

                    // Retrieve overtime_hours_worked
                    Time overtimeHoursWorked = resultSet.getTime("overtime_hours_worked");

                    // Set regular_hours_worked and overtime_hours_worked
                    timesheet.setRegularHoursWorked(regularHoursWorked);
                    timesheet.setOvertimeHoursWorked(overtimeHoursWorked);
                } catch (SQLException e) {

                }
                myTimesheetRecords.add(timesheet);
            }

            return myTimesheetRecords;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public ArrayList<Timesheet> fetchMyTimesheetAscending(int employeeId) {
        String query = "SELECT \n" +
                "        t.employee_id,\n" +
                "        t.timesheet_id,\n" +
                "        t.date,\n" +
                "        t.time_in,\n" +
                "        t.time_out,\n" +
                "        TIMEDIFF(\n" +
                "            CASE WHEN TIME(t.time_out) > '17:00:00' THEN '17:00:00' ELSE t.time_out END,\n" +
                "            CASE WHEN TIME(t.time_in) < '08:00:00' THEN '08:00:00' ELSE t.time_in END\n" +
                "        ) AS regular_hours_worked,\n" +
                "        CASE WHEN TIME(t.time_out) > '17:00:00' THEN \n" +
                "            TIMEDIFF(TIME(t.time_out), '17:00:00')\n" +
                "        ELSE '00:00:00' END AS overtime_hours_worked\n" +
                "    FROM timesheet t\n" +
                "    WHERE t.employee_id = ?\n" +
                "    ORDER BY t.date ASC, t.timesheet_id ASC"; // Order by date and timesheet_id

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Timesheet> myTimesheetRecords = new ArrayList<>();

            while (resultSet.next()) {
                Timesheet timesheet = new Timesheet(); // Move the creation of Timesheet object inside the loop

                timesheet.setTimesheetId(resultSet.getInt("timesheet_id"));
                timesheet.setEmployeeId(resultSet.getInt("employee_id"));
                timesheet.setDate(resultSet.getDate("date"));
                timesheet.setTimeIn(resultSet.getTime("time_in"));
                timesheet.setTimeOut(resultSet.getTime("time_out"));

                // Retrieve regular_hours_worked and overtime_hours_worked as strings
                String regularHoursWorked = resultSet.getString("regular_hours_worked");
                String overtimeHoursWorked = resultSet.getString("overtime_hours_worked");

                try {
                    // Set regular_hours_worked and overtime_hours_worked
                    timesheet.setRegularHoursWorked(Time.valueOf(regularHoursWorked));
                    timesheet.setOvertimeHoursWorked(Time.valueOf(overtimeHoursWorked));
                } catch (IllegalArgumentException e) {
                    if (regularHoursWorked == null) {
                        timesheet.setRegularHoursWorked(Time.valueOf("00:00:00"));
                    }
                    else if (regularHoursWorked.startsWith("-")) {
                        timesheet.setRegularHoursWorked(Time.valueOf("00:00:00"));
                    } else {
                        throw new RuntimeException("Error parsing time: " + e.getMessage(), e);
                    }
                }
            }
            return myTimesheetRecords;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


