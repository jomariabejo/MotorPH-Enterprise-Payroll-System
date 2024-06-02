package com.jomariabejo.motorph.controller.personalinformation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ComboBox<String> leaverequest_type;

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

    /** TODO: 1. Gumawa ka muna ng fetcher ng mga leave request category names such as (SICK, EMERGENCY, VACATION)
     *        2. After mo ma fetch yung mga leave types, then i inject mo yung data papunta sa combo box, supposedly tatlo yun.
     */
    public void setUpComboBox() {
        ObservableList observableList = FXCollections.observableList(null); // replace this
        this.leaverequest_type.setItems(observableList);
    }
}
