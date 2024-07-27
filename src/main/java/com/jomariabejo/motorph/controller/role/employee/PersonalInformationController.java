package com.jomariabejo.motorph.controller.role.employee;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalInformationController {

    private MoreInfoController moreInfoController;

    @FXML
    private TextField tf;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfDateOfBirth;

    @FXML
    private TextField tfEmployeeNumber;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPhoneNumber;

    public PersonalInformationController() {
    }
}
