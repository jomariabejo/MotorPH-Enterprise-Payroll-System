package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.Payslip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
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
        setupPayslipTableView();
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

    public void setupPayslipTableView() {
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
                setGraphic(actionsBox);

                updateButton.setOnAction(event -> {
                    System.out.println("I'm clicked");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        });
        return actionsColumn;
    }


    private Button createDownloadButton() {
        FontIcon fontIcon = new FontIcon(Feather.DOWNLOAD);
        Button updateButton = new Button(null, fontIcon);
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
        updateButton.setOnAction(event -> handleDownloadButtonClicked());
        return updateButton;
    }

    /**
     * TODO:
     * Add download button functionality 8/21/2024
     */
    private void handleDownloadButtonClicked() {

    }
}
