package com.jomariabejo.motorph.controller.personalinformation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MyLeaveRequestSubmissionController {
    @FXML
    private Button btn_count;

    @FXML
    private DatePicker dp_leave_end_date;

    @FXML
    private DatePicker dp_leave_start_date;

    @FXML
    private Label leave_request_owner;

    @FXML
    private ComboBox<?> leaverequest_type;

    @FXML
    private TextField tf_available_leave_credits;

    @FXML
    private TextField tf_reason;

    @FXML
    private TextField tf_total_days_left;

    @FXML
    void cancelBtnEvent(ActionEvent event) {

    }

    @FXML
    void countBtn(ActionEvent event) {

    }

    @FXML
    void submitBtnEvent(ActionEvent event) {

    }

    public void initData(int employeeId) {
        this.leave_request_owner.setText(String.valueOf(employeeId));
    }

}
