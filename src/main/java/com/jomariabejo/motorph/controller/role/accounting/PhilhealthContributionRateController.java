package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.PhilhealthContributionRate;
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
public class PhilhealthContributionRateController {

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
    private TableView<PhilhealthContributionRate> tvRates;

    private List<PhilhealthContributionRate> allRates;

    public PhilhealthContributionRateController() {
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

    private void openRateForm(PhilhealthContributionRate rate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/philhealth-rate-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(rate == null ? "Add New Philhealth Contribution Rate" : "Edit Philhealth Contribution Rate");
            formStage.setScene(new Scene(formPane));

            PhilhealthContributionRateFormController formController = loader.getController();
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
        TableColumn<PhilhealthContributionRate, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<PhilhealthContributionRate, String> salaryFromColumn = new TableColumn<>("Salary From");
        salaryFromColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketFrom() != null 
                                ? cellData.getValue().getSalaryBracketFrom().toString() : ""));
        salaryFromColumn.setPrefWidth(120);

        TableColumn<PhilhealthContributionRate, String> salaryToColumn = new TableColumn<>("Salary To");
        salaryToColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketTo() != null 
                                ? cellData.getValue().getSalaryBracketTo().toString() : ""));
        salaryToColumn.setPrefWidth(120);

        TableColumn<PhilhealthContributionRate, String> employeeShareColumn = new TableColumn<>("Employee Share");
        employeeShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployeeShare() != null 
                                ? cellData.getValue().getEmployeeShare().toString() : ""));
        employeeShareColumn.setPrefWidth(130);

        TableColumn<PhilhealthContributionRate, String> employerShareColumn = new TableColumn<>("Employer Share");
        employerShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployerShare() != null 
                                ? cellData.getValue().getEmployerShare().toString() : ""));
        employerShareColumn.setPrefWidth(130);

        TableColumn<PhilhealthContributionRate, String> effectiveDateColumn = new TableColumn<>("Effective Date");
        effectiveDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEffectiveDate() != null 
                                ? cellData.getValue().getEffectiveDate().toString() : ""));
        effectiveDateColumn.setPrefWidth(120);

        TableColumn<PhilhealthContributionRate, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

<<<<<<< HEAD
        @SuppressWarnings("unchecked")
        TableColumn<PhilhealthContributionRate, ?>[] columns = new TableColumn[]{
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

    private TableColumn<PhilhealthContributionRate, Void> createActionsColumn() {
        TableColumn<PhilhealthContributionRate, Void> actionsColumn = new TableColumn<>("Actions");
<<<<<<< HEAD
        actionsColumn.setCellFactory(param -> new ActionsTableCell());
        return actionsColumn;
    }

    private class ActionsTableCell extends TableCell<PhilhealthContributionRate, Void> {
        private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
        private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
        private final HBox actionsBox = new HBox(editButton, deleteButton);

        ActionsTableCell() {
            editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
            deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
            actionsBox.setAlignment(Pos.CENTER);
            actionsBox.setSpacing(10);

            editButton.setOnAction(event -> {
                PhilhealthContributionRate selected = getTableView().getItems().get(getIndex());
                if (selected != null) {
                    openRateForm(selected);
                }
            });

            deleteButton.setOnAction(event -> {
                PhilhealthContributionRate selected = getTableView().getItems().get(getIndex());
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
                    PhilhealthContributionRate selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openRateForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    PhilhealthContributionRate selected = getTableView().getItems().get(getIndex());
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
    private void deleteRate(PhilhealthContributionRate rate) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Contribution Rate",
                "Are you sure you want to delete this contribution rate?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPhilhealthContributionRateService().deleteRate(rate);

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
                "Populate Philhealth Rates",
                """
                This will populate all standard Philhealth contribution rates.
                
                Existing rates with the same effective date will be skipped.
                
                Continue?"""
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var serviceFactory = payrollAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory();

                int count = populateStandardPhilhealthRates(serviceFactory);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        String.format("Successfully populated %d Philhealth contribution rate(s).", count)
                );
                successAlert.showAndWait();
                populateRates();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        String.format("""
                        Failed to populate Philhealth rates: %s
                        
                        Please check the console for more details.""", e.getMessage())
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private int populateStandardPhilhealthRates(com.jomariabejo.motorph.service.ServiceFactory serviceFactory) {
        java.time.LocalDate effectiveDate = java.time.LocalDate.now();
        int createdCount = 0;
        var philhealthService = serviceFactory.getPhilhealthContributionRateService();

        // Philhealth Contribution Rates
        // 3% premium rate, split 50/50 between employee and employer
        // Fixed brackets: 0-10,000 (300), 60,000+ (1,800)
        // Percentage brackets: 10,000.01-59,999.99 (3% of salary)
        createdCount += createPhilhealthRateIfNotExists(philhealthService, effectiveDate,
                java.math.BigDecimal.ZERO, java.math.BigDecimal.valueOf(10000),
                java.math.BigDecimal.valueOf(150), java.math.BigDecimal.valueOf(150));

        // Create percentage-based brackets for 10,000.01 to 59,999.99 range
        // These represent the 3% calculation at key salary points
        double[][] percentageBrackets = {
            {10000.01, 20000, 450.00},     // 3% of 15,000 = 450 (225/225)
            {20000.01, 30000, 750.00},     // 3% of 25,000 = 750 (375/375)
            {30000.01, 40000, 1050.00},    // 3% of 35,000 = 1,050 (525/525)
            {40000.01, 50000, 1350.00},    // 3% of 45,000 = 1,350 (675/675)
            {50000.01, 59999.99, 1650.00}  // 3% of 55,000 = 1,650 (825/825)
        };

        for (double[] bracket : percentageBrackets) {
            java.math.BigDecimal bracketFrom = java.math.BigDecimal.valueOf(bracket[0]);
            java.math.BigDecimal bracketTo = java.math.BigDecimal.valueOf(bracket[1]);
            double totalContribution = bracket[2];
            java.math.BigDecimal employeeShare = java.math.BigDecimal.valueOf(totalContribution / 2.0);
            java.math.BigDecimal employerShare = java.math.BigDecimal.valueOf(totalContribution / 2.0);

            createdCount += createPhilhealthRateIfNotExists(philhealthService, effectiveDate,
                    bracketFrom, bracketTo, employeeShare, employerShare);
        }

        // Fixed bracket for 60,000+
        createdCount += createPhilhealthRateIfNotExists(philhealthService, effectiveDate,
                java.math.BigDecimal.valueOf(60000), java.math.BigDecimal.valueOf(999999999),
                java.math.BigDecimal.valueOf(900), java.math.BigDecimal.valueOf(900));

        return createdCount;
    }

    private int createPhilhealthRateIfNotExists(
            com.jomariabejo.motorph.service.PhilhealthContributionRateService service,
            java.time.LocalDate effectiveDate,
            java.math.BigDecimal salaryFrom,
            java.math.BigDecimal salaryTo,
            java.math.BigDecimal employeeShare,
            java.math.BigDecimal employerShare) {

        List<PhilhealthContributionRate> existingRates = service.getAllRates();
        for (PhilhealthContributionRate existing : existingRates) {
            if (existing.getSalaryBracketFrom().compareTo(salaryFrom) == 0 &&
                existing.getSalaryBracketTo().compareTo(salaryTo) == 0 &&
                existing.getEffectiveDate().equals(effectiveDate)) {
                return 0; // Already exists
            }
        }

        PhilhealthContributionRate rate = new PhilhealthContributionRate();
        rate.setSalaryBracketFrom(salaryFrom);
        rate.setSalaryBracketTo(salaryTo);
        rate.setEmployeeShare(employeeShare);
        rate.setEmployerShare(employerShare);
        rate.setEffectiveDate(effectiveDate);
        service.saveRate(rate);
        return 1;
    }

    public void populateRates() {
        if (payrollAdministratorNavigationController == null || 
            payrollAdministratorNavigationController.getMainViewController() == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        
        // Check if database is empty and auto-populate if needed
        List<PhilhealthContributionRate> existingRates = serviceFactory.getPhilhealthContributionRateService().getAllRates();
        if (existingRates == null || existingRates.isEmpty()) {
            // Database is empty, populate standard rates automatically
            populateStandardPhilhealthRates(serviceFactory);
            // Refresh the list after populating
            existingRates = serviceFactory.getPhilhealthContributionRateService().getAllRates();
        }

        allRates = existingRates;
=======
    public void populateRates() {
        allRates = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPhilhealthContributionRateService().getAllRates();
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
        List<PhilhealthContributionRate> pageData = allRates.subList(fromIndex, toIndex);
        tvRates.setItems(FXCollections.observableList(pageData));
    }
}








