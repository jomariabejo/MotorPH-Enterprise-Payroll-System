package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

public class SystemAdministratorNavigationController {

    @Getter
    @Setter
    @FXML
    private MainViewController mainViewController;

    public SystemAdministratorNavigationController() {

    }

    public SystemAdministratorNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void dashboard() {
        mainViewController.rewriteLabel("/ System Administrator / Dashboard");
    }

    @FXML
    public void logs() {
        mainViewController.rewriteLabel("/ System Administrator / Logs");
    }

    @FXML
    public void permissions() {
        mainViewController.rewriteLabel("/ System Administrator / Permissions");

    }

    @FXML
    public void rolePermission() {
        mainViewController.rewriteLabel("/ System Administrator / Role-Permission");

    }

    @FXML
    public void roles() {
        mainViewController.rewriteLabel("/ System Administrator / Roles");

    }

    @FXML
    public void users() {
        mainViewController.rewriteLabel("/ System Administrator / Users");
    }

}
