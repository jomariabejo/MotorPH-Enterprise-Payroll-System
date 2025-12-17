package com.jomariabejo.motorph.controller.role.systemadministrator;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.model.Announcement;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private Button btnCancel;

    @FXML
    private Button btnSubmit;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        btnCancel.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if (announcementController == null || announcementController.getSystemAdministratorNavigationController() == null) {
            return;
        }

        if (!announcementController.getSystemAdministratorNavigationController()
                .requirePermission(PermissionConstants.SYSTEM_ADMIN_ANNOUNCEMENTS_MANAGE, (announcement == null) ? "create announcements" : "edit announcements")) {
            return;
        }

        if (validateFields()) {
            if (announcement == null) {
                createAnnouncement();
            } else {
                updateAnnouncement();
            }
        }
    }

    public void setup() {
        if (announcement != null) {
            loadAnnouncementData();
        } else {
            dpAnnouncementDate.setValue(LocalDate.now());
        }
    }

    private void loadAnnouncementData() {
        tfTitle.setText(announcement.getTitle());
        taContent.setText(announcement.getContent());
        dpAnnouncementDate.setValue(announcement.getAnnouncementDate());
    }

    private boolean validateFields() {
        if (tfTitle.getText().isEmpty() ||
                taContent.getText().isEmpty() ||
                dpAnnouncementDate.getValue() == null) {
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

        // Set creator to currently logged-in employee where available
        Employee currentEmployee = announcementController.getSystemAdministratorNavigationController()
                .getMainViewController().getEmployee();
        if (currentEmployee != null) {
            newAnnouncement.setCreatedByEmployee(currentEmployee);
        }

        announcementController.getSystemAdministratorNavigationController()
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

        // If creator is missing, set it to current employee
        if (announcement.getCreatedByEmployee() == null) {
            Employee currentEmployee = announcementController.getSystemAdministratorNavigationController()
                    .getMainViewController().getEmployee();
            if (currentEmployee != null) {
                announcement.setCreatedByEmployee(currentEmployee);
            }
        }

        announcementController.getSystemAdministratorNavigationController()
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
    }
}


