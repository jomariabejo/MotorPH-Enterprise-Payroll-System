package com.jomariabejo.motorph.controller.role.employee;

import atlantafx.base.theme.Styles;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.model.Employee;
import com.jomariabejo.motorph.model.LeaveRequest;
import com.jomariabejo.motorph.model.LeaveRequestType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.glyphfont.FontAwesome;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LeaveHistoryController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private Pagination paginationLeaveRequests;

    @FXML
    private TableView<LeaveRequest> tvLeaveRequests;

    @FXML
    private ComboBox<Month> cbRequestedMonth;

    @FXML
    private ComboBox<Integer> cbRequestedYear;

    @FXML
    private ComboBox<String> cbStatus;

    @FXML
    private ComboBox<LeaveRequestType> cbLeaveType;

    private ObservableList<LeaveRequest> leaveRequestList;



    @FXML
    public void comboboxMonthClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxYearClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxLeaveTypeClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    @FXML
    public void comboBoxStatusClicked(ActionEvent actionEvent) {
        populateLeaveRequests();
    }

    public void setup() {
        populateMonths();
        populateYears();
        populateLeaveTypes();
        setupComboBox();
        populateLeaveRequests();
    }

    public void populateMonths() {
        ArrayList<Month> months = new ArrayList<>();
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);
        cbRequestedMonth.setItems(FXCollections.observableList(months));
    }

    public void populateYears() {
        cbRequestedYear.setItems(FXCollections.observableList(
                this.getEmployeeRoleNavigationController().getMainViewController().getLeaveRequestService().getYearsOfLeaveRequestOfEmployee(
                        this.getEmployeeRoleNavigationController().getMainViewController().getEmployee()
                ).get()
        ));
    }

    public void populateLeaveRequests() {
        Employee employee = this.getEmployeeRoleNavigationController().getMainViewController().getEmployee();
        String month = cbRequestedMonth.getSelectionModel().getSelectedItem() != null ? cbRequestedMonth.getSelectionModel().getSelectedItem().toString() : "";
        String year = cbRequestedYear.getSelectionModel().getSelectedItem() != null ? cbRequestedYear.getSelectionModel().getSelectedItem().toString() : "";
        String status = cbStatus.getSelectionModel().getSelectedItem() != null ? cbStatus.getSelectionModel().getSelectedItem().toString() : "";
        String leaveType = cbLeaveType.getSelectionModel().getSelectedItem() != null ? cbLeaveType.getSelectionModel().getSelectedItem().toString() : "";

        List<LeaveRequest> allLeaveRequests = this.getEmployeeRoleNavigationController()
                .getMainViewController()
                .getLeaveRequestService()
                .fetchLeaveRequestsForEmployee(employee, month, year, status, leaveType);

        leaveRequestList = FXCollections.observableList(allLeaveRequests);

        int itemsPerPage = 25;
        int pageCount = (int) Math.ceil((double) leaveRequestList.size() / itemsPerPage);
        paginationLeaveRequests.setPageCount(pageCount);

        // Set the page factory
        paginationLeaveRequests.setPageFactory(pageIndex -> {
            updateTableView(pageIndex, itemsPerPage);
            return new StackPane(); 
        });
    }

    private void updateTableView(int pageIndex, int itemsPerPage) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, leaveRequestList.size());
        List<LeaveRequest> pageData = leaveRequestList.subList(fromIndex, toIndex);
        tvLeaveRequests.setItems(FXCollections.observableList(pageData));
    }

    public void populateLeaveTypes() {
        cbLeaveType.setItems(FXCollections.observableList(
                this.getEmployeeRoleNavigationController().getMainViewController().getLeaveRequestTypeService().getAllLeaveRequestTypes()
        ));
    }

    public void setupComboBox() {
        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        cbRequestedMonth.getSelectionModel().select(month);
        cbRequestedYear.getSelectionModel().selectFirst();
        cbStatus.getSelectionModel().select("Approved");
        cbLeaveType.getSelectionModel().selectFirst();
    }

    private void setUpTableView() {
        TableColumn<LeaveRequest, Void> actionsColumn = createActionsColumn();
        this.tvLeaveRequests.getColumns().add(actionsColumn);
    }

    private TableColumn<LeaveRequest, Void> createActionsColumn() {
        TableColumn<LeaveRequest, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(200); // Adjusted width to accommodate both buttons
        actionsColumn.setCellFactory(param -> createActionsCell());
        return actionsColumn;
    }

    private TableCell<LeaveRequest, Void> createActionsCell() {
        return new TableCell<>() {
            private final Button viewButton = createUpdateButton();
            private final Button deleteButton = createDeleteButton();
            private final HBox actionsBox = createActionsBox();

            {
                actionsBox.setAlignment(Pos.CENTER); // Align HBox content to center
                actionsBox.setSpacing(10); // Set spacing between buttons
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionsBox);
            }
        };
    }

    private Button createUpdateButton() {
        Button updateButton = new Button(null, new FontIcon(Feather.PEN_TOOL));
        updateButton.getStyleClass().addAll(Styles.SUCCESS, Styles.BUTTON_CIRCLE);

        updateButton.setOnAction(event -> {
            // Your update logic here
        });
        return updateButton;
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button(null, new FontIcon(Feather.TRASH_2));
        deleteButton.getStyleClass().addAll(Styles.DANGER, Styles.BUTTON_CIRCLE);
        deleteButton.setOnAction(event -> {
            // Your delete logic here
        });

        return deleteButton;
    }

    private HBox createActionsBox() {
        HBox actionsBox = new HBox(createUpdateButton(), createDeleteButton());
        actionsBox.setAlignment(Pos.CENTER);
        actionsBox.setSpacing(10); // Adjust spacing if needed
        return actionsBox;
    }

    private ImageView createImageView(String imagePath, double width, double height) {
        Image image = loadImage(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private Image loadImage(String imagePath) {
        return new Image(getClass().getResourceAsStream(imagePath));
    }



    @FXML
    private void initialize() {
        setUpTableView();
    }

}
