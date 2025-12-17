package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.model.ReimbursementTransaction;
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
public class ReimbursementTransactionController {

    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;

    @FXML
    private Button btnAddTransaction;

    @FXML
    private Pagination paginationTransactions;

    @FXML
    private TableView<ReimbursementTransaction> tvTransactions;

    private List<ReimbursementTransaction> allTransactions;

    public ReimbursementTransactionController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        customizeAddButton();
    }

    @FXML
    void addTransactionClicked() {
        openTransactionForm(null);
    }

    private void openTransactionForm(ReimbursementTransaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/accounting/reimbursement-transaction-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(transaction == null ? "Add New Reimbursement Transaction" : "Edit Reimbursement Transaction");
            formStage.setScene(new Scene(formPane));

            ReimbursementTransactionFormController formController = loader.getController();
            formController.setTransactionController(this);
            formController.setTransaction(transaction);
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
        TableColumn<ReimbursementTransaction, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<ReimbursementTransaction, String> requestColumn = new TableColumn<>("Request ID");
        requestColumn.setCellValueFactory(cellData -> {
            ReimbursementRequest request = cellData.getValue().getRequestID();
            return new javafx.beans.property.SimpleStringProperty(
                    request != null ? String.valueOf(request.getReimbursementRequestId()) : "");
        });
        requestColumn.setPrefWidth(100);

        TableColumn<ReimbursementTransaction, String> dateColumn = new TableColumn<>("Transaction Date");
        dateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getTransactionDate() != null 
                                ? cellData.getValue().getTransactionDate().toString() : ""));
        dateColumn.setPrefWidth(150);

        TableColumn<ReimbursementTransaction, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAmount() != null 
                                ? cellData.getValue().getAmount().toString() : ""));
        amountColumn.setPrefWidth(150);

        TableColumn<ReimbursementTransaction, String> paidToColumn = new TableColumn<>("Paid To");
        paidToColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPaidTo()));
        paidToColumn.setPrefWidth(200);

        TableColumn<ReimbursementTransaction, String> paymentMethodColumn = new TableColumn<>("Payment Method");
        paymentMethodColumn.setCellValueFactory(cellData -> {
            String method = cellData.getValue().getPaymentMethod();
            return new javafx.beans.property.SimpleStringProperty(method != null ? method : "");
        });
        paymentMethodColumn.setPrefWidth(150);

        TableColumn<ReimbursementTransaction, String> detailsColumn = new TableColumn<>("Details");
        detailsColumn.setCellValueFactory(cellData -> {
            String details = cellData.getValue().getDetails();
            String display = details != null && details.length() > 50 ? details.substring(0, 50) + "..." : (details != null ? details : "");
            return new javafx.beans.property.SimpleStringProperty(display);
        });
        detailsColumn.setPrefWidth(250);

        TableColumn<ReimbursementTransaction, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(200);

        tvTransactions.getColumns().addAll(idColumn, requestColumn, dateColumn, amountColumn, paidToColumn, paymentMethodColumn, detailsColumn, actionsColumn);
        tvTransactions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<ReimbursementTransaction, Void> createActionsColumn() {
        TableColumn<ReimbursementTransaction, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));

            {
                editButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

                editButton.setOnAction(event -> {
                    ReimbursementTransaction selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        openTransactionForm(selected);
                    }
                });

                deleteButton.setOnAction(event -> {
                    ReimbursementTransaction selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        deleteTransaction(selected);
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

    private void deleteTransaction(ReimbursementTransaction transaction) {
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Transaction",
                "Are you sure you want to delete this reimbursement transaction?"
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            payrollAdministratorNavigationController.getMainViewController()
                    .getServiceFactory().getReimbursementTransactionService().deleteReimbursementTransaction(transaction);

            CustomAlert successAlert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Transaction Deleted",
                    "Reimbursement transaction has been deleted."
            );
            successAlert.showAndWait();
            populateTransactions();
        }
    }

    private void customizeAddButton() {
        FontIcon fontIcon = new FontIcon(Feather.PLUS);
        btnAddTransaction.setGraphic(fontIcon);
        btnAddTransaction.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    public void populateTransactions() {
        allTransactions = payrollAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getReimbursementTransactionService().getAllReimbursementTransactions();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allTransactions.size() / itemsPerPage));
        paginationTransactions.setPageCount(pageCount);

        paginationTransactions.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allTransactions.size());
        List<ReimbursementTransaction> pageData = allTransactions.subList(fromIndex, toIndex);
        tvTransactions.setItems(FXCollections.observableList(pageData));
    }
}








