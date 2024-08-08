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

    public void populatePieChartWithSampleData() {
        try {
            PieChart.Data[] sampleData = new PieChart.Data[]{
                    new PieChart.Data("Sick Leave", 30),
                    new PieChart.Data("Vacation Leave", 20),
                    new PieChart.Data("Personal Leave", 10),
                    new PieChart.Data("Unpaid Leave", 15)
            };

            pieChartLeaveBalance.setData(FXCollections.observableArrayList(sampleData));

            for (PieChart.Data data : sampleData) {
                data.setName(data.getName() + " (" + (int) data.getPieValue() + ")");
            }

            pieChartLeaveBalance.setTitle("Remaining Leave Balance");
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    public void populatePieChart() {
        // Assuming you have a method to fetch leave balance list
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Optional<List<LeaveBalance>> leaveBalanceOpt = this.getEmployeeRoleNavigationController().getMainViewController().getLeaveBalanceService().fetchEmployeeRemainingLeaveBalances(employee);

        if (leaveBalanceOpt.isPresent()) {
            try {
                List<LeaveBalance> leaveBalanceList = leaveBalanceOpt.get();

                ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

                for (LeaveBalance leaveBalance : leaveBalanceList) {
                    PieChart.Data data = new PieChart.Data(leaveBalance.getLeaveTypeID().getLeaveTypeName(), leaveBalance.getBalance());
                    pieChartData.add(data);
                }

                pieChartLeaveBalance.setData(pieChartData);

                for (PieChart.Data data : pieChartData) {
                    String formattedName = data.getName() + " (" + (int) data.getPieValue() + ")";
                    data.setName(formattedName);
                }

                pieChartLeaveBalance.setTitle("Remaining Leave Balance");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No leave balance data available.");
        }
    }


}
