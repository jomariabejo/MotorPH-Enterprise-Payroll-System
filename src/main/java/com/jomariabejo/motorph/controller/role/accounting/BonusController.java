package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.Bonus;
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
public class BonusController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddBonus;

    @FXML
    private Pagination paginationBonuses;

    @FXML
    private TableView<Bonus> tvBonuses;

    private List<Bonus> allBonuses;

    public BonusController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddBonusButton();
    }

    @FXML
    void addBonusClicked() {
        openBonusForm(null);
    }

    private void openBonusForm(Bonus bonus) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/bonus-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(bonus == null ? "Add New Bonus" : "Edit Bonus");
            formStage.setScene(new Scene(formPane));

            BonusFormController formController = loader.getController();
            formController.setBonusController(this);
            formController.setBonus(bonus);
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
        TableColumn<Bonus, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<Bonus, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getEmployeeNumber();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<Bonus, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getBonusAmount() != null 
                                ? cellData.getValue().getBonusAmount().toString() : ""));
        amountColumn.setPrefWidth(150);

        TableColumn<Bonus, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getBonusDate() != null 
                                ? cellData.getValue().getBonusDate().toString() : ""));
        dateColumn.setPrefWidth(120);

        TableColumn<Bonus, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        descriptionColumn.setPrefWidth(300);

        TableColumn<Bonus, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvBonuses.getColumns().addAll(idColumn, employeeColumn, amountColumn, dateColumn, descriptionColumn, actionsColumn);
        tvBonuses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<Bonus, Void> createActionsColumn() {
        TableColumn<Bonus, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    Bonus selectedBonus = getTableView().getItems().get(getIndex());
                    if (selectedBonus != null) {
                        openBonusForm(selectedBonus);
                    }
                });

                deleteButton.setOnAction(event -> {
                    Bonus selectedBonus = getTableView().getItems().get(getIndex());
                    if (selectedBonus != null) {
                        deleteBonus(selectedBonus);
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

    private void deleteBonus(Bonus bonus) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Bonus",
                "Are you sure you want to delete this bonus record?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getBonusService().deleteBonus(bonus);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Bonus Deleted",
                    "Bonus record has been deleted."
            );
            successAlert.showAndWait();
            populateBonuses();
        }
    }

    private void customizeAddBonusButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddBonus.setGraphic(fontIcon);
        btnAddBonus.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateBonuses() {
        allBonuses = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getBonusService().getAllBonuses();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allBonuses.size() / itemsPerPage));
        paginationBonuses.setPageCount(pageCount);

        paginationBonuses.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allBonuses.size());
        List<Bonus> pageData = allBonuses.subList(fromIndex, toIndex);
        tvBonuses.setItems(FXCollections.observableList(pageData));
    }
}








