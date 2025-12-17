package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollChange;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayrollChangeController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private TableView<PayrollChange> tvPayrollChanges;

    @FXML
    private Pagination paginationChanges;

    private List<PayrollChange> allChanges;

    public PayrollChangeController() {
    }

    @FXML
    void initialize() {
        setupTableView();
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<PayrollChange, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<PayrollChange, String> payrollColumn = new TableColumn<>("Payroll ID");
        payrollColumn.setCellValueFactory(cellData -> {
            Payroll payroll = cellData.getValue().getPayrollID();
            return new javafx.beans.property.SimpleStringProperty(
                    payroll != null ? String.valueOf(payroll.getId()) : "");
        });
        payrollColumn.setPrefWidth(100);

        TableColumn<PayrollChange, String> modifiedByColumn = new TableColumn<>("Modified By");
        modifiedByColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModifiedBy()));
        modifiedByColumn.setPrefWidth(150);

        TableColumn<PayrollChange, String> modificationDateColumn = new TableColumn<>("Modification Date");
        modificationDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getModificationDate() != null 
                                ? cellData.getValue().getModificationDate().toString() : ""));
        modificationDateColumn.setPrefWidth(200);

        TableColumn<PayrollChange, String> fieldChangedColumn = new TableColumn<>("Field Changed");
        fieldChangedColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFieldChanged()));
        fieldChangedColumn.setPrefWidth(150);

        TableColumn<PayrollChange, String> oldValueColumn = new TableColumn<>("Old Value");
        oldValueColumn.setCellValueFactory(cellData -> {
            String oldValue = cellData.getValue().getOldValue();
            return new javafx.beans.property.SimpleStringProperty(oldValue != null ? oldValue : "");
        });
        oldValueColumn.setPrefWidth(200);

        TableColumn<PayrollChange, String> newValueColumn = new TableColumn<>("New Value");
        newValueColumn.setCellValueFactory(cellData -> {
            String newValue = cellData.getValue().getNewValue();
            return new javafx.beans.property.SimpleStringProperty(newValue != null ? newValue : "");
        });
        newValueColumn.setPrefWidth(200);

        tvPayrollChanges.getColumns().addAll(idColumn, payrollColumn, modifiedByColumn, modificationDateColumn, fieldChangedColumn, oldValueColumn, newValueColumn);
        tvPayrollChanges.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void populateChanges() {
        allChanges = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPayrollChangeService().getAllPayrollChanges();

        // Sort by modification date descending (most recent first)
        allChanges.sort((c1, c2) -> c2.getModificationDate().compareTo(c1.getModificationDate()));

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allChanges.size() / itemsPerPage));
        paginationChanges.setPageCount(pageCount);

        paginationChanges.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allChanges.size());
        List<PayrollChange> pageData = allChanges.subList(fromIndex, toIndex);
        tvPayrollChanges.setItems(FXCollections.observableList(pageData));
    }
}








