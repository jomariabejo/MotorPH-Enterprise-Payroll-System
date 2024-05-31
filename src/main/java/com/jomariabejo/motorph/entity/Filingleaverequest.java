package com.jomariabejo.motorph.entity;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;

public class Filingleaverequest {

    @FXML
    private ComboBox<String> editDoctor_specialized;

    @FXML
    private TextField tf_fname;

    @FXML
    private DatePicker checkout_checkout;

    @FXML
    private DatePicker checkout_checkout1;

    @FXML
    private TextField tf_fname1;

    @FXML
    private TextField textFieldExplanation;

    @FXML
    private Button checkout_countBtn;

    @FXML
    private Button editApp_updateBtn;

    @FXML
    private Button editApp_cancelBtn;

    @FXML
    private Label labelTotalDays;

    @FXML
    private void countBtn(ActionEvent event) {
        if (checkout_checkout.getValue() != null && checkout_checkout1.getValue() != null) {
            long daysBetween = ChronoUnit.DAYS.between(checkout_checkout.getValue(), checkout_checkout1.getValue());
            tf_fname1.setText(Long.toString(daysBetween));
        }
    }

    @FXML
    private void handleProceed(ActionEvent event) {
        try (Connection conn = DatabaseConnectionUtility.getConnection()) {
            String sql = "INSERT INTO leave_request (user_id, request_category, leave_credits, leave_start, leave_end, total_days, leave_reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            int userId = getUserId();
            int requestCategoryId = getRequestCategoryId(editDoctor_specialized.getValue());
            int leaveCredits = Integer.parseInt(tf_fname.getText());
            Date leaveStart = Date.valueOf(checkout_checkout.getValue());
            Date leaveEnd = Date.valueOf(checkout_checkout1.getValue());
            int totalDays = Integer.parseInt(tf_fname1.getText());
            String leaveReason = textFieldExplanation.getText();

            pstmt.setInt(1, userId);
            pstmt.setInt(2, requestCategoryId);
            pstmt.setInt(3, leaveCredits);
            pstmt.setDate(4, leaveStart);
            pstmt.setDate(5, leaveEnd);
            pstmt.setInt(6, totalDays);
            pstmt.setString(7, leaveReason);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new leave request was inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Clear all fields
        editDoctor_specialized.setValue(null);
        tf_fname.clear();
        checkout_checkout.setValue(null);
        checkout_checkout1.setValue(null);
        tf_fname1.clear();
        textFieldExplanation.clear();
    }

    private int getUserId() {
        // Implement this to retrieve the actual user ID
        return 1; // Placeholder
    }

    private int getRequestCategoryId(String requestCategory) {
        // Implement this to map the request category name to its ID
        return 1; // Placeholder
    }
}

