package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.TimesheetDataGenerator;
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
public class PayrollController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddPayroll;

    @FXML
    private Button btnGenerateTestTimesheets;

    @FXML
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
        actionsColumn.setPrefWidth(200);

        tvPayrolls.getColumns().addAll(idColumn, runDateColumn, periodStartColumn, periodEndColumn, statusColumn, createdByColumn, actionsColumn);
        tvPayrolls.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Payroll, Void> createActionsColumn() {
        TableColumn<Payroll, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

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
            }

            private final HBox actionsBox = new HBox(viewButton, editButton, deleteButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(5);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
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
                "Continue?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                var serviceFactory = payrollAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory();

                int count = TimesheetDataGenerator.generateDecember2025Timesheets(serviceFactory);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        String.format("Successfully generated %d timesheet entries for December 2025.", count)
                );
                successAlert.showAndWait();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Failed to generate timesheets: " + e.getMessage()
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

