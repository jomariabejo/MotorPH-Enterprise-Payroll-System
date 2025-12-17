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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
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
    }

    @FXML
    public void cbYearClicked(ActionEvent actionEvent) {
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
            fileReimbursementRequestController.acceptNumericValuesOnlyForAmount();

            formStage.showAndWait();
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

        // display empty record if record doesn't exist.
        if (tvReimbursementRequest.getItems().isEmpty()) {
            CustomAlert customAlert = new CustomAlert(
                    Alert.AlertType.ERROR,
                    "Empty Record",
                    "You don't have any reimbursement request for " + cbYear.getSelectionModel().getSelectedItem().toString() + ". "
            );
            customAlert.showAndWait();
        }
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
