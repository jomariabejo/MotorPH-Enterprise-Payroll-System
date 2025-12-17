package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollTransaction;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.service.PayrollService;
import com.jomariabejo.motorph.service.PayrollTransactionService;
import com.jomariabejo.motorph.service.PayslipService;
import com.jomariabejo.motorph.service.ServiceFactory;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.PesoUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PayrollTransactionController {

    private static final int PAGE_SIZE = 20;

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;
    private ServiceFactory serviceFactory;
    private PayrollTransactionService transactionService;
    private PayrollService payrollService;
    private PayslipService payslipService;

    private ObservableList<PayrollTransaction> allTransactions;
    private ObservableList<PayrollTransaction> filteredTransactions;

    @FXML
    private ComboBox<Payroll> cbPayroll;
    @FXML
    private ComboBox<Payslip> cbPayslip;
    @FXML
    private ComboBox<String> cbTransactionType;
    @FXML
    private TextField tfSearch;
    @FXML
    private DatePicker dpFromDate;
    @FXML
    private DatePicker dpToDate;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnClearFilters;
    @FXML
    private Button btnAddTransaction;
    @FXML
    private Label lblTotalTransactions;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private TableView<PayrollTransaction> tvTransactions;
    @FXML
    private Pagination paginationTransactions;

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
        setupPagination();
        customizeButtons();
        
        // Load transactions if services are already available
        if (payrollAdministratorNavigationController != null) {
            initializeServices();
            loadTransactions();
        }
    }

    public void setPayrollAdministratorNavigationController(PayrollAdministratorNavigationController controller) {
        this.payrollAdministratorNavigationController = controller;
        initializeServices();
        // Reload transactions after services are initialized
        loadTransactions();
    }

    private void initializeServices() {
        if (payrollAdministratorNavigationController != null && payrollAdministratorNavigationController.getMainViewController() != null) {
            serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
            transactionService = serviceFactory.getPayrollTransactionService();
            payrollService = serviceFactory.getPayrollService();
            payslipService = serviceFactory.getPayslipService();
            
            // Refresh filters after services are initialized
            if (transactionService != null && payrollService != null) {
                setupFilters();
            }
        }
    }

    private void customizeButtons() {
        FontIcon addIcon = new FontIcon(Feather.PLUS);
        btnAddTransaction.setGraphic(addIcon);
        btnAddTransaction.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

        FontIcon filterIcon = new FontIcon(Feather.FILTER);
        btnFilter.setGraphic(filterIcon);
        btnFilter.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

        FontIcon clearIcon = new FontIcon(Feather.X);
        btnClearFilters.setGraphic(clearIcon);
        btnClearFilters.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void setupFilters() {
        // Setup transaction types
        if (transactionService != null) {
            List<String> types = transactionService.getTransactionTypes();
            cbTransactionType.setItems(FXCollections.observableArrayList(types));
            cbTransactionType.getSelectionModel().selectFirst();
        } else {
            // Default types if service not available yet
            List<String> defaultTypes = List.of("Deduction", "Bonus", "Adjustment", "Payment", "Refund", "Other");
            cbTransactionType.setItems(FXCollections.observableArrayList(defaultTypes));
            cbTransactionType.getSelectionModel().selectFirst();
        }

        // Setup payroll combo box
        if (payrollService != null) {
            List<Payroll> payrolls = payrollService.getAllPayrolls();
            cbPayroll.setItems(FXCollections.observableArrayList(payrolls));
        }

        // Setup payslip combo box (will be filtered by selected payroll)
        cbPayslip.setItems(FXCollections.observableArrayList());

        // Listen to payroll selection to filter payslips
        cbPayroll.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updatePayslipComboBox(newVal));
    }

    private void updatePayslipComboBox(Payroll payroll) {
        if (payroll == null || payslipService == null) {
            cbPayslip.setItems(FXCollections.observableArrayList());
            return;
        }

        java.util.Optional<java.util.List<Payslip>> payslipsOpt = payslipService.getPayslipsByPayroll(payroll);
        List<Payslip> payslips = payslipsOpt.orElse(java.util.Collections.emptyList());
        cbPayslip.setItems(FXCollections.observableArrayList(payslips));
    }

    private void setupTableView() {
        tvTransactions.getColumns().clear();

        // Transaction ID Column
        TableColumn<PayrollTransaction, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(60);

        // Payroll ID Column
        TableColumn<PayrollTransaction, Integer> payrollIdColumn = new TableColumn<>("Payroll ID");
        payrollIdColumn.setCellValueFactory(cellData -> {
            Payroll payroll = cellData.getValue().getPayrollID();
            return new javafx.beans.property.SimpleObjectProperty<>(
                    payroll != null ? payroll.getId() : null);
        });
        payrollIdColumn.setPrefWidth(100);

        // Payslip Number Column
        TableColumn<PayrollTransaction, String> payslipColumn = new TableColumn<>("Payslip Number");
        payslipColumn.setCellValueFactory(cellData -> {
            Payslip payslip = cellData.getValue().getPayslipID();
            return new javafx.beans.property.SimpleStringProperty(
                    payslip != null ? payslip.getPayslipNumber() : "");
        });
        payslipColumn.setPrefWidth(150);

        // Employee Name Column
        TableColumn<PayrollTransaction, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            Payslip payslip = cellData.getValue().getPayslipID();
            if (payslip != null && payslip.getEmployeeID() != null) {
                String name = payslip.getEmployeeID().getFirstName() + " " + 
                             payslip.getEmployeeID().getLastName();
                return new javafx.beans.property.SimpleStringProperty(name);
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        employeeColumn.setPrefWidth(200);

        // Transaction Type Column
        TableColumn<PayrollTransaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTransactionType()));
        typeColumn.setPrefWidth(120);

        // Amount Column
        TableColumn<PayrollTransaction, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> {
            BigDecimal amount = cellData.getValue().getTransactionAmount();
            return new javafx.beans.property.SimpleStringProperty(
                    PesoUtility.formatToPeso(String.valueOf(amount)));
        });
        amountColumn.setPrefWidth(120);

        // Transaction Date Column
        TableColumn<PayrollTransaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getTransactionDate();
            return new javafx.beans.property.SimpleStringProperty(
                    date != null ? date.toString() : "");
        });
        dateColumn.setPrefWidth(120);

        // Description Column
        TableColumn<PayrollTransaction, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            if (desc != null && desc.length() > 50) {
                desc = desc.substring(0, 47) + "...";
            }
            return new javafx.beans.property.SimpleStringProperty(desc != null ? desc : "");
        });
        descriptionColumn.setPrefWidth(250);

        // Actions Column
        TableColumn<PayrollTransaction, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(150);

        tvTransactions.getColumns().addAll(idColumn, payrollIdColumn, payslipColumn, employeeColumn,
                typeColumn, amountColumn, dateColumn, descriptionColumn, actionsColumn);
    }

    private TableColumn<PayrollTransaction, Void> createActionsColumn() {
        TableColumn<PayrollTransaction, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                viewButton.setOnAction(event -> {
                    PayrollTransaction transaction = getTableView().getItems().get(getIndex());
                    if (transaction != null) {
                        viewTransaction(transaction);
                    }
                });

                editButton.setOnAction(event -> {
                    PayrollTransaction transaction = getTableView().getItems().get(getIndex());
                    if (transaction != null) {
                        editTransaction(transaction);
                    }
                });

                deleteButton.setOnAction(event -> {
                    PayrollTransaction transaction = getTableView().getItems().get(getIndex());
                    if (transaction != null) {
                        deleteTransaction(transaction);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, viewButton, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
        return actionsColumn;
    }

    private void setupPagination() {
        paginationTransactions.setPageCount(1);
        paginationTransactions.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (filteredTransactions != null) {
                int page = newIndex.intValue();
                int fromIndex = page * PAGE_SIZE;
                int toIndex = Math.min(fromIndex + PAGE_SIZE, filteredTransactions.size());
                
                if (fromIndex < filteredTransactions.size()) {
                    ObservableList<PayrollTransaction> pageData = FXCollections.observableArrayList(
                            filteredTransactions.subList(fromIndex, toIndex));
                    tvTransactions.setItems(pageData);
                } else {
                    tvTransactions.setItems(FXCollections.observableArrayList());
                }
            }
        });
    }

    private void loadTransactions() {
        if (transactionService == null) {
            return;
        }

        allTransactions = FXCollections.observableArrayList(transactionService.getAllPayrollTransactions());
        filteredTransactions = FXCollections.observableArrayList(allTransactions);
        
        updateTable();
        updateSummary();
    }

    private void updateTable() {
        if (filteredTransactions == null) {
            return;
        }

        int totalPages = (int) Math.ceil((double) filteredTransactions.size() / PAGE_SIZE);
        paginationTransactions.setPageCount(Math.max(1, totalPages));

        // Load first page
        int fromIndex = 0;
        int toIndex = Math.min(PAGE_SIZE, filteredTransactions.size());
        
        if (fromIndex < filteredTransactions.size()) {
            ObservableList<PayrollTransaction> pageData = FXCollections.observableArrayList(
                    filteredTransactions.subList(fromIndex, toIndex));
            tvTransactions.setItems(pageData);
        } else {
            tvTransactions.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateSummary() {
        if (filteredTransactions == null) {
            lblTotalTransactions.setText("0");
            lblTotalAmount.setText("PHP 0.00");
            return;
        }

        int count = filteredTransactions.size();
        BigDecimal total = filteredTransactions.stream()
                .map(PayrollTransaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        lblTotalTransactions.setText(String.valueOf(count));
        lblTotalAmount.setText(PesoUtility.formatToPeso(String.valueOf(total)));
    }

    @FXML
    void filterClicked() {
        if (transactionService == null) {
            return;
        }

        Payroll selectedPayroll = cbPayroll.getSelectionModel().getSelectedItem();
        Payslip selectedPayslip = cbPayslip.getSelectionModel().getSelectedItem();
        String selectedType = cbTransactionType.getSelectionModel().getSelectedItem();
        LocalDate fromDate = dpFromDate.getValue();
        LocalDate toDate = dpToDate.getValue();
        String searchTerm = tfSearch.getText();

        // Apply filters
        List<PayrollTransaction> filtered = transactionService.filterTransactions(
                selectedPayroll, selectedPayslip, selectedType, fromDate, toDate, searchTerm);

        filteredTransactions = FXCollections.observableArrayList(filtered);
        updateTable();
        updateSummary();
    }

    @FXML
    void clearFiltersClicked() {
        cbPayroll.getSelectionModel().clearSelection();
        cbPayslip.getSelectionModel().clearSelection();
        cbTransactionType.getSelectionModel().selectFirst();
        dpFromDate.setValue(null);
        dpToDate.setValue(null);
        tfSearch.clear();

        filteredTransactions = FXCollections.observableArrayList(allTransactions);
        updateTable();
        updateSummary();
    }

    @FXML
    void addTransactionClicked() {
        openTransactionForm(null);
    }

    private void viewTransaction(PayrollTransaction transaction) {
        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Transaction Details",
                String.format(
                        "Transaction ID: %d\n" +
                        "Payroll ID: %d\n" +
                        "Payslip Number: %s\n" +
                        "Employee: %s\n" +
                        "Type: %s\n" +
                        "Amount: %s\n" +
                        "Date: %s\n" +
                        "Description: %s",
                        transaction.getId(),
                        transaction.getPayrollID() != null ? transaction.getPayrollID().getId() : "N/A",
                        transaction.getPayslipID() != null ? transaction.getPayslipID().getPayslipNumber() : "N/A",
                        transaction.getPayslipID() != null && transaction.getPayslipID().getEmployeeID() != null ?
                                transaction.getPayslipID().getEmployeeID().getFirstName() + " " +
                                transaction.getPayslipID().getEmployeeID().getLastName() : "N/A",
                        transaction.getTransactionType(),
                        PesoUtility.formatToPeso(String.valueOf(transaction.getTransactionAmount())),
                        transaction.getTransactionDate(),
                        transaction.getDescription() != null ? transaction.getDescription() : "N/A"
                )
        );
        alert.showAndWait();
    }

    private void editTransaction(PayrollTransaction transaction) {
        openTransactionForm(transaction);
    }

    private void deleteTransaction(PayrollTransaction transaction) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Transaction",
                "Are you sure you want to delete this transaction?\n\n" +
                "Transaction ID: " + transaction.getId() + "\n" +
                "Type: " + transaction.getTransactionType() + "\n" +
                "Amount: " + PesoUtility.formatToPeso(String.valueOf(transaction.getTransactionAmount()))
        );
        
        confirmAlert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK && transactionService != null) {
                try {
                    transactionService.deletePayrollTransaction(transaction);
                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Transaction deleted successfully."
                    );
                    successAlert.showAndWait();
                    refreshTable();
                } catch (Exception e) {
                    CustomAlert errorAlert = new CustomAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to delete transaction: " + e.getMessage()
                    );
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void openTransactionForm(PayrollTransaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/payroll-transaction-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(transaction == null ? "Add New Transaction" : "Edit Transaction");
            formStage.setScene(new Scene(formPane));

            PayrollTransactionFormController formController = loader.getController();
            formController.setPayrollTransactionController(this);
            formController.setTransaction(transaction);
            formController.setServiceFactory(serviceFactory);
            formController.setup();

            formStage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to open transaction form: " + e.getMessage()
            );
            errorAlert.showAndWait();
        }
    }

    public void refreshTable() {
        loadTransactions();
    }
}
