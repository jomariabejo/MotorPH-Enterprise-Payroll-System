package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.OvertimeRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;

@Getter
@Setter
public class ReimbursementController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private ComboBox<?> cbMonth;

    @FXML
    private ComboBox<?> cbYear;

    @FXML
    private Pagination pagination;

    @FXML
    private TableView<OvertimeRequest> tvOvertimeRequests;

    @FXML
    private Button fileReimbursement;

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
}
