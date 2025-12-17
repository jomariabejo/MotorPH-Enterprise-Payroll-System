package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.OvertimeRequest;
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
public class OvertimeRequestsController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    @FXML
    private TableView<OvertimeRequest> tvOvertimeRequests;

    @FXML
    private Pagination paginationOvertimeRequests;

    private List<OvertimeRequest> allOvertimeRequests;

    public OvertimeRequestsController() {
    }

    @FXML
    void initialize() {
        setupTableView();
        populateOvertimeRequests();
    }

    private void setupTableView() {
        createTableColumns();
    }

    private void createTableColumns() {
        TableColumn<OvertimeRequest, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        idColumn.setPrefWidth(80);

        TableColumn<OvertimeRequest, String> employeeColumn = new TableColumn<>("Employee");
        employeeColumn.setCellValueFactory(cellData -> {
            var emp = cellData.getValue().getEmployeeID();
            return new javafx.beans.property.SimpleStringProperty(
                    emp != null ? emp.getFirstName() + " " + emp.getLastName() : "");
        });
        employeeColumn.setPrefWidth(200);

        TableColumn<OvertimeRequest, String> dateRequestedColumn = new TableColumn<>("Date Requested");
        dateRequestedColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDateRequested() != null 
                                ? cellData.getValue().getDateRequested().toString() : ""));
        dateRequestedColumn.setPrefWidth(150);

        TableColumn<OvertimeRequest, String> overtimeDateColumn = new TableColumn<>("Overtime Date");
        overtimeDateColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getOvertimeDate() != null 
                                ? cellData.getValue().getOvertimeDate().toString() : ""));
        overtimeDateColumn.setPrefWidth(150);

        TableColumn<OvertimeRequest, String> hoursColumn = new TableColumn<>("Hours Requested");
        hoursColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getHoursRequested() != null 
                                ? cellData.getValue().getHoursRequested().toString() : ""));
        hoursColumn.setPrefWidth(120);

        TableColumn<OvertimeRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setPrefWidth(120);

        TableColumn<OvertimeRequest, Void> actionsColumn = createActionsColumn();
        actionsColumn.setPrefWidth(250);

        tvOvertimeRequests.getColumns().addAll(idColumn, employeeColumn, dateRequestedColumn, overtimeDateColumn, hoursColumn, statusColumn, actionsColumn);
        tvOvertimeRequests.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private TableColumn<OvertimeRequest, Void> createActionsColumn() {
        TableColumn<OvertimeRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button(null, new FontIcon(Feather.CHECK));
            private final Button rejectButton = new Button(null, new FontIcon(Feather.X));
            private final Button viewButton = new Button(null, new FontIcon(Feather.EYE));

            {
                approveButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                rejectButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                viewButton.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);

                approveButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Requested".equals(selected.getStatus())) {
                        approveOvertimeRequest(selected);
                    }
                });

                rejectButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null && "Requested".equals(selected.getStatus())) {
                        rejectOvertimeRequest(selected);
                    }
                });

                viewButton.setOnAction(event -> {
                    OvertimeRequest selected = getTableView().getItems().get(getIndex());
                    if (selected != null) {
                        viewOvertimeRequest(selected);
                    }
                });
            }

            private final HBox actionsBox = new HBox(viewButton, approveButton, rejectButton);

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
                    OvertimeRequest request = getTableView().getItems().get(getIndex());
                    if (request != null && "Requested".equals(request.getStatus())) {
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

    private void approveOvertimeRequest(OvertimeRequest request) {
        openApprovalDialog(request, "Approve");
    }

    private void rejectOvertimeRequest(OvertimeRequest request) {
        openApprovalDialog(request, "Reject");
    }

    private void openApprovalDialog(OvertimeRequest request, String action) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/human-resource/overtime-approval-form.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle(action + " Overtime Request");
            formStage.setScene(new Scene(formPane));

            OvertimeApprovalFormController formController = loader.getController();
            formController.setOvertimeRequestsController(this);
            formController.setOvertimeRequest(request);
            formController.setAction(action);
            formController.setup();

            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewOvertimeRequest(OvertimeRequest request) {
        StringBuilder details = new StringBuilder();
        details.append("Employee: ").append(request.getEmployeeID().getFirstName())
                .append(" ").append(request.getEmployeeID().getLastName()).append("\n");
        details.append("Date Requested: ").append(request.getDateRequested()).append("\n");
        details.append("Overtime Date: ").append(request.getOvertimeDate()).append("\n");
        details.append("Hours Requested: ").append(request.getHoursRequested()).append("\n");
        details.append("Status: ").append(request.getStatus()).append("\n");
        if (request.getHRRemarks() != null && !request.getHRRemarks().isEmpty()) {
            details.append("HR Remarks: ").append(request.getHRRemarks());
        }

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Overtime Request Details",
                details.toString()
        );
        alert.showAndWait();
    }

    public void populateOvertimeRequests() {
        allOvertimeRequests = humanResourceAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getOvertimeRequestService().getAllOvertimeRequests();

        int itemsPerPage = 25;
        int pageCount = Math.max(1, (int) Math.ceil((double) allOvertimeRequests.size() / itemsPerPage));
        paginationOvertimeRequests.setPageCount(pageCount);

        paginationOvertimeRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allOvertimeRequests.size());
        List<OvertimeRequest> pageData = allOvertimeRequests.subList(fromIndex, toIndex);
        tvOvertimeRequests.setItems(FXCollections.observableList(pageData));
    }

    public void processApproval(OvertimeRequest request, String action, String remarks) {
        if ("Approve".equals(action)) {
            request.setStatus("Approved");
        } else if ("Reject".equals(action)) {
            request.setStatus("Rejected");
        }
        request.setHRRemarks(remarks);

        humanResourceAdministratorNavigationController.getMainViewController()
                .getServiceFactory().getOvertimeRequestService().updateOvertimeRequest(request);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Overtime request has been " + action.toLowerCase() + "d."
        );
        alert.showAndWait();
        populateOvertimeRequests();
    }
}

