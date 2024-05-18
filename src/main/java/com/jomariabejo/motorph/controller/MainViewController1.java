package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.controller.personalinformation.MyProfileController;
import com.jomariabejo.motorph.controller.personalinformation.MyTimesheetController;
import com.jomariabejo.motorph.service.LoginManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;

public class MainViewController1 {

    @FXML
    private AnchorPane NAV_ANCHORPANE;

    @FXML
    private Button btn_finance_generate_payslip;

    @FXML
    private Button btn_finance_payroll;

    @FXML
    private Button btn_human_resource_leave_request;

    @FXML
    private Button btn_human_resource_timesheets;

    @FXML
    private Button btn_personal_information_leave_request;

    @FXML
    private Button btn_personal_information_payslip;

    @FXML
    private Button btn_personal_information_profile;

    @FXML
    private Button btn_personal_information_timesheet;

    @FXML
    private Button btn_system_users;

    @FXML
    private ComboBox<?> comboBoxSettings;

    @FXML
    private ComboBox<?> combobox_human_resource;

    @FXML
    private Label lbl_finance;

    @FXML
    private Label lbl_human_resource;

    @FXML
    private Label lbl_personal_information;

    @FXML
    private Label lbl_system;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Label lbl_user_clicked_path;

    @FXML
    private Label lbl_employee_id;


    @FXML
    void dropDownClicked(ActionEvent event) {

    }

    @FXML
    void financeGeneratePayslipClicked(ActionEvent event) {

    }

    @FXML
    void financePayrollClicked(ActionEvent event) {

    }

    @FXML
    void humanResourceComboBoxEmployeeClicked(ActionEvent event) {

    }

    @FXML
    void humanResourceLeaveRequestClicked(ActionEvent event) {

    }

    @FXML
    void humanResourceTimesheetsClicked(ActionEvent event) {

    }

    @FXML
    void personalInformationLeaveRequestClicked(ActionEvent event) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Leave Request");
    }

    @FXML
    void personalInformationPayslipClicked(ActionEvent event) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Payslips");
    }

    @FXML
    void personalInformationProfileClicked(ActionEvent event) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Profile");
        try {
            displayHomeClicked();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void personalInformationTimesheetClicked(ActionEvent event) throws IOException {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Timesheets");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/my-timesheet.fxml"));
        AnchorPane loader = fxmlLoader.load();

        // Get the controller instance from the FXMLLoader
        MyTimesheetController myTimesheetController = fxmlLoader.getController();

        // Set the necessary data or reference to the controller
        myTimesheetController.initData(Integer.parseInt(this.lbl_employee_id.getText()));

        // Set the controller for the loaded FXML
        fxmlLoader.setController(myTimesheetController);

        // Set the loaded content as the center of the main pane
        mainPane.setCenter(loader);
    }


    @FXML
    void systemUsersClicked(ActionEvent event) {
    }

    @FXML
    private void initialize() {
        // hide all buttons
        hideButtons();
    }
    public void initSessionId(final LoginManager loginManager, int user_id, int employee_id, String role) throws SQLException, IOException {
        this.lbl_employee_id.setText(String.valueOf(employee_id));

        switch (role) {
            case "HR Administrator":
                showHRAccessButtons();
                break;
            case "Payroll Administrator":
                showPayrollButtons();
                break;
            case "System Administrator":
                showSystemAdminButtons();
                break;
            case "Executive":
                showExecutiveButtons();
                break;
        }

        // Display Home
        displayHomeClicked();
    }

    public void displayHomeClicked() throws IOException, SQLException {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Profile");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/my-profile.fxml"));
        if (fxmlLoader != null) {
            AnchorPane loader = fxmlLoader.load(); // Load the FXML file
            mainPane.setCenter(loader); // Set the loaded content as the center of the main pane

            MyProfileController myProfileController = fxmlLoader.getController();
            myProfileController.initialize(Integer.parseInt(this.lbl_employee_id.getText()));
        }
    }

    private void showExecutiveButtons() {
        // all access granted
        showSystemAdminButtons();
        showPayrollButtons();
        showHRAccessButtons();
    }

    private void showSystemAdminButtons() {
        lbl_system.setVisible(true);
        btn_system_users.setVisible(true);
        lbl_system.setManaged(true);
        btn_system_users.setManaged(true);
    }

    private void showPayrollButtons() {
        lbl_finance.setVisible(true);
        btn_finance_generate_payslip.setVisible(true);
        btn_finance_payroll.setVisible(true);
        lbl_finance.setManaged(true);
        btn_finance_generate_payslip.setManaged(true);
        btn_finance_payroll.setManaged(true);
    }

    private void showHRAccessButtons() {
        lbl_human_resource.setVisible(true);
        btn_human_resource_leave_request.setVisible(true);
        btn_human_resource_timesheets.setVisible(true);
        lbl_human_resource.setManaged(true);
        btn_human_resource_leave_request.setManaged(true);
        btn_human_resource_timesheets.setManaged(true);
    }


    private void hideButtons() {
        btn_finance_generate_payslip.setVisible(false);
        btn_finance_payroll.setVisible(false);
        btn_human_resource_leave_request.setVisible(false);
        btn_human_resource_timesheets.setVisible(false);
        btn_system_users.setVisible(false);
        combobox_human_resource.setVisible(false);
        lbl_finance.setVisible(false);
        lbl_human_resource.setVisible(false);
        lbl_system.setVisible(false);

        btn_finance_generate_payslip.setManaged(false);
        btn_finance_payroll.setManaged(false);
        btn_human_resource_leave_request.setManaged(false);
        btn_human_resource_timesheets.setManaged(false);
        btn_system_users.setManaged(false);
        combobox_human_resource.setManaged(false);
        lbl_finance.setManaged(false);
        lbl_human_resource.setManaged(false);
        lbl_system.setManaged(false);
    }

    public Label getLbl_employee_id() {
        return lbl_employee_id;
    }

    public void setLbl_employee_id(Label lbl_employee_id) {
        this.lbl_employee_id = lbl_employee_id;
    }
}
