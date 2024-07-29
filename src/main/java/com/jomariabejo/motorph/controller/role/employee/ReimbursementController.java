package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.OvertimeRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

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
    public void fileOvertimeRequestClicked(ActionEvent actionEvent) {
    }
}
