package com.jomariabejo.motorph.controller.role.accounting;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.model.Payroll;
import com.jomariabejo.motorph.model.PayrollTransaction;
import com.jomariabejo.motorph.model.Payslip;
import com.jomariabejo.motorph.service.PayrollService;
import com.jomariabejo.motorph.service.PayrollTransactionService;
import com.jomariabejo.motorph.service.PayslipService;
import com.jomariabejo.motorph.service.ServiceFactory;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PayrollTransactionFormController {

    private PayrollTransactionController payrollTransactionController;
    private PayrollTransaction transaction;
    private ServiceFactory serviceFactory;
    private PayrollTransactionService transactionService;
    private PayrollService payrollService;
    private PayslipService payslipService;

    @FXML
    private ComboBox<Payroll> cbPayroll;
    @FXML
    private CheckBox cbApplyToAll;
    @FXML
    private ComboBox<Payslip> cbPayslip;
    @FXML
    private ComboBox<String> cbTransactionType;
    @FXML
    private ComboBox<String> cbAmountType;
    @FXML
    private TextField tfAmount;
    @FXML
    private Label lblAmountSuffix;
    @FXML
    private DatePicker dpTransactionDate;
    @FXML
    private TextArea taDescription;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    @FXML
    void initialize() {
        customizeButtons();
    }

    public void setup() {
        if (serviceFactory != null) {
            transactionService = serviceFactory.getPayrollTransactionService();
            payrollService = serviceFactory.getPayrollService();
            payslipService = serviceFactory.getPayslipService();
        }

        setupComboBoxes();
        populateForm();
    }

    private void customizeButtons() {
        FontIcon saveIcon = new FontIcon(Feather.SAVE);
        btnSave.setGraphic(saveIcon);
        btnSave.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);

        FontIcon cancelIcon = new FontIcon(Feather.X);
        btnCancel.setGraphic(cancelIcon);
        btnCancel.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
    }

    private void setupComboBoxes() {
        // Setup transaction types
        if (transactionService != null) {
            List<String> types = transactionService.getTransactionTypes();
            cbTransactionType.setItems(FXCollections.observableArrayList(types));
        }

        // Setup amount type
        List<String> amountTypes = List.of("Fixed Amount", "Percentage of Gross Pay", "Percentage of Basic Salary");
        cbAmountType.setItems(FXCollections.observableArrayList(amountTypes));
        cbAmountType.getSelectionModel().selectFirst();
        cbAmountType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateAmountSuffix(newVal));

        // Setup payroll combo box
        if (payrollService != null) {
            List<Payroll> payrolls = payrollService.getAllPayrolls();
            cbPayroll.setItems(FXCollections.observableArrayList(payrolls));
        }

        // Setup payslip combo box (will be filtered by selected payroll)
        cbPayslip.setItems(FXCollections.observableArrayList());

        // Listen to payroll selection to filter payslips
        cbPayroll.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updatePayslipComboBox(newVal);
            if (newVal != null && cbApplyToAll.isSelected()) {
                cbPayslip.setDisable(true);
            }
        });

        // Set default date to today
        dpTransactionDate.setValue(LocalDate.now());
        
        // Initialize amount suffix
        updateAmountSuffix(cbAmountType.getSelectionModel().getSelectedItem());
    }

    private void updateAmountSuffix(String amountType) {
        if (amountType == null) {
            lblAmountSuffix.setText("PHP");
            return;
        }
        
        switch (amountType) {
            case "Fixed Amount":
                lblAmountSuffix.setText("PHP");
                break;
            case "Percentage of Gross Pay":
            case "Percentage of Basic Salary":
                lblAmountSuffix.setText("%");
                break;
            default:
                lblAmountSuffix.setText("PHP");
        }
    }

    @FXML
    void applyToAllChanged() {
        boolean applyToAll = cbApplyToAll.isSelected();
        cbPayslip.setDisable(applyToAll);
        
        if (applyToAll) {
            cbPayslip.getSelectionModel().clearSelection();
        }
    }

    private void updatePayslipComboBox(Payroll payroll) {
        if (payroll == null || payslipService == null) {
            cbPayslip.setItems(FXCollections.observableArrayList());
            return;
        }

        java.util.Optional<java.util.List<Payslip>> payslipsOpt = payslipService.getPayslipsByPayroll(payroll);
        List<Payslip> payslips = payslipsOpt.orElse(java.util.Collections.emptyList());
        cbPayslip.setItems(FXCollections.observableArrayList(payslips));
        
        // If editing and not applying to all, select the payslip
        if (transaction != null && transaction.getPayslipID() != null && !cbApplyToAll.isSelected()) {
            cbPayslip.getSelectionModel().select(transaction.getPayslipID());
        }
    }

    private void populateForm() {
        if (transaction == null) {
            // New transaction - form is already set up with defaults
            // Default to "Apply to all" for new transactions
            cbApplyToAll.setSelected(true);
            cbPayslip.setDisable(true);
            return;
        }

        // Edit mode - populate with existing data
        // Editing single transaction, so disable "Apply to all"
        cbApplyToAll.setSelected(false);
        cbPayslip.setDisable(false);
        
        if (transaction.getPayrollID() != null) {
            cbPayroll.getSelectionModel().select(transaction.getPayrollID());
            updatePayslipComboBox(transaction.getPayrollID());
        }

        if (transaction.getPayslipID() != null) {
            cbPayslip.getSelectionModel().select(transaction.getPayslipID());
        }

        if (transaction.getTransactionType() != null) {
            cbTransactionType.getSelectionModel().select(transaction.getTransactionType());
        }

        if (transaction.getTransactionAmount() != null) {
            // For editing, always show as fixed amount
            cbAmountType.getSelectionModel().select("Fixed Amount");
            tfAmount.setText(String.valueOf(transaction.getTransactionAmount()));
        }

        if (transaction.getTransactionDate() != null) {
            dpTransactionDate.setValue(transaction.getTransactionDate());
        }

        if (transaction.getDescription() != null) {
            taDescription.setText(transaction.getDescription());
        }
    }

    @FXML
    void saveClicked() {
        if (!validateForm()) {
            return;
        }

        try {
            Payroll selectedPayroll = cbPayroll.getSelectionModel().getSelectedItem();
            Payslip selectedPayslip = cbPayslip.getSelectionModel().getSelectedItem();
            String selectedType = cbTransactionType.getSelectionModel().getSelectedItem();
            String amountType = cbAmountType.getSelectionModel().getSelectedItem();
            BigDecimal inputValue = new BigDecimal(tfAmount.getText().trim());
            LocalDate transactionDate = dpTransactionDate.getValue();
            String description = taDescription.getText().trim();
            boolean applyToAll = cbApplyToAll.isSelected();

            if (transaction == null) {
                // Create new transaction(s)
                if (applyToAll) {
                    // Create transaction for all payslips in the payroll
                    java.util.Optional<java.util.List<Payslip>> payslipsOpt = 
                            payslipService != null ? payslipService.getPayslipsByPayroll(selectedPayroll) : 
                            java.util.Optional.empty();
                    List<Payslip> payslips = payslipsOpt.orElse(java.util.Collections.emptyList());
                    
                    if (payslips.isEmpty()) {
                        CustomAlert errorAlert = new CustomAlert(
                                Alert.AlertType.ERROR,
                                "Error",
                                "No payslips found for the selected payroll."
                        );
                        errorAlert.showAndWait();
                        return;
                    }

                    int createdCount = 0;
                    for (Payslip payslip : payslips) {
                        BigDecimal calculatedAmount = calculateAmount(payslip, amountType, inputValue);
                        
                        PayrollTransaction newTransaction = new PayrollTransaction();
                        newTransaction.setPayrollID(selectedPayroll);
                        newTransaction.setPayslipID(payslip);
                        newTransaction.setTransactionType(selectedType);
                        newTransaction.setTransactionAmount(calculatedAmount);
                        newTransaction.setTransactionDate(transactionDate);
                        newTransaction.setDescription(description.isEmpty() ? null : description);

                        if (transactionService != null) {
                            transactionService.savePayrollTransaction(newTransaction);
                            createdCount++;
                        }
                    }

                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            String.format("Successfully created %d transactions for all payslips in payroll #%d.", 
                                    createdCount, selectedPayroll.getId())
                    );
                    successAlert.showAndWait();
                } else {
                    // Create single transaction
                    BigDecimal calculatedAmount = calculateAmount(selectedPayslip, amountType, inputValue);
                    
                    PayrollTransaction newTransaction = new PayrollTransaction();
                    newTransaction.setPayrollID(selectedPayroll);
                    newTransaction.setPayslipID(selectedPayslip);
                    newTransaction.setTransactionType(selectedType);
                    newTransaction.setTransactionAmount(calculatedAmount);
                    newTransaction.setTransactionDate(transactionDate);
                    newTransaction.setDescription(description.isEmpty() ? null : description);

                    if (transactionService != null) {
                        transactionService.savePayrollTransaction(newTransaction);
                    }

                    CustomAlert successAlert = new CustomAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Transaction created successfully."
                    );
                    successAlert.showAndWait();
                }
            } else {
                // Update existing transaction
                BigDecimal calculatedAmount = calculateAmount(selectedPayslip, amountType, inputValue);
                
                transaction.setPayrollID(selectedPayroll);
                transaction.setPayslipID(selectedPayslip);
                transaction.setTransactionType(selectedType);
                transaction.setTransactionAmount(calculatedAmount);
                transaction.setTransactionDate(transactionDate);
                transaction.setDescription(description.isEmpty() ? null : description);

                if (transactionService != null) {
                    transactionService.updatePayrollTransaction(transaction);
                }

                CustomAlert successAlert = new CustomAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Transaction updated successfully."
                );
                successAlert.showAndWait();
            }

            closeWindow();
        } catch (NumberFormatException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Invalid amount format. Please enter a valid number."
            );
            errorAlert.showAndWait();
        } catch (Exception e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to save transaction: " + e.getMessage()
            );
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void cancelClicked() {
        closeWindow();
    }

    private boolean validateForm() {
        if (cbPayroll.getSelectionModel().getSelectedItem() == null) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select a payroll."
            );
            errorAlert.showAndWait();
            return false;
        }

        if (!cbApplyToAll.isSelected() && cbPayslip.getSelectionModel().getSelectedItem() == null) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select a payslip or check 'Apply to all payslips in this payroll'."
            );
            errorAlert.showAndWait();
            return false;
        }

        if (cbTransactionType.getSelectionModel().getSelectedItem() == null) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select a transaction type."
            );
            errorAlert.showAndWait();
            return false;
        }

        String amountType = cbAmountType.getSelectionModel().getSelectedItem();
        if (amountType == null || amountType.isEmpty()) {
            // Ensure default is selected if somehow nothing is selected
            cbAmountType.getSelectionModel().selectFirst();
            amountType = cbAmountType.getSelectionModel().getSelectedItem();
        }

        if (tfAmount.getText() == null || tfAmount.getText().trim().isEmpty()) {
            String fieldName = (amountType != null && amountType.contains("Percentage")) ? "percentage" : "amount";
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please enter an " + fieldName + "."
            );
            errorAlert.showAndWait();
            return false;
        }

        try {
            BigDecimal inputValue = new BigDecimal(tfAmount.getText().trim());
            if (inputValue.compareTo(BigDecimal.ZERO) <= 0) {
                String fieldName = (amountType != null && amountType.contains("Percentage")) ? "Percentage" : "Amount";
                CustomAlert errorAlert = new CustomAlert(
                        Alert.AlertType.ERROR,
                        "Validation Error",
                        fieldName + " must be greater than zero."
                );
                errorAlert.showAndWait();
                return false;
            }
            
            if (amountType != null && amountType.contains("Percentage")) {
                if (inputValue.compareTo(BigDecimal.valueOf(100)) > 0) {
                    CustomAlert errorAlert = new CustomAlert(
                            Alert.AlertType.ERROR,
                            "Validation Error",
                            "Percentage cannot exceed 100%."
                    );
                    errorAlert.showAndWait();
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Invalid amount/percentage format. Please enter a valid number."
            );
            errorAlert.showAndWait();
            return false;
        }

        if (dpTransactionDate.getValue() == null) {
            CustomAlert errorAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Validation Error",
                    "Please select a transaction date."
            );
            errorAlert.showAndWait();
            return false;
        }

        return true;
    }

    private BigDecimal calculateAmount(Payslip payslip, String amountType, BigDecimal inputValue) {
        if (amountType == null || amountType.equals("Fixed Amount")) {
            return inputValue;
        }
        
        if (payslip == null) {
            return inputValue; // Fallback to input value if payslip is null
        }
        
        BigDecimal baseAmount;
        switch (amountType) {
            case "Percentage of Gross Pay":
                baseAmount = payslip.getGrossIncome() != null ? payslip.getGrossIncome() : BigDecimal.ZERO;
                break;
            case "Percentage of Basic Salary":
                // Use monthly rate as basic salary
                baseAmount = payslip.getMonthlyRate() != null ? payslip.getMonthlyRate() : BigDecimal.ZERO;
                break;
            default:
                return inputValue;
        }
        
        // Calculate: (percentage / 100) * baseAmount
        return inputValue.divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP)
                .multiply(baseAmount)
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}

