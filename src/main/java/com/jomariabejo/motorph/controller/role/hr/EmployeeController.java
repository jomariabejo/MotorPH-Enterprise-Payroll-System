package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.util.List;
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
        // Default constructor
    }

    @FXML
    private Button btnAddNewEmployee;

    @FXML
    private Button btnInitializeLeaveBalances;

    @FXML
    private Pagination paginationEmployees;

    @FXML
    private TableView<Employee> tvEmployees;

    @FXML
    private ComboBox<String> cbStatusFilter;

    private ObservableList<Employee> allEmployeesList;
    private static final int ITEMS_PER_PAGE = 25;
    private static final String STATUS_INACTIVE = "INACTIVE";
    private static final String STATUS_REGULAR = "REGULAR";

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
            addNewEmployeeController.setupAfterControllerSet();
            addNewEmployeeController.addIcons();
            addNewEmployeeController.addButtonColor();
            FontIcon fontIcon = new FontIcon(Material.SECURITY);
            fontIcon.setIconSize(150);
            formStage.showAndWait();
            // Refresh table after adding new employee
            populateEmployees();
        } catch (IOException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    ERROR_TITLE,
                    "Failed to open add employee form: " + e.getMessage()
            );
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    private void setupTableView() {
        // Add action buttons in our tableview
        TableColumn<Employee, Void> actionsColumn = createActionsColumn();
        this.tvEmployees.getColumns().add(actionsColumn);
    }

    private void customizeAddNewEmployeeButton() {
        FontIcon fontIcon = new FontIcon(Feather.USER_PLUS);
        btnAddNewEmployee.setGraphic(fontIcon);
        btnAddNewEmployee.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void customizeInitializeLeaveBalancesButton() {
        FontIcon fontIcon = new FontIcon(Feather.CALENDAR);
        btnInitializeLeaveBalances.setGraphic(fontIcon);
        btnInitializeLeaveBalances.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
    }

    @FXML
    void initializeLeaveBalancesClicked() {
        // Show confirmation dialog
        );
        
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Get all employees
                List<Employee> employees = this.getHumanResourceAdministratorNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getEmployeeService()
                        .getAllEmployees();
                
                // Initialize leave balances for all employees
                this.getHumanResourceAdministratorNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getLeaveBalanceService()
                        .initializeLeaveBalancesForAllEmployees(employees);
                
                // Show success message
                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Leave balances have been initialized for all employees."
                );
                successAlert.showAndWait();
                
            } catch (Exception e) {
                // Show error message
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "An error occurred while initializing leave balances:\n" + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void populateEmployees() {
        // Get all employees
        List<Employee> allEmployees = this.getHumanResourceAdministratorNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getEmployeeService()
                .getAllEmployees();

        // Filter by status if filter is selected
        String selectedStatus = cbStatusFilter.getSelectionModel().getSelectedItem();
        if (selectedStatus != null && !selectedStatus.equals("All")) {
            if ("Active".equals(selectedStatus)) {
                allEmployees = allEmployees.stream()
                        .filter(e -> !STATUS_INACTIVE.equalsIgnoreCase(e.getStatus()))
                        .collect(Collectors.toList());
            } else if ("Inactive".equals(selectedStatus)) {
                allEmployees = allEmployees.stream()
                        .filter(e -> STATUS_INACTIVE.equalsIgnoreCase(e.getStatus()))
                        .collect(Collectors.toList());
            }
        }

        allEmployeesList = FXCollections.observableList(allEmployees);

        // Setup pagination
        setupPagination();
    }

    private void setupPagination() {
        if (allEmployeesList == null || allEmployeesList.isEmpty()) {
            paginationEmployees.setPageCount(0);
            tvEmployees.setItems(FXCollections.observableArrayList());
            return;
        }

        int pageCount = (int) Math.ceil((double) allEmployeesList.size() / ITEMS_PER_PAGE);
        paginationEmployees.setPageCount(pageCount > 0 ? pageCount : 1);

        paginationEmployees.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, ITEMS_PER_PAGE);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        if (allEmployeesList == null || allEmployeesList.isEmpty()) {
            tvEmployees.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allEmployeesList.size());
        
        if (fromIndex >= allEmployeesList.size()) {
            tvEmployees.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Employee> pageData = allEmployeesList.subList(fromIndex, toIndex);
        tvEmployees.setItems(FXCollections.observableList(pageData));
    }

    private void setupStatusFilter() {
        cbStatusFilter.setItems(FXCollections.observableArrayList("All", "Active", "Inactive"));
        cbStatusFilter.getSelectionModel().selectFirst(); // Select "All" by default
    }

    public void setup() {
        setupTableView(); // create action columns
        customizeAddNewEmployeeButton(); // add icon to button
        customizeInitializeLeaveBalancesButton(); // add icon to initialize button
    }

    private TableColumn<Employee, Void> createActionsColumn() {
        TableColumn<Employee, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(300);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = createViewButton();
            private final Button editBtn = createEditButton();
            private final Button deleteBtn = createDeleteButton();
            private final Button reactivateBtn = createReactivateButton();

            private Button createViewButton() {
                Button btn = new Button(null, new FontIcon(Feather.EYE));
                btn.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
                return btn;
            }

            private Button createEditButton() {
                Button btn = new Button(null, new FontIcon(Feather.EDIT));
                btn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return btn;
            }

            private Button createDeleteButton() {
                Button btn = new Button(null, new FontIcon(Feather.TRASH));
                btn.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                return btn;
            }

            private Button createReactivateButton() {
                Button btn = new Button(null, new FontIcon(Feather.USER_CHECK));
                btn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return btn;
            }

            private HBox actionsBox;

            {
                viewBtn.setOnAction(event -> {
                    Employee selectedEmployee = getTableView().getItems().get(getIndex());
                    if (selectedEmployee != null) {
                        viewEmployee(selectedEmployee);
                    }
                });

                editBtn.setOnAction(event -> {
                    Employee selectedEmployee = getTableView().getItems().get(getIndex());
                    if (selectedEmployee != null) {
                        editEmployee(selectedEmployee);
                    }
                });

                deleteBtn.setOnAction(event -> {
                    Employee selectedEmployee = getTableView().getItems().get(getIndex());
                    if (selectedEmployee != null) {
                        deleteEmployee(selectedEmployee);
                    }
                });

                reactivateBtn.setOnAction(event -> {
                    Employee selectedEmployee = getTableView().getItems().get(getIndex());
                    if (selectedEmployee != null) {
                        reactivateEmployee(selectedEmployee);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }

                Employee employee = getTableView().getItems().get(getIndex());
                boolean isInactive = employee != null && STATUS_INACTIVE.equalsIgnoreCase(employee.getStatus());

                // Show reactivate button for inactive employees, delete button for active employees
                if (isInactive) {
                    actionsBox = new HBox(viewBtn, editBtn, reactivateBtn);
                } else {
                    actionsBox = new HBox(viewBtn, editBtn, deleteBtn);
                }

                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private static final String ERROR_TITLE = "Error";

    private void viewEmployee(Employee employee) {
        String details = """
            Employee Number: %d
            Name: %s %s
            Birthday: %s
            Date Hired: %s
            Address: %s
            Phone Number: %s
            Position: %s
            Status: %s
            SSS Number: %s
            Philhealth Number: %s
            TIN Number: %s
            Pagibig Number: %s
            Basic Salary: %s
            Hourly Rate: %s
            """.formatted(
                employee.getEmployeeNumber(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getBirthday(),
                employee.getDateHired(),
                employee.getAddress(),
                employee.getPhoneNumber(),
                employee.getPositionID() != null ? employee.getPositionID().getPositionName() : "N/A",
                employee.getStatus(),
                employee.getSSSNumber(),
                employee.getPhilhealthNumber(),
                employee.getTINNumber(),
                employee.getPagibigNumber(),
                employee.getBasicSalary(),
                employee.getHourlyRate()
            );

        CustomAlert viewAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Employee Details",
                details
        );
        viewAlert.showAndWait();
    }

    private void editEmployee(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/human-resource/add-employee.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setTitle("Edit Employee");
            formStage.setScene(new Scene(formPane));

            HumanResourceAddNewEmployeeController editEmployeeController = loader.getController();
            editEmployeeController.setEmployeeController(this);
            editEmployeeController.setupAfterControllerSet();
            editEmployeeController.populateFormWithEmployee(employee);
            editEmployeeController.addIcons();
            editEmployeeController.addButtonColor();
            
            formStage.showAndWait();
            // Refresh table after editing
            populateEmployees();
        } catch (IOException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    ERROR_TITLE,
                    "Failed to open edit form: " + e.getMessage()
            );
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    private void deleteEmployee(Employee employee) {
        String confirmMessage = """
            Are you sure you want to deactivate employee: %s %s (Employee #%d)?
            
            This will set the employee status to INACTIVE.
            """.formatted(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmployeeNumber()
            );
        
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Deactivate Employee",
                confirmMessage
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Set status to INACTIVE (soft delete)
                employee.setStatus(STATUS_INACTIVE);
                this.getHumanResourceAdministratorNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getEmployeeService()
                        .updateEmployee(employee);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Employee Deactivated",
                        "Employee " + employee.getFirstName() + " " + employee.getLastName() + 
                        " has been successfully deactivated."
                );
                successAlert.showAndWait();
                
                // Refresh table
                populateEmployees();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        ERROR_TITLE,
                        "Failed to deactivate employee: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void reactivateEmployee(Employee employee) {
        String confirmMessage = """
            Are you sure you want to reactivate employee: %s %s (Employee #%d)?
            
            This will set the employee status to REGULAR.
            """.formatted(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmployeeNumber()
            );
        
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reactivate Employee",
                confirmMessage
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Set status to REGULAR (reactivate)
                employee.setStatus(STATUS_REGULAR);
                this.getHumanResourceAdministratorNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getEmployeeService()
                        .updateEmployee(employee);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Employee Reactivated",
                        "Employee " + employee.getFirstName() + " " + employee.getLastName() + 
                        " has been successfully reactivated."
                );
                successAlert.showAndWait();
                
                // Refresh table
                populateEmployees();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        ERROR_TITLE,
                        "Failed to reactivate employee: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

}
