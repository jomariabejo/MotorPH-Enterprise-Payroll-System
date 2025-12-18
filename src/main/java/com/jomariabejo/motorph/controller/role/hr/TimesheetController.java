package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class TimesheetController {

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    @FXML
    private ComboBox<Employee> cbEmployee;

    @FXML
    private ComboBox<Month> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private DatePicker dpDate;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private TableView<Timesheet> tvTimesheets;

    @FXML
    private Pagination paginationTimesheets;

    private ObservableList<Timesheet> allTimesheetsList;
    private ObservableList<Employee> allEmployeesList;
    private static final int ITEMS_PER_PAGE = 25;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TimesheetController() {
        // Default constructor
    }

    @FXML
    void filterChanged() {
        populateTimesheets();
    }

    @FXML
    void initialize() {
        setupTableColumns();
        setupEmployeeComboBox();
        setupMonthComboBox();
        setupYearComboBox();
        setupStatusComboBox();
        setupDatePicker();
    }

    private String getEmployeeNumber(Timesheet timesheet) {
        if (timesheet == null) {
            return "N/A";
        }
        try {
            Employee emp = timesheet.getEmployeeID();
            return emp != null ? String.valueOf(emp.getEmployeeNumber()) : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String getEmployeeName(Timesheet timesheet) {
        if (timesheet == null) {
            return "N/A";
        }
        try {
            Employee emp = timesheet.getEmployeeID();
            if (emp != null) {
                String firstName = emp.getFirstName() != null ? emp.getFirstName() : "";
                String lastName = emp.getLastName() != null ? emp.getLastName() : "";
                String fullName = (firstName + " " + lastName).trim();
                return fullName.isEmpty() ? "N/A" : fullName;
            }
            return "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void setupTableColumns() {
        // Employee Number column
        TableColumn<Timesheet, String> employeeNumberCol = new TableColumn<>("Employee #");
        employeeNumberCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(getEmployeeNumber(cellData.getValue()))
        );
        employeeNumberCol.setPrefWidth(120);

        // Employee Name column (combine first and last name)
        TableColumn<Timesheet, String> employeeNameCol = new TableColumn<>("Employee Name");
        employeeNameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(getEmployeeName(cellData.getValue()))
        );
        employeeNameCol.setPrefWidth(200);

        // Date column
        TableColumn<Timesheet, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDate();
            return new SimpleStringProperty(date != null ? date.format(DATE_FORMATTER) : "");
        });
        dateCol.setPrefWidth(150);

        // Time In column
        TableColumn<Timesheet, String> timeInCol = new TableColumn<>("Time In");
        timeInCol.setCellValueFactory(cellData -> {
            java.sql.Time timeIn = cellData.getValue().getTimeIn();
            return new SimpleStringProperty(timeIn != null ? timeIn.toLocalTime().format(TIME_FORMATTER) : "");
        });
        timeInCol.setPrefWidth(120);

        // Time Out column
        TableColumn<Timesheet, String> timeOutCol = new TableColumn<>("Time Out");
        timeOutCol.setCellValueFactory(cellData -> {
            java.sql.Time timeOut = cellData.getValue().getTimeOut();
            return new SimpleStringProperty(timeOut != null ? timeOut.toLocalTime().format(TIME_FORMATTER) : "");
        });
        timeOutCol.setPrefWidth(120);

        // Hours Worked column
        TableColumn<Timesheet, String> hoursWorkedCol = new TableColumn<>("Hours Worked");
        hoursWorkedCol.setCellValueFactory(cellData -> {
            Float hours = cellData.getValue().getHoursWorked();
            return new SimpleStringProperty(hours != null ? String.format("%.2f", hours) : "0.00");
        });
        hoursWorkedCol.setPrefWidth(130);

        // Status column
        TableColumn<Timesheet, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(150);

        // Remarks column
        TableColumn<Timesheet, String> remarksCol = new TableColumn<>("Remarks");
        remarksCol.setCellValueFactory(cellData -> {
            String remarks = cellData.getValue().getRemarks();
            return new SimpleStringProperty(remarks != null ? remarks : "");
        });
        remarksCol.setPrefWidth(200);

        // Actions column
        TableColumn<Timesheet, Void> actionsCol = createActionsColumn();
        actionsCol.setPrefWidth(250);

        @SuppressWarnings("unchecked")
        TableColumn<Timesheet, String>[] columns = new TableColumn[]{
                employeeNumberCol, employeeNameCol, dateCol, timeInCol, timeOutCol,
                hoursWorkedCol, statusCol, remarksCol
        };
        tvTimesheets.getColumns().setAll(columns);
        tvTimesheets.getColumns().add(actionsCol);
    }

    private TableColumn<Timesheet, Void> createActionsColumn() {
        TableColumn<Timesheet, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(250);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = createViewButton();
            private final Button editBtn = createEditButton();
            private final Button deleteBtn = createDeleteButton();

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

            private final HBox actionsBox = new HBox(viewBtn, editBtn, deleteBtn);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);

                viewBtn.setOnAction(event -> {
                    Timesheet selectedTimesheet = getTableView().getItems().get(getIndex());
                    if (selectedTimesheet != null) {
                        viewTimesheet(selectedTimesheet);
                    }
                });

                editBtn.setOnAction(event -> {
                    Timesheet selectedTimesheet = getTableView().getItems().get(getIndex());
                    if (selectedTimesheet != null) {
                        editTimesheet(selectedTimesheet);
                    }
                });

                deleteBtn.setOnAction(event -> {
                    Timesheet selectedTimesheet = getTableView().getItems().get(getIndex());
                    if (selectedTimesheet != null) {
                        deleteTimesheet(selectedTimesheet);
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
                setGraphic(actionsBox);
            }
        });
        return actionsColumn;
    }

    private static final String ERROR_TITLE = "Error";

    private void viewTimesheet(Timesheet timesheet) {
        String employeeName = getEmployeeName(timesheet);
        String employeeNumber = getEmployeeNumber(timesheet);
        String date = timesheet.getDate() != null ? timesheet.getDate().format(DATE_FORMATTER) : "N/A";
        String timeIn = timesheet.getTimeIn() != null ? timesheet.getTimeIn().toLocalTime().format(TIME_FORMATTER) : "N/A";
        String timeOut = timesheet.getTimeOut() != null ? timesheet.getTimeOut().toLocalTime().format(TIME_FORMATTER) : "N/A";
        String hoursWorked = timesheet.getHoursWorked() != null ? String.format("%.2f", timesheet.getHoursWorked()) : "0.00";
        String status = timesheet.getStatus() != null ? timesheet.getStatus() : "N/A";
        String remarks = timesheet.getRemarks() != null ? timesheet.getRemarks() : "None";

        String details = """
            Employee Number: %s
            Employee Name: %s
            Date: %s
            Time In: %s
            Time Out: %s
            Hours Worked: %s hours
            Status: %s
            Remarks: %s
            """.formatted(
                employeeNumber,
                employeeName,
                date,
                timeIn,
                timeOut,
                hoursWorked,
                status,
                remarks
            );

        CustomAlert viewAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Timesheet Details",
                details
        );
        viewAlert.showAndWait();
    }

    private void editTimesheet(Timesheet timesheet) {
        // Placeholder for edit functionality - will be implemented with edit form/dialog
        // For now, show informational message
        String message = """
            Edit timesheet functionality will be implemented soon.
            
            You can currently view and delete timesheets.
            
            Timesheet Details:
            Employee: %s
            Date: %s
            """.formatted(
                getEmployeeName(timesheet),
                timesheet.getDate() != null ? timesheet.getDate().format(DATE_FORMATTER) : "N/A"
            );
        
        CustomAlert infoAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Edit Timesheet",
                message
        );
        infoAlert.showAndWait();
    }

    private void deleteTimesheet(Timesheet timesheet) {
        String employeeName = getEmployeeName(timesheet);
        String date = timesheet.getDate() != null ? timesheet.getDate().format(DATE_FORMATTER) : "N/A";
        
        String confirmMessage = """
            Are you sure you want to delete this timesheet?
            
            Employee: %s
            Date: %s
            Time In: %s
            Time Out: %s
            
            This action cannot be undone.
            """.formatted(
                employeeName,
                date,
                timesheet.getTimeIn() != null ? timesheet.getTimeIn().toLocalTime().format(TIME_FORMATTER) : "N/A",
                timesheet.getTimeOut() != null ? timesheet.getTimeOut().toLocalTime().format(TIME_FORMATTER) : "N/A"
            );
        
        CustomAlert confirmAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Delete Timesheet",
                confirmMessage
        );
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                humanResourceAdministratorNavigationController
                        .getMainViewController()
                        .getServiceFactory()
                        .getTimesheetService()
                        .deleteTimesheet(timesheet);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "SUCCESS",
                        "Timesheet has been deleted successfully."
                );
                successAlert.showAndWait();
                
                // Refresh table
                populateTimesheets();
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        ERROR_TITLE,
                        "Failed to delete timesheet: " + e.getMessage()
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private void setupEmployeeComboBox() {
        if (humanResourceAdministratorNavigationController == null) {
            return;
        }

        // Get all active employees
        List<Employee> employees = humanResourceAdministratorNavigationController
                .getMainViewController()
                .getServiceFactory()
                .getEmployeeService()
                .getAllEmployees()
                .stream()
                .filter(e -> !"INACTIVE".equalsIgnoreCase(e.getStatus()))
                .toList();

        allEmployeesList = FXCollections.observableArrayList(employees);
        cbEmployee.setItems(allEmployeesList);

        // Set up StringConverter to display employee names
        cbEmployee.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee == null ? "" : employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName();
            }

            @Override
            public Employee fromString(String string) {
                if (string == null || string.isEmpty() || allEmployeesList == null) {
                    return null;
                }
                // Try to find by employee number or name
                return allEmployeesList.stream()
                        .filter(e -> {
                            String display = e.getEmployeeNumber() + " - " + e.getFirstName() + " " + e.getLastName();
                            return display.equalsIgnoreCase(string) || 
                                   display.toLowerCase().contains(string.toLowerCase());
                        })
                        .findFirst()
                        .orElse(null);
            }
        });

        // Add search filtering
        cbEmployee.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                cbEmployee.setItems(allEmployeesList);
            } else {
                ObservableList<Employee> filtered = FXCollections.observableArrayList(
                        allEmployeesList.stream()
                                .filter(e -> {
                                    String display = e.getEmployeeNumber() + " - " + e.getFirstName() + " " + e.getLastName();
                                    return display.toLowerCase().contains(newValue.toLowerCase());
                                })
                                .toList()
                );
                cbEmployee.setItems(filtered);
            }
        });
    }

    private void setupMonthComboBox() {
        cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
    }

    private void setupYearComboBox() {
        if (humanResourceAdministratorNavigationController == null) {
            return;
        }

        List<Integer> years = humanResourceAdministratorNavigationController
                .getMainViewController()
                .getServiceFactory()
                .getTimesheetService()
                .getAllYearsFromTimesheets();

        if (years.isEmpty()) {
            // Add current year if no years found
            years = List.of(Year.now().getValue());
        }

        cbYear.setItems(FXCollections.observableArrayList(years));
    }

    private void setupStatusComboBox() {
        cbStatus.setItems(FXCollections.observableArrayList("All", "Submitted", "Not Submitted"));
        cbStatus.getSelectionModel().selectFirst();
    }

    private void setupDatePicker() {
        // Clear date picker by default
        dpDate.setValue(null);
        // Add listener to handle clearing
        dpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                filterChanged();
            }
        });
    }

    private void populateTimesheets() {
        if (humanResourceAdministratorNavigationController == null) {
            return;
        }

        // Get filter values
        Employee selectedEmployee = cbEmployee.getValue();
        Month selectedMonth = cbMonth.getValue();
        Integer selectedYear = cbYear.getValue();
        LocalDate selectedDate = dpDate.getValue();
        String selectedStatus = cbStatus.getValue();

        Year yearObj = selectedYear != null ? Year.of(selectedYear) : null;

        // Fetch timesheets with filters
        List<Timesheet> timesheets = humanResourceAdministratorNavigationController
                .getMainViewController()
                .getServiceFactory()
                .getTimesheetService()
                .getTimesheetsWithFilters(selectedEmployee, yearObj, selectedMonth, selectedDate, selectedStatus);

        allTimesheetsList = FXCollections.observableList(timesheets);
        setupPagination();
    }

    private void setupPagination() {
        if (allTimesheetsList == null || allTimesheetsList.isEmpty()) {
            paginationTimesheets.setPageCount(0);
            tvTimesheets.setItems(FXCollections.observableArrayList());
            return;
        }

        int pageCount = (int) Math.ceil((double) allTimesheetsList.size() / ITEMS_PER_PAGE);
        paginationTimesheets.setPageCount(pageCount > 0 ? pageCount : 1);

        paginationTimesheets.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, ITEMS_PER_PAGE);
            return new StackPane();
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        if (allTimesheetsList == null || allTimesheetsList.isEmpty()) {
            tvTimesheets.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, allTimesheetsList.size());

        if (fromIndex >= allTimesheetsList.size()) {
            tvTimesheets.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Timesheet> pageData = allTimesheetsList.subList(fromIndex, toIndex);
        tvTimesheets.setItems(FXCollections.observableList(pageData));
    }

    public void setup() {
        setupEmployeeComboBox();
        setupYearComboBox();
        populateTimesheets();
    }
}
