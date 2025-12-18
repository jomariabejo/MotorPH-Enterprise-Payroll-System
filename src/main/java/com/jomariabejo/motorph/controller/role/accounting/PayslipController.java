package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.YearToDateFigures;
import com.jomariabejo.motorph.service.PayslipPdfService;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
public class PayslipController {

    // Use a fixed thread pool with limited threads to prevent resource exhaustion
    // Shutdown hook will clean up when application exits
    private static final ExecutorService executorService = Executors.newFixedThreadPool(2, r -> {
        Thread t = new Thread(r, "PayslipPDF-Generator");
        t.setDaemon(true); // Daemon threads won't prevent JVM shutdown
        return t;
    });
    
    static {
        // Register shutdown hook to properly close executor service
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));
    }
    
    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<Payslip> tvPayslips;

    @FXML
    private Pagination paginationPayslips;

    @FXML
    private TextField tfSearchEmployee;

    @FXML
    private ComboBox<String> cbStatusFilter;

    private List<Payslip> allPayslips;

    public PayslipController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
    }

    private void setupFilters() {
        // Setup status filter if needed
        if (cbStatusFilter != null) {
            cbStatusFilter.getItems().addAll("All", "Active", "Processed");
            cbStatusFilter.getSelectionModel().selectFirst();
            cbStatusFilter.setOnAction(e -> filterPayslips());
        }

        // Setup search field
        if (tfSearchEmployee != null) {
            tfSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> filterPayslips());
        }
    }

    private void filterPayslips() {
        if (allPayslips == null) {
            return;
        }

        String searchText = tfSearchEmployee != null ? tfSearchEmployee.getText().toLowerCase() : "";

        List<Payslip> filtered = allPayslips.stream()
                .filter(payslip -> {
                    // Search filter
                    if (!searchText.isEmpty()) {
                        Employee emp = payslip.getEmployeeID();
                        if (emp != null) {
                            String employeeName = (emp.getFirstName() + " " + emp.getLastName()).toLowerCase();
                            String employeeId = String.valueOf(emp.getEmployeeNumber());
                            if (!employeeName.contains(searchText) && !employeeId.contains(searchText)) {
                                return false;
                            }
                        }
                    }
                    return true;
                })
                .toList();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) filtered.size() / itemsPerPage));
        paginationPayslips.setPageCount(pageCount);

        paginationPayslips.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage, filtered);
            return new StackPane();
        });
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<Payslip, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Payslip, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<Payslip, String> employeeIdColumn = new TableColumn<>("Employee ID");
        employeeIdColumn.setCellValueFactory(cellData -> {
            Employee emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? String.valueOf(emp.getEmployeeNumber()) : "");
        });
        employeeIdColumn.setPrefWidth(100);

        TableColumn<Payslip, String> payPeriodColumn = new TableColumn<>("Pay Period");
        payPeriodColumn.setCellValueFactory(cellData -> {
            Payslip payslip = cellData.getValue();
            if (payslip.getPeriodStartDate() != null && payslip.getPeriodEndDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                String start = sdf.format(payslip.getPeriodStartDate());
                String end = sdf.format(payslip.getPeriodEndDate());
                return new javafx.beans.property.SimpleStringProperty(start + " - " + end);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        payPeriodColumn.setPrefWidth(200);

        TableColumn<Payslip, String> grossIncomeColumn = new TableColumn<>("Gross Income");
        grossIncomeColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getGrossIncome() != null
                                ? "₱" + String.format("%,.2f", cellData.getValue().getGrossIncome()) : "₱0.00"));
        grossIncomeColumn.setPrefWidth(150);

        TableColumn<Payslip, String> netPayColumn = new TableColumn<>("Net Pay");
        netPayColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getNetPay() != null
                                ? "₱" + String.format("%,.2f", cellData.getValue().getNetPay()) : "₱0.00"));
        netPayColumn.setPrefWidth(150);

        TableColumn<Payslip, String> payrollDateColumn = new TableColumn<>("Payroll Date");
        payrollDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getPayrollRunDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(cellData.getValue().getPayrollRunDate()));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        payrollDateColumn.setPrefWidth(150);

        TableColumn<Payslip, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvPayslips.getColumns().addAll(idColumn, employeeColumn, employeeIdColumn, payPeriodColumn,
                grossIncomeColumn, netPayColumn, payrollDateColumn, actionsColumn);
        tvPayslips.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Payslip, Void> createActionsColumn() {
        TableColumn<Payslip, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));
            private final Button downloadButton = new Button(null, new FontIcon(Feather.DOWNLOAD));

            {
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                downloadButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

                viewButton.setOnAction(event -> {
                    Payslip selectedPayslip = getTableView().getItems().get(getIndex());
                    if (selectedPayslip != null) {
                        viewPayslip(selectedPayslip);
                    }
                });

                downloadButton.setOnAction(event -> {
                    Payslip selectedPayslip = getTableView().getItems().get(getIndex());
                    if (selectedPayslip != null) {
                        downloadPayslip(selectedPayslip);
                    }
                });
            }

            private final HBox actionsBox = new HBox(viewButton, downloadButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
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
                    java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO,
                    java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO
            ));

            // Load preview dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/payslip-preview-dialog.fxml"));
            AnchorPane dialogPane = loader.load();
            Stage dialogStage = new Stage();
            
            // Set parent stage and modality
            if (tvPayslips != null && tvPayslips.getScene() != null) {
                Stage parentStage = (Stage) tvPayslips.getScene().getWindow();
                if (parentStage != null) {
                    dialogStage.initOwner(parentStage);
                }
            }
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // Add close request handler
            dialogStage.setOnCloseRequest(event -> dialogStage.close());
            
            String employeeName = payslip.getEmployeeID().getFirstName() + " " + payslip.getEmployeeID().getLastName();
            dialogStage.setTitle("Payslip Preview - " + employeeName);
            dialogStage.setScene(new Scene(dialogPane));

            PayslipPreviewDialog previewController = loader.getController();
            previewController.setup(payslip, ytd, null); // No parent dialog for this view
            previewController.setPayrollAdministratorNavigationController(payrollAdministratorNavigationController);

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

    private void downloadPayslip(Payslip payslip) {
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

                    Stage stage = tvPayslips != null && tvPayslips.getScene() != null 
                            ? (Stage) tvPayslips.getScene().getWindow() 
                            : null;
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
                    
                    // Suggest garbage collection to free memory after PDF operations
                    System.gc();
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

    public void populatePayslips() {
        allPayslips = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPayslipService().getAllPayslips();

        filterPayslips();
    }

    private void updateTableView(int pageIndex, int itemsPerPage, List<Payslip> data) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        List<Payslip> pageData = data.subList(fromIndex, toIndex);
        tvPayslips.setItems(FXCollections.observableList(pageData));
    }
}
