package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
public class FileLeaveRequestController {


    @FXML
    private ComboBox<?> cbLeaveTypes;

    @FXML
    private DatePicker dpEndLeaveDate;

    @FXML
    private DatePicker dpStartLeaveDate;

    @FXML
    private HBox endOfLeave;

    @FXML
    private Label lblLeaveApplicationName;

    @FXML
    private Label lblStartLeaveDate;

    @FXML
    private RadioButton radioMultiDayLeave;

    @FXML
    private RadioButton radioSingleLeave;

    @FXML
    private TextArea taReason;


    @FXML
    void cancelButtonClicked() {

    }

    @FXML
    void submitButtonClicked() {

    }


    private EmployeeRoleNavigationController employeeRoleNavigationController;

    public FileLeaveRequestController() {
    }

    public void configDatePicker() {
        dpStartLeaveDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    // Disable previous days
                    setDisable(date.isBefore(LocalDate.now()));

                    // Disable weekends (Saturday and Sunday)
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });

        // Optional: Set initial date to today or later
        dpStartLeaveDate.setValue(LocalDate.now());


        dpEndLeaveDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setDisable(true);
                } else {
                    // Disable previous days
                    setDisable(date.isBefore(LocalDate.now()));

                    // Disable weekends (Saturday and Sunday)
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                        setDisable(true);
                    }
                }
            }
        });

        // Optional: Set initial date to today or later
        dpEndLeaveDate.setValue(LocalDate.now());
    }


    @FXML
    void multiDayLeaveClicked() {
        lblLeaveApplicationName.setText("Multi day leave application");
        lblStartLeaveDate.setText("Start Leave Date");
        radioMultiDayLeave.setSelected(true);
        radioSingleLeave.setSelected(false);
        makeVisibleEndOfLeaveDate();
    }

    @FXML
    void singleLeaveClicked() {
        lblLeaveApplicationName.setText("Single leave application");
        lblStartLeaveDate.setText("Leave Date");
        radioSingleLeave.setSelected(true);
        radioMultiDayLeave.setSelected(false);
        makeInvisibleEndOfLeaveDate();
    }

    @FXML
    void vbLeaveTypeSelected() {

    }

    private void makeInvisibleEndOfLeaveDate() {
        endOfLeave.setVisible(false);
        endOfLeave.setManaged(false);
    }

    private void makeVisibleEndOfLeaveDate() {
        endOfLeave.setVisible(true);
        endOfLeave.setManaged(true);
    }
}
