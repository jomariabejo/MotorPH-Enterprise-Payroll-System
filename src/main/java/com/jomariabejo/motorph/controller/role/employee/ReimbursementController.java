package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.ReimbursementRequest;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.jomariabejo.motorph.utility.PesoUtility;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class ReimbursementController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private ComboBox<?> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<ReimbursementRequest> tvReimbursementRequest;

    @FXML
    private Button fileReimbursement;

    private ObservableList<ReimbursementRequest> reimbursementRequests;

    @FXML
    private void initialize() {
        addIconToFileOvertimeRequestBtn();
        fileReimbursement.getStyleClass().add(Styles.SUCCESS);
        setupTableView();
    }

    private void setupTableView() {
        // Clear existing columns and create custom ones
        tvReimbursementRequest.getColumns().clear();
        createTableColumns();
    }

    private void createTableColumns() {
        // Request ID Column
        TableColumn<ReimbursementRequest, Integer> idColumn = new TableColumn<>("Request ID");
        idColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getReimbursementRequestId()));
        idColumn.setPrefWidth(100);

        // Request Date Column
        TableColumn<ReimbursementRequest, String> dateColumn = new TableColumn<>("Request Date");
        dateColumn.setCellValueFactory(cellData -> {
            Date requestDate = cellData.getValue().getRequestDate();
            if (requestDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(requestDate));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });
        dateColumn.setPrefWidth(120);

        // Amount Column
        TableColumn<ReimbursementRequest, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getAmount() != null
                                ? PesoUtility.formatToPeso(String.valueOf(cellData.getValue().getAmount())) : "₱0.00"));
        amountColumn.setPrefWidth(150);

        // Description Column (truncated)
        TableColumn<ReimbursementRequest, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            if (desc != null && desc.length() > 50) {
                desc = desc.substring(0, 47) + "...";
            }
            return new javafx.beans.property.SimpleStringProperty(desc != null ? desc : "");
        });
        descriptionColumn.setPrefWidth(300);

        // Status Column (with color coding)
        TableColumn<ReimbursementRequest, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> 
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    // Color code status
                    switch (status.toLowerCase()) {
                        case "pending", "submitted":
                            setStyle("-fx-text-fill: #ffc107; -fx-font-weight: bold;");
                            break;
                        case "approved":
                            setStyle("-fx-text-fill: #28a745; -fx-font-weight: bold;");
                            break;
                        case "rejected":
                            setStyle("-fx-text-fill: #dc3545; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        statusColumn.setPrefWidth(120);

        // Processed Date Column
        TableColumn<ReimbursementRequest, String> processedDateColumn = new TableColumn<>("Processed Date");
        processedDateColumn.setCellValueFactory(cellData -> {
            Date processedDate = cellData.getValue().getProcessedDate();
            if (processedDate != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                return new javafx.beans.property.SimpleStringProperty(sdf.format(processedDate));
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        processedDateColumn.setPrefWidth(120);

        // Processed By Column
        TableColumn<ReimbursementRequest, String> processedByColumn = new TableColumn<>("Processed By");
        processedByColumn.setCellValueFactory(cellData -> {
            var processedBy = cellData.getValue().getProcessedBy();
            if (processedBy != null) {
                return new javafx.beans.property.SimpleStringProperty(
                        processedBy.getFirstName() + " " + processedBy.getLastName());
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        processedByColumn.setPrefWidth(150);

        tvReimbursementRequest.getColumns().addAll(idColumn, dateColumn, amountColumn, 
                descriptionColumn, statusColumn, processedDateColumn, processedByColumn);
        tvReimbursementRequest.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void addIconToFileOvertimeRequestBtn() {
        FontIcon icon = new FontIcon(Material.ADD_CIRCLE);
        icon.setIconSize(24);
        fileReimbursement.setGraphic(icon);
    }

    public ReimbursementController() {

    }

    @FXML
    public void cbMonthClicked(ActionEvent actionEvent) {
        // Month filtering can be implemented if needed
        populateTableViewByMyOwnReimbursements();
    }

    @FXML
    public void cbYearClicked(ActionEvent actionEvent) {
        populateTableViewByMyOwnReimbursements();
    }

    @FXML
    public void paginationOnDragDetected(MouseEvent mouseEvent) {
    }

    @FXML
    public void fileReimbursementRequest(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/file-reimbursement.fxml"));
            AnchorPane formPane = loader.load();
            Stage formStage = new Stage();
            formStage.setScene(new Scene(formPane));

            FileReimbursementRequestController fileReimbursementRequestController = loader.getController();
            fileReimbursementRequestController.setReimbursementController(this);

            // Set parent stage and modality
            if (tvReimbursementRequest != null && tvReimbursementRequest.getScene() != null) {
                Stage parentStage = (Stage) tvReimbursementRequest.getScene().getWindow();
                if (parentStage != null) {
                    formStage.initOwner(parentStage);
                }
            }
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.setTitle("File Reimbursement Request");
            
            // Add close request handler
            formStage.setOnCloseRequest(e -> formStage.close());

            formStage.showAndWait();
            
            // Refresh table after form closes
            populateTableViewByMyOwnReimbursements();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * TODO FINISH
     */

    private void populateTableViewByMyOwnReimbursements() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer year = this.cbYear.getSelectionModel().getSelectedItem();

        Optional<List<ReimbursementRequest>> reimbursementOpt = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getReimbursementRequestService()
                .fetchReimbursementByEmployeeIdAndYear(employee, year);

        if (reimbursementOpt.isPresent()) {
            reimbursementRequests = FXCollections.observableList(reimbursementOpt.get());
        } else {
            reimbursementRequests = FXCollections.observableArrayList();
        }
        tvReimbursementRequest.setItems(reimbursementRequests);

        // Don't show error for empty records - just show empty table
        // Users can file new requests if they don't have any
    }

    private void populateYears() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Optional<List<Integer>> years = this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getReimbursementRequestService().fetchEmployeeYearsOfReimbursements(employee);
        
        if (years.isPresent() && !years.get().isEmpty()) {
            cbYear.setItems(FXCollections.observableList(years.get()));
        } else {
            cbYear.getItems().add(Integer.valueOf(String.valueOf(Year.now()))); // ginbutangan ko osa na year bas cool
        }
        cbYear.getSelectionModel().selectFirst();
    }

    public void setup() {
        populateYears();
        populateTableViewByMyOwnReimbursements();
    }
}
