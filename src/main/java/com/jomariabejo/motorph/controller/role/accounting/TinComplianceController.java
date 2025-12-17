package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.TinCompliance;
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
public class TinComplianceController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddTinCompliance;

    @FXML
    private Pagination paginationTinCompliance;

    @FXML
    private TableView<TinCompliance> tvTinCompliance;

    private List<TinCompliance> allTinCompliance;

    public TinComplianceController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddButton();
    }

    @FXML
    void addTinComplianceClicked() {
        openTinComplianceForm(null);
    }

    private void openTinComplianceForm(TinCompliance tinCompliance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/tin-compliance-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(tinCompliance == null ? "Add New TIN Compliance" : "Edit TIN Compliance");
            formStage.setScene(new Scene(formPane));

            TinComplianceFormController formController = loader.getController();
            formController.setTinComplianceController(this);
            formController.setTinCompliance(tinCompliance);
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
        TableColumn<TinCompliance, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<TinCompliance, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<TinCompliance, String> tinColumn = new TableColumn<>("TIN Number");
        tinColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTINNumber()));
        tinColumn.setPrefWidth(150);

        TableColumn<TinCompliance, String> dateColumn = new TableColumn<>("Date Registered");
        dateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDateRegistered() != null 
                                ? cellData.getValue().getDateRegistered().toString() : ""));
        dateColumn.setPrefWidth(150);

        TableColumn<TinCompliance, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvTinCompliance.getColumns().addAll(idColumn, employeeColumn, tinColumn, dateColumn, actionsColumn);
        tvTinCompliance.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<TinCompliance, Void> createActionsColumn() {
        TableColumn<TinCompliance, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    TinCompliance selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openTinComplianceForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    TinCompliance selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deleteTinCompliance(selected);
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

    private void deleteTinCompliance(TinCompliance tinCompliance) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete TIN Compliance",
                "Are you sure you want to delete this TIN compliance record?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getTinComplianceService().deleteTinCompliance(tinCompliance);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "TIN Compliance Deleted",
                    "TIN compliance record has been deleted."
            );
            successAlert.showAndWait();
            populateTinCompliance();
        }
    }

    private void customizeAddButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddTinCompliance.setGraphic(fontIcon);
        btnAddTinCompliance.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateTinCompliance() {
        allTinCompliance = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getTinComplianceService().getAllTinCompliances();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allTinCompliance.size() / itemsPerPage));
        paginationTinCompliance.setPageCount(pageCount);

        paginationTinCompliance.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allTinCompliance.size());
        List<TinCompliance> pageData = allTinCompliance.subList(fromIndex, toIndex);
        tvTinCompliance.setItems(FXCollections.observableList(pageData));
    }
}

