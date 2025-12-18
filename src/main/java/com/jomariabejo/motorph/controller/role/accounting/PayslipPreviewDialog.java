package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.YearToDateFigures;
import com.jomariabejo.motorph.service.PayslipPdfService;
import com.jomariabejo.motorph.service.UserService;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.PesoUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
public class PayslipPreviewDialog {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final SimpleDateFormat DATE_FORMAT_SHORT = new SimpleDateFormat("MMM dd, yyyy");

    private Payslip payslip;
    private YearToDateFigures ytd;
    private PayrollEmployeeListViewDialog parentDialog;
    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    // Header Labels
    @FXML
    private Label lblEmployeeName;
    @FXML
    private Label lblPayPeriod;

    // Employee Information Labels
    @FXML
    private Label lblEmployeeID;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPosition;
    @FXML
    private Label lblDepartment;
    @FXML
    private Label lblHourlyRate;
    @FXML
    private Label lblMonthlyRate;
    @FXML
    private Label lblDateHired;
    @FXML
    private Label lblTIN;
    @FXML
    private Label lblSSS;
    @FXML
    private Label lblPhilhealth;
    @FXML
    private Label lblPagibig;

    // Earnings Table
    @FXML
    private TableView<EarningsRow> tvEarnings;
    @FXML
    private TableColumn<EarningsRow, String> colEarningsDesc;
    @FXML
    private TableColumn<EarningsRow, String> colEarningsRate;
    @FXML
    private TableColumn<EarningsRow, String> colEarningsAmount;
    @FXML
    private Label lblGrossPay;

    // Benefits Table
    @FXML
    private TableView<BenefitsRow> tvBenefits;
    @FXML
    private TableColumn<BenefitsRow, String> colBenefitsDesc;
    @FXML
    private TableColumn<BenefitsRow, String> colBenefitsAmount;
    @FXML
    private Label lblTotalBenefits;

    // Deductions Table
    @FXML
    private TableView<DeductionsRow> tvDeductions;
    @FXML
    private TableColumn<DeductionsRow, String> colDeductionsDesc;
    @FXML
    private TableColumn<DeductionsRow, String> colDeductionsAmount;
    @FXML
    private Label lblTotalDeductions;

    // Withholding Tax Calculation Labels
    @FXML
    private Label lblTaxableIncome;
    @FXML
    private Label lblTaxBracket;
    @FXML
    private Label lblTaxCalculation;
    @FXML
    private Label lblWithholdingTaxBreakdown;

    // Summary Labels
    @FXML
    private Label lblSummaryGrossIncome;
    @FXML
    private Label lblSummaryTotalBenefits;
    @FXML
    private Label lblSummaryTotalDeductions;
    @FXML
    private Label lblNetPay;

    // Year-to-Date Labels
    @FXML
    private Label lblYTDGrossIncome;
    @FXML
    private Label lblYTDTaxableIncome;
    @FXML
    private Label lblYTDTotalBenefits;
    @FXML
    private Label lblYTDWithholdingTax;
    @FXML
    private Label lblYTDTotalDeductions;
    @FXML
    private Label lblYTDNetPay;

    // Buttons
    @FXML
    private Button btnSavePayslip;
    @FXML
    private Button btnClose;

    @FXML
    void initialize() {
        setupTables();
        customizeButtons();
    }

    public void setup(Payslip payslip, YearToDateFigures ytd, PayrollEmployeeListViewDialog parentDialog) {
        this.payslip = payslip;
        this.ytd = ytd;
        this.parentDialog = parentDialog;
        this.payrollAdministratorNavigationController = parentDialog.getPayrollAdministratorNavigationController();
        populatePayslipData();
    }

    private void customizeButtons() {
        FontIcon saveIcon = new FontIcon(Feather.SAVE);
        btnSavePayslip.setGraphic(saveIcon);
        btnSavePayslip.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

        FontIcon closeIcon = new FontIcon(Feather.X);
        btnClose.setGraphic(closeIcon);
        btnClose.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void setupTables() {
        // Earnings Table
        colEarningsDesc.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colEarningsRate.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRate()));
        colEarningsAmount.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAmount()));

        // Benefits Table
        colBenefitsDesc.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colBenefitsAmount.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAmount()));

        // Deductions Table
        colDeductionsDesc.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        colDeductionsAmount.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAmount()));
    }

    private void populatePayslipData() {
        if (payslip == null) {
            return;
        }

        Employee employee = payslip.getEmployeeID();

        // Header
        lblEmployeeName.setText(employee.getFirstName() + " " + employee.getLastName());
        String periodStart = DATE_FORMAT_SHORT.format(payslip.getPeriodStartDate());
        String periodEnd = DATE_FORMAT_SHORT.format(payslip.getPeriodEndDate());
        lblPayPeriod.setText(periodStart + " - " + periodEnd);

        // Employee Information
        lblEmployeeID.setText(String.valueOf(employee.getEmployeeNumber()));
        
        // Get email
        String email = "N/A";
        if (payrollAdministratorNavigationController != null) {
            UserService userService = payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getUserService();
            email = userService.fetchEmailByEmployeeId(employee)
                    .map(user -> user.getEmail())
                    .orElse("N/A");
        }
        lblEmail.setText(email);
        
        lblPosition.setText(payslip.getEmployeePosition() != null ? 
                payslip.getEmployeePosition().getPositionName() : "N/A");
        lblDepartment.setText(payslip.getEmployeeDepartment() != null ? 
                payslip.getEmployeeDepartment().getDepartmentName() : "N/A");
        lblHourlyRate.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getHourlyRate()))));
        lblMonthlyRate.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getMonthlyRate()))));
        lblDateHired.setText(DATE_FORMAT_SHORT.format(employee.getDateHired()));
        lblTIN.setText(employee.getTINNumber() != null ? employee.getTINNumber() : "N/A");
        lblSSS.setText(employee.getSSSNumber() != null ? employee.getSSSNumber() : "N/A");
        lblPhilhealth.setText(employee.getPhilhealthNumber() != null ? employee.getPhilhealthNumber() : "N/A");
        lblPagibig.setText(employee.getPagibigNumber() != null ? employee.getPagibigNumber() : "N/A");

        // Earnings
        populateEarningsTable();
        lblGrossPay.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))));

        // Benefits
        populateBenefitsTable();
        lblTotalBenefits.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))));

        // Deductions
        populateDeductionsTable();
        lblTotalDeductions.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))));

        // Withholding Tax Calculation
        populateWithholdingTaxBreakdown();

        // Summary
        lblSummaryGrossIncome.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getGrossIncome()))));
        lblSummaryTotalBenefits.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalBenefits()))));
        lblSummaryTotalDeductions.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getTotalDeductions()))));
        lblNetPay.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getNetPay()))));

        // Year-to-Date
        if (ytd != null) {
            lblYTDGrossIncome.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdGrossIncome()))));
            lblYTDTaxableIncome.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTaxableIncome()))));
            lblYTDTotalBenefits.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTotalBenefits()))));
            lblYTDWithholdingTax.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdWitholdingTax()))));
            lblYTDTotalDeductions.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdTotalDeductions()))));
            lblYTDNetPay.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(ytd.ytdNetPay()))));
        }
    }

    private void populateEarningsTable() {
        ObservableList<EarningsRow> earnings = FXCollections.observableArrayList();

        // Regular Hours
        BigDecimal regularHours = payslip.getTotalHoursWorked().subtract(payslip.getOvertimeHours());
        BigDecimal regularHoursPay = regularHours.multiply(payslip.getHourlyRate());
        earnings.add(new EarningsRow(
                "Regular Hours",
                regularHours + " hrs @ " + toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getHourlyRate()))) + "/hr",
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(regularHoursPay)))
        ));

        // Overtime
        if (payslip.getOvertimeHours().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal overtimePay = payslip.getOvertimeHours().multiply(payslip.getHourlyRate())
                    .multiply(BigDecimal.valueOf(1.25));
            earnings.add(new EarningsRow(
                    "Overtime Hours",
                    payslip.getOvertimeHours() + " hrs @ 1.25x",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(overtimePay)))
            ));
        }

        // Bonus
        if (payslip.getBonus() != null && payslip.getBonus().compareTo(BigDecimal.ZERO) > 0) {
            earnings.add(new EarningsRow(
                    "Taxable Bonus",
                    "-",
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getBonus())))
            ));
        }

        tvEarnings.setItems(earnings);
    }

    private void populateBenefitsTable() {
        ObservableList<BenefitsRow> benefits = FXCollections.observableArrayList();
        benefits.add(new BenefitsRow("Rice Subsidy", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getRiceSubsidy())))));
        benefits.add(new BenefitsRow("Phone Allowance", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPhoneAllowance())))));
        benefits.add(new BenefitsRow("Clothing Allowance", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getClothingAllowance())))));
        tvBenefits.setItems(benefits);
    }

    private void populateDeductionsTable() {
        ObservableList<DeductionsRow> deductions = FXCollections.observableArrayList();
        deductions.add(new DeductionsRow("SSS", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getSss())))));
        deductions.add(new DeductionsRow("Philhealth", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPhilhealth())))));
        deductions.add(new DeductionsRow("Pag-IBIG", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getPagIbig())))));
        deductions.add(new DeductionsRow("Withholding Tax", 
                toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getWithholdingTax())))));
        
        if (payslip.getOtherDeductions() != null && payslip.getOtherDeductions().compareTo(BigDecimal.ZERO) > 0) {
            deductions.add(new DeductionsRow("Other Deductions", 
                    toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getOtherDeductions())))));
        }

        tvDeductions.setItems(deductions);
    }

    private void populateWithholdingTaxBreakdown() {
        BigDecimal taxableIncome = payslip.getTaxableIncome() != null ? 
                payslip.getTaxableIncome() : BigDecimal.ZERO;
        
        lblTaxableIncome.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(taxableIncome))));
        
        // Calculate withholding tax breakdown using BIR tax brackets
        WithholdingTaxBreakdown breakdown = calculateWithholdingTaxBreakdown(taxableIncome);
        
        lblTaxBracket.setText(breakdown.getBracket());
        lblTaxCalculation.setText(breakdown.getCalculation());
        lblWithholdingTaxBreakdown.setText(toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(payslip.getWithholdingTax()))));
    }

    private WithholdingTaxBreakdown calculateWithholdingTaxBreakdown(BigDecimal taxableIncome) {
        // BIR withholding tax calculation (2023 rates)
        // Same logic as PayrollService.calculateWithholdingTax
        
        if (taxableIncome.compareTo(BigDecimal.valueOf(20833)) <= 0) {
            return new WithholdingTaxBreakdown(
                    "0% (PHP 0 - 20,833)",
                    "No tax (below threshold)",
                    BigDecimal.ZERO
            );
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(33333)) <= 0) {
            BigDecimal excess = taxableIncome.subtract(BigDecimal.valueOf(20833));
            BigDecimal tax = excess.multiply(BigDecimal.valueOf(0.20))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            return new WithholdingTaxBreakdown(
                    "20% (PHP 20,833 - 33,333)",
                    String.format("20%% of excess over PHP 20,833 = 20%% × %s = %s",
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(excess))),
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(tax)))),
                    tax
            );
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(66667)) <= 0) {
            BigDecimal excess = taxableIncome.subtract(BigDecimal.valueOf(33333));
            BigDecimal tax = BigDecimal.valueOf(2500)
                    .add(excess.multiply(BigDecimal.valueOf(0.25)))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            return new WithholdingTaxBreakdown(
                    "25% (PHP 33,333 - 66,667)",
                    String.format("PHP 2,500 + 25%% of excess over PHP 33,333 = PHP 2,500 + 25%% × %s = %s",
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(excess))),
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(tax)))),
                    tax
            );
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(166667)) <= 0) {
            BigDecimal excess = taxableIncome.subtract(BigDecimal.valueOf(66667));
            BigDecimal tax = BigDecimal.valueOf(10833.33)
                    .add(excess.multiply(BigDecimal.valueOf(0.30)))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            return new WithholdingTaxBreakdown(
                    "30% (PHP 66,667 - 166,667)",
                    String.format("PHP 10,833.33 + 30%% of excess over PHP 66,667 = PHP 10,833.33 + 30%% × %s = %s",
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(excess))),
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(tax)))),
                    tax
            );
        } else if (taxableIncome.compareTo(BigDecimal.valueOf(666667)) <= 0) {
            BigDecimal excess = taxableIncome.subtract(BigDecimal.valueOf(166667));
            BigDecimal tax = BigDecimal.valueOf(40833.33)
                    .add(excess.multiply(BigDecimal.valueOf(0.32)))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            return new WithholdingTaxBreakdown(
                    "32% (PHP 166,667 - 666,667)",
                    String.format("PHP 40,833.33 + 32%% of excess over PHP 166,667 = PHP 40,833.33 + 32%% × %s = %s",
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(excess))),
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(tax)))),
                    tax
            );
        } else {
            BigDecimal excess = taxableIncome.subtract(BigDecimal.valueOf(666667));
            BigDecimal tax = BigDecimal.valueOf(200833.33)
                    .add(excess.multiply(BigDecimal.valueOf(0.35)))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
            return new WithholdingTaxBreakdown(
                    "35% (PHP 666,667 and above)",
                    String.format("PHP 200,833.33 + 35%% of excess over PHP 666,667 = PHP 200,833.33 + 35%% × %s = %s",
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(excess))),
                            toPdfSafePeso(PesoUtility.formatToPeso(String.valueOf(tax)))),
                    tax
            );
        }
    }

    @FXML
    void savePayslipClicked() {
        if (payslip == null || payrollAdministratorNavigationController == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        PayslipPdfService pdfService = serviceFactory.getPayslipPdfService();

        // Show loading dialog
        Dialog<Void> loadingDialog = new Dialog<>();
        loadingDialog.setTitle("Generating PDF");
        loadingDialog.setContentText("Please wait while the payslip PDF is being generated...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.setResultConverter(buttonType -> null);
        
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(-1); // Indeterminate progress
        loadingDialog.getDialogPane().setContent(progressIndicator);

        // Show loading dialog
        loadingDialog.show();

        // Generate PDF in background thread
        CompletableFuture<java.io.File> pdfFuture = CompletableFuture.supplyAsync(() -> {
            try {
                String tempDir = System.getProperty("java.io.tmpdir");
                return pdfService.generatePayslipPdf(payslip, tempDir);
            } catch (Exception e) {
                throw new RuntimeException("PDF generation failed", e);
            }
        }, executorService);

        // Handle completion on JavaFX thread
        pdfFuture.whenComplete((tempPdfFile, throwable) -> {
            javafx.application.Platform.runLater(() -> {
                try {
                    // Close loading dialog
                    loadingDialog.close();

                    if (throwable != null) {
                        // Error occurred
                        Throwable cause = throwable.getCause() != null ? throwable.getCause() : throwable;
                        CustomAlert errorAlert = new CustomAlert(
                                Alert.AlertType.ERROR,
                                "PDF Generation Failed",
                                "Failed to generate payslip PDF: " + 
                                (cause.getMessage() != null ? cause.getMessage() : "Unknown error")
                        );
                        errorAlert.showAndWait();
                        return;
                    }

                    if (tempPdfFile == null || !tempPdfFile.exists()) {
                        CustomAlert errorAlert = new CustomAlert(
                                Alert.AlertType.ERROR,
                                "PDF Generation Failed",
                                "Failed to generate payslip PDF. The file was not created."
                        );
                        errorAlert.showAndWait();
                        return;
                    }

                    // Open file chooser for user to save the PDF
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Payslip PDF");
                    
                    String employeeName = payslip.getEmployeeID().getLastName().toUpperCase() + "_" 
                            + payslip.getEmployeeID().getFirstName().toUpperCase();
                    String defaultFileName = "Payslip_" + payslip.getPayslipNumber() + "_" + employeeName + ".pdf";
                    fileChooser.setInitialFileName(defaultFileName);
                    
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                    fileChooser.getExtensionFilters().add(extFilter);

                    Stage stage = (Stage) btnSavePayslip.getScene().getWindow();
                    java.io.File selectedFile = fileChooser.showSaveDialog(stage);

                    if (selectedFile != null) {
                        try {
                            // Copy temp file to selected location
                            java.nio.file.Files.copy(
                                    tempPdfFile.toPath(),
                                    selectedFile.toPath(),
                                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                            );

                            CustomAlert successAlert = new CustomAlert(
                                    Alert.AlertType.INFORMATION,
                                    "PDF Generated",
                                    "Payslip PDF has been saved successfully:\n" + selectedFile.getAbsolutePath()
                            );
                            successAlert.showAndWait();

                            // Optionally open the PDF file
                            if (java.awt.Desktop.isDesktopSupported()) {
                                try {
                                    java.awt.Desktop.getDesktop().open(selectedFile);
                                } catch (Exception e) {
                                    // Ignore if can't open - user can manually open the file
                                }
                            }
                        } catch (java.io.IOException e) {
                            CustomAlert errorAlert = new CustomAlert(
                                    Alert.AlertType.ERROR,
                                    "Save Failed",
                                    "Failed to save PDF file: " + e.getMessage()
                            );
                            errorAlert.showAndWait();
                        }
                    }

                    // Clean up temp file
                    if (tempPdfFile != null && tempPdfFile.exists()) {
                        try {
                            java.nio.file.Files.delete(tempPdfFile.toPath());
                        } catch (Exception ignored) {
                            // Ignore deletion errors
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Ensure loading dialog is closed
                    try {
                        loadingDialog.close();
                    } catch (Exception ignored) {
                    }
                    
                    CustomAlert errorAlert = new CustomAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "An unexpected error occurred: " + (e.getMessage() != null ? e.getMessage() : "Unknown error")
                    );
                    errorAlert.showAndWait();
                }
            });
        });
    }

    @FXML
    void closeClicked() {
        if (btnClose != null && btnClose.getScene() != null) {
            Stage stage = (Stage) btnClose.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    private String toPdfSafePeso(String pesoFormattedString) {
        if (pesoFormattedString == null) {
            return "";
        }
        return pesoFormattedString.replace("₱", "PHP ");
    }

    // Inner classes for table rows
    @Getter
    @Setter
    public static class EarningsRow {
        private String description;
        private String rate;
        private String amount;

        public EarningsRow(String description, String rate, String amount) {
            this.description = description;
            this.rate = rate;
            this.amount = amount;
        }
    }

    @Getter
    @Setter
    public static class BenefitsRow {
        private String description;
        private String amount;

        public BenefitsRow(String description, String amount) {
            this.description = description;
            this.amount = amount;
        }
    }

    @Getter
    @Setter
    public static class DeductionsRow {
        private String description;
        private String amount;

        public DeductionsRow(String description, String amount) {
            this.description = description;
            this.amount = amount;
        }
    }

    @Getter
    @Setter
    private static class WithholdingTaxBreakdown {
        private String bracket;
        private String calculation;
        private BigDecimal tax;

        public WithholdingTaxBreakdown(String bracket, String calculation, BigDecimal tax) {
            this.bracket = bracket;
            this.calculation = calculation;
            this.tax = tax;
        }
    }
}

