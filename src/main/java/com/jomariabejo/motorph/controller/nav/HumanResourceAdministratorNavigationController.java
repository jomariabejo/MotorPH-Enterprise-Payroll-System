package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

public class HumanResourceAdministratorNavigationController {
    @Getter
    @Setter
    private MainViewController mainViewController;

    public HumanResourceAdministratorNavigationController() {

    }

    public HumanResourceAdministratorNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void humanResourceDashboardOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Human Resource / Dashboard");
    }

    public void employeesOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Human Resource / H");
    }

    public void timesheetsOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Human Resource / H");
    }

    public void leaveRequestsOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Human Resource / H");
    }

    public void overtimeRequestsOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Human Resource / H");
    }


}
