package com.jomariabejo.motorph.controller.role.accounting;

import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.model.ReimbursementTransaction;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReimbursementTransactionFormController {

    private ReimbursementTransactionController transactionController;
    private ReimbursementTransaction transaction;

    @FXML
    private ComboBox<ReimbursementRequest> cbRequest;

    @FXML
    private DatePicker dpTransactionDate;

    @FXML
    private TextField tfAmount;

    @FXML
    private TextField tfPaidTo;

    @FXML
    private ComboBox<String> cbPaymentMethod;

    @FXML
    private TextArea taDetails;

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
            if (transaction == null) {
                createTransaction();
            } else {
                updateTransaction();
            }
        }
    }

    public void setup() {
        populateRequests();
        populatePaymentMethods();
        
        if (transaction != null) {
            loadTransactionData();
        } else {
            dpTransactionDate.setValue(LocalDate.now());
            cbPaymentMethod.getSelectionModel().select("Bank Transfer");
        }
    }

    private void populateRequests() {
        List<ReimbursementRequest> requests = transactionController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory().getReimbursementRequestService().getAllRequests();
        cbRequest.setItems(FXCollections.observableList(requests));
        cbRequest.setCellFactory(param -> new ListCell<ReimbursementRequest>() {
            @Override
            protected void updateItem(ReimbursementRequest request, boolean empty) {
                super.updateItem(request, empty);
                if (empty || request == null) {
                    setText(null);
                } else {
                    setText("ID: " + request.getReimbursementRequestId() + 
                            " - " + request.getAmount() + 
                            " (" + request.getStatus() + ")");
                }
            }
        });
        cbRequest.setButtonCell(new ListCell<ReimbursementRequest>() {
            @Override
            protected void updateItem(ReimbursementRequest request, boolean empty) {
                super.updateItem(request, empty);
                if (empty || request == null) {
                    setText(null);
                } else {
                    setText("ID: " + request.getReimbursementRequestId() + 
                            " - " + request.getAmount() + 
                            " (" + request.getStatus() + ")");
                }
            }
        });
    }

    private void populatePaymentMethods() {
        cbPaymentMethod.setItems(FXCollections.observableArrayList(
                "Bank Transfer", "Check", "Cash", "PayPal", "Other"
        ));
    }

    private void loadTransactionData() {
        cbRequest.getSelectionModel().select(transaction.getRequestID());
        dpTransactionDate.setValue(transaction.getTransactionDate());
        tfAmount.setText(transaction.getAmount().toString());
        tfPaidTo.setText(transaction.getPaidTo());
        if (transaction.getPaymentMethod() != null) {
            cbPaymentMethod.getSelectionModel().select(transaction.getPaymentMethod());
        }
        if (transaction.getDetails() != null) {
            taDetails.setText(transaction.getDetails());
        }
    }

    private boolean validateFields() {
        if (cbRequest.getSelectionModel().getSelectedItem() == null ||
                dpTransactionDate.getValue() == null ||
                tfAmount.getText().isEmpty() ||
                tfPaidTo.getText().isEmpty() ||
                cbPaymentMethod.getSelectionModel().getSelectedItem() == null) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please fill in all required fields."
            );
            alert.showAndWait();
            return false;
        }

        try {
            new BigDecimal(tfAmount.getText());
        } catch (NumberFormatException e) {
            CustomAlert alert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter a valid amount."
            );
            alert.showAndWait();
            return false;
        }

        return true;
    }

    private void createTransaction() {
        ReimbursementTransaction newTransaction = new ReimbursementTransaction();
        mapFieldsToTransaction(newTransaction);
        
        transactionController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getReimbursementTransactionService().saveReimbursementTransaction(newTransaction);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Reimbursement transaction created successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        transactionController.populateTransactions();
    }

    private void updateTransaction() {
        mapFieldsToTransaction(transaction);
        
        transactionController.getPayrollAdministratorNavigationController()
                .getMainViewController().getServiceFactory()
                .getReimbursementTransactionService().updateReimbursementTransaction(transaction);

        CustomAlert alert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Success",
                "Reimbursement transaction updated successfully."
        );
        alert.showAndWait();

        btnSubmit.getScene().getWindow().hide();
        transactionController.populateTransactions();
    }

    private void mapFieldsToTransaction(ReimbursementTransaction transaction) {
        transaction.setRequestID(cbRequest.getSelectionModel().getSelectedItem());
        transaction.setTransactionDate(dpTransactionDate.getValue());
        transaction.setAmount(new BigDecimal(tfAmount.getText()));
        transaction.setPaidTo(tfPaidTo.getText());
        transaction.setPaymentMethod(cbPaymentMethod.getSelectionModel().getSelectedItem());
        transaction.setDetails(taDetails.getText().isEmpty() ? null : taDetails.getText());
    }
}

