package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.OvertimeRequest;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

@Getter
@Setter
public class OvertimeController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private TableView<OvertimeRequest> tvOvertime;

    @FXML
    private Button overtimeRequestBtn;

    @FXML
    private ComboBox<Month> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private void initialize() {
        enhanceOvertimeBtn();
        cbStatus.getItems().addAll("Requested", "Accepted", "Rejected");
        cbStatus.getSelectionModel().selectFirst();
    }

    public OvertimeController() {
    }

    public void cbMonthClicked(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void cbYearClicked(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void cbStatusChanged(ActionEvent actionEvent) {
        populateOvertimeTableView();
    }

    public void fileOvertimeRequestClicked(ActionEvent actionEvent) throws IOException {
        /**
         * TODO: Display the overtime application form
         * 1. Create overtime application form FXML
         * 2. Name the controller of the form to (FileOvertimeRequestController)
         * 3. Create functionality of the form
         * 4. Make sure the required fields is visible
         * 5. You can only submit one overtime request daily.
         *
         */

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/file-overtime-request.fxml"));
        AnchorPane formPane = loader.load();
        Stage formStage = new Stage();
        formStage.setScene(new Scene(formPane));

        FileOvertimeRequestController fileOvertimeRequestController = loader.getController();
        fileOvertimeRequestController.setOvertimeController(this);

        formStage.showAndWait();
    }

    public void enhanceOvertimeBtn() {
        overtimeRequestBtn.setGraphic(new FontIcon(Material.FILE_UPLOAD));
        overtimeRequestBtn.getStyleClass().addAll(
                Styles.SUCCESS
        );
    }

    public void populateOvertimeTableView() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer month = this.cbMonth.getSelectionModel().getSelectedItem().getValue();
        Integer year = this.cbYear.getSelectionModel().getSelectedItem();
        String status = this.cbStatus.getSelectionModel().getSelectedItem();

        tvOvertime.setItems(FXCollections.observableList(
                this
                    .getEmployeeRoleNavigationController()
                    .getMainViewController()
                    .getOvertimeRequestService()
                    .getOvertimeRequestsByEmployeeId(
                            employee, month, year, status
                    )
        ));
    }

    public void populateMonths() {
        this.cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
        // select current month
        LocalDateTime localDateTime = LocalDateTime.now();
        this.cbMonth.getSelectionModel().select(localDateTime.getMonth());
    }

    public void populateYears() {
        this.cbYear.setItems(FXCollections.observableArrayList(
                this.getEmployeeRoleNavigationController().getMainViewController().getOvertimeRequestService().getOvertimeRequestYears(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee()
                )
        ));
        // select current year
        cbYear.getSelectionModel().selectFirst();
    }
}
