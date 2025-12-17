package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollApproval;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.utility.PesoUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class AccountingDashboard {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Label lblTotalPayrolls;

    @FXML
    private Label lblTotalGross;

    @FXML
    private Label lblTotalNet;

    @FXML
    private Label lblTotalDeductions;

    @FXML
    private Label lblTotalBenefits;

    @FXML
    private Label lblPendingApprovals;

    @FXML
    private ComboBox<String> cbPeriodType;

    @FXML
    private ComboBox<Integer> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private LineChart<String, Number> lineChartMonthlyTrend;

    @FXML
    private PieChart pieChartPayrollStatus;

    @FXML
    private BarChart<String, Number> barChartGrossVsNet;

    @FXML
    private PieChart pieChartDeductions;

    @FXML
    private AreaChart<String, Number> areaChartMonthlyExpenses;

    @FXML
    private BarChart<String, Number> barChartBenefits;

    private List<Payroll> allPayrolls;
    private List<Payslip> allPayslips;
    private List<PayrollApproval> allApprovals;

    private static final String PERIOD_MONTHLY = "Monthly";
    private static final String PERIOD_YEARLY = "Yearly";
    private static final String DATE_FORMAT_MONTH_YEAR = "MMM yyyy";
    private static final String KEY_GROSS = "gross";
    private static final String KEY_NET = "net";
    private static final String KEY_RICE = "rice";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CLOTHING = "clothing";

    public AccountingDashboard() {
        // Default constructor for FXML
    }

    @FXML
    void initialize() {
        setupPeriodControls();
        loadData();
        populateKPICards();
        populateCharts();
    }

    private void setupPeriodControls() {
        // Setup period type combo box
        cbPeriodType.setItems(FXCollections.observableArrayList(PERIOD_YEARLY, PERIOD_MONTHLY));
        cbPeriodType.getSelectionModel().selectFirst();
        cbPeriodType.setOnAction(e -> {
            cbMonth.setVisible(PERIOD_MONTHLY.equals(cbPeriodType.getValue()));
            filterAndUpdateCharts();
        });

        // Setup month combo box
        ObservableList<Integer> months = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            months.add(i);
        }
        cbMonth.setItems(months);
        cbMonth.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cbMonth.setOnAction(e -> filterAndUpdateCharts());

        // Setup year combo box
        int currentYear = Year.now().getValue();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = currentYear - 5; i <= currentYear; i++) {
            years.add(i);
        }
        cbYear.setItems(years);
        cbYear.getSelectionModel().select(years.size() - 1);
        cbYear.setOnAction(e -> filterAndUpdateCharts());
    }

    public void loadData() {
        if (payrollAdministratorNavigationController == null ||
                payrollAdministratorNavigationController.getMainViewController() == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        allPayrolls = serviceFactory.getPayrollService().getAllPayrolls();
        allPayslips = serviceFactory.getPayslipService().getAllPayslips();
        allApprovals = serviceFactory.getPayrollApprovalService().getAllPayrollApprovals();
    }

    public void populateKPICards() {
        if (allPayrolls == null || allPayslips == null || allApprovals == null) {
            return;
        }

        // Filter data based on selected period
        List<Payslip> filteredPayslips = filterPayslips();
        List<Payroll> filteredPayrolls = filterPayrolls();

        // Total Payrolls
        lblTotalPayrolls.setText(String.valueOf(filteredPayrolls.size()));

        // Total Gross Income
        BigDecimal totalGross = filteredPayslips.stream()
                .map(Payslip::getGrossIncome)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotalGross.setText(PesoUtility.formatToPeso(totalGross.toString()));

        // Total Net Pay
        BigDecimal totalNet = filteredPayslips.stream()
                .map(Payslip::getNetPay)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotalNet.setText(PesoUtility.formatToPeso(totalNet.toString()));

        // Total Deductions
        BigDecimal totalDeductions = filteredPayslips.stream()
                .map(Payslip::getTotalDeductions)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotalDeductions.setText(PesoUtility.formatToPeso(totalDeductions.toString()));

        // Total Benefits
        BigDecimal totalBenefits = filteredPayslips.stream()
                .map(Payslip::getTotalBenefits)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        lblTotalBenefits.setText(PesoUtility.formatToPeso(totalBenefits.toString()));

        // Pending Approvals
        long pendingCount = allApprovals.stream()
                .filter(approval -> "Pending".equals(approval.getStatus()))
                .count();
        lblPendingApprovals.setText(String.valueOf(pendingCount));
    }

    public void populateCharts() {
        populateMonthlyTrendChart();
        populatePayrollStatusChart();
        populateGrossVsNetChart();
        populateDeductionsChart();
        populateMonthlyExpensesChart();
        populateBenefitsChart();
    }

    private void populateMonthlyTrendChart() {
        XYChart.Series<String, Number> grossSeries = new XYChart.Series<>();
        grossSeries.setName("Gross Income");
        XYChart.Series<String, Number> netSeries = new XYChart.Series<>();
        netSeries.setName("Net Pay");

        List<Payslip> filteredPayslips = filterPayslips();
        Map<String, Map<String, BigDecimal>> monthlyData = new TreeMap<>();

        for (Payslip payslip : filteredPayslips) {
            if (payslip.getPayrollRunDate() == null) continue;

            LocalDate date = payslip.getPayrollRunDate().toLocalDate();
            String monthKey = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_YEAR));

            monthlyData.putIfAbsent(monthKey, new HashMap<>());
            Map<String, BigDecimal> monthData = monthlyData.get(monthKey);

            monthData.put(KEY_GROSS, monthData.getOrDefault(KEY_GROSS, BigDecimal.ZERO)
                    .add(payslip.getGrossIncome() != null ? payslip.getGrossIncome() : BigDecimal.ZERO));
            monthData.put(KEY_NET, monthData.getOrDefault(KEY_NET, BigDecimal.ZERO)
                    .add(payslip.getNetPay() != null ? payslip.getNetPay() : BigDecimal.ZERO));
        }

        for (Map.Entry<String, Map<String, BigDecimal>> entry : monthlyData.entrySet()) {
            grossSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_GROSS).doubleValue()));
            netSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_NET).doubleValue()));
        }

        lineChartMonthlyTrend.getData().clear();
        lineChartMonthlyTrend.getData().addAll(grossSeries, netSeries);
    }

    private void populatePayrollStatusChart() {
        List<Payroll> filteredPayrolls = filterPayrolls();
        Map<String, Long> statusCount = filteredPayrolls.stream()
                .collect(Collectors.groupingBy(
                        payroll -> payroll.getStatus() != null ? payroll.getStatus() : "Unknown",
                        Collectors.counting()
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Long> entry : statusCount.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }

        pieChartPayrollStatus.setData(pieChartData);
    }

    private void populateGrossVsNetChart() {
        XYChart.Series<String, Number> grossSeries = new XYChart.Series<>();
        grossSeries.setName("Gross Income");
        XYChart.Series<String, Number> netSeries = new XYChart.Series<>();
        netSeries.setName("Net Pay");

        List<Payslip> filteredPayslips = filterPayslips();
        Map<String, Map<String, BigDecimal>> periodData = new LinkedHashMap<>();

        for (Payslip payslip : filteredPayslips) {
            if (payslip.getPayrollRunDate() == null) continue;

            LocalDate date = payslip.getPayrollRunDate().toLocalDate();
            String periodKey;
            if (PERIOD_MONTHLY.equals(cbPeriodType.getValue())) {
                periodKey = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_YEAR));
            } else {
                periodKey = String.valueOf(date.getYear());
            }

            periodData.putIfAbsent(periodKey, new HashMap<>());
            Map<String, BigDecimal> period = periodData.get(periodKey);

            period.put(KEY_GROSS, period.getOrDefault(KEY_GROSS, BigDecimal.ZERO)
                    .add(payslip.getGrossIncome() != null ? payslip.getGrossIncome() : BigDecimal.ZERO));
            period.put(KEY_NET, period.getOrDefault(KEY_NET, BigDecimal.ZERO)
                    .add(payslip.getNetPay() != null ? payslip.getNetPay() : BigDecimal.ZERO));
        }

        for (Map.Entry<String, Map<String, BigDecimal>> entry : periodData.entrySet()) {
            grossSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_GROSS).doubleValue()));
            netSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_NET).doubleValue()));
        }

        barChartGrossVsNet.getData().clear();
        barChartGrossVsNet.getData().addAll(grossSeries, netSeries);
    }

    private void populateDeductionsChart() {
        List<Payslip> filteredPayslips = filterPayslips();

        BigDecimal totalSSS = filteredPayslips.stream()
                .map(Payslip::getSss)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPhilhealth = filteredPayslips.stream()
                .map(Payslip::getPhilhealth)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPagibig = filteredPayslips.stream()
                .map(Payslip::getPagIbig)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTax = filteredPayslips.stream()
                .map(Payslip::getWithholdingTax)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        if (totalSSS.compareTo(BigDecimal.ZERO) > 0) {
            pieChartData.add(new PieChart.Data("SSS", totalSSS.doubleValue()));
        }
        if (totalPhilhealth.compareTo(BigDecimal.ZERO) > 0) {
            pieChartData.add(new PieChart.Data("PhilHealth", totalPhilhealth.doubleValue()));
        }
        if (totalPagibig.compareTo(BigDecimal.ZERO) > 0) {
            pieChartData.add(new PieChart.Data("Pag-IBIG", totalPagibig.doubleValue()));
        }
        if (totalTax.compareTo(BigDecimal.ZERO) > 0) {
            pieChartData.add(new PieChart.Data("Withholding Tax", totalTax.doubleValue()));
        }

        pieChartDeductions.setData(pieChartData);
    }

    private void populateMonthlyExpensesChart() {
        XYChart.Series<String, Number> expensesSeries = new XYChart.Series<>();
        expensesSeries.setName("Total Expenses");

        List<Payslip> filteredPayslips = filterPayslips();
        Map<String, BigDecimal> monthlyExpenses = new TreeMap<>();

        for (Payslip payslip : filteredPayslips) {
            if (payslip.getPayrollRunDate() == null) continue;

            LocalDate date = payslip.getPayrollRunDate().toLocalDate();
            String monthKey = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_YEAR));

            BigDecimal totalExpense = (payslip.getGrossIncome() != null ? payslip.getGrossIncome() : BigDecimal.ZERO)
                    .add(payslip.getTotalBenefits() != null ? payslip.getTotalBenefits() : BigDecimal.ZERO);

            monthlyExpenses.put(monthKey, monthlyExpenses.getOrDefault(monthKey, BigDecimal.ZERO).add(totalExpense));
        }

        for (Map.Entry<String, BigDecimal> entry : monthlyExpenses.entrySet()) {
            expensesSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().doubleValue()));
        }

        areaChartMonthlyExpenses.getData().clear();
        areaChartMonthlyExpenses.getData().add(expensesSeries);
    }

    private void populateBenefitsChart() {
        XYChart.Series<String, Number> riceSeries = new XYChart.Series<>();
        riceSeries.setName("Rice Subsidy");
        XYChart.Series<String, Number> phoneSeries = new XYChart.Series<>();
        phoneSeries.setName("Phone Allowance");
        XYChart.Series<String, Number> clothingSeries = new XYChart.Series<>();
        clothingSeries.setName("Clothing Allowance");

        List<Payslip> filteredPayslips = filterPayslips();
        Map<String, Map<String, BigDecimal>> monthlyBenefits = new TreeMap<>();

        for (Payslip payslip : filteredPayslips) {
            if (payslip.getPayrollRunDate() == null) continue;

            LocalDate date = payslip.getPayrollRunDate().toLocalDate();
            String monthKey = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_MONTH_YEAR));

            monthlyBenefits.putIfAbsent(monthKey, new HashMap<>());
            Map<String, BigDecimal> monthData = monthlyBenefits.get(monthKey);

            monthData.put(KEY_RICE, monthData.getOrDefault(KEY_RICE, BigDecimal.ZERO)
                    .add(payslip.getRiceSubsidy() != null ? payslip.getRiceSubsidy() : BigDecimal.ZERO));
            monthData.put(KEY_PHONE, monthData.getOrDefault(KEY_PHONE, BigDecimal.ZERO)
                    .add(payslip.getPhoneAllowance() != null ? payslip.getPhoneAllowance() : BigDecimal.ZERO));
            monthData.put(KEY_CLOTHING, monthData.getOrDefault(KEY_CLOTHING, BigDecimal.ZERO)
                    .add(payslip.getClothingAllowance() != null ? payslip.getClothingAllowance() : BigDecimal.ZERO));
        }

        for (Map.Entry<String, Map<String, BigDecimal>> entry : monthlyBenefits.entrySet()) {
            riceSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_RICE).doubleValue()));
            phoneSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_PHONE).doubleValue()));
            clothingSeries.getData().add(new XYChart.Data<>(entry.getKey(),
                    entry.getValue().get(KEY_CLOTHING).doubleValue()));
        }

        barChartBenefits.getData().clear();
        barChartBenefits.getData().addAll(riceSeries, phoneSeries, clothingSeries);
    }

    private List<Payslip> filterPayslips() {
        if (allPayslips == null) return new ArrayList<>();

        Integer selectedYear = cbYear.getValue();
        if (selectedYear == null) return allPayslips;

        if (PERIOD_MONTHLY.equals(cbPeriodType.getValue())) {
            Integer selectedMonth = cbMonth.getValue();
            if (selectedMonth == null) return allPayslips;

            return allPayslips.stream()
                    .filter(payslip -> {
                        if (payslip.getPayrollRunDate() == null) return false;
                        LocalDate date = payslip.getPayrollRunDate().toLocalDate();
                        return date.getYear() == selectedYear && date.getMonthValue() == selectedMonth;
                    })
                    .collect(Collectors.toList());
        } else {
            return allPayslips.stream()
                    .filter(payslip -> {
                        if (payslip.getPayrollRunDate() == null) return false;
                        LocalDate date = payslip.getPayrollRunDate().toLocalDate();
                        return date.getYear() == selectedYear;
                    })
                    .collect(Collectors.toList());
        }
    }

    private List<Payroll> filterPayrolls() {
        if (allPayrolls == null) return new ArrayList<>();

        Integer selectedYear = cbYear.getValue();
        if (selectedYear == null) return allPayrolls;

        if (PERIOD_MONTHLY.equals(cbPeriodType.getValue())) {
            Integer selectedMonth = cbMonth.getValue();
            if (selectedMonth == null) return allPayrolls;

            return allPayrolls.stream()
                    .filter(payroll -> {
                        if (payroll.getPayrollRunDate() == null) return false;
                        LocalDate date = payroll.getPayrollRunDate().toLocalDate();
                        return date.getYear() == selectedYear && date.getMonthValue() == selectedMonth;
                    })
                    .collect(Collectors.toList());
        } else {
            return allPayrolls.stream()
                    .filter(payroll -> {
                        if (payroll.getPayrollRunDate() == null) return false;
                        LocalDate date = payroll.getPayrollRunDate().toLocalDate();
                        return date.getYear() == selectedYear;
                    })
                    .collect(Collectors.toList());
        }
    }

    private void filterAndUpdateCharts() {
        populateKPICards();
        populateCharts();
    }
}
