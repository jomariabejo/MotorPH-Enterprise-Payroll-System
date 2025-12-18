package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.model.PayslipHistory;
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

import java.text.SimpleDateFormat;
import java.util.List;

@Getter
@Setter
public class PayslipHistoryController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<PayslipHistory> tvPayslipHistory;

    @FXML
    private Pagination paginationHistory;

    @FXML
    private TextField tfSearchEmployee;

    @FXML
    private TextField tfSearchField;

    private List<PayslipHistory> allHistory;

    public PayslipHistoryController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        setupFilters();
    }

    private void setupFilters() {
        // Setup search field for employee
        if (tfSearchEmployee != null) {
            tfSearchEmployee.textProperty().addListener((observable, oldValue, newValue) -> filterHistory());
        }

        // Setup search field for changed field
        if (tfSearchField != null) {
            tfSearchField.textProperty().addListener((observable, oldValue, newValue) -> filterHistory());
        }
    }

    private void filterHistory() {
        if (allHistory == null) {
            return;
        }

        String searchEmployee = tfSearchEmployee != null ? tfSearchEmployee.getText().toLowerCase() : "";
        String searchField = tfSearchField != null ? tfSearchField.getText().toLowerCase() : "";

        List<PayslipHistory> filtered = allHistory.stream()
                .filter(history -> {
                    // Employee search filter
                    if (!searchEmployee.isEmpty()) {
                        Payslip payslip = history.getPayslipID();
                        if (payslip != null) {
                            Employee emp = payslip.getEmployeeID();
                            if (emp != null) {
                                String employeeName = (emp.getFirstName() + " " + emp.getLastName()).toLowerCase();
                                String employeeId = String.valueOf(emp.getEmployeeNumber());
                                if (!employeeName.contains(searchEmployee) && !employeeId.contains(searchEmployee)) {
                                    return false;
                                }
                            }
                        }
                    }
                    // Field search filter
                    if (!searchField.isEmpty()) {
                        String fieldChanged = history.getFieldChanged() != null ? history.getFieldChanged().toLowerCase() : "";
                        if (!fieldChanged.contains(searchField)) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) filtered.size() / itemsPerPage));
        paginationHistory.setPageCount(pageCount);

        paginationHistory.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage, filtered);
            return new StackPane();
        });
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<PayslipHistory, Integer> idColumn = new TableColumn<>("History ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(100);

        TableColumn<PayslipHistory, String> payslipIdColumn = new TableColumn<>("Payslip ID");
        payslipIdColumn.setCellValueFactory(cellData -> {
            Payslip payslip = cellData.getValue().getPayslipID();
            return new javafx.beans.property.SimpleStringProperty(
                    payslip != null ? String.valueOf(payslip.getId()) : "");
        });
        payslipIdColumn.setPrefWidth(100);

        TableColumn<PayslipHistory, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            Payslip payslip = cellData.getValue().getPayslipID();
            if (payslip != null) {
                Employee emp = payslip.getEmployeeID();
                if (emp != null) {
                    return new javafx.beans.property.SimpleStringProperty(
                            emp.getFirstName() + " " + emp.getLastName());
                }
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<PayslipHistory, String> fieldChangedColumn = new TableColumn<>("Field Changed");
        fieldChangedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFieldChanged()));
        fieldChangedColumn.setPrefWidth(150);

        TableColumn<PayslipHistory, String> oldValueColumn = new TableColumn<>("Old Value");
        oldValueColumn.setCellValueFactory(cellData -> {
            String oldValue = cellData.getValue().getOldValue();
            return new javafx.beans.property.SimpleStringProperty(
                    oldValue != null && oldValue.length() > 50 ? oldValue.substring(0, 50) + "..." : (oldValue != null ? oldValue : ""));
        });
        oldValueColumn.setPrefWidth(200);

        TableColumn<PayslipHistory, String> newValueColumn = new TableColumn<>("New Value");
        newValueColumn.setCellValueFactory(cellData -> {
            String newValue = cellData.getValue().getNewValue();
            return new javafx.beans.property.SimpleStringProperty(
                    newValue != null && newValue.length() > 50 ? newValue.substring(0, 50) + "..." : (newValue != null ? newValue : ""));
        });
        newValueColumn.setPrefWidth(200);

        TableColumn<PayslipHistory, String> modifiedByColumn = new TableColumn<>("Modified By");
        modifiedByColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModifiedBy()));
        modifiedByColumn.setPrefWidth(150);

        TableColumn<PayslipHistory, String> modificationDateColumn = new TableColumn<>("Modification Date");
        modificationDateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getModificationDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                return new javafx.beans.property.SimpleStringProperty(
                        sdf.format(java.util.Date.from(cellData.getValue().getModificationDate())));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        modificationDateColumn.setPrefWidth(180);

        TableColumn<PayslipHistory, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(150);

        tvPayslipHistory.getColumns().addAll(idColumn, payslipIdColumn, employeeColumn, fieldChangedColumn,
                oldValueColumn, newValueColumn, modifiedByColumn, modificationDateColumn, actionsColumn);
        tvPayslipHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<PayslipHistory, Void> createActionsColumn() {
        TableColumn<PayslipHistory, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

                viewButton.setOnAction(event -> {
                    PayslipHistory selectedHistory = getTableView().getItems().get(getIndex());
                    if (selectedHistory != null) {
                        viewHistoryDetails(selectedHistory);
                    }
                });
            }

            private final HBox actionsBox = new HBox(viewButton);

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

    private void viewHistoryDetails(PayslipHistory history) {
        StringBuilder details = new StringBuilder();
        details.append("History ID: ").append(history.getId()).append("\n");
        
        Payslip payslip = history.getPayslipID();
        if (payslip != null) {
            details.append("Payslip ID: ").append(payslip.getId()).append("\n");
            Employee emp = payslip.getEmployeeID();
            if (emp != null) {
                details.append("Employee: ").append(emp.getFirstName()).append(" ").append(emp.getLastName()).append("\n");
            }
        }
        
        details.append("Field Changed: ").append(history.getFieldChanged()).append("\n");
        details.append("Old Value: ").append(history.getOldValue() != null ? history.getOldValue() : "N/A").append("\n");
        details.append("New Value: ").append(history.getNewValue() != null ? history.getNewValue() : "N/A").append("\n");
        details.append("Modified By: ").append(history.getModifiedBy()).append("\n");
        
        if (history.getModificationDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            details.append("Modification Date: ").append(sdf.format(java.util.Date.from(history.getModificationDate())));
        }
        
        if (history.getModifiedFieldDetails() != null && !history.getModifiedFieldDetails().isEmpty()) {
            details.append("\n\nDetails:\n").append(history.getModifiedFieldDetails());
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Payslip History Details",
                details.toString()
        );
        alert.showAndWait();
    }

    public void populateHistory() {
        allHistory = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPayslipHistoryService().getAllPayslipHistory();

        filterHistory();
    }

    private void updateTableView(int pageIndex, int itemsPerPage, List<PayslipHistory> data) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        List<PayslipHistory> pageData = data.subList(fromIndex, toIndex);
        tvPayslipHistory.setItems(FXCollections.observableList(pageData));
    }
}
