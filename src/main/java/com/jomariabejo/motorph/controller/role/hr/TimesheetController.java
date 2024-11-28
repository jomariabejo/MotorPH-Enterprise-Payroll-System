package com.jomariabejo.motorph.controller.role.hr;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.model.Timesheet;
import com.jomariabejo.motorph.utility.CustomAlert;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    private TextField tfApprover;

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
        try {
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
        } catch (NoSuchElementException noSuchElementException) {
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
        boolean isSearchBarEmpty = searchBar.getText().isEmpty();

        if (isSearchBarEmpty) {
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
        } else {
            boolean isEmployeeNameExist = isEmployeeNameExist(searchBar.getText());

            if (isEmployeeNameExist) {
                switch (fetchType) {
                    case "Daily":
                        displayTimesheetsByDatePickerSelectedDayAndEmployeeName();
                        break;
                    case "Monthly":
                        displayTimesheetsByDatePickerSelectedMonthAndEmployeeName();
                        break;
                    case "Yearly":
                        displayTimesheetsByDatePickerSelectedYearAndEmployeeName();
                        break;
                }
            } else {
                displayEmployeeNameDoesNotExist();
            }
        }
    }

    private void displayEmployeeNameDoesNotExist() {
        CustomAlert customAlert = new CustomAlert(Alert.AlertType.INFORMATION,
                "Employee not found.", "Employee name doesn't exist");
        customAlert.showAndWait();
    }

    private void displayTimesheetsByDatePickerSelectedYearAndEmployeeName() {
        populateTableViewByDatePickerSelectedYearAndEmployeeName();
    }

    private void populateTableViewByDatePickerSelectedYearAndEmployeeName() {
        try {
            tvTimesheets.setItems(
                    FXCollections.observableList(
                            this.getHumanResourceAdministratorNavigationController()
                                    .getMainViewController()
                                    .getServiceFactory()
                                    .getTimesheetService()
                                    .getTimesheetByEmployeeNameAndYear(
                                            timesheetDatePicker.getValue(),
                                            searchBar.getText()
                                    ).get()
                    )
            );
        } catch (NoSuchElementException noSuchElementException) {
            CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "No timesheet found", STR."No timesheet found for \{searchBar.getText()}");
            customAlert.showAndWait();
        }
    }

    private void displayTimesheetsByDatePickerSelectedMonthAndEmployeeName() {
        populateTableViewByDatePickerSelectedMonthAndEmployeeName();
    }

    private void populateTableViewByDatePickerSelectedMonthAndEmployeeName() {
        try {
            tvTimesheets.setItems(
                    FXCollections.observableList(
                            this.getHumanResourceAdministratorNavigationController()
                                    .getMainViewController()
                                    .getServiceFactory()
                                    .getTimesheetService()
                                    .getEmployeeTimesheetsByYearAndMonth(
                                            timesheetDatePicker.getValue(),
                                            searchBar.getText()
                                    ).get()
                    )
            );
        } catch (NoSuchElementException noSuchElementException) {
            CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "No timesheet found", STR."No timesheet found for \{searchBar.getText()}");
            customAlert.showAndWait();
        }
    }

    private void displayTimesheetsByDatePickerSelectedDayAndEmployeeName() {
        populateTableViewByDatePickerSelectedDayAndEmployeeName();
    }

    private void populateTableViewByDatePickerSelectedDayAndEmployeeName() {
        try {
            tvTimesheets.setItems(
                    FXCollections.observableList(
                            this.getHumanResourceAdministratorNavigationController()
                                    .getMainViewController()
                                    .getServiceFactory()
                                    .getTimesheetService()
                                    .getTimesheetByEmployeeNameAndDay(
                                            timesheetDatePicker.getValue(),
                                            searchBar.getText()
                                    ).get()
                    )
            );
        } catch (NoSuchElementException noSuchElementException) {
            CustomAlert customAlert = new CustomAlert(Alert.AlertType.ERROR, "No timesheet found", STR."No timesheet found for \{searchBar.getText()}");
            customAlert.showAndWait();
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
        applySearchText(searchBar.getText());
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
                    displayEmployeeTimesheetsBySelectedDay(employeeName);
                    break;
                case "Monthly":
                    displayEmployeeTimesheetsBySelectedMonth(employeeName);
                    break;
                case "Yearly":
                    displayEmployeeTimesheetsBySelectedYear(employeeName);
                    break;
            }
        }
    }

    private void displayEmployeeTimesheetsBySelectedYear(String employeeName) {
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

    private void displayEmployeeTimesheetsBySelectedMonth(String employeeName) {
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

    private void displayEmployeeTimesheetsBySelectedDay(String employeeName) {
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

    private TableColumn<Timesheet, Void> createActionsColumn() {
        TableColumn<Timesheet, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifydBtn = createModifyBtn();

            private Button createModifyBtn() {
                FontIcon fontIcon = new FontIcon(Feather.EDIT);
                Button updateButton = new Button(null, fontIcon);
                updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_OUTLINED);
                return updateButton;
            }


            private Button createDeleteBtn() {
                FontIcon fontIcon = new FontIcon(Feather.TRASH_2);
                Button deleteButton = new Button(null, fontIcon);
                deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_OUTLINED);
                return deleteButton;
            }

            private final Button deleteBtn = createDeleteBtn();


            private final HBox actionsBox = new HBox(modifydBtn, deleteBtn);

            {
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.setSpacing(10);
                setGraphic(actionsBox);

                // Create timesheet form wherein we can modify employee timesheets
                modifydBtn.setOnAction(event -> {
                    try {
                        Timesheet selectedTimesheet = getTableView().getItems().get(getIndex());

                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource
                                    ("/com/jomariabejo/motorph/role/human-resource/timesheet-viewer.fxml"));

                            Parent root = fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.initStyle(StageStyle.DECORATED);
                            stage.setTitle("Viewing employee timesheet.");
                            stage.setScene(new Scene(root));
                            stage.show();

                            TimesheetModifier timesheetViewForm = fxmlLoader.getController();
                            timesheetViewForm.injectTimesheet(selectedTimesheet);
                            timesheetViewForm.setTimesheetController(TimesheetController.this);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                });

                deleteBtn.setOnAction(event -> {
                    Timesheet selectedTimesheet = getTableView().getItems().get(getIndex());

                    CustomAlert customAlert = new CustomAlert(Alert.AlertType.CONFIRMATION, "Timesheet removal confirmation","Are you sure you want to delete this timesheet?");
                    Optional<ButtonType> result = customAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        this.getTableView().getItems().remove(selectedTimesheet);
                        this.getTableView().refresh();
                        humanResourceAdministratorNavigationController.getMainViewController().getServiceFactory().getTimesheetService().deleteTimesheet(selectedTimesheet);
                        CustomAlert alertSuccess = new CustomAlert(Alert.AlertType.INFORMATION, "Timesheet successfully removed.", "Employee timesheet has been removed.");
                        alertSuccess.showAndWait();
                    }
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

    @FXML
    private void initialize() {
        setupTableViewOnAction();
    }

    private void setupTableViewOnAction() {
        TableColumn<Timesheet, Void> actionsColumn = createActionsColumn();
        this.tvTimesheets.getColumns().add(actionsColumn);
    }

    public void timesheetDateChanged(ActionEvent actionEvent) {
        timesheetDateFilterChanged(actionEvent);
    }
}
