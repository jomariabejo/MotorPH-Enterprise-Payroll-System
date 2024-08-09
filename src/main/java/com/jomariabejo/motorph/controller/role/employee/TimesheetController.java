package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.TimeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Setter
public class TimesheetController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;
    private ObservableList<Timesheet> timesheetObservableList;

    @FXML
    private Button clockInBtn;

    @FXML
    private Button clockOutBtn;

    @FXML
    private TableView<Timesheet> tvTimesheets;

    @FXML
    private Pagination pagination;

    @FXML
    private ComboBox<Month> cbMonth;

    @FXML
    private ComboBox<Integer> cbYear;

    @FXML
    private void initialize() {
        enhanceButtonStyle();
    }


    @FXML
    void clockInClicked(ActionEvent event) {
        if (YouHaveTimeInToday())
            displayYourTimeInAlreadyExist();
        else
            saveYourTimeIn();
    }

    private void saveYourTimeIn() {
        Timesheet timesheet = new Timesheet();
        timesheet.setDate(LocalDate.now());
        timesheet.setEmployeeID(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee());
        timesheet.setTimeIn(Time.valueOf(LocalTime.now()));
        timesheet.setHoursWorked(0.00f);
        timesheet.setStatus("Not Submitted");
        this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().saveTimesheet(timesheet);
        tvTimesheets.getItems().add(timesheet);
    }

    private void displayYourTimeInAlreadyExist() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR, "Time out Already Exists", "You have already recorded time in for today."
        );
        customAlert.showAndWait();
    }

    private void displayTimeOutAlreadyExist() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR, "Time in Already Exists", "You have already recorded time out for today."
        );
        customAlert.showAndWait();
    }


    private boolean YouHaveTimeInToday() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().getTimesheetByEmployeeAndDate(
                this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                LocalDate.now()
        );
        return timesheet.isPresent();
    }

    @FXML
    void clockOutClicked(ActionEvent event) {

        if (AreYouSureYouWantToTimeOutToday()) {
            if (youHaveTimeOutToday()) {
                displayTimeOutAlreadyExist();
            } else
                modifyYourTimeOut();
        }

    }

    private void modifyYourTimeOut() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().getTimesheetByEmployeeAndDate(
                this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                LocalDate.now()
        );
        if (timesheet.isPresent()) {
            if (timesheet.get().getTimeOut() == null) {
                timesheet.get().setTimeOut(Time.valueOf(LocalTime.now()));
                timesheet.get().setHoursWorked(TimeUtils.calculateTimeDifference(
                        timesheet.get().getTimeIn().toLocalTime(),
                        timesheet.get().getTimeOut().toLocalTime()
                ));
                this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().updateTimesheet(timesheet.get());
            }
        }

    }

    private boolean AreYouSureYouWantToTimeOutToday() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.CONFIRMATION, "Save Time Out Confirmation",
                "Are you sure you want to time out today?"
        );

        Optional<ButtonType> userAction = customAlert.showAndWait();

        return userAction.get().equals(ButtonType.OK);
    }


    private boolean youHaveTimeOutToday() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().getTimesheetByEmployeeAndDate(
                this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                LocalDate.now()
        );
        return timesheet.get().getTimeOut() != null;
    }

    @FXML
    void paginationOnDragDetected(MouseEvent event) {

    }

    public TimesheetController() {

    }

    @FXML
    public void cbMonthClicked(ActionEvent actionEvent) {
        populateTableview();
    }

    @FXML
    public void cbYearClicked(ActionEvent actionEvent) {
        populateTableview();
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

    public void populateMonths() {
        cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
        selectCurrentMonth();
    }

    private void selectCurrentMonth() {
        LocalDate localDate = LocalDate.now();
        cbMonth.getSelectionModel().select(localDate.getMonth());
    }

    public void populateYears() {
        cbYear.setItems(FXCollections.observableList(
                this.getEmployeeRoleNavigationController().getMainViewController().getTimesheetService().getYearsOfLeaveRequestOfEmployee(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee()
                ).get()
        ));
        selectCurrentYear();
    }

    private void selectCurrentYear() {
        LocalDate localDate = LocalDate.now();
        if (!cbYear.getItems().contains(localDate.getYear())) {
            cbYear.getItems().add(0, localDate.getYear());
            cbYear.getSelectionModel().select(0);
        } else {
            cbYear.getSelectionModel().select(0);
        }
    }

    public void populateTableview() {
        try {
            setTimesheetObservableList(FXCollections.observableArrayList(
                    this.getEmployeeRoleNavigationController()
                            .getMainViewController()
                            .getTimesheetService()
                            .getTimesheetsByEmployeeAndDate(
                                    this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                                    Year.of(cbYear.getSelectionModel().getSelectedItem()),
                                    cbMonth.getSelectionModel().getSelectedItem()
                            ).get()
            ));

            int itemsPerPage = 25;
            int pageCount = (int) Math.ceil((double) timesheetObservableList.size() / itemsPerPage);
            pagination.setPageCount(pageCount);

            pagination.setPageFactory(pageIndex -> {
                updateTableView(pageIndex, itemsPerPage);
                return new StackPane();
            });
        }
        catch (NoSuchElementException noSuchElementException) {
            displayYouDontHaveTimesheetForThisPeriod();
        }
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, timesheetObservableList.size());
        List<Timesheet> pageData = timesheetObservableList.subList(fromIndex, toIndex);
        tvTimesheets.setItems(FXCollections.observableList(pageData));
    }

    private void displayYouDontHaveTimesheetForThisPeriod() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.ERROR,
                "No timesheet found",
                "You dont have any timesheet for this period."
        );
        customAlert.showAndWait();
    }
}
