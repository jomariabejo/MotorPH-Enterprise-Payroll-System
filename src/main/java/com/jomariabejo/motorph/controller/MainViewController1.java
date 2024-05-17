package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.service.LoginManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
    }

    @FXML
    void personalInformationTimesheetClicked(ActionEvent event) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Timesheets");
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
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employee-profile.fxml"));
//        if (fxmlLoader != null) {
//            AnchorPane loader = fxmlLoader.load(); // Load the FXML file
//            mainPane.setCenter(loader); // Set the loaded content as the center of the main pane
//
//            // Lookup for the username label in the loaded content
//            Label lblUserName = (Label) loader.lookup("#username");
//
//            // Create an instance of the EmployeeService to fetch employee name
//            EmployeeService employeeService = new EmployeeService();
//
//            // Fetch employee name and handle optional presence
//            Optional<String> employeeNameOptional = employeeService.fetchEmployeeName(Integer.parseInt(this.employee_id.getText()));
//            String name = employeeNameOptional.orElse("No Name"); // If value is present, return it; otherwise return "No Name"
//            System.out.println("Employee Name: " + name); // Print the employee name
//            lblUserName.setText(String.valueOf(name)); // Set the employee name in the username label
//        } else {
//            System.err.println("FXML file not found!"); // Print an error message if the FXML file is not found
//        }
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
        lbl_personal_information.setVisible(false);
        lbl_system.setVisible(false);

        btn_finance_generate_payslip.setManaged(false);
        btn_finance_payroll.setManaged(false);
        btn_human_resource_leave_request.setManaged(false);
        btn_human_resource_timesheets.setManaged(false);
        btn_system_users.setManaged(false);
        combobox_human_resource.setManaged(false);
        lbl_finance.setManaged(false);
        lbl_human_resource.setManaged(false);
        lbl_personal_information.setManaged(false);
        lbl_system.setManaged(false);
    }

    public void btn_personal_information_profile_on_mouse_moved(MouseEvent mouseEvent) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Profile");
    }

    public void btn_personal_information_timesheet_on_mouse_moved(MouseEvent mouseEvent) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Timesheets");
    }

    public void btn_personal_information_leave_request_on_mouse_moved(MouseEvent mouseEvent) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Leave Request");
    }

    public void btn_personal_information_payslip_on_mouse_moved(MouseEvent mouseEvent) {
        this.lbl_user_clicked_path.setText("/ Personal Information / My Payslips");
    }
}
