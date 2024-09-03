package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HRDashboardController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;


    @FXML
    private AreaChart<?, ?> areaChartCumulativePayrollExpenses;

    @FXML
    private BarChart<?, ?> barChartPayrollCostByDepartment;

    @FXML
    private LineChart<?, ?> lineChartPayrollExpenseTrends;

    @FXML
    private PieChart pieChartEmployeeBenefitsDistribution;

    public HRDashboardController() {
    }

}
