package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.service.ServiceFactory;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
<<<<<<< HEAD
import javafx.stage.Modality;
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.model.SssContributionRate;
import com.jomariabejo.motorph.model.PhilhealthContributionRate;
import com.jomariabejo.motorph.model.PagibigContributionRate;
import com.jomariabejo.motorph.model.YearToDateFigures;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class PayrollEmployeeListViewDialog {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;
    private Payroll payroll;
    private Map<Employee, Payslip> employeePayslipMap = new HashMap<>();

    @FXML
    private Label lblPayrollInfo;

    @FXML
    private TableView<EmployeePayslipRow> tvEmployees;

    @FXML
    private Button btnGeneratePayslips;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnClose;

    @FXML
    void initialize() {
        setupTableView();
        customizeButtons();
    }

    public void setup(Payroll payroll, PayrollAdministratorNavigationController navController) {
        this.payroll = payroll;
        this.payrollAdministratorNavigationController = navController;
        updatePayrollInfoLabel();
        loadEmployeeData();
    }

    private void updatePayrollInfoLabel() {
        if (payroll != null) {
            String info = String.format("Payroll #%d - Period: %s to %s | Status: %s",
                    payroll.getId(),
                    payroll.getPeriodStartDate(),
                    payroll.getPeriodEndDate(),
                    payroll.getStatus());
            lblPayrollInfo.setText(info);
        }
    }

    private void customizeButtons() {
        FontIcon refreshIcon = new FontIcon(Feather.REFRESH_CW);
        btnRefresh.setGraphic(refreshIcon);
        btnRefresh.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

        FontIcon generateIcon = new FontIcon(Feather.FILE_PLUS);
        btnGeneratePayslips.setGraphic(generateIcon);
        btnGeneratePayslips.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

        FontIcon closeIcon = new FontIcon(Feather.X);
        btnClose.setGraphic(closeIcon);
        btnClose.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void setupTableView() {
        tvEmployees.getColumns().clear();

        TableColumn<EmployeePayslipRow, Integer> empNumColumn = new TableColumn<>("Employee #");
        empNumColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEmployeeNumber()));
        empNumColumn.setPrefWidth(100);

        TableColumn<EmployeePayslipRow, String> nameColumn = new TableColumn<>("Employee Name");
        nameColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmployeeName()));
        nameColumn.setPrefWidth(200);

        TableColumn<EmployeePayslipRow, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartment()));
        departmentColumn.setPrefWidth(150);

        TableColumn<EmployeePayslipRow, String> positionColumn = new TableColumn<>("Position");
        positionColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPosition()));
        positionColumn.setPrefWidth(150);

        TableColumn<EmployeePayslipRow, String> grossIncomeColumn = new TableColumn<>("Gross Income");
        grossIncomeColumn.setCellValueFactory(cellData -> {
            BigDecimal gross = cellData.getValue().getGrossIncome();
            return new javafx.beans.property.SimpleStringProperty(
                    gross != null ? String.format("₱%,.2f", gross) : "N/A");
        });
        grossIncomeColumn.setPrefWidth(120);

        TableColumn<EmployeePayslipRow, String> netPayColumn = new TableColumn<>("Net Pay");
        netPayColumn.setCellValueFactory(cellData -> {
            BigDecimal net = cellData.getValue().getNetPay();
            return new javafx.beans.property.SimpleStringProperty(
                    net != null ? String.format("₱%,.2f", net) : "N/A");
        });
        netPayColumn.setPrefWidth(120);

        TableColumn<EmployeePayslipRow, String> statusColumn = new TableColumn<>("Payslip Status");
        statusColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPayslipStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<EmployeePayslipRow, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(150);

        tvEmployees.getColumns().addAll(empNumColumn, nameColumn, departmentColumn, positionColumn,
                grossIncomeColumn, netPayColumn, statusColumn, actionsColumn);
        tvEmployees.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<EmployeePayslipRow, Void> createActionsColumn() {
        TableColumn<EmployeePayslipRow, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));
            private final HBox actionsBox = new HBox(viewButton);

            {
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(5);

                viewButton.setOnAction(event -> {
                    EmployeePayslipRow row = getTableView().getItems().get(getIndex());
                    if (row != null && row.getPayslip() != null) {
                        viewPayslip(row.getPayslip());
                    } else {
                        CustomAlert alert = new CustomAlert(
                                Alert.AlertType.INFORMATION,
                                "No Payslip",
                                "Payslip has not been generated for this employee yet."
                        );
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }

    private void viewPayslip(Payslip payslip) {
        if (payrollAdministratorNavigationController == null) {
            return;
        }

        try {
            // Get year-to-date figures
            var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
            Optional<YearToDateFigures> ytdOpt = serviceFactory.getPayslipService().getYearToDateFigures(
                    payslip.getEmployeeID(), 
                    Year.from(payslip.getPayrollRunDate().toLocalDate())
            );
            YearToDateFigures ytd = ytdOpt.orElse(new YearToDateFigures(
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
            ));

            // Load preview dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/payslip-preview-dialog.fxml"));
            AnchorPane dialogPane = loader.load();
            Stage dialogStage = new Stage();
            
<<<<<<< HEAD
            // Set parent stage and modality to prevent interaction with parent window
            // Get parent stage from btnClose's scene if available
            if (btnClose != null && btnClose.getScene() != null) {
                Stage parentStage = (Stage) btnClose.getScene().getWindow();
                if (parentStage != null) {
                    dialogStage.initOwner(parentStage);
                }
            }
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Add close request handler to properly handle X button clicks
            dialogStage.setOnCloseRequest(event -> dialogStage.close());
            
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
            String employeeName = payslip.getEmployeeID().getFirstName() + " " + payslip.getEmployeeID().getLastName();
            dialogStage.setTitle("Payslip Preview - " + employeeName);
            dialogStage.setScene(new Scene(dialogPane));

            PayslipPreviewDialog previewController = loader.getController();
            previewController.setup(payslip, ytd, this);

            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to open payslip preview: " + (e.getMessage() != null ? e.getMessage() : "Unknown error")
            );
            errorAlert.showAndWait();
        }
    }

    @FXML
    void refreshClicked(ActionEvent event) {
        loadEmployeeData();
    }

    @FXML
    void generatePayslipsClicked(ActionEvent event) {
        if (payroll == null) {
            return;
        }

        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Generate Payslips",
                "This will generate payslips for all active employees for this payroll period. Continue?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                payrollAdministratorNavigationController.getMainViewController()
                        .getServiceFactory()
                        .getPayrollService()
                        .generatePayslipsForPayroll(payroll, 
                                payrollAdministratorNavigationController.getMainViewController().getServiceFactory());

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Payslips generated successfully."
                );
                successAlert.showAndWait();
                loadEmployeeData();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to generate payslips: " + e.getMessage()
                );
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    void closeClicked(ActionEvent event) {
        btnClose.getScene().getWindow().hide();
    }

    private void loadEmployeeData() {
        if (payroll == null || payrollAdministratorNavigationController == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        
        // Get existing payslips for this payroll
        Optional<List<Payslip>> existingPayslipsOpt = serviceFactory.getPayslipService().getPayslipsByPayroll(payroll);
        Map<Integer, Payslip> payslipMap = new HashMap<>();
        if (existingPayslipsOpt.isPresent()) {
            for (Payslip payslip : existingPayslipsOpt.get()) {
                payslipMap.put(payslip.getEmployeeID().getEmployeeNumber(), payslip);
            }
        }

        // Get all active employees
        Optional<List<Employee>> activeEmployeesOpt = serviceFactory.getEmployeeService().getActiveEmployees();
        
        ObservableList<EmployeePayslipRow> rows = FXCollections.observableArrayList();
        
        if (activeEmployeesOpt.isPresent()) {
            for (Employee employee : activeEmployeesOpt.get()) {
                Payslip payslip = payslipMap.get(employee.getEmployeeNumber());
                
                EmployeePayslipRow row = new EmployeePayslipRow();
                row.setEmployee(employee);
                row.setEmployeeNumber(employee.getEmployeeNumber());
                row.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
                
                if (employee.getPositionID() != null) {
                    row.setPosition(employee.getPositionID().getPositionName());
                    if (employee.getPositionID().getDepartmentID() != null) {
                        row.setDepartment(employee.getPositionID().getDepartmentID().getDepartmentName());
                    } else {
                        row.setDepartment("N/A");
                    }
                } else {
                    row.setPosition("N/A");
                    row.setDepartment("N/A");
                }
                
                if (payslip != null) {
                    row.setPayslip(payslip);
                    row.setGrossIncome(payslip.getGrossIncome());
                    row.setNetPay(payslip.getNetPay());
                    row.setPayslipStatus("Generated");
                } else {
                    row.setPayslip(null);
                    // Calculate preview values even if payslip not generated yet
                    PayrollCalculationPreview preview = calculatePayrollPreview(employee, payroll, serviceFactory);
                    row.setGrossIncome(preview.getGrossIncome());
                    row.setNetPay(preview.getNetPay());
                    row.setPayslipStatus("Not Generated");
                }
                
                rows.add(row);
            }
        }

        tvEmployees.setItems(rows);
    }

    /**
     * Calculate payroll preview for an employee without generating a payslip.
     * Uses the same calculation logic as PayrollService.
     */
    private PayrollCalculationPreview calculatePayrollPreview(Employee employee, Payroll payroll, ServiceFactory serviceFactory) {
        PayrollCalculationPreview preview = new PayrollCalculationPreview();
        
        if (payroll == null || employee == null) {
            return preview;
        }
        
        LocalDate periodStart = payroll.getPeriodStartDate().toLocalDate();
        LocalDate periodEnd = payroll.getPeriodEndDate().toLocalDate();
        
        // Calculate hours worked
        BigDecimal totalHoursWorked = calculatePreviewHoursWorked(employee, periodStart, periodEnd, serviceFactory);
        
        // Get hourly rate (calculate if not set)
        BigDecimal hourlyRate = employee.getHourlyRate();
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
            hourlyRate = calculateHourlyRateFromSalary(employee);
        }
        
        // Calculate gross income
        BigDecimal grossIncome = BigDecimal.ZERO;
        if (hourlyRate != null && hourlyRate.compareTo(BigDecimal.ZERO) > 0) {
            grossIncome = hourlyRate.multiply(totalHoursWorked);
        }
        preview.setGrossIncome(grossIncome);
        
        // Calculate benefits
        BigDecimal riceSubsidy = employee.getRiceSubsidy() != null ? employee.getRiceSubsidy() : BigDecimal.ZERO;
        BigDecimal phoneAllowance = employee.getPhoneAllowance() != null ? employee.getPhoneAllowance() : BigDecimal.ZERO;
        BigDecimal clothingAllowance = employee.getClothingAllowance() != null ? employee.getClothingAllowance() : BigDecimal.ZERO;
        BigDecimal totalBenefits = riceSubsidy.add(phoneAllowance).add(clothingAllowance);
        
        // Calculate taxable income
        BigDecimal taxableIncome = grossIncome.add(totalBenefits);
        
        // Calculate deductions
        BigDecimal sssDeduction = calculatePreviewSSSDeduction(taxableIncome, serviceFactory);
        BigDecimal philhealthDeduction = calculatePreviewPhilhealthDeduction(taxableIncome, serviceFactory);
        BigDecimal pagibigDeduction = calculatePreviewPagibigDeduction(taxableIncome, serviceFactory);
        BigDecimal withholdingTax = calculatePreviewWithholdingTax(taxableIncome);
        
        BigDecimal totalDeductions = sssDeduction.add(philhealthDeduction)
                .add(pagibigDeduction).add(withholdingTax);
        
        // Calculate net pay
        BigDecimal netPay = grossIncome.add(totalBenefits).subtract(totalDeductions);
        preview.setNetPay(netPay.max(BigDecimal.ZERO));
        
        return preview;
    }
    
    private BigDecimal calculatePreviewHoursWorked(Employee employee, LocalDate periodStart, LocalDate periodEnd, ServiceFactory serviceFactory) {
        BigDecimal totalHours = BigDecimal.ZERO;
        
        YearMonth startYearMonth = YearMonth.from(periodStart);
        YearMonth endYearMonth = YearMonth.from(periodEnd);
        YearMonth currentYearMonth = startYearMonth;
        
        while (!currentYearMonth.isAfter(endYearMonth)) {
            Year year = Year.of(currentYearMonth.getYear());
            Optional<List<Timesheet>> timesheetsOpt = serviceFactory.getTimesheetService()
                    .getTimesheetsByEmployeeAndDate(employee, year, currentYearMonth.getMonth());
            
            if (timesheetsOpt.isPresent()) {
                for (Timesheet timesheet : timesheetsOpt.get()) {
                    LocalDate timesheetDate = timesheet.getDate();
                    if (!timesheetDate.isBefore(periodStart) && !timesheetDate.isAfter(periodEnd)) {
                        if (timesheet.getHoursWorked() != null && timesheet.getHoursWorked() > 0) {
                            totalHours = totalHours.add(BigDecimal.valueOf(timesheet.getHoursWorked()));
                        } else if (timesheet.getTimeIn() != null && timesheet.getTimeOut() != null) {
                            BigDecimal calculatedHours = calculateHoursFromTimesheet(timesheet);
                            totalHours = totalHours.add(calculatedHours);
                        }
                    }
                }
            }
            currentYearMonth = currentYearMonth.plusMonths(1);
        }
        
        // Fallback to working days calculation
        if (totalHours.compareTo(BigDecimal.ZERO) == 0) {
            long workingDays = periodStart.datesUntil(periodEnd.plusDays(1))
                    .filter(date -> {
                        int dayOfWeek = date.getDayOfWeek().getValue();
                        return dayOfWeek >= DayOfWeek.MONDAY.getValue() && dayOfWeek <= DayOfWeek.FRIDAY.getValue();
                    })
                    .count();
            totalHours = BigDecimal.valueOf(workingDays * 8);
        }
        
        return totalHours.setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateHoursFromTimesheet(Timesheet timesheet) {
        if (timesheet.getTimeIn() == null || timesheet.getTimeOut() == null) {
            return BigDecimal.ZERO;
        }
        
        LocalTime timeIn = timesheet.getTimeIn().toLocalTime();
        LocalTime timeOut = timesheet.getTimeOut().toLocalTime();
        
        LocalTime effectiveTimeIn = timeIn.isBefore(LocalTime.of(8, 0)) ? LocalTime.of(8, 0) : timeIn;
        LocalTime effectiveTimeOut = timeOut.isAfter(LocalTime.of(17, 0)) ? LocalTime.of(17, 0) : timeOut;
        
        if (effectiveTimeOut.isBefore(effectiveTimeIn)) {
            return BigDecimal.ZERO;
        }
        
        long minutesWorked = java.time.Duration.between(effectiveTimeIn, effectiveTimeOut).toMinutes();
        return BigDecimal.valueOf(minutesWorked).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculateHourlyRateFromSalary(Employee employee) {
        BigDecimal monthlySalary = BigDecimal.ZERO;
        
        if (employee.getGrossSemiMonthlyRate() != null && employee.getGrossSemiMonthlyRate().compareTo(BigDecimal.ZERO) > 0) {
            monthlySalary = employee.getGrossSemiMonthlyRate().multiply(BigDecimal.valueOf(2));
        } else if (employee.getBasicSalary() != null && employee.getBasicSalary().compareTo(BigDecimal.ZERO) > 0) {
            monthlySalary = employee.getBasicSalary();
        }
        
        if (monthlySalary.compareTo(BigDecimal.ZERO) > 0) {
            return monthlySalary.divide(BigDecimal.valueOf(176), 4, RoundingMode.HALF_UP); // 176 hours/month
        }
        
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculatePreviewSSSDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        List<SssContributionRate> rates = serviceFactory.getSssContributionRateService().getAllSssContributionRates();
        
        if (rates.isEmpty()) {
            return taxableIncome.multiply(BigDecimal.valueOf(0.045)).setScale(4, RoundingMode.HALF_UP);
        }
        
        SssContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1));
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculatePreviewPhilhealthDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        List<PhilhealthContributionRate> rates = serviceFactory.getPhilhealthContributionRateService().getAllRates();
        
        if (rates.isEmpty()) {
            return taxableIncome.multiply(BigDecimal.valueOf(0.015)).setScale(4, RoundingMode.HALF_UP);
        }
        
        PhilhealthContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1));
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculatePreviewPagibigDeduction(BigDecimal taxableIncome, ServiceFactory serviceFactory) {
        List<PagibigContributionRate> rates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        
        if (rates.isEmpty()) {
            return taxableIncome.multiply(BigDecimal.valueOf(0.01)).setScale(4, RoundingMode.HALF_UP);
        }
        
        PagibigContributionRate applicableRate = rates.stream()
                .filter(rate -> taxableIncome.compareTo(rate.getSalaryBracketFrom()) >= 0 &&
                               taxableIncome.compareTo(rate.getSalaryBracketTo()) <= 0)
                .findFirst()
                .orElse(rates.get(rates.size() - 1));
        
        return applicableRate.getEmployeeShare().setScale(4, RoundingMode.HALF_UP);
    }
    
    private BigDecimal calculatePreviewWithholdingTax(BigDecimal taxableIncome) {
        if (taxableIncome.compareTo(BigDecimal.valueOf(20833)) <= 0) {
            return BigDecimal.ZERO;
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(33333)) <= 0) {
            return taxableIncome.subtract(BigDecimal.valueOf(20833))
                    .multiply(BigDecimal.valueOf(0.20))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(66667)) <= 0) {
            return BigDecimal.valueOf(2500)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(33333))
                            .multiply(BigDecimal.valueOf(0.25)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(166667)) <= 0) {
            return BigDecimal.valueOf(10833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(66667))
                            .multiply(BigDecimal.valueOf(0.30)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(666667)) <= 0) {
            return BigDecimal.valueOf(40833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(166667))
                            .multiply(BigDecimal.valueOf(0.32)))
                    .setScale(4, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.valueOf(200833.33)
                    .add(taxableIncome.subtract(BigDecimal.valueOf(666667))
                            .multiply(BigDecimal.valueOf(0.35)))
                    .setScale(4, RoundingMode.HALF_UP);
        }
    }

    // Inner class for payroll calculation preview
    @Getter
    @Setter
    private static class PayrollCalculationPreview {
        private BigDecimal grossIncome = BigDecimal.ZERO;
        private BigDecimal netPay = BigDecimal.ZERO;
    }

    // Inner class to represent table row data
    @Getter
    @Setter
    public static class EmployeePayslipRow {
        private Employee employee;
        private Integer employeeNumber;
        private String employeeName;
        private String department;
        private String position;
        private BigDecimal grossIncome;
        private BigDecimal netPay;
        private String payslipStatus;
        private Payslip payslip;
    }
}

