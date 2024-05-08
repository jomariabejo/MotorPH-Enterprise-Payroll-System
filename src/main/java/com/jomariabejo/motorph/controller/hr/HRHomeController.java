package com.jomariabejo.motorph.controller.hr;

import com.jomariabejo.motorph.controller.myprofile.EmployeeProfile;
import com.jomariabejo.motorph.entity.Employee;
import com.jomariabejo.motorph.service.EmployeeService;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HRHomeController {

    @FXML
    private ComboBox cb_employment_status;

    @FXML
    private Button btn_search;

    @FXML
    private Label res_lbl_activeEmployees;

    @FXML
    private Label res_lbl_totalNumberOfEmployees;

    @FXML
    private TableColumn<Integer, Employee> tc_employeeNo;

    @FXML
    private TableColumn<Integer, Employee> tc_firstName;

    @FXML
    private TableColumn<Integer, Employee> tc_lastName;

    @FXML
    private TableView<Employee> tv_employee;

    @FXML
    private TextField tf_searchField;

    private EmployeeService employeeService = new EmployeeService();

    @FXML
    private void initialize() throws SQLException {
        setEmployeeSummaryDashboard();
    }

    /**
     * Sets up the employee summary dashboard.
     * This method updates the total number of employees and active employees count,
     * and sets up the employees table.
     *
     * @throws SQLException if there's an issue accessing the database.
     */
    private void setEmployeeSummaryDashboard() throws SQLException {
        /**
         * Set total number of employees
         */
        int resultEmployeesCount = employeeService.countEmployees();
        res_lbl_totalNumberOfEmployees.setText(String.valueOf(resultEmployeesCount));

        /**
         * Set toal number of inactive employees
         */
        int resultActiveEmployeesCount = employeeService.countActiveEmployees();
        res_lbl_activeEmployees.setText(String.valueOf(resultActiveEmployeesCount));

        /**
         * Set employees table
         */
        setEmployeesTable();

        /**
         * TODO: Add search functionality
         */
    }

    /**
     * Clear employee table
     */
    private void clearActionsTable() {
        // Assuming you have a TableView called employeesTableView

        // Clear existing columns named "Actions"
        List<TableColumn<Employee, ?>> columnsToRemove = new ArrayList<>();
        for (TableColumn<Employee, ?> column : tv_employee.getColumns()) {
            if (column.getText().equals("Actions")) {
                columnsToRemove.add(column);
            }
        }
        tv_employee.getColumns().removeAll(columnsToRemove);

        // Clear TableView items
        tv_employee.getItems().clear();

    }

    private void setEmployeesTable() throws SQLException {
        tv_employee.setItems(null); // clear data employee table view
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

                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Editing " + employee.getEmployeeId() + " record");
                        stage.setScene(new Scene(root));
                        stage.show();

                        EmployeeProfile employeeProfile = fxmlLoader.getController();
                        employeeProfile.initData(employee.getEmployeeId());
                        employeeProfile.buttonVisible();

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    finally {
                        refreshTable();
                    }

                });

                viewButton.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-profile.fxml"));

                        Parent root = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.DECORATED);
                        stage.setTitle("Viewing " + employee.getEmployeeId() + " record");
                        stage.setScene(new Scene(root));
                        stage.show();

                        EmployeeProfile employeeProfile = fxmlLoader.getController();
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
            if(tf_searchField.getText().isEmpty()) {
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

            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Adding new employee record");
            stage.setScene(new Scene(root));
            stage.show();

            EmployeeProfile employeeProfile = fxmlLoader.getController();
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
}