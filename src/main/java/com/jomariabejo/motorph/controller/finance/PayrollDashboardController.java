package com.jomariabejo.motorph.controller.finance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

public class PayrollDashboardController {

    @FXML
    private TableView<?> TABLEVIEW_MONTHLY_PAYROLL_SUMMARY_REPORT;

    @FXML
    private ComboBox<?> comboBoxMonth;

    @FXML
    private ComboBox<?> comboBoxYear;

    @FXML
    private Pagination pagination;

    @FXML
    void comboBoxMonthChanged(ActionEvent event) {

    }

    @FXML
    void comboBoxYearChanged(ActionEvent event) {

    }

    @FXML
    void viewReportClicked(ActionEvent event) {

    }

}

