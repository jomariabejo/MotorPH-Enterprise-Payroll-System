package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.entity.Timesheet;
import com.jomariabejo.motorph.service.TimesheetService;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class HRTimesheetsController implements Initializable {

    private TimesheetService timesheetService = new TimesheetService();

    private final int ROWS_PER_PAGE = 100;

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private TextField tf_search;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<Timesheet> tv_timesheets;

    @FXML
    private TableColumn<Integer, Timesheet> tc_timesheetId;
    @FXML
    private TableColumn<Integer, Timesheet> tc_employeeId;
    @FXML
    private TableColumn<Date, Timesheet> tc_date;
    @FXML
    private TableColumn<Timestamp, Timesheet> tc_timeIn;
    @FXML
    private TableColumn<Timestamp, Timesheet> tc_timeOut;

    @FXML
    void paginationChanged(MouseEvent event) {
        loadTimesheetsForPage(pagination.getCurrentPageIndex());
    }

    private void loadTimesheetsForPage(int currentPageIndex) {
        try {
            ArrayList<Timesheet> timesheets = timesheetService.fetchTimesheetsForPage(currentPageIndex, ROWS_PER_PAGE);
            tv_timesheets.setItems(FXCollections.observableList(timesheets));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }

    private void loadTimesheetsForPage(int currentPageIndex, int employeeId) {
        try {
            ArrayList<Timesheet> timesheets = timesheetService.fetchTimesheetsForPage(employeeId, currentPageIndex, ROWS_PER_PAGE);
            tv_timesheets.setItems(FXCollections.observableList(timesheets));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }

    @FXML
    void searching(ActionEvent event) throws SQLException {
        try {
            boolean isEmployeeHasTimesheetRecords = timesheetService.checkIfEmployeeIdExist(Integer.valueOf(tf_search.getText()));

            if (isEmployeeHasTimesheetRecords) {
                setUpSearchedEmployeePagination();
                tv_timesheets.setItems(FXCollections.observableList(timesheetService.fetchTimesheetsForPage(
                        Integer.valueOf(tf_search.getText()),
                        pagination.getCurrentPageIndex(), ROWS_PER_PAGE)));
                lbl_tv_total_result.setText(String.valueOf(timesheetService.countEmployeeTimesheets(Integer.valueOf(tf_search.getText()))));
            }
            // Restore entire timesheets into tableview
            else {
                AlertUtility.showErrorAlert("Employee not found", "The employee timesheets that you're looking for doesn't exist...", null);
                setUpPagination();
                setUpTableView();
                setUpLblTotalResult();
            }
        } catch (NumberFormatException numberFormatException) {
            AlertUtility.showErrorAlert("Employee not found", "The employee timesheets that you're looking for doesn't exist...", null);
            setUpPagination();
            setUpTableView();
            setUpLblTotalResult();
        }
    }

    @FXML
    void viewEmployeePerformanceButtonClicked(ActionEvent event) {

    }

    public void setUpPagination() {
        pagination.setPageCount(timesheetService.getTimesheetPageCount());
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadTimesheetsForPage(newIndex.intValue());
        });
    }

    public void setUpSearchedEmployeePagination() {
        int employeeId = Integer.valueOf(tf_search.getText());

        pagination.setPageCount(timesheetService.getTimesheetPageCount(employeeId));
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadTimesheetsForPage(newIndex.intValue());
        });
    }

    public void setUpTableView() throws SQLException {
        tv_timesheets.setItems(FXCollections.observableList(timesheetService.fetchTimesheetsForPage(pagination.getCurrentPageIndex(), ROWS_PER_PAGE)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpPagination();
        try {
            setUpTableView();
            setUpLblTotalResult();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpLblTotalResult() throws SQLException {
        lbl_tv_total_result.setText(String.valueOf(timesheetService.getTimesheets().size()));
    }
}