package com.jomariabejo.motorph.controller.role.hr;

import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TimesheetController {

    @FXML
    private ComboBox<String> dateFilter;

    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;

    public TimesheetController() {
    }

    @FXML
    private Label lblDate;

    @FXML
    private Pagination paginationTimesheets;

    @FXML
    private Button printBtn;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchBtn;

    @FXML
    private DatePicker timesheetDatePicker;

    @FXML
    private TableView<Timesheet> tvTimesheets;

    @FXML
    void clockOutClicked(ActionEvent event) {

    }

    @FXML
    void searchBtnClicked(ActionEvent event) {

    }

    private void addPrintIcon() {
        printBtn.setGraphic(new FontIcon(FontAwesomeRegular.CALENDAR_CHECK));
        printBtn.setMaxSize(36, 36);
    }

    public void enhanceUI() {
        addPrintIcon();
    }

    public void setup() {
        setDatePickerToCurrentDate();
        populateTableViewByDatePickerSelectedDay();
        enhanceUI();
        automateSearchBar();
    }

    private void setDatePickerToCurrentDate() {
        timesheetDatePicker.setValue(LocalDate.from(LocalDateTime.now()));
    }

    private void populateTableViewByDatePickerSelectedDay() {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getAllTimesheetsToday(theDate).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private void populateTableViewByDatePickerSelectedMonth() {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getAllTimesheetsByMonthlyBasis(theDate).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private void populateTableViewByDatePickerSelectedYear() {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getAllTimesheetsByYear(theDate).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private void alertUserTableIsEmpty() {
        CustomAlert customAlert = new CustomAlert(
                Alert.AlertType.INFORMATION,
                "Timesheet empty",
                "No timesheet found"
        );
        customAlert.showAndWait();
    }

    private boolean timesheetTableViewEmpty() {
        return tvTimesheets.getItems().isEmpty();
    }

    public void timesheetDateFilterChanged(ActionEvent actionEvent) {
        String fetchType = dateFilter.getSelectionModel().getSelectedItem().toString();

        switch (fetchType) {
            case "Daily":
                displayTimesheetsByDatePickerSelectedDay();
                break;
            case "Monthly":
                displayTimesheetsByDatePickerSelectedMonth();
                break;
            case "Yearly":
                displayTimesheetsByDatePickerSelectedYear();
                break;
        }
    }

    private void displayTimesheetsByDatePickerSelectedMonth() {
        populateTableViewByDatePickerSelectedMonth();
    }

    private void displayTimesheetsByDatePickerSelectedDay() {
        populateTableViewByDatePickerSelectedDay();
    }

    private void displayTimesheetsByDatePickerSelectedYear() {
        populateTableViewByDatePickerSelectedYear();
    }

    public void searchBarChanged(ActionEvent actionEvent) {

    }

    private void automateSearchBar() {
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            applySearchText(newValue);
        });
    }

    private void applySearchText(String employeeName) {
        if (isEmployeeNameExist(employeeName)) {
            String fetchType = dateFilter.getSelectionModel().getSelectedItem().toString();

            switch (fetchType) {
                case "Daily":
                    displayEmployeeTimesheetsByDatePickerSelectedDay(employeeName);
                    break;
                case "Monthly":
                    displayEmployeeTimesheetsByDatePickerSelectedMonth(employeeName);
                    break;
                case "Yearly":
                    displayEmployeeTimesheetsByDatePickerSelectedYear(employeeName);
                    break;
            }
        }
    }

    private void displayEmployeeTimesheetsByDatePickerSelectedYear(String employeeName) {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getTimesheetByEmployeeNameAndYear(theDate, employeeName).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private void displayEmployeeTimesheetsByDatePickerSelectedMonth(String employeeName) {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getEmployeeTimesheetsByYearAndMonth(theDate, employeeName).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private void displayEmployeeTimesheetsByDatePickerSelectedDay(String employeeName) {
        LocalDate theDate = timesheetDatePicker.getValue();
        tvTimesheets.setItems(
                FXCollections.observableList(
                        this.
                                getHumanResourceAdministratorNavigationController()
                                .getMainViewController()
                                .getServiceFactory()
                                .getTimesheetService()
                                .getAllTimesheetsTodayByEmployeeName(theDate, employeeName).get()
                )
        );

        if (timesheetTableViewEmpty()) {
            alertUserTableIsEmpty();
        }
    }

    private boolean isEmployeeNameExist(String employeeName) {
        return this.getHumanResourceAdministratorNavigationController().getMainViewController().getServiceFactory().getEmployeeService().isEmployeeNameExist(employeeName);
    }
}
