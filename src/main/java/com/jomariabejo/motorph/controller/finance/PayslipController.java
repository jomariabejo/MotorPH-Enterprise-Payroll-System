package com.jomariabejo.motorph.controller.finance;

import com.jomariabejo.motorph.controller.employee.MyPayslipController;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.service.PayslipService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class PayslipController {
    private PayslipService payslipService;

    public PayslipController() {
        this.payslipService = new PayslipService();
    }

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private TableView<Payslip> tv_employee_payslips;

    @FXML
    private void initialize() throws SQLException {
        setUpTableView();
        populateTableView();
        setUpTotalResult();
    }

    private void populateTableView() {
        this.tv_employee_payslips.setItems(payslipService.fetchPayslipSummary());
    }

    private void setUpTableView() {
        // Define the custom cell for the actions column
        TableColumn<Payslip, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button viewButton = new Button();
            final Button updateButton = new Button();

            final HBox actionsBox = new HBox(viewButton);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(5);

                viewButton.setOnAction(event -> {
                    Payslip payslip = getTableView().getItems().get(getIndex());
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/employee-payslip-viewer.fxml"));

                    Parent root;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initStyle(StageStyle.DECORATED);
                    stage.setScene(new Scene(root));
                    stage.setTitle("Payslip Generated from " + payslip.getPayPeriodStart() + " to " + payslip.getPayPeriodEnd());
                    stage.show();


                    EmployeesPayslipController payslipController = fxmlLoader.getController();
                    payslipController.initEntirePayslip(
                            payslip.getPayPeriodStart(),
                            payslip.getPayPeriodEnd()
                    );
                });

                Image viewIcon = new Image(getClass().getResourceAsStream("/img/view-icon.png"));
                Image updateIcon = new Image(getClass().getResourceAsStream("/img/editemployeeicon.png"));

                ImageView viewIconView = new ImageView(viewIcon);
                viewIconView.setFitWidth(36);
                viewIconView.setFitHeight(24);

                ImageView updateIconView = new ImageView(updateIcon);
                updateIconView.setFitWidth(36);
                updateIconView.setFitHeight(24);

                Image deleteIcon = new Image(getClass().getResourceAsStream("/img/delete-icon.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(24);
                deleteIconView.setFitHeight(24);

                viewButton.setGraphic(viewIconView);
                updateButton.setGraphic(updateIconView);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });
        this.tv_employee_payslips.getColumns().add(actionsColumn);
    }


    private void setUpTotalResult() {
        int tblViewSize = tv_employee_payslips.getItems().size();
        this.lbl_tv_total_result.setText(String.valueOf(tblViewSize));
    }
}
