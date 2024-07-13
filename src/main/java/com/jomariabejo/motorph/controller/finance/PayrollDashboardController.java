package com.jomariabejo.motorph.controller.finance;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.record.PayrollReport;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PayrollDashboardController {

    @FXML
    private BarChart<String, Number> barchartPayrollReportByDepartment;

    @FXML
    private DatePicker dp_start_pay_date;

    @FXML
    private DatePicker dp_end_pay_date;

    @FXML
    private PieChart piechart_departmentTotalEmployees;

    @FXML
    private StackedBarChart<?, ?> stackChartByDepartment;

    @FXML
    private TableView<?> tv_puntualEmployees;

    final CategoryAxis yAxisBarchart = new CategoryAxis();
    final NumberAxis xAxisBarchart = new NumberAxis();

    public PayrollDashboardController() {

    }

    @FXML
    void comboBoxMonthChanged(ActionEvent event) {

    }

    @FXML
    void comboBoxYearChanged(ActionEvent event) {

    }

    @FXML
    void reviewClicked(ActionEvent event) {
        setUpBarchart();
    }

    public boolean isStartPayDateNull() {
        return this.dp_start_pay_date.getValue() == null;
    }

    public boolean isEndPayDateNull() {
        return this.dp_start_pay_date.getValue() == null;
    }

    private void setUpBarchart() {
        if (isStartPayDateNull() || isEndPayDateNull()) {
            AlertUtility.showInformation("Please provide start pay date and end pay date", null, null);
        } else {
            fetchPayrollReportData();
        }
    }

    private void fetchPayrollReportData() {
        String query = "SELECT d.name AS department_name,\n" +
                "    SUM(p.gross_income) AS total_gross_income,\n" +
                "    SUM(p.net_income) AS total_net_income,\n" +
                "    SUM(a.total_amount) AS total_benefits,\n" +
                "    (SUM(dd.total_contribution) + SUM(t.withheld_tax)) AS total_deduction\n" +
                "FROM payslip p\n" +
                "JOIN employee e ON p.employee_id = e.employee_id\n" +
                "JOIN department d ON e.dept_id = d.dept_id\n" +
                "JOIN allowance a ON p.alw_id = a.alw_id\n" +
                "JOIN deduction dd ON p.deduction_id = dd.deduction_id\n" +
                "JOIN tax t ON p.tax_id = t.tax_id\n" +
                "WHERE p.pay_period_start = ?\n" +
                "    AND p.pay_period_end = ?\n" +
                "GROUP BY d.name";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            java.sql.Date startDate = java.sql.Date.valueOf(this.dp_start_pay_date.getValue());
            java.sql.Date endDate = java.sql.Date.valueOf(this.dp_end_pay_date.getValue());

            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);

            ResultSet resultSet = pstmt.executeQuery();
            XYChart.Series<String, Number> seriesGrossIncomes = new XYChart.Series<>();
            seriesGrossIncomes.setName("Gross Incomes");
            XYChart.Series<String, Number> seriesBenefits = new XYChart.Series<>();
            seriesBenefits.setName("Benefits");
            XYChart.Series<String, Number> seriesDeductions = new XYChart.Series<>();
            seriesDeductions.setName("Deductions");
            XYChart.Series<String, Number> seriesNetIncomes = new XYChart.Series<>();
            seriesNetIncomes.setName("Net Incomes");

            while (resultSet.next()) {
                String departmentName = resultSet.getString("department_name");
                int totalGrossIncome = resultSet.getInt("total_gross_income");
                int totalNetIncome = resultSet.getInt("total_net_income");
                int totalBenefits = resultSet.getInt("total_benefits");
                int totalDeduction = resultSet.getInt("total_deduction");

                // Handle null values, if necessary
                if (departmentName != null) {
                    seriesGrossIncomes.getData().add(new XYChart.Data<>(departmentName, totalGrossIncome));
                    seriesBenefits.getData().add(new XYChart.Data<>(departmentName, totalBenefits));
                    seriesDeductions.getData().add(new XYChart.Data<>(departmentName, totalDeduction));
                    seriesNetIncomes.getData().add(new XYChart.Data<>(departmentName, totalNetIncome));
                }
            }

            // Clear existing data and add new series
            barchartPayrollReportByDepartment.getData().clear();
            barchartPayrollReportByDepartment.getData().addAll(seriesGrossIncomes, seriesBenefits, seriesDeductions, seriesNetIncomes);

            // Handle empty chart data
            if (seriesGrossIncomes.getData().isEmpty() && seriesBenefits.getData().isEmpty() &&
                    seriesDeductions.getData().isEmpty() && seriesNetIncomes.getData().isEmpty()) {
                AlertUtility.showInformation("No payslips found",
                        "For start pay date: " + dp_start_pay_date.getValue() + " and end pay date: " + dp_end_pay_date.getValue(), null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
