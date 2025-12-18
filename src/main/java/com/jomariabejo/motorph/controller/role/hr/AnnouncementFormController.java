package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.model.Announcement;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AnnouncementFormController {

    private AnnouncementController announcementController;
    private Announcement announcement;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextArea taContent;

    @FXML
    private DatePicker dpAnnouncementDate;

    @FXML
    private ComboBox<Employee> cbCreatedBy;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (validateFields()) {
            if (announcement == null) {
                createAnnouncement();
            } else {
                updateAnnouncement();
            }
        }
    }

    public void setup() {
        populateEmployees();
        
        if (announcement != null) {
            loadAnnouncementData();
        } else {
            dpAnnouncementDate.setValue(LocalDate.now());
            // Set current logged-in employee as creator
            Employee currentEmployee = announcementController.getHumanResourceAdministratorNavigationController()
                    .getMainViewController().getEmployee();
            if (currentEmployee != null) {
                cbCreatedBy.getSelectionModel().select(currentEmployee);
            }
        }
    }

    private void populateEmployees() {
        List<Employee> employees = announcementController.getHumanResourceAdministratorNavigationController()
                .getMainViewController().getServiceFactory().getEmployeeService().getAllEmployees();
        cbCreatedBy.setItems(FXCollections.observableList(employees));
        cbCreatedBy.setCellFactory(param -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
        cbCreatedBy.setButtonCell(new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(employee.getEmployeeNumber() + " - " + employee.getFirstName() + " " + employee.getLastName());
                }
            }
        });
    }

    private void loadAnnouncementData() {
        tfTitle.setText(announcement.getTitle());
        taContent.setText(announcement.getContent());
        dpAnnouncementDate.setValue(announcement.getAnnouncementDate());
        cbCreatedBy.getSelectionModel().select(announcement.getCreatedByEmployee());
    }

    private boolean validateFields() {
        if (tfTitle.getText().isEmpty() ||
                taContent.getText().isEmpty() ||
                dpAnnouncementDate.getValue() == null ||
                cbCreatedBy.getSelectionModel().getSelectedItem() == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all fields."
            );
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void createAnnouncement() {
        Announcement newAnnouncement = new Announcement();
        mapFieldsToAnnouncement(newAnnouncement);
        
        announcementController.getHumanResourceAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getAnnouncementService().saveAnnouncement(newAnnouncement);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Announcement created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        announcementController.populateAnnouncements();
    }

    private void updateAnnouncement() {
        mapFieldsToAnnouncement(announcement);
        
        announcementController.getHumanResourceAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getAnnouncementService().updateAnnouncement(announcement);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Announcement updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        announcementController.populateAnnouncements();
    }

    private void mapFieldsToAnnouncement(Announcement announcement) {
        announcement.setTitle(tfTitle.getText());
        announcement.setContent(taContent.getText());
        announcement.setAnnouncementDate(dpAnnouncementDate.getValue());
        announcement.setCreatedByEmployee(cbCreatedBy.getSelectionModel().getSelectedItem());
    }
}










