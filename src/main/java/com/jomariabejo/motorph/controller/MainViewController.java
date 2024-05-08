package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.controller.myprofile.EmployeeProfile;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.service.LoginManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Controls the main application screen
 */
public class MainViewController {

    private final String BORDERPANE_CENTER_BASE_PATH = "/com/jomariabejo/motorph/center";

    private String loginUserID;

    @FXML
    private Label employee_id;

    @FXML
    private Label user_id;

    @FXML
    private Button logoutButton;

    @FXML
    private Label sessionLabel;

    @FXML
    private ComboBox < ? > comboBoxSettings;

    @FXML
    private Button employeeHomeBtn;

    @FXML
    private Button employeeLeaveRequestBtn;

    @FXML
    private Button employeeSalaryDetailsBtn;

    @FXML
    private Button hrEmployeesBtn;

    @FXML
    private Button hrLeaveRequestsBtn;

    @FXML
    private Button hrTimesheetsBtn;

    @FXML
    private Button itDataBackupBtn;

    @FXML
    private Button itUsers;

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button payrollDashboardBtn;

    @FXML
    private Button payrollGeneratePayslip;

    @FXML
    private Button payrollReportBtn;

    @FXML
    private ImageView userProfileImage;

    @FXML
    private Label lblHR;

    @FXML
    private Label lblIT;

    @FXML
    private Label lblPayroll;

    @FXML
    private Line lineHR;

    @FXML
    private Line lineIT;

    @FXML
    private Line linePayroll;

    @FXML
    private void initialize() throws IOException, SQLException {
        hideButtons();
    }

    public void initSessionID(final LoginManager loginManager, int user_id, int employee_id, String role) throws SQLException, IOException {
        this.user_id.setText(String.valueOf(user_id));
        this.employee_id.setText(String.valueOf(employee_id));

        switch (role) {
            case "HR Administrator":
                showHRAccessButtons();
                break;
            case "Payroll Administrator":
                showPayrollButtons();
                break;
            case "System Administrator":
                showInformationTechnologyButtons();
                break;
            case "Executive":
                showExecutiveButtons();
                break;
        }

        // Display Home
        displayHomeClicked();
    }

    /**
     * Loads and displays the home screen content.
     *
     * @throws IOException  if an I/O error occurs while loading the FXML file
     * @throws SQLException if a SQL error occurs while fetching employee data
     */
    public void displayHomeClicked() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/employee-main-center.fxml"));
        if (fxmlLoader != null) {
            AnchorPane loader = fxmlLoader.load(); // Load the FXML file
            mainPane.setCenter(loader); // Set the loaded content as the center of the main pane

            // Lookup for the username label in the loaded content
            Label lblUserName = (Label) loader.lookup("#username");

            // Create an instance of the EmployeeService to fetch employee name
            EmployeeService employeeService = new EmployeeService();

            // Fetch employee name and handle optional presence
            Optional < String > employeeNameOptional = employeeService.fetchEmployeeName(Integer.parseInt(this.employee_id.getText()));
            String name = employeeNameOptional.orElse("No Name"); // If value is present, return it; otherwise return "No Name"
            System.out.println("Employee Name: " + name); // Print the employee name
            lblUserName.setText(String.valueOf(name)); // Set the employee name in the username label
        } else {
            System.err.println("FXML file not found!"); // Print an error message if the FXML file is not found
        }
    }

    public void showProfile(ActionEvent event) {}

    public void showTimesheet(ActionEvent event) {}

    public void showLeaveRequest(ActionEvent event) {}

    public void showPayslip(ActionEvent event) {}

    public void settingsClicked(ActionEvent event) {
        System.out.println(event.getSource());
    }

    public void dropDownClicked(ActionEvent event) {
        switch (comboBoxSettings.getSelectionModel().getSelectedItem().toString()) {
            case "Logout":
                LoginManager loginManager = new LoginManager(comboBoxSettings.getScene().getWindow().getScene());
                loginManager.logout();
                break;
        }
    }


    public void editClicked(ActionEvent event) {}

    public void moreinfoClicked(ActionEvent event) {}

    /**
     * Setup empployee profile
     *
     * @param event
     * @throws IOException
     */
    public void profileClicked(ActionEvent event) throws IOException, SQLException {
        displayHomeClicked();
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void timesheetClicked(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/employee-timesheet-view.fxml"));
        mainPane.setCenter(anchorPane);
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void leaveRequestClicked(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/employee-timesheet-view.fxml"));
        mainPane.setCenter(anchorPane);
    }

    /**
     *
     * @param event
     */
    public void payslipClicked(ActionEvent event) {}

    /**
     *
     * @param event
     * @throws IOException
     */
    public void salaryDetailsClicked(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/employee-salary-details.fxml"));
        mainPane.setCenter(anchorPane);
    }

    /**
     *
     * @param mouseEvent
     * @throws IOException
     */
    public void viewMyProfileClicked(MouseEvent mouseEvent) throws IOException {
        try {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH+"/employee-profile.fxml"));
        AnchorPane loader = (AnchorPane) fxmlLoader.load();
        mainPane.setCenter(loader);
        EmployeeProfile employeeProfile = fxmlLoader.getController();
        employeeProfile.initData(Integer.parseInt(this.employee_id.getText()));
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void myLeaveRequestClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/employee-leave-request-view.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void hrEmployeesClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/hr-home.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void hrTimesheetsClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/hr-timesheets-view.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void hrLeaveRequestClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/hr-leave-request-view.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void payrollDashboardClicked(ActionEvent event) throws IOException {

    }

    /**
     *
     * @param event
     */
    public void generatePayslipClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/finance-generate-payslip.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void payrollReportClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(BORDERPANE_CENTER_BASE_PATH + "/finance-payroll-dashboard.fxml"));
        AnchorPane leaveRequestView = fxmlLoader.load();
        mainPane.setCenter(leaveRequestView);
    }

    /**
     *
     * @param event
     */
    public void usersClicked(ActionEvent event) {}

    /**
     *
     * @param event
     */
    public void dataBackupClicked(ActionEvent event) {}

    /**
     * Hides all buttons and related UI elements.
     * This method sets the visibility and management properties for all buttons and related UI elements to false.
     */
    public void hideButtons() {
        // Hide HR buttons and related UI elements
        hrEmployeesBtn.setVisible(false);
        hrEmployeesBtn.setVisible(false);
        hrLeaveRequestsBtn.setVisible(false);
        hrTimesheetsBtn.setVisible(false);
        lblHR.setVisible(false);
        lineHR.setVisible(false);
        hrEmployeesBtn.setManaged(false);
        hrEmployeesBtn.setManaged(false);
        hrLeaveRequestsBtn.setManaged(false);
        hrTimesheetsBtn.setManaged(false);
        lblHR.setManaged(false);
        lineHR.setManaged(false);

        // Hide IT buttons and related UI elements
        itDataBackupBtn.setVisible(false);
        itUsers.setVisible(false);
        lblIT.setVisible(false);
        lineIT.setVisible(false);
        itDataBackupBtn.setManaged(false);
        itUsers.setManaged(false);
        lblIT.setManaged(false);
        lineIT.setManaged(false);

        // Hide Payroll buttons and related UI elements
        payrollDashboardBtn.setVisible(false);
        payrollGeneratePayslip.setVisible(false);
        payrollReportBtn.setVisible(false);
        lblPayroll.setVisible(false);
        linePayroll.setVisible(false);
        payrollDashboardBtn.setManaged(false);
        payrollGeneratePayslip.setManaged(false);
        payrollReportBtn.setManaged(false);
        lblPayroll.setManaged(false);
        linePayroll.setManaged(false);
    }

    /**
     * Displays Human Resources (HR) access buttons and related UI elements.
     * This method sets the visibility and management properties for HR-related buttons.
     */
    public void showHRAccessButtons() {
        lblHR.setVisible(true);
        lblHR.setManaged(true);
        // Set HR line visible
        lineHR.setManaged(true);
        lineHR.setVisible(true);
        // Set HR buttons visible
        hrEmployeesBtn.setVisible(true);
        hrEmployeesBtn.setVisible(true);
        hrLeaveRequestsBtn.setVisible(true);
        hrTimesheetsBtn.setVisible(true);

        // Ensure HR buttons are managed
        hrEmployeesBtn.setManaged(true);
        hrEmployeesBtn.setManaged(true);
        hrLeaveRequestsBtn.setManaged(true);
        hrTimesheetsBtn.setManaged(true);
    }

    /**
     * Displays Payroll buttons and related UI elements.
     * This method sets the visibility and management properties for Payroll-related buttons.
     */
    public void showPayrollButtons() {
        lblPayroll.setVisible(true);
        lblPayroll.setManaged(true);
        // Set Payroll line visible
        linePayroll.setVisible(true);
        linePayroll.setManaged(true);
        // Set Payroll buttons visible
        payrollDashboardBtn.setVisible(true);
        payrollGeneratePayslip.setVisible(true);
        payrollReportBtn.setVisible(true);

        // Ensure Payroll buttons are managed
        payrollDashboardBtn.setManaged(true);
        payrollGeneratePayslip.setManaged(true);
        payrollReportBtn.setManaged(true);
    }

    /**
     * Displays Information Technology buttons and related UI elements.
     * This method sets the visibility and management properties for IT-related buttons, labels, and lines.
     */
    public void showInformationTechnologyButtons() {
        // Set line visible
        lineIT.setVisible(true);
        lineIT.setManaged(true);
        // Set IT buttons and labels visible
        itDataBackupBtn.setVisible(true);
        itUsers.setVisible(true);
        lblIT.setVisible(true);

        // Ensure IT buttons and labels are managed
        itDataBackupBtn.setManaged(true);
        itUsers.setManaged(true);
        lblIT.setManaged(true);
    }

    /**
     * Displays User Executive buttons and related UI elements.
     * This method sets the visibility and management properties for various UI components
     * related to HR, IT, and Payroll functionalities.
     */
    public void showExecutiveButtons() {
        // HR Buttons and Labels
        hrEmployeesBtn.setVisible(true);
        hrEmployeesBtn.setVisible(true);
        hrLeaveRequestsBtn.setVisible(true);
        hrTimesheetsBtn.setVisible(true);
        lblHR.setVisible(true);
        lineHR.setVisible(true);
        hrEmployeesBtn.setManaged(true);
        hrEmployeesBtn.setManaged(true);
        hrLeaveRequestsBtn.setManaged(true);
        hrTimesheetsBtn.setManaged(true);
        lblHR.setManaged(true);
        lineHR.setManaged(true);

        // IT Buttons and Labels
        itDataBackupBtn.setVisible(true);
        itUsers.setVisible(true);
        lblIT.setVisible(true);
        lineIT.setVisible(true);
        itDataBackupBtn.setManaged(true);
        itUsers.setManaged(true);
        lblIT.setManaged(true);
        lineIT.setManaged(true);

        // Payroll Buttons and Labels
        payrollDashboardBtn.setVisible(true);
        payrollGeneratePayslip.setVisible(true);
        payrollReportBtn.setVisible(true);
        lblPayroll.setVisible(true);
        linePayroll.setVisible(true);
        payrollDashboardBtn.setManaged(true);
        payrollGeneratePayslip.setManaged(true);
        payrollReportBtn.setManaged(true);
        lblPayroll.setManaged(true);
        linePayroll.setManaged(true);
    }
}