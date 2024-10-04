package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

@Getter
@Setter
public class FileReimbursementRequestController {

    private ReimbursementController ReimbursementController;

    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker dpReimbursementDate;

    @FXML
    private Button submitBtn;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfDescription;

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        dpReimbursementDate.getScene().getWindow().hide();
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        // Display request confirmation
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Reimbursement request confirmation",
                "Do you really wish to submit this request for reimbursement?"
        );
        Optional<ButtonType> buttonTypeOptional = customAlert.showAndWait();
        if (buttonTypeOptional.isPresent() && buttonTypeOptional.get() == ButtonType.OK) {
            if (!tfDescription.getText().isEmpty() && tfDescription.getText().length() >= 50) {
                this.getReimbursementController().getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getReimbursementRequestService().saveRequest(mapComponentsToReimbursement());
                // hide the request window
                tfAmount.getScene().getWindow().hide();
            }
            else {
                displayRequestDescriptionShouldBeAtleast50();
            }
        }
        else {
            customAlert.close();
            dpReimbursementDate.getScene().getWindow().hide(); // hide the scene
        }
    }

    private void displayRequestDescriptionShouldBeAtleast50() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Description too short.",
                "Please make sure that your description have at least 50 characters long");
        customAlert.showAndWait();
    }

    private ReimbursementRequest mapComponentsToReimbursement() {
        ReimbursementRequest reimbursementRequest = new ReimbursementRequest();
        try {
            reimbursementRequest.setEmployeeNumber(this.getReimbursementController().getEmployeeRoleNavigationController().getMainViewController().getEmployee()); // the employee that files reimbursement request.
            reimbursementRequest.setAmount(new BigDecimal(tfAmount.getText()));
            reimbursementRequest.setDescription(tfDescription.getText());
            reimbursementRequest.setRequestDate(Date.valueOf(dpReimbursementDate.getValue()));
            reimbursementRequest.setStatus("Submitted");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reimbursementRequest;
    }

    public void acceptNumericValuesOnlyForAmount() {
        tfAmount.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    /**
     * 1. When I clicked the reimbursement, it should display submitted reimbursement
     */
}
