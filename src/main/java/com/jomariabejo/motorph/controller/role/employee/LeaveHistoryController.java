package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.LeaveRequestType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;

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
        addActionButtonsColumn();
    }

    public void populateMonths() {
        ArrayList<Month> months = new ArrayList<>();
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);
        cbRequestedMonth.setItems(FXCollections.observableList(months));
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

        // Set the page factory
        paginationLeaveRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane(); // Return a StackPane but it's not used, only required to satisfy PageFactory
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
        Month month = today.getMonth();
        cbRequestedMonth.getSelectionModel().select(month);
        cbRequestedYear.getSelectionModel().selectFirst();
        cbStatus.getSelectionModel().select("Approved");
        cbLeaveType.getSelectionModel().selectFirst();
    }

    public void paginationClicked(MouseEvent mouseEvent) {
    }

    private void addActionButtonsColumn() {
        TableColumn<LeaveRequest, LeaveRequest> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));

        actionsColumn.setCellFactory(createActionButtonsCellFactory());

        tvLeaveRequests.getColumns().add(actionsColumn);
    }

    private Callback<TableColumn<LeaveRequest, LeaveRequest>, TableCell<LeaveRequest, LeaveRequest>> createActionButtonsCellFactory() {
        return column -> new TableCell<LeaveRequest, LeaveRequest>() {

            private final Button viewButton = new Button("View");
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            {
                viewButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    handleViewAction(leaveRequest);
                });

                updateButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    handleUpdateAction(leaveRequest);
                });

                deleteButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    handleDeleteAction(leaveRequest);
                });

                HBox hbox = new HBox(viewButton, updateButton, deleteButton);
                hbox.setSpacing(5);
                hbox.setPrefWidth(200);
                setGraphic(hbox);
            }

            @Override
            protected void updateItem(LeaveRequest item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : getGraphic());
            }
        };
    }

    private void handleDeleteAction(LeaveRequest leaveRequest) {
    }

    private void handleUpdateAction(LeaveRequest leaveRequest) {

    }

    private void handleViewAction(LeaveRequest leaveRequest) {

    }

}
