package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

@Getter
@Setter
public class PayslipController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    private ObservableList<Payslip> payslips;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private TableView<Payslip> tvPayslip;

    @FXML
    private Pagination pagination;

    public PayslipController() {
    }

    public void paginationOnDragDetected(MouseEvent mouseEvent) {
    }

    @FXML
    private void initialize() {
    }


    public void cbYearChanged() {
        populatePayslipTableView();
    }

    public void populatePayslipTableView() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        Integer year = this.cbYear.getSelectionModel().getSelectedItem();

        payslips = FXCollections.observableList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getPayslipService()
                        .getPayslipByEmployeeIdAndYear(
                                employee, year
                        ).get()
        );
        tvPayslip.setItems(payslips);
    }

    public void populateYears() {
        this.cbYear.setItems(FXCollections.observableArrayList(
                this.getEmployeeRoleNavigationController()
                        .getMainViewController()
                        .getServiceFactory()
                        .getPayslipService()
                        .getEmployeeYearsOfPayslip(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee())
                        .get()
        ));
        // select latest year fetched.
        cbYear.getSelectionModel().selectFirst();
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, payslips.size());
        List<Payslip> pageData = payslips.subList(fromIndex, toIndex);
        tvPayslip.setItems(FXCollections.observableList(pageData));
    }

    public void setupOvertimeTableView() {
        TableColumn<Payslip, Void> actionsColumn = createActionsColumn();
        this.tvPayslip.getColumns().add(actionsColumn);
    }

    private TableColumn<Payslip, Void> createActionsColumn() {
        TableColumn<Payslip, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = createDownloadButton();
            private final HBox actionsBox = new HBox(updateButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                /**
                 * Create Download On Action
                 */

            }

            private Button createDownloadButton() {
                Button updateButton = new Button(null, new FontIcon(Feather.DOWNLOAD));
                updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return updateButton;
            }
        });
        return actionsColumn;
    }
}
