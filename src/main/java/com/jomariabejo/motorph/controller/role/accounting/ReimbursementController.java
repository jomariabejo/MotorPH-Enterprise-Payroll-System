package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.PesoUtility;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReimbursementController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<ReimbursementRequest> tvReimbursementRequests;

    @FXML
    private Pagination paginationRequests;

    @FXML
    private TextField tfSearchEmployee;

    @FXML
    private ComboBox<String> cbStatusFilter;

    private List<ReimbursementRequest> allRequests;
    private List<ReimbursementRequest> filteredRequests;

    public ReimbursementController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
    }

    private void setupFilters() {
        // Setup status filter
        if (cbStatusFilter != null) {
            cbStatusFilter.getItems().addAll("All", "Pending", "Approved", "Rejected");
            cbStatusFilter.getSelectionModel().selectFirst();
            cbStatusFilter.setOnAction(e -> filterRequests());
        }

        // Setup search field
        if (tfSearchEmployee != null) {
            tfSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> filterRequests());
        }
    }

    private void filterRequests() {
        if (allRequests == null) {
            return;
        }

        String searchText = tfSearchEmployee != null ? tfSearchEmployee.getText().toLowerCase() : "";
        String statusFilter = cbStatusFilter != null && cbStatusFilter.getSelectionModel().getSelectedItem() != null
                ? cbStatusFilter.getSelectionModel().getSelectedItem() : "All";

        filteredRequests = allRequests.stream()
                .filter(request -> {
                    // Status filter
                    if (!"All".equals(statusFilter) && !statusFilter.equalsIgnoreCase(request.getStatus())) {
                        return false;
                    }

                    // Search filter
                    if (!searchText.isEmpty()) {
                        boolean matches = false;
                        
                        // Search by request ID
                        if (String.valueOf(request.getReimbursementRequestId()).contains(searchText)) {
                            matches = true;
                        }
                        
                        // Search by employee name or ID
                        Employee emp = request.getEmployee();
                        if (emp != null) {
                            if (emp.getFirstName() != null && emp.getFirstName().toLowerCase().contains(searchText)) {
                                matches = true;
                            }
                            if (emp.getLastName() != null && emp.getLastName().toLowerCase().contains(searchText)) {
                                matches = true;
                            }
                            if (String.valueOf(emp.getEmployeeNumber()).contains(searchText)) {
                                matches = true;
                            }
                        }
                        
                        // Search by status
                        if (request.getStatus() != null && request.getStatus().toLowerCase().contains(searchText)) {
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
        int pageCount = Math.max(1, (int) Math.ceil((double) filteredRequests.size() / itemsPerPage));
        paginationRequests.setPageCount(pageCount);

        paginationRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<ReimbursementRequest, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReimbursementRequestId()));
        idColumn.setPrefWidth(80);

        TableColumn<ReimbursementRequest, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            ReimbursementRequest request = cellData.getValue();
            Employee emp = request.getEmployee();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<ReimbursementRequest, String> employeeIdColumn = new TableColumn<>("Employee ID");
        employeeIdColumn.setCellValueFactory(cellData -> {
            ReimbursementRequest request = cellData.getValue();
            Employee emp = request.getEmployee();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? String.valueOf(emp.getEmployeeNumber()) : "");
        });
        employeeIdColumn.setPrefWidth(100);

        TableColumn<ReimbursementRequest, String> requestDateColumn = new TableColumn<>("Request Date");
        requestDateColumn.setCellValueFactory(cellData -> {
            Date requestDate = cellData.getValue().getRequestDate();
            if (requestDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(requestDate));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        requestDateColumn.setPrefWidth(150);

        TableColumn<ReimbursementRequest, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAmount() != null
                                ? PesoUtility.formatToPeso(String.valueOf(cellData.getValue().getAmount())) : "₱0.00"));
        amountColumn.setPrefWidth(150);

        TableColumn<ReimbursementRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<ReimbursementRequest, String> processedDateColumn = new TableColumn<>("Processed Date");
        processedDateColumn.setCellValueFactory(cellData -> {
            Date processedDate = cellData.getValue().getProcessedDate();
            if (processedDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(processedDate));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        processedDateColumn.setPrefWidth(150);

        TableColumn<ReimbursementRequest, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(250);

        tvReimbursementRequests.getColumns().addAll(idColumn, employeeColumn, employeeIdColumn, requestDateColumn,
                amountColumn, statusColumn, processedDateColumn, actionsColumn);
        tvReimbursementRequests.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<ReimbursementRequest, Void> createActionsColumn() {
        TableColumn<ReimbursementRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

                approveButton.setOnAction(event -> {
                    ReimbursementRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && ("Pending".equals(selected.getStatus()) || "Submitted".equals(selected.getStatus()))) {
                        approveRequest(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    ReimbursementRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && ("Pending".equals(selected.getStatus()) || "Submitted".equals(selected.getStatus()))) {
                        rejectRequest(selected);
                    }
                });

                viewButton.setOnAction(event -> {
                    ReimbursementRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        viewRequestDetails(selected);
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
                    ReimbursementRequest request = getTableView().getItems().get(getIndex());
                    // Show approve/reject buttons for both "Pending" and "Submitted" statuses
                    if (request != null && ("Pending".equals(request.getStatus()) || "Submitted".equals(request.getStatus()))) {
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

    private void approveRequest(ReimbursementRequest request) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Approve Reimbursement",
                "Are you sure you want to approve this reimbursement request?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                request.setStatus("Approved");
                request.setProcessedDate(new Date(System.currentTimeMillis()));
                
                // Set processed by to current user's employee
                Employee currentUserEmployee = payrollAdministratorNavigationController.getMainViewController().getEmployee();
                request.setProcessedBy(currentUserEmployee);

                payrollAdministratorNavigationController.getMainViewController()
                        .getServiceFactory().getReimbursementRequestService().updateRequest(request);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Reimbursement Approved",
                        "Reimbursement request has been approved successfully."
                );
                successAlert.showAndWait();
                populateRequests();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to approve reimbursement: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void rejectRequest(ReimbursementRequest request) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reject Reimbursement",
                "Are you sure you want to reject this reimbursement request?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                request.setStatus("Rejected");
                request.setProcessedDate(new Date(System.currentTimeMillis()));
                
                // Set processed by to current user's employee
                Employee currentUserEmployee = payrollAdministratorNavigationController.getMainViewController().getEmployee();
                request.setProcessedBy(currentUserEmployee);

                payrollAdministratorNavigationController.getMainViewController()
                        .getServiceFactory().getReimbursementRequestService().updateRequest(request);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Reimbursement Rejected",
                        "Reimbursement request has been rejected."
                );
                successAlert.showAndWait();
                populateRequests();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to reject reimbursement: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void viewRequestDetails(ReimbursementRequest request) {
        StringBuilder details = new StringBuilder();
        details.append("Request ID: ").append(request.getReimbursementRequestId()).append("\n\n");
        
        Employee emp = request.getEmployee();
        if (emp != null) {
            details.append("Employee Information:\n");
            details.append("  Name: ").append(emp.getFirstName()).append(" ").append(emp.getLastName()).append("\n");
            details.append("  Employee ID: ").append(emp.getEmployeeNumber()).append("\n\n");
        }
        
        details.append("Request Information:\n");
        if (request.getRequestDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            details.append("  Request Date: ").append(sdf.format(request.getRequestDate())).append("\n");
        }
        details.append("  Amount: ").append(PesoUtility.formatToPeso(String.valueOf(request.getAmount()))).append("\n");
        details.append("  Status: ").append(request.getStatus()).append("\n");
        
        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            details.append("  Description: ").append(request.getDescription()).append("\n");
        }
        
        if (request.getProcessedDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            details.append("  Processed Date: ").append(sdf.format(request.getProcessedDate())).append("\n");
        }
        
        if (request.getProcessedBy() != null) {
            Employee processor = request.getProcessedBy();
            details.append("  Processed By: ").append(processor.getFirstName()).append(" ").append(processor.getLastName());
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Reimbursement Request Details",
                details.toString()
        );
        alert.showAndWait();
    }

    public void populateRequests() {
        allRequests = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getReimbursementRequestService().getAllRequests();

        filterRequests();
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        if (filteredRequests == null) {
            tvReimbursementRequests.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, filteredRequests.size());
        List<ReimbursementRequest> pageData = filteredRequests.subList(fromIndex, toIndex);
        tvReimbursementRequests.setItems(FXCollections.observableList(pageData));
    }
}

