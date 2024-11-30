package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class LeaveRequestController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    @FXML
    private Button btnFileLeaveRequest;

    @FXML
    private ComboBox<String> cbLeaveStatus;

    @FXML
    private Pagination paginationLeaveRequests;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private TableView<LeaveRequest> tvLeaveRequests;

    @FXML
    void addNewEmployeeClicked(ActionEvent event) {

    }

    @FXML
    void searchBtnClicked(ActionEvent event) {

    }

    @FXML
    void leaveStatusChanged(ActionEvent event) {
        populateLeaveRequestsBySelectedStatus();
    }

    @FXML
    private void initialize() {
        setupTableViewOnAction();
    }

    public void setup() {
        customizeFileLeaveButton(); // add icon to button
        populateLeaveRequestsBySelectedStatus(); // add data to tableview
        automateSearchBar();
    }


    private void setupTableViewOnAction() {
        TableColumn<LeaveRequest, Void> actionsColumn = createActionsColumn();
        this.tvLeaveRequests.getColumns().add(actionsColumn);
    }

    private TableColumn<LeaveRequest, Void> createActionsColumn() {
        TableColumn<LeaveRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifydBtn = createModifyBtn();
            private final Button deleteBtn = createDeleteBtn();

            private Button createModifyBtn() {
                FontIcon fontIcon = new FontIcon(Feather.EDIT);
                Button updateButton = new Button(null, fontIcon);
                updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return updateButton;
            }


            private Button createDeleteBtn() {
                FontIcon fontIcon = new FontIcon(Feather.TRASH_2);
                Button deleteButton = new Button(null, fontIcon);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                return deleteButton;
            }



            private final HBox actionsBox = new HBox(modifydBtn
                    ,deleteBtn
            );

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                setGraphic(actionsBox);

                modifydBtn.setOnAction(event -> {
//                        try {
//                            Employee selectedEmployee = getTableView().getItems().get(getIndex());
//
//                            System.out.println("Include modify employee form");
//                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource
//                                    ("/com/jomariabejo/motorph/role/human-resource/modify-employee.fxml"));
//
//                            Parent root = fxmlLoader.load();
//                            Stage stage = new Stage();
//                            stage.initModality(Modality.APPLICATION_MODAL);
//                            stage.initStyle(StageStyle.DECORATED);
//                            stage.setTitle("Modify Employee.");
//                            stage.setScene(new Scene(root));
//                            stage.show();
//
//                            HumanResourceModifyEmployeeController humanResourceModifyEmployeeController = fxmlLoader.getController();
//                            humanResourceModifyEmployeeController.injectEmployee(selectedEmployee);
//                            humanResourceModifyEmployeeController.setEmployeeController(EmployeeController.this);
//                        } catch (RuntimeException e) {
//                            throw new RuntimeException(e);
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                });

//                deleteBtn.setOnAction(event -> {
//                    Employee selectedEmployee = getTableView().getItems().get(getIndex());
//
//                    CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Employee removal confirmation","Are you sure you want to delete this employee?");
//                    Optional<ButtonType> result = customAlert.showAndWait();
//                    if (result.isPresent() && result.get() == ButtonType.OK) {
//                        this.getTableView().getItems().remove(selectedEmployee);
//                        this.getTableView().refresh();
//                        humanResourceAdministratorNavigationController.getMainViewController().getServiceFactory().getEmployeeService().deleteEmployee(selectedEmployee);
//                        CustomAlert alertSuccess = new CustomAlert(Alert.AlertType.INFORMATION, "Employee successfully removed.", "Employee record has been removed.");
//                        alertSuccess.showAndWait();
//                    }
//                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;

    }

    private void customizeFileLeaveButton() {
        FontIcon fontIcon = new FontIcon(Feather.USER_PLUS);
        btnFileLeaveRequest.setGraphic(fontIcon);
        btnFileLeaveRequest.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void populateLeaveRequestsBySelectedStatus() {
        tvLeaveRequests.setItems(
                FXCollections.observableList(
                        this.getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getLeaveRequestService()
                                .fetchLeaveRequestsByStatus(cbLeaveStatus.getSelectionModel().getSelectedItem())
                )
        );
    }

    private void automateSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            applySearchText(newValue);
        });
    }

    private void applySearchText(String searchedText) {
        if (searchedText.isEmpty()) {
            populateLeaveRequestsBySelectedStatus();
        }
        else {
            String lowerCaseSearchText = searchedText.toLowerCase();
            // Assuming you have a repository method or service to fetch the filtered list
            List<LeaveRequest> searchResults = this.getHumanResourceAdministratorNavigationController().getMainViewController().getServiceFactory().getLeaveRequestService()
                    .findLeaveRequestsByEmployeeNameOrEmployeeNumber(lowerCaseSearchText);

            ObservableList<LeaveRequest> searchResultNew = FXCollections.observableArrayList(searchResults);
            tvLeaveRequests.setItems(searchResultNew);
        }
    }


    private ObservableList<LeaveRequest> fetchLeaveRequests() {
        return FXCollections.observableArrayList(
                this.humanResourceAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory()
                        .getLeaveRequestService()
                        .getAllLeaveRequests()
        );
    }

}
