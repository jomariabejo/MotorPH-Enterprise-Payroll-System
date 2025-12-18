package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.TimesheetDataGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
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

        tvPayrolls.getColumns().addAll(idColumn, runDateColumn, periodStartColumn, periodEndColumn, statusColumn, createdByColumn, actionsColumn);
        tvPayrolls.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Payroll, Void> createActionsColumn() {
        TableColumn<Payroll, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

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

                approveButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        approvePayroll(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    Payroll selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        rejectPayroll(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox(viewButton, editButton, approveButton, rejectButton, deleteButton);

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
                    Payroll payroll = getTableView().getItems().get(getIndex());
                    if (payroll != null && "Pending".equals(payroll.getStatus())) {
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

    private void approvePayroll(Payroll payroll) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Approve Payroll",
                "Are you sure you want to approve this payroll?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payroll.setStatus("Approved");
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPayrollService().updatePayroll(payroll);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Payroll Approved",
                    "Payroll has been approved successfully."
            );
            successAlert.showAndWait();
            populatePayrolls();
        }
    }

    private void rejectPayroll(Payroll payroll) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reject Payroll",
                "Are you sure you want to reject this payroll?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payroll.setStatus("Rejected");
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getPayrollService().updatePayroll(payroll);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Payroll Rejected",
                    "Payroll has been rejected."
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
        // Create a dialog to select month and year
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Generate Timesheets");
        dialog.setHeaderText("Select Month and Year for Timesheet Generation");

        ComboBox<Integer> monthCombo = new ComboBox<>();
        monthCombo.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        monthCombo.setValue(LocalDate.now().getMonthValue());

        ComboBox<Integer> yearCombo = new ComboBox<>();
        yearCombo.getItems().addAll(2024, 2025, 2026);
        yearCombo.setValue(LocalDate.now().getYear());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Month:"), 0, 0);
        grid.add(monthCombo, 1, 0);
        grid.add(new Label("Year:"), 0, 1);
        grid.add(yearCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType generateButtonType = new ButtonType("Generate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                return new Pair<>(monthCombo.getValue(), yearCombo.getValue());
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        result.ifPresent(monthYear -> {
            int month = monthYear.getKey();
            int year = monthYear.getValue();

            CustomAlert confirmAlert = new CustomAlert(
                    Alert.AlertType.CONFIRMATION,
                    "Generate Timesheets",
                    "Generate timesheets for " + YearMonth.of(year, month) + "?\n\n" +
                            "This will create timesheets for all active employees.\n" +
                            "Continue?"
            );
            Optional<ButtonType> confirmResult = confirmAlert.showAndWait();

            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                try {
                    var serviceFactory = payrollAdministratorNavigationController
                            .getMainViewController()
                            .getServiceFactory();

                    int generated = TimesheetDataGenerator.generateTimesheetsForMonth(year, month, serviceFactory);

                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Generated " + generated + " timesheets for " + YearMonth.of(year, month)
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
        });
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

