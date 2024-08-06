package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LeaveHistoryController {

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
                this.getEmployeeRoleNavigationController().getMainViewController().getLeaveRequestService().getYearsOfLeaveRequestOfEmployee(
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
                this.getEmployeeRoleNavigationController().getMainViewController().getLeaveRequestTypeService().getAllLeaveRequestTypes()
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
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_CIRCLE);

        updateButton.setOnAction(event -> {
            // Get the current TableCell
            TableCell<LeaveRequest, Void> cell = (TableCell<LeaveRequest, Void>) ((Button) event.getSource()).getParent().getParent();
            int selectedIndex = cell.getIndex(); // Get the index of the row
            LeaveRequest selectedLeaveRequest = tvLeaveRequests.getSelectionModel().select(); // Get the LeaveRequest from the row

            if (selectedLeaveRequest != null) {
                tvLeaveRequests.getSelectionModel().select(selectedIndex); // Select the row
                if (selectedLeaveRequest.getStatus().equals("Pending")) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/file-leave-request.fxml"));
                        Parent root = fxmlLoader.load();

                        Stage stage = new Stage();
                        stage.setTitle("Edit Leave Request");
                        stage.setScene(new Scene(root));
                        stage.show();

                        FileLeaveRequestController fileLeaveRequestController = fxmlLoader.getController();
                        fileLeaveRequestController.mapLeaveRequest(selectedLeaveRequest);
                        fileLeaveRequestController.setEmployeeRoleNavigationController(this.getEmployeeRoleNavigationController());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    errorPendingLeaveRequestOnly();
                }
            } else {
                CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Leave request not found", "Please select leave request.");
                customAlert.showAndWait();
            }
        });

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
        deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_CIRCLE);
        deleteButton.setOnAction(event -> {
            // Your delete logic here
            deleteButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_CIRCLE);

            var noBtn = new Button("No");
            noBtn.setDefaultButton(true);
            var yesBtn = new Button("YES");

            Notification notification = new Notification("Are you sure you want to delete this leave request?", new FontIcon(Feather.TRASH_2));
            notification.getStyleClass().addAll(Styles.ELEVATED_4, Styles.ACCENT);
            notification.setOnClose(e -> {
                Animations.flash(notification).playFromStart();
            });
            notification.setPrimaryActions(yesBtn, noBtn);
            notification.setSecondaryActions(new MenuItem("Item 1"), new MenuItem("Item 2"));

            yesBtn.setOnAction(y -> {
                System.out.println("Yes clicked");
                Animations.flash(notification).playFromStart();
            });

            noBtn.setOnAction(n -> {
                System.out.println("No clicked");
                Animations.flash(notification).playFromStart();
            });
        });

        return deleteButton;
    }

    @FXML
    private void initialize() {
        setUpTableView();
    }
}
