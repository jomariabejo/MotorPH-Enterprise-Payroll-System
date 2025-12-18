package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.OvertimeRequest;
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

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class OvertimeApprovalController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<OvertimeRequest> tvOvertimeRequests;

    @FXML
    private Pagination paginationOvertimeRequests;

    @FXML
    private TextField tfSearchEmployee;

    @FXML
    private ComboBox<String> cbStatusFilter;

    private List<OvertimeRequest> allOvertimeRequests;

    public OvertimeApprovalController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
    }

    private void setupFilters() {
        // Setup status filter
        if (cbStatusFilter != null) {
            cbStatusFilter.getItems().addAll("All", "Requested", "Approved", "Rejected");
            cbStatusFilter.getSelectionModel().selectFirst();
            cbStatusFilter.setOnAction(e -> filterOvertimeRequests());
        }

        // Setup search field
        if (tfSearchEmployee != null) {
            tfSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> filterOvertimeRequests());
        }
    }

    private void filterOvertimeRequests() {
        if (allOvertimeRequests == null) {
            return;
        }

        String searchText = tfSearchEmployee != null ? tfSearchEmployee.getText().toLowerCase() : "";
        String statusFilter = cbStatusFilter != null && cbStatusFilter.getSelectionModel().getSelectedItem() != null
                ? cbStatusFilter.getSelectionModel().getSelectedItem() : "All";

        List<OvertimeRequest> filtered = allOvertimeRequests.stream()
                .filter(request -> {
                    // Search filter
                    if (!searchText.isEmpty()) {
                        var emp = request.getEmployeeID();
                        if (emp != null) {
                            String employeeName = (emp.getFirstName() + " " + emp.getLastName()).toLowerCase();
                            String employeeId = String.valueOf(emp.getEmployeeNumber());
                            if (!employeeName.contains(searchText) && !employeeId.contains(searchText)) {
                                return false;
                            }
                        }
                    }
                    // Status filter
                    if (!"All".equals(statusFilter)) {
                        if (!statusFilter.equals(request.getStatus())) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) filtered.size() / itemsPerPage));
        paginationOvertimeRequests.setPageCount(pageCount);

        paginationOvertimeRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage, filtered);
            return new StackPane();
        });
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<OvertimeRequest, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<OvertimeRequest, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<OvertimeRequest, String> employeeIdColumn = new TableColumn<>("Employee ID");
        employeeIdColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? String.valueOf(emp.getEmployeeNumber()) : "");
        });
        employeeIdColumn.setPrefWidth(100);

        TableColumn<OvertimeRequest, String> dateRequestedColumn = new TableColumn<>("Date Requested");
        dateRequestedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDateRequested() != null
                                ? cellData.getValue().getDateRequested().toString() : ""));
        dateRequestedColumn.setPrefWidth(150);

        TableColumn<OvertimeRequest, String> overtimeDateColumn = new TableColumn<>("Overtime Date");
        overtimeDateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getOvertimeDate() != null
                                ? cellData.getValue().getOvertimeDate().toString() : ""));
        overtimeDateColumn.setPrefWidth(150);

        TableColumn<OvertimeRequest, String> hoursColumn = new TableColumn<>("Hours Requested");
        hoursColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getHoursRequested() != null
                                ? cellData.getValue().getHoursRequested().toString() : ""));
        hoursColumn.setPrefWidth(120);

        TableColumn<OvertimeRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<OvertimeRequest, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(250);

        tvOvertimeRequests.getColumns().addAll(idColumn, employeeColumn, employeeIdColumn, dateRequestedColumn,
                overtimeDateColumn, hoursColumn, statusColumn, actionsColumn);
        tvOvertimeRequests.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<OvertimeRequest, Void> createActionsColumn() {
        TableColumn<OvertimeRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

                approveButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Requested".equals(selected.getStatus())) {
                        approveOvertimeRequest(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Requested".equals(selected.getStatus())) {
                        rejectOvertimeRequest(selected);
                    }
                });

                viewButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        viewOvertimeRequest(selected);
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
                    OvertimeRequest request = getTableView().getItems().get(getIndex());
                    if (request != null && "Requested".equals(request.getStatus())) {
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

    private void approveOvertimeRequest(OvertimeRequest request) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Approve Overtime Request",
                "Are you sure you want to approve this overtime request?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            request.setStatus("Approved");
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getOvertimeRequestService().updateOvertimeRequest(request);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Overtime Request Approved",
                    "Overtime request has been approved successfully."
            );
            successAlert.showAndWait();
            populateOvertimeRequests();
        }
    }

    private void rejectOvertimeRequest(OvertimeRequest request) {
        // Open a dialog to get rejection remarks
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reject Overtime Request");
        dialog.setHeaderText("Please provide a reason for rejection:");
        dialog.setContentText("Remarks:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            request.setStatus("Rejected");
            request.setHRRemarks(result.get().trim());
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getOvertimeRequestService().updateOvertimeRequest(request);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Overtime Request Rejected",
                    "Overtime request has been rejected."
            );
            successAlert.showAndWait();
            populateOvertimeRequests();
        }
    }

    private void viewOvertimeRequest(OvertimeRequest request) {
        StringBuilder details = new StringBuilder();
        var emp = request.getEmployeeID();
        if (emp != null) {
            details.append("Employee: ").append(emp.getFirstName()).append(" ").append(emp.getLastName()).append("\n");
            details.append("Employee ID: ").append(emp.getEmployeeNumber()).append("\n");
        }
        details.append("Date Requested: ").append(request.getDateRequested()).append("\n");
        details.append("Overtime Date: ").append(request.getOvertimeDate()).append("\n");
        details.append("Hours Requested: ").append(request.getHoursRequested()).append("\n");
        details.append("Status: ").append(request.getStatus()).append("\n");
        if (request.getHRRemarks() != null && !request.getHRRemarks().isEmpty()) {
            details.append("Remarks: ").append(request.getHRRemarks());
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Overtime Request Details",
                details.toString()
        );
        alert.showAndWait();
    }

    public void populateOvertimeRequests() {
        allOvertimeRequests = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getOvertimeRequestService().getAllOvertimeRequests();

        filterOvertimeRequests();
    }

    private void updateTableView(int pageIndex, int itemsPerPage, List<OvertimeRequest> data) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        List<OvertimeRequest> pageData = data.subList(fromIndex, toIndex);
        tvOvertimeRequests.setItems(FXCollections.observableList(pageData));
    }
}
