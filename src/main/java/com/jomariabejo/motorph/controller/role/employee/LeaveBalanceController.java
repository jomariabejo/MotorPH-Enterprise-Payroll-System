package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveBalance;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class LeaveBalanceController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private PieChart pieChartLeaveBalance;

    public LeaveBalanceController() {
        // Constructor
    }

    public void populatePieChart() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Optional<List<LeaveBalance>> leaveBalanceOpt = this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveBalanceService().fetchEmployeeRemainingLeaveBalances(employee);

        if (leaveBalanceOpt.isPresent()) {
            try {
                List<LeaveBalance> leaveBalanceList = leaveBalanceOpt.get();
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                for (LeaveBalance leaveBalance : leaveBalanceList) {
                    String leaveTypeName = leaveBalance.getLeaveTypeID().getLeaveTypeName();
                    double balance = leaveBalance.getBalance();
                    String formattedName = String.format("%s (%d)", leaveTypeName, (int) balance);

                    PieChart.Data data = new PieChart.Data(formattedName, balance);
                    pieChartData.add(data);
                }

                pieChartLeaveBalance.setData(pieChartData);
                pieChartLeaveBalance.setTitle("Remaining Leave Balance");

            } catch (Exception e) {
                System.err.println("Error populating pie chart: " + e.getMessage());
                e.printStackTrace(); // Consider logging this exception to a file or monitoring system
            }
        } else {
            System.out.println("No leave balance data available.");
        }
    }
}
