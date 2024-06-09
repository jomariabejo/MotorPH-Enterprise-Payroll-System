package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
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
        // Handle count button event
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

    /**
     * Fetch leave request categories from the database and populate the ComboBox.
     */
    public void setUpComboBox() {
        ObservableList<String> observableList = FXCollections.observableArrayList(fetchLeaveCategories());
        this.leaverequest_type.setItems(observableList);

        // Add a listener to the ComboBox to handle selection changes
        this.leaverequest_type.setOnAction(event -> {
            String selectedCategory = this.leaverequest_type.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                int maxCredits = fetchMaxCredits(selectedCategory);
                this.tf_available_leave_credits.setText(String.valueOf(maxCredits));
            }
        });
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

    /**
     * Fetches the max credits for the given category from the database.
     *
     * @param categoryName the name of the leave request category.
     * @return the max credits for the category.
     */
    private int fetchMaxCredits(String categoryName) {
        int maxCredits = 0;
        String query = "SELECT maxCredits FROM payroll_sys.leave_request_category WHERE categoryName = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    maxCredits = resultSet.getInt("maxCredits");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return maxCredits;
    }

    /**
     * Calculate total days between start and end dates and display the result in tf_total_days_left.
     */
    private void calculateTotalDays() {
        LocalDate startDate = dp_leave_start_date.getValue();
        LocalDate endDate = dp_leave_end_date.getValue();

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        tf_total_days_left.setText(String.valueOf(daysBetween));
    }

    /**
     * Save the leave request to the database.
     */
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

        String insertQuery = "INSERT INTO payroll_sys.leave_request (employee_id, leave_request_category_id, start_date, end_date, reason) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, leaveReqCatId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
            preparedStatement.setString(5, reason);

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

    /**
     * Show an alert with the specified title and content.
     *
     * @param title the title of the alert.
     * @param content the content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
