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
    private Pagination paginationRates;

    @FXML
    private TableView<PhilhealthContributionRate> tvRates;

    private List<PhilhealthContributionRate> allRates;

    public PhilhealthContributionRateController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddButton();
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

        tvRates.getColumns().addAll(idColumn, salaryFromColumn, salaryToColumn, employeeShareColumn, employerShareColumn, effectiveDateColumn, actionsColumn);
        tvRates.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<PhilhealthContributionRate, Void> createActionsColumn() {
        TableColumn<PhilhealthContributionRate, Void> actionsColumn = new TableColumn<>("Actions");
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

    public void populateRates() {
        allRates = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getPhilhealthContributionRateService().getAllRates();

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








