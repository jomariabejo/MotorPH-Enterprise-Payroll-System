package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.controller.role.employee.EmployeeChangePasswordController;
import com.jomariabejo.motorph.controller.role.employee.LeaveHistoryController;
import com.jomariabejo.motorph.controller.role.employee.ModifyLeaveRequestController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.util.Optional;

/**
 * TODO:
 * 1. UPDATE cellvaluefactory for the tableview ✅
 * 2. Create populate query
 * 3. Add new employee form(different fxml file).
 * 4. Modify employee functionality(Dito parang add new employee pero e iinject natin ang data papunta
 *      sa add new employee form(dapat different form siya para hindi nakakalito).
 * 5. Delete employee functionality(it should not delete the record, but it only make it into inactive state record)
 */
@Getter
@Setter
public class EmployeeController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    public EmployeeController() {
    }

    @FXML
    private Button btnAddNewEmployee;

    @FXML
    private Pagination paginationEmployees;

    @FXML
    private TableView<Employee> tvEmployees;

    @FXML
    void addNewEmployeeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/human-resource/add-employee.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle("Add New Employee");
            formStage.setScene(new Scene(formPane));

            HumanResourceAddNewEmployeeController addNewEmployeeController = loader.getController();
            addNewEmployeeController.setEmployeeController(this);
            addNewEmployeeController.addIcons();
            addNewEmployeeController.addButtonColor();
            FontIcon fontIcon = new FontIcon(Material.SECURITY);
            fontIcon.setIconSize(150);
            formStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupTableView() {
        // Add action buttons in our tableview
    }

    private void customizeAddNewEmployeeButton() {
        FontIcon fontIcon = new FontIcon(Feather.USER_PLUS);
        btnAddNewEmployee.setGraphic(fontIcon);
        btnAddNewEmployee.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

    }

    private void populateEmployees() {
        tvEmployees.setItems(
                FXCollections.observableList(
                        this.getHumanResourceAdministratorNavigationController().getMainViewController().getServiceFactory()
                                .getEmployeeService().getAllEmployees()
                )
        );
    }

    public void setup() {
        setupTableView(); // create action columns
        customizeAddNewEmployeeButton(); // add icon to button
        populateEmployees(); // add data to tableview
    }

    private TableColumn<Employee, Void> createActionsColumn() {
        TableColumn<Employee, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = createUpdateButton();
            private final Button deleteButton = createDeleteButton();

            private Button createUpdateButton() {
                Button updateButton = new Button(null, new FontIcon(Feather.PEN_TOOL));
                updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return updateButton;
            }

            private Button createDeleteButton() {
                Button updateButton = new Button(null, new FontIcon(Feather.USER_MINUS));
                updateButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                return updateButton;
            }

            private final HBox actionsBox = new HBox(updateButton, deleteButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                /**
                 * Create Update On Action
                 */

                updateButton.setOnAction(event -> {
//                    LeaveRequest selectedLeaveRequest = getTableView().getItems().get(getIndex());
//                    if (selectedLeaveRequest != null) {
//                        if (selectedLeaveRequest.getStatus().equals("Pending")) {
//                            System.out.println("Selected Leave Request is = " + selectedLeaveRequest.toString());
//                            try {
//                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/update-leave-request.fxml"));
//                                Parent root = fxmlLoader.load();
//
//                                Stage stage = new Stage();
//                                stage.setTitle("Edit Leave Request");
//                                stage.setScene(new Scene(root));
//                                stage.show();
//
//                                ModifyLeaveRequestController fileLeaveRequestController = fxmlLoader.getController();
//                                fileLeaveRequestController.setLeaveHistoryController(LeaveHistoryController.this);
//                                fileLeaveRequestController.setLeaveRequest(selectedLeaveRequest);
//                                fileLeaveRequestController.setupComponents();
//                                fileLeaveRequestController.setTableViewIndex(getIndex());
//
//                            } catch (IOException ioException) {
//                                ioException.printStackTrace();
//                            }
//                        } else {
//                            errorPendingLeaveRequestOnly();
//                        }
//                    }
                });


                deleteButton.setOnAction(event -> {

//                    LeaveRequest selectedLeaveRequest = getTableView().getItems().get(getIndex());
//                    if (selectedLeaveRequest.getStatus().equals("Pending")) {
//                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Delete leave request", "Are you sure you want to delete this leave request?");
//                        Optional<ButtonType> result = customAlert.showAndWait();
//                        if (result.isPresent() && result.get() == ButtonType.OK) {
//                            employeeRoleNavigationController
//                                    .getMainViewController()
//                                    .getServiceFactory()
//                                    .getLeaveRequestService()
//                                    .deleteLeaveRequest(selectedLeaveRequest);
//                            tvLeaveRequests.getItems().remove(selectedLeaveRequest);
//                        }
//
//
//                    } else {
//                        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Leave request can't be deleted", "Leave request is already processed.");
//                        customAlert.showAndWait();
//                    }
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

    private void setUpTableView() {
        TableColumn<Employee, Void> actionsColumn = createActionsColumn();
        this.tvEmployees.getColumns().add(actionsColumn);
    }

}
