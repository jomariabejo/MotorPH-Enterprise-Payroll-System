package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.enums.EmployeeStatus;
import com.jomariabejo.motorph.service.AllowanceService;
import com.jomariabejo.motorph.service.EmployeeService;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.CSVReaderUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HRHomeController {

    @FXML
    private ComboBox cb_employment_status;

    @FXML
    private TableView<Employee> tv_employee;

    @FXML
    private TextField tf_searchField;

    private final EmployeeService employeeService = new EmployeeService();

    private final AllowanceService allowanceService = new AllowanceService();

    @FXML
    private void initialize() {
        try {
            setUpEmployeesTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void setUpEmployeesTableView() throws SQLException {
        tv_employee.setItems(null); // clear data employee table view
        ObservableList<Employee> employees = FXCollections.observableArrayList();

        switch (cb_employment_status.getSelectionModel().getSelectedItem().toString()) {
            case "Active":
                employees.addAll(employeeService.fetchActiveEmployees());
                break;
            case "Inactive":
                employees.addAll(employeeService.fetchInactiveEmployees());
                break;
        }


        // Define the custom cell for the actions column
        TableColumn<Employee, Void> actionsColumn = new TableColumn<>("Actions");
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
                    Employee employee = getTableView().getItems().get(getIndex());
                    // Handle edit action here ⚠️

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-profile.fxml"));

                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Editing " + employee.getEmployeeId() + " record");
                        stage.setScene(new Scene(root));
                        stage.show();

                        HRViewEmployeeProfile employeeProfile = fxmlLoader.getController();
                        employeeProfile.initData(employee.getEmployeeId());
                        employeeProfile.buttonVisible();

                    } catch (IOException | SQLException e) {
                        throw new RuntimeException(e);
                    } finally {
                        refreshTable();
                    }

                });

                viewButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-profile.fxml"));

                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Viewing " + employee.getEmployeeId() + " record");
                        stage.setScene(new Scene(root));
                        stage.show();

                        HRViewEmployeeProfile employeeProfile = fxmlLoader.getController();
                        employeeProfile.initData(employee.getEmployeeId());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
                deleteButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    // Handle delete action here
                    String customHeader = "Confirm Deletion";
                    String customContent = "Are you sure you want to delete employee " + employee.getFirstName() + " " + employee.getLastName() + "?";
                    // Ask for confirmation before deleting an employee
                    boolean isDeletionConfirmed = AlertUtility.showConfirmation(
                            customContent, // Message asking if the user wants to delete the employee
                            customHeader, // Title of the confirmation dialog
                            "Once confirmed, the employee will be removed from the database."
                    );

                    if (isDeletionConfirmed) {
                        employeeService.setEmployeeInactive(employee.getEmployeeId());
                        String title = employee.getFirstName() + " " + employee.getLastName() + " deleted successfully";
                        String header = "Deletion Confirmation";
                        String content = "The employee has been successfully deleted from the database.";
                        AlertUtility.showInformation(title, header, content);
                        refreshTable();
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

        tv_employee.getColumns().add(actionsColumn);

        tv_employee.setItems(employees);
    }

    private void refreshTable() {
        tv_employee.setItems(null);
        try {
            EmployeeService employeeService = new EmployeeService();
            ArrayList<Employee> employeeArrayList = new ArrayList<>();

            switch (cb_employment_status.getSelectionModel().getSelectedItem().toString()) {
                case "Active":
                    employeeArrayList = employeeService.fetchActiveEmployees();
                    break;
                case "Inactive":
                    employeeArrayList = employeeService.fetchInactiveEmployees();
                    break;
            }
            ObservableList<Employee> employees = FXCollections.observableArrayList(employeeArrayList);

            tv_employee.setItems(employees);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void searching(ActionEvent event) {
        try {
            int searchedEmployeeId = Integer.parseInt(tf_searchField.getText().trim());
            String employmentStatus = cb_employment_status.getSelectionModel().getSelectedItem().toString();
            Employee employee = null;

            if ("Active".equals(employmentStatus)) {
                employee = employeeService.fetchActiveEmployee(searchedEmployeeId);
            } else if ("Inactive".equals(employmentStatus)) {
                employee = employeeService.fetchInActiveEmployee(searchedEmployeeId);
            }

            if (employee != null) {
                tv_employee.setItems(FXCollections.observableArrayList(employee));
            } else {
                AlertUtility.showErrorAlert("Search not found", "Employee Id: " + searchedEmployeeId, "Employee with id " + searchedEmployeeId + " not found");
                refreshTable();
            }
        } catch (NumberFormatException e) {
            if (tf_searchField.getText().isEmpty()) {
                refreshTable();
            }
            AlertUtility.showErrorAlert("Invalid employee id", null, "Invalid employee id " + tf_searchField.getText());
        }
    }


    public void employeementIsActiveChanged(ActionEvent event) {
        refreshTable();
    }

    public void searchBtnClicked(ActionEvent event) {
        searching(event);
    }

    public void addNewEmployeeClicked(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-profile.fxml"));

            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Adding new employee record");
            stage.setScene(new Scene(root));
            stage.show();

            HRViewEmployeeProfile employeeProfile = fxmlLoader.getController();
            employeeProfile.buttonVisible();
            employeeProfile.generateEmployeeIdNumber();
            employeeProfile.setComboBox();
            employeeProfile.editClicked(event);

        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            refreshTable();
        }
    }

    public void addMultipleEmployeeClicked() {
        try {
            String csvPath;
            csvPath = AlertUtility.showPathInputDialog();
            boolean fileExist = checkIfExist(csvPath);
            boolean columnSizeShouldBe20 = CSVReaderUtility.readHeaders(Path.of(csvPath)).size() == 20;
            System.out.println("Size sample: " + CSVReaderUtility.readHeaders(Path.of(csvPath)).size());
            ArrayList<String> defaultHeaders = getStrings();


            // Normalize and trim headers
            List<String> normalizedDefaultHeaders = normalizeAndTrimHeaders(defaultHeaders);
            List<String> normalizedCSVHeaders = normalizeAndTrimHeaders(CSVReaderUtility.readHeaders(Path.of(csvPath)));

            // Compare
            if (normalizedDefaultHeaders.equals(normalizedCSVHeaders)) {
                System.out.println("Headers match!");
            } else {
                System.out.println("Headers do not match.");
            }

            if (!fileExist) {
                throw new FileNotFoundException();
            } else if (!columnSizeShouldBe20) {
                AlertUtility.showErrorAlert
                        ("Invalid csv path",
                                "The csv that you have entered has invalid column size",
                                "Please make sure that you're using the 'multi-employee-registration-template.csv'");
            } else if (!normalizedDefaultHeaders.equals(normalizedCSVHeaders)) {
                AlertUtility.showErrorAlert(
                        "Headers do not match",
                        "Please try again.",
                        "Please make sure that you follow the format of 'multi-employee-registration-template.csv'");
            } else {
                saveEmployees(CSVReaderUtility.readAllLines(Path.of(csvPath)));
            }
        } catch (IOException | CsvException e) {
            AlertUtility.showErrorAlert(e.getMessage(), "File not found", e.getLocalizedMessage());
        }
    }

    private static ArrayList<String> getStrings() {
        ArrayList<String> defaultHeaders = new ArrayList<>();
        defaultHeaders.add("Employee #");
        defaultHeaders.add("Last Name");
        defaultHeaders.add("First Name");
        defaultHeaders.add("Birthday");
        defaultHeaders.add("Address");
        defaultHeaders.add("Phone Number");
        defaultHeaders.add("SSS #");
        defaultHeaders.add("Philhealth #");
        defaultHeaders.add("TIN #");
        defaultHeaders.add("Pag-ibig #");
        defaultHeaders.add("Status");
        defaultHeaders.add("Position");
        defaultHeaders.add("Immediate Supervisor");
        defaultHeaders.add("Basic Salary");
        defaultHeaders.add("Rice Subsidy");
        defaultHeaders.add("Phone Allowance");
        defaultHeaders.add("Clothing Allowance");
        defaultHeaders.add("Gross Semi-monthly Rate");
        defaultHeaders.add("Hourly Rate");
        defaultHeaders.add("Department Name");
        return defaultHeaders;
    }

    private void saveEmployees(List<String[]> employeeList) {
        employeeList.remove(0); // remove headers
        try {
            for (String[] data : employeeList) {
                Employee newEmployee = mapToEmployee(data);
                Allowance allowance = mapToAllowance(data);
                employeeService.saveEmployeeWithProvidedEmployeeId(newEmployee);
                allowanceService.createAllowance(allowance);
            }
            AlertUtility.showInformation("Success", "Employee saved successfully", String.valueOf(employeeList.size()) + " employees added." );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Allowance mapToAllowance(String[] data) {
        Allowance allowance = new Allowance();
        allowance.setEmployeeID(Integer.parseInt(data[0]));
        allowance.setRiceAllowance(Integer.parseInt(data[14].replace(",","")));
        allowance.setClothingAllowance(Integer.parseInt(data[15].replace(",","")));
        allowance.setPhoneAllowance(Integer.parseInt(data[16].replace(",","")));
        allowance.setTotalAmount(allowance.getRiceAllowance() + allowance.getClothingAllowance() + allowance.getPhoneAllowance());
        allowance.setDateCreated(DateUtility.getDate());
        allowance.setDateModified(Timestamp.from(Instant.now()));
        return allowance;
    }

    private Employee mapToEmployee(String[] data) {
        Employee employee = new Employee();
        employee.setEmployeeId(Integer.parseInt(data[0]));
        employee.setLastName(data[1]);
        employee.setFirstName(data[2]);
        employee.setBirthday(switchToYYYYMMDD(data[3]));
        employee.setAddress(data[4]);
        employee.setContactNumber(data[5]);
        employee.setSss(data[6]);
        employee.setPhilhealth(data[7]);
        employee.setTin(data[8]);
        employee.setPagibig(data[9]);
        employee.setStatus(EmployeeStatus.valueOf(data[10]));
        employee.setPositionId(switchPositionNameToPositionId(data[11]));
        employee.setDeptId(switchDepartmentNameToDepartmentId(data[19]));
        employee.setSupervisor(data[12]);
        employee.setBasicSalary(BigDecimal.valueOf(Long.parseLong(data[13].replace(",",""))));
        employee.setDateHired(DateUtility.getDate());
        employee.setGrossSemiMonthlyRate(BigDecimal.valueOf(Float.parseFloat(data[17].replace(",",""))));
        employee.setHourlyRate(BigDecimal.valueOf(Float.parseFloat(data[18].replace(",",""))));
        return employee;
    }

    private int switchDepartmentNameToDepartmentId(String departmentName) {
        System.out.println("My department name is : " + departmentName);
        String query = "SELECT department.dept_id FROM department WHERE department.name = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)
        ) {
            pstmt.setString(1, departmentName);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0; // not found
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int switchPositionNameToPositionId(String positionName) {
        String query = "SELECT position.position_id FROM position WHERE position.name = ?";

        try (Connection connection = DatabaseConnectionUtility.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)
        ) {
            pstmt.setString(1, positionName);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else return 0; // not found
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Date switchToYYYYMMDD(String datum) {
        String[] date = datum.split("/");
        short year = Short.parseShort(date[2]);
        byte month = Byte.parseByte(date[0]);
        byte day = Byte.parseByte(date[1]);

        // Correct way to format the date as "YYYY-MM-DD"
        String formattedDate = String.format("%04d-%02d-%02d", year, month, day);

        // Return the java.sql.Date object
        return Date.valueOf(formattedDate);
    }

    private static List<String> normalizeAndTrimHeaders(List<String> headers) {
        return headers.stream()
                .map(header -> header == null ? "" : header.trim().toLowerCase())
                .collect(Collectors.toList());
    }

    private boolean checkIfExist(String csvPath) {
        return CSVReaderUtility.checkIfExist(csvPath);
    }
}