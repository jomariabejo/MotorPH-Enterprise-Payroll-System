package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyProfileController {

    @FXML
    private PieChart chart_allowance;

    @FXML
    private TextField tf_address;

    @FXML
    private TextField tf_basic_salary;

    @FXML
    private TextField tf_birthday;

    @FXML
    private TextField tf_clothing_allowance;

    @FXML
    private TextField tf_employee_id;

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_hourly_rate;

    @FXML
    private TextField tf_last_name;

    @FXML
    private TextField tf_phone_allowance;

    @FXML
    private TextField tf_position;

    @FXML
    private TextField tf_rice_subsidy;

    @FXML
    private TextField tf_semi_monthly_rate;

    @FXML
    private TextField tf_status;

    @FXML
    private TextField tf_supervisor;

    @FXML
    public void initialize(int employeeId) {
        this.tf_employee_id.setText(String.valueOf(employeeId));
        setupMyProfile();
        setupMyAllowance();
        setupMySalaryDetails();
    }

    private void setupMySalaryDetails() {
        String query = "SELECT employee.basic_salary, employee.gross_semi_monthly_rate, employee.hourly_rate FROM payroll_sys.employee where employee.employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_basic_salary.setText(String.valueOf(resultSet.getBigDecimal("basic_salary")));
                tf_semi_monthly_rate.setText(String.valueOf(resultSet.getBigDecimal("gross_semi_monthly_rate")));
                tf_hourly_rate.setText(String.valueOf(resultSet.getBigDecimal("hourly_rate")));
                resultSet.getBigDecimal("hourly_rate");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


        private void setupMyAllowance() {
        String query = "SELECT rice, clothing, phone FROM allowance WHERE allowance.employee_id = ?";
        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                tf_rice_subsidy.setText(String.valueOf(resultSet.getInt("rice")));
                tf_clothing_allowance.setText(String.valueOf(resultSet.getInt("clothing")));
                tf_phone_allowance.setText(String.valueOf(resultSet.getInt("phone")));
                showAllowancePieChart();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAllowancePieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Rice Subsidy", Integer.valueOf(this.tf_rice_subsidy.getText())),
                new PieChart.Data("Phone Allowance", Integer.valueOf(this.tf_phone_allowance.getText())),
                new PieChart.Data("Clothing Allowance", Integer.valueOf(this.tf_clothing_allowance.getText())));

        this.chart_allowance.setTitle("My Allowance Breakdown");
        this.chart_allowance.setData(pieChartData);
    }





    private void setupMyProfile()  {
        String query = "SELECT e.first_name, e.last_name, e.birthday, e.address, e.employee_id, e.STATUS, p.name AS position_name, e.supervisor FROM employee e JOIN position p ON e.position_id = p.position_id WHERE e.employee_id = ?;";

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            int employeeId = Integer.parseInt(this.tf_employee_id.getText());
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_first_name.setText(resultSet.getString("first_name"));
                tf_last_name.setText(resultSet.getString("last_name"));
                tf_birthday.setText(resultSet.getDate("birthday").toString());
                tf_address.setText(resultSet.getString("address"));
                tf_status.setText(String.valueOf(EmployeeStatus.valueOf(resultSet.getString("STATUS"))));
                tf_position.setText(resultSet.getString("position_name"));
                tf_supervisor.setText(resultSet.getString("supervisor"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
