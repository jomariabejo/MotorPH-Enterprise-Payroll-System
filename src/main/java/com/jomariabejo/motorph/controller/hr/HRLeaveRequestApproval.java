package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.entity.LeaveRequest;
import com.jomariabejo.motorph.repository.LeaveRequestCategoryRepository;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.LeaveRequestCategoryService;
import com.jomariabejo.motorph.service.LeaveRequestService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.SQLDateUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class HRLeaveRequestApproval {
    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private ComboBox<String> cb_status;

    @FXML
    private Label employeeName;

    @FXML
    private Label endDate;

    @FXML
    private Label leaveRequestId;

    @FXML
    private Label leaveType;

    @FXML
    private Label numberOfLeaveDays;

    @FXML
    private Label startDate;

    @FXML
    void viewLeaveMessageClicked(ActionEvent event) {
        AlertUtility.showInformation("Leave Request Of " + employeeName,leaveRequestService.getLeaveRequestMessage(Integer.parseInt(leaveRequestId.getText())), null);
    }

    @FXML
    private void initialize() {

    }

    private LeaveRequestService leaveRequestService = new LeaveRequestService(new LeaveRequestRepository());
    private LeaveRequestCategoryService leaveRequestCategoryService = new LeaveRequestCategoryService(new LeaveRequestCategoryRepository());
    private EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initDate(int leaveRequestId) throws SQLException {
        displayLeaveRequestDataIntoFields(leaveRequestId);
    }

    private void displayLeaveRequestDataIntoFields(int leaveRequestId) throws SQLException {
        LeaveRequest leaveRequest = leaveRequestService.getLeaveRequestById(leaveRequestId);
        String employeeName = employeeService.fetchEmployeeName(leaveRequest.getEmployeeID()).get();

        this.leaveRequestId.setText(String.valueOf(leaveRequest.getLeaveRequestID()));
        this.employeeName.setText(employeeName);
        this.endDate.setText(leaveRequest.getEndDate().toString());
        this.startDate.setText(leaveRequest.getStartDate().toString());
        this.leaveType.setText(leaveRequestCategoryService.fetchLeaveRequestCategoryName(leaveRequest.getLeaveRequestCategoryId()));
        this.numberOfLeaveDays.setText(String.valueOf(SQLDateUtility.getDifferenceInDays(leaveRequest.getEndDate(), leaveRequest.getStartDate())));
        this.cb_status.setValue(leaveRequest.getStatus().toString());
    }

    public void cancelButtonClicked(ActionEvent event) {
        leaveRequestId.getScene().getWindow().hide();
    }

    public void saveButtonClicked(ActionEvent event) {
        boolean isConfirmed = AlertUtility.showConfirmation("Leave Request Modification Confirmation", "Are you sure you want to set " + cb_status.getSelectionModel().getSelectedItem().toString() + " the leave request of " + employeeName.getText(), "Date Start Leave: " + this.startDate.getText() + "\n " + "End Date Leave " + this.endDate.getText());

        if (isConfirmed) {
            leaveRequestService.updateLeaveRequestStatus(Integer.valueOf(this.leaveRequestId.getText()), LeaveRequest.LeaveRequestStatus.valueOf(cb_status.getSelectionModel().getSelectedItem()));
        }
        leaveRequestId.getScene().getWindow().hide();
    }

    public void setButtonHidden() {
        btn_save.setVisible(false);
        btn_cancel.setVisible(false);
    }
}

