package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.PagibigContributionRate;
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
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PagibigContributionRateController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddRate;

    @FXML
<<<<<<< HEAD
    private Button btnPopulateRates;

    @FXML
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    private Pagination paginationRates;

    @FXML
    private TableView<PagibigContributionRate> tvRates;

    private List<PagibigContributionRate> allRates;

    public PagibigContributionRateController() {
<<<<<<< HEAD
        // Default constructor required by JavaFX
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddButton();
<<<<<<< HEAD
        customizePopulateButton();
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    }

    @FXML
    void addRateClicked() {
        openRateForm(null);
    }

    private void openRateForm(PagibigContributionRate rate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/pagibig-rate-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(rate == null ? "Add New Pagibig Contribution Rate" : "Edit Pagibig Contribution Rate");
            formStage.setScene(new Scene(formPane));

            PagibigContributionRateFormController formController = loader.getController();
            formController.setRateController(this);
            formController.setRate(rate);
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
        TableColumn<PagibigContributionRate, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<PagibigContributionRate, String> salaryFromColumn = new TableColumn<>("Salary From");
        salaryFromColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketFrom() != null 
                                ? cellData.getValue().getSalaryBracketFrom().toString() : ""));
        salaryFromColumn.setPrefWidth(120);

        TableColumn<PagibigContributionRate, String> salaryToColumn = new TableColumn<>("Salary To");
        salaryToColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketTo() != null 
                                ? cellData.getValue().getSalaryBracketTo().toString() : ""));
        salaryToColumn.setPrefWidth(120);

        TableColumn<PagibigContributionRate, String> employeeShareColumn = new TableColumn<>("Employee Share");
        employeeShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployeeShare() != null 
                                ? cellData.getValue().getEmployeeShare().toString() : ""));
        employeeShareColumn.setPrefWidth(130);

        TableColumn<PagibigContributionRate, String> employerShareColumn = new TableColumn<>("Employer Share");
        employerShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployerShare() != null 
                                ? cellData.getValue().getEmployerShare().toString() : ""));
        employerShareColumn.setPrefWidth(130);

        TableColumn<PagibigContributionRate, String> effectiveDateColumn = new TableColumn<>("Effective Date");
        effectiveDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEffectiveDate() != null 
                                ? cellData.getValue().getEffectiveDate().toString() : ""));
        effectiveDateColumn.setPrefWidth(120);

        TableColumn<PagibigContributionRate, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

<<<<<<< HEAD
        @SuppressWarnings("unchecked")
        TableColumn<PagibigContributionRate, ?>[] columns = new TableColumn[]{
                idColumn, salaryFromColumn, salaryToColumn, employeeShareColumn, employerShareColumn, effectiveDateColumn, actionsColumn
        };
        tvRates.getColumns().addAll(columns);
        // Note: CONSTRAINED_RESIZE_POLICY is deprecated but still functional
        @SuppressWarnings("deprecation")
        var resizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
        tvRates.setColumnResizePolicy(resizePolicy);
=======
        tvRates.getColumns().addAll(idColumn, salaryFromColumn, salaryToColumn, employeeShareColumn, employerShareColumn, effectiveDateColumn, actionsColumn);
        tvRates.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    }

    private TableColumn<PagibigContributionRate, Void> createActionsColumn() {
        TableColumn<PagibigContributionRate, Void> actionsColumn = new TableColumn<>("Actions");
<<<<<<< HEAD
        actionsColumn.setCellFactory(param -> new ActionsTableCell());
        return actionsColumn;
    }

    private class ActionsTableCell extends TableCell<PagibigContributionRate, Void> {
        private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
        private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
        private final HBox actionsBox = new HBox(editButton, deleteButton);

        ActionsTableCell() {
            editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
            deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
            actionsBox.setAlignment(Pos.CENTER);
            actionsBox.setSpacing(10);

            editButton.setOnAction(event -> {
                PagibigContributionRate selected = getTableView().getItems().get(getIndex());
                if (selected != null) {
                    openRateForm(selected);
                }
            });

            deleteButton.setOnAction(event -> {
                PagibigContributionRate selected = getTableView().getItems().get(getIndex());
                if (selected != null) {
                    deleteRate(selected);
                }
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : actionsBox);
        }
    }

=======
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    PagibigContributionRate selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openRateForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    PagibigContributionRate selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deleteRate(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox(editButton, deleteButton);

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

>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
    private void deleteRate(PagibigContributionRate rate) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Contribution Rate",
                "Are you sure you want to delete this contribution rate?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPagibigContributionRateService().deletePagibigContributionRate(rate);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Rate Deleted",
                    "Contribution rate has been deleted."
            );
            successAlert.showAndWait();
            populateRates();
        }
    }

    private void customizeAddButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddRate.setGraphic(fontIcon);
        btnAddRate.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

<<<<<<< HEAD
    private void customizePopulateButton() {
        FontIcon fontIcon = new FontIcon(Feather.DATABASE);
        btnPopulateRates.setGraphic(fontIcon);
        btnPopulateRates.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

    @FXML
    void populateRatesClicked() {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Populate Pagibig Rates",
                """
                This will populate all standard Pagibig contribution rates.
                
                Existing rates with the same effective date will be skipped.
                
                Continue?"""
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var serviceFactory = payrollAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory();

                int count = populateStandardPagibigRates(serviceFactory);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        String.format("Successfully populated %d Pagibig contribution rate(s).", count)
                );
                successAlert.showAndWait();
                populateRates();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to populate Pagibig rates: " + e.getMessage() +
                        "\n\nPlease check the console for more details."
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private int populateStandardPagibigRates(com.jomariabejo.motorph.service.ServiceFactory serviceFactory) {
        java.time.LocalDate effectiveDate = java.time.LocalDate.now();
        int createdCount = 0;
        var pagibigService = serviceFactory.getPagibigContributionRateService();

        // Pagibig Contribution Rates
        // 1,000 to 1,500: Employee 1%, Employer 2% (at 1,250 midpoint: Employee 12.50, Employer 25.00)
        // Over 1,500: Employee 2%, Employer 2% (at 2,000 representative: Employee 40, Employer 40)
        createdCount += createPagibigRateIfNotExists(pagibigService, effectiveDate,
                java.math.BigDecimal.valueOf(1000), java.math.BigDecimal.valueOf(1500),
                java.math.BigDecimal.valueOf(12.50), java.math.BigDecimal.valueOf(25.00));

        createdCount += createPagibigRateIfNotExists(pagibigService, effectiveDate,
                java.math.BigDecimal.valueOf(1500.01), java.math.BigDecimal.valueOf(999999999),
                java.math.BigDecimal.valueOf(40.00), java.math.BigDecimal.valueOf(40.00));

        return createdCount;
    }

    private int createPagibigRateIfNotExists(
            com.jomariabejo.motorph.service.PagibigContributionRateService service,
            java.time.LocalDate effectiveDate,
            java.math.BigDecimal salaryFrom,
            java.math.BigDecimal salaryTo,
            java.math.BigDecimal employeeShare,
            java.math.BigDecimal employerShare) {

        List<PagibigContributionRate> existingRates = service.getAllPagibigContributionRates();
        for (PagibigContributionRate existing : existingRates) {
            if (existing.getSalaryBracketFrom().compareTo(salaryFrom) == 0 &&
                existing.getSalaryBracketTo().compareTo(salaryTo) == 0 &&
                existing.getEffectiveDate().equals(effectiveDate)) {
                return 0; // Already exists
            }
        }

        PagibigContributionRate rate = new PagibigContributionRate();
        rate.setSalaryBracketFrom(salaryFrom);
        rate.setSalaryBracketTo(salaryTo);
        rate.setEmployeeShare(employeeShare);
        rate.setEmployerShare(employerShare);
        rate.setEffectiveDate(effectiveDate);
        service.savePagibigContributionRate(rate);
        return 1;
    }

    public void populateRates() {
        if (payrollAdministratorNavigationController == null || 
            payrollAdministratorNavigationController.getMainViewController() == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        
        // Check if database is empty and auto-populate if needed
        List<PagibigContributionRate> existingRates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        if (existingRates == null || existingRates.isEmpty()) {
            // Database is empty, populate standard rates automatically
            populateStandardPagibigRates(serviceFactory);
            // Refresh the list after populating
            existingRates = serviceFactory.getPagibigContributionRateService().getAllPagibigContributionRates();
        }

        allRates = existingRates;
=======
    public void populateRates() {
        allRates = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPagibigContributionRateService().getAllPagibigContributionRates();
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allRates.size() / itemsPerPage));
        paginationRates.setPageCount(pageCount);

        paginationRates.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allRates.size());
        List<PagibigContributionRate> pageData = allRates.subList(fromIndex, toIndex);
        tvRates.setItems(FXCollections.observableList(pageData));
    }
}








