package com.jomariabejo.motorph.controller.personalinformation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class MyLeaveRequestController {

    @FXML
    private Button buttonCheckLeaveCreditsEvent;

    @FXML
    private ComboBox<?> cb_leave_req_status;

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private Label lbl_tv_total_result1;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<?, ?> tc_endDate;

    @FXML
    private TableColumn<?, ?> tc_leaveRequestCategoryId;

    @FXML
    private TableColumn<?, ?> tc_leaveRequestId;

    @FXML
    private TableColumn<?, ?> tc_startDate;

    @FXML
    private TableColumn<?, ?> tc_status;

    @FXML
    private TableView<?> tv_leave_requests;

    @FXML
    void buttonFileLeaveRequestEvent(ActionEvent event) {
        System.out.println("File Leave Request Clciked...");
    }

    @FXML
    void comboBoxChanged(ActionEvent event) {

    }

    @FXML
    void paginationChanged(MouseEvent event) {

    }

    public void buttonCheckLeaveCreditsEvent(ActionEvent event) {
        System.out.println("Check Leave Credits Clciked...");
    }
}
