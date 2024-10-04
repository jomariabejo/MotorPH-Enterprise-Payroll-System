package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.LeaveRequestType;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class LeaveHistoryController {

    public LeaveHistoryController() {
    }

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Pagination paginationLeaveRequests;

    @FXML
    private TableView<LeaveRequest> tvLeaveRequests;

    @FXML
    private ComboBox<Month> cbRequestedMonth;

    @FXML
    private ComboBox<Integer> cbRequestedYear;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ComboBox<LeaveRequestType> cbLeaveType;

    private ObservableList<LeaveRequest> leaveRequestList;

    @FXML
    public void comboboxMonthClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxYearClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxLeaveTypeClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxStatusClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    public void setup() {
        populateMonths();
        populateYears();
        populateLeaveTypes();
        setupComboBox();
        populateLeaveRequests();
    }

    public void populateMonths() {
        cbRequestedMonth.setItems(FXCollections.observableArrayList(Month.values()));
    }

    public void populateYears() {
        cbRequestedYear.setItems(FXCollections.observableList(
                this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveRequestService().getYearsOfLeaveRequestOfEmployee(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee()
                ).get()
        ));
    }

    public void populateLeaveRequests() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        String month = cbRequestedMonth.getSelectionModel().getSelectedItem() != null ? cbRequestedMonth.getSelectionModel().getSelectedItem().toString() : "";
        String year = cbRequestedYear.getSelectionModel().getSelectedItem() != null ? cbRequestedYear.getSelectionModel().getSelectedItem().toString() : "";
        String status = cbStatus.getSelectionModel().getSelectedItem() != null ? cbStatus.getSelectionModel().getSelectedItem().toString() : "";
        String leaveType = cbLeaveType.getSelectionModel().getSelectedItem() != null ? cbLeaveType.getSelectionModel().getSelectedItem().toString() : "";

        List<LeaveRequest> allLeaveRequests = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getLeaveRequestService()
                .fetchLeaveRequestsForEmployee(employee, month, year, status, leaveType);

        leaveRequestList = FXCollections.observableList(allLeaveRequests);

        int itemsPerPage = 25;
        int pageCount = (int) Math.ceil((double) leaveRequestList.size() / itemsPerPage);
        paginationLeaveRequests.setPageCount(pageCount);

        paginationLeaveRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, leaveRequestList.size());
        List<LeaveRequest> pageData = leaveRequestList.subList(fromIndex, toIndex);
        tvLeaveRequests.setItems(FXCollections.observableList(pageData));
    }

    public void populateLeaveTypes() {
        cbLeaveType.setItems(FXCollections.observableList(
                this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getLeaveRequestTypeService().getAllLeaveRequestTypes()
        ));
    }

    public void setupComboBox() {
        LocalDate today = LocalDate.now();
        cbRequestedMonth.getSelectionModel().select(today.getMonth());
        cbRequestedYear.getSelectionModel().selectFirst();
        cbStatus.getSelectionModel().select("Approved");
        cbLeaveType.getSelectionModel().selectFirst();
    }

    private void setUpTableView() {
        TableColumn<LeaveRequest, Void> actionsColumn = createActionsColumn();
        this.tvLeaveRequests.getColumns().add(actionsColumn);
    }

    private TableColumn<LeaveRequest, Void> createActionsColumn() {
        TableColumn<LeaveRequest, Void> actionsColumn = new TableColumn<>("Actions");
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
                    LeaveRequest selectedLeaveRequest = getTableView().getItems().get(getIndex());
                    if (selectedLeaveRequest != null) {
                        if (selectedLeaveRequest.getStatus().equals("Pending")) {
                            System.out.println("Selected Leave Request is = " + selectedLeaveRequest.toString());
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/update-leave-request.fxml"));
                                Parent root = fxmlLoader.load();

                                Stage stage = new Stage();
                                stage.setTitle("Edit Leave Request");
                                stage.setScene(new Scene(root));
                                stage.show();

                                ModifyLeaveRequestController fileLeaveRequestController = fxmlLoader.getController();
                                fileLeaveRequestController.setLeaveHistoryController(LeaveHistoryController.this);
                                fileLeaveRequestController.setLeaveRequest(selectedLeaveRequest);
                                fileLeaveRequestController.setupComponents();
                                fileLeaveRequestController.setTableViewIndex(getIndex());

                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else {
                            errorPendingLeaveRequestOnly();
                        }
                    }
                });


                deleteButton.setOnAction(event -> {

                    LeaveRequest selectedLeaveRequest = getTableView().getItems().get(getIndex());
                    if (selectedLeaveRequest.getStatus().equals("Pending")) {
                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Delete leave request", "Are you sure you want to delete this leave request?");
                        Optional<ButtonType> result = customAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            employeeRoleNavigationController
                                    .getMainViewController()
                                    .getServiceFactory()
                                    .getLeaveRequestService()
                                    .deleteLeaveRequest(selectedLeaveRequest);
                            tvLeaveRequests.getItems().remove(selectedLeaveRequest);
                        }


                    } else {
                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Leave request can't be deleted", "Leave request is already processed.");
                        customAlert.showAndWait();
                    }
                });
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


    private void errorPendingLeaveRequestOnly() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR, "Leave request can't modify", "Leave request already processed"
        );
        customAlert.showAndWait();
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button(null, new FontIcon(Feather.TRASH_2));
        deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
        return deleteButton;
    }

    @FXML
    private void initialize() {
        setUpTableView();
    }


}
