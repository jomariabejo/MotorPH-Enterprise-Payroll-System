package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
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

@Getter
@Setter
public class TimesheetController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Button clockInBtn;

    @FXML
    private Button clockOutBtn;

    @FXML
    private TableView<?> tvLeaveRequests;

    @FXML
    private Pagination pagination;

    @FXML
    private ComboBox cbMonth;

    @FXML
    private ComboBox cbYear;

    @FXML
    private void initialize() {
        enhanceButtonStyle();
    }

    @FXML
    void clockInClicked(ActionEvent event) {

    }

    @FXML
    void clockOutClicked(ActionEvent event) {

    }

    @FXML
    void paginationOnDragDetected(MouseEvent event) {

    }

    public TimesheetController() {

    }

    @FXML
    public void cbMonthClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void cbYearClicked(ActionEvent actionEvent) {
    }

    private void enhanceButtonStyle() {
        clockInBtn.getStyleClass().addAll(
                Styles.SUCCESS
        );
        clockOutBtn.getStyleClass().addAll(
                Styles.DANGER);
        clockInBtn.setGraphic(new FontIcon(FontAwesomeRegular.CALENDAR_CHECK));
        clockOutBtn.setGraphic(new FontIcon(FontAwesomeRegular.CALENDAR_MINUS));
    }
}
