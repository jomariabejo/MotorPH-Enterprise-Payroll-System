package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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
    private ComboBox<String> cbSorter;

    @FXML
    private Button btnAddNewEmployee;

    @FXML
    private Pagination paginationEmployees;

    @FXML
    private TableView<Employee> tvEmployees;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    void addNewEmployeeClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/human-resource/add-employee.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle("Adding New Employee");
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
        automateSearchBar();
        sorter(cbSorter.getSelectionModel());
    }

    private void sorter(SingleSelectionModel<String> selectionModel) {
        String sortBy = (selectionModel.getSelectedItem());
        switch (sortBy) {
            case "Last Name":
                sortByLastName();
                break;
            case "Age":
                sortByAge();
                break;
            case "Salary":
                sortBySalary();
                break;
            case "Department":
                sortByDepartment();
                break;

        }
    }

    private void sortByDepartment() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(tvEmployees.getItems());

        ObservableList<Employee> sortedEmployeeByDepartment = FXCollections.observableArrayList(
                employeeList.stream()
                        .sorted(Comparator.comparing(
                                employee ->
                                        employee.getPositionID().getDepartmentID().getDepartmentName())) // Sorting by department name
                        .collect(Collectors.toList()) // Collect into a list, then convert to ObservableList
        );

        tvEmployees.setItems(sortedEmployeeByDepartment); // Set the sorted list to the TableView
    }


    private void sortBySalary() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(tvEmployees.getItems());

        ObservableList<Employee> sortedEmployeeBySalary = FXCollections.observableArrayList(
                employeeList.stream()
                        .sorted(Comparator.comparing(Employee::getBasicSalary))
                        .collect(Collectors.toList())
        );

        tvEmployees.setItems(sortedEmployeeBySalary);
    }

    private void sortByAge() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(tvEmployees.getItems());

        // Sort by birthday (oldest to youngest, i.e., the earliest birthday comes first)
        ObservableList<Employee> sortedAgeList = FXCollections.observableArrayList(
                employeeList.stream()
                        .sorted(Comparator.comparing(Employee::getBirthday)) // Sorting by birthday, earliest first
                        .collect(Collectors.toList()) // Collect into a list, then convert to ObservableList
        );

        tvEmployees.setItems(sortedAgeList); // Set the sorted list to the TableView
    }

    private void sortByLastName() {
        ObservableList<Employee> employeeList = FXCollections.observableArrayList(tvEmployees.getItems());
        ObservableList<Employee> sortedList = FXCollections.observableArrayList(
                employeeList.stream()
                        .sorted(Comparator.comparing(Employee::getLastName))
                        .collect(Collectors.toList()) // Collect into a list first, then convert to ObservableList
        );
        tvEmployees.setItems(sortedList); // Set the sorted list to the TableView
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

    private ObservableList<Employee> getEmployees() {
        return FXCollections.observableArrayList(
                this.humanResourceAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory()
                        .getEmployeeService()
                        .getAllEmployees()
        );
    }


    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchText = searchBar.getText().toLowerCase(); // Convert search text to lowercase for case-insensitive comparison

        if (searchText.isEmpty()) {
            displayPleaseProvidesearchInput();
        } else {
            applySearchText(searchText);
        }
    }


    private void applySearchText(String searchedText) {
        ObservableList<Employee> searchResultNew = FXCollections.observableArrayList(
                getEmployees().stream()
                        .filter(employee ->
                                employee.getFirstName().toLowerCase().contains(searchedText) ||
                                        employee.getLastName().toLowerCase().contains(searchedText) ||
                                        String.valueOf(employee.getEmployeeNumber()).contains(searchedText)
                        )
                        .collect(Collectors.toList())
        );
        tvEmployees.setItems(searchResultNew);
    }


    private void displayPleaseProvidesearchInput() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Blank searched text", "Please provide search input");
        customAlert.showAndWait();
    }

    public void automateSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            applySearchText(newValue);
        });
    }

    @FXML
    public void sortEvent(ActionEvent actionEvent) {
        sorter(cbSorter.getSelectionModel());
    }
}
