package com.jomariabejo.motorph.controller.employee;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class MyLeaveRequestSubmissionController {
    @FXML
    private DatePicker dp_leave_end_date;

    @FXML
    private DatePicker dp_leave_start_date;

    @FXML
    private Label employeeId;

    @FXML
    private ComboBox<String> leaverequest_type;

    @FXML
    private TextField tf_reason;

    @FXML
    void cancelBtnEvent(ActionEvent event) {
        // Handle cancel button event
        tf_reason.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnEvent(ActionEvent event) {
        saveLeaveRequest();
    }

    public void initData(int employeeId) {
        this.employeeId.setText(String.valueOf(employeeId));
    }

    @FXML
    private void initialize() {
        setUpComboBox();
        setUpDatePicker();
    }

    /**
     * Fetch leave request categories from the database and populate the ComboBox.
     */
    public void setUpComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList(fetchLeaveCategories());
        this.leaverequest_type.setItems(observableList);
    }

    /**
     * Sets up the DatePicker to disable past dates.
     */
    public void setUpDatePicker() {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Disable all dates before today
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }
                    }
                };
            }
        };

        dp_leave_start_date.setDayCellFactory(dayCellFactory);
        dp_leave_start_date.setValue(LocalDate.now()); // Default to today's date

        dp_leave_end_date.setDayCellFactory(dayCellFactory);
        dp_leave_end_date.setValue(LocalDate.now()); // Default to today's date
    }

    /**
     * Fetches leave request categories from the database.
     *
     * @return a list of leave request categories.
     */
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

    public void validateSelectedCategory(String selectedCategory) {
        if (selectedCategory == null) {
            AlertUtility.showErrorAlert("Error", "Please select a leave request type.", null);
            return;
        }

        int leaveReqCatId = fetchLeaveRequestCategoryId(selectedCategory);
        if (leaveReqCatId == -1) {
            AlertUtility.showErrorAlert("Error", "Invalid leave request type.", null);
            return;
        }
    }


    private void saveLeaveRequest() {
        String selectedCategory = leaverequest_type.getSelectionModel().getSelectedItem();

        validateSelectedCategory(selectedCategory);

        LocalDate leaveStartDateValue = dp_leave_start_date.getValue();
        LocalDate leaveEndDateValue = dp_leave_end_date.getValue();
        String reason = tf_reason.getText();
        int employeeId = Integer.parseInt(this.employeeId.getText());

        if (leaveStartDateValue.isBefore(leaveEndDateValue) || leaveStartDateValue.isEqual(leaveEndDateValue)) {
            // Check for overlapping leave requests
            if (hasOverlappingLeaveRequest(employeeId, leaveStartDateValue, leaveEndDateValue)) {
                AlertUtility.showErrorAlert("Error", "Leave Request Submission Error", "You already have a leave request overlapping with the selected dates.");
                return;
            }

            else if (hasRemainingLeaveCredits(leaveStartDateValue, leaveEndDateValue)) {
                String insertQuery = "INSERT INTO payroll_sys.leave_request (employee_id, leave_request_category_id, start_date, end_date, reason, date_created) VALUES (?, ?, ?, ?, ?, ?)";

                try (Connection connection = DatabaseConnectionUtility.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                    preparedStatement.setInt(1, employeeId);
                    preparedStatement.setInt(2, fetchLeaveRequestCategoryId(selectedCategory));
                    preparedStatement.setDate(3, java.sql.Date.valueOf(leaveStartDateValue));
                    preparedStatement.setDate(4, java.sql.Date.valueOf(leaveEndDateValue));
                    preparedStatement.setString(5, reason);
                    preparedStatement.setDate(6, DateUtility.getDate());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        AlertUtility.showInformation("Success", "Leave Request Submitted", "Leave request submitted successfully.");
                    }
                } catch (SQLException e) {
                }
            } else {
                AlertUtility.showErrorAlert("Error", "Leave Request Submission Error", "You have fully consumed your leaves for this year. Cannot submit leave request.");
            }
        }
        else {
            AlertUtility.showErrorAlert("Error", "Invalid leave end date.", "Leave end date should be after start date.");
        }
    }

    /**
     * Checks if there is any overlapping leave request for the given employee and dates.
     *
     * @param employeeId the ID of the employee.
     * @param startDate  the start date of the leave request.
     * @param endDate    the end date of the leave request.
     * @return true if there is an overlap, false otherwise.
     */
    private boolean hasOverlappingLeaveRequest(int employeeId, LocalDate startDate, LocalDate endDate) {
        String query = "SELECT COUNT(*) AS overlapCount " +
                "FROM payroll_sys.leave_request " +
                "WHERE employee_id = ? " +
                "AND status IN ('Approved', 'Pending') " +  // Consider only approved and pending requests
                "AND start_date <= ? AND end_date >= ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setDate(2, java.sql.Date.valueOf(endDate));
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int overlapCount = resultSet.getInt("overlapCount");
                    return overlapCount > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private boolean hasRemainingLeaveCredits(LocalDate leaveStartDateValue, LocalDate leaveEndDateValue) {
        long remainingLeaveCredits = fetchRemainingLeaveCredits();
        long appliedLeaveInDays = ChronoUnit.DAYS.between(leaveStartDateValue, leaveEndDateValue);
        System.out.println("Applied leave in day is = " + appliedLeaveInDays);
        return remainingLeaveCredits > appliedLeaveInDays;
    }

    private int fetchRemainingLeaveCredits() {
        String query = "SELECT \n" +
                "    COALESCE(\n" +
                "        lrc.maxCredits - COALESCE(SUM(\n" +
                "            CASE \n" +
                "                WHEN lr.start_date = lr.end_date THEN 1  -- Count as 1 day if start_date equals end_date\n" +
                "                ELSE DATEDIFF(lr.end_date, lr.start_date) + 1  -- Calculate days between start_date and end_date\n" +
                "            END\n" +
                "        ), 0),\n" +
                "        lrc.maxCredits\n" +
                "    ) AS remaining_leave_balance\n" +
                "FROM \n" +
                "    leave_request_category lrc\n" +
                "LEFT JOIN \n" +
                "    leave_request lr ON lr.leave_request_category_id = lrc.leave_req_cat_id\n" +
                "        AND lr.employee_id = ?\n" +
                "        AND YEAR(lr.start_date) = YEAR(CURDATE())\n" +
                "        AND YEAR(lr.end_date) = YEAR(CURDATE())\n" +
                "        AND lr.leave_request_category_id = ?\n" +
                "        AND lr.status = 'Approved'\n" +
                "WHERE \n" +
                "    lrc.leave_req_cat_id = ?\n" +
                "GROUP BY\n" +
                "    lrc.maxCredits;\n";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, Integer.valueOf(employeeId.getText()));
            pstmt.setInt(2, Integer.valueOf(fetchLeaveRequestCategoryId(leaverequest_type.getSelectionModel().getSelectedItem())));
            pstmt.setInt(3, Integer.valueOf(fetchLeaveRequestCategoryId(leaverequest_type.getSelectionModel().getSelectedItem())));
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("remaining_leave_balance");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1; // means you can't submit leave request
    }

    /**
     * Fetches the leave request category ID for the given category name from the database.
     *
     * @param categoryName the name of the leave request category.
     * @return the ID of the leave request category, or -1 if not found.
     */
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
