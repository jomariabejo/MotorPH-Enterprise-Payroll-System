package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollApproval;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.Instant;
<<<<<<< HEAD
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
=======
import java.util.List;
import java.util.Optional;
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

@Getter
@Setter
public class PayrollApprovalController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<PayrollApproval> tvPayrollApprovals;

    @FXML
    private Pagination paginationApprovals;

<<<<<<< HEAD
    @FXML
    private TextField tfSearch;

    @FXML
    private ComboBox<String> cbStatusFilter;

    private List<PayrollApproval> allApprovals;
    private List<PayrollApproval> filteredApprovals;
=======
    private List<PayrollApproval> allApprovals;
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

    public PayrollApprovalController() {
    }

    @FXML
    void initialize() {
        setupTableView();
<<<<<<< HEAD
        setupFilters();
    }

    private void setupFilters() {
        // Setup status filter
        if (cbStatusFilter != null) {
            cbStatusFilter.getItems().addAll("All", "Pending", "Approved", "Rejected");
            cbStatusFilter.getSelectionModel().selectFirst();
            cbStatusFilter.setOnAction(e -> filterApprovals());
        }

        // Setup search field
        if (tfSearch != null) {
            tfSearch.textProperty().addListener((observable, oldValue, newValue) -> filterApprovals());
        }
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<PayrollApproval, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<PayrollApproval, String> payrollColumn = new TableColumn<>("Payroll ID");
        payrollColumn.setCellValueFactory(cellData -> {
            Payroll payroll = cellData.getValue().getPayrollID();
            return new javafx.beans.property.SimpleStringProperty(
                    payroll != null ? String.valueOf(payroll.getId()) : "");
        });
        payrollColumn.setPrefWidth(100);

        TableColumn<PayrollApproval, String> approverColumn = new TableColumn<>("Approver ID");
        approverColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getApproverID())));
        approverColumn.setPrefWidth(120);

        TableColumn<PayrollApproval, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<PayrollApproval, String> approvalDateColumn = new TableColumn<>("Approval Date");
<<<<<<< HEAD
        approvalDateColumn.setCellValueFactory(cellData -> {
            Instant approvalDate = cellData.getValue().getApprovalDate();
            if (approvalDate != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
                        .withZone(ZoneId.systemDefault());
                return new javafx.beans.property.SimpleStringProperty(formatter.format(approvalDate));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
=======
        approvalDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getApprovalDate() != null 
                                ? cellData.getValue().getApprovalDate().toString() : ""));
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        approvalDateColumn.setPrefWidth(200);

        TableColumn<PayrollApproval, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(250);

        tvPayrollApprovals.getColumns().addAll(idColumn, payrollColumn, approverColumn, statusColumn, approvalDateColumn, actionsColumn);
        tvPayrollApprovals.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<PayrollApproval, Void> createActionsColumn() {
        TableColumn<PayrollApproval, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

                approveButton.setOnAction(event -> {
                    PayrollApproval selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Pending".equals(selected.getStatus())) {
                        approvePayroll(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    PayrollApproval selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Pending".equals(selected.getStatus())) {
                        rejectPayroll(selected);
                    }
                });

                viewButton.setOnAction(event -> {
                    PayrollApproval selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        viewApprovalDetails(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox(viewButton, approveButton, rejectButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(5);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    PayrollApproval approval = getTableView().getItems().get(getIndex());
                    if (approval != null && "Pending".equals(approval.getStatus())) {
                        approveButton.setVisible(true);
                        rejectButton.setVisible(true);
                    } else {
                        approveButton.setVisible(false);
                        rejectButton.setVisible(false);
                    }
                    setGraphic(actionsBox);
                }
            }
        });
        return actionsColumn;
    }

    private void approvePayroll(PayrollApproval approval) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Approve Payroll",
                "Are you sure you want to approve this payroll?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            approval.setStatus("Approved");
            approval.setApprovalDate(Instant.now());
            approval.setApproverID(getCurrentUserId());

            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPayrollApprovalService().updatePayrollApproval(approval);

            // Also update payroll status
            Payroll payroll = approval.getPayrollID();
            if (payroll != null) {
                payroll.setStatus("Approved");
                payrollAdministratorNavigationController.getMainViewController()
                        .getServiceFactory().getPayrollService().updatePayroll(payroll);
            }

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Payroll Approved",
                    "Payroll has been approved successfully."
            );
            successAlert.showAndWait();
            populateApprovals();
        }
    }

    private void rejectPayroll(PayrollApproval approval) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reject Payroll",
                "Are you sure you want to reject this payroll?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            approval.setStatus("Rejected");
            approval.setApprovalDate(Instant.now());
            approval.setApproverID(getCurrentUserId());

            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPayrollApprovalService().updatePayrollApproval(approval);

            // Also update payroll status
            Payroll payroll = approval.getPayrollID();
            if (payroll != null) {
                payroll.setStatus("Rejected");
                payrollAdministratorNavigationController.getMainViewController()
                        .getServiceFactory().getPayrollService().updatePayroll(payroll);
            }

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Payroll Rejected",
                    "Payroll has been rejected."
            );
            successAlert.showAndWait();
            populateApprovals();
        }
    }

    private void viewApprovalDetails(PayrollApproval approval) {
        StringBuilder details = new StringBuilder();
<<<<<<< HEAD
        details.append("Approval ID: ").append(approval.getId()).append("\n\n");
        
        Payroll payroll = approval.getPayrollID();
        if (payroll != null) {
            details.append("Payroll Information:\n");
            details.append("  Payroll ID: ").append(payroll.getId()).append("\n");
            details.append("  Payroll Run Date: ").append(payroll.getPayrollRunDate()).append("\n");
            details.append("  Period: ").append(payroll.getPeriodStartDate())
                    .append(" to ").append(payroll.getPeriodEndDate()).append("\n");
            details.append("  Payroll Status: ").append(payroll.getStatus()).append("\n\n");
        }
        
        details.append("Approval Information:\n");
        details.append("  Approver ID: ").append(approval.getApproverID()).append("\n");
        details.append("  Status: ").append(approval.getStatus()).append("\n");
        
        if (approval.getApprovalDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm:ss")
                    .withZone(ZoneId.systemDefault());
            details.append("  Approval Date: ").append(formatter.format(approval.getApprovalDate()));
        }
=======
        details.append("Approval ID: ").append(approval.getId()).append("\n");
        Payroll payroll = approval.getPayrollID();
        if (payroll != null) {
            details.append("Payroll ID: ").append(payroll.getId()).append("\n");
            details.append("Payroll Period: ").append(payroll.getPeriodStartDate())
                    .append(" to ").append(payroll.getPeriodEndDate()).append("\n");
        }
        details.append("Approver ID: ").append(approval.getApproverID()).append("\n");
        details.append("Status: ").append(approval.getStatus()).append("\n");
        details.append("Approval Date: ").append(approval.getApprovalDate());
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Approval Details",
                details.toString()
        );
        alert.showAndWait();
    }

    private Integer getCurrentUserId() {
        return payrollAdministratorNavigationController.getMainViewController().getUser().getId();
    }

    public void populateApprovals() {
        allApprovals = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPayrollApprovalService().getAllPayrollApprovals();

<<<<<<< HEAD
        filterApprovals();
    }

    private void filterApprovals() {
        if (allApprovals == null) {
            return;
        }

        String searchText = tfSearch != null ? tfSearch.getText().toLowerCase() : "";
        String statusFilter = cbStatusFilter != null && cbStatusFilter.getSelectionModel().getSelectedItem() != null
                ? cbStatusFilter.getSelectionModel().getSelectedItem() : "All";

        filteredApprovals = allApprovals.stream()
                .filter(approval -> {
                    // Status filter
                    if (!"All".equals(statusFilter) && !statusFilter.equalsIgnoreCase(approval.getStatus())) {
                        return false;
                    }

                    // Search filter
                    if (!searchText.isEmpty()) {
                        boolean matches = false;
                        
                        // Search by approval ID
                        if (String.valueOf(approval.getId()).contains(searchText)) {
                            matches = true;
                        }
                        
                        // Search by payroll ID
                        if (approval.getPayrollID() != null) {
                            if (String.valueOf(approval.getPayrollID().getId()).contains(searchText)) {
                                matches = true;
                            }
                        }
                        
                        // Search by approver ID
                        if (String.valueOf(approval.getApproverID()).contains(searchText)) {
                            matches = true;
                        }
                        
                        // Search by status
                        if (approval.getStatus() != null && approval.getStatus().toLowerCase().contains(searchText)) {
                            matches = true;
                        }
                        
                        if (!matches) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) filteredApprovals.size() / itemsPerPage));
=======
        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allApprovals.size() / itemsPerPage));
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        paginationApprovals.setPageCount(pageCount);

        paginationApprovals.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
<<<<<<< HEAD
        if (filteredApprovals == null) {
            tvPayrollApprovals.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, filteredApprovals.size());
        List<PayrollApproval> pageData = filteredApprovals.subList(fromIndex, toIndex);
=======
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allApprovals.size());
        List<PayrollApproval> pageData = allApprovals.subList(fromIndex, toIndex);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        tvPayrollApprovals.setItems(FXCollections.observableList(pageData));
    }
}

