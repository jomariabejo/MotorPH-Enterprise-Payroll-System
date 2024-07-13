package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.entity.LeaveRequest;
import com.jomariabejo.motorph.repository.LeaveRequestRepository;
import com.jomariabejo.motorph.service.LeaveRequestService;
import com.jomariabejo.motorph.utility.AlertUtility;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class HRLeaveRequestController {
    private final int ROWS_PER_PAGE = 100;
    private LeaveRequestService leaveRequestService;

    @FXML
    private ComboBox<String> cb_leave_req_status;

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private Label lbl_tv_total_result1;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<Date, LeaveRequest> tc_leaveRequestId;

    @FXML
    private TableColumn<Integer, LeaveRequest> tc_employeeId;

    @FXML
    private TableColumn<Integer, LeaveRequest> tc_leaveRequestCategoryId;

    @FXML
    private TableColumn<Date, LeaveRequest> tc_startDate;

    @FXML
    private TableColumn<Date, LeaveRequest> tc_endDate;

    @FXML
    private TableColumn<LeaveRequest.LeaveRequestStatus, LeaveRequest> tc_status;

    @FXML
    private TextField tf_search;

    @FXML
    private TableView<LeaveRequest> tv_leave_requests;

    @FXML
    void paginationChanged(MouseEvent event) {

    }

    @FXML
    void searchingEmployeeLeaveRequests(ActionEvent event) throws SQLException {
        try {
            int employeeId = (Integer.parseInt(tf_search.getText()));
            try {
                boolean isEmployeeHasLeaveRequests = leaveRequestService.checkIfEmployeeHasLeaveRequestRecords(employeeId, cb_leave_req_status.getSelectionModel().getSelectedItem());

                if (isEmployeeHasLeaveRequests) {
                    tv_leave_requests.setItems(FXCollections.observableArrayList(getLeaveRequestBasedOnSelectedComboBox()));
                    modifyLeaveRequestPagination();
                } else {
                    String title = "No Leave Requests";
                    String header = "Employee's Leave Request List is Empty";
                    String content = "This employee has not submitted any leave requests for the selected leave type (" + cb_leave_req_status.getSelectionModel().getSelectedItem() + ").";
                    AlertUtility.showErrorAlert(title, header, content);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException numberFormatException) {
            if (tf_search.getText().isEmpty()) {
                ObservableList<LeaveRequest> leaveRequests = FXCollections.observableArrayList(getLeaveRequestBasedOnSelectedComboBox());
                tv_leave_requests.setItems(leaveRequests);
                modifyLeaveRequestPagination();
            }
            AlertUtility.showErrorAlert("Employee not found", "Invalid employee number : " + tf_search.getText(), null);
        }
    }

    @FXML
    private void initialize() throws SQLException {
        try {
            setUpTableView();
            setUpLblTotalResult();
            setUpPagination();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public HRLeaveRequestController() {
        this.leaveRequestService = new LeaveRequestService();
    }

    private void setUpTableView() throws SQLException {


        // Define the custom cell for the actions column
        TableColumn<LeaveRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button editButton = new Button();
            final Button viewButton = new Button();
            final Button deleteButton = new Button();

            final HBox actionsBox = new HBox(editButton, viewButton, deleteButton);

            {
                actionsBox.setAlignment(Pos.CENTER); // Align HBox content to center
                actionsBox.setSpacing(5); // Set spacing between buttons

                editButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    // Handle edit action here ⚠️

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/popup/leave_request/leave-req-approval.fxml"));

                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Editing Leave Request Record");
                    stage.setScene(new Scene(root));
                    stage.show();

                    HRLeaveRequestApproval hrLeaveRequestApproval = fxmlLoader.getController();

                    try {
                        hrLeaveRequestApproval.initDate(leaveRequest.getLeaveRequestID());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });
                // Handle view action here ⚠️
                viewButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    // Handle edit action here ⚠️

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/popup/leave_request/leave-req-approval.fxml"));

                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setTitle("Viewing Leave Request Record");
                    stage.setScene(new Scene(root));
                    stage.show();

                    HRLeaveRequestApproval hrLeaveRequestApproval = fxmlLoader.getController();
                    hrLeaveRequestApproval.setButtonHidden();
                    try {
                        hrLeaveRequestApproval.initDate(leaveRequest.getLeaveRequestID());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });


                // Load the edit and view icon images
                Image editIcon = new Image(getClass().getResourceAsStream("/img/modify-icon.png"));
                Image viewIcon = new Image(getClass().getResourceAsStream("/img/view-icon.png"));

                ImageView editIconView = new ImageView(editIcon);
                editIconView.setFitWidth(24);
                editIconView.setFitHeight(24);

                ImageView viewIconView = new ImageView(viewIcon);
                viewIconView.setFitWidth(36);
                viewIconView.setFitHeight(24);


                // Set the icon images as graphics for the edit and view buttons
                // Handle delete action here ⚠️
                deleteButton.setOnAction(event -> {
                    LeaveRequest leaveRequest = getTableView().getItems().get(getIndex());
                    boolean isDeleteLeaveRequestConfirmed = AlertUtility.showConfirmation("Remove Leave Request Confirmation", "Are you sure you want to remove the leave request?", null);

                    if (isDeleteLeaveRequestConfirmed) {
                        leaveRequestService.deleteLeaveRequest(leaveRequest.getLeaveRequestID());
                        try {
                            tv_leave_requests.setItems(FXCollections.observableArrayList(getLeaveRequestBasedOnSelectedComboBox()));
                            setUpLblTotalResult();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                // Load the delete icon image
                Image deleteIcon = new Image(getClass().getResourceAsStream("/img/delete-icon.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(24);
                deleteIconView.setFitHeight(24);

                // Set the delete icon as the graphic for the delete button
                editButton.setGraphic(editIconView);
                viewButton.setGraphic(viewIconView);
                deleteButton.setGraphic(deleteIconView);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });

        tv_leave_requests.getColumns().add(actionsColumn);

        tv_leave_requests.setItems(FXCollections.observableArrayList(getLeaveRequestBasedOnSelectedComboBox()));
    }

    private void setUpPagination() throws SQLException {
        pagination.setPageCount(leaveRequestService.countLeaveRequestPage(cb_leave_req_status.getSelectionModel().getSelectedItem()));
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(10);
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            loadLeaveRequestForPage(newIndex.intValue());
        });
    }

    private void loadLeaveRequestForPage(int currentPageIndex) {
        try {
            tv_leave_requests.setItems(FXCollections.observableList(leaveRequestService.fetchLeaveRequestForPage(currentPageIndex, ROWS_PER_PAGE, (cb_leave_req_status.getSelectionModel().getSelectedItem()))));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly in your application
        }
    }

    private void setUpLblTotalResult() throws SQLException {
        lbl_tv_total_result.setText(String.valueOf(leaveRequestService.fetchLeaveRequestForPage(
                        pagination.getCurrentPageIndex(),
                        ROWS_PER_PAGE,
                        (cb_leave_req_status.getSelectionModel().getSelectedItem()))
                .size()));
    }

    public void comboBoxChanged(ActionEvent event) throws SQLException {
        setUpPagination();
        try {
            setUpTableViewViaComboBoxEvent();
            setUpLblTotalResult();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpTableViewViaComboBoxEvent() throws SQLException {
        tv_leave_requests.setItems(null); // clear data leave request table view
        ArrayList<LeaveRequest> leaveRequestArrayList = getLeaveRequestBasedOnSelectedComboBox();

        tv_leave_requests.setItems(FXCollections.observableArrayList(leaveRequestArrayList));
        modifyLeaveRequestPagination();
    }

    private ArrayList<LeaveRequest> getLeaveRequestBasedOnSelectedComboBox() throws SQLException {
        switch (cb_leave_req_status.getSelectionModel().getSelectedItem()) {
            case "Pending":
                return leaveRequestService.fetchLeaveRequestForPage(pagination.getCurrentPageIndex(), ROWS_PER_PAGE, "Pending");
            case "Approved":
                return leaveRequestService.fetchLeaveRequestForPage(pagination.getCurrentPageIndex(), ROWS_PER_PAGE, "Approved");
            case "Disapproved":
                return leaveRequestService.fetchLeaveRequestForPage(pagination.getCurrentPageIndex(), ROWS_PER_PAGE, "Disapproved");
        }
        return null;
    }

    private void modifyLeaveRequestPagination() throws SQLException {
        switch (cb_leave_req_status.getSelectionModel().getSelectedItem()) {
            case "Pending":
                pagination.setPageCount(leaveRequestService.countLeaveRequestPage("Pending"));
                break;
            case "Approved":
                pagination.setPageCount(leaveRequestService.countLeaveRequestPage("Approved"));
                break;
            case "Disapproved":
                pagination.setPageCount(leaveRequestService.countLeaveRequestPage("Disapproved"));
                break;
        }
    }

    public void btnSearchEvent(ActionEvent event) {
        try {
            searchingEmployeeLeaveRequests(event);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
