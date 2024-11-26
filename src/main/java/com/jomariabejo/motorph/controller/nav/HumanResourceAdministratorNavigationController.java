package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.hr.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Setter
@Getter
public class HumanResourceAdministratorNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    public HumanResourceAdministratorNavigationController() {

    }

    public void humanResourceDashboardOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Dashboard");

        loadView("/com/jomariabejo/motorph/role/human-resource/hr-dashboard.fxml", controller -> {

            if (controller instanceof HRDashboardController hrDashboardController) {
                hrDashboardController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }

    public void employeesOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Employees");

        loadView("/com/jomariabejo/motorph/role/human-resource/employee.fxml", controller -> {

            if (controller instanceof EmployeeController employeeController) {
                employeeController.setHumanResourceAdministratorNavigationController(this);
                employeeController.setup();
            }
        });
    }

    public void timesheetsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Timesheets");

        loadView("/com/jomariabejo/motorph/role/human-resource/timesheets.fxml", controller -> {

            if (controller instanceof TimesheetController timesheetController) {
                timesheetController.setHumanResourceAdministratorNavigationController(this);
                timesheetController.setup();
            }
        });
    }

    public void leaveRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Leave Requests");

        loadView("/com/jomariabejo/motorph/role/human-resource/leave-requests.fxml", controller -> {

            if (controller instanceof LeaveRequestController leaveRequestController) {
                leaveRequestController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }

    public void overtimeRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Overtime Requests");

        loadView("/com/jomariabejo/motorph/role/human-resource/overtime-requests.fxml", controller -> {

            if (controller instanceof OvertimeRequestsController overtimeRequestController) {
                overtimeRequestController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }


    public <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Load the UI (AnchorPane)
            AnchorPane pane = loader.load();
            this.mainViewController.getMainBorderPane().setCenter(pane);

            // Initialize the controller
            T controller = loader.getController();
            controllerInitializer.accept(controller);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }
}
