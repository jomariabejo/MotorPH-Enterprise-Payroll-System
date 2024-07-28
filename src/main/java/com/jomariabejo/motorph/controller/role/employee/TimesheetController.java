package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.glyphfont.FontAwesome;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material.Material;

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
    private void initialize() {
        clockInBtn.getStyleClass().add(Styles.SUCCESS);
        FontIcon fontIconClockIn = new FontIcon(FontAwesomeRegular.CALENDAR_CHECK);
        fontIconClockIn.setIconSize(24);
        clockOutBtn.getStyleClass().add(Styles.DANGER);
        FontIcon fontIconClockOut = new FontIcon(FontAwesomeRegular.CALENDAR_MINUS);
        fontIconClockOut.setIconSize(24);
        clockInBtn.setGraphic((fontIconClockIn));
        clockOutBtn.setGraphic((fontIconClockOut));

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
    @FXML
    public void cbClockInClicked(ActionEvent actionEvent) {
    }
    @FXML
    public void cbClockOutClicked(ActionEvent actionEvent) {
    }
}
