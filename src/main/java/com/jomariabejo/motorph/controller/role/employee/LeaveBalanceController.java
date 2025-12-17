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
                
                // Check if list is empty
                if (leaveBalanceList.isEmpty()) {
                    System.out.println("No leave balance data available for employee.");
                    pieChartLeaveBalance.setData(FXCollections.observableArrayList());
                    pieChartLeaveBalance.setTitle("Remaining Leave Balance - No Data Available");
                    return;
                }
                
                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                for (LeaveBalance leaveBalance : leaveBalanceList) {
                    try {
                        String leaveTypeName = leaveBalance.getLeaveTypeID().getLeaveTypeName();
                        double balance = leaveBalance.getBalance();
                        String formattedName = String.format("%s (%d)", leaveTypeName, (int) balance);

                        PieChart.Data data = new PieChart.Data(formattedName, balance);
                        pieChartData.add(data);
                    } catch (Exception e) {
                        System.err.println("Error processing leave balance entry: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (!pieChartData.isEmpty()) {
                    pieChartLeaveBalance.setData(pieChartData);
                    pieChartLeaveBalance.setTitle("Remaining Leave Balance");
                } else {
                    pieChartLeaveBalance.setData(FXCollections.observableArrayList());
                    pieChartLeaveBalance.setTitle("Remaining Leave Balance - No Valid Data");
                }

            } catch (Exception e) {
                System.err.println("Error populating pie chart: " + e.getMessage());
                e.printStackTrace(); // Consider logging this exception to a file or monitoring system
                pieChartLeaveBalance.setData(FXCollections.observableArrayList());
                pieChartLeaveBalance.setTitle("Remaining Leave Balance - Error Loading Data");
            }
        } else {
            System.out.println("No leave balance data available.");
            pieChartLeaveBalance.setData(FXCollections.observableArrayList());
            pieChartLeaveBalance.setTitle("Remaining Leave Balance - No Data Available");
        }
    }
}
