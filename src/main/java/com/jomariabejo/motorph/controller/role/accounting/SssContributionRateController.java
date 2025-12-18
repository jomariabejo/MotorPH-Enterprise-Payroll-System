package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.SssContributionRate;
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
public class SssContributionRateController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddRate;

    @FXML
    private Button btnPopulateRates;

    @FXML
    private Pagination paginationRates;

    @FXML
    private TableView<SssContributionRate> tvRates;

    private List<SssContributionRate> allRates;

    public SssContributionRateController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddButton();
        customizePopulateButton();
    }

    @FXML
    void addRateClicked() {
        openRateForm(null);
    }

    private void openRateForm(SssContributionRate rate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/sss-rate-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(rate == null ? "Add New SSS Contribution Rate" : "Edit SSS Contribution Rate");
            formStage.setScene(new Scene(formPane));

            SssContributionRateFormController formController = loader.getController();
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
        TableColumn<SssContributionRate, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<SssContributionRate, String> salaryFromColumn = new TableColumn<>("Salary From");
        salaryFromColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketFrom() != null 
                                ? cellData.getValue().getSalaryBracketFrom().toString() : ""));
        salaryFromColumn.setPrefWidth(120);

        TableColumn<SssContributionRate, String> salaryToColumn = new TableColumn<>("Salary To");
        salaryToColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getSalaryBracketTo() != null 
                                ? cellData.getValue().getSalaryBracketTo().toString() : ""));
        salaryToColumn.setPrefWidth(120);

        TableColumn<SssContributionRate, String> employeeShareColumn = new TableColumn<>("Employee Share");
        employeeShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployeeShare() != null 
                                ? cellData.getValue().getEmployeeShare().toString() : ""));
        employeeShareColumn.setPrefWidth(130);

        TableColumn<SssContributionRate, String> employerShareColumn = new TableColumn<>("Employer Share");
        employerShareColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEmployerShare() != null 
                                ? cellData.getValue().getEmployerShare().toString() : ""));
        employerShareColumn.setPrefWidth(130);

        TableColumn<SssContributionRate, String> effectiveDateColumn = new TableColumn<>("Effective Date");
        effectiveDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getEffectiveDate() != null 
                                ? cellData.getValue().getEffectiveDate().toString() : ""));
        effectiveDateColumn.setPrefWidth(120);

        TableColumn<SssContributionRate, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvRates.getColumns().addAll(idColumn, salaryFromColumn, salaryToColumn, employeeShareColumn, employerShareColumn, effectiveDateColumn, actionsColumn);
        tvRates.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<SssContributionRate, Void> createActionsColumn() {
        TableColumn<SssContributionRate, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    SssContributionRate selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openRateForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    SssContributionRate selected = getTableView().getItems().get(getIndex());
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

    private void deleteRate(SssContributionRate rate) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Contribution Rate",
                "Are you sure you want to delete this contribution rate?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getSssContributionRateService().deleteSssContributionRate(rate);

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

    private void customizePopulateButton() {
        FontIcon fontIcon = new FontIcon(Feather.DATABASE);
        btnPopulateRates.setGraphic(fontIcon);
        btnPopulateRates.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

    @FXML
    void populateRatesClicked() {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Populate SSS Rates",
                "This will populate all standard SSS contribution rates.\n\n" +
                "Existing rates with the same effective date will be skipped.\n\n" +
                "Continue?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var serviceFactory = payrollAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory();

                int count = populateStandardSssRates(serviceFactory);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        String.format("Successfully populated %d SSS contribution rate(s).", count)
                );
                successAlert.showAndWait();
                populateRates();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to populate SSS rates: " + e.getMessage() +
                        "\n\nPlease check the console for more details."
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private int populateStandardSssRates(com.jomariabejo.motorph.service.ServiceFactory serviceFactory) {
        java.time.LocalDate effectiveDate = java.time.LocalDate.now();
        int createdCount = 0;

        // SSS Contribution Rates data
        // Format: [salaryFrom, salaryTo, totalContribution]
        // Employee and Employer each pay 50% of the total contribution
        double[][] sssRates = {
            {0, 3250, 135.00},
            {3250, 3750, 157.50},
            {3750, 4250, 180.00},
            {4250, 4750, 202.50},
            {4750, 5250, 225.00},
            {5250, 5750, 247.50},
            {5750, 6250, 270.00},
            {6250, 6750, 292.50},
            {6750, 7250, 315.00},
            {7250, 7750, 337.50},
            {7750, 8250, 360.00},
            {8250, 8750, 382.50},
            {8750, 9250, 405.00},
            {9250, 9750, 427.50},
            {9750, 10250, 450.00},
            {10250, 10750, 472.50},
            {10750, 11250, 495.00},
            {11250, 11750, 517.50},
            {11750, 12250, 540.00},
            {12250, 12750, 562.50},
            {12750, 13250, 585.00},
            {13250, 13750, 607.50},
            {13750, 14250, 630.00},
            {14250, 14750, 652.50},
            {14750, 15250, 675.00},
            {15250, 15750, 697.50},
            {15750, 16250, 720.00},
            {16250, 16750, 742.50},
            {16750, 17250, 765.00},
            {17250, 17750, 787.50},
            {17750, 18250, 810.00},
            {18250, 18750, 832.50},
            {18750, 19250, 855.00},
            {19250, 19750, 877.50},
            {19750, 20250, 900.00},
            {20250, 20750, 922.50},
            {20750, 21250, 945.00},
            {21250, 21750, 967.50},
            {21750, 22250, 990.00},
            {22250, 22750, 1012.50},
            {22750, 23250, 1035.00},
            {23250, 23750, 1057.50},
            {23750, 24250, 1080.00},
            {24250, 24750, 1102.50},
            {24750, 999999999, 1125.00} // "Over" means anything above 24,750
        };

        var sssService = serviceFactory.getSssContributionRateService();

        for (double[] rate : sssRates) {
            java.math.BigDecimal salaryFrom = java.math.BigDecimal.valueOf(rate[0]);
            java.math.BigDecimal salaryTo = java.math.BigDecimal.valueOf(rate[1]);
            double totalContribution = rate[2];
            
            // Split 50/50 between employee and employer
            java.math.BigDecimal employeeShare = java.math.BigDecimal.valueOf(totalContribution / 2.0);
            java.math.BigDecimal employerShare = java.math.BigDecimal.valueOf(totalContribution / 2.0);

            // Check if rate already exists for this date range
            boolean exists = false;
            List<SssContributionRate> existingRates = sssService.getAllSssContributionRates();
            for (SssContributionRate existing : existingRates) {
                if (existing.getSalaryBracketFrom().compareTo(salaryFrom) == 0 &&
                    existing.getSalaryBracketTo().compareTo(salaryTo) == 0 &&
                    existing.getEffectiveDate().equals(effectiveDate)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                SssContributionRate sssRate = new SssContributionRate();
                sssRate.setSalaryBracketFrom(salaryFrom);
                sssRate.setSalaryBracketTo(salaryTo);
                sssRate.setEmployeeShare(employeeShare);
                sssRate.setEmployerShare(employerShare);
                sssRate.setEffectiveDate(effectiveDate);

                sssService.saveSssContributionRate(sssRate);
                createdCount++;
            }
        }

        return createdCount;
    }

    public void populateRates() {
        if (payrollAdministratorNavigationController == null || 
            payrollAdministratorNavigationController.getMainViewController() == null) {
            return;
        }

        var serviceFactory = payrollAdministratorNavigationController.getMainViewController().getServiceFactory();
        
        // Check if database is empty and auto-populate if needed
        List<SssContributionRate> existingRates = serviceFactory.getSssContributionRateService().getAllSssContributionRates();
        if (existingRates == null || existingRates.isEmpty()) {
            // Database is empty, populate standard rates automatically
            populateStandardSssRates(serviceFactory);
            // Refresh the list after populating
            existingRates = serviceFactory.getSssContributionRateService().getAllSssContributionRates();
        }

        allRates = existingRates;

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
        List<SssContributionRate> pageData = allRates.subList(fromIndex, toIndex);
        tvRates.setItems(FXCollections.observableList(pageData));
    }
}








