package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.OvertimeRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class OvertimeController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    private ObservableList<OvertimeRequest> overtimeRequests;

    @FXML
    private TableView<OvertimeRequest> tvOvertime;

    @FXML
    private Button overtimeRequestBtn;

    @FXML
    private ComboBox<Month> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private Pagination paginationOvertimeRequest;

    @FXML
    private void initialize() {
        enhanceOvertimeBtn();
        cbStatus.getItems().addAll("Requested", "Accepted", "Rejected");
        cbStatus.getSelectionModel().selectFirst();
    }

    public OvertimeController() {
    }

    public void cbMonthClicked() {
        populateOvertimeTableView();
    }

    public void cbYearClicked() {
        populateOvertimeTableView();
    }

    public void cbStatusChanged() {
        populateOvertimeTableView();
    }

    public void fileOvertimeRequestClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/file-overtime-request.fxml"));
        AnchorPane formPane = loader.load();
        Stage formStage = new Stage();
        formStage.setScene(new Scene(formPane));

        FileOvertimeRequestController fileOvertimeRequestController = loader.getController();
        fileOvertimeRequestController.setOvertimeController(this);

        formStage.showAndWait();
    }

    public void enhanceOvertimeBtn() {
        overtimeRequestBtn.setGraphic(new FontIcon(Material.FILE_UPLOAD));
        overtimeRequestBtn.getStyleClass().addAll(
                Styles.SUCCESS
        );
    }

    public void populateOvertimeTableView() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer month = this.cbMonth.getSelectionModel().getSelectedItem().getValue();
        Integer year = this.cbYear.getSelectionModel().getSelectedItem();
        String status = this.cbStatus.getSelectionModel().getSelectedItem();

        overtimeRequests = FXCollections.observableList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getOvertimeRequestService()
                        .getOvertimeRequestsByEmployeeId(
                                employee, month, year, status
                        )
        );

        // Add pagination functionality

        int itemsPerPage = 25;
        int pageCount = (int) Math.ceil((double) overtimeRequests.size() / itemsPerPage);
        paginationOvertimeRequest.setPageCount(pageCount);

        paginationOvertimeRequest.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    public void populateMonths() {
        this.cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
        LocalDateTime localDateTime = LocalDateTime.now();
        this.cbMonth.getSelectionModel().select(localDateTime.getMonth());
    }

    public void populateYears() {
        this.cbYear.setItems(FXCollections.observableArrayList(
                this.getEmployeeRoleNavigationController().getMainViewController().getOvertimeRequestService().getOvertimeRequestYears(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee()
                )
        ));
        // select current year
        cbYear.getSelectionModel().selectFirst();
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, overtimeRequests.size());
        List<OvertimeRequest> pageData = overtimeRequests.subList(fromIndex, toIndex);
        tvOvertime.setItems(FXCollections.observableList(pageData));
    }

    public void setupOvertimeTableView() {
        TableColumn<OvertimeRequest, Void> actionsColumn = createActionsColumn();
        this.tvOvertime.getColumns().add(actionsColumn);
    }

    private TableColumn<OvertimeRequest, Void> createActionsColumn() {
        TableColumn<OvertimeRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = createUpdateButton();
            private final Button deleteButton = createDeleteButton();
            private final HBox actionsBox = new HBox(updateButton, deleteButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                /**
                 * Create Update On Action
                 */

                updateButton.setOnAction(event -> {
                    OvertimeRequest selectedOvertimeRequest = getTableView().getItems().get(getIndex());
                    if (selectedOvertimeRequest != null) {
                        if (selectedOvertimeRequest.getStatus().equals("Requested")) {
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/update-overtime-request.fxml"));
                                Parent root = fxmlLoader.load();

                                Stage stage = new Stage();
                                stage.setTitle("Edit Overtime Request");
                                stage.setScene(new Scene(root));
                                stage.show();

                                ModifyOvertimeRequestController modifyOvertimeRequestController = fxmlLoader.getController();
                                modifyOvertimeRequestController.setOvertimeController(OvertimeController.this);
                                modifyOvertimeRequestController.setOvertimeRequest(selectedOvertimeRequest);
                                modifyOvertimeRequestController.setupComponents();
                                modifyOvertimeRequestController.setTableViewIndex(getIndex());
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            errorPendingLeaveRequestOnly();
                        }
                    }
                });


                deleteButton.setOnAction(event -> {

                    OvertimeRequest selectedLeaveRequest = getTableView().getItems().get(getIndex());
                    if (selectedLeaveRequest.getStatus().equals("Requested")) {
                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Delete overtime request", "Are you sure you want to delete this overtime request?");
                        Optional<ButtonType> result = customAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            employeeRoleNavigationController
                                    .getMainViewController()
                                    .getOvertimeRequestService()
                                    .deleteOvertimeRequest(selectedLeaveRequest);
                            tvOvertime.getItems().remove(selectedLeaveRequest);
                        }
                    } else {
                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Leave request can't be deleted", "Leave request is already processed.");
                        customAlert.showAndWait();
                    }
                });
            }

            private void errorPendingLeaveRequestOnly() {
                CustomAlert customAlert = new CustomAlert(
                        Alert.AlertType.ERROR, "Overtime request can't modify", "Leave request already processed"
                );
                customAlert.showAndWait();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }

    private Button createUpdateButton() {
        Button updateButton = new Button(null, new FontIcon(Feather.PEN_TOOL));
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        return updateButton;
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button(null, new FontIcon(Feather.TRASH_2));
        deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
        return deleteButton;
    }
}
