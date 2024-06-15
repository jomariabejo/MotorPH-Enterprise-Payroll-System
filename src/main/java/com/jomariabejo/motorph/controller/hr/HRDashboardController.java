package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class HRDashboardController {

    @FXML
    private BarChart<String, Number> barchart_monthly_leave_request;

    @FXML
    private ComboBox<String> comboBoxMonth;

    @FXML
    private ComboBox<Integer> comboBoxYear;

    @FXML
    private Label lbl_active_employees;

    @FXML
    private Label lbl_total_employees;

    @FXML
    private LineChart<Number, Number> line_chart_monthly_leave_request;

    @FXML
    private Label total_leave_request;

    @FXML
    void comboBoxMonthChanged(ActionEvent event) {

    }

    @FXML
    void reviewClicked(ActionEvent event) {

    }

    @FXML
    private void initialize() {
        setupComboBox();
        writeNumberOfEmployees();
        writeNumberOfActiveEmployees();
        writeNumberOfLeaveRequest();
        populateDailyAttendanceByEmployee();
        populateBarchartLeaveRequestByEmployee();
    }


    private void populateBarchartLeaveRequestByEmployee() {
        // setup series data for our barchart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
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
                System.out.printf(employeeName);
                int totalLeaveRequests = rs.getInt("total_leave_requests");
                series.getData().add(new XYChart.Data<>(employeeName, totalLeaveRequests));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        this.barchart_monthly_leave_request.getData().add(series);
    }


    private void populateDailyAttendanceByEmployee() {

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
             PreparedStatement pstmt = connection.prepareStatement(query)){
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
            PreparedStatement pstmt = connection.prepareStatement(query)){
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
             PreparedStatement pstmt = connection.prepareStatement(query)){

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.comboBoxYear.getItems().add(rs.getInt(1));
            }

        }
        catch (Exception e) {

        }
    }
}
