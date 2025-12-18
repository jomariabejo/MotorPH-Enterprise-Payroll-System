package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OvertimeApprovalFormController {

    private OvertimeRequestsController overtimeRequestsController;
    private OvertimeRequest overtimeRequest;
    private String action;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblOvertimeDate;

    @FXML
    private Label lblHoursRequested;

    @FXML
    private TextArea taRemarks;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        overtimeRequestsController.processApproval(overtimeRequest, action, taRemarks.getText());
        btnSubmit.getScene().getWindow().hide();
    }

    public void setup() {
        if (overtimeRequest != null) {
            lblEmployeeName.setText(overtimeRequest.getEmployeeID().getFirstName() + " " + 
                    overtimeRequest.getEmployeeID().getLastName());
            lblOvertimeDate.setText(overtimeRequest.getOvertimeDate().toString());
            lblHoursRequested.setText(overtimeRequest.getHoursRequested().toString());
            
            if (overtimeRequest.getHRRemarks() != null) {
                taRemarks.setText(overtimeRequest.getHRRemarks());
            }
        }
        
        btnSubmit.setText(action);
    }
}













