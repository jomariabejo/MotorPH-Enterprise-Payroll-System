package com.jomariabejo.motorph.controller.finance;

import com.jomariabejo.motorph.controller.employee.ViewPayslipController;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.service.PayslipService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Date;
import java.sql.SQLException;

public class EmployeesPayslipController {
    private PayslipService payslipService;

    public EmployeesPayslipController() {
        this.payslipService = new PayslipService();
    }

    @FXML
    private Label lbl_tv_total_result;

    @FXML
    private TableView<Payslip> tv_my_payslips;

    @FXML
    private void initialize() throws SQLException {
        setUpTableView();
    }

    private void setUpTableView() throws SQLException {
        // Define the custom cell for the actions column
        TableColumn<Payslip, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(150);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            final Button viewButton = new Button();

            final HBox actionsBox = new HBox(viewButton);

            {
                actionsBox.setAlignment(Pos.CENTER); // Align HBox content to center
                actionsBox.setSpacing(5); // Set spacing between buttons

                viewButton.setOnAction(event -> {
                    Payslip payslip = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/center/payslip-view.fxml"));

                        Parent root = fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setScene(new Scene(root));
                        stage.show();

                        ViewPayslipController viewMyPayslipController = fxmlLoader.getController();
                        viewMyPayslipController.initPayslipId(payslip.getPayslipID());
                        viewMyPayslipController.displayButtons();
                        System.out.println("Should display buttons");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

                Image viewIcon = new Image(getClass().getResourceAsStream("/img/view-icon.png"));

                ImageView viewIconView = new ImageView(viewIcon);
                viewIconView.setFitWidth(36);
                viewIconView.setFitHeight(24);

                Image deleteIcon = new Image(getClass().getResourceAsStream("/img/delete-icon.png"));
                ImageView deleteIconView = new ImageView(deleteIcon);
                deleteIconView.setFitWidth(24);
                deleteIconView.setFitHeight(24);

                viewButton.setGraphic(viewIconView);
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
        this.tv_my_payslips.getColumns().add(actionsColumn);
    }


    private void setUpTotalResult() {
        int tblViewSize = tv_my_payslips.getItems().size();
        this.lbl_tv_total_result.setText(String.valueOf(tblViewSize));
    }

    public void initEntirePayslip(Date payPeriodStart, Date payPeriodEnd) {
        ObservableList observableList = FXCollections.observableList(payslipService.fetchPayslipBetweenPayStartDateAndPayEndDate(payPeriodStart, payPeriodEnd));
        this.tv_my_payslips.setItems(observableList);
        setUpTotalResult();
    }
}
