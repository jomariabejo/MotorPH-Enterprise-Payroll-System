package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
<<<<<<< HEAD
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollApproval;
=======
import com.jomariabejo.motorph.model.Payroll;
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.TimesheetDataGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
<<<<<<< HEAD
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Dialog;
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
<<<<<<< HEAD
import javafx.util.Pair;
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
<<<<<<< HEAD
import java.time.Instant;
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PayrollController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddPayroll;

    @FXML
    private Button btnGenerateTestTimesheets;

    @FXML
<<<<<<< HEAD
    private Button btnMarkAllEmployeesActive;

    @FXML
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    private Pagination paginationPayrolls;

    @FXML
    private TableView<Payroll> tvPayrolls;

    private List<Payroll> allPayrolls;

    public PayrollController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddPayrollButton();
        customizeGenerateTestTimesheetsButton();
<<<<<<< HEAD
        customizeMarkAllEmployeesActiveButton();
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    }

    @FXML
    void addPayrollClicked() {
        openPayrollForm(null);
    }

    private void openPayrollForm(Payroll payroll) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/payroll-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(payroll == null ? "Add New Payroll" : "Edit Payroll");
            formStage.setScene(new Scene(formPane));

            PayrollFormController formController = loader.getController();
            formController.setPayrollController(this);
            formController.setPayroll(payroll);
            formController.setup();

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<Payroll, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Payroll, String> runDateColumn = new TableColumn<>("Run Date");
        runDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getPayrollRunDate() != null 
                                ? cellData.getValue().getPayrollRunDate().toString() : ""));
        runDateColumn.setPrefWidth(120);

        TableColumn<Payroll, String> periodStartColumn = new TableColumn<>("Period Start");
        periodStartColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getPeriodStartDate() != null 
                                ? cellData.getValue().getPeriodStartDate().toString() : ""));
        periodStartColumn.setPrefWidth(120);

        TableColumn<Payroll, String> periodEndColumn = new TableColumn<>("Period End");
        periodEndColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getPeriodEndDate() != null 
                                ? cellData.getValue().getPeriodEndDate().toString() : ""));
        periodEndColumn.setPrefWidth(120);

        TableColumn<Payroll, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<Payroll, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(cellData -> {
            String createdBy = cellData.getValue().getCreatedBy();
            return new javafx.beans.property.SimpleStringProperty(createdBy != null ? createdBy : "");
        });
        createdByColumn.setPrefWidth(150);

        TableColumn<Payroll, Void> actionsColumn = createActionsColumn();
<<<<<<< HEAD
        actionsColumn.setPrefWidth(250);
=======
        actionsColumn.setPrefWidth(200);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

        tvPayrolls.getColumns().addAll(idColumn, runDateColumn, periodStartColumn, periodEndColumn, statusColumn, createdByColumn, actionsColumn);
        tvPayrolls.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Payroll, Void> createActionsColumn() {
        TableColumn<Payroll, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));
<<<<<<< HEAD
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
<<<<<<< HEAD
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

                editButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openPayrollForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deletePayroll(selected);
                    }
                });

                viewButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        viewPayrollDetails(selected);
                    }
                });
<<<<<<< HEAD

                approveButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        approvePayroll(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        rejectPayroll(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox();
=======
            }

            private final HBox actionsBox = new HBox(viewButton, editButton, deleteButton);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(5);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
<<<<<<< HEAD
                if (empty) {
                    setGraphic(null);
                } else {
                    Payroll payroll = getTableView().getItems().get(getIndex());
                    actionsBox.getChildren().clear();
                    
                    // Always show view button
                    actionsBox.getChildren().add(viewButton);
                    
                    // Show approve/reject buttons only for pending payrolls
                    if (payroll != null && "Pending".equals(payroll.getStatus())) {
                        actionsBox.getChildren().add(approveButton);
                        actionsBox.getChildren().add(rejectButton);
                        editButton.setVisible(false);
                        deleteButton.setVisible(false);
                    } else {
                        // Show edit/delete for non-pending payrolls
                        actionsBox.getChildren().add(editButton);
                        actionsBox.getChildren().add(deleteButton);
                        approveButton.setVisible(false);
                        rejectButton.setVisible(false);
                    }
                    
                    setGraphic(actionsBox);
                }
=======
                setGraphic(empty ? null : actionsBox);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
            }
        });
        return actionsColumn;
    }

    private void deletePayroll(Payroll payroll) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Payroll",
                "Are you sure you want to delete this payroll record? This action cannot be undone."
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPayrollService().deletePayroll(payroll);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Payroll Deleted",
                    "Payroll record has been deleted."
            );
            successAlert.showAndWait();
            populatePayrolls();
        }
    }

    private void viewPayrollDetails(Payroll payroll) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/payroll-employee-list-view.fxml"));
            AnchorPane dialogPane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Payroll Employees - Payroll #" + payroll.getId());
            dialogStage.setScene(new Scene(dialogPane));

            PayrollEmployeeListViewDialog dialogController = loader.getController();
            dialogController.setup(payroll, payrollAdministratorNavigationController);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to open employee list view: " + e.getMessage()
            );
            alert.showAndWait();
        }
    }

    private void customizeAddPayrollButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddPayroll.setGraphic(fontIcon);
        btnAddPayroll.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void customizeGenerateTestTimesheetsButton() {
        FontIcon fontIcon = new FontIcon(Feather.CLOCK);
        btnGenerateTestTimesheets.setGraphic(fontIcon);
        btnGenerateTestTimesheets.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

<<<<<<< HEAD
    private void customizeMarkAllEmployeesActiveButton() {
        FontIcon fontIcon = new FontIcon(Feather.USER_CHECK);
        btnMarkAllEmployeesActive.setGraphic(fontIcon);
        btnMarkAllEmployeesActive.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void approvePayroll(Payroll payroll) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Approve Payroll",
                "Are you sure you want to approve payroll #" + payroll.getId() + "?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Update or create approval record
                var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
                var approvalService = serviceFactory.getPayrollApprovalService();
                
                Optional<PayrollApproval> existingApproval = approvalService.getApprovalByPayroll(payroll);
                PayrollApproval approval;
                
                if (existingApproval.isPresent()) {
                    approval = existingApproval.get();
                } else {
                    Integer approverId = payrollAdministratorNavigationController.getMainViewController().getUser().getId();
                    approval = approvalService.createApprovalRequest(payroll, approverId);
                }
                
                approval.setStatus("Approved");
                approval.setApprovalDate(Instant.now());
                approval.setApproverID(payrollAdministratorNavigationController.getMainViewController().getUser().getId());
                approvalService.updatePayrollApproval(approval);

                // Update payroll status
                payroll.setStatus("Approved");
                serviceFactory.getPayrollService().updatePayroll(payroll);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Payroll Approved",
                        "Payroll #" + payroll.getId() + " has been approved successfully."
                );
                successAlert.showAndWait();
                populatePayrolls();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to approve payroll: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void rejectPayroll(Payroll payroll) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reject Payroll",
                "Are you sure you want to reject payroll #" + payroll.getId() + "?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Update or create approval record
                var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
                var approvalService = serviceFactory.getPayrollApprovalService();
                
                Optional<PayrollApproval> existingApproval = approvalService.getApprovalByPayroll(payroll);
                PayrollApproval approval;
                
                if (existingApproval.isPresent()) {
                    approval = existingApproval.get();
                } else {
                    Integer approverId = payrollAdministratorNavigationController.getMainViewController().getUser().getId();
                    approval = approvalService.createApprovalRequest(payroll, approverId);
                }
                
                approval.setStatus("Rejected");
                approval.setApprovalDate(Instant.now());
                approval.setApproverID(payrollAdministratorNavigationController.getMainViewController().getUser().getId());
                approvalService.updatePayrollApproval(approval);

                // Update payroll status
                payroll.setStatus("Rejected");
                serviceFactory.getPayrollService().updatePayroll(payroll);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Payroll Rejected",
                        "Payroll #" + payroll.getId() + " has been rejected."
                );
                successAlert.showAndWait();
                populatePayrolls();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to reject payroll: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void generateTestTimesheetsClicked() {
        // Create a dialog to select month and year
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Generate Timesheets");
        dialog.setHeaderText("Select Month and Year for Timesheet Generation");

        // Create month and year selection
        ComboBox<Integer> monthCombo = new ComboBox<>();
        monthCombo.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        monthCombo.setValue(12); // Default to December
        monthCombo.setPrefWidth(150);

        ComboBox<Integer> yearCombo = new ComboBox<>();
        int currentYear = java.time.Year.now().getValue();
        for (int year = currentYear - 1; year <= currentYear + 2; year++) {
            yearCombo.getItems().add(year);
        }
        yearCombo.setValue(2025); // Default to 2025
        yearCombo.setPrefWidth(150);

        HBox content = new HBox(10);
        content.getChildren().addAll(
                new Label("Month:"), monthCombo,
                new Label("Year:"), yearCombo
        );
        content.setAlignment(javafx.geometry.Pos.CENTER);
        content.setPadding(new javafx.geometry.Insets(20));

        dialog.getDialogPane().setContent(content);

        ButtonType generateButtonType = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                return new Pair<>(monthCombo.getValue(), yearCombo.getValue());
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        if (result.isPresent()) {
            int month = result.get().getKey();
            int year = result.get().getValue();
            
            String monthName = java.time.Month.of(month).name();
            CustomAlert confirmAlert = new CustomAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Generate Test Timesheets",
                    String.format("This will generate timesheet entries for all active employees for %s %d.\n\n" +
                            "Details:\n" +
                            "- Weekdays only (Monday-Friday)\n" +
                            "- Time In: 8:00 AM\n" +
                            "- Time Out: 5:00 PM\n" +
                            "- Status: Approved\n\n" +
                            "Existing timesheets for the same dates will be skipped.\n\n" +
                            "Continue?", monthName, year)
            );
            Optional<ButtonType> confirmResult = confirmAlert.showAndWait();

            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                try {
                    var serviceFactory = payrollAdministratorNavigationController
                            .getMainViewController()
                            .getServiceFactory();

                    int count = TimesheetDataGenerator.generateTimesheetsForMonth(year, month, serviceFactory);

                    if (count > 0) {
                        CustomAlert successAlert = new CustomAlert(
                                Alert.AlertType.INFORMATION,
                                "Success",
                                String.format("Successfully generated %d timesheet entries for %s %d.", count, monthName, year)
                        );
                        successAlert.showAndWait();
                    } else {
                        CustomAlert infoAlert = new CustomAlert(
                                Alert.AlertType.INFORMATION,
                                "No Timesheets Generated",
                                String.format("No timesheet entries were generated for %s %d.\n\n" +
                                        "Possible reasons:\n" +
                                        "- No active employees found\n" +
                                        "- All timesheets for this period already exist", monthName, year)
                        );
                        infoAlert.showAndWait();
                    }
                } catch (Exception e) {
                    CustomAlert errorAlert = new CustomAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to generate timesheets: " + e.getMessage() + 
                            "\n\nPlease check the console for more details."
                    );
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void markAllEmployeesActiveClicked() {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Mark All Employees as Active",
                "This will:\n" +
                "1. Set isDeleted = false for all employees (marking them as active)\n" +
                "2. Set status to 'Regular' or 'Probationary' (if not already set)\n\n" +
                "This will allow timesheet generation to work for all employees.\n\n" +
=======
    @FXML
    void generateTestTimesheetsClicked() {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Generate Test Timesheets",
                "This will generate timesheet entries for all active employees for December 2025.\n\n" +
                "Details:\n" +
                "- Weekdays only (Monday-Friday)\n" +
                "- Time In: 8:00 AM\n" +
                "- Time Out: 5:00 PM\n" +
                "- Status: Approved\n\n" +
                "Existing timesheets for the same dates will be skipped.\n\n" +
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
                "Continue?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var serviceFactory = payrollAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory();

<<<<<<< HEAD
                List<Employee> allEmployees = serviceFactory.getEmployeeService().getAllEmployees();
                int updatedCount = 0;
                int statusUpdatedCount = 0;

                for (Employee employee : allEmployees) {
                    boolean needsUpdate = false;
                    
                    // Set isDeleted to false (active)
                    if (employee.getIsDeleted() == null || employee.getIsDeleted()) {
                        employee.setIsDeleted(false);
                        needsUpdate = true;
                    }
                    
                    // Ensure status is Regular or Probationary
                    String currentStatus = employee.getStatus();
                    if (currentStatus == null || 
                        (!currentStatus.equalsIgnoreCase("Regular") && 
                         !currentStatus.equalsIgnoreCase("Probationary"))) {
                        // Default to Regular if status is invalid
                        employee.setStatus("Regular");
                        statusUpdatedCount++;
                        needsUpdate = true;
                    }
                    
                    if (needsUpdate) {
                        serviceFactory.getEmployeeService().updateEmployee(employee);
                        updatedCount++;
                    }
                }
=======
                int count = TimesheetDataGenerator.generateDecember2025Timesheets(serviceFactory);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
<<<<<<< HEAD
                        String.format("Successfully updated %d employee(s).\n\n" +
                                "Total employees: %d\n" +
                                "Marked as active (isDeleted=false): %d\n" +
                                "Status updated to Regular/Probationary: %d\n" +
                                "Already active: %d", 
                                updatedCount, allEmployees.size(), updatedCount, 
                                statusUpdatedCount, allEmployees.size() - updatedCount)
=======
                        String.format("Successfully generated %d timesheet entries for December 2025.", count)
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
                );
                successAlert.showAndWait();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
<<<<<<< HEAD
                        "Failed to mark employees as active: " + e.getMessage() + 
                        "\n\nPlease check the console for more details."
=======
                        "Failed to generate timesheets: " + e.getMessage()
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    public void populatePayrolls() {
        allPayrolls = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPayrollService().getAllPayrolls();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allPayrolls.size() / itemsPerPage));
        paginationPayrolls.setPageCount(pageCount);

        paginationPayrolls.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allPayrolls.size());
        List<Payroll> pageData = allPayrolls.subList(fromIndex, toIndex);
        tvPayrolls.setItems(FXCollections.observableList(pageData));
    }
}

