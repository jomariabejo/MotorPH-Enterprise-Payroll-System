package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import lombok.Getter;
import lombok.Setter;

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
}
