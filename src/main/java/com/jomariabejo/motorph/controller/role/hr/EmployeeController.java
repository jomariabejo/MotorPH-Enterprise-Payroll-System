package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
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

    @FXML
    private void initialize() {
        setupTableViewOnAction();
    }

    private void setupTableViewOnAction() {
        TableColumn<Employee, Void> actionsColumn = createActionsColumn();
        this.tvEmployees.getColumns().add(actionsColumn);
    }

    private TableColumn<Employee, Void> createActionsColumn() {
        TableColumn<Employee, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifydBtn = createModifyBtn();

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

//            private final Button deleteBtn = createDeleteBtn();


            private final HBox actionsBox = new HBox(modifydBtn
//                    ,deleteBtn
            );

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                setGraphic(actionsBox);

                modifydBtn.setOnAction(event -> {
                    try {
                        Employee selectedEmployee = getTableView().getItems().get(getIndex());

                        System.out.println("Include modify employee form");
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource
                                    ("/com/jomariabejo/motorph/role/human-resource/modify-employee.fxml"));

                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(StageStyle.DECORATED);
                            stage.setTitle("Modify Employee.");
                            stage.setScene(new Scene(root));
                            stage.show();

                            HumanResourceModifyEmployeeController humanResourceModifyEmployeeController = fxmlLoader.getController();
                            humanResourceModifyEmployeeController.injectEmployee(selectedEmployee);
                            humanResourceModifyEmployeeController.setEmployeeController(EmployeeController.this);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

}
