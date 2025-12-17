package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.model.Conversation;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class NewConversationFormController {

    private ConversationController conversationController;
    private Employee currentEmployee;
    
    private ObservableList<Employee> allEmployeesList;
    private FilteredList<Employee> filteredEmployeesList;

    @FXML
    private ComboBox<Employee> cbEmployee;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnStart;
    
    @FXML
    private Label lblEmployeeCount;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void startBtnClicked(ActionEvent event) {
        if (validateFields()) {
            createConversation();
        }
    }

    public void setup() {
        populateEmployees();
        setupSearchFunctionality();
        updateEmployeeCount();
    }

    private void populateEmployees() {
        // Get all employees except current employee
        List<Employee> allEmployees = conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory().getEmployeeService().getAllEmployees();
        
        List<Employee> otherEmployees = allEmployees.stream()
                .filter(emp -> !emp.getEmployeeNumber().equals(currentEmployee.getEmployeeNumber()))
                .collect(Collectors.toList());

        // Store all employees for filtering
        allEmployeesList = FXCollections.observableArrayList(otherEmployees);
        filteredEmployeesList = new FilteredList<>(allEmployeesList, p -> true);
        
        // Make ComboBox editable for search
        cbEmployee.setEditable(true);
        cbEmployee.setItems(filteredEmployeesList);
        
        // Set prompt text
        cbEmployee.setPromptText("Search by name or employee number...");
        
        // Setup cell factory for dropdown items
        cbEmployee.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(formatEmployeeDisplay(employee));
                }
            }
        });
        
        // Setup button cell (selected item display)
        cbEmployee.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(formatEmployeeDisplay(employee));
                }
            }
        });
        
        // Setup string converter for editable ComboBox
        cbEmployee.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                if (employee == null) {
                    return "";
                }
                return formatEmployeeDisplay(employee);
            }

            @Override
            public Employee fromString(String string) {
                // Find employee by matching the string
                return filteredEmployeesList.stream()
                        .filter(emp -> formatEmployeeDisplay(emp).equalsIgnoreCase(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
    
    private String formatEmployeeDisplay(Employee employee) {
        return employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName();
    }
    
    private void setupSearchFunctionality() {
        // Add listener to filter employees as user types
        cbEmployee.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Show all employees when search is cleared
                filteredEmployeesList.setPredicate(employee -> true);
            } else {
                // Filter employees based on search text
                String searchText = newValue.toLowerCase().trim();
                filteredEmployeesList.setPredicate(employee -> matchesSearch(employee, searchText));
            }
            updateEmployeeCount();
            
            // Show dropdown when typing
            if (!cbEmployee.isShowing() && !newValue.isEmpty()) {
                cbEmployee.show();
            }
        });
        
        // Handle selection change to update the editor text
        cbEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cbEmployee.getEditor() != null) {
                cbEmployee.getEditor().setText(formatEmployeeDisplay(newValue));
            }
        });
    }
    
    private boolean matchesSearch(Employee employee, String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }
        
        // Search by employee number
        String employeeNumber = String.valueOf(employee.getEmployeeNumber());
        if (employeeNumber.toLowerCase().contains(searchText)) {
            return true;
        }
        
        // Search by first name
        if (employee.getFirstName() != null && 
            employee.getFirstName().toLowerCase().contains(searchText)) {
            return true;
        }
        
        // Search by last name
        if (employee.getLastName() != null && 
            employee.getLastName().toLowerCase().contains(searchText)) {
            return true;
        }
        
        // Search by full name (first + last)
        String fullName = (employee.getFirstName() + " " + employee.getLastName()).toLowerCase();
        if (fullName.contains(searchText)) {
            return true;
        }
        
        // Search by last + first name
        String reverseFullName = (employee.getLastName() + " " + employee.getFirstName()).toLowerCase();
        return reverseFullName.contains(searchText);
    }
    
    private void updateEmployeeCount() {
        if (lblEmployeeCount != null) {
            int count = filteredEmployeesList.size();
            if (count == 0) {
                lblEmployeeCount.setText("No employees found");
                lblEmployeeCount.setStyle("-fx-text-fill: red;");
            } else {
                lblEmployeeCount.setText(count + " employee(s) available");
                lblEmployeeCount.setStyle("-fx-text-fill: gray;");
            }
        }
    }

    private boolean validateFields() {
        // Check if an employee is selected
        Employee selectedEmployee = cbEmployee.getSelectionModel().getSelectedItem();
        
        // Also try to find employee from editor text if no selection but text exists
        if (selectedEmployee == null && cbEmployee.getEditor() != null) {
            String editorText = cbEmployee.getEditor().getText();
            if (editorText != null && !editorText.trim().isEmpty()) {
                // Try to find exact match
                Employee foundEmployee = filteredEmployeesList.stream()
                        .filter(emp -> formatEmployeeDisplay(emp).equalsIgnoreCase(editorText.trim()))
                        .findFirst()
                        .orElse(null);
                
                // If exact match found, set it as selected
                if (foundEmployee != null) {
                    cbEmployee.getSelectionModel().select(foundEmployee);
                    selectedEmployee = foundEmployee;
                }
            }
        }
        
        if (selectedEmployee == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select an employee to start a conversation with."
            );
            alert.showAndWait();
            return false;
        }

        // Make final reference for lambda
        final Employee finalSelectedEmployee = selectedEmployee;

        // Check if conversation already exists (selectedEmployee already validated above)
        List<Conversation> existingConversations = conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory().getConversationService().getAllConversations()
                .stream()
                .filter(conv -> {
                    Employee p1 = conv.getParticipant1Employee();
                    Employee p2 = conv.getParticipant2Employee();
                    return (p1 != null && p1.getEmployeeNumber().equals(currentEmployee.getEmployeeNumber()) &&
                            p2 != null && p2.getEmployeeNumber().equals(finalSelectedEmployee.getEmployeeNumber())) ||
                           (p1 != null && p1.getEmployeeNumber().equals(finalSelectedEmployee.getEmployeeNumber()) &&
                            p2 != null && p2.getEmployeeNumber().equals(currentEmployee.getEmployeeNumber()));
                })
                .collect(Collectors.toList());

        if (!existingConversations.isEmpty()) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.INFORMATION,
                    "Conversation Exists",
                    "A conversation with this employee already exists. Opening existing conversation..."
            );
            alert.showAndWait();
            
            // Open existing conversation
            conversationController.openConversation(existingConversations.get(0));
            btnStart.getScene().getWindow().hide();
            return false;
        }

        return true;
    }

    private void createConversation() {
        Employee selectedEmployee = cbEmployee.getSelectionModel().getSelectedItem();
        
        Conversation newConversation = new Conversation();
        newConversation.setParticipant1Employee(currentEmployee);
        newConversation.setParticipant2Employee(selectedEmployee);
        newConversation.setLastMessageTimestamp(Instant.now());
        newConversation.setLastMessageContent("Conversation started");
        newConversation.setUnreadCount(0);

        conversationController.getEmployeeRoleNavigationController()
                .getMainViewController().getServiceFactory()
                .getConversationService().saveConversation(newConversation);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Conversation Created",
                "Conversation created successfully. Opening conversation..."
        );
        alert.showAndWait();

        btnStart.getScene().getWindow().hide();
        conversationController.populateConversations();
        
        // Open the new conversation
        conversationController.openConversation(newConversation);
    }
}





