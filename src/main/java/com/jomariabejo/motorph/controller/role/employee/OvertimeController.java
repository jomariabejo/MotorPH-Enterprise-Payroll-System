package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.OvertimeRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Getter
@Setter
public class OvertimeController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    private ObservableList<OvertimeRequest> overtimeRequests;

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
    private Pagination paginationOvertimeRequest;

    @FXML
    private void initialize() {
        enhanceOvertimeBtn();
        cbStatus.getItems().addAll("Requested", "Accepted", "Rejected");
        cbStatus.getSelectionModel().selectFirst();
    }

    public OvertimeController() {
    }

    public void cbMonthClicked() {
        populateOvertimeTableView();
    }

    public void cbYearClicked() {
        populateOvertimeTableView();
    }

    public void cbStatusChanged() {
        populateOvertimeTableView();
    }

    public void fileOvertimeRequestClicked() throws IOException {
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

        overtimeRequests = FXCollections.observableList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getOvertimeRequestService()
                        .getOvertimeRequestsByEmployeeId(
                                employee, month, year, status
                        )
        );

        // Add pagination functionality

        int itemsPerPage = 25;
        int pageCount = (int) Math.ceil((double) overtimeRequests.size() / itemsPerPage);
        paginationOvertimeRequest.setPageCount(pageCount);

        paginationOvertimeRequest.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane();
        });
    }

    public void populateMonths() {
        this.cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
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

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, overtimeRequests.size());
        List<OvertimeRequest> pageData = overtimeRequests.subList(fromIndex, toIndex);
        tvOvertime.setItems(FXCollections.observableList(pageData));
    }
}
