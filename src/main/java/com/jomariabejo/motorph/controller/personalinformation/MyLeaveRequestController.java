package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.LeaveRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;

public class MyLeaveRequestController {
    @FXML
    private Button buttonCheckLeaveCreditsEvent;

    @FXML
    private ComboBox<String> cb_leave_req_status;

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private Label lbl_tv_total_result1;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<Date, LeaveRequest> tc_endDate;

    @FXML
    private TableColumn<Integer, LeaveRequest> tc_leaveRequestCategoryId;

    @FXML
    private TableColumn<Integer, LeaveRequest> tc_leaveRequestId;

    @FXML
    private TableColumn<Date, LeaveRequest> tc_startDate;

    @FXML
    private TableColumn<LeaveRequest, LeaveRequest.LeaveRequestStatus> tc_status;

    @FXML
    private TableView<LeaveRequest> tv_leave_requests;

    private int employeeId;

    @FXML
    void buttonFileLeaveRequestEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/filing-leave-request.fxml"));

        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Filing New Leave Request");
        stage.setScene(new Scene(root));
        stage.show();
        MyLeaveRequestSubmissionController myLeaveRequestSubmissionController = fxmlLoader.getController();
        myLeaveRequestSubmissionController.initData(this.employeeId);
    }

    public void buttonCheckLeaveCreditsEvent(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/my-remaining-leave-request-credits.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Remaing Leave Credits");
        stage.setScene(new Scene(root));
        stage.show();
        MyRemainingLeaveRequestCredits myRemainingLeaveRequestCredits = fxmlLoader.getController();
        myRemainingLeaveRequestCredits.initData(employeeId);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setInitData(int employeeId) { // employee id from main view.
        this.employeeId = employeeId;
        displayLeaveRequests();
    }

    private void displayLeaveRequests() {
        tv_leave_requests.getItems().clear();
        String query = "SELECT l.leave_request_id, l.leave_request_category_id, l.start_date,l.end_date,l.`status`\n" +
                "FROM leave_request l\n" +
                "WHERE l.employee_id = ? \n" +
                "ORDER BY l.leave_request_id DESC;\n";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setLeaveRequestID(rs.getInt(1));
                leaveRequest.setLeaveRequestCategoryId(rs.getInt(2));
                leaveRequest.setStartDate(rs.getDate(3));
                leaveRequest.setEndDate(rs.getDate(4));
                leaveRequest.setStatus(LeaveRequest.LeaveRequestStatus.valueOf(rs.getString(5)));
                tv_leave_requests.getItems().add(leaveRequest);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            lbl_tv_total_result.setText(String.valueOf(tv_leave_requests.getItems().size()));
        }
    }
}
