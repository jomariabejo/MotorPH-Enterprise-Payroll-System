package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class HRDashboardController {

    @FXML
    private BarChart<String, Double> barchart_monthly_leave_request;

    @FXML
    private ComboBox<String> comboBoxMonth;

    @FXML
    private ComboBox<Integer> comboBoxYear;

    @FXML
    private Label lbl_active_employees;

    @FXML
    private Label lbl_total_employees;

    @FXML
    private LineChart<String, BigDecimal> line_chart_avg_hours_worked_per_employee;

    @FXML
    private Label total_leave_request;


    @FXML
    void reviewClicked() {
        clearCharts();
        populateAverageHoursWorkedByEmployee();
        populateBarchartLeaveRequestByEmployee();
    }

    private void clearCharts() {
        this.line_chart_avg_hours_worked_per_employee.getData().clear();
        this.barchart_monthly_leave_request.getData().clear();
    }

    @FXML
    private void initialize() {
        setupComboBox();
        writeNumberOfEmployees();
        writeNumberOfActiveEmployees();
        writeNumberOfLeaveRequest();
    }


    private void populateBarchartLeaveRequestByEmployee() {
        // setup series data for our barchart
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Number of Leave Requests");

        String query = "SELECT CONCAT(e.first_name, ' ', e.last_name), COUNT(lr.leave_request_id) AS total_leave_requests\n" +
                "FROM leave_request lr\n" +
                "JOIN employee e ON lr.employee_id = e.employee_id\n" +
                "WHERE MONTH(lr.start_date) = ? AND YEAR(lr.start_date) = ?\n" +
                "GROUP BY e.employee_id\n" +
                "ORDER BY e.employee_id";

        // Get selected month and year from comboBoxMonth and comboBoxYear
        int selectedMonthIndex = comboBoxMonth.getSelectionModel().getSelectedIndex() + 1; // Month index is 0-based
        int selectedYear = comboBoxYear.getSelectionModel().getSelectedItem();

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, selectedMonthIndex);
            pstmt.setInt(2, selectedYear);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String employeeName = rs.getString(1);
                double totalLeaveRequests = rs.getInt("total_leave_requests");
                series.getData().add(new XYChart.Data<>(employeeName, totalLeaveRequests));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.barchart_monthly_leave_request.getData().add(series);
    }


    private void populateAverageHoursWorkedByEmployee() {
        String query = "\n" +
                "SELECT\n" +
                "    CONCAT(e.first_name, ' ', e.last_name) AS employee_name,\n" +
                "    ROUND(\n" +
                "        AVG(\n" +
                "            CASE\n" +
                "                WHEN TIME(t.time_in) < '08:00:00' THEN (\n" +
                "                    TIME_TO_SEC(TIMEDIFF('08:00:00', t.time_in)) / 3600.0\n" +
                "                )\n" +
                "                WHEN TIME(t.time_in) >= '08:00:00' AND TIME(t.time_in) <= '17:00:00' THEN (\n" +
                "                    TIME_TO_SEC(TIMEDIFF(t.time_out, t.time_in)) / 3600.0\n" +
                "                )\n" +
                "                ELSE 0  -- Exclude cases where time_in > '17:00:00'\n" +
                "            END\n" +
                "        ),\n" +
                "        2\n" +
                "    ) AS avg_regular_hours_worked\n" +
                "FROM\n" +
                "    timesheet t\n" +
                "    JOIN employee e ON t.employee_id = e.employee_id\n" +
                "WHERE\n" +
                "    e.isActive = 1\n" +
                "    AND MONTH(t.date) = ?\n" +
                "    AND YEAR(t.date) = ?\n" +
                "GROUP BY\n" +
                "    e.employee_id, CONCAT(e.first_name, ' ', e.last_name)\n" +
                "ORDER BY\n" +
                "    avg_regular_hours_worked;\n";


        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            // Get selected month and year from comboBoxMonth and comboBoxYear
            int selectedMonthIndex = comboBoxMonth.getSelectionModel().getSelectedIndex() + 1; // Month index is 0-based
            int selectedYear = comboBoxYear.getSelectionModel().getSelectedItem();

            // Set parameters
            pstmt.setInt(1, selectedMonthIndex);
            pstmt.setInt(2, selectedYear);

            ResultSet rs = pstmt.executeQuery();

            // setup line chart
            XYChart.Series<String, BigDecimal> averageRegularHoursWorked = new XYChart.Series<>();
            averageRegularHoursWorked.setName("Average Regular Hours Worked");

            while (rs.next()) {
                String employeeName = rs.getString(1);
                BigDecimal employeeAverageRegularHoursWorked = rs.getBigDecimal(2);
                averageRegularHoursWorked.getData().add(new XYChart.Data<>(
                        employeeName,
                        employeeAverageRegularHoursWorked
                ));
            }
            line_chart_avg_hours_worked_per_employee.getData().add(averageRegularHoursWorked);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeNumberOfLeaveRequest() {
        String query = "SELECT COUNT(*) as total_leave_request " +
                "FROM leave_request " +
                "WHERE MONTH(leave_request.start_date) = ? " +
                "AND YEAR(leave_request.start_date) = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            // Get selected month and year from comboBoxMonth and comboBoxYear
            int selectedMonthIndex = comboBoxMonth.getSelectionModel().getSelectedIndex() + 1; // Month index is 0-based
            int selectedYear = comboBoxYear.getSelectionModel().getSelectedItem();

            // Set parameters
            pstmt.setInt(1, selectedMonthIndex);
            pstmt.setInt(2, selectedYear);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                total_leave_request.setText(String.valueOf(rs.getInt("total_leave_request")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeNumberOfActiveEmployees() {
        String query = "SELECT COUNT(*) as total_active_employees FROM employee WHERE employee.isActive = 1;";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lbl_active_employees.setText(String.valueOf(rs.getInt("total_active_employees")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeNumberOfEmployees() {
        String query = "SELECT COUNT(*) as total_employees FROM employee";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lbl_total_employees.setText(String.valueOf(rs.getInt("total_employees")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupComboBox() {
        includeMonthlyComboBox();
        includeYearlyComboBox();
        selectCurrentMonthAndYear();
    }

    private void includeMonthlyComboBox() {
        // Populate month ComboBox
        comboBoxMonth.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
    }

    private void selectCurrentMonthAndYear() {
        // Get current date
        LocalDate currentDate = LocalDate.now();

        // Set current month in comboBoxMonth
        int currentMonth = currentDate.getMonthValue();
        comboBoxMonth.getSelectionModel().select(currentMonth - 1); // -1 because ComboBox is zero-indexed

        // Set current year in comboBoxYear
        int currentYear = currentDate.getYear();
        comboBoxYear.getSelectionModel().select(Integer.valueOf(currentYear));
    }

    // We will include only year between 2000 & 2050
    private void includeYearlyComboBox() {
        String query = "SELECT DISTINCT YEAR(timesheet.date) AS year FROM timesheet;";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.comboBoxYear.getItems().add(rs.getInt(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
