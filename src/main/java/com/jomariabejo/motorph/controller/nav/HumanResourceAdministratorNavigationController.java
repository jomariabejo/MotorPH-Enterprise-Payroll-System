package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
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

    public void humanResourceDashboardOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Dashboard");
    }

    public void employeesOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Employees");
    }

    public void timesheetsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Timesheets");
    }

    public void leaveRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Leave Requests");
    }

    public void overtimeRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Overtime Requests");
    }


}
