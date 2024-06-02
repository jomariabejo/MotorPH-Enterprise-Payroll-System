package com.jomariabejo.motorph.controller.personalinformation;

import com.jomariabejo.motorph.controller.MainViewController;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

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

    /** TODO: Dapat makakapag submit ng leave request ang employee
     * VACATION = 10,
     * EMERGENCY = 5,
     * SICK = 5
     */
    @FXML
    void buttonFileLeaveRequestEvent(ActionEvent event) throws IOException {
        System.out.println("File Leave Request Clicked...");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/filing-leave-request.fxml"));

        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle("Filing New Leave Request");
        stage.setScene(new Scene(root));
        stage.show();
        MyLeaveRequestSubmissionController myLeaveRequestSubmissionController = fxmlLoader.getController();
        System.out.println("FROM LEAVE REQUEST CONTROLLER: EMPLOYEE ID IS = " + this.employeeId);
        myLeaveRequestSubmissionController.initData(this.employeeId);
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

    private int employeeId;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public void setInitData(int employeeId) { // employee id from main view.
        this.employeeId = employeeId;
    }
}
