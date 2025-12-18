package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
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
    private TextArea tfDescription;

    @FXML
    void initialize() {
        customizeButtons();
        setupValidation();
        // Set default date to today
        dpReimbursementDate.setValue(LocalDate.now());
    }

    private void customizeButtons() {
        FontIcon cancelIcon = new FontIcon(Feather.X);
        cancelBtn.setGraphic(cancelIcon);
        cancelBtn.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);

        FontIcon submitIcon = new FontIcon(Feather.CHECK);
        submitBtn.setGraphic(submitIcon);
        submitBtn.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
    }

    private void setupValidation() {
        // Amount field - numeric only with decimal support
        tfAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                tfAmount.setText(oldValue);
            }
        });

        // Description character counter
        tfDescription.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue != null ? newValue.length() : 0;
            if (length < 50) {
                tfDescription.setStyle("-fx-border-color: #dc3545;");
            } else {
                tfDescription.setStyle("-fx-border-color: #28a745;");
            }
        });
    }

    @FXML
    void cancelBtnClicked(ActionEvent event) {
        if (dpReimbursementDate != null && dpReimbursementDate.getScene() != null) {
            dpReimbursementDate.getScene().getWindow().hide();
        }
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        // Validate form
        if (!validateForm()) {
            return;
        }

        // Display request confirmation with details
        StringBuilder confirmMessage = new StringBuilder();
        confirmMessage.append("Please review your reimbursement request:\n\n");
        confirmMessage.append("Date: ").append(dpReimbursementDate.getValue()).append("\n");
        confirmMessage.append("Amount: ₱").append(String.format("%,.2f", new BigDecimal(tfAmount.getText()))).append("\n");
        confirmMessage.append("Description: ").append(tfDescription.getText().substring(0, Math.min(100, tfDescription.getText().length())));
        if (tfDescription.getText().length() > 100) {
            confirmMessage.append("...");
        }
        confirmMessage.append("\n\nDo you wish to submit this request?");

        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION,
                "Confirm Reimbursement Request",
                confirmMessage.toString()
        );
        Optional<ButtonType> buttonTypeOptional = customAlert.showAndWait();
        
        if (buttonTypeOptional.isPresent() && buttonTypeOptional.get() == ButtonType.OK) {
            try {
                ReimbursementRequest request = mapComponentsToReimbursement();
                this.getReimbursementController().getEmployeeRoleNavigationController()
                        .getMainViewController().getServiceFactory()
                        .getReimbursementRequestService().saveRequest(request);

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Request Submitted",
                        "Your reimbursement request has been submitted successfully!\n\n" +
                        "Request ID: " + request.getReimbursementRequestId() + "\n" +
                        "Status: " + request.getStatus() + "\n\n" +
                        "You will be notified once it is reviewed."
                );
                successAlert.showAndWait();

                // Close the form window
                if (tfAmount != null && tfAmount.getScene() != null) {
                    tfAmount.getScene().getWindow().hide();
                }
            } catch (Exception e) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Submission Failed",
                        "Failed to submit reimbursement request: " + e.getMessage() + 
                        "\n\nPlease try again or contact support if the problem persists."
                );
                errorAlert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    private boolean validateForm() {
        // Validate date
        if (dpReimbursementDate.getValue() == null) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select a reimbursement date."
            );
            errorAlert.showAndWait();
            dpReimbursementDate.requestFocus();
            return false;
        }

        // Validate amount
        if (tfAmount.getText() == null || tfAmount.getText().trim().isEmpty()) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter an amount."
            );
            errorAlert.showAndWait();
            tfAmount.requestFocus();
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(tfAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Validation Error",
                        "Amount must be greater than zero."
                );
                errorAlert.showAndWait();
                tfAmount.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a valid amount (numbers only)."
            );
            errorAlert.showAndWait();
            tfAmount.requestFocus();
            return false;
        }

        // Validate description
        String description = tfDescription.getText();
        if (description == null || description.trim().isEmpty()) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please provide a description for your reimbursement request."
            );
            errorAlert.showAndWait();
            tfDescription.requestFocus();
            return false;
        }

        if (description.trim().length() < 50) {
            displayRequestDescriptionShouldBeAtleast50();
            tfDescription.requestFocus();
            return false;
        }

        return true;
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
        reimbursementRequest.setEmployeeNumber(
                this.getReimbursementController().getEmployeeRoleNavigationController()
                        .getMainViewController().getEmployee()
        );
        reimbursementRequest.setAmount(new BigDecimal(tfAmount.getText().trim()));
        reimbursementRequest.setDescription(tfDescription.getText().trim());
        reimbursementRequest.setRequestDate(Date.valueOf(dpReimbursementDate.getValue()));
        reimbursementRequest.setStatus("Pending"); // Changed from "Submitted" to "Pending" to match admin view
        return reimbursementRequest;
    }

    // Deprecated - validation is now handled in initialize()
    @Deprecated
    public void acceptNumericValuesOnlyForAmount() {
        // Method kept for backward compatibility but functionality moved to setupValidation()
    }

    /**
     * 1. When I clicked the reimbursement, it should display submitted reimbursement
     */
}
