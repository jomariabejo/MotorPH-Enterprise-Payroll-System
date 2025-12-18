package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
<<<<<<< HEAD
import com.jomariabejo.motorph.utility.DateTimeUtil;
import com.jomariabejo.motorph.utility.LoggingUtility;
=======
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
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
import java.time.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Setter
public class TimesheetController {

    // Allowed time window constants
    private static final LocalTime ALLOWED_START_TIME = LocalTime.of(8, 0); // 8:00 AM
    private static final LocalTime ALLOWED_END_TIME = LocalTime.of(17, 0); // 5:00 PM

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
        if (YouHaveTimeInToday()) {
            displayYourTimeInAlreadyExist();
        } else {
            if (isTimeWithinAllowedWindow()) {
                saveYourTimeIn();
            } else {
                displayTimeInOutsideAllowedWindow();
            }
        }
    }

    private void saveYourTimeIn() {
        Timesheet timesheet = new Timesheet();
        timesheet.setDate(DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate());
        timesheet.setEmployeeID(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee());
        timesheet.setTimeIn(Time.valueOf(DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalTime()));
        timesheet.setHoursWorked(0.00f);
        timesheet.setStatus("Not Submitted");
        this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().saveTimesheet(timesheet);
        tvTimesheets.getItems().add(timesheet);
        
        // Log clock in action
        var user = this.getEmployeeRoleNavigationController().getMainViewController().getUser();
        if (user != null) {
            Integer employeeNumber = timesheet.getEmployeeID().getEmployeeNumber();
            String timeIn = timesheet.getTimeIn().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
            LoggingUtility.logClockIn(user, employeeNumber, timeIn);
        }
    }

    private void displayYourTimeInAlreadyExist() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Time In Already Exists", "You have already recorded time in for today.");
        customAlert.showAndWait();
    }

    private void displayTimeOutAlreadyExist() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "Time Out Already Exists", "You have already recorded time out for today.");
        customAlert.showAndWait();
    }

    /**
     * Displays an alert when user tries to time in outside allowed hours (8:00 AM - 5:00 PM)
     */
    private void displayTimeInOutsideAllowedWindow() {
<<<<<<< HEAD
        LocalTime currentTime = DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalTime();
=======
        LocalTime currentTime = LocalTime.now();
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        int hour = currentTime.getHour();
        int hour12;
        if (hour == 0) {
            hour12 = 12;
        } else if (hour > 12) {
            hour12 = hour - 12;
        } else {
            hour12 = hour;
        }
        String amPm = hour >= 12 ? "PM" : "AM";
        String timeFormat = String.format("%02d:%02d %s", hour12, currentTime.getMinute(), amPm);
        
        String message = String.format(
            "You cannot time in at %s.%n%nTime in is only allowed between 8:00 AM and 5:00 PM.",
            timeFormat
        );
        
        CustomAlert customAlert = new CustomAlert(
            Alert.AlertType.WARNING, 
            "Time In Not Allowed", 
            message
        );
        customAlert.showAndWait();
    }

    /**
     * Displays an alert when user tries to time out outside allowed hours (8:00 AM - 5:00 PM)
     */
    private void displayTimeOutOutsideAllowedWindow() {
<<<<<<< HEAD
        LocalTime currentTime = DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalTime();
=======
        LocalTime currentTime = LocalTime.now();
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        int hour = currentTime.getHour();
        int hour12;
        if (hour == 0) {
            hour12 = 12;
        } else if (hour > 12) {
            hour12 = hour - 12;
        } else {
            hour12 = hour;
        }
        String amPm = hour >= 12 ? "PM" : "AM";
        String timeFormat = String.format("%02d:%02d %s", hour12, currentTime.getMinute(), amPm);
        
        String message = String.format(
            "You cannot time out at %s.%n%nTime out is only allowed between 8:00 AM and 5:00 PM.",
            timeFormat
        );
        
        CustomAlert customAlert = new CustomAlert(
            Alert.AlertType.WARNING, 
            "Time Out Not Allowed", 
            message
        );
        customAlert.showAndWait();
    }

    /**
     * Checks if the current time is within the allowed window (8:00 AM - 5:00 PM)
     * @return true if current time is between 8:00 AM and 5:00 PM (inclusive), false otherwise
     */
    private boolean isTimeWithinAllowedWindow() {
<<<<<<< HEAD
        LocalTime currentTime = DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalTime();
=======
        LocalTime currentTime = LocalTime.now();
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        // Check if current time is between 8:00 AM (08:00) and 5:00 PM (17:00)
        return !currentTime.isBefore(ALLOWED_START_TIME) && !currentTime.isAfter(ALLOWED_END_TIME);
    }


    private boolean YouHaveTimeInToday() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().getTimesheetByEmployeeAndDate(
                this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate()
        );
        return timesheet.isPresent();
    }

    @FXML
    void clockOutClicked(ActionEvent event) {
        if (AreYouSureYouWantToTimeOutToday()) {
            if (youHaveTimeOutToday()) {
                displayTimeOutAlreadyExist();
            } else {
                if (isTimeWithinAllowedWindow()) {
                    modifyYourTimeOut();
                } else {
                    displayTimeOutOutsideAllowedWindow();
                }
            }
        }
    }

    private void modifyYourTimeOut() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().getTimesheetByEmployeeAndDate(
                this.getEmployeeRoleNavigationController().getMainViewController().getEmployee(),
                DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate()
        );
        if (timesheet.isPresent()) {
            if (timesheet.get().getTimeOut() == null) {
                timesheet.get().setTimeOut(Time.valueOf(DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalTime()));
                timesheet.get().setHoursWorked((
                        calculateHoursWorked(
                                timesheet.get()
                        )));
                this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().updateTimesheet(timesheet.get());
                populateTableview();
                
                // Log clock out action
                var user = this.getEmployeeRoleNavigationController().getMainViewController().getUser();
                if (user != null) {
                    Integer employeeNumber = timesheet.get().getEmployeeID().getEmployeeNumber();
                    String timeOut = timesheet.get().getTimeOut().toLocalTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                    LoggingUtility.logClockOut(user, employeeNumber, timeOut);
                }
            }
        }
    }

    private static float calculateHoursWorked(Timesheet timesheet) {
        LocalTime startTime = timesheet.getTimeIn().toLocalTime();
        LocalTime endTime = timesheet.getTimeOut().toLocalTime();

        if (endTime.isBefore(startTime)) {
            endTime = endTime.plusHours(24);
        }

        Duration duration = Duration.between(startTime, endTime);

        return (float) duration.toMinutes() / 60;
    }

    private boolean AreYouSureYouWantToTimeOutToday() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Save Time Out Confirmation", "Are you sure you want to time out today?");
        Optional<ButtonType> userAction = customAlert.showAndWait();
        return userAction.isPresent() && userAction.get().equals(ButtonType.OK);
    }


    private boolean youHaveTimeOutToday() {
        Optional<Timesheet> timesheet = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getServiceFactory()
                .getTimesheetService()
                .getTimesheetByEmployeeAndDate(
                        this.getEmployeeRoleNavigationController()
                                .getMainViewController()
                                .getEmployee(),
                        DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate()
                );
        return timesheet.isPresent() && timesheet.get().getTimeOut() != null;
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
        clockInBtn.getStyleClass().addAll(Styles.SUCCESS);
        clockOutBtn.getStyleClass().addAll(Styles.DANGER);
        clockInBtn.setGraphic(new FontIcon(FontAwesomeRegular.CALENDAR_CHECK));
        clockOutBtn.setGraphic(new FontIcon(FontAwesomeRegular.CALENDAR_MINUS));
    }

    public void populateMonths() {
        cbMonth.setItems(FXCollections.observableArrayList(Month.values()));
        selectCurrentMonth();
    }

    private void selectCurrentMonth() {
        LocalDate localDate = DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate();
        cbMonth.getSelectionModel().select(localDate.getMonth());
    }

    public void populateYears() {
        Optional<List<Integer>> yearsOpt = this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().getYearsOfLeaveRequestOfEmployee(this.getEmployeeRoleNavigationController().getMainViewController().getEmployee());
        
        if (yearsOpt.isPresent()) {
            cbYear.setItems(FXCollections.observableList(yearsOpt.get()));
        } else {
            // If no years found, set current year as default
<<<<<<< HEAD
            cbYear.setItems(FXCollections.observableArrayList(DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate().getYear()));
=======
            cbYear.setItems(FXCollections.observableArrayList(LocalDate.now().getYear()));
>>>>>>> b44be3fc1877fa0790d469aafceed9f64b2cd89f
        }
        selectCurrentYear();
    }

    private void selectCurrentYear() {
        LocalDate localDate = DateTimeUtil.getCurrentDateTimeInPhilippines().toLocalDate();
        // Kung wala tayong current year, lalagyan natin ito at saka itutuk natin sa year na yan🔫
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
                    this.getEmployeeRoleNavigationController().getMainViewController().getServiceFactory().getTimesheetService().getTimesheetsByEmployeeAndDate(
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
        } catch (NoSuchElementException noSuchElementException) {
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
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "No timesheet found", "You dont have any timesheet for this period(" + cbMonth.getSelectionModel().getSelectedItem() + " " + cbYear.getSelectionModel().getSelectedItem() + ").");
        customAlert.showAndWait();
    }
}
