package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MyRemainingLeaveRequestCredits {
    private int employeeId;

    @FXML
    private ComboBox<String> leaveTypes;

    @FXML
    private Label lblRemainingLeaveCreditsValue;

    private void setupComboBox() {
        String query = "SELECT categoryName FROM payroll_sys.leave_request_category";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                leaveTypes.getItems().add((resultSet.getString("categoryName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initData(int employeeId) {
        this.employeeId = employeeId;
        setupComboBox();
        fetchAndDisplayRemainingLeaveCredits();
    }

    public void leaveTypeChanged(ActionEvent actionEvent) {
        fetchAndDisplayRemainingLeaveCredits();
    }

    private void fetchAndDisplayRemainingLeaveCredits() {
        String query = "SELECT COALESCE(lrc.maxCredits, 0) - IFNULL(SUM(DATEDIFF(\n" +
                "        CASE WHEN lr.start_date = lr.end_date THEN lr.start_date ELSE DATE_ADD(lr.end_date, INTERVAL 1 DAY) END,\n" +
                "        lr.start_date)), 0) AS remaining_leave_balance, lrc.categoryName\n" +
                "FROM leave_request_category lrc\n" +
                "         LEFT JOIN leave_request lr ON lr.leave_request_category_id = lrc.leave_req_cat_id\n" +
                "    AND lr.employee_id = ?\n" +
                "    AND lr.status = 'Approved'\n" +
                "    AND YEAR(lr.start_date) = YEAR(CURDATE())\n" +
                "WHERE lrc.leave_req_cat_id = ?;";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, Integer.valueOf(employeeId));
            pstmt.setInt(2, Integer.valueOf(fetchLeaveRequestCategoryId(leaveTypes.getSelectionModel().getSelectedItem())));
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    lblRemainingLeaveCreditsValue.setText(String.valueOf(resultSet.getInt("remaining_leave_balance")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int fetchLeaveRequestCategoryId(String categoryName) {
        int categoryId = -1;
        String query = "SELECT leave_req_cat_id FROM payroll_sys.leave_request_category WHERE categoryName = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryId = resultSet.getInt("leave_req_cat_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId;
    }
}
