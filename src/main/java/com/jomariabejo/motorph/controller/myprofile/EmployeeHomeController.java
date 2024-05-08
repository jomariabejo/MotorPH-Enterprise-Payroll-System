package com.jomariabejo.motorph.controller.myprofile;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

public class EmployeeHomeController  {
    @FXML
    private TabPane tabPane;

    @FXML
    private Label employee_id;

    @FXML
    private ComboBox<?> comboboxPayslipYear;

    @FXML
    private PieChart piechartApril;

    @FXML
    private PieChart piechartMay;

    @FXML
    private PieChart piechartJune;

    @FXML
    private PieChart piechartJul;

    @FXML
    private PieChart piechartAug;

    @FXML
    private PieChart piechartSept;

    @FXML
    private PieChart piechartOct;

    @FXML
    private PieChart piechartNov;

    @FXML
    private PieChart piechartDec;

    @FXML
    private PieChart piechartFeb;

    @FXML
    private PieChart piechartJan;

    @FXML
    private PieChart piechartMarch;

    @FXML
    private Label resultDeductions;

    @FXML
    private Label resultGrossIncome;

    @FXML
    private Label resultGrossPay;

    @FXML
    private Label resultPayslipID;

    @FXML
    private Label resultTakeHomePay;

    @FXML
    private Label username;

    @FXML
    void viewPayslip(ActionEvent event) {

    }

    @FXML
    public void tabClicked(MouseEvent mouseEvent) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        String month = selectedTab.getText();

        switch (month) {
            case "Jan":
                break;
            case "Feb":
                break;
            case "Mar":
                break;
            case "Apr":
                break;
            case "May":
                break;
            case "Jun":
                break;
            case "Jul":
                break;
            case "Aug":
                break;
            case "Sep":
                break;
            case "Oct":
                break;
            case "Nov":
                break;
            case "Dec":
                break;
            default:
                break;
        }
    }
}
