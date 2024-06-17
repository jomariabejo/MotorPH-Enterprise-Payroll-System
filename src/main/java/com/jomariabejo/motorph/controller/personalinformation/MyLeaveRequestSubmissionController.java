package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyLeaveRequestSubmissionController implements Initializable {
    @FXML
    private Button btn_count;

    @FXML
    private DatePicker dp_leave_end_date;

    @FXML
    private DatePicker dp_leave_start_date;

    @FXML
    private Label leave_request_owner;

    @FXML
    private ComboBox<String> leaverequest_type;

    @FXML
    private TextField tf_available_leave_credits;

    @FXML
    private TextField tf_reason;

    @FXML
    private TextField tf_total_days_left;

    @FXML
    void cancelBtnEvent(ActionEvent event) {
        // Handle cancel button event
    }

    @FXML
    void countBtn(ActionEvent event) {
        calculateTotalDays();
    }

    @FXML
    void submitBtnEvent(ActionEvent event) {
        saveLeaveRequest();
    }

    public void initData(int employeeId) {
        this.leave_request_owner.setText(String.valueOf(employeeId));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpComboBox();
        setUpDatePicker();
    }

    private void setUpComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList(fetchLeaveCategories());
        this.leaverequest_type.setItems(observableList);

        this.leaverequest_type.setOnAction(event -> {
            String selectedCategory = this.leaverequest_type.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                int remainingCredits = fetchRemainingCredits(selectedCategory);
                System.out.println("Selected Category: " + selectedCategory);
                System.out.println("Remaining Credits: " + remainingCredits);
                this.tf_available_leave_credits.setText(String.valueOf(remainingCredits));
            }
        });
    }

    private void setUpDatePicker() {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };

        dp_leave_start_date.setDayCellFactory(dayCellFactory);
        dp_leave_start_date.setValue(LocalDate.now());

        dp_leave_end_date.setDayCellFactory(dayCellFactory);
        dp_leave_end_date.setValue(LocalDate.now());
    }

    private List<String> fetchLeaveCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT categoryName FROM payroll_sys.leave_request_category";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                categories.add(resultSet.getString("categoryName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    private int fetchRemainingCredits(String categoryName) {
        int remainingCredits = 0;
        int employeeId = Integer.parseInt(leave_request_owner.getText());

        String query = "SELECT COALESCE(lrc.maxCredits, 0) - IFNULL(SUM(DATEDIFF(" +
                "CASE WHEN lr.start_date = lr.end_date THEN lr.start_date ELSE DATE_ADD(lr.end_date, INTERVAL 1 DAY) END, " +
                "lr.start_date)), 0) AS remaining_leave_balance " +
                "FROM leave_request_category lrc " +
                "LEFT JOIN leave_request lr ON lr.leave_request_category_id = lrc.leave_req_cat_id " +
                "AND lr.employee_id = ? " +
                "AND lr.status = 'Approved' " +
                "AND YEAR(lr.start_date) = YEAR(CURDATE()) " +
                "WHERE lrc.categoryName = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    remainingCredits = resultSet.getInt("remaining_leave_balance");
                    System.out.println("Fetched remaining credits: " + remainingCredits);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return remainingCredits;
    }

    private boolean hasOverlappingApprovedLeave(int employeeId, LocalDate startDate, LocalDate endDate) {
        String query = "SELECT COUNT(*) FROM leave_request WHERE employee_id = ? AND status = 'Approved' " +
                "AND (start_date <= ? AND end_date >= ?)";
        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void calculateTotalDays() {
        LocalDate startDate = dp_leave_start_date.getValue();
        LocalDate endDate = dp_leave_end_date.getValue();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        tf_total_days_left.setText(String.valueOf(daysBetween));
    }

    private void saveLeaveRequest() {
        String selectedCategory = leaverequest_type.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert("Error", "Please select a leave request type.");
            return;
        }

        int leaveReqCatId = fetchLeaveRequestCategoryId(selectedCategory);
        if (leaveReqCatId == -1) {
            showAlert("Error", "Invalid leave request type.");
            return;
        }

        LocalDate startDate = dp_leave_start_date.getValue();
        LocalDate endDate = dp_leave_end_date.getValue();
        String reason = tf_reason.getText();
        int employeeId = Integer.parseInt(leave_request_owner.getText());

        int remainingCredits = fetchRemainingCredits(selectedCategory);
        long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (remainingCredits < requestedDays) {
            showAlert("Error", "Insufficient leave credits.");
            return;
        }

        if (hasOverlappingApprovedLeave(employeeId, startDate, endDate)) {
            showAlert("Error", "You already have an approved leave during the requested period.");
            return;
        }

        String insertQuery = "INSERT INTO payroll_sys.leave_request (employee_id, leave_request_category_id, start_date, end_date, reason, date_created) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, leaveReqCatId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
            preparedStatement.setString(5, reason);
            preparedStatement.setDate(6, DateUtility.getDate());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Leave request submitted successfully.");
            } else {
                showAlert("Error", "Failed to submit leave request.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while submitting the leave request.");
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
