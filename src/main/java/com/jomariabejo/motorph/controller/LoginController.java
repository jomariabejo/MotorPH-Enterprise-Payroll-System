package com.jomariabejo.motorph.controller;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.utils.StringUtils.containsAny;

public class LoginController implements Initializable {

    @FXML
    private MFXTextField textField;

    @FXML
    private MFXPasswordField passwordField;

    @FXML
    private Label validationLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleLogin(ActionEvent event) {
    }

    public void openStepperStage(ActionEvent event) {
        System.out.println("Open");
    }

    public void forgotPasswordClicked(MouseEvent mouseEvent) {
        System.out.println("I'm Clicked...");
    }
}